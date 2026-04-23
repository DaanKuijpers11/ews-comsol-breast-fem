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

Comparison:
- manual_lobules_balanced is een nette verbetering t.o.v. baseline: lagere piekspanning, bijna dezelfde verplaatsing, nog steeds stabiel.
- manual_lobules_dense kost veel extra rekentijd, maar geeft mechanisch maar beperkte extra winst boven manual_lobules_balanced.
- asym_manual_lobules laat iets meer verplaatsing zien en lijkt nuttig als je geometrische variatie wilt meenemen.
- adipose_radial_gradient wijkt het duidelijkst af van de baseline: lagere maximale verplaatsing, duidelijk ander responsprofiel, terwijl min J bijna gelijk blijft. Dat maakt deze case nu de meest interessante kandidaat als je wilt aantonen dat extra anatomische realisme ook echt een merkbaar biomechanisch effect heeft.
