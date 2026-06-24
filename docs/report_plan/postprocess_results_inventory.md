# Postprocess results inventory for Results section

Date checked: 2026-06-23

This file records which recent COMSOL postprocess outputs are available for the report and which results are safe to use. It is intended as a working inventory for the Results section, not as final report text.

## Short conclusion

The Cooper-support `ews_surface` batch is complete and report-ready. The no-Cooper comparison case and all five Cooper variants have summary, time-series, metrics and landmark-displacement output files.

For tumor results, the medium upper-outer tumor case is usable and has both `ews_surface` and `internal_tumor` outputs. The two large central hard-100-kPa cases should be treated carefully: one did not produce a normal time-series, and the "from validated build" case has almost zero dynamic response. These are diagnostic outputs, not strong main Results evidence.

Additional Queue A update: a matched realistic-gland, volumetric-skin medium upper-outer tumor case was solved successfully on 2026-06-24. Full `ews_surface` post-processing was too expensive for the 5.1 GB result MPH, but a lightweight `global` quick postprocess now provides report-ready displacement and tumor-displacement time series. This new case should be used only for displacement/tumor-displacement trends, not for stress or surface-landmark claims.

## File types to use

- `*_ews_surface_time_series.csv`: time-dependent global breast/tissue response. Use for line plots of `disp_max_mm`, `disp_avg_mm` and `vm_max_pa`.
- `*_ews_surface_summary.json`: summary metadata and review-time surface scalars, especially `review_surface_disp_mag_max_mm` and `review_surface_signed_w_min_mm`.
- `*_ews_surface_landmark_displacement.csv`: landmark displacement export. Use if a compact landmark/surface-motion table or plot is needed.
- `*_internal_tumor_time_series.csv`: tumor-mask time-dependent response. Use for tumor-only displacement and stress trends.
- `*_internal_tumor_summary.json`: tumor volume, tumor peak displacement and tumor peak stress. Use for a compact tumor metrics table.

## Cooper-support cases

Reference case for percentage differences: `CTRL_REF`.

| Report label | Case meaning | Status | Peak disp. breast (mm) | Mean disp. breast max (mm) | Peak VM stress (kPa) | Review surface max disp. (mm) | Delta peak disp. vs CTRL | Delta review surface vs CTRL |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| `CTRL_REF` | No-Cooper reference, volumetric skin, soft interior, 1.25 g | OK | 45.582 | 22.554 | 19.283 | 11.603 | 0.00% | 0.00% |
| `COOP_MILD` | Mild Cooper-like support | OK | 44.994 | 22.442 | 19.069 | 11.650 | -1.29% | +0.41% |
| `COOP_G2S_A006` | Skin-to-glandular support, area fraction 0.06 | OK | 44.970 | 22.437 | 19.062 | 11.652 | -1.34% | +0.42% |
| `COOP_G2S_A012` | Skin-to-glandular support, area fraction 0.12 | OK | 44.956 | 22.434 | 19.058 | 11.653 | -1.37% | +0.43% |
| `COOP_N2CW` | Nipple-region to chestwall support direction | OK | 45.637 | 22.548 | 19.273 | 11.563 | +0.12% | -0.34% |
| `COOP_DENSE` | Dense skin-to-glandular support selection | OK | 44.994 | 22.442 | 19.069 | 11.650 | -1.29% | +0.41% |

Main interpretation for Results: in this current postprocessed set, Cooper-like support changes the global peak displacement only weakly, roughly within -1.4% to +0.1% compared with the no-Cooper reference. The largest reductions occur for the skin-to-glandular support variants. Keep this descriptive; do not claim anatomical validation.

### Cooper source folders

- `CTRL_REF`: `runs/comsol_runs/geometry_stage5/outputs/output_stage5_scout_simple_gland_volskin_15mm_femke_skin_soft_interior_125g_solve_only_preview/solve`
- `COOP_MILD`: `runs/comsol_runs/geometry_stage5/outputs/output_stage5_scout_simple_gland_cooper_mild_volskin_femke_skin_soft_interior_125g_solve_only_preview/solve`
- `COOP_G2S_A006`: `runs/comsol_runs/geometry_stage5/outputs/output_stage5_scout_simple_gland_cooper_g2skin_area006_volskin_femke_skin_soft_interior_125g/solve`
- `COOP_G2S_A012`: `runs/comsol_runs/geometry_stage5/outputs/output_stage5_scout_simple_gland_cooper_g2skin_area012_volskin_femke_skin_soft_interior_125g/solve`
- `COOP_N2CW`: `runs/comsol_runs/geometry_stage5/outputs/output_stage5_scout_simple_gland_cooper_nipple_to_chestwall_volskin_femke_skin_soft_interior_125g/solve`
- `COOP_DENSE`: `runs/comsol_runs/geometry_stage5/outputs/output_stage5_scout_simple_gland_cooper_dense_network_volskin_femke_skin_soft_interior_125g/solve`

## Tumor cases

Reference for the surface/global comparison is again `CTRL_REF`, but be careful: the tumor cases are stage 6 and not necessarily an exact one-to-one no-tumor paired geometry unless the case setup confirms this.

| Report label | Case meaning | Status | Peak disp. breast (mm) | Mean disp. breast max (mm) | Peak VM stress (kPa) | Review surface max disp. (mm) | Tumor volume (ml) | Peak tumor disp. (mm) | Peak tumor VM stress (kPa) |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|
| `TUM_UO_MED` | Medium upper-outer tumor case | OK | 45.486 | 22.536 | 19.275 | 11.674 | 0.897 | 37.294 | 13.070 |
| `TUM_UO_MED_RG_VS` | Medium upper-outer tumor, realistic gland, volumetric skin, soft interior, xoffset055 | Global quick only | 10.916 | 5.903 | n/a | n/a | 0.912 | 8.496 | n/a |
| `TUM_LC_HARD100_COUPLED` | Large central hard tumor, material-coupled route | Not report-ready | n/a | n/a | n/a | n/a | n/a | n/a | n/a |
| `TUM_LC_HARD100_VALIDATED` | Large central hard tumor, from validated build | Diagnostic only | ~0 | ~0 | ~0 | ~0 | 4.180 | ~0 | ~0 |

Main interpretation for Results: use `TUM_UO_MED` for the current tumor-overlay sensitivity result when stress and surface metrics are needed, because it has valid surface/global and internal tumor metrics. Use `TUM_UO_MED_RG_VS` as the newer matched realistic-gland/volumetric-skin displacement-only tumor result. The large central hard tumor runs should only be mentioned as diagnostic limitations if needed, because they do not currently provide a physically useful dynamic response.

### Tumor source folders

- `TUM_UO_MED`: `runs/comsol_runs/geometry_stage6/outputs/output_stage6_scout_simple_gland_medium_upper_outer_softskin_soft_interior_125g/solve`
- `TUM_UO_MED_RG_VS`: `runs/comsol_runs/geometry_stage6/outputs/output_stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview/solve`
- `TUM_LC_HARD100_COUPLED`: `runs/comsol_runs/geometry_stage6/outputs/output_stage6_tumor_large_central_hard100kpa_xoffset055_125g_volumetric_skin_soft_interior_material_coupled_solve_only_preview/solve`
- `TUM_LC_HARD100_VALIDATED`: `runs/comsol_runs/geometry_stage6/outputs/output_stage6_tumor_large_central_hard100kpa_xoffset055_125g_volumetric_skin_soft_interior_material_coupled_from_validated_build_solve_only_preview/solve`

### New Queue A global quick outputs

- Summary JSON: `runs/comsol_runs/geometry_stage6/outputs/output_stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview/solve/stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview_global_summary.json`
- Time series CSV: `runs/comsol_runs/geometry_stage6/outputs/output_stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview/solve/stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview_global_time_series.csv`
- Limitation: `vm_*`, surface and landmark columns are intentionally `NaN` or absent for this case; this output is displacement-only.

## Suggested Results tables

1. Overview table of completed postprocessed cases:
   - Columns: report label, model route, comparison case, postprocess mode, output status, use in report.
   - Source: this inventory plus the source folders above.

2. Cooper-support sensitivity table:
   - Columns: label, peak displacement, percentage change, peak VM stress, percentage change, review surface displacement.
   - Source: `*_ews_surface_time_series.csv` plus `*_ews_surface_summary.json`.

3. Tumor-overlay sensitivity table:
   - Columns: label, surface/global response, tumor volume, tumor peak displacement, tumor peak VM stress, status.
   - Source: `*_ews_surface_time_series.csv`, `*_ews_surface_summary.json`, `*_internal_tumor_time_series.csv`, `*_internal_tumor_summary.json`.

## Suggested Results figures

1. Cooper line plot:
   - Plot `disp_max_mm` over `time_s` for `CTRL_REF`, `COOP_G2S_A006`, `COOP_G2S_A012`, `COOP_N2CW` and `COOP_DENSE`.
   - Optional second panel: `vm_max_pa / 1000` over `time_s`.

2. Cooper bar plot:
   - Bar chart of percentage change in peak displacement and peak VM stress relative to `CTRL_REF`.

3. Tumor response figure:
   - For `TUM_UO_MED`, plot breast peak displacement and tumor peak displacement over time.
   - Source: `stage6_scout_simple_gland_medium_upper_outer_softskin_soft_interior_125g_solve_internal_tumor_time_series.csv`.

4. Surface/landmark figure:
   - If needed for EWS context, use `*_ews_surface_landmark_displacement.csv` to show landmark displacement over time or a compact landmark peak table.
   - Avoid claiming full dense surface-map comparison unless the plotted data source is explicitly a surface field export.

## Caveats for report wording

- Do not describe Cooper support as a reconstructed anatomical ligament network. It is a simplified restoring-traction approximation.
- Do not overstate the tumor findings. The currently useful tumor case is one medium upper-outer tumor scenario; the hard-central tumor routes are not yet robust Results evidence.
- Do not call the `ews_surface_time_series.csv` a dense surface field export. It mainly contains time-dependent global response metrics. Surface-specific review metrics are stored in the summary JSON.
- Avoid report labels that include internal development names such as `femke`. Use the clean labels in this file.

## Queue C skin/material postprocess update

Queue C was completed after the initial inventory was written. It adds `ews_surface` postprocess outputs for additional skin/material comparison cases. The skipped case below did not fail during postprocessing; it was skipped because its required solved `.mph` file was not present.

Reference case for the percentages: `CTRL_REF_15mm_softskin`.

| Report label | Case meaning | Status | Peak disp. breast (mm) | Mean disp. breast (mm) | Peak VM stress (kPa) | Review surface max disp. (mm) | Peak disp. change vs ref. | Review surface change vs ref. |
|---|---|---:|---:|---:|---:|---:|---:|---:|
| `CTRL_REF_15mm_softskin` | 1.5 mm soft volumetric skin reference | Existing OK | 45.582 | 22.554 | 19.283 | 11.603 | 0.0% | 0.0% |
| `NO_SKIN` | No volumetric skin | OK | 61.531 | 27.556 | 3.127 | 9.038 | +35.0% | -22.1% |
| `SKIN_01mm_SOFT` | 0.1 mm soft volumetric skin | OK | 60.032 | 27.200 | 44.583 | 9.413 | +31.7% | -18.9% |
| `SKIN_01mm_MID088` | 0.1 mm intermediate skin stiffness | Skipped, missing result MPH | n/a | n/a | n/a | n/a | n/a | n/a |
| `SKIN_15mm_MID088` | 1.5 mm intermediate skin stiffness | OK | 25.490 | 13.242 | 42.897 | 10.375 | -44.1% | -10.6% |
| `SKIN_15mm_STIFF` | 1.5 mm stiff volumetric skin | OK | 11.144 | 5.704 | 69.671 | 3.362 | -75.6% | -71.0% |

Main interpretation for Results: skin thickness/stiffness produces a strong global displacement effect compared with the reference. Removing skin or using a very thin soft skin increases the global peak displacement by roughly `32-35%`. Increasing the 1.5 mm skin stiffness reduces peak displacement by about `44%` for the intermediate skin case and about `76%` for the stiff skin case. The stress values increase strongly for stiffer skin routes, so phrase this as a stress-concentration/model-response comparison rather than as a validated physiological stress value.
