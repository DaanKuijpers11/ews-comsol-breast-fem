# ews_fem_clean

Finite element breast modelling workspace for the EWS internship project.

This repository contains:

- the original FEBio-oriented modelling workflow
- a cleaned Python pipeline for case generation and post-processing
- an extended COMSOL pipeline for automated geometry construction and first-order mechanical analysis

The current project focus is the improvement of anatomical realism of the internal glandular structure while keeping the pipeline automated and reproducible.

## Project Status

The current model state can be summarized as follows:

- the Python pipeline is functional for case generation, meshing, and export
- the COMSOL pipeline can generate, build, and solve automated cases
- the internal glandular structure has progressed from a simple ellipsoidal region toward a Chen-inspired multicomponent lobe-based structure
- the current glandular layout uses a `6 + 12` ring organization with `18` anatomical lobes
- the COMSOL route is currently strongest for geometry iteration and static benchmarking
- material fidelity in COMSOL is still an intermediate step: the source model is Mooney-Rivlin based, while the current COMSOL branch still uses a linearized approximation for initial solves

Main current development priorities:

1. freeze a final glandular geometry
2. introduce a faster geometry mode for iteration
3. clean up legacy output and duplicated artefacts
4. make glandular coverage/fraction easier to control
5. improve the COMSOL material implementation toward direct Mooney-Rivlin consistency

## Repository Layout

- `src/ews_fem_pipeline_clean`
  - cleaned FEBio-oriented preparation and simulation pipeline
- `src/ews_fem_pipeline_comsol`
  - COMSOL case generation, Java builder generation, batch build, and post-processing
- `runs/elipse_lobules_testcases`
  - FEBio-style source and benchmark/test cases
- `runs/comsol_testcases`
  - COMSOL pipeline test cases and glandular geometry tuning cases
- `Model_current`
  - screenshots of the latest model state used for visual comparison
- `Lobules model pictures`
  - reference screenshots/images used during glandular geometry development

## Main Pipelines

### 1. FEBio-Oriented Pipeline

Run a full FEBio-style case:

```powershell
python -m ews_fem_pipeline_clean run runs/elipse_lobules_testcases/base_ellipsoid/base_ellipsoid.toml -j 1
```

Run a sweep with evaluation:

```powershell
python -m ews_fem_pipeline_clean sweep runs/elipse_lobules_testcases/large_ellipsoid/large_ellipsoid.toml runs/elipse_lobules_testcases/base_ellipsoid/base_ellipsoid.toml runs/elipse_lobules_testcases/medium_ellipsoid/medium_ellipsoid.toml runs/elipse_lobules_testcases/wide_strong_ellipsoid/wide_strong_ellipsoid.toml runs/elipse_lobules_testcases/xlarge_ellipsoid/xlarge_ellipsoid.toml --evaluate
```

This route remains the source reference for:

- Mooney-Rivlin material definitions
- dynamic loading setup
- VTK-based export
- baseline biomechanical comparison

### 2. COMSOL Pipeline

The repository includes a separate COMSOL package:

- `src/ews_fem_pipeline_comsol`

This pipeline reuses the cleaned FEBio-style preprocessing and then exports COMSOL-ready artefacts, including:

- mesh node CSV
- mesh NPZ
- lobule JSON
- expanded source settings TOML
- COMSOL Java builder scaffold
- COMSOL build plan JSON
- COMSOL post-processing Java scaffold

### Typical COMSOL Workflow

Write a default COMSOL settings file:

```powershell
python -m ews_fem_pipeline_comsol write-default-settings runs/comsol_testcases/default_comsol.toml
```

Generate COMSOL case input JSON and artefacts:

```powershell
python -m ews_fem_pipeline_comsol generate runs/comsol_testcases/default_comsol.toml
```

Run a build-only COMSOL case:

```powershell
python -m ews_fem_pipeline_comsol build-only runs/comsol_testcases/default_comsol.toml
```

Run a full COMSOL case:

```powershell
python -m ews_fem_pipeline_comsol run runs/comsol_testcases/default_comsol.toml
```

Check COMSOL license connectivity:

```powershell
python -m ews_fem_pipeline_comsol license-check runs/comsol_testcases/default_comsol.toml
```

## Active COMSOL Cases

The most relevant current COMSOL case files are:

- [runs/comsol_testcases/default_comsol.toml](runs/comsol_testcases/default_comsol.toml)
  - main default COMSOL pipeline case
- [runs/comsol_testcases/chen2024_droplet_auto_comsol_source.toml](runs/comsol_testcases/chen2024_droplet_auto_comsol_source.toml)
  - active COMSOL-specific source geometry/material settings
- [runs/comsol_testcases/final_freeze_probe.toml](runs/comsol_testcases/final_freeze_probe.toml)
  - first anterior-shift freeze probe for glandular structure
- [runs/comsol_testcases/final_freeze_probe_v2.toml](runs/comsol_testcases/final_freeze_probe_v2.toml)
  - second freeze probe with stronger anterior shift and longer duct reach

To run the current freeze probe:

```powershell
python -m ews_fem_pipeline_comsol build-only runs/comsol_testcases/final_freeze_probe_v2.toml
```

## Current Glandular Geometry Concept

The outer breast envelope is still based on a simplified analytical shape. The internal glandular region is no longer intended to be represented by a simple ellipsoidal inclusion.

The current glandular structure is instead built as:

- `18` glandular lobes
- arranged in a `6 + 12` concentric organization
- with petal-like lobe bodies
- posterior lobe volume extending toward the chest wall
- duct-like convergence toward a shared nipple-adjacent hub region

In COMSOL, the main glandular geometry is constructed in:

- `gland_lobules`
  - union of all generated lobe, duct, and hub primitives
- `gland_clip`
  - final glandular domain after clipping the glandular source to the outer breast volume

The remaining interior volume is constructed as:

- `adipose_diff`
  - outer breast minus glandular region

The final geometry union used for physics is:

- `breast_union`
  - adipose + glandular + chest-wall support

### Important Current Limitation

The current COMSOL geometry does **not yet include a separate anatomical skin layer** as its own domain.

Current COMSOL domains are effectively:

- glandular
- adipose
- chest wall support

Skin properties still exist in the source settings, but the COMSOL branch has not yet turned them into a distinct outer shell geometry. This is a planned future refinement.

## COMSOL Build Behaviour

The COMSOL build stage can be slow for the high-fidelity glandular geometry because it performs many boolean and union operations on overlapping solids.

Typical observations:

- `build-only` can take many minutes
- final `Form Union` and boolean steps are often the slowest part
- the detailed lobe/duct geometry is much slower than older ellipsoidal glandular models

This is expected for the current high-detail geometry and is one reason why a future `fast geometry mode` is planned.

## Material Model Notes

The source model uses Mooney-Rivlin-style parameters for:

- skin
- adipose tissue
- glandular tissue
- optional tumour inclusion

However, the current COMSOL builder still converts these source properties into a small-strain linear elastic approximation for the initial COMSOL implementation.

This means:

- FEBio source settings remain the constitutive reference
- COMSOL geometry/region handling is currently ahead of COMSOL constitutive fidelity
- one of the next project steps is to improve the COMSOL materials so they better reflect the source Mooney-Rivlin formulation

## Output Folders

Each COMSOL testcase writes to its own output folder, for example:

- `runs/comsol_testcases/output`
- `runs/comsol_testcases/output_final_freeze_probe`
- `runs/comsol_testcases/output_final_freeze_probe_v2`

These folders may contain:

- `prepare`
- `build`
- `solve`
- `logs`

Important artefacts typically include:

- `*_lobules.json`
- `*_comsol_build_plan.json`
- `*_comsol_builder.java`
- `*_generated.mph`
- `*_metrics.json`

The `runs/comsol_testcases/output` tree can accumulate duplicate or stale artefacts over time. Cleanup is planned, but avoid deleting files while a COMSOL run is still active.

## Recommended Workflow

For geometry development:

1. edit or create a dedicated source TOML in `runs/comsol_testcases`
2. run `build-only`
3. inspect the resulting geometry in COMSOL
4. compare with `Model_current` screenshots and literature reference images
5. only after geometry is acceptable, proceed to solve and metric comparison

For final comparison work:

1. freeze glandular geometry
2. compute glandular fraction / internal coverage
3. improve COMSOL material fidelity
4. compare COMSOL and FEBio responses on shared benchmark cases

## Known Limitations

- COMSOL build-only can be slow for the detailed glandular geometry
- the current COMSOL branch does not yet include a separate skin shell domain
- the COMSOL material representation is not yet a direct Mooney-Rivlin implementation
- geometry realism has been prioritized over dynamic fidelity in the current phase
- multiple older output files and legacy artefacts still exist in the workspace and should be cleaned later

## Practical Notes

- If COMSOL reports `License error: -15`, the license server is not currently reachable.
- If `build-only` reports success but the generated `.mph` file timestamp did not change, inspect the build logs before assuming the geometry updated.
- For the current detailed geometry, use dedicated probe cases rather than constantly overwriting the default case.

## Short-Term Next Steps

Planned next steps in this repository:

1. add a fast geometry mode for quicker COMSOL iteration
2. clean up duplicated and stale output artefacts
3. make glandular fraction more explicitly controllable
4. improve the COMSOL material implementation toward the source Mooney-Rivlin model

