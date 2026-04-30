$ErrorActionPreference = "Stop"

$root = "C:\Users\20223231\ews_fem_clean"
$suiteDir = Join-Path $root "runs\comsol_full_overnight_suite"
$cases = @(
    "full_baseline_reference",
    "full_curved_chest_reference",
    "full_freeze_probe_v1",
    "full_freeze_probe_v2_curved",
    "full_adipose_gradient_stress"
)

$rows = foreach ($case in $cases) {
    $toml = Join-Path $suiteDir "$case.toml"
    $outputDir = Join-Path $suiteDir ("output_" + $case)
    $resolved = Join-Path $outputDir ($case + "_resolved_case.toml")
    $verification = Join-Path $outputDir ("build\" + $case + "_build_verification.json")
    $metrics = Join-Path $outputDir ("solve\" + $case + "_metrics.json")
    $resultMph = Join-Path $outputDir ("solve\" + $case + "_result.mph")
    $log = Join-Path $outputDir ("logs\" + $case + "_comsol.log")

    [PSCustomObject]@{
        Case = $case
        Toml = Test-Path $toml
        ResolvedCase = Test-Path $resolved
        BuildVerification = Test-Path $verification
        MetricsJson = Test-Path $metrics
        ResultMph = Test-Path $resultMph
        ComsolLog = Test-Path $log
    }
}

Write-Host ""
Write-Host "Morning checklist for COMSOL full overnight suite"
Write-Host ""
$rows | Format-Table -AutoSize

Write-Host ""
Write-Host "Open these first if something failed:"
Write-Host "1. logs\*_comsol.log"
Write-Host "2. build\*_build_verification.json"
Write-Host "3. *_resolved_case.toml"
Write-Host ""
Write-Host "If all MetricsJson values are True, the suite is ready for compare-metrics."
