$ErrorActionPreference = "Stop"

$pythonExe = "C:\Users\20223231\.conda\envs\ews-fem\python.exe"
$root = "C:\Users\20223231\ews_fem_clean"

Set-Location $root

& $pythonExe -m ews_fem_pipeline_comsol compare-metrics `
  "runs/comsol_full_overnight_suite/full_baseline_reference.toml" `
  "runs/comsol_full_overnight_suite/full_curved_chest_reference.toml" `
  "runs/comsol_full_overnight_suite/full_freeze_probe_v1.toml" `
  "runs/comsol_full_overnight_suite/full_freeze_probe_v2_curved.toml" `
  "runs/comsol_full_overnight_suite/full_adipose_gradient_stress.toml" `
  --baseline "full_baseline_reference"

Write-Host ""
Write-Host "Metrics comparison written to:"
Write-Host "C:\Users\20223231\ews_fem_clean\analysis_output\metrics_compare\solver_metrics_compare.csv"
Write-Host "C:\Users\20223231\ews_fem_clean\analysis_output\metrics_compare\solver_metrics_compare.md"
