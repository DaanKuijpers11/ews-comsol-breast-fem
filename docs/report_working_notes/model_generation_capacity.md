# Model Generation Capacity

This note gives a compact estimate of how many model variants the current COMSOL
pipeline can define. The goal is not to claim that all variants have been solved
or validated, but to show the practical model-generation capacity created during
the project.

## Existing Case Library

The repository currently contains 113 COMSOL case-definition files in
`runs/comsol_runs/`. These TOML files include reference cases, scout cases,
build-only checks, sensitivity cases and post-processing targets. They should be
interpreted as a case library, not as 113 fully solved and validated final
models.

| Case group | Number of TOML cases |
|---|---:|
| `dynamic_realism_branch` | 7 |
| `geometry_stage1` | 11 |
| `geometry_stage2_chestwall` | 11 |
| `geometry_stage3` | 3 |
| `geometry_stage4` | 4 |
| `geometry_stage5` | 38 |
| `geometry_stage5_1_motion_scout` | 8 |
| `geometry_stage6` | 9 |
| `material_parameter_sensitivity` | 6 |
| `report_fixed_material_suite` | 6 |
| `sandbox_testcases` | 10 |
| **Total** | **113** |

## Discrete Variant Families

The model generator can combine several implemented option families. A
conservative way to describe the capacity is to count only discrete options that
were implemented or used as project case families, while excluding arbitrary
continuous parameter sweeps.

| Model component | Implemented option family | Conservative count |
|---|---|---:|
| Anatomical tissue layout | Simple glandular model plus realistic compact, reference and wide glandular distributions | 4 |
| Outer shape and nipple placement | Reference outer shape, profile/asymmetry scout, lateral nipple shift and superior nipple shift | 4 |
| Posterior support curvature | Slab/reference support, transverse curved support, sagittal/combined curvature scouts and selected transverse-offset reference | 4 |
| Skin/material route | No skin, thin soft skin, 1.5 mm soft skin, 1.5 mm intermediate skin and 1.5 mm stiff skin | 5 |
| Cooper-like support | No support, mild support, low-area skin-gland support, reference-area skin-gland support, nipple-chestwall support and dense skin-gland support | 6 |
| Dynamic loading | Gravity/static checks, fixed-support acceleration amplitudes and prescribed support-displacement scouts | 8 |
| Tumor overlay state | No tumor plus 3 tumor sizes across 5 placement families | 16 |

Using only the broad structural, support, loading and tumor families gives an
order-of-magnitude capacity of:

```text
4 tissue layouts
x 4 outer/nipple options
x 4 posterior-support options
x 6 Cooper-support options
x 8 dynamic-loading options
x 16 tumor states
= 49,152 discrete model definitions
```

Including the finite skin/material routes used in the report as an additional
factor gives:

```text
49,152 x 5 = 245,760 discrete model definitions
```

This number is a theoretical configuration count. It is useful for describing
the flexibility of the pipeline, but it does not mean that all combinations are
scientifically meaningful, numerically stable, solved, post-processed or
validated.

## Why Material Parameters Are Not Counted as Unlimited Variants

Material stiffnesses, damping settings, exact chestwall offsets, mesh density,
tumor coordinates and loading amplitudes are numerical parameters. They can be
changed continuously or sampled at many levels. Counting every possible value
would make the model space effectively open-ended and would overstate the
amount of completed result data.

For reporting and handover, it is therefore clearer to separate:

- **Implemented discrete model families:** anatomy, skin route, Cooper-support
  route, loading route and tumor placement route.
- **Numerical sensitivity parameters:** material coefficients, stiffness
  contrasts, damping, mesh size, exact tumor position and loading amplitude.
- **Completed result cases:** the subset of TOML cases that were actually built,
  solved and post-processed.

## Suggested Report or Presentation Wording

The COMSOL pipeline now supports a combinatorial set of model variants rather
than a single manually edited model. In the cleaned repository, 113 TOML case
definitions are stored as reference, scout, build-check and sensitivity cases.
When the implemented anatomical, support, loading and tumor-option families are
combined conservatively, the pipeline can define on the order of tens of
thousands of discrete model configurations. If the finite skin/material routes
used in the report are also included, this rises to approximately
2.5e5 possible discrete definitions. This number should be interpreted as model
generation capacity only; the completed quantitative results remain limited to
the solved and post-processed case subset.

