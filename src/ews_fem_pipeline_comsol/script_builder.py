from __future__ import annotations

import json
import re
from pathlib import Path


def _comsol_safe_name(text: str) -> str:
    return text.replace("\\", "/")


def _safe_java_identifier(text: str) -> str:
    identifier = re.sub(r"[^0-9a-zA-Z_]", "_", text)
    if not identifier:
        identifier = "comsol_case"
    if identifier[0].isdigit():
        identifier = f"case_{identifier}"
    return identifier


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

    left_pos = left_rel * radius
    nipple_pos = nipple_rel * radius
    center_pos = center_rel * radius

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
            "geom1_chest_cyl_dom": "Chest-wall support cylinder",
        },
        "component_boundary_selections": {
            "geom1_breast_union_bnd": "Exterior boundaries of the full assembled breast/chest geometry",
            "geom1_adipose_diff_bnd": "Boundaries of the adipose domain",
            "geom1_gland_clip_bnd": "Boundaries of the glandular domain",
            "geom1_chest_cyl_bnd": "Boundaries of the chest-wall support domain",
        },
        "geometry_feature_tags": {
            "breast_outer": "Outer breast base derived from the FEBio-style half-sphere baseline",
            "gland_keep_anterior": "Anterior half-space used to clip the glandular ellipsoid flat at the chest wall",
            "gland_clip": "Glandular source volume clipped to the breast outer volume",
            "gland_lobules": "Union of COMSOL-native lobule ellipsoids derived from the exported FEBio lobule layout",
            "adipose_diff": "Adipose outer volume minus glandular volume",
            "chest_cyl": "Chest-wall cylindrical support",
            "breast_union": "Final union used for meshing/physics",
        },
        "recommended_physics_targets": {
            "full_breast_domain_selection": "geom1_breast_union_dom",
            "adipose_domain_selection": "geom1_adipose_diff_dom",
            "glandular_domain_selection": "geom1_gland_clip_dom",
            "chest_domain_selection": "geom1_chest_cyl_dom",
            "fixed_boundary_selection": "geom1_chest_cyl_bnd",
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

    lobule_feature_tags: list[str] = []
    lobule_specs: list[dict[str, float | str]] = []
    lobule_java_blocks: list[str] = []
    for idx, lobule in enumerate(lobules, start=1):
        center = lobule.get("center", [0.0, 0.0, 0.0])
        if not isinstance(center, list) or len(center) != 3:
            continue
        cx, cy, cz = (float(center[0]), float(center[1]), float(center[2]))
        sx = max(float(lobule.get("width_x", lobule.get("width", 0.002))) * 1.08, radius * 0.01)
        sy = max(float(lobule.get("width_y", lobule.get("width", 0.002))) * 1.08, radius * 0.01)
        sz = max(float(lobule.get("width_z", lobule.get("width", 0.002))) * 1.08, radius * 0.01)
        tag = f"lobule_{idx:02d}"
        lobule_feature_tags.append(tag)
        lobule_specs.append({"tag": tag, "cx": cx, "cy": cy, "cz": cz, "sx": sx, "sy": sy, "sz": sz})
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
    bridge_feature_tags: list[str] = []
    bridge_java_blocks: list[str] = []
    if droplet_components > 1:
        for start in range(0, len(lobule_specs), droplet_components):
            group = lobule_specs[start : start + droplet_components]
            if len(group) < 2:
                continue
            for left_idx in range(len(group) - 1):
                left = group[left_idx]
                right = group[left_idx + 1]
                cx = (float(left["cx"]) + float(right["cx"])) / 2.0
                cy = (float(left["cy"]) + float(right["cy"])) / 2.0
                cz = (float(left["cz"]) + float(right["cz"])) / 2.0
                sx = max(
                    (float(left["sx"]) + float(right["sx"])) * 0.55,
                    abs(float(right["cx"]) - float(left["cx"])) * 0.60 + min(float(left["sx"]), float(right["sx"])) * 0.55,
                )
                sy = max(
                    (float(left["sy"]) + float(right["sy"])) * 0.55,
                    abs(float(right["cy"]) - float(left["cy"])) * 0.60 + min(float(left["sy"]), float(right["sy"])) * 0.55,
                )
                sz = max(
                    (float(left["sz"]) + float(right["sz"])) * 0.55,
                    abs(float(right["cz"]) - float(left["cz"])) * 0.60 + min(float(left["sz"]), float(right["sz"])) * 0.55,
                )
                tag = f"lobule_bridge_{start + left_idx + 1:02d}"
                bridge_feature_tags.append(tag)
                bridge_java_blocks.append(
                    f"""
    model.component("comp1").geom("geom1").create("{tag}", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("{tag}").set("semiaxes", "{sx:.8f} {sz:.8f} {sy:.8f}");
    model.component("comp1").geom("geom1").feature("{tag}").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("{tag}").set("pos", "{cx:.8f} {cy:.8f} {cz:.8f}");
    model.component("comp1").geom("geom1").feature("{tag}").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("{tag}").set("selresultshow", "all");
"""
                )
    lobule_union_inputs = ", ".join(f'"{tag}"' for tag in [*lobule_feature_tags, *bridge_feature_tags])
    use_lobules = bool(lobule_feature_tags)
    lobule_java = "".join([*lobule_java_blocks, *bridge_java_blocks])
    gland_source_tag = "gland_lobules" if use_lobules else "gland_seed"
    gland_source_objects_var = "glandLobuleObjs" if use_lobules else "glandSeedObjs"
    lobule_union_java = (
        f"""
    {lobule_java}
    model.component("comp1").geom("geom1").create("gland_lobules", "Union");
    model.component("comp1").geom("geom1").feature("gland_lobules").selection("input").set({lobule_union_inputs});
    model.component("comp1").geom("geom1").feature("gland_lobules").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_lobules");
    String[] glandLobuleObjs = model.component("comp1").geom("geom1").feature("gland_lobules").objectNames();
"""
        if use_lobules
        else ""
    )

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
    String[] breastBaseObjs = model.component("comp1").geom("geom1").feature("breast_base").objectNames();

    model.component("comp1").geom("geom1").create("breast_outer", "Union");
    model.component("comp1").geom("geom1").feature("breast_outer").selection("input").set(breastBaseObjs);
    model.component("comp1").geom("geom1").feature("breast_outer").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("breast_outer");
    String[] breastOuterObjs = model.component("comp1").geom("geom1").feature("breast_outer").objectNames();

    model.component("comp1").geom("geom1").create("chest_cyl", "Cylinder");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("r", "breast_radius");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("h", "chest_thickness");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("pos", "0 -chest_thickness 0");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("chest_cyl");
    String[] chestObjs = model.component("comp1").geom("geom1").feature("chest_cyl").objectNames();

    model.component("comp1").geom("geom1").create("gland_keep_anterior", "Block");
    model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("size", "2*breast_radius 2*breast_radius 2*breast_radius");
    model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("pos", "-breast_radius 0 -breast_radius");
    model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresultshow", "all");
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
    // - Lobules in plan: {build_plan_summary["lobule_count"]}
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

    // Current builder scope:
    // 1) build a COMSOL-native outer breast, glandular core, and chest-wall support
    // 2) expose stable finalized geometry selections for the main regions
    // 3) attach initial linearized materials and solid mechanics
    // 4) run and save MPH
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
