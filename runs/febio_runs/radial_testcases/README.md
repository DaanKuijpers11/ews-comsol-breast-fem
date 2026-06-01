# Radial Gradient Testcases

Dit pad bevat vijf testcases voor het tunen van de adipose radial gradient in het model.
Elke testcase varieert de `material.adipose.hetero`-instellingen om te zien hoe sterkte, breedte en positie van de heterogeniteit de stress- en vervormingsrespons beïnvloeden.

## Structuur

- `runs\radial_testcases\radial_alpha_low/radial_alpha_low.toml`
- `runs\radial_testcases\radial_alpha_medium/radial_alpha_medium.toml`
- `runs\radial_testcases\radial_alpha_high/radial_alpha_high.toml`
- `runs\radial_testcases\radial_center_offset/radial_center_offset.toml`
- `runs\radial_testcases\radial_narrow_gradient/radial_narrow_gradient.toml`

## Wat wordt getest

- `radial_alpha_*`: de sterkte van de heterogeniteit in adipose materiaalwaarden.
- `radial_L`: de schaal/breedte van de radial gradient.
- `radial_center`: de positie van het gradientcentrum in de borst.

## Verwachte effecten

- Een **hogere alpha** maakt de heterogeniteit sterker en kan de stressverdeling duidelijker veranderen, maar kan ook numerieke gevoeligheid verhogen.
- Een **lagere alpha** geeft een mildere heterogeniteit en kan de respons dichter bij het baseline-model houden.
- Een **narrow gradient** (`radial_L` kleiner) concentreert de heterogeniteit in een kleiner gebied, wat resultaat kan geven in lokalere veranderingen van spanning en verplaatsing.
- Een **center offset** test de gevoeligheid voor het model ten opzichte van een niet-perfect gecentreerde heterogeniteitsverdeling.

## Run en vergelijking

1. Run een case met:

   ```powershell
   python -m ews_fem_pipeline_clean run runs\radial_testcases\radial_alpha_low\radial_alpha_low.toml
   ```

2. Analyseer de resultaten met:

   ```powershell
   $env:RUN_NAME='radial_alpha_low'
   python scripts/data_analysis_main.py
   ```

3. Vergelijk vervolgens de output metrics:
   - `vm_max` / `vm_mean`
   - `disp_max`
   - `J_min`
   - `inverted_elements`
   - `near_inverted_elements`

4. Idealiter wil je een model met lagere piekstresses en een vergelijkbare verplaatsing, zonder verlies van mesh-stabiliteit.

## Notitie

Deze testcases zijn bedoelt als een eerste verkenning van radial heterogeniteit in adipose materiaal. Gebruik `radial_alpha_medium` als baseline en vergelijk `low`, `high`, `narrow` en `offset` om te zoeken naar de beste trade-off tussen realisme en numerieke stabiliteit.
