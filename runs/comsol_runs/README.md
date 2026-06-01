# COMSOL run configurations

This folder contains the active TOML configuration files for the staged COMSOL
breast-model pipeline. The TOMLs are provenance and should remain versioned.
Generated build, solve, mesh, MPH, recovery, and COMSOL cache artefacts should
stay local and outside GitHub.

## Active staged route

### `geometry_stage1`
Motion sanity baseline. The current reference is the mild fixed-support
acceleration pulse:

- 0.25g
- 0.60 s pulse duration
- mass damping alpha = 60 1/s

Stage 1 is useful for checking the dynamic input, but it is not the final
anatomical reference because the geometry and volume differ from later stages.

### `geometry_stage2_chestwall`
Selected chestwall route. The current report route is the transverse x-offset
chestwall curvature around `xoffset055`, with volume preservation and
nipple/gland auto-alignment. This is the first fair anatomical baseline for the
later stages.

### `geometry_stage3`
Realistic glandular reference route. The active report direction is the
chestwall-aware realistic lobule spread on top of the Stage 2 xoffset055
geometry. This is the current anatomical/glandular reference.

### `geometry_stage4`
Asymmetry and nipple-position sensitivity. The Stage 4 reference is intentionally
close to Stage 3 and acts as a no-asymmetry control. Asymmetry cases are
sensitivity cases unless their complete dynamic output is verified.

### `geometry_stage5`
Cooper ligament support sensitivity. The no-Cooper case is the current stable
control. Cooper ligament variants should be interpreted cautiously until a
stable full dynamic Cooper case is available.

### `geometry_stage6`
Tumor/lesion sensitivity. Current cases use build-only/screening first and then
selected dynamic runs. The current implementation is an analytic tumor-mask
material overlay rather than a separate tumor COMSOL domain.

## Secondary or review folders

### `material_parameter_sensitivity`
Material sensitivity work. Keep if still used for comparison or future material
calibration; otherwise treat as review material rather than the main report
route.

### `report_fixed_material_suite`
Older fixed-material report suite. Keep only if specific plots or tables are
still referenced by the report.

### `dynamic_realism_branch`
Exploratory dynamic realism tests. Keep as review material unless promoted into
the staged route.

### `sandbox_testcases`
Small scratch/test TOMLs. Keep only if they are still useful for quick pipeline
checks.

### `legacy_suite`
Historical exploratory COMSOL cases and probes. This folder is not part of the
active Stage 1-6 route and contains large generated COMSOL artefacts. It is a
delete/archive candidate after any useful TOMLs or notes have been copied into
the active staged folders or docs.

### `evaluation_contact_sheets`
Old generated contact-sheet PNGs. The active report contact sheets now live in
`docs/Traineeship_report___Daan_Kuijpers/Figures/comsol_contact_sheets`. This
folder is a delete/archive candidate if no report or script references it.

## GitHub rule

Track:

- active TOMLs
- source code
- documentation/report notes
- small report-ready summary tables and plots

Do not track:

- `.mph`, `.recovery`, `.status`
- COMSOL configuration/cache folders
- generated `output*`, `build`, `solve`, and `prepare` folders
- large mesh exports and full run folders
