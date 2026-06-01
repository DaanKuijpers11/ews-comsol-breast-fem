# Stage 1 COMSOL Runs: Baseline Motion, Gravity, and Dynamic Bounce

Deze map bevat de Stage 1 COMSOL-cases voor het simpele baseline breast model. Stage 1 is bedoeld om eerst de basisfysica betrouwbaar te krijgen voordat chestwall curvature, glandular fraction, asymmetry of Cooper ligaments worden geinterpreteerd.

De belangrijkste vraag in Stage 1 is nu:

- wat is een cleane static/gravity sag baseline;
- wat is een verdedigbare dynamische bounce/jump sensitivity;
- welke displacementmaat laat echte verticale beweging zien in plaats van alleen displacement magnitude of solver-artefacten.

## Coordinaten en interpretatie

In de COMSOL pipeline geldt:

- `u` / x = links-rechts;
- `v` / y = anterior-posterior;
- `w` / z = verticaal;
- negatieve `w` betekent omlaag;
- `solid.disp` is displacement magnitude en heeft geen teken.

Voor jump/bounce interpretatie is `w` belangrijker dan `solid.disp`, omdat magnitude een op-en-neer beweging kan maskeren of als rare golf kan laten lijken.

Voor fixed-support dynamic cases moet de hoofdplot worden gelezen als:

```text
w(t) - w(t_dynamic_start)
```

Dus: verticale verplaatsing ten opzichte van de toestand vlak voor de dynamische excitatie.

Support-relative displacement mag alleen gebruikt worden als de support zelf bewust beweegt en de geexporteerde support displacement aantoonbaar nonzero is.

## Huidige Stage 1 status

### Meest bruikbaar als static/gravity reference

Gebruik eerst:

```text
stage1_quasistatic_gravity_sag_reference.toml
```

Dit is de nieuwe kandidaat voor een cleane gravity sag reference.

Deze case gebruikt:

- fixed posterior breast attachment/chestwall;
- geen prescribed support motion;
- alleen gravity;
- langere gravity ramp: `gravity_ramp_duration_s = 2.0`;
- langere settle: `dynamic_settle_duration_s = 3.0`;
- hogere Rayleigh mass damping: `dynamic_mass_damping_alpha_s_inv = 120.0`.

Doel: de oude gravity-only ringing verminderen zodat de eindtoestand beter als report-ready sag/stress baseline kan dienen.

Dit is nog steeds een time-dependent quasi-static route, geen echte COMSOL Stationary study. Als deze nog zichtbaar blijft natrillen, dan is de volgende stap een echte stationary gravity solve of een nog sterkere quasi-static/damped setup.

Outputmap:

```text
outputs/output_stage1_quasistatic_gravity_sag_reference
```

### Bruikbaar als dynamic bounce sensitivity

Gebruik daarna:

```text
stage1_fixed_support_acceleration_pulse.toml
```

Deze case gebruikt:

- fixed posterior breast attachment/chestwall;
- gravity preload;
- daarna een gladde verticale inertial acceleration pulse op het breast tissue;
- geen bewegende support boundary;
- amplitude: `dynamic_acceleration_amplitude_g = 0.75`;
- duur: `dynamic_acceleration_duration_s = 0.45`;
- mass damping: `dynamic_mass_damping_alpha_s_inv = 20.0`.

Fysische interpretatie: de borst wordt in een torso/chestwall frame bekeken. De support blijft vast, en de inertial pulse bootst relatieve dynamische belasting na, alsof het lichaam versnelt tijdens een sprongachtige beweging.

Belangrijk: deze case is pas report-ready als postprocess laat zien dat:

- de solve tot `dynamic_end_time_s` kwam;
- de time-series het volledige pulse interval bevat;
- nipple/surface `w(t)-w(t_dynamic_start)` een duidelijke response laat zien;
- stress niet alleen door een lokale max-hotspot wordt gedomineerd.

Omdat de bestaande result `.mph` al is opgeslagen, hoef je voor de huidige check niet opnieuw te solven. Gebruik eerst postprocess-only.

Outputmap:

```text
outputs/output_stage1_fixed_support_acceleration_pulse
```

Status na postprocess:

- de solve en postprocess zijn gelukt;
- de case haalt `t_dynamic_end = 2.2 s`;
- de beweging is fysisch duidelijk zichtbaar, maar te agressief als realistische hoofdbeweging;
- nipple dynamic-start response is ongeveer `-81 mm` tot `+84 mm`, dus ongeveer `165 mm peak-to-peak`;
- outer-surface mean dynamic-start response is ongeveer `-42 mm` tot `+46 mm`, dus ongeveer `88 mm peak-to-peak`;
- max displacement hotspots lopen richting `117 mm`.

Conclusie: deze case is nuttig als upper-bound / diagnostic dynamic sensitivity, maar voorlopig niet als report-ready "realistische jump" baseline.

### Mildere fixed-support dynamic kandidaten

Omdat `stage1_fixed_support_acceleration_pulse.toml` te hard oscilleert, zijn twee mildere varianten toegevoegd:

```text
stage1_fixed_support_acceleration_pulse_mild_025g.toml
stage1_fixed_support_acceleration_pulse_moderate_050g.toml
```

Beide gebruiken:

- fixed posterior support;
- gravity preload;
- een langere inertial pulse: `dynamic_acceleration_duration_s = 0.60`;
- meer demping: `dynamic_mass_damping_alpha_s_inv = 60.0`;
- metrics-only postprocess: `postprocess_export_plot_images = false`.

Verschil:

- `mild_025g`: `dynamic_acceleration_amplitude_g = 0.25`;
- `moderate_050g`: `dynamic_acceleration_amplitude_g = 0.50`.

Aanbevolen: run eerst `mild_025g`. Alleen als die response te klein is, run daarna `moderate_050g`.

Gewenst gedrag:

- duidelijke neerwaartse en opwaartse nipple/surface response;
- geen extreem grote resonante oscillatie;
- displacement liefst veel kleiner dan de huidige 0.75g case;
- stress mean bruikbaar en max stress alleen als hotspot/sensitivity.

Gedetailleerde interpretatie van de 0.25g, 0.60 s beweging staat in:

```text
docs/report_notes/comsol_pipeline/model_justification/stage1_025g_dynamic_motion_interpretation.md
```

### Diagnostic: oude gravity-only transient

```text
stage1_gravity_only_reference.toml
```

Deze case rekent door, maar liet een golvende displacementcurve zien. Dat is waarschijnlijk transient ringing doordat gravity in een time-dependent run wordt opgebouwd en het fixed-support breast model daarna rond het statische evenwicht natrilt.

Gebruik deze case voorlopig als diagnostic, niet als de cleane static sag reference.

Outputmap:

```text
outputs/output_stage1_gravity_only_reference
```

### Diagnostic / niet report-ready: oude prescribed support jump

```text
baseline_simple_gland_dynamic_solid_only.toml
```

Deze baseline gebruikt:

- `dynamic_motion_mode = "prescribed_support_displacement"`;
- `dynamic_motion_profile = "febio_parabolic_support"`.

Probleem:

- de solve stopte rond de start van de jump;
- support displacement werd niet betrouwbaar nonzero in de export;
- de review time lag eerst voor de echte jump;
- displacementcurves waren daardoor niet geschikt als report-ready jump/bounce resultaat.

Gebruik deze case alleen als historische baseline/diagnostic, niet als hoofdroute voor Stage 1 dynamic displacement.

Outputmap:

```text
outputs/output_baseline_simple_gland_dynamic_solid_only
```

### Diagnostic / fallback: smooth support motion

```text
stage1_smooth_support_motion.toml
```

Deze case probeert een gladdere prescribed support displacement:

- `dynamic_motion_mode = "smooth_support_displacement"`;
- `dynamic_motion_profile = "smooth_cosine_bump"`.

Probleem:

- deze route stopte nog steeds rond de jump-start;
- support displacement bleef nul in de beschikbare metrics;
- daarmee is support-relative displacement niet betrouwbaar.

Gebruik deze alleen als fallback als de fixed-support acceleration pulse niet bruikbaar blijkt en je support motion expliciet wilt debuggen.

Outputmap:

```text
outputs/output_stage1_smooth_support_motion
```

### Oud/simple static reference

```text
full_baseline_reference_simple_gland_static_baseline.toml
```

Dit is de oudere simple/reference baseline. Hij is nuttig als simpele vergelijking, maar bevat niet dezelfde nieuwe displacement/postprocess-instrumentatie als de nieuwste Stage 1 candidates.

Outputmap:

```text
outputs/output_full_baseline_reference_simple_gland_static_baseline
```

## Postprocess en plots

De clean evaluatie-output staat hier:

```text
analysis_output/comsol_pipeline/stage1_baseline
```

Belangrijke tabellen:

```text
analysis_output/comsol_pipeline/stage1_baseline/tables/case_status.csv
analysis_output/comsol_pipeline/stage1_baseline/tables/review_metrics.csv
```

Belangrijke figuren:

```text
analysis_output/comsol_pipeline/stage1_baseline/figures/surface_vertical_dynamic_response.png
analysis_output/comsol_pipeline/stage1_baseline/figures/landmark_nipple_vertical_dynamic_response.png
analysis_output/comsol_pipeline/stage1_baseline/figures/stress_evolution.png
analysis_output/comsol_pipeline/stage1_baseline/figures/contact_sheet.png
```

Voor `stage1_fixed_support_acceleration_pulse.toml` staat image export bewust uit:

```text
postprocess_export_plot_images = false
postprocess_save_postprocessed_mph = false
```

Daardoor test `postprocess-only` eerst alleen metrics/CSV export. Dat is sneller en voorkomt dat automatische PNG export de metrics blokkeert.

## Aanbevolen volgorde nu

### 1. Laat de huidige postprocess-only run uitlopen

Command:

```powershell
python -m ews_fem_pipeline_comsol postprocess-only runs\comsol_runs\geometry_stage1\stage1_fixed_support_acceleration_pulse.toml
```

Deze run start geen solve. Hij laadt de bestaande `.mph` en probeert metrics/CSV's te schrijven.

Als dit lukt, draai:

```powershell
python tools\make_comsol_evaluation_plots.py
```

Kijk daarna vooral naar:

```text
analysis_output/comsol_pipeline/stage1_baseline/tables/case_status.csv
analysis_output/comsol_pipeline/stage1_baseline/figures/landmark_nipple_vertical_dynamic_response.png
analysis_output/comsol_pipeline/stage1_baseline/figures/surface_vertical_dynamic_response.png
```

### 2. Run daarna de quasi-static gravity sag reference

Alleen als je een nieuwe solve wilt draaien:

```powershell
python -m ews_fem_pipeline_comsol run runs\comsol_runs\geometry_stage1\stage1_quasistatic_gravity_sag_reference.toml
python tools\make_comsol_evaluation_plots.py
```

Deze is bedoeld als betere report-ready gravity/sag baseline dan `stage1_gravity_only_reference.toml`.

### 3. Pas daarna beslissen

Beslis na de plots:

- als quasi-static gravity stabiel is: gebruik die als Stage 1 static reference;
- als acceleration pulse een duidelijke nipple/surface `w` response geeft: gebruik die als exploratory dynamic bounce sensitivity;
- als acceleration pulse nog te zwaar/onduidelijk is: maak mildere pulse variants, bijvoorbeeld `0.25g`, `0.5g`, langere pulse duration of hogere damping;
- gebruik prescribed support motion pas weer als aparte diagnostic, niet als hoofdroute.

## Hoe lang duurt de huidige postprocess-only run?

De bestaande acceleration-pulse result file is ongeveer 1.68 GB. De postprocess laadt deze file en evalueert volume-, surface- en landmark-series. Dat kan duidelijk langer duren dan gewone plotgeneratie.

Praktische verwachting:

- als de COMSOL license goed pakt en de file normaal laadt: vaak tientallen minuten;
- de ingestelde timeout is `postprocess_timeout_s = 3600`, dus maximaal ongeveer 1 uur;
- als hij opnieuw snel faalt door license/server, staat dat direct in de postprocess log;
- als hij lang blijft hangen zonder statusmarkers, dan zit hij waarschijnlijk bij model loading voordat Java stdout wordt teruggegeven.

Na afloop moet minimaal verschijnen:

```text
outputs/output_stage1_fixed_support_acceleration_pulse/solve/stage1_fixed_support_acceleration_pulse_metrics.json
outputs/output_stage1_fixed_support_acceleration_pulse/solve/stage1_fixed_support_acceleration_pulse_time_series.csv
```

Als die bestanden verschijnen, is de postprocess-route geslaagd en kunnen de clean evaluation plots worden bijgewerkt.
