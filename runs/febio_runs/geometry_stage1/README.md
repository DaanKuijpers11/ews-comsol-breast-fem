# Geometry Stage 1

This folder contains the first FEBio-only geometry validation sweep for the refactored outer breast contour. The goal of stage 1 was to test whether controlled outer-envelope changes in the shared Python/FEBio geometry source produce:

- solver-stable FEBio runs
- clear displacement and stress differences
- acceptable mesh-quality and `J` behavior

Stage 1 is mainly an outer-geometry study. Most cases deliberately keep the internal glandular structure simple so that the mechanical differences can be linked mainly to the outer shape.

## What changed relative to the older baseline

The stage-1 refactor introduced explicit outer-geometry controls in the shared FEBio/Python pipeline:

- `outer_profile_mode`
- `anterior_projection_scale`
- `superior_pole_scale`

This means the outer contour is no longer limited to the original circular profile only. The new elliptic variants can push the breast more anteriorly and change the superior-pole fullness while still using the same shared geometry generator.

## Case overview

| Case | Main purpose | Key settings | Internal structure | Final status |
|---|---|---|---|---|
| `baseline_stage1_reference` | clean reference case | `circular`, `anterior=1.0`, `superior=1.0`, `density=110`, `order=1` | simple glandular core, no heterogeneity | successful |
| `elliptic_mild_projection` | mild outer-shape improvement | `elliptic`, `anterior=1.08`, `superior=0.98`, `density=110`, `order=1` | simple glandular core, no heterogeneity | successful |
| `elliptic_mild_projection_chen_materials` | stable Chen-material probe on the successful mild stage-1 geometry | `elliptic`, `anterior=1.08`, `superior=0.98`, `density=110`, `order=1`, explicit Chen-inspired materials | simple glandular core, no heterogeneity | intended as current safest Chen-material solver check |
| `elliptic_projected_compact_pole` | stronger projection and tighter superior pole | `elliptic`, `anterior=1.15`, `superior=0.94`, `density=110`, `order=1` | simple glandular core, no heterogeneity | successful |
| `elliptic_chen2024_compatibility` | compatibility probe with richer heterogeneity | `elliptic`, `anterior=1.10`, `superior=0.97`, `density=120`, `order=1` | Chen-inspired glandular generator + adipose heterogeneity | not a clean final comparison case |
| `elliptic_mild_projection_order2_probe` | higher-order feasibility probe | `elliptic`, `anterior=1.08`, `superior=0.98`, `density=90`, `order=2` | simple glandular core, no heterogeneity | successful probe |
| `elliptic_chen2024_compatibility_order2_probe` | heavy higher-order compatibility probe | `elliptic`, `anterior=1.10`, `superior=0.97`, `density=95`, `order=2` | Chen-inspired glandular generator + adipose heterogeneity | not a clean final comparison case |

## Which models are directly comparable

Use these as the main stage-1 comparison set:

- `baseline_stage1_reference`
- `elliptic_mild_projection`
- `elliptic_projected_compact_pole`

Treat this as a useful but separate feasibility probe:

- `elliptic_mild_projection_order2_probe`
- `elliptic_mild_projection_chen_materials`

Do not use these two as your main final comparison set:

- `elliptic_chen2024_compatibility`
- `elliptic_chen2024_compatibility_order2_probe`

Those Chen-inspired compatibility cases are useful as stress-tests of the refactored geometry together with more advanced internal heterogeneity, but they are not the cleanest report-quality cases for the stage-1 outer-geometry comparison.

## What each successful model showed

### `baseline_stage1_reference`

This is the stage-1 reference model. It keeps the original circular outer contour and simple internal structure. Use it as the baseline for all stage-1 geometry comparisons.

Key outcome:

- peak displacement max: `35.01 mm`
- peak von Mises max: `21061`
- minimum `J`: `0.97094`

### `elliptic_mild_projection`

This is the first successful outer-envelope improvement case. It introduces a modest elliptic contour with slightly increased anterior projection and a slightly more compact superior pole.

Key outcome relative to the baseline:

- displacement: `+15.26%`
- peak stress: `+71.63%`
- minimum `J`: `-0.92%`

Interpretation:

- the geometry change is not just cosmetic
- even a mild outer-shape change measurably affects deformation and stress
- the case remained solver-stable

### `elliptic_mild_projection_chen_materials`

This case keeps the same stable mild elliptic stage-1 geometry but writes the harder Chen-inspired material values explicitly into the TOML.

Interpretation:

- this is the current safest way to test the new literature-aligned material baseline
- it avoids the currently fragile stage-2 volume-topology branch
- use this first if you want to check whether the harder material core solves cleanly before returning to stage 2

### `elliptic_projected_compact_pole`

This is the more aggressive order-1 outer-envelope case. It pushes the contour further anteriorly and reduces superior-pole height more strongly.

Key outcome relative to the baseline:

- displacement: `+22.74%`
- peak stress: `+38.65%`
- minimum `J`: `-3.27%`

Interpretation:

- this produced the largest displacement of the successful order-1 geometry cases
- this is the mechanically most aggressive successful stage-1 order-1 variant
- it remains usable, but its lower `J` means it is closer to local compression/distortion than the baseline

### `elliptic_mild_projection_order2_probe`

This case reuses the mild elliptic shape idea but switches to `order = 2`. It was included to test whether higher-order elements can already run on the improved stage-1 geometry.

Key outcome relative to the baseline:

- displacement: `+17.85%`
- peak stress: `-84.39%`
- minimum `J`: `+1.36%`

Interpretation:

- this run is important because it shows the improved geometry can also be solved with higher-order elements
- the stress values differ strongly from the order-1 set, so this should be treated as a feasibility probe rather than a direct one-to-one replacement

## Which cases were not fully successful for final reporting

### `elliptic_chen2024_compatibility`

Why it exists:

- to test whether the stage-1 outer-envelope refactor still behaves when combined with the richer Chen-inspired glandular generator and adipose heterogeneity

Why it is not a clean final stage-1 comparison case:

- it changes both the outer contour and the internal heterogeneity at the same time
- that makes it harder to attribute result differences purely to outer geometry

### `elliptic_chen2024_compatibility_order2_probe`

Why it exists:

- to push both complexity axes together: richer internal heterogeneity plus `order = 2`

Why it is not a clean final stage-1 comparison case:

- it is intentionally heavy
- it is useful as a robustness experiment, not as the main report-quality geometry comparison

## Main stage-1 conclusion

Stage 1 successfully showed that:

- the shared FEBio/Python geometry source can now generate more than the original circular outer envelope
- modest elliptic geometry changes already alter displacement and stress noticeably
- the stronger projected/compact-pole case is the most aggressive successful order-1 outer-shape variant
- higher-order elements are feasible on the improved geometry, but should still be validated more carefully before becoming the default comparison route

## Files to inspect per case

Inside each case folder:

- `<case>.toml`
  - input settings used for the run
- `<case>.feb`
  - FEBio input model; best file for checking the pre-solve mesh/geometry in FEBio Studio
- `output/<case>.0.vtk` through `output/<case>.48.vtk`
  - time-series result files for ParaView or FEBio Studio
- `output/<case>.obj`
  - surface mesh for Blender
- `output/<case>.npy`
  - surface displacement animation for Blender
- `<case>_summary_statistics.csv`
  - compact quantitative summary for report writing

## Suggested visual interpretation workflow

Use FEBio Studio for:

- checking the pre-solve geometry and mesh
- comparing the outer contour between cases

Use ParaView for:

- stress fields
- displacement fields
- `J` / local compression behavior

Use Blender for:

- quick surface-motion comparison only

## Quick run examples from the repository root

```powershell
$env:PYTHONPATH='src'
& 'C:\Users\20223231\.conda\envs\ews-fem\python.exe' -m ews_fem_pipeline_clean run runs/febio_runs/geometry_stage1/elliptic_mild_projection/elliptic_mild_projection.toml -j 1
```

```powershell
.\scripts\run_febio_geometry_stage1.ps1 -Mode quick -Case elliptic_mild_projection -Jobs 1
```

## Sweep examples from the repository root

```powershell
$env:PYTHONPATH='src'
& 'C:\Users\20223231\.conda\envs\ews-fem\python.exe' -m ews_fem_pipeline_clean sweep runs/febio_runs/geometry_stage1/baseline_stage1_reference/baseline_stage1_reference.toml runs/febio_runs/geometry_stage1/elliptic_mild_projection/elliptic_mild_projection.toml runs/febio_runs/geometry_stage1/elliptic_projected_compact_pole/elliptic_projected_compact_pole.toml runs/febio_runs/geometry_stage1/elliptic_chen2024_compatibility/elliptic_chen2024_compatibility.toml runs/febio_runs/geometry_stage1/elliptic_mild_projection_order2_probe/elliptic_mild_projection_order2_probe.toml runs/febio_runs/geometry_stage1/elliptic_chen2024_compatibility_order2_probe/elliptic_chen2024_compatibility_order2_probe.toml -j 1 --evaluate
```

```powershell
.\scripts\run_febio_geometry_stage1.ps1 -Mode sweep -Jobs 1 -Evaluate
```
