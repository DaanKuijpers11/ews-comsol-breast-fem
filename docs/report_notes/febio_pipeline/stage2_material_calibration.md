# FEBio Stage 2 Material Calibration

## What stage 2 became

Stage 2 started as a follow-up geometry direction after stage 1, but its most useful final outcome became a material-calibration stage on a fixed reference geometry.

## Cases that matter most

- `stage2_reference_refinedmesh`
- `stage2_reference_intermediate_materials`
- `stage2_reference_chen_materials`

## Main result

Moving from the older softer baseline to the harder Chen-inspired material probe:

- strongly reduced displacement
- increased stress sharply
- improved `J`

This showed that:

- the old soft baseline was likely too compliant
- the full Chen-style stiffness was informative, but too aggressive as a direct default for the current simplified anatomy

## Best stage-2 interpretation

The clean stage-2 outcome is therefore:

- soft baseline: too compliant
- full Chen-inspired probe: too stiff for the present structure
- intermediate material baseline: best current compromise

## Why the intermediate baseline is preferred

It keeps the useful direction of the literature-based stiffening without forcing the model to compensate too much for missing support structures and remaining anatomical simplifications.

## Geometry direction learned during stage 2

The early stage-2 planning also clarified that future geometry realism should still be added in a controlled shared-pipeline way:

- refine the outer contour further
- improve chest-wall and anterior geometry carefully
- refine mesh density in a targeted way

But the stable report line from stage 2 is the material ladder, not a large geometry sweep.

## Practical conclusion

Stage 2 should be remembered mainly as the calibration stage that produced the current preferred FEBio material core for later stages.
