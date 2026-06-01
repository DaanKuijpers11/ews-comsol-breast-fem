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

Conclusion:
De resultaten laten zien dat niet elke complexere modeluitbreiding evenveel biomechanische meerwaarde geeft. Handmatig verdeelde glandulaire lobules geven een stabiele en geloofwaardige verfijning ten opzichte van het baseline-model, met lagere piekspanningen zonder grote verandering in globale verplaatsing. Verdere meshverfijning verhoogt vooral de rekentijd, terwijl de mechanische uitkomsten slechts beperkt veranderen. De meest onderscheidende uitbreiding is de radiale heterogeniteit in het adipose weefsel, omdat deze de grootste afwijking in het vervormingsgedrag veroorzaakt ten opzichte van de baseline, zonder duidelijke verslechtering van de numerieke stabiliteit. Daarmee lijkt adipose heterogeniteit op dit moment de meest veelbelovende richting voor verdere verbetering van het modelrealisme.


Scherper nog:
Het model van je voorganger was bruikbaar als basis, maar de resultaten suggereren dat vooral een meer realistische interne verdeling van het adipose weefsel en, in mindere mate, glandulaire heterogeniteit, relevant zijn voor het voorspelde vervormingsgedrag. Niet elke toename in modelcomplexiteit levert daarbij automatisch extra inzicht op; sommige uitbreidingen verhogen vooral de rekentijd. Voor dit project lijkt daarom een gecontroleerde uitbreiding met stabiele heterogene weefselverdelingen zinvoller dan maximale geometrische of numerieke complexiteit.
