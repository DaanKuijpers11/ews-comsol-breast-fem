# Curved Cap Tuning

This folder contains **geometry-only preview cases** for tuning the stage-3 curved-cap pectoralis support.

These cases are meant for:

- visual inspection in `debug_view`
- checking whether the posterior support footprint looks more literature-aligned
- tuning the footprint before running new long FEBio solves

## Why this tuning round exists

The first curved-cap support case was solver-stable and slightly promising, but it still raised two open questions:

- is the posterior support footprint large enough to resemble the literature direction?
- is the mesh density high enough to judge the geometry shape fairly?

The literature wording about the **upper two-thirds** should not be interpreted as:

- "make the pectoralis occupy two-thirds of the breast volume"

Instead, it is better interpreted here as:

- a posterior support region that mainly spans the upper-to-mid posterior breast attachment zone
- while remaining thin in the anterior-posterior direction

So these preview cases mainly vary:

- posterior support height coverage
- posterior support center height
- support projection depth
- mesh density for visual inspection

## Cases

### `curved_cap_preview_compact150`

- finer preview mesh
- relatively compact support footprint
- useful as a lower-bound support patch

### `curved_cap_preview_mid150`

- finer preview mesh
- balanced footprint around the current stable curved-cap idea
- good first candidate to inspect

### `curved_cap_preview_upper2thirds175`

- densest preview mesh of the three
- taller posterior support patch aimed at the upper two-thirds interpretation
- useful for checking whether a larger superior footprint reads more literature-like

### `fascia_patch_preview_mid150`

- thinner and broader posterior support patch
- intended to read more as a fascia-like support layer than as a compact muscle lump
- good first candidate if the curved-cap cases feel too embedded

### `fascia_patch_preview_upper2thirds175`

- thinner and broader posterior support patch
- larger superior coverage aimed at the upper two-thirds interpretation
- meant for checking whether a more spread posterior base support looks closer to the literature

## Run

From the repo root:

```powershell
python -m ews_fem_pipeline_clean generate runs/febio_runs/geometry_stage3/curved_cap_tuning/curved_cap_preview_mid150/curved_cap_preview_mid150.toml
```

All three cases already default to:

- `debug_view = true`
- `debug_stop_after_mesh = true`

So they should open directly as mesh previews.
