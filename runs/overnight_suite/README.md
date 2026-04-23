Suggested overnight suite:

- `baseline_fast.toml`
- `manual_lobules_balanced.toml`
- `manual_lobules_dense.toml`
- `asym_manual_lobules.toml`
- `adipose_radial_gradient.toml`
- `auto_lobules_safe.toml`

Command:

```bat
python -m ews_fem_pipeline_clean sweep ^
  runs\overnight_suite\baseline_fast.toml ^
  runs\overnight_suite\manual_lobules_balanced.toml ^
  runs\overnight_suite\manual_lobules_dense.toml ^
  runs\overnight_suite\asym_manual_lobules.toml ^
  runs\overnight_suite\adipose_radial_gradient.toml ^
  runs\overnight_suite\auto_lobules_safe.toml ^
  --evaluate
```

This suite is meant to compare:

- baseline vs glandular heterogeneity
- moderate vs denser mesh
- symmetry vs asymmetry
- glandular-only heterogeneity vs adipose radial heterogeneity
- a safer reduced auto-lobule configuration
