# ews_fem_clean
Clean-up version of old ews_fem_pipeline repository for Intership project

## FEBio pipeline
Run a full case:

```powershell
python -m ews_fem_pipeline_clean run runs/elipse_lobules_testcases/base_ellipsoid/base_ellipsoid.toml -j 1
```

Run a sweep with evaluation:

```powershell
python -m ews_fem_pipeline_clean sweep runs/elipse_lobules_testcases/large_ellipsoid/large_ellipsoid.toml runs/elipse_lobules_testcases/base_ellipsoid/base_ellipsoid.toml runs/elipse_lobules_testcases/medium_ellipsoid/medium_ellipsoid.toml runs/elipse_lobules_testcases/wide_strong_ellipsoid/wide_strong_ellipsoid.toml runs/elipse_lobules_testcases/xlarge_ellipsoid/xlarge_ellipsoid.toml --evaluate
```

## COMSOL pipeline (new scaffold)
The repository now includes a separate package:

- `src/ews_fem_pipeline_comsol`

Write default COMSOL settings:

```powershell
python -m ews_fem_pipeline_comsol write-default-settings runs/comsol_testcases/default_comsol.toml
```

Generate COMSOL case input JSON:

```powershell
python -m ews_fem_pipeline_comsol generate runs/comsol_testcases/default_comsol.toml
```

`generate` now also reuses FEBio `prepare_simulation` and exports:

- mesh node CSV
- mesh NPZ (nodes + tissue connectivity)
- lobule JSON
- expanded source settings TOML
- COMSOL Java API builder scaffold (`*_comsol_builder.java`)
- COMSOL build plan JSON (`*_comsol_build_plan.json`)

Run COMSOL case (requires COMSOL batch executable and model file):

```powershell
set COMSOL_BATCH_EXE=C:\Program Files\COMSOL\COMSOL61\Multiphysics\bin\win64\comsolbatch.exe
python -m ews_fem_pipeline_comsol run runs/comsol_testcases/default_comsol.toml
```

Check license connectivity before running:

```powershell
python -m ews_fem_pipeline_comsol license-check runs/comsol_testcases/default_comsol.toml
```

If `comsol.mph_file` is empty, the runner will first try to auto-build an MPH from the generated Java builder scaffold and then run batch solve.
If build fails with `License error: -15`, COMSOL cannot reach the license server yet; check your COMSOL license connection before rerunning.

Expected TOML keys for COMSOL:

- `[comsol] mph_file = "C:/path/to/model.mph"`
- `[comsol] study = "std1"`
- `[comsol] execute = true|false`
- `[comsol] auto_build_from_java = true|false`
- `[comsol] configuration_dir = "comsol_configuration"`
- `[source] base_case_toml = "../elipse_lobules_testcases/...toml"`
