# Stage 2 - Nipple-Aligned Volume-Preserving Width-Curved Chestwall

This is the canonical Stage 2 COMSOL folder after the width-curved chestwall refinement.

## Final Interpretation

Stage 2 should be described as a physically defensible mild transverse/width chestwall curvature, not as a universal anatomical chestwall shape. The curvature is patient-dependent; the current implementation is a clean model step that introduces a curved posterior support while preserving breast volume and keeping the model dynamic.

## What Was Tried

1. Old curved/slab Stage 2 variants were archived in `runs/comsol_runs/geometry_stage2_old`.
2. Initial width-curvature mild/medium/strong cases were built in this folder.
3. Those first width-curvature cases were not clean report cases because they changed breast volume strongly.
4. A first volume-preserving suite restored total volume, but scaling the gland fully with the outer AP compensation created a local glandular stress hotspot.
5. The refined VP suite tested gland AP scale variants.
6. A geometry bug was found: the outer breast was shifted anteriorly for volume preservation, but nipple/areola geometry stayed at the old spherical position. This made the nipple appear partially embedded in the breast.
7. The builder was fixed so nipple geometry, nipple selections, anterior skin patch, and gland-nipple core follow the volume-preserved anterior surface.

## Current Main Case

The current Stage 2 report candidate is:

`stage2_vp_refined_mild_g1050_fixed_order2.toml`

This case uses:

- `support_geometry_mode = "transverse_vp"`
- `chestwall_curve_depth_m = 0.0045`
- outer AP scale `1.073`
- gland AP scale `1.050`
- fixed literature-based materials
- `ls = 0.005`
- `density = 125`
- order 2
- Stage 5B glandular-to-skin Cooper scaffold
- nipple-aligned anterior geometry

The nipple-aligned rerun gave approximately:

| Metric at review time 1.125 s | Value |
| --- | ---: |
| Breast volume | 719.19 mL |
| Glandular volume | 82.65 mL |
| Glandular fraction | 11.49% |
| Max displacement | 11.98 mm |
| Max breast VM | 1.92 kPa |
| Avg breast VM | 0.44 kPa |
| Max gland VM | 1.92 kPa |
| Avg gland VM | 0.91 kPa |
| Hotspot factor breast | 4.45 |
| Hotspot factor gland | 2.17 |

The run still stops with a final non-converged time step around 1.25 s, but the review-time output at 1.125 s is available and the result MPH, metrics, logs, and screenshots were saved.

## Sensitivity Cases To Keep

- `stage2_vp_refined_mild_g1025_fixed_order2.toml`: solver/geometry sensitivity with slightly smaller gland AP scale.
- `stage2_vp_refined_mild_g1073_fixed_order2.toml`: hotspot sensitivity; useful to explain why fully scaling the gland with the outer AP compensation was rejected.
- `stage2_vp_refined_slab_reference_fixed_order2.toml`: flat-support reference.

## Historical Cases

The old `stage2_width_*` cases are retained for traceability in `_archive_stage2_tomls` and `_archive_stage2_summaries`, but they should not be used as final Stage 2 conclusions because they are non-volume-preserving. Their active output folders were removed after summaries/screenshots/metrics had been preserved in the archive.

## CMD Commands

Build/check the main nipple-aligned case:

```cmd
python -m ews_fem_pipeline_comsol build-only runs\comsol_runs\geometry_stage2\stage2_vp_refined_mild_g1050_fixed_order2.toml
```

Run the final Stage 2 main/sensitivity pair:

```cmd
python -m ews_fem_pipeline_comsol run runs\comsol_runs\geometry_stage2\stage2_vp_refined_mild_g1050_fixed_order2.toml runs\comsol_runs\geometry_stage2\stage2_vp_refined_mild_g1025_fixed_order2.toml
```

Compare metrics:

```cmd
python -m ews_fem_pipeline_comsol compare-metrics runs\comsol_runs\geometry_stage2\outputs\output_stage2_vp_refined_slab_reference_fixed_order2\solve\stage2_vp_refined_slab_reference_fixed_order2_metrics.json runs\comsol_runs\geometry_stage2\outputs\output_stage2_vp_refined_mild_g1050_fixed_order2\solve\stage2_vp_refined_mild_g1050_fixed_order2_metrics.json runs\comsol_runs\geometry_stage2\outputs\output_stage2_vp_refined_mild_g1025_fixed_order2\solve\stage2_vp_refined_mild_g1025_fixed_order2_metrics.json --baseline stage2_vp_refined_slab_reference_fixed_order2
```

## Cleanup

See `cleanup_plan.md`. The cleanup has been executed: old Stage 2 folders were removed after TOMLs and lightweight summaries were archived, and the active `outputs` folder now contains only the slab reference, `g1050`, and `g1025` report-relevant summaries/screenshots/metrics.

No Stage 2 `.mph` files are currently present after cleanup. Recreate COMSOL-openable result MPH files only when needed by rerunning the three report-relevant TOMLs listed above.
