# Long run queue for additional Results evidence

Date prepared: 2026-06-23

Use this file as a copy-paste queue for the Anaconda terminal from the repository root:

```bat
cd "C:\Users\20223231\ews_fem_clean"
```

## Priority choice

The most useful additional evidence for the Results section is a matched tumor set on the Stage 5 volumetric-skin, soft-interior, no-Cooper basis. This directly strengthens the `Tumor-overlay sensitivity` subsection. The current Cooper set is already sufficient; more Cooper runs are lower value.

## Queue A: longest and most useful tumor runs

This runs two matched realistic-gland tumor cases:

- medium upper-outer surface-proximal tumor, 12 mm diameter;
- large central tumor, 20 mm diameter.

These are more report-relevant than the simple-gland tumor scout because they use the Stage 5 volumetric-skin/soft-interior no-Cooper basis.

```bat
python -m ews_fem_pipeline_comsol run "runs\comsol_runs\geometry_stage6\stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview.toml" "runs\comsol_runs\geometry_stage6\stage6_tumor_large_central_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview.toml"
```

After the solve finishes, run surface/EWS post-processing:

```bat
python -m ews_fem_pipeline_comsol postprocess-only --mode ews_surface "runs\comsol_runs\geometry_stage6\stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview.toml" "runs\comsol_runs\geometry_stage6\stage6_tumor_large_central_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview.toml"
```

Then run tumor-mask post-processing:

```bat
python -m ews_fem_pipeline_comsol postprocess-only --mode internal_tumor "runs\comsol_runs\geometry_stage6\stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview.toml" "runs\comsol_runs\geometry_stage6\stage6_tumor_large_central_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview.toml"
```

Report value if successful:

- stronger tumor sensitivity subsection;
- one location/size comparison: medium upper-outer vs large central;
- tumor volume, tumor displacement, tumor stress and surface response from the same route;
- better basis for saying whether tumor effects are visible globally or mainly in tumor-mask/internal metrics.

## Queue B: postprocess existing medium tumor solve

This case already has an output folder but did not appear in the latest compact postprocess inventory. Run this after Queue A or in a second terminal if you want to use time efficiently.

```bat
python -m ews_fem_pipeline_comsol postprocess-only --mode ews_surface "runs\comsol_runs\geometry_stage6\stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_solve_only_preview.toml"
```

```bat
python -m ews_fem_pipeline_comsol postprocess-only --mode internal_tumor "runs\comsol_runs\geometry_stage6\stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_solve_only_preview.toml"
```

Report value if successful:

- comparison against the matched volumetric-skin/soft-interior tumor route;
- useful as a diagnostic note if tumor sensitivity changes strongly with material/skin route.

## Queue C: skin/material postprocess back-up

This does not add new full solves, but it turns existing skin/material cases into more consistent `ews_surface` outputs. Run this if Queue A is still solving in another terminal or if you want backup material for `Effect of volumetric skin representation`.

```bat
python -m ews_fem_pipeline_comsol postprocess-only --mode ews_surface "runs\comsol_runs\geometry_stage5\stage5_scout_simple_gland_no_skin_soft_interior_125g_solve_only_preview.toml" "runs\comsol_runs\geometry_stage5\stage5_scout_simple_gland_volskin_01mm_softskin_soft_interior_125g_solve.toml" "runs\comsol_runs\geometry_stage5\stage5_scout_simple_gland_volskin_01mm_mid_skin088kpa_soft_interior_125g_solve.toml" "runs\comsol_runs\geometry_stage5\stage5_scout_simple_gland_volskin_15mm_femke_skin_soft_interior_125g_solve_only_preview.toml" "runs\comsol_runs\geometry_stage5\stage5_scout_simple_gland_volskin_15mm_mid_skin088kpa_soft_interior_125g_solve_only_preview.toml" "runs\comsol_runs\geometry_stage5\stage5_scout_simple_gland_volskin_15mm_stiff_skin_soft_interior_125g_solve_only_preview.toml"
```

Report value if successful:

- cleaner skin-thickness/stiffness table;
- better support for whether the 1.5 mm volumetric skin layer suppresses surface displacement;
- optional comparison between 0.1 mm and 1.5 mm volumetric skin scouts.

## Queue D: check what finished

After any queue finishes, run:

```bat
powershell -Command "Get-ChildItem -Path runs\comsol_runs\geometry_stage6\outputs -Recurse -Filter '*_ews_surface_summary.json' | Select-Object -ExpandProperty FullName"
```

```bat
powershell -Command "Get-ChildItem -Path runs\comsol_runs\geometry_stage6\outputs -Recurse -Filter '*_internal_tumor_summary.json' | Select-Object -ExpandProperty FullName"
```

For skin/material postprocess:

```bat
powershell -Command "Get-ChildItem -Path runs\comsol_runs\geometry_stage5\outputs -Recurse -Filter '*_ews_surface_summary.json' | Select-Object -ExpandProperty FullName"
```

## Practical order

Recommended order:

1. Run Queue A solve.
2. While that is running, optionally run Queue C in a separate terminal only if COMSOL accepts another batch job on the machine/license.
3. After Queue A completes, run Queue A `ews_surface`.
4. After that completes, run Queue A `internal_tumor`.
5. Run Queue B only if there is still time.

If only one long job can be run, choose Queue A.
