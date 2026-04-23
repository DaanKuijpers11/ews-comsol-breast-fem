# Manual Lobule Improvement Plan

Dit document legt uit hoe je van **willekeurige lobule-plaatsing** naar een **anatomisch realistisch model** gaat.

---

## 🔍 Huidige situatie: Manual Lobules (WILLEKEURIG)

In `manual_lobules_balanced.toml` staan nu 3 willekeurige punten:

```toml
[[material.glandular.hetero.lobules]]
center = [ 0.02, 0.045, 0.0,]        # Punt 1: willekeurig
center = [ 0.025, 0.047, 0.002,]     # Punt 2: willekeurig
center = [ 0.018, 0.043, -0.002,]    # Punt 3: willekeurig
```

**Probleem:** Dit ziet niet uit als een echte borst! Anatomisch gezien zou je:
- Een **radiale fan-structuur** rond de nipple verwachten
- Meerdere lobes (6-8 richtingen)
- Meer lobules per lobe (3-5)
- Steeds kleinere lobules naar buiten toe

---

## ✅ Oplossing: Systematische Plaatsing

Ik heb een Python script gemaakt: `scripts/generate_systematic_lobules.py`

Dit script genereert:
- **6 lobes** (radiale richtingen)
- **3 lobules per lobe** = 18 totaal
- Anatomisch correct: fan-vorm rond nipple
- Progressief kleinere widths naar buiten

### Gegenereerde configuratie

De nieuwe config heet: **`manual_lobules_systematic.toml`**

Dit bevat 18 structureel geplande lobules:

```toml
# Lobe 1 (onderste): 3 punten van nipple uitwaarts
[[material.glandular.hetero.lobules]]
center = [ 0.00000, 0.04308, -0.00335,]   # Dichtbij nipple
width = 0.003700                           # Groot

[[material.glandular.hetero.lobules]]
center = [ 0.00000, 0.05117, -0.00670,]   # Middel
width = 0.003400                           # Iets kleiner

[[material.glandular.hetero.lobules]]
center = [ 0.00000, 0.05925, -0.01005,]   # Ver weg
width = 0.003100                           # Klein

# Lobe 2, 3, 4, 5, 6 volgen hetzelfde patroon...
```

**Key differences:**
- ✅ Systematische spreiding (fan-vorm)
- ✅ Steeds kleinere widths met afstand (realistisch)
- ✅ Symmetrisch boven/onder de y-as
- ✅ Geen willekeurige plaatsing

---

## 🧪 Hoe te testen

### Stap 1: Run de nieuwe config

```powershell
cd c:\Users\20223231\ews_fem_clean
python -m ews_fem_pipeline_clean run runs\overnight_suite\manual_lobules_systematic.toml
```

Dit runt de simulatie met de nieuwe lobules. Output gaat naar `runs/overnight_suite/output/`.

### Stap 2: Analyseer de resultaten

```powershell
$env:RUN_NAME='manual_lobules_systematic'
python scripts/data_analysis_main.py
```

Dit genereert figuren en metrics.

### Stap 3: Vergelijk met baseline

Kijk naar de summary statistics CSV:
- `runs/overnight_suite/output/*_summary_statistics.csv`

Vergelijk deze metrics:

| Metric | Baseline | Manual (willekeurig) | Manual (systematisch) | Verwacht |
|--------|----------|----------------------|----------------------|----------|
| vm_max | ~2100    | ~2000                | ? (test dit!)         | ≤2000 = goed |
| vm_mean| ~520     | ~510                 | ? (test dit!)         | ≈490 = beter |
| disp_max | ~0.035 | ~0.036               | ? (test dit!)         | ≈0.035 = stabiel |
| J_min  | ~0.989   | ~0.988               | ? (test dit!)         | >0.985 = stabiel |
| inverted_elements | 0 | 0               | ? (test dit!)         | 0 = perfect |

---

## 📊 Wat je kan aanpassen voor verdere verbetering

In `scripts/generate_systematic_lobules.py` kun je parameters tunen:

```python
lobules = generate_systematic_lobules(
    n_lobes=6,              # Hoeveel lobe-richtingen? (6-8 realistisch)
    n_per_lobe=3,           # Hoeveel lobules per lobe? (3-5 realistisch)
    nipple=(0.0, 0.035, 0.0),
    lobe_length=0.035,      # Hoe ver strekken? (0.03-0.04 realistisch)
    spread_angle=45.0,      # Hoeveel graden fan? (45-60 realistisch)
)
```

Wil je meer lobules? Verander `n_per_lobe=4` of `n_lobes=8`, run het script opnieuw, en test.

---

## 🚀 Volgende stappen na deze test

1. **Test `manual_lobules_systematic`** en vergelijk metrics
2. **Fijn-tune parameters** via het script (bv. meer lobes, andere spreiding)
3. **Daarna:** Kijk naar `auto_generate=true` optie (die doet dit automatisch!)

---

## 💡 Waarom dit beter is

| Aspect | Willekeurig | Systematisch |
|--------|-----------|-------------|
| Reproductibiliteit | ❌ Moeilijk | ✅ Script bepaalt alles |
| Anatomisch realistisch | ❌ Nee | ✅ Ja (fan-vorm) |
| Testbaar | ❌ Waar staan ze? | ✅ Logische richtingen |
| Schaalbaar | ❌ Handmatig | ✅ Verander n_lobes, klaar |
| Valideerbaar | ❌ Random | ✅ Vergelijkbare cases |

---

## 📝 Samenvatting

**Wat je nu hebt:**
- `scripts/generate_systematic_lobules.py` - Generator script
- `manual_lobules_systematic.toml` - Test config (18 systematische lobules)
- Dit document - Stap-voor-stap uitleg

**Wat je doet:**
1. Run `manual_lobules_systematic.toml`
2. Kijk naar metrics
3. Vergelijk met baseline
4. Pas script aan voor verdere tuning als nodig

**Doel:** Een reproduceerbaar, anatomisch realistisch model in plaats van willekeurige punten.
