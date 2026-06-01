# Stage 4 - Realistic Asymmetry Previews

Stage 4 is now the asymmetry/nipple-position stage on top of the current selected model stack:

- Stage 1 motion: fixed-support `0.25g` acceleration pulse;
- Stage 2 geometry: transverse x-offset chestwall with `chestwall_curve_center_x_offset_m = 0.055`;
- Stage 3 glandular model: realistic reference lobule spread;
- Cooper scaffold: Stage 5B glandular-to-skin setting.

The old simple-gland and older refined/asymmetry experiments were archived without deletion in:

`_archive_stage4_legacy_2026-05-22/`

The old README is preserved there as:

`_archive_stage4_legacy_2026-05-22/README_legacy_before_2026-05-22.md`

## Active Cases

| Case | Main change | Role |
| --- | --- | --- |
| `stage4_realistic_reference_xoffset055_preview.toml` | no extra asymmetry | clean reference for Stage 4 |
| `stage4_realistic_profile_asym_xoffset055_preview.toml` | near-volume-preserving outer profile scaling, `outer_shape_scale_x = 1.16`, `outer_shape_scale_z = 0.931` | outer-envelope/profile asymmetry preview |
| `stage4_realistic_nipple_lateral_xoffset055_preview.toml` | `nipple_geometry_x_offset_m = 0.010` | lateral nipple-position sensitivity |
| `stage4_realistic_nipple_superior_xoffset055_preview.toml` | `nipple_geometry_z_offset_m = 0.010` | superior/inferior nipple-position sensitivity |

The nipple offset parameters move the final COMSOL nipple geometry in x/z after the Stage 2 projected-normal chestwall alignment. The realistic lobules, ducts, subareolar core, and bridge are translated with the final nipple axis through `lobule_alignment_dx_m` and `lobule_alignment_dz_m`.

The asymmetric/profile and shifted-nipple previews enable `nipple_surface_normal_alignment_enabled = true`. This computes the local normal of the outer breast ellipsoid at the shifted nipple anchor and uses that direction for the nipple cap and subareolar helper geometry. The clean reference keeps this disabled because it has no extra nipple shift and the global-y placement is less fragile for the baseline mesh.

The old additive `outer_lateral_fullness` and `outer_inferior_fullness` routes remain disabled because they produced loose/faulty extra volumes in `breast_union`. Use the profile-scaling case as the current clean outer-envelope asymmetry route.

## Build-Only Checks

The four active cases generated successfully:

- 6079 checked elements;
- min quality `0.1061`;
- poor quality elements `<0.1`: `0`.

Before any solve, inspect the build-only MPH/screenshots for:

- nipple cap sits on the outer breast surface;
- glandular lobules/core/bridge follow the shifted nipple axis;
- glandular tissue remains clipped inside the breast;
- breast volume, glandular volume, and glandular fraction are close enough to the reference for the intended comparison once postprocess/metrics are available;
- no detached outer-envelope/fullness volume is present;
- chestwall contact still looks clean with the Stage 2 xoffset055 support.

Build-only is for geometry inspection only. Extra geometry-volume metrics during the builder run are disabled for now because COMSOL may block writing extra metrics files in class-based build mode.

## Commands

Build-only all active Stage 4 previews:

```cmd
python -m ews_fem_pipeline_comsol build-only runs\comsol_runs\geometry_stage4\stage4_realistic_reference_xoffset055_preview.toml runs\comsol_runs\geometry_stage4\stage4_realistic_profile_asym_xoffset055_preview.toml runs\comsol_runs\geometry_stage4\stage4_realistic_nipple_lateral_xoffset055_preview.toml runs\comsol_runs\geometry_stage4\stage4_realistic_nipple_superior_xoffset055_preview.toml
```

Only after the build-only geometry and volume checks look good, a short run can be considered:

```cmd
python -m ews_fem_pipeline_comsol run runs\comsol_runs\geometry_stage4\stage4_realistic_reference_xoffset055_preview.toml runs\comsol_runs\geometry_stage4\stage4_realistic_profile_asym_xoffset055_preview.toml
```

Keep nipple-offset cases build-only until the shifted nipple/gland alignment has been visually checked.
