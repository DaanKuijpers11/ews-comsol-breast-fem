# COMSOL long run status

Date: 2026-06-23

Purpose: track additional COMSOL runs started from Codex for the Results section.

## Active queue

Queue A from `docs/report_plan/long_run_queue_results.md`.

Cases:

- `stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview.toml`
- `stage6_tumor_large_central_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview.toml`

## Status log

- License check: passed before starting Queue A.
- Queue A full solve: started.
  - Python PID: `10004`
  - COMSOL batch PID observed after start: `35736`
  - Logs: `docs/report_plan/run_logs/queue_a_full_solve_stdout.log` and `docs/report_plan/run_logs/queue_a_full_solve_stderr.log`
- Queue A case 1 build: completed after about 1.1 min.
- Queue A case 1 solve: running.
  - COMSOL solve PID observed: `19672`
  - COMSOL reported equation generation and `1606596` solved DOFs.
  - COMSOL result status file reports `Running`.
  - Recovery file: `C:\Users\20223231\.comsol\v64\recoveries\MPHRecovery8802date_Jun_23_2026_7-23_PM.mph`
  - Continued monitoring: no stderr output, no license error, process still consuming CPU. No final `result.mph` yet.
  - Around 20:22, the case was still running with increasing COMSOL CPU time.
  - Case-specific COMSOL log shows active transient solve progress, about `9%` and model time about `0.058 s`; peak memory reported around 18 GB.
  - Around 20:33, progress reached about `11%` and model time about `0.107 s`.
  - Around 20:48, progress reached about `16%` and model time about `0.211 s`.
  - Around 21:03, progress reached about `20%` and model time about `0.315 s`.
  - Around 21:24, progress reached about `26%` and model time about `0.447 s`.
  - Around 21:44, progress reached about `32%` and model time about `0.586 s`.
  - Around 22:14, progress reached about `40%` and model time about `0.787 s`.
  - Around 22:45, progress reached about `44%` and model time about `0.868 s`.
  - Around 23:15, progress reached about `48%` and model time about `0.981 s`.
  - Around 23:45, progress reached about `53%` and model time about `1.09 s`.
  - Around 00:15, progress reached about `58%` and model time about `1.21 s`.
  - Around 00:46, progress reached about `60%` and model time about `1.27 s`; adaptive step size became smaller.
  - Around 01:16, progress reached about `63%` and model time about `1.34 s`; still active, no stderr output.
  - Around 01:47, progress reached about `65%` and model time about `1.36 s`; solver is in a slower adaptive-step region.
  - Around 02:17, progress reached about `68%` and model time about `1.44 s`.
  - Around 02:48, progress reached about `73%` and model time about `1.55 s`.
  - Around 03:18, progress reached about `75%` and model time about `1.61 s`.
  - Around 03:49, progress was still about `75%`, but model time advanced slightly to about `1.614 s`; no stderr output.
  - Around 04:19, progress reached about `78%` and model time about `1.675 s`.
  - Around 04:49, progress reached about `81%` and model time about `1.75 s`.
  - Around 05:23, progress reached about `84%` and model time about `1.825 s`; COMSOL CPU time was still increasing and no stderr/license error was present.
  - Around 05:34, progress reached about `85%` and model time about `1.85 s`; COMSOL CPU time continued to increase.
  - Around 05:55, progress reached about `87%` and model time about `1.90 s`; no final `result.mph` yet.
  - Around 06:25, progress reached about `90%` and model time about `1.97 s`; still no stderr output or license error.
  - Around 06:56, progress reached about `94%` and model time about `2.05 s`; case 2 output folder exists from preparation, but the wrapper has not started case 2 yet.
  - Around 07:16, progress reached about `96%` and model time about `2.10 s`; case 1 is still solving with small adaptive time steps.
  - Around 07:31, progress reached about `97%` and model time about `2.14 s`; case 1 is still active and no final `result.mph` exists yet.
  - Around 07:52, progress reached about `99%` and model time about `2.18 s`; case 2 has not started yet.
- Queue A case 1 solve: completed successfully.
  - Solution time: `45268 s` (`12 hours, 34 minutes, 28 seconds`).
  - Wrapper solve time: about `755.4 min`.
  - Result file: `runs/comsol_runs/geometry_stage6/outputs/output_stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview/solve/stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview_result.mph`
  - Result file size: about `5093.6 MB`.
- Queue A case 2 solve: not run.
  - The wrapper skipped the large-central case with: `no readable MPH available`.
  - Cause found: the configured `comsol.mph_file` points to a Stage 5 `reuse_parameter_patched.mph` that is not present in the repository/output tree.
  - No COMSOL crash or license failure was observed for case 2; it was skipped before launching COMSOL.
- Queue A case 1 `ews_surface` postprocess: first attempt timed out.
  - Timeout: `1800 s`.
  - Debug log reached `load_complete`, `volume_scalars_ready`, and `scalar_review_metrics_start`.
  - This means the large `5.1 GB` result MPH loaded correctly; the timeout occurred during scalar review metric evaluation, not during COMSOL startup or model loading.
  - Added finer scalar-review progress logging in `src/ews_fem_pipeline_comsol/script_builder.py`.
  - Increased `postprocess_timeout_s` for the medium upper-outer tumor TOML to `7200`.
- Queue A case 1 `ews_surface` postprocess: retry started.
  - Python PID: `23436`
  - COMSOL batch PID: `1664`
  - Around 08:47, the retry had progressed through breast displacement and tumor displacement scalar metrics.
  - Current last observed status: `scalar_review_mises_max_start`.
  - Around 08:58, COMSOL was still active at `scalar_review_mises_max_start`; CPU time continued to increase.
- Queue A case 1 `ews_surface` retry: stopped after diagnosis.
  - Around 09:18, the retry was still at `scalar_review_mises_max_start` after about `36 min`.
  - This indicated that full stress/surface postprocessing for the `5.1 GB` result MPH was not practical for report turnaround.
  - The process was stopped without deleting outputs.
- Queue A case 1 `global` quick postprocess: completed.
  - Implemented a lightweight global quick route that exports displacement and tumor-displacement time series and deliberately skips von Mises, surface and landmark metrics.
  - Runtime: about `4.2 min`.
  - Outputs:
    - `runs/comsol_runs/geometry_stage6/outputs/output_stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview/solve/stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview_global_summary.json`
    - `runs/comsol_runs/geometry_stage6/outputs/output_stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview/solve/stage6_tumor_medium_upper_outer_surface_proximal_xoffset055_125g_volumetric_skin_soft_interior_solve_only_preview_global_time_series.csv`
  - Key values:
    - Breast volume: `585.090 ml`
    - Tumor volume: `0.912 ml`
    - Peak breast displacement: `10.916 mm`
    - Peak tumor-region displacement: `8.496 mm`
    - Time of peak displacement: `1.445 s`
    - Review-time breast displacement at `1.55 s`: `4.963 mm`
    - Review-time tumor displacement at `1.55 s`: `3.956 mm`

## Output checks

- Queue A medium upper-outer tumor full solve output exists and is usable for post-processing.
- Queue A medium upper-outer tumor has lightweight `global` postprocess outputs suitable for displacement/tumor-displacement Results. Stress and surface metrics are not available for this new case.
- Queue A large central tumor output folder contains generated/prepared files only; no solve result exists.

## Report-use notes

- Use the new Queue A medium upper-outer tumor output as a matched realistic-gland/volumetric-skin tumor-displacement result.
- Do not use it as a complete `ews_surface` or stress result; the stress/surface postprocess route was too expensive for this `5.1 GB` result MPH.

## Queue C skin/material postprocess

- Queue C missing `ews_surface` postprocess batch: started.
  - Python PID: `19552`
  - First observed COMSOL batch PID: `3200`
  - Cases included:
    - `stage5_scout_simple_gland_no_skin_soft_interior_125g_solve_only_preview.toml`
    - `stage5_scout_simple_gland_volskin_01mm_softskin_soft_interior_125g_solve.toml`
    - `stage5_scout_simple_gland_volskin_01mm_mid_skin088kpa_soft_interior_125g_solve.toml`
    - `stage5_scout_simple_gland_volskin_15mm_mid_skin088kpa_soft_interior_125g_solve_only_preview.toml`
    - `stage5_scout_simple_gland_volskin_15mm_stiff_skin_soft_interior_125g_solve_only_preview.toml`
  - The existing `stage5_scout_simple_gland_volskin_15mm_femke_skin_soft_interior_125g_solve_only_preview` `ews_surface` output was not rerun.
  - Around 09:38, case 1 was still running in COMSOL; no Queue C summary/time-series outputs had been written yet.
- Queue C missing `ews_surface` postprocess batch: completed.
  - No active COMSOL/Python queue process remained after the final check.
  - Case 1 `no_skin_soft_interior`: completed in about `2.3 min`.
  - Case 2 `volskin_01mm_softskin`: completed in about `11.2 min`.
  - Case 3 `volskin_01mm_mid_skin088kpa`: skipped because the required result MPH was missing.
  - Case 4 `volskin_15mm_mid_skin088kpa`: completed in about `9.3 min`.
  - Case 5 `volskin_15mm_stiff_skin`: completed in about `9.1 min`.
  - Queue C is therefore finished and does not need VPN/COMSOL time anymore.
  - Log files:
    - `docs/report_plan/run_logs/queue_c_missing_ews_surface_stdout.log`
    - `docs/report_plan/run_logs/queue_c_missing_ews_surface_stderr.log`

### Queue C key comparison values

Reference case: `stage5_scout_simple_gland_volskin_15mm_femke_skin_soft_interior_125g_solve_only_preview`.

| Report label | Peak breast displacement (mm) | Mean breast displacement (mm) | Peak VM stress (kPa) | Review surface max displacement (mm) | Peak displacement change vs reference | Review surface change vs reference |
|---|---:|---:|---:|---:|---:|---:|
| `REF_15mm_soft` | 45.582 | 22.554 | 19.283 | 11.603 | 0.0% | 0.0% |
| `NO_SKIN` | 61.531 | 27.556 | 3.127 | 9.038 | +35.0% | -22.1% |
| `SKIN_01mm_soft` | 60.032 | 27.200 | 44.583 | 9.413 | +31.7% | -18.9% |
| `SKIN_15mm_mid088` | 25.490 | 13.242 | 42.897 | 10.375 | -44.1% | -10.6% |
| `SKIN_15mm_stiff` | 11.144 | 5.704 | 69.671 | 3.362 | -75.6% | -71.0% |

Report-use note: these Queue C results are useful for the skin/material Results subsection because changing the volumetric skin thickness/stiffness produces much larger response changes than the Cooper-support variants. Interpret the higher stress values carefully as model stress concentrations caused by the stiffer/volumetric skin route, not as validated tissue stress predictions.
