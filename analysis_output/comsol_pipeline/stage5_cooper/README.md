# COMSOL Evaluation: stage5_cooper

Generated from existing COMSOL solve outputs only. This script does not run COMSOL.

## Contents

- `figures/contact_sheet.png`
- `figures/displacement_magnitude_response.png`
- `figures/stress_mean_max_response.png`
- `figures/tissue_stress_review_available.png`
- `figures/surface_signed_vertical_response.png`
- `figures/surface_displacement_statistics.png`
- `figures/surface_vertical_dynamic_response.png`
- `figures/landmark_nipple_signed_vertical_response.png`
- `figures/landmark_nipple_vertical_dynamic_response.png`
- `figures/response_change_vs_reference.png`
- `tables/review_metrics.csv`
- `tables/review_metrics.md`
- `tables/summary_results.csv`
- `tables/summary_results.md`
- `sources.csv`

## Interpretation Notes

- Volume displacement magnitude (`disp_avg_mm`, `disp_max_mm`) is kept as a diagnostic continuity metric.
- Mean stress is plotted only when `vm_avg_pa` is present. COMSOL median stress is not exported in the current result files.
- Adipose stress is plotted only when adipose stress columns are present. Missing adipose data is omitted, not set to zero.
- Signed surface displacement uses the COMSOL `w` component on the exported outer-skin surface CSV.
- Dynamic-response plots prefer support-relative displacement when `support_*` columns exist; otherwise they fall back to dynamic-start correction as diagnostic output.
- Landmark plots use patch-average displacement, not a single mesh node.
- Tissue stress stats CSV is present; median/p95/p99 remain blank unless sampled-field exports are added.

## Source Cases

- No Cooper xoffset055 0.25g: `runs/comsol_runs/geometry_stage5/outputs/output_stage5_reference_no_cooper_xoffset055_025g_preview`
- 5B default xoffset055 failed early: `runs/comsol_runs/geometry_stage5/outputs/output_stage5b_cooper_default_xoffset055_025g_preview`
