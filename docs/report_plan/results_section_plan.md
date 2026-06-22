# Results Section Plan

This plan defines a concrete, evidence-based structure for the Results chapter of the COMSOL EWS breast FEM report. It does not rewrite the report text. It identifies which existing repository files can support each subsection, which tables or figures can be used directly, and which report-ready overview tables still need to be assembled.

## Git And Repository Status

Local check date: 2026-06-22.

Important distinction: I can verify whether files are present locally and whether they are tracked by git. I cannot verify from here whether the latest local state has actually been pushed to GitHub.

Current status:

| Item | Local status | Git status | Meaning for GitHub |
|---|---|---|---|
| `analysis_output/comsol_pipeline` | Present | Tracked by git for many derived summaries/plots | Main result tables and many derived plots are already suitable for GitHub, assuming committed/pushed. |
| `docs/report_notes/comsol_pipeline` | Present | Tracked by git | Method/status notes that support interpretation are available for GitHub. |
| `runs/comsol_runs/**/*.toml` and README files | Present | Intended to be tracked | TOML provenance is available; generated output folders are mostly ignored by design. |
| `runs/**/outputs` | Present locally | Ignored by `.gitignore` | Heavy/generated COMSOL outputs are not meant to be on GitHub. Use compact tables/plots instead. |
| `docs/Internship_report_Daan_Kuijpers` | Present locally | Currently untracked | The new report folder and its figures are not yet in GitHub unless you add/commit/push them. |
| old `docs/Traineeship report - Daan_Kuijpers` | Deleted locally | Tracked as deleted | The repo currently sees this as a folder replacement/rename situation. Add the new folder before committing. |
| `docs/report_plan/results_section_plan.md` | Created by this plan | New/untracked until added | Add this file if you want the plan in GitHub. |

Conclusion: the compact result evidence is mostly available in tracked repository locations, especially under `analysis_output/comsol_pipeline` and `docs/report_notes/comsol_pipeline`. The main missing GitHub step is that the new report folder `docs/Internship_report_Daan_Kuijpers` is currently untracked.

## Recommended Results Structure

Use the thematic structure already chosen:

1. Overview of completed model variants
2. Reference model response
3. Effect of material-parameter variation
4. Effect of volumetric skin representation
5. Effect of Cooper-like support
6. Effect of dynamic loading route
7. Tumor-overlay sensitivity
8. Summary of main result trends

Keep the chapter evidence-tiered: solved/postprocessed results first, manual scout results second, invalid/build-only results only as status.

## Results Subsection Plan

### 1. Overview Of Completed Model Variants

Purpose: open the Results with a clear status table so the reader knows which outputs are solved/postprocessed, manual scout, build-only, failed, or diagnostic only.

Use existing sources:

| Source | Use |
|---|---|
| `analysis_output/comsol_pipeline/tier1_comparison/tables/case_definition_summary.md` | Explains what Stage 1-5 cases actually change. |
| `analysis_output/comsol_pipeline/tier1_comparison/tables/summary_results.csv` | Main compact Tier 1 solved/postprocessed metrics. |
| `analysis_output/comsol_pipeline/tier1_comparison_without_stage1/tables/summary_results.csv` | Better anatomical comparison excluding Stage 1. |
| `analysis_output/comsol_pipeline/stage5_cooper/tables/summary_results.csv` | Shows no-Cooper solved and default Cooper failed early. |
| `analysis_output/comsol_pipeline/stage5_dynamic_amplitude_scout/tables/summary_results.csv` | Shows 0.25g/0.50g solved and 0.75g failed. |
| `analysis_output/comsol_pipeline/stage6_fast_tumor_screening/tables/summary_results.csv` | Shows Stage 6 fast tumor screening invalid/NaN status. |
| `analysis_output/comsol_pipeline/manual_postprocess/tables/stage5_manual_postprocess_summary.csv` | Manual skin/material scout results. |
| `analysis_output/comsol_pipeline/manual_postprocess/comparison_figures/stage5_scout_cooper_support_summary.md` | Manual Cooper simple-gland scout. |

Report table to make:

`Table R1. Overview of completed model variants and evidence status.`

Columns:

| Column | Content |
|---|---|
| Result group | Reference, material, skin, Cooper, loading, tumor |
| Control/comparison case | Which baseline is used |
| Variants | Tested variants |
| Output status | solved/postprocessed, manual scout, failed, invalid, build-only |
| Main usable metrics | Displacement, signed w, stress, tumor mask, etc. |
| Report interpretation | Main result, scout only, or limitation |
| Source table | Path to CSV/MD source |

Safe claim: the model-development workflow produced multiple traceable COMSOL variants, but not all variants have equal evidence status.

Avoid: presenting build-only/failed/tumor-screening rows as final quantitative results.

### 2. Reference Model Response

Purpose: report the response of the current best anatomical/reference route before sensitivity comparisons.

Recommended reference:

Use `Stage 5 no-Cooper control` conceptually as the final no-support control, because it is the control for Cooper and tumor routes. However, note that the compact summary exists in `analysis_output`, while the corresponding `runs/comsol_runs/geometry_stage5/outputs/output_stage5_reference_no_cooper_xoffset055_025g_preview` folder was not found in the current local output tree. If exact source provenance is needed, use Stage 3/4 reference or regenerate/postprocess Stage 5 no-Cooper.

Use existing sources:

| Source | Use |
|---|---|
| `analysis_output/comsol_pipeline/tier1_comparison_without_stage1/tables/summary_results.csv` | Main anatomical comparison: Stage 2-5, all solved to 2.2 s. |
| `analysis_output/comsol_pipeline/tier1_comparison/tables/summary_results.csv` | Includes Stage 1 but should be used cautiously. |
| `analysis_output/comsol_pipeline/tier1_comparison_without_stage1/figures/surface_signed_vertical_response.png` | Surface signed vertical response for Stage 2-5. |
| `analysis_output/comsol_pipeline/tier1_comparison_without_stage1/figures/surface_displacement_statistics.png` | Surface displacement statistics. |
| `analysis_output/comsol_pipeline/tier1_comparison_without_stage1/figures/stress_mean_max_response.png` | Stress comparison. |
| `docs/Internship_report_Daan_Kuijpers/Figures/comsol_contact_sheets/stage3_glandular_fraction_contact_sheet.png` | Existing report figure for glandular/reference context. |
| `docs/Internship_report_Daan_Kuijpers/Figures/comsol_contact_sheets/stage5_cooper_contact_sheet.png` | Existing report figure for Stage 5/no-Cooper context, but check caption carefully. |

Report table to make:

`Table R2. Reference model response metrics.`

Use only a compact set:

- breast volume;
- glandular fraction;
- review average displacement;
- review surface signed vertical displacement;
- review mean von Mises stress;
- review max von Mises stress;
- solve end time/status.

Recommended values can be taken from `tier1_comparison_without_stage1/tables/summary_results.csv`.

Safe claim: the selected reference route has approximately 585 mL volume, approximately 24.3% glandular fraction after realistic glandular refinement, and a small but measurable dynamic response under the 0.25g input.

Avoid: using Stage 1 as the final reference response, because Stage 1 has different volume and simpler geometry.

### 3. Effect Of Material-Parameter Variation

Purpose: show how changing interior stiffness/material settings changes the response, especially after volumetric skin is included.

Use existing sources:

| Source | Use |
|---|---|
| `analysis_output/comsol_pipeline/manual_postprocess/tables/stage5_manual_postprocess_summary.csv` | Main manual Stage 5 material/skin peak table. |
| `analysis_output/comsol_pipeline/manual_postprocess/tables/manual_postprocess_summary.csv` | Extended manual table including extra diagnostic material/tumor rows. |
| `analysis_output/comsol_pipeline/manual_postprocess/comparison_figures/stage5_scout_skin_material_summary.md` | Skin/material scout summary with percentage changes. |
| `analysis_output/comsol_pipeline/manual_postprocess/surface_comparison/stage5_full_skin_material_surface/surface_summary.md` | Outer-surface comparison for soft interior versus 88 kPa skin case. |
| `analysis_output/comsol_pipeline/manual_postprocess/comparison_figures/stage5_manual_peak_comparison.png` | Existing source figure. |
| `analysis_output/comsol_pipeline/manual_postprocess/comparison_figures/stage5_manual_max_displacement_timeseries.png` | Existing source figure. |
| `docs/Internship_report_Daan_Kuijpers/Figures/stage5_manual_postprocess/stage5_manual_peak_comparison.png` | Already copied into report folder. |
| `docs/Internship_report_Daan_Kuijpers/Figures/stage5_manual_postprocess/stage5_manual_max_displacement_timeseries.png` | Already copied into report folder. |

Report table to make:

`Table R3. Material and skin scout peak response.`

Recommended rows:

- No skin, 1.00g;
- No skin, 1.25g;
- Volumetric skin, 1.25g;
- Volumetric skin + soft interior, 1.25g;
- optional diagnostic rows: 88 kPa skin and 0.1 mm soft skin only if needed.

Recommended columns:

- peak average displacement;
- peak maximum displacement;
- peak average VM;
- peak maximum VM;
- evidence status: manual Derived Values.

Safe claim: softer internal tissue partly recovers displacement after the volumetric skin layer reduces motion.

Avoid: treating the soft-interior scout as calibrated tissue truth.

### 4. Effect Of Volumetric Skin Representation

Purpose: isolate the effect of adding a volumetric skin layer.

This can be a separate subsection, but it uses the same source data as material-parameter variation. If the Results becomes long, combine Sections 3 and 4 under one subsection such as `Effect of skin and material settings`.

Use existing sources:

| Source | Use |
|---|---|
| `analysis_output/comsol_pipeline/manual_postprocess/tables/stage5_manual_postprocess_summary.csv` | No-skin versus volumetric-skin comparison. |
| `analysis_output/comsol_pipeline/manual_postprocess/comparison_figures/stage5_scout_skin_material_summary.md` | Contains percent changes for multiple skin/material settings. |
| `analysis_output/comsol_pipeline/manual_postprocess/surface_comparison/stage5_scouts_surface/stage5_surface_summary.md` | Surface-level response for no skin, skin88, softskin. |
| `docs/Internship_report_Daan_Kuijpers/Figures/model_pictures/stage5_cooper/stage5_nocooper_125g_vonMises_bovenaanzicht.png` | Stress hotspot supporting image if used. |
| `docs/Internship_report_Daan_Kuijpers/Figures/model_pictures/stage5_cooper/stage5_nocooper_125g_soft_interior_materials_vonMises_bovenaanzicht.png` | Soft-interior stress image if used. |

Report table to make:

If not merged with Table R3, make:

`Table R4. Volumetric skin effect relative to no-skin control.`

Recommended rows:

- No skin, 1.25g;
- 1.5 mm volumetric skin, 1.25g;
- 1.5 mm volumetric skin + soft interior, 1.25g.

Recommended columns:

- peak average displacement;
- peak maximum displacement;
- percent change in displacement;
- peak max VM;
- note that max VM is a local/interface hotspot diagnostic.

Safe claim: the volumetric skin layer strongly reduces displacement amplitude in the current scout and changes local stress distribution.

Avoid: using max VM alone as a global material-quality metric.

### 5. Effect Of Cooper-Like Support

Purpose: report Cooper-support sensitivity carefully: current final/default Cooper route is not a solved report-ready effect, but manual simple-gland scouts show small global changes.

Use existing sources:

| Source | Use |
|---|---|
| `analysis_output/comsol_pipeline/stage5_cooper/tables/summary_results.csv` | Current route: no-Cooper solved; default 5B failed early. |
| `analysis_output/comsol_pipeline/stage5_cooper/tables/case_status.md` | Status if needed. |
| `analysis_output/comsol_pipeline/manual_postprocess/comparison_figures/stage5_scout_cooper_support_summary.md` | Manual simple-gland Cooper scout summary. |
| `analysis_output/comsol_pipeline/manual_postprocess/comparison_figures/stage5_scout_cooper_support_summary.csv` | Same data in CSV form. |
| `docs/report_notes/comsol_pipeline/model_justification/cooper_ligament_support_scouts_2026-06-11.md` | Interpretation and caution. |
| `docs/Internship_report_Daan_Kuijpers/Figures/model_pictures/stage5_cooper/stage5_cooper_variants_schematic.png` | Existing schematic figure. |
| `analysis_output/comsol_pipeline/stage5_cooper/figures/contact_sheet.png` | Existing generated contact sheet. |

Report table to make:

`Table R5. Cooper-support result status and scout response.`

Recommended split:

- Part A: current realistic route status:
  - No-Cooper xoffset055 0.25g solved.
  - 5B default xoffset055 failed early at about `2.15e-6 s`.
- Part B: simple-gland manual scout:
  - No Cooper baseline;
  - glandular-to-skin area 0.03/0.06/0.12;
  - nipple-to-chest area 0.03;
  - dense network area 0.03.

Safe claim: in the simple-gland scout, the implemented Cooper-like support produced only small global changes in peak displacement and stress; the final realistic Cooper support effect is not yet proven because the current default dynamic case failed early.

Avoid: saying anatomical Cooper ligaments have no effect.

### 6. Effect Of Dynamic Loading Route

Purpose: separate the fixed-support acceleration amplitude effect from the prescribed support-displacement scout.

Use existing sources:

| Source | Use |
|---|---|
| `analysis_output/comsol_pipeline/stage5_dynamic_amplitude_scout/tables/summary_results.csv` | 0.25g, 0.50g and failed 0.75g status/metrics. |
| `analysis_output/comsol_pipeline/stage5_dynamic_amplitude_scout/figures/surface_signed_vertical_response.png` | Good figure for signed vertical response. |
| `analysis_output/comsol_pipeline/stage5_dynamic_amplitude_scout/figures/surface_displacement_statistics.png` | Displacement statistics. |
| `analysis_output/comsol_pipeline/stage5_dynamic_amplitude_scout/figures/stress_mean_max_response.png` | Stress response. |
| `analysis_output/comsol_pipeline/manual_postprocess/tables/stage51_motion/40mm_060s/avg_skin_outer.csv` | Prescribed support scout raw average surface response. |
| `analysis_output/comsol_pipeline/manual_postprocess/tables/stage51_motion/40mm_060s/max_skin_outer.csv` | Prescribed support scout raw max surface response. |
| `analysis_output/comsol_pipeline/manual_postprocess/tables/stage51_motion/40mm_060s/avg_nipple_landmark.csv` | Prescribed support scout raw nipple response. |
| `analysis_output/comsol_pipeline/manual_postprocess/tables/stage51_motion/150mm_120s/avg_surface_skin_outer.csv` | Larger prescribed-support diagnostic scout. |
| `docs/report_notes/comsol_pipeline/model_justification/stage1_025g_dynamic_motion_interpretation.md` | Interpretation and support-motion caveats. |

Report table to make:

`Table R6. Dynamic loading route and amplitude response.`

Recommended rows:

- fixed-support acceleration 0.25g;
- fixed-support acceleration 0.50g;
- fixed-support acceleration 0.75g failed;
- prescribed support 40 mm / 0.60 s scout;
- optional prescribed support 150 mm / 1.20 s diagnostic scout.

Recommended columns:

- route;
- amplitude/duration;
- solve/manual status;
- review average displacement;
- review surface signed w;
- support-relative surface response where available;
- stress metric;
- interpretation.

Values still to assemble:

- For the 40 mm prescribed-support scout, compute a compact row from the raw TSV-like CSV files:
  - average outer-skin support-relative `w`: max absolute approximately 8.81 mm;
  - maximum outer-skin support-relative `w`: max absolute approximately 15.70 mm;
  - average nipple support-relative `w`: max absolute approximately 15.51 mm.
- For the 150 mm / 1.20 s diagnostic scout:
  - average absolute `w` follows the imposed motion up to about 143.9 mm;
  - average support-relative `w` reaches about 11.4 mm;
  - maximum support-relative `w` reaches about 20.6 mm.

Safe claim: 0.50g increases signed surface response relative to 0.25g, while 0.75g did not produce a valid solved result in the current scout. Prescribed support displacement is promising as an interpretable motion scout but is not the final validated loading route.

Avoid: calling the prescribed-support scout a validated patient/platform experiment.

### 7. Tumor-Overlay Sensitivity

Purpose: report what is currently available for tumor overlay without overclaiming. This is primarily a screening/scout result, not a final tumor-detectability result.

Use existing sources:

| Source | Use |
|---|---|
| `analysis_output/comsol_pipeline/stage6_fast_tumor_screening/tables/summary_results.csv` | Shows fast tumor screening cases are invalid/failed/NaN; use as status evidence. |
| `analysis_output/comsol_pipeline/stage6_fast_tumor_screening/tables/case_status.md` | Status table if needed. |
| `analysis_output/comsol_pipeline/manual_postprocess/surface_comparison/simple_gland_softskin_tumor/simple_gland_tumor_summary.md` | Simple-gland tumor/control manual scout summary. |
| `analysis_output/comsol_pipeline/manual_postprocess/surface_comparison/simple_gland_softskin_tumor/global_delta_summary.csv` | Compact tumor minus control global deltas. |
| `analysis_output/comsol_pipeline/manual_postprocess/surface_comparison/simple_gland_softskin_tumor/surface_summary.csv` | Surface-level tumor/control comparison. |
| `analysis_output/comsol_pipeline/manual_postprocess/surface_comparison/stage5_soft_interior_vs_stage6_hard100kPa/surface_delta_summary.csv` | Surface delta for hard100 kPa scout. |
| `analysis_output/comsol_pipeline/manual_postprocess/surface_comparison/stage5_soft_interior_vs_stage6_medium_upper_surface/surface_delta_summary.csv` | Surface delta for medium upper-outer surface scout. |
| `analysis_output/comsol_pipeline/manual_postprocess/comparison_figures/stage6_tumor_*.png` | Diagnostic time-series/bar figures; use carefully. |
| `docs/Internship_report_Daan_Kuijpers/Figures/model_pictures/stage6_tumor/*.png` | Tumor placement figures for geometry/placement intent. |
| `docs/report_notes/comsol_pipeline/model_justification/stage6_tumor_lesion_plan_2026-05-26.md` | Cautions, retained route, and interpretation. |

Report table to make:

`Table R7. Tumor-overlay screening and diagnostic comparisons.`

Recommended rows:

- Fast tumor screening set: status invalid/failed; no quantitative tumor-mask conclusion.
- Simple-gland softskin tumor/control: manual scout, small global/surface differences.
- Hard 100 kPa large central scout: surface delta diagnostic.
- Medium upper-outer surface-proximal scout: surface delta diagnostic.

Recommended columns:

- tumor case;
- control;
- tumor size/location/stiffness target;
- output status;
- global displacement delta;
- surface displacement delta;
- tumor-mask metric availability;
- interpretation.

Safe claim: the analytic tumor-overlay route and placement figures are available, and diagnostic/manual scouts suggest that global aggregate surface changes are small in the tested cases.

Avoid: claiming tumor detectability has been demonstrated, or claiming absence of a tumor signal in the final model.

### 8. Summary Of Main Result Trends

Purpose: close the Results with a compact trend table, not a discussion.

Report table to make:

`Table R8. Summary of main result trends and evidence strength.`

Recommended columns:

- model component;
- strongest observed trend;
- evidence source;
- evidence level;
- limitation.

Suggested rows:

| Component | Trend | Evidence level |
|---|---|---|
| Reference anatomical route | Stable 585 mL reference, modest 0.25g response | solved/postprocessed |
| Volumetric skin | Strongly reduced displacement in scout; local stress hotspots increased | manual scout |
| Soft interior material | Partly recovered displacement with volumetric skin | manual scout |
| Cooper-like support | Current final route not solved; simple scout showed small global effect | failed + manual scout |
| Dynamic amplitude | 0.50g solved; 0.75g failed; prescribed support route diagnostic | mixed |
| Tumor overlay | Geometry/scout route exists; quantitative final tumor conclusion not ready | diagnostic/scout |

Avoid adding new interpretation here. Save reasons, implications, and future work for Discussion.

## Existing Figures To Use Or Copy

Figures already in the report folder:

| Figure path | Recommended use |
|---|---|
| `docs/Internship_report_Daan_Kuijpers/Figures/comsol_contact_sheets/stage1_baseline_contact_sheet.png` | Only if Stage 1 motion sanity is shown. |
| `docs/Internship_report_Daan_Kuijpers/Figures/comsol_contact_sheets/stage2_chestwall_contact_sheet.png` | Optional overview of selected chestwall route. |
| `docs/Internship_report_Daan_Kuijpers/Figures/comsol_contact_sheets/stage3_glandular_fraction_contact_sheet.png` | Reference/anatomical response if using contact sheets. |
| `docs/Internship_report_Daan_Kuijpers/Figures/comsol_contact_sheets/stage5_cooper_contact_sheet.png` | Cooper/status context, but caption must mention failed/default status carefully. |
| `docs/Internship_report_Daan_Kuijpers/Figures/stage5_manual_postprocess/stage5_manual_peak_comparison.png` | Strong candidate for skin/material results. |
| `docs/Internship_report_Daan_Kuijpers/Figures/stage5_manual_postprocess/stage5_manual_max_displacement_timeseries.png` | Strong candidate for skin/material time-series. |
| `docs/Internship_report_Daan_Kuijpers/Figures/model_pictures/stage5_cooper/stage5_cooper_variants_schematic.png` | Cooper setup/status figure. |
| `docs/Internship_report_Daan_Kuijpers/Figures/model_pictures/stage6_tumor/*.png` | Tumor placement figure, not quantitative result by itself. |

Generated figures present under `analysis_output/comsol_pipeline` but not necessarily copied into the report figure folder:

| Figure path | Recommended use |
|---|---|
| `analysis_output/comsol_pipeline/tier1_comparison_without_stage1/figures/surface_signed_vertical_response.png` | Good reference response figure. Copy to report folder if used. |
| `analysis_output/comsol_pipeline/tier1_comparison_without_stage1/figures/surface_displacement_statistics.png` | Good reference displacement-statistics figure. |
| `analysis_output/comsol_pipeline/tier1_comparison_without_stage1/figures/stress_mean_max_response.png` | Good reference stress figure. |
| `analysis_output/comsol_pipeline/stage5_dynamic_amplitude_scout/figures/surface_signed_vertical_response.png` | Best dynamic-amplitude figure. |
| `analysis_output/comsol_pipeline/manual_postprocess/surface_comparison/stage5_soft_interior_vs_stage6_medium_upper_surface/delta_w_surface_xz_peak_t1.445.png` | Tumor surface-delta diagnostic only. |
| `analysis_output/comsol_pipeline/manual_postprocess/surface_comparison/stage5_soft_interior_vs_stage6_medium_upper_surface/delta_vm_surface_xz_peak_t1.445.png` | Tumor stress-delta diagnostic only. |

If a figure from `analysis_output` is used in the LaTeX report, copy it into `docs/Internship_report_Daan_Kuijpers/Figures/...` and cite the source path in a comment or appendix/source index.

## Tables Still To Assemble For The Report

These are not yet present as final LaTeX tables in `Chapters/4.Results.tex`.

| New table | Existing source files | Required action |
|---|---|---|
| R1 Evidence/status overview | Multiple `summary_results.csv`, `case_status.md`, manual scout summaries | Manually assemble one compact status table. |
| R2 Reference model response | `tier1_comparison_without_stage1/tables/summary_results.csv` | Extract Stage 3/4/5 reference row(s), keep compact. |
| R3 Material/skin peak scout | `manual_postprocess/tables/stage5_manual_postprocess_summary.csv` | Convert to LaTeX, mark as manual Derived Values. |
| R4 Volumetric skin percentage effect | `stage5_scout_skin_material_summary.md/csv` | Optional if not merged with R3. |
| R5 Cooper status/scout response | `stage5_cooper/tables/summary_results.csv` and `stage5_scout_cooper_support_summary.md` | Combine failed-current-route status with simple-gland scout. |
| R6 Dynamic route summary | `stage5_dynamic_amplitude_scout/tables/summary_results.csv` and `stage51_motion/*/*.csv` | Add computed support-relative prescribed-support rows. |
| R7 Tumor screening/scout summary | `stage6_fast_tumor_screening/tables/summary_results.csv`, tumor manual summaries/deltas | Combine status and diagnostic deltas; do not overclaim. |
| R8 Main trends | All above | Manually summarize trends and evidence level. |

## Minimum Figure Set

To keep the chapter compact, use about 5-6 main figures:

1. Reference response figure: surface signed vertical response or contact sheet.
2. Reference stress/displacement contact or compact plot.
3. Skin/material peak comparison.
4. Skin/material time-series.
5. Dynamic loading response figure.
6. Tumor placement or tumor diagnostic delta figure.

Optional figures:

- Cooper variants schematic, if Cooper is discussed visually.
- Tumor placement montage, if the tumor subsection needs geometry context.

Avoid overloading the chapter with all contact sheets; place provenance contact sheets in appendix if needed.

## Claims That Are Supported

- The current COMSOL pipeline produces reproducible case variants and compact evaluation outputs.
- The Stage 2-5 anatomical reference route reaches a stable 585 mL scale in the available Tier 1 summaries.
- The realistic glandular reference increases glandular fraction to about 24.3% while keeping volume close to the selected reference.
- Volumetric skin and internal material settings strongly affect displacement amplitude in the manual Stage 5 scouts.
- The current Cooper route is not yet a solved final effect; simple-gland Cooper scouts show only small global changes.
- Dynamic amplitude and prescribed support motion are useful sensitivity/scout routes, but not validated motion experiments.
- Tumor overlay has geometry/scout evidence, but no final validated tumor-detectability result.

## Claims To Avoid

- Do not claim a validated Early Warning Scan tumor detector.
- Do not claim patient-specific anatomy.
- Do not claim tumor detectability is proven or absent.
- Do not claim anatomical Cooper-ligament reconstruction.
- Do not treat Stage 1 as the final anatomical baseline.
- Do not interpret maximum von Mises stress alone as global mechanical response.
- Do not describe ignored/failed/NaN Stage 6 fast screening as quantitative tumor evidence.

## Immediate Next Steps

1. Decide whether `Reference model response` uses Stage 3 realistic reference, Stage 4 realistic reference, or Stage 5 no-Cooper control as the named reference.
2. Assemble Table R1 first; this prevents overclaiming later.
3. Copy any selected `analysis_output` figures into `docs/Internship_report_Daan_Kuijpers/Figures/results/`.
4. Add the new report folder to git if it should be on GitHub:
   - `docs/Internship_report_Daan_Kuijpers`
   - `docs/report_plan`
5. Keep generated heavy COMSOL output folders out of GitHub; use compact CSV/MD/PNG summaries instead.

