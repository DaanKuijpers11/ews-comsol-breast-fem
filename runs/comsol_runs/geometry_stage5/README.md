# Stage 5 - Cooper Ligament Support

Stage 5 is the Cooper/fibrous-support sensitivity stage. The active route is now based on the current reference stack:

- Stage 1: fixed-support `0.25g` acceleration pulse for `0.60 s`;
- Stage 2: transverse x-offset `0.055 m`, volume-preserving, auto-aligned chestwall;
- Stage 3: realistic reference lobule spread;
- Stage 4 asymmetry is not included in the default Stage 5 stack.

The current Cooper implementation is a COMSOL boundary-load surrogate, not an explicit anatomical ligament network. It applies displacement-proportional restoring loads on selected anterior skin/gland/subareolar patches.

## Active Preview Cases

| TOML | Role | Main Cooper setting |
| --- | --- | --- |
| `stage5_reference_no_cooper_xoffset055_025g_preview.toml` | no-Cooper control | scaffold disabled |
| `stage5b_cooper_mild_xoffset055_025g_preview.toml` | lower-support sensitivity | Stage 5B, area fraction `0.06` |
| `stage5b_cooper_default_xoffset055_025g_preview.toml` | preferred default | Stage 5B, area fraction `0.12` |
| `stage5b_cooper_stiff_xoffset055_025g_preview.toml` | upper-support sensitivity | Stage 5B, area fraction `0.24` |
| `stage5b_cooper_damped_xoffset055_025g_preview.toml` | damping diagnostic | Stage 5B default stiffness plus `1e5 N*s/m^3` damping |
| `stage5c_dense_network_diagnostic_xoffset055_025g_preview.toml` | dense-web diagnostic | dense label, area fraction `0.18`, still patch-load based |

The older `stage5_variant_a/b/c` TOMLs are kept as legacy/simple-baseline references. Do not use them as the current final Stage 5 route unless you intentionally want the older slab/simple setup.

## Parameter Meaning

Effective normal support is approximately:

`cooper_spring_ky = cooper_ligament_effective_modulus_pa * cooper_ligament_area_fraction / cooper_ligament_reference_length_m`

For the default values `5.8 MPa * 0.12 / 0.04 m`, this gives about `17.4e6 N/m^3`. The skin and gland patch loads use scaled versions of this value.

- `cooper_ligament_effective_modulus_pa`: material-scale stiffness assumption. Literature values are uncertain, so avoid treating this as a directly calibrated anatomical modulus.
- `cooper_ligament_area_fraction`: effective active support fraction. This is the cleanest current sensitivity parameter because it scales support strength without changing geometry.
- `cooper_ligament_reference_length_m`: effective ligament length. Shorter length means stronger support for the same modulus and area.
- `cooper_ligament_tangential_scale`: x/z support relative to the main anterior-posterior support. High values risk artificial lateral pinning.
- `cooper_ligament_damping_pa_s_per_m`: optional velocity-proportional damping. Keep diagnostic until verified; it can reduce ringing but may also over-smooth motion.

## Build-Only Command

```cmd
python -m ews_fem_pipeline_comsol build-only runs\comsol_runs\geometry_stage5\stage5_reference_no_cooper_xoffset055_025g_preview.toml runs\comsol_runs\geometry_stage5\stage5b_cooper_mild_xoffset055_025g_preview.toml runs\comsol_runs\geometry_stage5\stage5b_cooper_default_xoffset055_025g_preview.toml runs\comsol_runs\geometry_stage5\stage5b_cooper_stiff_xoffset055_025g_preview.toml runs\comsol_runs\geometry_stage5\stage5b_cooper_damped_xoffset055_025g_preview.toml runs\comsol_runs\geometry_stage5\stage5c_dense_network_diagnostic_xoffset055_025g_preview.toml
```

## Checks

Before solves, inspect:

- `anterior_skin_bnd` highlights only the outer breast/skin surface patch, not internal lobule surfaces;
- `anterior_gland_bnd` highlights the broad glandular/lobule boundary selection, not only the central anterior lobules;
- `anterior_gland_box_bnd` is only the broad helper/search box and is not the actual Stage 5B load target;
- `nipple_support_bnd` only matters for Stage 5A/legacy nipple tether, not the default Stage 5B route;
- Cooper loads are present for 5B cases and absent for the no-Cooper control;
- no legacy `08/09/10` Cooper arrow screenshots are required for postprocessing;
- the realistic glandular structure remains clipped inside `breast_union`.

Stage 5B default is the preferred end-model route if it builds cleanly and later solves without artificial pinning or ligament-dominated stress hotspots.
