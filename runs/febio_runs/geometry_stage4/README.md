# Geometry Stage 4

Stage 4 now focuses on the **anterior nipple/subareolar region** before broader asymmetry is introduced.

Important scope note:

- this is **not yet true left-right 3D asymmetry**
- the current source model is still built from a revolved profile
- all active stage-4 cases currently keep `profile_asymmetry_enabled = false`
- stage 4 therefore focuses first on:
  - preserving a small explicit nipple tip
  - improving the subareolar/gland-front relationship
- broader asymmetry is intentionally postponed to stage 5

This keeps the work:

- anatomically easier to justify
- visually easier to inspect
- more stable than trying to force asymmetry while the nipple/front shape is still weak

## What Stage 4 changes compared with the earlier model

Before stage 4:

- the anterior tip was mostly just the front of the outer envelope
- the glandular ellipse could visually dominate the nipple-front transition
- profile experiments could make the nipple look clipped or flattened

Stage 4A now does this instead:

- keeps a small explicit nipple-tip projection
- introduces a controlled transition height from the tip back into the breast body
- keeps the pectoralis support hidden only in the **preview** so the front contour is easier to judge

Stage 4B builds on that by:

- keeping the stabilized tip from 4A
- shifting the glandular ellipse into a more **subareolar** position
- reducing the impression that the nipple itself is just the front of the glandular ellipse

## Active stage-4 cases

### `stage4_nipple_tip_preview`

Purpose:

- geometry-only preview for stage 4A
- preserves a small explicit anterior nipple tip
- keeps support geometry out of view so the front contour can be judged cleanly

Important settings:

- `density = 150`
- `debug_view = true`
- `debug_stop_after_mesh = true`
- `pectoralis_support_projection_scale = 0.0`
- `nipple_projection_ratio = 0.05`
- `nipple_transition_height_ratio = 0.08`

### `stage4_nipple_tip_baseline`

Purpose:

- first run-oriented stage-4A baseline
- restores the curved-cap pectoralis support direction used in stage 3
- keeps the nipple-tip stabilization from stage 4A

Important settings:

- `density = 125`
- `debug_view = false`
- `debug_stop_after_mesh = false`
- `pectoralis_support_projection_scale = 0.35`
- `pectoralis_support_shape = "curved_cap"`

### `stage4_subareolar_gland_preview`

Purpose:

- geometry-only preview for stage 4B
- keeps the stabilized nipple tip from stage 4A
- moves the glandular ellipse slightly more subareolar and less aggressively into the tip
- hides pectoralis support again in the preview so the gland-front change can be judged cleanly

Important settings:

- `density = 150`
- `debug_view = true`
- `debug_stop_after_mesh = true`
- `pectoralis_support_projection_scale = 0.0`
- `right_relative_position_ellipse = 0.05`
- `center_relative_position_ellipse = 0.27`

### `stage4_subareolar_gland_baseline`

Purpose:

- first run-oriented stage-4B baseline
- combines stage-4A nipple-tip stabilization with the mild subareolar gland shift
- keeps the stage-3 curved-cap pectoralis support active during the run

Important settings:

- `density = 125`
- `debug_view = false`
- `debug_stop_after_mesh = false`
- `pectoralis_support_projection_scale = 0.35`
- `right_relative_position_ellipse = 0.05`
- `center_relative_position_ellipse = 0.27`

## Suggested workflow

First inspect the nipple-tip preview:

```powershell
python -m ews_fem_pipeline_clean generate runs/febio_runs/geometry_stage4/stage4_nipple_tip_preview/stage4_nipple_tip_preview.toml
```

Then inspect the subareolar/gland-front preview:

```powershell
python -m ews_fem_pipeline_clean generate runs/febio_runs/geometry_stage4/stage4_subareolar_gland_preview/stage4_subareolar_gland_preview.toml
```

If both look reasonable, the next stage-4 runs to try are:

```powershell
python -m ews_fem_pipeline_clean run runs/febio_runs/geometry_stage4/stage4_nipple_tip_baseline/stage4_nipple_tip_baseline.toml -j 1
```

```powershell
python -m ews_fem_pipeline_clean run runs/febio_runs/geometry_stage4/stage4_subareolar_gland_baseline/stage4_subareolar_gland_baseline.toml -j 1
```

## Current caution

- the preview cases are useful and technically generate
- the full stage-4 run path is still under active validation
- stage 4 should currently be interpreted as an **anterior-region realism** stage, not yet an asymmetry stage
