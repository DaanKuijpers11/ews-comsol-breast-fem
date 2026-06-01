# Tier 1 Case Definition Summary

The current Tier 1 COMSOL comparison is a stage-effect sanity set, not yet a complete final sensitivity set for every stage.

## What Each Stage Adds

| Stage | Current Tier 1 case | What it adds | Important interpretation |
|---|---|---|---|
| Stage 1 | `stage1_fixed_support_acceleration_pulse_mild_025g.toml` | 0.25g fixed-support dynamic motion on the simple baseline model | Motion sanity baseline; not directly comparable to Stage 2-5 anatomy because volume and geometry differ. |
| Stage 2 | `stage2_chestwall_xoffset_055_autoalign_vp_fixed_order2.toml` | Selected transverse x-offset chestwall, volume-preserving and auto-aligned | First fair geometric baseline for later stages. |
| Stage 3 | `stage3_glandular_realistic_spread_reference_xoffset055_preview.toml` | Realistic chestwall-aware glandular lobule reference on Stage 2 geometry | Adds anatomical glandular realism; effect is modest in the current review-time metrics. |
| Stage 4 | `stage4_realistic_reference_xoffset055_preview.toml` | Reference case for asymmetry stage, with asymmetry disabled | Control case only. It is expected to be almost identical to Stage 3. |
| Stage 5 | `stage5_reference_no_cooper_xoffset055_025g_preview.toml` | No-Cooper control for Cooper stage | Control case only. It is expected to be almost identical to Stage 4 reference. |

## Why Stage 3, Stage 4, And Stage 5 Look Similar

They are mostly the same model in this Tier 1 comparison:

- Stage 3 introduces the realistic reference glandular structure.
- Stage 4 reference keeps that same structure and has `profile_asymmetry_enabled = false`.
- Stage 5 no-Cooper keeps that same structure and has `enable_cooper_ligament_scaffold = false`.

So the current Stage 4 and Stage 5 rows are not meant to show an asymmetry or Cooper effect yet. They are control/reference runs used to make later Stage 4 asymmetry and Stage 5 Cooper sensitivities fair.

## Quantitative Snapshot

From `analysis_output/comsol_pipeline/tier1_comparison/tables/summary_results.csv`:

| Case | Breast volume (ml) | Glandular fraction (%) | Review avg displacement (mm) | Review surface dynamic w (mm) | Breast mean VM (kPa) | Breast max VM (kPa) |
|---|---:|---:|---:|---:|---:|---:|
| Stage 1 0.25g baseline | 718.715 | 11.944 | 17.758 | -1.129 | 0.433 | 10.733 |
| Stage 2 xoffset055 chestwall | 585.060 | 9.162 | 3.033 | 0.133 | 0.311 | 1.348 |
| Stage 3 realistic glandular | 585.087 | 24.308 | 2.998 | 0.161 | 0.324 | 1.674 |
| Stage 4 realistic reference | 585.087 | 24.309 | 2.997 | 0.161 | 0.324 | 1.633 |
| Stage 5 no-Cooper control | 585.087 | 24.309 | 2.998 | 0.161 | 0.324 | 1.615 |

## Current Limitation

The current Stage 5B default Cooper run failed almost immediately and is not report-ready. A stable mild/default Cooper variant is still needed before the Cooper effect can be evaluated dynamically.

