# Clean COMSOL Evaluation Output

This folder is the proposed clean replacement for the old mixed `analysis_output/figures` tree.
It contains figures and tables generated from current COMSOL `*_metrics.json` and `*_time_series.csv` outputs.
When available, it also consumes `*_surface_displacement.csv`, `*_landmark_displacement.csv`, and `*_tissue_stress_stats.csv`.

The plot set emphasizes signed surface/landmark displacement when exported, average response metrics, and max values as hotspot indicators.
Use `figure_index.md` or `figure_index.csv` to trace every generated figure to its stage source table.

Stages generated:

- `tier1_comparison`
- `tier1_comparison_without_stage1`
- `stage1_baseline`
- `stage2_chestwall`
- `stage3_glandular_fraction`
- `stage4_asymmetry`
- `report_fixed_material_suite`
- `stage5_cooper`
- `stage5_dynamic_amplitude_scout`
- `stage6_fast_tumor_screening`
- `stage6_tumor_preview`
