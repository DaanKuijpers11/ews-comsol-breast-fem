# Stage 1: Asymmetry

Use this stage to test visible outer-shape asymmetry while keeping:

- simple glandular structure
- dynamic loading
- no tumor
- no explicit ligaments yet

Recommended order:

1. `dr_stage1_reference_dynamic.toml`
2. `dr_stage1_axis_asymmetry_probe.toml`
3. `dr_stage1_profile_asymmetry_probe.toml`
4. `dr_stage1_profile_compact_upper_pole.toml`
5. `dr_stage1_profile_projected_inferior_full.toml`

Visual validation:

- compare front and side views in COMSOL
- check whether the nipple region remains plausible
- inspect whether gravity settling still behaves smoothly

Important:

- the older `geometry.asymmetry.enabled` scaling mainly affects the internal gland ellipse, not the full outer breast envelope
- this branch therefore uses the safer outer-profile asymmetry controls instead of the older gland-only scaling
- the current generator still revolves a 2D profile, so these cases are visually distinct profile variants, not true left-right 3D asymmetry
