# Geometry Stage 5

Stage 5 is the first return to **profile asymmetry**, now that stage 4 has stabilized:

- the explicit nipple tip
- the anterior transition
- the subareolar gland relationship

Important scope note:

- this is still **not true left-right 3D asymmetry**
- the source geometry remains revolve-based
- stage 5 therefore currently means:
  - mild **side-profile** asymmetry
  - more lower-pole fullness
  - mild superior flattening
  - while preserving the stabilized nipple/front region from stage 4

## Why stage 5 is preview-first

The current asymmetry route is visually usable, but the full 3D gland/adipose partition is not yet robust enough to treat the stage-5 baseline as production-ready.

What currently works:

- geometry-only preview
- front/profile inspection
- checking whether the asymmetry direction looks anatomically reasonable

What still needs more work:

- preserving a healthy glandular domain after the revolve/fragment cleanup
- keeping the stage-4 subareolar relationship intact in a fully run-ready asymmetry case

## Active stage-5 cases

### `stage5_profile_asymmetry_preview`

Purpose:

- first visual stage-5 case
- builds on stage 4B
- adds mild lower-pole fullness and mild superior flattening
- keeps pectoralis support hidden so the outer profile is easier to judge

Important settings:

- `density = 150`
- `debug_view = true`
- `debug_stop_after_mesh = true`
- `profile_asymmetry_enabled = true`
- `inferior_fullness_ratio = 0.08`
- `superior_flattening_ratio = 0.08`
- `nipple_projection_ratio = 0.05`
- `center_relative_position_ellipse = 0.27`

### `stage5_profile_asymmetry_baseline_experimental`

Purpose:

- placeholder for the future first run-oriented stage-5 case
- currently marked experimental because the glandular partition still collapses too much in the asymmetry path

Important settings:

- `density = 125`
- `debug_view = false`
- `debug_stop_after_mesh = false`
- `profile_asymmetry_enabled = true`
- `inferior_fullness_ratio = 0.08`
- `superior_flattening_ratio = 0.08`
- `pectoralis_support_projection_scale = 0.35`

## Suggested current workflow

First inspect the preview:

```powershell
python -m ews_fem_pipeline_clean generate runs/febio_runs/geometry_stage5/stage5_profile_asymmetry_preview/stage5_profile_asymmetry_preview.toml
```

Only after the profile looks right should the asymmetry partition path be debugged further for a real run baseline.

## Practical recommendation right now

- if you want results today, run the two stage-4 baselines first
- if you want to keep moving visually, inspect the stage-5 preview in parallel
- do **not** rely on the stage-5 baseline for a trustworthy FEBio comparison yet
