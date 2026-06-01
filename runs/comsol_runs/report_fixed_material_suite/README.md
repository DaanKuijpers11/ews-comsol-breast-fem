# Report fixed-material COMSOL suite

This suite is intended for fair report-ready comparison runs without changing material parameters between cases.

Material values are taken from:

`docs/report_notes/comsol_pipeline/model_justification/material_parameter_recommendations.md`

Main comparison:

- `report_baseline_fixed_materials_order1.toml`
- `report_stage5b_fixed_materials_order1.toml`

Both main cases use:

- `ls = 0.005`
- `density = 125`
- `order = 1`
- fixed literature-based skin, adipose, glandular, and chestwall parameters
- identical geometry and time stepping
- automatic COMSOL result plots and screenshot export enabled
- automatic screenshots use the updated front-camera view and semi-transparent result surfaces when supported by COMSOL

Optional sensitivity comparison:

- `sensitivity_baseline_fixed_materials_order2.toml`
- `sensitivity_stage5a_fixed_materials_order2.toml`
- `sensitivity_stage5b_fixed_materials_order2.toml`
- `sensitivity_stage5c_fixed_materials_order2.toml`

These use the same `ls = 0.005` and `density = 125`, but `order = 2`.

## Night run: main report comparison

Run from the repository root:

```powershell
& 'C:\Users\20223231\.conda\envs\ews-fem\python.exe' -m ews_fem_pipeline_comsol run runs\comsol_runs\report_fixed_material_suite\report_baseline_fixed_materials_order1.toml runs\comsol_runs\report_fixed_material_suite\report_stage5b_fixed_materials_order1.toml
```

Expected output folders:

- `runs/comsol_runs/report_fixed_material_suite/outputs/output_report_baseline_fixed_materials_order1`
- `runs/comsol_runs/report_fixed_material_suite/outputs/output_report_stage5b_fixed_materials_order1`

Automatic screenshots should be written inside each case as:

- `plot_screens_auto`

## Optional night run: order 2 sensitivity

Only run this after the order 1 report comparison is finished and checked:

```powershell
& 'C:\Users\20223231\.conda\envs\ews-fem\python.exe' -m ews_fem_pipeline_comsol run runs\comsol_runs\report_fixed_material_suite\sensitivity_baseline_fixed_materials_order2.toml runs\comsol_runs\report_fixed_material_suite\sensitivity_stage5a_fixed_materials_order2.toml runs\comsol_runs\report_fixed_material_suite\sensitivity_stage5b_fixed_materials_order2.toml runs\comsol_runs\report_fixed_material_suite\sensitivity_stage5c_fixed_materials_order2.toml
```

## Metric comparison after the main run

```powershell
& 'C:\Users\20223231\.conda\envs\ews-fem\python.exe' -m ews_fem_pipeline_comsol compare-metrics runs\comsol_runs\report_fixed_material_suite\outputs\output_report_baseline_fixed_materials_order1\solve\report_baseline_fixed_materials_order1_metrics.json runs\comsol_runs\report_fixed_material_suite\outputs\output_report_stage5b_fixed_materials_order1\solve\report_stage5b_fixed_materials_order1_metrics.json --baseline report_baseline_fixed_materials_order1
```

## Metric comparison after the order 2 sensitivity run

```powershell
& 'C:\Users\20223231\.conda\envs\ews-fem\python.exe' -m ews_fem_pipeline_comsol compare-metrics runs\comsol_runs\report_fixed_material_suite\outputs\output_sensitivity_baseline_fixed_materials_order2\solve\sensitivity_baseline_fixed_materials_order2_metrics.json runs\comsol_runs\report_fixed_material_suite\outputs\output_sensitivity_stage5a_fixed_materials_order2\solve\sensitivity_stage5a_fixed_materials_order2_metrics.json runs\comsol_runs\report_fixed_material_suite\outputs\output_sensitivity_stage5b_fixed_materials_order2\solve\sensitivity_stage5b_fixed_materials_order2_metrics.json runs\comsol_runs\report_fixed_material_suite\outputs\output_sensitivity_stage5c_fixed_materials_order2\solve\sensitivity_stage5c_fixed_materials_order2_metrics.json --baseline sensitivity_baseline_fixed_materials_order2
```
