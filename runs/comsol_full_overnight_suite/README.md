# COMSOL Full Overnight Suite

Deze map bevat 5 zelfstandige full-model COMSOL cases voor een overnight sweep.

Cases:
- `full_baseline_reference.toml`
- `full_curved_chest_reference.toml`
- `full_freeze_probe_v1.toml`
- `full_freeze_probe_v2_curved.toml`
- `full_adipose_gradient_stress.toml`

Kort doel per case:
- `full_baseline_reference`: full referentiecase met huidige Mooney-Rivlin scaffold + shell scaffold.
- `full_curved_chest_reference`: zelfde full model maar met lichte curved chest wall.
- `full_freeze_probe_v1`: full gland layout met anterior freeze-probe v1 configuratie.
- `full_freeze_probe_v2_curved`: v2 gland layout, iets dichter bij de nipple, plus curved chest.
- `full_adipose_gradient_stress`: materiaalgevoelige case met sterkere adipose radiale heterogeniteit en iets dichtere mesh.

## Anaconda Prompt

Open een `Anaconda Prompt` en run:

```powershell
conda activate ews-fem
cd "C:\Users\20223231\ews_fem_clean"
```

De overnight sweep:

```powershell
python -m ews_fem_pipeline_comsol sweep runs/comsol_full_overnight_suite/full_baseline_reference.toml runs/comsol_full_overnight_suite/full_curved_chest_reference.toml runs/comsol_full_overnight_suite/full_freeze_probe_v1.toml runs/comsol_full_overnight_suite/full_freeze_probe_v2_curved.toml runs/comsol_full_overnight_suite/full_adipose_gradient_stress.toml
```

## Output

Elke case schrijft naar zijn eigen outputmap:
- `output_full_baseline_reference`
- `output_full_curved_chest_reference`
- `output_full_freeze_probe_v1`
- `output_full_freeze_probe_v2_curved`
- `output_full_adipose_gradient_stress`

Handige bestanden om morgenochtend eerst te checken:
- `*_resolved_case.toml`
- `build/*_build_verification.json`
- `solve/*_metrics.json`
- `logs/*_comsol.log`

## Morning Checklist

Er staan nu ook twee Python-hulpscripts klaar in deze map:
- `morning_checklist.py`
- `morning_compare_metrics.py`

Eerst snelle statuscheck:

```powershell
cd C:\Users\20223231\ews_fem_clean\runs\comsol_full_overnight_suite
python .\morning_checklist.py
```

Daarna metrics vergelijken tegen de baseline:

```powershell
cd C:\Users\20223231\ews_fem_clean\runs\comsol_full_overnight_suite
python .\morning_compare_metrics.py
```

De compare-metrics output komt hier terecht:
- `C:\Users\20223231\ews_fem_clean\analysis_output\metrics_compare\solver_metrics_compare.csv`
- `C:\Users\20223231\ews_fem_clean\analysis_output\metrics_compare\solver_metrics_compare.md`
