# Geometry Stage 3

Stage 3 starts from the best current stage-2 outcome and is intended to focus on:

- support/anatomy refinement
- modest mesh refinement
- stress redistribution rather than another large material jump

## Starting point

Stage 2 ended as a material-calibration stage, with:

- `stage2_reference_refinedmesh` as the soft anchor
- `stage2_reference_intermediate_materials` as the preferred calibrated baseline
- `stage2_reference_chen_materials` as the stiff literature-directed probe

The main lesson from stage 2 was:

- displacement improved strongly as stiffness increased
- `J` improved as well
- but stress remained high in the simplified anatomy

That means stage 3 should try to improve support and load distribution rather than just making the tissues even stiffer.

## Active stage-3 cases

### `stage3_reference_support_baseline`

Purpose:

- clean stage-3 starting baseline
- inherits the preferred intermediate stage-2 material set
- acts as the reference for later support/anatomy changes

Important settings:

- `density = 125`
- `order = 1`
- same material core as `stage2_reference_intermediate_materials`

### `stage3_reference_support_baseline_refined150`

Purpose:

- first denser baseline reference
- keeps stage-3 materials and geometry unchanged
- isolates the effect of a modest mesh refinement

### `stage3_reference_support_baseline_refined175`

Purpose:

- second denser baseline reference
- pushes the same baseline to a more demanding order-1 mesh
- useful for checking whether stress/displacement trends are mesh-sensitive

### `stage3_pectoral_support_preview`

Purpose:

- stage-3A pectoralis-lite support case
- introduces a separate ellipsoidal pectoralis domain behind the breast
- keeps the visible outer breast silhouette unchanged
- can be previewed first and later reused as a solvable support case

Literature intent:

- follows the Chen-style direction of improving the torso/pectoralis support interface
- avoids the less defensible "curved posterior adipose crescent" interpretation
- acts as a simple separate pectoralis material domain rather than a cosmetic posterior shape tweak

Important settings:

- `density = 120`
- `order = 1`
- `debug_stop_after_mesh = true`
- `pectoralis_support_projection_scale = 0.5`
- `pectoralis_support_center_ratio = 0.64`
- `pectoralis_support_span_ratio = 0.24`

Current technical status:

- the pectoralis domain is now a real separate solid partition inside the model
- it currently generates as:
  - `glandular`: about `1469` tet4 elements
  - `adipose`: about `4326` tet4 elements
  - `pectoralis`: about `137` tet4 elements
- for visual inspection keep `debug_stop_after_mesh = true`
- for a real FEBio run, temporarily switch:
  - `debug_view = false`
  - `debug_stop_after_mesh = false`

### `stage3_pectoral_support_curved_cap`

Purpose:

- literature-closer alternative to the slab support case
- uses a curved elliptical cap-like pectoralis domain instead of a rectangular slab
- keeps the same stage-3 material core so support-shape effects are easier to compare

Literature intent:

- closer to the pectoralis major layer concept described by Chen and Zhang
- still simplified, but less block-like than the slab case
- meant as the better candidate for later stress/displacement comparison against the slab case

Important settings:

- `density = 120`
- `order = 1`
- `pectoralis_support_shape = "curved_cap"`
- `pectoralis_support_projection_scale = 0.45`
- `pectoralis_support_center_ratio = 0.64`
- `pectoralis_support_span_ratio = 0.22`
- defaulted as a solvable case (`debug_view = false`, `debug_stop_after_mesh = false`)

Current technical status:

- mesh/extraction validated
- current approximate partition sizes:
  - `glandular`: `1469` tet4 elements
  - `adipose`: `4279` tet4 elements
  - `pectoralis`: `85` tet4 elements
- this is intentionally lighter and more footprint-like than the slab case

### `stage3_pectoral_support_slab_stable`

Purpose:

- overnight-ready slab-support variant
- smaller support footprint and softer pectoralis than the exploratory slab case
- uses a gentler dynamic step setup to reduce inversion risk

### `stage3_pectoral_support_curved_cap_stable`

Purpose:

- overnight-ready curved-cap support variant
- keeps the literature-closer support shape while using a smaller footprint
- uses the same gentler dynamic step setup as the slab-stable case

### `stage3_pectoral_support_curved_cap_upper2thirds175_stable`

Purpose:

- overnight-ready run case based directly on the preferred `upper2thirds175` preview footprint
- keeps the larger superior coverage that looked closest to the literature-guided intent
- uses a denser `175` mesh so the support footprint is less visually and mechanically coarse

Important settings:

- `density = 175`
- `order = 1`
- `pectoralis_support_shape = "curved_cap"`
- `pectoralis_support_projection_scale = 0.38`
- `pectoralis_support_center_ratio = 0.69`
- `pectoralis_support_span_ratio = 0.30`
- `debug_view = false`
- `debug_stop_after_mesh = false`
- gentler dynamic solve settings copied from the stable support cases

### `stage3_reference_mesh_refined_preview`

Purpose:

- quick geometry/mesh-only preview
- slightly denser mesh for checking coarseness before heavier stage-3 runs
- no solve, just generation/debug viewing

Important settings:

- `density = 150`
- `order = 1`
- `debug_stop_after_mesh = true`

## Recommended first stage-3 workflow

Mesh-only preview:

```powershell
python -m ews_fem_pipeline_clean generate runs/febio_runs/geometry_stage3/stage3_reference_mesh_refined_preview/stage3_reference_mesh_refined_preview.toml
```

Pectoralis-lite support preview:

```powershell
python -m ews_fem_pipeline_clean generate runs/febio_runs/geometry_stage3/stage3_pectoral_support_preview/stage3_pectoral_support_preview.toml
```

Curved-cap pectoralis support case:

```powershell
python -m ews_fem_pipeline_clean run runs/febio_runs/geometry_stage3/stage3_pectoral_support_curved_cap/stage3_pectoral_support_curved_cap.toml -j 1
```

Preferred overnight upper-two-thirds support run:

```powershell
python -m ews_fem_pipeline_clean run runs/febio_runs/geometry_stage3/stage3_pectoral_support_curved_cap_upper2thirds175_stable/stage3_pectoral_support_curved_cap_upper2thirds175_stable.toml -j 1
```

Overnight stage-3 sweep:

```powershell
.\scripts\run_febio_stage3_support_sweep.ps1 -Jobs 1 -Evaluate
```

If you want to inspect the mesh interactively, temporarily set:

```toml
debug_view = true
```

Short solvable baseline:

```powershell
python -m ews_fem_pipeline_clean run runs/febio_runs/geometry_stage3/stage3_reference_support_baseline/stage3_reference_support_baseline.toml -j 1
```

Then evaluate:

```powershell
python -m ews_fem_pipeline_clean evaluate runs/febio_runs/geometry_stage3/stage3_reference_support_baseline/stage3_reference_support_baseline.toml
```

## Stage-3 intent

The next meaningful stage-3 implementations should be things like:

- pectoralis-lite support that can later be turned into a solvable support baseline
- cleaner torso/posterior support representation
- mild mesh refinement on the calibrated baseline

Those are the changes most likely to make the stress state more literature-aligned without undoing the stage-2 displacement gains.

## Curved-cap tuning previews

There is also a dedicated visual tuning set in:

- `runs/febio_runs/geometry_stage3/curved_cap_tuning`

These are geometry-only preview cases with finer meshes and different posterior support footprints, meant to help judge:

- whether the curved-cap support is too small
- whether the superior coverage better matches the "upper two-thirds" literature interpretation
- whether a denser preview mesh makes the support shape easier to judge fairly
