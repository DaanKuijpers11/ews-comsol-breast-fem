# COMSOL EWS Report Status Overview

Date: 2026-05-26

## Current Report-Ready Narrative

The COMSOL EWS FEM pipeline can currently be reported as a controlled staged model-building workflow. The main claim should be that the pipeline parameterizes breast geometry, glandular layout, motion input, and early tumor/lesion sensitivity in a reproducible way. It should not yet be claimed as a patient-specific diagnostic detector.

## Current Standard Dynamic Input

- Motion route: fixed-support acceleration pulse.
- Amplitude: 0.25g.
- Pulse duration: 0.60 s.
- Damping: mass damping alpha = 60 1/s.
- Interpretation: mild platform/torso-like acceleration, not an exact jump.
- Report status: suitable as a gentle dynamic sanity input for controlled comparisons.

## Stage Status

| Stage | Current role | Report status | Main caveat |
|---|---|---|---|
| Stage 1 | Motion sanity baseline | Report as motion baseline only | Geometry/volume differs from later anatomical route |
| Stage 2 | Selected anatomical chestwall baseline | Report-ready as first fair geometry baseline | Chestwall curvature is patient-dependent sensitivity, not universal anatomy |
| Stage 3 | Realistic glandular reference | Report as realistic/diagnostic reference | Solver/postprocess robustness should be checked per run |
| Stage 4 | Asymmetry controls and nipple/gland alignment | Sensitivity/control status | Reference is intentionally near Stage 3; asymmetry cases need careful output checks |
| Stage 5 | No-Cooper control and Cooper sensitivity setup | No-Cooper control is useful; Cooper effect not proven yet | Default Cooper run failed; use Cooper as mechanical support sensitivity only |
| Stage 6 | Tumor/lesion overlay and screening | Build/fast-screening stage | Current tumor is analytic material overlay, not separate tumor domain |

## Current Tier 1 Interpretation

The Tier 1 comparison is a staged sanity comparison, not a complete sensitivity study. Stage 1 is intentionally separated conceptually from Stages 2-5 because its volume and simple geometry differ. Stage 2 is the first fair anatomical baseline. Stages 3-5 appear similar in Tier 1 because Stage 4 reference and Stage 5 no-Cooper are deliberately control cases that preserve the Stage 3 realistic reference.

Use `tier1_case_definition_summary_2026-05-26.md` as the concise Tier 1 explanation.

## Current Geometry Baseline

The preferred anatomical route is:

1. Stage 2 xoffset055 transverse chestwall, volume-preserving and auto-aligned.
2. Stage 3 realistic chestwall-aware glandular reference.
3. Stage 4 no-asymmetry control unless asymmetry is explicitly being tested.
4. Stage 5 no-Cooper control unless Cooper support is explicitly being tested.
5. Stage 6 tumor/lesion overlay on the same controlled reference.

The current Stage 2-5 breast volume is about 585 ml, which falls between the 25th percentile and median of the 100-sample volume dataset. This is a defensible lower-mid anatomical baseline.

## Stage 6 Tumor Reporting Position

The current tumor implementation uses:

- analytic spherical `tumor_mask`;
- no separate COMSOL tumor domain;
- no standalone `mat_tumor`;
- local modification of density and Mooney-Rivlin coefficients inside adipose/glandular material expressions;
- tumor-region displacement/stress metrics computed using mask-weighted integrals.

This should be described as a first-order lesion stiffness/location sensitivity, not as a segmented anatomical tumor reconstruction.

Useful Stage 6 report tests, after successful metrics are available:

1. Control/no tumor.
2. Central size sweep: 6 mm, 12 mm, 20 mm.
3. Location sweep: upper-outer, central, subareolar, posterior.
4. Stiffness sweep: mild vs stiff central lesion.

## Volume Reporting Position

The 100-sample breast-volume dataset has median 691 ml and mean 728 ml. It is broadly consistent with MRI-based literature around 700-740 cm3, but the contours were drawn approximately 5 mm inside the external boundary and in a lying position. Report them as conservative internal-envelope volumes rather than exact external upright breast volumes.

Recommended later volume sensitivity targets:

- 350-400 ml: small/P10-like.
- 520-600 ml: lower-mid/current baseline.
- 690-750 ml: median/mean.
- 900-950 ml: large/Q3-like.
- 1100-1250 ml: very large/P90-like sensitivity.

## What Is Ready To Use In The Report

- Motion input definition and interpretation.
- Stage 2 selected chestwall route.
- Tier 1 case-definition explanation.
- Breast-volume literature context and target-volume rationale.
- Stage 6 tumor/lesion build and screening rationale.
- Existing postprocess fields for displacement, signed surface displacement, landmarks, tissue stress, and tumor-mask metrics.

## What Should Stay As Limitation Or Future Work

- Patient-specific anatomy.
- Exact tumor detection claim.
- Separate tumor domain/material assignment.
- Robust Cooper ligament effect.
- Fully report-ready asymmetry sensitivity.
- Quantitative tumor effect until Stage 6 fast or full runs produce valid non-NaN metrics.

## Recommended Report Note Order

1. `report_status_overview_2026-05-26.md`
2. `tier1_case_definition_summary_2026-05-26.md`
3. `model_justification/stage1_025g_dynamic_motion_interpretation.md`
4. `model_justification/breast_volume_literature_context_2026-05-26.md`
5. `model_justification/stage6_tumor_lesion_plan_2026-05-26.md`
6. `comsol_extended_displacement_stress_exports.md`
7. `report_figures_metrics_index.md`

Use the remaining active notes as supporting material rather than primary report text.
