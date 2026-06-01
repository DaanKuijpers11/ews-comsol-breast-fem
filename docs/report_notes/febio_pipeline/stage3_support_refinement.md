# FEBio Stage 3 Support Refinement

## Goal

Stage 3 focuses on support and anatomy refinement, not on another large material sweep.

## Main idea

The stage-2 intermediate baseline became the stable starting point.
Stage 3 then tested whether posterior or pectoralis-like support changes could improve realism without destabilizing the pipeline.

## Cases compared

- `stage3_reference_support_baseline`
- `stage3_reference_support_baseline_refined150`
- `stage3_reference_support_baseline_refined175`
- `stage3_pectoral_support_slab_stable`
- `stage3_pectoral_support_curved_cap_stable`

## Main findings

### Mesh refinement changed little

The denser order-1 meshes changed displacement, stress, and `J` only modestly.
That suggests the stage-2 or stage-3 baseline is already reasonably mesh-stable at order 1.

### Support modelling is now working

Both support variants solved successfully and showed that support-domain refinement can be tested in a controlled way.

### The biomechanical effect is real but still modest

The support variants slightly reduced peak stress, but also slightly increased displacement.
So the current support shapes are active, but not yet a decisive realism improvement.

### Best current direction

The curved-cap support case is the better next-step shape direction because it is more literature-aligned and gave the lowest support-variant stress, even though it also gave the highest displacement of the sweep.

## Practical conclusion

Stage 3 achieved a useful first milestone:

- support-domain refinement is now part of the working FEBio stage line

But it did not yet produce a final preferred support solution.
