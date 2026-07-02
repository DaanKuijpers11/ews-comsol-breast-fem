# COMSOL Testcase Templates

This folder contains small reference TOML files for inspecting the COMSOL pipeline settings.

## Files

- `all_default_settings.toml`
  - Full default settings template written from `ews_fem_pipeline_comsol.settings`.
  - Useful as a settings overview when creating or reviewing new cases.
  - Not intended as a final solved model case without editing at least `[pipeline]`, `[source]`, and relevant `[comsol]` settings.

For runnable examples, use the curated cases in `runs/comsol_runs/geometry_stage*`.
