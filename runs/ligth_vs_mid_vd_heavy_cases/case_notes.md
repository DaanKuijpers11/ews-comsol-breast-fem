`light_case`
- Quick baseline.
- No tumor.
- No glandular heterogeneity.
- Lowest mesh density of the three.

`medium_case`
- Manual glandular lobules.
- No tumor.
- Moderate mesh density.

`heavy_case`
- Auto-generated lobules.
- Mild asymmetry enabled.
- No tumor.
- Highest mesh density of the three.

Example batch run:

```powershell
$env:PYTHONPATH="C:\Users\20223231\ews_fem_clean\src"
python -m ews_fem_pipeline_clean sweep `
  runs\light_case\light_case.toml `
  runs\medium_case\medium_case.toml `
  runs\heavy_case\heavy_case.toml --evaluate
```
