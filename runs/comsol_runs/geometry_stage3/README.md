# Stage 3 - Glandular Fraction And Realism

This is the canonical Stage 3 COMSOL folder. Stage 3 now focuses only on glandular tissue: first a clean glandular fraction sensitivity, then a more realistic glandular shape/structure step.

## Current Working Set

The active working set in this folder is now intentionally small:

- `stage3_glandular_realistic_spread_compact_xoffset055_preview.toml`
- `stage3_glandular_realistic_spread_reference_xoffset055_preview.toml`
- `stage3_glandular_realistic_spread_wide_xoffset055_preview.toml`

These cases use the selected Stage 2 transverse x-offset chestwall (`0.055 m`), projected-normal auto-alignment, transverse-only curvature, and the Stage 1 `0.25g` fixed-support acceleration pulse settings.

All older Stage 3 TOMLs and output folders were moved, without deletion, to:

`_archive_stage3_legacy_2026-05-22/`

Use that archive for traceability only. The current development route is the realistic lobule-spread set above.

## Current Structure

| Path | Role |
| --- | --- |
| `stage3_glandular_realistic_spread_*_xoffset055_preview.toml` | Active build-only realistic lobule-spread previews on the selected Stage 2 xoffset055 autoalign chestwall |
| `outputs/output_stage3_glandular_realistic_spread_*_xoffset055_preview/` | Active build/generate outputs for the three realistic-spread previews |
| `_archive_stage3_legacy_2026-05-22/tomls/` | Archived older Stage 3 TOMLs |
| `_archive_stage3_legacy_2026-05-22/outputs/` | Archived older Stage 3 output folders |
| `_archive_old_stage4_glandular_ideas/` | Legacy Stage 4 glandular/subareolar/rich-gland ideas, archived without heavy `.mph` files |
| `_archive_old_stage4a_notes/` | Legacy Stage 4A notes |
| `cleanup_plan.md` | What was moved, archived, and removed |

The previous material-test Stage 3 cases were moved out of this geometry stage and live in:

`runs/comsol_runs/material_parameter_sensitivity`

## Fixed Across Active Stage 3 Cases

- outer breast geometry;
- nipple-aligned anterior geometry;
- Stage 2 volume-preserving width-curved chestwall settings;
- Stage 5B glandular-to-skin Cooper scaffold;
- fixed literature-based material parameters;
- `ls = 0.005`;
- `density = 125`;
- dynamic setup;
- automatic COMSOL result plots/screenshots.

## Glandular Fraction Strategy

The current first-pass sensitivity varies only glandular x/z scale. The AP/y gland scale stays at the Stage 2 g1050 value so the anterior nipple/Cooper region is not changed at the same time.

| Case | x/z gland scale | Intended interpretation | Approx target volumetric FGT fraction |
| --- | ---: | --- | ---: |
| `stage3_glandular_low_fixed_order*` | 0.720 | adipose-dominant / low FGT | about 6% |
| `stage3_glandular_reference_fixed_order*` | 1.000 | current Stage 2 g1050 reference | about 11.5% |
| `stage3_glandular_high_fixed_order*` | 1.320 | high FGT / dense sensitivity | about 20% |
| `stage3_glandular_very_high_fixed_order*` | 1.616 | upper-tail / very dense sensitivity | about 30% |

These percentages are volumetric FEM fractions:

`glandular volume / total breast volume`

They are not the same as older projected mammographic BI-RADS area percentages. The rationale is documented in:

`docs/report_notes/comsol_pipeline/model_justification/glandular_fraction_recommendations.md`

## Generated Checks

The `generate` command completed for all active configs after the Stage 3 folder rename.

Mesh pre-check:

- order 1: 6079 elements, min quality 1.0000, poor elements 0;
- order 2: 6079 elements, min quality 0.1061, poor elements 0.

Generated Java semiaxes for the gland seed:

| Case | x semiaxis | z semiaxis | y semiaxis |
| --- | ---: | ---: | ---: |
| low | 0.017388 m | 0.016632 m | 0.077175 m |
| reference | 0.024150 m | 0.023100 m | 0.077175 m |
| high | 0.031878 m | 0.030492 m | 0.077175 m |
| very high | 0.039026 m | 0.037330 m | 0.077175 m |

The nipple outer position remains `y = 0.07563920 m` for all cases.

## Recommended Interpretation

Use the reference fraction as the report continuation from Stage 2. Use low/high as the main fraction sensitivity pair. Treat very-high as an optional upper-tail geometry/solver limit case, not as the default model.

After the fraction sweep, the next realism step should use the archived Stage 4 ideas to replace the perfect ellipsoid with a more defensible glandular distribution, while holding the selected fraction fixed.

## Realistic Lobule-Spread Previews On Stage 2 XOffset055

The new realistic spread previews use the selected Stage 2 transverse x-offset chestwall instead of the older centered Stage 2 VP g1050 setup:

- `chestwall_curve_depth_m = 0.021`;
- `chestwall_curve_center_x_offset_m = 0.055`;
- `chestwall_alignment_mode = "projected_normal_axis"`;
- `chestwall_curve_si_depth_m = 0.0`;
- `dynamic_acceleration_amplitude_g = 0.25`;
- `dynamic_acceleration_duration_s = 0.60`.

They vary lobule spread while keeping lobule count and width fixed:

| Case | inner/outer ring radius | Intended interpretation |
| --- | ---: | --- |
| `stage3_glandular_realistic_spread_compact_xoffset055_preview.toml` | 10 / 20 mm | compact lobule spread |
| `stage3_glandular_realistic_spread_reference_xoffset055_preview.toml` | 12.5 / 25 mm | reference lobule spread |
| `stage3_glandular_realistic_spread_wide_xoffset055_preview.toml` | 15 / 30 mm | wider lobule spread |

The COMSOL builder now translates generated lobules to the final COMSOL nipple axis after Stage 2 auto-alignment. The generator also has an x-offset-aware chestwall clearance parameter for the xoffset055 chestwall.

These are build-only preview cases. Their actual glandular fraction must be measured after COMSOL clipping; do not describe them as constant-volume cases until the volume checks prove that.

Build-only volume export is deliberately disabled for now: COMSOL's class-based builder can be blocked from writing extra metrics files, and that made the preview route brittle. Use build-only for visual geometry checks; use postprocess/metrics after a successful COMSOL model build or solve when exact clipped glandular fractions are needed.

The older `stage3_glandular_spatial_*_xoffset055_preview.toml` cases shifted the simple ellipsoid gland as a whole. That changed fraction/alignment too strongly and should only be kept as diagnostic history.

## Improved Chestwall-Aware Core Fraction Suite

The newest report-oriented Stage 3 suite applies the same low/reference/high/very-high idea to the visually approved chestwall-aware core structure. These cases keep the same Stage 2 VP g1050 chestwall, Stage 5B glandular-to-skin Cooper scaffold, fixed materials, `ls = 0.005`, `density = 125`, dynamic setup, and automatic result plots. The breast geometry is not rescaled; only the internal lobule/duct/core dimensions are changed.

| Case | Order | Nominal FGT target | Main scaling changes | Role |
| --- | ---: | ---: | --- | --- |
| `stage3_glandular_chestwall_aware_core_low_fixed_order*` | 1/2 | about 6% | smaller lobule width, rings, duct radius, bridge/core | low FGT sensitivity |
| `stage3_glandular_chestwall_aware_core_reference_fixed_order*` | 1/2 | about 11.5% | same as the approved core A2 preview | primary report candidate |
| `stage3_glandular_chestwall_aware_core_high_fixed_order*` | 1/2 | about 20% | larger lobules/rings/ducts/core | dense FGT sensitivity |
| `stage3_glandular_chestwall_aware_core_very_high_fixed_order*` | 1/2 | about 30% | upper-tail larger lobules/rings/core | optional geometry/solver limit |

Generate-time checks for the new suite:

- all 8 improved core fraction configs generated successfully;
- order 1: 6079 checked elements, min quality 1.0000, poor elements 0;
- order 2: 6079 checked elements, min quality 0.1061, poor elements 0;
- all core fraction variants keep 12 lobes and use continuous `ellipsoid_segments` ducts;
- `gland_lobule_source` includes `gland_lobules`, `gland_nipple_core`, and `gland_subareolar_bridge`, so the anterior duct/core region is part of the final `gland_clip` source.

Approximate posterior clearance diagnostics from generated lobule metadata:

| Case | Lobules | Min estimated posterior clearance | Max anterior shift from chestwall-aware placement |
| --- | ---: | ---: | ---: |
| low | 12 | 13.05 mm | 0.00 mm |
| reference | 12 | 11.93 mm | 3.74 mm |
| high | 12 | 13.63 mm | 11.06 mm |
| very-high | 12 | 15.13 mm | 17.20 mm |

The high and very-high cases intentionally push the geometry harder and must be visually checked in COMSOL before being interpreted. The exact glandular fraction must come from COMSOL clipped volume metrics after build/run; the nominal labels are targets, not final measured values.

## Glandular Realism Candidates

The first shape previews showed a useful problem: when lobules were enabled, the COMSOL builder used only the generated lobule/duct union as the glandular source. The simple ellipsoid seed and anterior gland-nipple core were still generated, but they were not included in the final `gland_clip` source. That made the `stage3_glandular_shape_subareolar_connected_fixed_order1` preview look more anatomical than a plain ellipsoid, but left too little glandular tissue in the nipple/subareolar direction.

The builder now supports adding an explicit subareolar glandular core to lobular cases before clipping the glandular domain to the breast volume. This keeps the structure connected toward the nipple region without using the entire ellipsoid as glandular tissue.

The next preview issue was posterior realism: the lobes still used a slab-like posterior layout, so some posterior lobe/cap geometry could be cut by the Stage 2 transverse curved chestwall. The newest candidates therefore make the template lobes chestwall-aware. Their posterior bulb centers are shifted anteriorly only when required so the estimated posterior lobe extent keeps a 3-3.5 mm clearance from the Stage 2 VP g1050 chestwall arc. The ducts are also no longer generated as small bead chains; the COMSOL builder now uses overlapping elongated ellipsoid segments for a more continuous ductal path into the subareolar core. After the first chestwall-aware preview, an explicit `gland_subareolar_bridge` was added to the glandular source union so the duct/core region reaches further anterior toward the nipple instead of leaving a grey non-glandular gap.

Current order-1 shape candidates:

| Case | Generator | Lobule count | Glandular source | Interpretation |
| --- | --- | ---: | --- | --- |
| `stage3_glandular_reference_fixed_order1.toml` | ellipsoid seed | 0 | clipped ellipsoid + nipple core | current simple reference gland |
| `stage3_glandular_shape_lobular_reference_fixed_order1.toml` | `chen_2024_double_ring` | 36 | lobules/ducts only | legacy richer lobular preview; useful visual comparison |
| `stage3_glandular_shape_subareolar_connected_fixed_order1.toml` | `chen_2024_template_lobes` | 18 | lobules/ducts only | legacy subareolar-connected preview; ducts stopped too early near nipple |
| `stage3_glandular_shape_subareolar_core_fixed_order1.toml` | `chen_2024_template_lobes` | 12 | lobules/ducts + scaled subareolar core | candidate A: robust/simple report-ready candidate |
| `stage3_glandular_shape_rich_lobular_core_fixed_order1.toml` | `chen_2024_template_lobes` | 18 | lobules/ducts + larger subareolar core | candidate B: richer realism sensitivity |
| `stage3_glandular_shape_chestwall_aware_core_fixed_order1.toml` | `chen_2024_template_lobes` | 12 | chestwall-aware lobules + continuous ducts + subareolar core | candidate A2: preferred robust preview |
| `stage3_glandular_shape_chestwall_aware_rich_fixed_order1.toml` | `chen_2024_template_lobes` | 18 | chestwall-aware lobules + continuous ducts + larger subareolar core | candidate B2: richer Chen-inspired sensitivity |
| `stage3_glandular_shape_chestwall_aware_rich_fixed_order2.toml` | `chen_2024_template_lobes` | 18 | same as B2 | order-2 richer realism sensitivity |

Generated checks for the new candidates:

- all active shape candidates use the same Stage 2 VP g1050 chestwall, Stage 5B Cooper scaffold, fixed materials, `ls = 0.005`, and `density = 125`;
- the newest chestwall-aware candidates generated with 6079 checked mesh elements, min precheck quality 1.0000, poor elements 0;
- candidate A2 has 12 lobes, 4 continuous duct segments per lobe, 3.5 mm chestwall clearance, and is the preferred robust first report candidate if COMSOL build-only looks clean;
- candidate B2 has 18 lobes, 5 continuous duct segments per lobe, 3.0 mm chestwall clearance, and should be treated as the richer visual/solver sensitivity until COMSOL volume and stress checks confirm it behaves cleanly;
- both A2/B2 include `gland_nipple_core` and `gland_subareolar_bridge` in `gland_lobule_source`, so the anterior ductal/subareolar region is part of `geom1_gland_clip_dom`;
- generate-time lobe metadata confirms posterior center adjustment is active: A2 shifts some posterior lobes by up to about 3.75 mm, B2 by up to about 1.72 mm, following the transverse chestwall surface.

These shape candidates have only been generated/prechecked, not COMSOL-built or solved. Their final glandular volume/fraction must be checked after COMSOL build/run because boolean union and clipping can change the actual glandular volume relative to the rough generated lobe estimate.

The most sensible report comparison is now:

1. simple ellipsoid fraction sweep, to quantify glandular fraction without shape realism;
2. improved chestwall-aware core reference, to test the report-ready realistic gland;
3. improved chestwall-aware core low/high, to quantify fraction effects within the realistic gland;
4. rich B2, as a Chen-inspired realism sensitivity.

The old lobular/subareolar-core shape outputs are historical diagnostics only. Their heavy `.mph` files have been removed; TOMLs and lightweight artifacts remain for traceability.

## CMD Commands

Build-only simple ellipsoid fraction preview:

```cmd
python -m ews_fem_pipeline_comsol build-only runs\comsol_runs\geometry_stage3\stage3_glandular_low_fixed_order1.toml runs\comsol_runs\geometry_stage3\stage3_glandular_reference_fixed_order1.toml runs\comsol_runs\geometry_stage3\stage3_glandular_high_fixed_order1.toml runs\comsol_runs\geometry_stage3\stage3_glandular_very_high_fixed_order1.toml
```

Build-only improved core fraction preview:

```cmd
python -m ews_fem_pipeline_comsol build-only runs\comsol_runs\geometry_stage3\stage3_glandular_chestwall_aware_core_low_fixed_order1.toml runs\comsol_runs\geometry_stage3\stage3_glandular_chestwall_aware_core_reference_fixed_order1.toml runs\comsol_runs\geometry_stage3\stage3_glandular_chestwall_aware_core_high_fixed_order1.toml runs\comsol_runs\geometry_stage3\stage3_glandular_chestwall_aware_core_very_high_fixed_order1.toml
```

Build-only rich realism sensitivity preview:

```cmd
python -m ews_fem_pipeline_comsol build-only runs\comsol_runs\geometry_stage3\stage3_glandular_shape_chestwall_aware_rich_fixed_order1.toml runs\comsol_runs\geometry_stage3\stage3_glandular_shape_chestwall_aware_rich_fixed_order2.toml
```

Build-only realistic lobule-spread preview on the selected Stage 2 xoffset055 chestwall:

```cmd
python -m ews_fem_pipeline_comsol build-only runs\comsol_runs\geometry_stage3\stage3_glandular_realistic_spread_compact_xoffset055_preview.toml runs\comsol_runs\geometry_stage3\stage3_glandular_realistic_spread_reference_xoffset055_preview.toml runs\comsol_runs\geometry_stage3\stage3_glandular_realistic_spread_wide_xoffset055_preview.toml
```

Short order-1 smoke run after build-only previews are clean:

```cmd
python -m ews_fem_pipeline_comsol run runs\comsol_runs\geometry_stage3\stage3_glandular_reference_fixed_order1.toml runs\comsol_runs\geometry_stage3\stage3_glandular_chestwall_aware_core_reference_fixed_order1.toml runs\comsol_runs\geometry_stage3\stage3_glandular_chestwall_aware_core_high_fixed_order1.toml runs\comsol_runs\geometry_stage3\stage3_glandular_shape_chestwall_aware_rich_fixed_order1.toml
```

Main Stage 3 overnight command:

```cmd
python -m ews_fem_pipeline_comsol run runs\comsol_runs\geometry_stage3\stage3_glandular_low_fixed_order2.toml runs\comsol_runs\geometry_stage3\stage3_glandular_reference_fixed_order2.toml runs\comsol_runs\geometry_stage3\stage3_glandular_high_fixed_order2.toml runs\comsol_runs\geometry_stage3\stage3_glandular_very_high_fixed_order2.toml runs\comsol_runs\geometry_stage3\stage3_glandular_chestwall_aware_core_low_fixed_order2.toml runs\comsol_runs\geometry_stage3\stage3_glandular_chestwall_aware_core_reference_fixed_order2.toml runs\comsol_runs\geometry_stage3\stage3_glandular_chestwall_aware_core_high_fixed_order2.toml runs\comsol_runs\geometry_stage3\stage3_glandular_chestwall_aware_core_very_high_fixed_order2.toml runs\comsol_runs\geometry_stage3\stage3_glandular_shape_chestwall_aware_rich_fixed_order2.toml
```

Run the build-only preview first. If the high or very-high improved core preview looks clipped or too anteriorly crowded, drop only those cases from the overnight command and keep low/reference/high simple ellipsoid plus low/reference/high improved core.

## What To Check After Running

- breast volume remains near Stage 2 g1050;
- glandular volume/fraction follows low-reference-high ordering;
- adipose volume changes as the internal glandular region replaces adipose;
- glandular region remains inside skin/chestwall/nipple;
- posterior lobules follow the transverse chestwall curve and keep visible clearance instead of being cut flat;
- ducts appear as continuous paths into the subareolar core, not separate small bead clusters;
- nipple remains on the anterior surface;
- max and average VM in breast/gland/adipose;
- hotspot factors;
- displacement and glandular motion at `review_time_s = 1.125 s`;
- automatic screenshots `01`, `05`, `06`, and `07`.
