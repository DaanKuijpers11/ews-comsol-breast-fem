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

Many of these cases were built, solved or post-processed during development, but
not all generated output folders were retained after repository cleanup. The
counts below therefore describe the preserved case-definition library. For
completed result evidence, use the retained analysis summaries in
`analysis_output/comsol_pipeline/` and the final report figures/tables.

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
conservative way to describe the capacity is to count only the discrete options
that were explicitly included as tests, scouts or report case families. This is
different from the full mathematical parameter space. Most components are
defined by numerical parameters, so their location, size, scale, stiffness,
offset or amplitude can in principle be changed in many more ways than the
finite examples listed below.

This is an important difference from a single manually edited COMSOL model. The
current pipeline does not only store one breast geometry. It stores a scripted
route for generating breast-model variants by changing TOML settings. The table
below therefore counts a conservative set of implemented test options, while the
actual generator can create many more variants if additional parameter values
are sampled.

| Model component | Implemented option family | Conservative count |
|---|---|---:|
| Anatomical tissue layout | Simple glandular model plus realistic compact, reference and wide glandular distributions | 4 |
| Outer shape and nipple placement | Reference outer shape, profile/asymmetry scout, lateral nipple shift and superior nipple shift | 4 |
| Posterior support curvature | Slab/reference support, transverse curved support, sagittal/combined curvature scouts and selected transverse-offset reference | 4 |
| Skin/material route | No skin, thin soft skin, 1.5 mm soft skin, 1.5 mm intermediate skin and 1.5 mm stiff skin | 5 |
| Cooper-like support | No support, mild support, low-area skin-gland support, reference-area skin-gland support, nipple-chestwall support and dense skin-gland support | 6 |
| Dynamic loading | Gravity/static checks, fixed-support acceleration amplitudes and prescribed support-displacement scouts | 8 |
| Tumor overlay state | No tumor plus 3 tumor sizes across 5 placement families | 16 |

The counts in this table should be read as included option examples:

- The anatomical layout count includes only the simple glandular case and three
  realistic glandular spread variants. In principle, the glandular region can be
  changed by adjusting lobule number, lobule spread, seed position, scaling,
  volume fraction and posterior clearance.
- The outer-shape and nipple-placement count includes the implemented reference,
  asymmetry and nipple-shift examples. The same parameter route can also be used
  to sample different breast widths, projections, inferior fullness, lateral
  fullness and nipple coordinates.
- The posterior-support count includes the main support-curvature examples used
  in the project. The curve depth, curvature direction, centre offset and support
  alignment are numerical settings and can be sampled more finely if needed.
- The skin/material count includes only the finite skin routes used for the
  report comparison. Skin thickness and stiffness are numerical parameters, so a
  future study could sample them as continuous sensitivity variables.
- The Cooper-support count includes the implemented simplified support-selection
  routes. The support stiffness, area fraction, reference length and tangential
  scale can all be changed independently.
- The dynamic-loading count includes the loading routes and amplitudes used in
  the project. The pulse amplitude, pulse duration, support-displacement
  amplitude, support-displacement duration and damping value can be varied
  further.
- The tumor-overlay count includes the tested size and placement families. The
  tumor radius, centre position, stiffness contrast and material-coupling route
  are numerical inputs, so the number of possible tumor variants is effectively
  open-ended.

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

This number is a theoretical configuration count based on the included discrete
test families. It is useful for describing the flexibility of the pipeline, but
it does not mean that all combinations are scientifically meaningful, numerically
stable, solved, post-processed or validated.

## Why Material Parameters Are Not Counted as Unlimited Variants

Material stiffnesses, damping settings, exact chestwall offsets, mesh density,
tumor coordinates and loading amplitudes are numerical parameters. They can be
changed continuously or sampled at many levels. Counting every possible value
would make the model space effectively open-ended and would overstate the amount
of completed result data. For example, a tumor is not limited to only "small",
"medium" and "large"; those are the three sizes used in the project. The same
logic applies to skin thickness, Cooper-support stiffness, chestwall curvature
and dynamic input amplitude.

For reporting and handover, it is therefore clearer to separate:

- **Implemented discrete model families:** anatomy, skin route, Cooper-support
  route, loading route and tumor placement route.
- **Numerical sensitivity parameters:** material coefficients, stiffness
  contrasts, damping, mesh size, exact tumor position and loading amplitude.
- **Completed result cases:** the subset of TOML cases that were actually built,
  solved and post-processed.

## Interpretation

The case counts and combination estimates in this note describe model-generation
capacity. They should not be used as counts of validated models or final result
cases. The main practical distinction is:

- The 113 TOML files describe the preserved case-definition library.
- The discrete combination count describes how many model definitions can be
  created by recombining the implemented option families.
- The actual parameter space is larger because many settings are numerical and
  can be sampled at arbitrary values.
- The quantitative result set is smaller and is limited to the cases that were
  selected for solving, post-processing and reporting.

This makes the pipeline useful for systematic future model generation: new cases
can be created by copying an existing TOML file, changing one or more parameters
and rerunning the same build/solve/post-processing route.
