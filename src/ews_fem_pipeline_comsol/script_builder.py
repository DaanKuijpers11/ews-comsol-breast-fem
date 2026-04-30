from __future__ import annotations

import json
import re
from collections import defaultdict
from pathlib import Path
import numpy as np

from ews_fem_pipeline_comsol.settings import ComsolSettings


def _comsol_safe_name(text: str) -> str:
    return text.replace("\\", "/")


def _safe_java_identifier(text: str) -> str:
    identifier = re.sub(r"[^0-9a-zA-Z_]", "_", text)
    if not identifier:
        identifier = "comsol_case"
    if identifier[0].isdigit():
        identifier = f"case_{identifier}"
    return identifier


def _chunk_list(items: list[str], size: int) -> list[list[str]]:
    return [items[idx:idx + size] for idx in range(0, len(items), size)]


def _linearize_mooney_rivlin(material: dict[str, object]) -> tuple[float, float]:
    """
    Infer a small-strain isotropic linear elastic approximation from FEBio inputs.

    Inference:
    - Shear modulus G ~= 2 * (c1 + c2)
    - Bulk modulus K from the existing FEBio input
    - Then E = 9KG / (3K + G), nu = (3K - 2G) / (2 * (3K + G))
    """
    bulk_modulus = float(material.get("bulk_modulus", 1.0))
    coef1 = float(material.get("coef1", 0.0))
    coef2 = float(material.get("coef2", 0.0))
    shear_modulus = max(2.0 * (coef1 + coef2), 1e-9)
    youngs_modulus = 9.0 * bulk_modulus * shear_modulus / (3.0 * bulk_modulus + shear_modulus)
    poissons_ratio = (3.0 * bulk_modulus - 2.0 * shear_modulus) / (2.0 * (3.0 * bulk_modulus + shear_modulus))
    poissons_ratio = min(max(poissons_ratio, -0.49), 0.499)
    return youngs_modulus, poissons_ratio


def generate_comsol_java_builder(
    *,
    case_name: str,
    output_dir: Path,
    prepare_artefacts: dict[str, str],
    comsol_settings: ComsolSettings | None = None,
) -> dict[str, str]:
    """
    Generate COMSOL Java API scaffolding from exported prepare artefacts.
    """
    output_dir.mkdir(parents=True, exist_ok=True)
    class_name = _safe_java_identifier(f"{case_name}_comsol_builder")

    build_plan_path = prepare_artefacts.get("comsol_build_plan_json", "")
    build_plan_summary = {
        "lobule_count": 0,
        "geometry": {},
        "mesh": {},
        "material": {},
        "lobules": [],
    }
    if build_plan_path and Path(build_plan_path).exists():
        plan = json.loads(Path(build_plan_path).read_text(encoding="utf-8"))
        build_plan_summary["lobule_count"] = len(plan.get("lobules", []))
        build_plan_summary["geometry"] = plan.get("geometry", {})
        build_plan_summary["mesh"] = plan.get("mesh", {})
        build_plan_summary["material"] = plan.get("material", {})
        build_plan_summary["lobules"] = plan.get("lobules", [])

    skin_material = build_plan_summary["material"].get("skin", {})
    adipose_material = build_plan_summary["material"].get("adipose", {})
    glandular_material = build_plan_summary["material"].get("glandular", {})
    shell_physics_enabled = bool(comsol_settings.enable_skin_shell_physics) if comsol_settings else False
    shell_coupling_enabled = bool(comsol_settings.enable_skin_solid_coupling_scaffold) if comsol_settings else False
    skin_shell_thickness_m = float(comsol_settings.skin_shell_thickness_m) if comsol_settings else 0.0001
    curved_chestwall_enabled = bool(comsol_settings.enable_curved_chestwall) if comsol_settings else False
    chestwall_curve_depth_m = float(comsol_settings.chestwall_curve_depth_m) if comsol_settings else 0.0007
    skin_E, skin_nu = _linearize_mooney_rivlin(skin_material)
    adipose_E, adipose_nu = _linearize_mooney_rivlin(adipose_material)
    glandular_E, glandular_nu = _linearize_mooney_rivlin(glandular_material)

    geometry = build_plan_summary["geometry"]
    radius = float(geometry.get("radius", 0.07))
    chest_thickness = float(geometry.get("thickness_chest_wall", 0.002))
    left_rel = float(geometry.get("left_relative_position_ellipse", 0.4))
    nipple_rel = float(geometry.get("right_relative_position_ellipse", 0.05))
    center_rel = float(geometry.get("center_relative_position_ellipse", 0.3))
    asymmetry = geometry.get("asymmetry", {}) or {}
    scale_y = float(asymmetry.get("scale_y", 1.0))
    scale_z = float(asymmetry.get("scale_z", 1.0))
    asym_enabled = bool(asymmetry.get("enabled", False))
    gland_hetero = build_plan_summary["material"].get("glandular", {}).get("hetero", {}) or {}
    droplet_components = max(1, int(gland_hetero.get("droplet_components", 1)))
    comsol_detail_mode = str(gland_hetero.get("comsol_geometry_detail_mode", "full")).lower()
    comsol_petal_segments_override = max(0, int(gland_hetero.get("comsol_petal_segments", 0) or 0))
    comsol_duct_beads_override = max(0, int(gland_hetero.get("comsol_duct_beads", 0) or 0))

    left_pos = left_rel * radius
    nipple_pos = nipple_rel * radius
    center_pos = center_rel * radius
    chest_curve_depth = min(max(chestwall_curve_depth_m, chest_thickness * 0.05), radius * 0.2)
    chest_curve_radius = ((radius * radius) + (chest_curve_depth * chest_curve_depth)) / max(2.0 * chest_curve_depth, 1e-9)
    chest_curve_center_y = chest_curve_radius - chest_curve_depth

    # Model the glandular core as a half-ellipsoid clipped at the chest-wall plane.
    # The clipping plane y=0 should coincide with the ellipse midline so the gland
    # remains broad at the chest wall, while the anterior reach is preserved toward
    # the nipple side.
    gland_center_y = 0.0
    gland_semiaxis_y = max(radius + nipple_pos, radius * 0.05)
    gland_semiaxis_x = max(center_pos * 1.15, radius * 0.09)
    gland_semiaxis_z = max(center_pos * (scale_z if asym_enabled else 1.0) * 1.10, radius * 0.09)
    gland_center_z = -0.15 * center_pos if asym_enabled else 0.0
    lobules: list[dict[str, object]] = list(build_plan_summary["lobules"])

    script_path = output_dir / f"{class_name}.java"
    result_mph = (output_dir / f"{case_name}_generated.mph").resolve()
    build_plan_java = _comsol_safe_name(build_plan_path)
    output_dir_java = _comsol_safe_name(str(output_dir.resolve()))
    result_mph_java = result_mph.as_posix()
    output_root = output_dir.parent
    solve_dir = output_root / "solve"
    metrics_json_path = solve_dir / f"{case_name}_metrics.json"
    selection_hints_path = output_dir / f"{case_name}_comsol_selection_hints.json"
    selection_hints = {
        "component_domain_selections": {
            "geom1_breast_union_dom": "Final union of adipose, glandular, and chest-wall domains",
            "geom1_adipose_diff_dom": "Adipose region after subtracting glandular volume from outer breast",
            "geom1_gland_clip_dom": "Glandular ellipsoid clipped to the outer breast",
            "geom1_chest_cyl_dom": "Chest-wall support domain",
        },
        "component_boundary_selections": {
            "geom1_breast_union_bnd": "Exterior boundaries of the full assembled breast/chest geometry",
            "geom1_breast_outer_bnd": "Outer breast envelope boundary; used as the current skin-shell carrier boundary",
            "geom1_adipose_diff_bnd": "Boundaries of the adipose domain",
            "geom1_gland_clip_bnd": "Boundaries of the glandular domain",
            "geom1_chest_cyl_bnd": "Boundaries of the chest-wall support domain",
        },
        "geometry_feature_tags": {
            "breast_outer": "Outer breast envelope with FEBio-style baseline and optional light thorax curvature",
            "gland_keep_anterior": "Anterior keep region used to clip the glandular ellipsoid at the chest wall",
            "gland_clip": "Glandular source volume clipped to the breast outer volume",
            "gland_lobules": "Union of COMSOL-native lobule ellipsoids derived from the exported FEBio lobule layout",
            "adipose_diff": "Adipose outer volume minus glandular volume",
            "chest_cyl": "Chest-wall support body; cylindrical in baseline mode, lightly curved in curved mode",
            "breast_union": "Final union used for meshing/physics",
        },
        "recommended_physics_targets": {
            "full_breast_domain_selection": "geom1_breast_union_dom",
            "skin_shell_boundary_selection": "geom1_breast_outer_bnd",
            "adipose_domain_selection": "geom1_adipose_diff_dom",
            "glandular_domain_selection": "geom1_gland_clip_dom",
            "chest_domain_selection": "geom1_chest_cyl_dom",
            "fixed_boundary_selection": "geom1_chest_cyl_bnd",
        },
        "skin_shell_scaffold": {
            "enabled": shell_physics_enabled,
            "solid_coupling_scaffold_enabled": shell_coupling_enabled,
            "skin_shell_thickness_m": skin_shell_thickness_m,
            "shell_boundary_selection": "geom1_breast_outer_bnd",
            "notes": [
                "The shell scaffold is generated defensively because COMSOL API identifiers can vary by version/license.",
                "The Solid-Thin Structure Connection is emitted as a scaffold on the same outer boundary selection and may need manual refinement in COMSOL.",
            ],
        },
        "chest_wall_scaffold": {
            "curved_enabled": curved_chestwall_enabled,
            "curve_depth_m": chest_curve_depth,
            "curve_radius_m": chest_curve_radius,
            "curve_center_y_m": -chest_curve_center_y,
            "notes": [
                "Curved mode replaces the flat posterior clip plane with a shallow cylindrical arc in the yz side-view, so the chest wall reads as a ')' style curve.",
                "The chest-wall support domain reuses that same interface so the breast and chest wall remain conformal.",
            ],
        },
        "linearized_material_inference": {
            "note": "Young's modulus and Poisson's ratio are inferred from FEBio Mooney-Rivlin inputs for an initial small-strain COMSOL model.",
            "skin": {"youngs_modulus_pa": skin_E, "poissons_ratio": skin_nu},
            "adipose": {"youngs_modulus_pa": adipose_E, "poissons_ratio": adipose_nu},
            "glandular": {"youngs_modulus_pa": glandular_E, "poissons_ratio": glandular_nu},
        },
        "planned_metrics_export": {
            "metrics_json": str(metrics_json_path.resolve()),
            "metrics": [
                "breast_volume",
                "glandular_volume",
                "adipose_volume",
                "max_displacement_breast",
                "avg_displacement_breast",
                "max_von_mises_breast",
                "max_von_mises_glandular",
            ],
        },
    }
    selection_hints_path.write_text(json.dumps(selection_hints, indent=2), encoding="utf-8")
    selection_hints_java = selection_hints_path.as_posix()

    nipple = gland_hetero.get("nipple", [0.0, 0.068, 0.0])
    nipple_x = float(nipple[0]) if isinstance(nipple, list) and len(nipple) >= 1 else 0.0
    nipple_y = float(nipple[1]) if isinstance(nipple, list) and len(nipple) >= 2 else radius
    nipple_z = float(nipple[2]) if isinstance(nipple, list) and len(nipple) >= 3 else 0.0
    use_template_lobes = any(str(lob.get("template_kind", "")) == "duct_lobe" for lob in lobules)

    lobule_feature_tags: list[str] = []
    lobule_specs: list[dict[str, float | str | int]] = []
    lobule_java_blocks: list[str] = []
    for idx, lobule in enumerate(lobules, start=1):
        center = lobule.get("center", [0.0, 0.0, 0.0])
        if not isinstance(center, list) or len(center) != 3:
            continue
        cx, cy, cz = (float(center[0]), float(center[1]), float(center[2]))
        component_role = str(lobule.get("component_role", "bulb"))
        component_index = int(lobule.get("component_index", 0))
        component_count = int(lobule.get("component_count", 1))
        lobe_id = int(lobule.get("lobe_id", idx))

        base_sx = float(lobule.get("width_x", lobule.get("width", 0.002)))
        base_sy = float(lobule.get("width_y", lobule.get("width", 0.002)))
        base_sz = float(lobule.get("width_z", lobule.get("width", 0.002)))

        if component_role == "duct":
            sx = max(base_sx * 0.78, radius * 0.007)
            sy = max(base_sy * 1.45, radius * 0.014)
            sz = max(base_sz * 0.78, radius * 0.007)
        else:
            sx = max(base_sx * 1.12, radius * 0.010)
            sy = max(base_sy * 1.08, radius * 0.012)
            sz = max(base_sz * 1.12, radius * 0.010)

        tag = f"lobule_{idx:02d}"
        lobule_specs.append(
            {
                "tag": tag,
                "cx": cx,
                "cy": cy,
                "cz": cz,
                "sx": sx,
                "sy": sy,
                "sz": sz,
                "lobe_id": lobe_id,
                "component_index": component_index,
                "component_count": component_count,
                "component_role": component_role,
                "ring_name": str(lobule.get("ring_name", "inner")),
            }
        )
        if not use_template_lobes:
            lobule_feature_tags.append(tag)
            lobule_java_blocks.append(
                f"""
    model.component("comp1").geom("geom1").create("{tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{tag}").set("semiaxes", "{sx:.8f} {sz:.8f} {sy:.8f}");
    model.component("comp1").geom("geom1").feature("{tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{tag}").set("pos", "{cx:.8f} {cy:.8f} {cz:.8f}");
    model.component("comp1").geom("geom1").feature("{tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{tag}").set("selresultshow", "all");
"""
            )
    lobe_groups: dict[int, list[dict[str, float | str | int]]] = defaultdict(list)
    for spec in lobule_specs:
        lobe_groups[int(spec["lobe_id"])].append(spec)

    anatomical_lobe_tags: list[str] = []
    anatomical_lobe_java_blocks: list[str] = []
    lobe_refinement_java_blocks: list[str] = []
    shared_duct_tags: list[str] = []
    shared_duct_java_blocks: list[str] = []
    if lobe_groups and use_template_lobes:
        fast_detail_mode = comsol_detail_mode == "fast"
        hub_y = nipple_y - radius * 0.17
        hub_tag = "duct_hub_core"
        hub_cap_tag = "duct_hub_cap"
        trunk_tag = "duct_trunk_main"
        shared_duct_tags.extend([hub_tag, hub_cap_tag, trunk_tag])
        shared_duct_java_blocks.extend(
            [
                f"""
    model.component("comp1").geom("geom1").create("{hub_tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{hub_tag}").set("semiaxes", "{radius * 0.0205:.8f} {radius * 0.0205:.8f} {radius * 0.0300:.8f}");
    model.component("comp1").geom("geom1").feature("{hub_tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{hub_tag}").set("pos", "0 {hub_y:.8f} 0");
    model.component("comp1").geom("geom1").feature("{hub_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{hub_tag}").set("selresultshow", "all");
""",
                f"""
    model.component("comp1").geom("geom1").create("{hub_cap_tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{hub_cap_tag}").set("semiaxes", "{radius * 0.0280:.8f} {radius * 0.0280:.8f} {radius * 0.0160:.8f}");
    model.component("comp1").geom("geom1").feature("{hub_cap_tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{hub_cap_tag}").set("pos", "0 {hub_y - radius * 0.0130:.8f} 0");
    model.component("comp1").geom("geom1").feature("{hub_cap_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{hub_cap_tag}").set("selresultshow", "all");
""",
                f"""
    model.component("comp1").geom("geom1").create("{trunk_tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{trunk_tag}").set("semiaxes", "{radius * 0.0120:.8f} {radius * 0.0120:.8f} {radius * 0.0440:.8f}");
    model.component("comp1").geom("geom1").feature("{trunk_tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{trunk_tag}").set("pos", "0 {hub_y - radius * 0.0300:.8f} 0");
    model.component("comp1").geom("geom1").feature("{trunk_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{trunk_tag}").set("selresultshow", "all");
""",
            ]
        )
        for lobe_id in sorted(lobe_groups):
            bulb = lobe_groups[lobe_id][0]
            bx = float(bulb["cx"])
            by = float(bulb["cy"])
            bz = float(bulb["cz"])
            bsx = float(bulb["sx"])
            bsy = float(bulb["sy"])
            bsz = float(bulb["sz"])
            ring_name = str(bulb.get("ring_name", "inner"))

            original = next((lob for lob in lobules if int(lob.get("lobe_id", -1)) == lobe_id), None)
            if original is None:
                continue
            bulb_sidecar = original.get("bulb_sidecar", [bx, by, bz])
            duct_mid = original.get("duct_mid", [bx, by, bz])
            duct_tip = original.get("duct_tip", [bx, by, bz])
            if not isinstance(bulb_sidecar, list) or len(bulb_sidecar) != 3:
                bulb_sidecar = [bx, by, bz]
            if not isinstance(duct_mid, list) or len(duct_mid) != 3:
                duct_mid = [bx, by, bz]
            if not isinstance(duct_tip, list) or len(duct_tip) != 3:
                duct_tip = [bx, by, bz]

            radial_x = bx
            radial_z = bz
            radial_norm = max((radial_x ** 2 + radial_z ** 2) ** 0.5, 1e-9)
            radial_x /= radial_norm
            radial_z /= radial_norm
            tangent_x = -radial_z
            tangent_z = radial_x

            petal_segment_tags: list[str] = []
            petal_segment_java_blocks: list[str] = []
            segment_count = (
                comsol_petal_segments_override
                if comsol_petal_segments_override > 0
                else (4 if fast_detail_mode else 8)
            )
            petal_span = 2.45 if ring_name == "outer" else 2.05
            petal_curve = 0.42 if ring_name == "outer" else 0.32
            petal_twist = 0.14 if ring_name == "outer" else 0.10
            for seg_idx in range(segment_count):
                t = seg_idx / max(segment_count - 1, 1)
                seg_tag = f"lobe_{lobe_id:02d}_petal_seg_{seg_idx + 1:02d}"
                petal_segment_tags.append(seg_tag)
                radial_shift = (-0.30 + petal_span * t) * bsx
                tangent_shift = (petal_curve * np.sin(np.pi * t) - petal_twist * t) * bsx
                seg_cx = bx + radial_x * radial_shift + tangent_x * tangent_shift
                seg_cy = by - (0.08 + 0.70 * t) * bsy
                seg_cz = bz + radial_z * radial_shift + tangent_z * tangent_shift
                seg_sx = max(bsx * (0.88 + 0.34 * np.sin(np.pi * t)), radius * 0.0068)
                seg_sy = max(bsy * (0.68 - 0.16 * t), radius * 0.0050)
                seg_sz = max(bsz * (0.82 + 0.26 * np.sin(np.pi * t)), radius * 0.0062)
                petal_segment_java_blocks.append(
                    f"""
    model.component("comp1").geom("geom1").create("{seg_tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{seg_tag}").set("semiaxes", "{seg_sx:.8f} {seg_sz:.8f} {seg_sy:.8f}");
    model.component("comp1").geom("geom1").feature("{seg_tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{seg_tag}").set("pos", "{seg_cx:.8f} {seg_cy:.8f} {seg_cz:.8f}");
    model.component("comp1").geom("geom1").feature("{seg_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{seg_tag}").set("selresultshow", "all");
"""
                )

            petal_wing_tag = f"lobe_{lobe_id:02d}_petal_wing"
            wing_cx = float(bulb_sidecar[0]) + tangent_x * (0.12 * bsx)
            wing_cy = float(bulb_sidecar[1]) - 0.24 * bsy
            wing_cz = float(bulb_sidecar[2]) + tangent_z * (0.12 * bsz)
            wing_sx = max(bsx * (1.16 if ring_name == "outer" else 1.05), radius * 0.0060)
            wing_sy = max(bsy * 0.48, radius * 0.0044)
            wing_sz = max(bsz * (1.12 if ring_name == "outer" else 1.02), radius * 0.0062)

            posterior_cap_tag = f"lobe_{lobe_id:02d}_posterior_cap"
            cap_cx = bx - radial_x * (0.60 * bsx)
            cap_cy = by - 0.66 * bsy
            cap_cz = bz - radial_z * (0.60 * bsz)
            cap_sx = max(bsx * 1.04, radius * 0.0070)
            cap_sy = max(bsy * 0.62, radius * 0.0050)
            cap_sz = max(bsz * 0.96, radius * 0.0062)

            duct_bead_tags: list[str] = []
            duct_bead_java_blocks: list[str] = []
            duct_start_x = bx - radial_x * (0.30 * bsx)
            duct_start_y = by + 0.02 * bsy
            duct_start_z = bz - radial_z * (0.30 * bsz)
            control_x = float(duct_mid[0])
            control_y = float(duct_mid[1])
            control_z = float(duct_mid[2])
            end_x = float(duct_tip[0])
            end_y = float(duct_tip[1])
            end_z = float(duct_tip[2])
            bead_count = (
                comsol_duct_beads_override
                if comsol_duct_beads_override > 0
                else (6 if fast_detail_mode else 18)
            )
            for bead_idx in range(1, bead_count + 1):
                t = bead_idx / bead_count
                omt = 1.0 - t
                px = omt * omt * duct_start_x + 2.0 * omt * t * control_x + t * t * end_x
                py = omt * omt * duct_start_y + 2.0 * omt * t * control_y + t * t * end_y
                pz = omt * omt * duct_start_z + 2.0 * omt * t * control_z + t * t * end_z
                bead_tag = f"lobe_{lobe_id:02d}_duct_bead_{bead_idx:02d}"
                duct_bead_tags.append(bead_tag)
                bead_rxy = max(radius * (0.0105 + 0.0055 * omt), radius * 0.0068)
                bead_ry = max(radius * (0.0135 + 0.0065 * omt), radius * 0.0084)
                duct_bead_java_blocks.append(
                    f"""
    model.component("comp1").geom("geom1").create("{bead_tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{bead_tag}").set("semiaxes", "{bead_rxy:.8f} {bead_rxy:.8f} {bead_ry:.8f}");
    model.component("comp1").geom("geom1").feature("{bead_tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{bead_tag}").set("pos", "{px:.8f} {py:.8f} {pz:.8f}");
    model.component("comp1").geom("geom1").feature("{bead_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{bead_tag}").set("selresultshow", "all");
"""
                )

            lobe_refinement_java_blocks.append(
                f"""
{"".join(petal_segment_java_blocks)}
    model.component("comp1").geom("geom1").create("{petal_wing_tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{petal_wing_tag}").set("semiaxes", "{wing_sx:.8f} {wing_sz:.8f} {wing_sy:.8f}");
    model.component("comp1").geom("geom1").feature("{petal_wing_tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{petal_wing_tag}").set("pos", "{wing_cx:.8f} {wing_cy:.8f} {wing_cz:.8f}");
    model.component("comp1").geom("geom1").feature("{petal_wing_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{petal_wing_tag}").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("{posterior_cap_tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{posterior_cap_tag}").set("semiaxes", "{cap_sx:.8f} {cap_sz:.8f} {cap_sy:.8f}");
    model.component("comp1").geom("geom1").feature("{posterior_cap_tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{posterior_cap_tag}").set("pos", "{cap_cx:.8f} {cap_cy:.8f} {cap_cz:.8f}");
    model.component("comp1").geom("geom1").feature("{posterior_cap_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{posterior_cap_tag}").set("selresultshow", "all");
{"".join(duct_bead_java_blocks)}
"""
            )

            lobe_tag = f"anatomical_lobe_{lobe_id:02d}"
            anatomical_lobe_tags.append(lobe_tag)
            lobe_input_tags = [*petal_segment_tags, petal_wing_tag, posterior_cap_tag, *duct_bead_tags]
            lobe_input_args = ", ".join(f'"{tag}"' for tag in lobe_input_tags)
            anatomical_lobe_java_blocks.append(
                f"""
    model.component("comp1").geom("geom1").create("{lobe_tag}", "Union");
    model.component("comp1").geom("geom1").feature("{lobe_tag}").selection("input").set({lobe_input_args});
    model.component("comp1").geom("geom1").feature("{lobe_tag}").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("{lobe_tag}").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("{lobe_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{lobe_tag}").set("selresultshow", "all");
"""
            )
    elif lobe_groups:
        for lobe_id in sorted(lobe_groups):
            ordered_group = sorted(lobe_groups[lobe_id], key=lambda item: int(item["component_index"]))
            comp_tags = [str(spec["tag"]) for spec in ordered_group]
            bulb = next((spec for spec in ordered_group if str(spec["component_role"]) == "bulb"), ordered_group[0])
            duct = next((spec for spec in ordered_group if str(spec["component_role"]) == "duct"), ordered_group[-1])
            ring_name = str(bulb.get("ring_name", "inner"))

            bx = float(bulb["cx"])
            by = float(bulb["cy"])
            bz = float(bulb["cz"])
            bsx = float(bulb["sx"])
            bsy = float(bulb["sy"])
            bsz = float(bulb["sz"])
            dx = float(duct["cx"])
            dy = float(duct["cy"])
            dz = float(duct["cz"])
            dsx = float(duct["sx"])
            dsy = float(duct["sy"])
            dsz = float(duct["sz"])

            toward_x = nipple_x - bx
            toward_y = nipple_y - by
            toward_z = nipple_z - bz
            toward_norm = max((toward_x ** 2 + toward_y ** 2 + toward_z ** 2) ** 0.5, 1e-9)
            toward_x /= toward_norm
            toward_y /= toward_norm
            toward_z /= toward_norm

            tangent_x = -bz
            tangent_z = bx
            tangent_norm = max((tangent_x ** 2 + tangent_z ** 2) ** 0.5, 1e-9)
            tangent_x /= tangent_norm
            tangent_z /= tangent_norm

            radial_x = bx
            radial_z = bz
            radial_norm = max((radial_x ** 2 + radial_z ** 2) ** 0.5, 1e-9)
            radial_x /= radial_norm
            radial_z /= radial_norm

            bulb_sidecar_tag = f"lobe_{lobe_id:02d}_bulb_sidecar"
            bulb_sidecar_cx = bx + radial_x * (0.36 * bsx) - tangent_x * (0.18 * bsx)
            bulb_sidecar_cy = by - 0.16 * bsy
            bulb_sidecar_cz = bz + radial_z * (0.36 * bsz) + tangent_z * (0.18 * bsz)
            bulb_sidecar_sx = max(bsx * 0.62, radius * 0.006)
            bulb_sidecar_sy = max(bsy * 0.72, radius * 0.008)
            bulb_sidecar_sz = max(bsz * 0.58, radius * 0.006)

            duct_tip_tag = f"lobe_{lobe_id:02d}_duct_tip"
            curvature_scale = 0.45 if ring_name == "outer" else 0.20
            duct_tip_cx = dx + toward_x * (0.42 * dsy) + tangent_x * (curvature_scale * dsx)
            duct_tip_cy = dy + toward_y * (0.48 * dsy)
            duct_tip_cz = dz + toward_z * (0.42 * dsy) + tangent_z * (curvature_scale * dsz)
            duct_tip_sx = max(dsx * 0.82, radius * 0.005)
            duct_tip_sy = max(dsy * 0.96, radius * 0.010)
            duct_tip_sz = max(dsz * 0.82, radius * 0.005)

            lobe_refinement_java_blocks.append(
                f"""
    model.component("comp1").geom("geom1").create("{bulb_sidecar_tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{bulb_sidecar_tag}").set("semiaxes", "{bulb_sidecar_sx:.8f} {bulb_sidecar_sz:.8f} {bulb_sidecar_sy:.8f}");
    model.component("comp1").geom("geom1").feature("{bulb_sidecar_tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{bulb_sidecar_tag}").set("pos", "{bulb_sidecar_cx:.8f} {bulb_sidecar_cy:.8f} {bulb_sidecar_cz:.8f}");
    model.component("comp1").geom("geom1").feature("{bulb_sidecar_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{bulb_sidecar_tag}").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("{duct_tip_tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{duct_tip_tag}").set("semiaxes", "{duct_tip_sx:.8f} {duct_tip_sz:.8f} {duct_tip_sy:.8f}");
    model.component("comp1").geom("geom1").feature("{duct_tip_tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{duct_tip_tag}").set("pos", "{duct_tip_cx:.8f} {duct_tip_cy:.8f} {duct_tip_cz:.8f}");
    model.component("comp1").geom("geom1").feature("{duct_tip_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{duct_tip_tag}").set("selresultshow", "all");
"""
            )

            lobe_union_inputs = ", ".join(f'"{tag}"' for tag in [*comp_tags, bulb_sidecar_tag, duct_tip_tag])
            lobe_tag = f"anatomical_lobe_{lobe_id:02d}"
            anatomical_lobe_tags.append(lobe_tag)
            anatomical_lobe_java_blocks.append(
                f"""
    model.component("comp1").geom("geom1").create("{lobe_tag}", "Union");
    model.component("comp1").geom("geom1").feature("{lobe_tag}").selection("input").set({lobe_union_inputs});
    model.component("comp1").geom("geom1").feature("{lobe_tag}").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("{lobe_tag}").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("{lobe_tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{lobe_tag}").set("selresultshow", "all");
"""
            )

    union_source_tags = [*(anatomical_lobe_tags or lobule_feature_tags), *shared_duct_tags]
    lobule_union_inputs = ", ".join(f'"{tag}"' for tag in union_source_tags)
    use_lobules = bool(union_source_tags)
    lobule_helper_methods: list[str] = []
    lobule_helper_invocations: list[str] = []

    if use_template_lobes and anatomical_lobe_java_blocks:
        if shared_duct_java_blocks:
            shared_method_name = "buildSharedDuctHub"
            lobule_helper_invocations.append(f"    {shared_method_name}(model);\n")
            lobule_helper_methods.append(
                f"""
  private static void {shared_method_name}(Model model) {{
{"".join(shared_duct_java_blocks)}
  }}
"""
            )
        for method_index, (refinement_block, lobe_union_block) in enumerate(
            zip(lobe_refinement_java_blocks, anatomical_lobe_java_blocks),
            start=1,
        ):
            method_name = f"buildAnatomicalLobe{method_index:02d}"
            lobule_helper_invocations.append(f"    {method_name}(model);\n")
            lobule_helper_methods.append(
                f"""
  private static void {method_name}(Model model) {{
{refinement_block}
{lobe_union_block}
  }}
"""
            )
    else:
        primitive_blocks = [*lobule_java_blocks, *shared_duct_java_blocks]
        for chunk_index, chunk in enumerate(_chunk_list(primitive_blocks, 40), start=1):
            method_name = f"buildLobulePrimitiveChunk{chunk_index:02d}"
            lobule_helper_invocations.append(f"    {method_name}(model);\n")
            lobule_helper_methods.append(
                f"""
  private static void {method_name}(Model model) {{
{"".join(chunk)}
  }}
"""
            )
        for chunk_index, chunk in enumerate(_chunk_list(anatomical_lobe_java_blocks, 20), start=1):
            method_name = f"buildLobuleUnionChunk{chunk_index:02d}"
            lobule_helper_invocations.append(f"    {method_name}(model);\n")
            lobule_helper_methods.append(
                f"""
  private static void {method_name}(Model model) {{
{"".join(chunk)}
  }}
"""
            )

    lobule_helper_methods_java = "".join(lobule_helper_methods)
    lobule_helper_invocations_java = "".join(lobule_helper_invocations)
    lobule_builder_method_java = (
        f"""
  private static String[] buildGlandLobules(Model model) {{
{lobule_helper_invocations_java}
    model.component("comp1").geom("geom1").create("gland_lobules", "Union");
    model.component("comp1").geom("geom1").feature("gland_lobules").selection("input").set({lobule_union_inputs});
    model.component("comp1").geom("geom1").feature("gland_lobules").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_lobules");
    return model.component("comp1").geom("geom1").feature("gland_lobules").objectNames();
  }}
"""
        if use_lobules
        else ""
    )
    gland_source_tag = "gland_lobules" if use_lobules else "gland_seed"
    gland_source_objects_var = "glandLobuleObjs" if use_lobules else "glandSeedObjs"
    lobule_union_java = (
        f"""
    String[] glandLobuleObjs = buildGlandLobules(model);
"""
        if use_lobules
        else ""
    )

    shell_physics_java = ""
    if shell_physics_enabled:
        shell_physics_java = f"""
    StringBuilder shellScaffoldNotes = new StringBuilder();
    String shellPhysicsTag = tryCreatePhysics(model, "shell1", new String[] {{ "Shell", "shell" }}, "geom1", shellScaffoldNotes);
    if (shellPhysicsTag != null) {{
      model.component("comp1").physics(shellPhysicsTag).selection().named("geom1_breast_outer_bnd");
      tryConfigureShellThickness(model, shellPhysicsTag, "skin_shell_thickness", shellScaffoldNotes);
    }}
    if ({str(shell_coupling_enabled).lower()} && shellPhysicsTag != null) {{
      tryCreateSolidThinStructureConnection(
        model,
        "sthin1",
        new String[] {{ "SolidThinStructureConnection", "SolidShellConnection", "solidthin", "sthin" }},
        "geom1",
        "geom1_breast_outer_bnd",
        "solid",
        shellPhysicsTag,
        shellScaffoldNotes
      );
    }}
    model.param().set("skin_shell_scaffold_notes", shellScaffoldNotes.toString());
"""

    java_source = f"""import com.comsol.model.*;
import com.comsol.model.util.*;

public class {class_name} {{
  public static Model run() {{
    ModelUtil.initStandalone(true);
    Model model = ModelUtil.create("Model");
    model.label("{result_mph.name}");
    model.modelPath("{output_dir_java}");

    model.param().set("breast_radius", "{build_plan_summary["geometry"].get("radius", 0.07)}[m]");
    model.param().set("chest_thickness", "{build_plan_summary["geometry"].get("thickness_chest_wall", 0.002)}[m]");
    model.param().set("skin_shell_thickness", "{skin_shell_thickness_m:.10f}[m]");
    model.param().set("chest_curve_depth", "{chest_curve_depth:.10f}[m]");
    model.param().set("chest_curve_radius", "{chest_curve_radius:.10f}[m]");
    model.param().set("chest_curve_center_y", "{-chest_curve_center_y:.10f}[m]");
    model.param().set("mesh_density_hint", "{build_plan_summary["mesh"].get("density", 140.0)}");
    model.param().set("skin_density", "{build_plan_summary["material"].get("skin", {}).get("density", 1100.0)}[kg/m^3]");
    model.param().set("adipose_density", "{build_plan_summary["material"].get("adipose", {}).get("density", 911.0)}[kg/m^3]");
    model.param().set("glandular_density", "{build_plan_summary["material"].get("glandular", {}).get("density", 911.0)}[kg/m^3]");
    model.param().set("g_acc", "9.81[m/s^2]");
    model.param().set("skin_E", "{skin_E:.12f}[Pa]");
    model.param().set("skin_nu", "{skin_nu:.12f}");
    model.param().set("adipose_E", "{adipose_E:.12f}[Pa]");
    model.param().set("adipose_nu", "{adipose_nu:.12f}");
    model.param().set("glandular_E", "{glandular_E:.12f}[Pa]");
    model.param().set("glandular_nu", "{glandular_nu:.12f}");

    // Base component/geometry
    model.component().create("comp1", true);
    model.component("comp1").geom().create("geom1", 3);
    model.component("comp1").mesh().create("mesh1");
    model.component("comp1").geom("geom1").lengthUnit("m");

    // Minimal study scaffold so COMSOL batch can target std1.
    model.study().create("std1");
    model.study("std1").create("stat", "Stationary");

    // Geometry scaffold matching the current FEBio-style baseline more closely.
    model.component("comp1").geom("geom1").create("sph_outer", "Sphere");
    model.component("comp1").geom("geom1").feature("sph_outer").set("r", "breast_radius");
    model.component("comp1").geom("geom1").feature("sph_outer").set("pos", "0 0 0");
    model.component("comp1").geom("geom1").feature("sph_outer").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("sph_outer").set("selresultshow", "all");

    String[] breastBaseObjs;
    if ({str(curved_chestwall_enabled).lower()}) {{
      model.component("comp1").geom("geom1").create("thorax_keep_blk", "Block");
      model.component("comp1").geom("geom1").feature("thorax_keep_blk").set("size", "2*breast_radius 2*breast_radius 2*breast_radius");
      model.component("comp1").geom("geom1").feature("thorax_keep_blk").set("pos", "-breast_radius 0 -breast_radius");
      model.component("comp1").geom("geom1").feature("thorax_keep_blk").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("thorax_keep_blk").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("thorax_keep", "Cylinder");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("axistype", "x");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("r", "chest_curve_radius");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("h", "2*breast_radius");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("pos", "-breast_radius chest_curve_center_y 0");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("thorax_keep_reg", "Difference");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").selection("input").set("thorax_keep_blk");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").selection("input2").set("thorax_keep");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("thorax_keep_reg");

      model.component("comp1").geom("geom1").create("breast_base", "Intersection");
      model.component("comp1").geom("geom1").feature("breast_base").selection("input").set("sph_outer", "thorax_keep_reg");
      model.component("comp1").geom("geom1").feature("breast_base").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("breast_base").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("breast_base").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("breast_base").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("breast_base");
      breastBaseObjs = model.component("comp1").geom("geom1").feature("breast_base").objectNames();
    }} else {{
      model.component("comp1").geom("geom1").create("blk_half", "Block");
      model.component("comp1").geom("geom1").feature("blk_half").set("size", "2*breast_radius breast_radius 2*breast_radius");
      model.component("comp1").geom("geom1").feature("blk_half").set("pos", "-breast_radius 0 -breast_radius");
      model.component("comp1").geom("geom1").feature("blk_half").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("blk_half").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("breast_base", "Intersection");
      model.component("comp1").geom("geom1").feature("breast_base").selection("input").set("sph_outer", "blk_half");
      model.component("comp1").geom("geom1").feature("breast_base").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("breast_base").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("breast_base").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("breast_base").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("breast_base");
      breastBaseObjs = model.component("comp1").geom("geom1").feature("breast_base").objectNames();
    }}

    model.component("comp1").geom("geom1").create("breast_outer", "Union");
    model.component("comp1").geom("geom1").feature("breast_outer").selection("input").set(breastBaseObjs);
    model.component("comp1").geom("geom1").feature("breast_outer").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("breast_outer");
    String[] breastOuterObjs = model.component("comp1").geom("geom1").feature("breast_outer").objectNames();

    String[] chestObjs;
    if ({str(curved_chestwall_enabled).lower()}) {{
      model.component("comp1").geom("geom1").create("thorax_outer", "Cylinder");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("axistype", "x");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("r", "chest_curve_radius+chest_thickness");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("h", "2*breast_radius");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("pos", "-breast_radius chest_curve_center_y 0");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("chest_trim_blk", "Block");
      model.component("comp1").geom("geom1").feature("chest_trim_blk").set("size", "2*breast_radius chest_thickness+chest_curve_depth breast_radius*2");
      model.component("comp1").geom("geom1").feature("chest_trim_blk").set("pos", "-breast_radius -chest_thickness -breast_radius");
      model.component("comp1").geom("geom1").feature("chest_trim_blk").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("chest_trim_blk").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("chest_band", "Difference");
      model.component("comp1").geom("geom1").feature("chest_band").selection("input").set("thorax_outer");
      model.component("comp1").geom("geom1").feature("chest_band").selection("input2").set("thorax_keep");
      model.component("comp1").geom("geom1").feature("chest_band").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("chest_band").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("chest_band").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("chest_band").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("chest_band");

      model.component("comp1").geom("geom1").create("chest_cyl", "Intersection");
      model.component("comp1").geom("geom1").feature("chest_cyl").selection("input").set("chest_band", "chest_trim_blk");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("chest_cyl");
      chestObjs = model.component("comp1").geom("geom1").feature("chest_cyl").objectNames();
    }} else {{
      model.component("comp1").geom("geom1").create("chest_cyl", "Cylinder");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("axistype", "y");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("r", "breast_radius");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("h", "chest_thickness");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("pos", "0 -chest_thickness 0");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("chest_cyl");
      chestObjs = model.component("comp1").geom("geom1").feature("chest_cyl").objectNames();
    }}

    if ({str(curved_chestwall_enabled).lower()}) {{
      model.component("comp1").geom("geom1").create("gland_keep_blk", "Block");
      model.component("comp1").geom("geom1").feature("gland_keep_blk").set("size", "2*breast_radius 2*breast_radius 2*breast_radius");
      model.component("comp1").geom("geom1").feature("gland_keep_blk").set("pos", "-breast_radius 0 -breast_radius");
      model.component("comp1").geom("geom1").feature("gland_keep_blk").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("gland_keep_blk").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("gland_keep_anterior", "Difference");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").selection("input").set("gland_keep_blk");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").selection("input2").set("thorax_keep");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresultshow", "all");
    }} else {{
      model.component("comp1").geom("geom1").create("gland_keep_anterior", "Block");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("size", "2*breast_radius 2*breast_radius 2*breast_radius");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("pos", "-breast_radius 0 -breast_radius");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresultshow", "all");
    }}
    model.component("comp1").geom("geom1").run("gland_keep_anterior");
    String[] glandKeepAnteriorObjs = model.component("comp1").geom("geom1").feature("gland_keep_anterior").objectNames();

    model.component("comp1").geom("geom1").create("gland_seed", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("gland_seed").set("semiaxes", "{gland_semiaxis_x:.8f} {gland_semiaxis_z:.8f} {gland_semiaxis_y:.8f}");
    model.component("comp1").geom("geom1").feature("gland_seed").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("gland_seed").set("pos", "0 {gland_center_y:.8f} {gland_center_z:.8f}");
    model.component("comp1").geom("geom1").feature("gland_seed").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_seed").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_seed");
    String[] glandSeedObjs = model.component("comp1").geom("geom1").feature("gland_seed").objectNames();
{lobule_union_java}

    model.component("comp1").geom("geom1").create("gland_clip", "Intersection");
    model.component("comp1").geom("geom1").feature("gland_clip").selection("input").set({gland_source_objects_var}[0], breastOuterObjs[0], glandKeepAnteriorObjs[0]);
    model.component("comp1").geom("geom1").feature("gland_clip").set("keep", "on");
    model.component("comp1").geom("geom1").feature("gland_clip").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("gland_clip").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("gland_clip").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_clip").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_clip");
    String[] glandClipObjs = model.component("comp1").geom("geom1").feature("gland_clip").objectNames();

    model.component("comp1").geom("geom1").create("adipose_diff", "Difference");
    model.component("comp1").geom("geom1").feature("adipose_diff").selection("input").set(breastOuterObjs);
    model.component("comp1").geom("geom1").feature("adipose_diff").selection("input2").set(glandClipObjs);
    model.component("comp1").geom("geom1").feature("adipose_diff").set("keepsubtract", "on");
    model.component("comp1").geom("geom1").feature("adipose_diff").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("adipose_diff").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("adipose_diff").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("adipose_diff").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("adipose_diff");
    String[] adiposeObjs = model.component("comp1").geom("geom1").feature("adipose_diff").objectNames();

    model.component("comp1").geom("geom1").create("breast_union", "Union");
    String[] unionInput = new String[] {{ adiposeObjs[0], glandClipObjs[0], chestObjs[0] }};
    model.component("comp1").geom("geom1").feature("breast_union").selection("input").set(unionInput);
    model.component("comp1").geom("geom1").feature("breast_union").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("breast_union").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("breast_union").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("breast_union").set("selresultshow", "all");

    // Auto-generated pointers:
    // - Build plan JSON: {build_plan_java}
    // - Selection hints JSON: {selection_hints_java}
    // - Lobule primitives in plan: {build_plan_summary["lobule_count"]}
    // - Anatomical lobe groups interpreted in COMSOL: {len(anatomical_lobe_tags) if anatomical_lobe_tags else build_plan_summary["lobule_count"]}
    //
    // Source geometry summary:
    // - radius: {build_plan_summary["geometry"].get("radius", "n/a")}
    // - chest-wall thickness: {build_plan_summary["geometry"].get("thickness_chest_wall", "n/a")}
    // - asymmetry enabled: {build_plan_summary["geometry"].get("asymmetry", {}).get("enabled", False)}
    //
    // Source mesh summary:
    // - density: {build_plan_summary["mesh"].get("density", "n/a")}
    // - order: {build_plan_summary["mesh"].get("order", "n/a")}
    //
    // Source material summary:
    // - skin density: {build_plan_summary["material"].get("skin", {}).get("density", "n/a")}
    // - adipose density: {build_plan_summary["material"].get("adipose", {}).get("density", "n/a")}
    // - glandular density: {build_plan_summary["material"].get("glandular", {}).get("density", "n/a")}
    //
    // Physics scaffold:
    model.component("comp1").material().create("mat_chest", "Common");
    model.component("comp1").material("mat_chest").label("ChestWall");
    model.component("comp1").material("mat_chest").selection().named("geom1_chest_cyl_dom");
    model.component("comp1").material("mat_chest").propertyGroup("def").set("density", new String[] {{ "skin_density" }});
    model.component("comp1").material("mat_chest").propertyGroup("def").set("youngsmodulus", new String[] {{ "skin_E" }});
    model.component("comp1").material("mat_chest").propertyGroup("def").set("poissonsratio", new String[] {{ "skin_nu" }});

    model.component("comp1").material().create("mat_skin_shell", "Common");
    model.component("comp1").material("mat_skin_shell").label("SkinShellScaffold");
    model.component("comp1").material("mat_skin_shell").selection().named("geom1_breast_outer_bnd");
    model.component("comp1").material("mat_skin_shell").propertyGroup("def").set("density", new String[] {{ "skin_density" }});
    model.component("comp1").material("mat_skin_shell").propertyGroup("def").set("youngsmodulus", new String[] {{ "skin_E" }});
    model.component("comp1").material("mat_skin_shell").propertyGroup("def").set("poissonsratio", new String[] {{ "skin_nu" }});

    model.component("comp1").material().create("mat_adipose", "Common");
    model.component("comp1").material("mat_adipose").label("Adipose");
    model.component("comp1").material("mat_adipose").selection().named("geom1_adipose_diff_dom");
    model.component("comp1").material("mat_adipose").propertyGroup("def").set("density", new String[] {{ "adipose_density" }});
    model.component("comp1").material("mat_adipose").propertyGroup("def").set("youngsmodulus", new String[] {{ "adipose_E" }});
    model.component("comp1").material("mat_adipose").propertyGroup("def").set("poissonsratio", new String[] {{ "adipose_nu" }});

    model.component("comp1").material().create("mat_glandular", "Common");
    model.component("comp1").material("mat_glandular").label("Glandular");
    model.component("comp1").material("mat_glandular").selection().named("geom1_gland_clip_dom");
    model.component("comp1").material("mat_glandular").propertyGroup("def").set("density", new String[] {{ "glandular_density" }});
    model.component("comp1").material("mat_glandular").propertyGroup("def").set("youngsmodulus", new String[] {{ "glandular_E" }});
    model.component("comp1").material("mat_glandular").propertyGroup("def").set("poissonsratio", new String[] {{ "glandular_nu" }});

    model.component("comp1").physics().create("solid", "SolidMechanics", "geom1");
    model.component("comp1").physics("solid").selection().named("geom1_breast_union_dom");
    model.component("comp1").physics("solid").create("fix1", "Fixed", 2);
    model.component("comp1").physics("solid").feature("fix1").selection().named("geom1_chest_cyl_bnd");
    model.component("comp1").physics("solid").create("gacc1", "GravityAcceleration", -1);
    model.component("comp1").physics("solid").feature("gacc1").set("g", new String[] {{ "0", "0", "-g_acc" }});
{shell_physics_java}

    // Current builder scope:
    // 1) build a COMSOL-native outer breast, glandular core, and chest-wall support
    // 2) expose stable finalized geometry selections for the main regions
    // 3) attach initial linearized materials and a skin-shell scaffold boundary selection
    // 4) optionally scaffold a COMSOL Shell interface and a first Solid-Thin Structure Connection attempt
    // 5) run and save MPH
    //
    // Note:
    // This file is still a scaffold. It now creates real geometry, materials, and
    // a first solid mechanics solve, but it does not yet reproduce the full FEBio
    // hyperelastic material law or dynamic motion pipeline automatically.

    model.component("comp1").geom("geom1").run("breast_union");
    model.component("comp1").mesh("mesh1").run();
    return model;
  }}

  public static void main(String[] args) throws Exception {{
    Model model = run();
    model.save("{result_mph_java}");
    ModelUtil.disconnect();
  }}
  private static String tryCreatePhysics(Model model, String tag, String[] candidateIds, String geomTag, StringBuilder notes) {{
    for (String candidateId : candidateIds) {{
      try {{
        model.component("comp1").physics().create(tag, candidateId, geomTag);
        notes.append("Created physics ").append(tag).append(" with id ").append(candidateId).append("\\n");
        return tag;
      }} catch (Exception ex) {{
        notes.append("Physics id ").append(candidateId).append(" failed: ").append(ex.getMessage()).append("\\n");
      }}
    }}
    return null;
  }}

  private static void tryConfigureShellThickness(Model model, String physicsTag, String thicknessExpr, StringBuilder notes) {{
    String[] candidateFeatureTags = new String[] {{ "thick1", "thk1", "to1", "t1" }};
    for (String featureTag : candidateFeatureTags) {{
      try {{
        model.component("comp1").physics(physicsTag).feature(featureTag).set("d0", thicknessExpr);
        notes.append("Assigned shell thickness on feature ").append(featureTag).append("\\n");
        return;
      }} catch (Exception ignored) {{
      }}
      try {{
        model.component("comp1").physics(physicsTag).feature(featureTag).set("thickness", thicknessExpr);
        notes.append("Assigned shell thickness on feature ").append(featureTag).append(" via thickness property\\n");
        return;
      }} catch (Exception ignored) {{
      }}
    }}
    try {{
      for (String featureTag : model.component("comp1").physics(physicsTag).feature().tags()) {{
        try {{
          model.component("comp1").physics(physicsTag).feature(featureTag).set("d0", thicknessExpr);
          notes.append("Assigned shell thickness on discovered feature ").append(featureTag).append("\\n");
          return;
        }} catch (Exception ignored) {{
        }}
        try {{
          model.component("comp1").physics(physicsTag).feature(featureTag).set("thickness", thicknessExpr);
          notes.append("Assigned shell thickness on discovered feature ").append(featureTag).append(" via thickness property\\n");
          return;
        }} catch (Exception ignored) {{
        }}
      }}
    }} catch (Exception ex) {{
      notes.append("Could not inspect shell features for thickness assignment: ").append(ex.getMessage()).append("\\n");
      return;
    }}
    notes.append("Shell physics was created, but no thickness feature accepted skin_shell_thickness automatically.\\n");
  }}

  private static String tryCreateSolidThinStructureConnection(
    Model model,
    String tag,
    String[] candidateIds,
    String geomTag,
    String selectionName,
    String solidPhysicsTag,
    String shellPhysicsTag,
    StringBuilder notes
  ) {{
    for (String candidateId : candidateIds) {{
      try {{
        model.multiphysics().create(tag, candidateId, geomTag);
        try {{
          model.multiphysics(tag).selection().named(selectionName);
        }} catch (Exception selectionEx) {{
          notes.append("Created ").append(tag).append(" but selection binding failed: ").append(selectionEx.getMessage()).append("\\n");
        }}
        trySetStringProperties(model, tag, new String[] {{ "solid", "solidphys", "solidtag", "solidphysics" }}, solidPhysicsTag, notes);
        trySetStringProperties(model, tag, new String[] {{ "shell", "thinstructure", "shellphys", "shelltag", "shellphysics" }}, shellPhysicsTag, notes);
        notes.append("Created multiphysics ").append(tag).append(" with id ").append(candidateId).append("\\n");
        return tag;
      }} catch (Exception ex) {{
        notes.append("Multiphysics id ").append(candidateId).append(" failed: ").append(ex.getMessage()).append("\\n");
      }}
    }}
    return null;
  }}

  private static void trySetStringProperties(Model model, String multiphysicsTag, String[] keys, String value, StringBuilder notes) {{
    for (String key : keys) {{
      try {{
        model.multiphysics(multiphysicsTag).set(key, value);
        notes.append("Set ").append(multiphysicsTag).append(".").append(key).append("=").append(value).append("\\n");
        return;
      }} catch (Exception ignored) {{
      }}
      try {{
        model.multiphysics(multiphysicsTag).set(key, new String[] {{ value }});
        notes.append("Set ").append(multiphysicsTag).append(".").append(key).append("=[").append(value).append("]\\n");
        return;
      }} catch (Exception ignored) {{
      }}
    }}
  }}
{lobule_builder_method_java}
{lobule_helper_methods_java}
}}
"""
    script_path.write_text(java_source, encoding="utf-8")

    readme_path = output_dir / f"{case_name}_comsol_builder_README.txt"
    postprocess_class_name = _safe_java_identifier(f"{case_name}_comsol_postprocess")
    postprocess_java_path = output_dir / f"{postprocess_class_name}.java"
    postprocess_result_mph_java = (solve_dir / f"{case_name}_result.mph").resolve().as_posix()
    postprocess_metrics_json_java = metrics_json_path.resolve().as_posix()
    postprocess_java = f"""import com.comsol.model.*;
import com.comsol.model.util.*;

public class {postprocess_class_name} {{
  private static double firstReal(double[][] values) {{
    if (values == null || values.length == 0 || values[0].length == 0) {{
      return Double.NaN;
    }}
    return values[0][0];
  }}

  private static double evalIntVolume(Model model, String tag, String selectionTag, String expr) {{
    model.result().numerical().create(tag, "IntVolume");
    model.result().numerical(tag).selection().named(selectionTag);
    model.result().numerical(tag).set("expr", new String[] {{ expr }});
    return firstReal(model.result().numerical(tag).getReal());
  }}

  private static double evalMaxVolume(Model model, String tag, String selectionTag, String expr) {{
    model.result().numerical().create(tag, "MaxVolume");
    model.result().numerical(tag).selection().named(selectionTag);
    model.result().numerical(tag).set("expr", new String[] {{ expr }});
    return firstReal(model.result().numerical(tag).getReal());
  }}

  public static Model run() throws Exception {{
    ModelUtil.initStandalone(true);
    Model model = ModelUtil.load("PostModel", "{postprocess_result_mph_java}");

    double breastVolume = evalIntVolume(model, "ivBreastVol", "geom1_breast_union_dom", "1");
    double glandVolume = evalIntVolume(model, "ivGlandVol", "geom1_gland_clip_dom", "1");
    double adiposeVolume = evalIntVolume(model, "ivAdiposeVol", "geom1_adipose_diff_dom", "1");

    double maxDispBreast = evalMaxVolume(model, "mvDispBreast", "geom1_breast_union_dom", "solid.disp");
    double intDispBreast = evalIntVolume(model, "ivDispBreast", "geom1_breast_union_dom", "solid.disp");
    double avgDispBreast = breastVolume != 0.0 ? intDispBreast / breastVolume : Double.NaN;

    double maxMisesBreast = evalMaxVolume(model, "mvMisesBreast", "geom1_breast_union_dom", "solid.mises");
    double maxMisesGland = evalMaxVolume(model, "mvMisesGland", "geom1_gland_clip_dom", "solid.mises");

    String json = ""
      + "{{\\n"
      + "  \\"case_name\\": \\"{case_name}\\",\\n"
      + "  \\"source\\": \\"COMSOL\\",\\n"
      + "  \\"breast_volume\\": " + breastVolume + ",\\n"
      + "  \\"glandular_volume\\": " + glandVolume + ",\\n"
      + "  \\"adipose_volume\\": " + adiposeVolume + ",\\n"
      + "  \\"max_displacement_breast\\": " + maxDispBreast + ",\\n"
      + "  \\"avg_displacement_breast\\": " + avgDispBreast + ",\\n"
      + "  \\"max_von_mises_breast\\": " + maxMisesBreast + ",\\n"
      + "  \\"max_von_mises_glandular\\": " + maxMisesGland + "\\n"
      + "}}\\n";

    System.out.println("COMSOL_METRICS_JSON_BEGIN");
    System.out.print(json);
    System.out.println("COMSOL_METRICS_JSON_END");
    return model;
  }}

  public static void main(String[] args) throws Exception {{
    run();
    ModelUtil.disconnect();
  }}
}}
"""
    postprocess_java_path.write_text(postprocess_java, encoding="utf-8")
    readme_path.write_text(
        "\n".join(
            [
                "Generated COMSOL builder scaffold",
                f"Java source: {script_path}",
                f"Build plan: {build_plan_path}",
                f"Selection hints: {selection_hints_path}",
                f"Metrics postprocess Java: {postprocess_java_path}",
                f"Metrics JSON target: {metrics_json_path}",
                f"Target MPH: {result_mph}",
                "",
                "Typical next step:",
                "1) Open/compile this Java file via COMSOL Java API tooling",
                "2) Inspect the generated finalized geometry selections such as geom1_breast_union_dom",
                "3) Validate displacement/stress output for the static gravity case",
                "4) Replace the linearized material approximation if a higher-fidelity COMSOL constitutive law is needed",
                "5) Add the dynamic motion case after the static setup is stable",
                "",
                "Important:",
            "This Java file now builds real geometry and region selections, but material laws and loading are still the next step.",
            ]
        )
        + "\n",
        encoding="utf-8",
    )

    return {
        "comsol_builder_java": str(script_path.resolve()),
        "comsol_builder_readme": str(readme_path.resolve()),
        "comsol_generated_mph_target": str(result_mph.resolve()),
        "comsol_selection_hints_json": str(selection_hints_path.resolve()),
        "comsol_postprocess_java": str(postprocess_java_path.resolve()),
        "comsol_metrics_json_target": str(metrics_json_path.resolve()),
    }
