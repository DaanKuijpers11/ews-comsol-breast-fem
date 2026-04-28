# COMSOL Overnight Suite

Suggested run order:

# eduVPN Niet vergeten aan te zetten!!

```powershell
python -m ews_fem_pipeline_comsol sweep runs/comsol_overnight_suite/base_ellipsoid_reference.toml runs/comsol_overnight_suite/medium_ellipsoid.toml runs/comsol_overnight_suite/large_ellipsoid.toml runs/comsol_overnight_suite/wide_strong_ellipsoid.toml runs/comsol_overnight_suite/chen2024_baseline.toml runs/comsol_overnight_suite/asym_manual_lobules.toml
```

Compare COMSOL outputs the next morning:

```powershell
python -m ews_fem_pipeline_comsol compare-metrics runs/comsol_overnight_suite/base_ellipsoid_reference.toml runs/comsol_overnight_suite/medium_ellipsoid.toml runs/comsol_overnight_suite/large_ellipsoid.toml runs/comsol_overnight_suite/wide_strong_ellipsoid.toml runs/comsol_overnight_suite/chen2024_baseline.toml runs/comsol_overnight_suite/asym_manual_lobules.toml --baseline chen2024_baseline
```

Comparison outputs are written to:

- `analysis_output/metrics_compare/solver_metrics_compare.csv`
- `analysis_output/metrics_compare/solver_metrics_compare.md`
