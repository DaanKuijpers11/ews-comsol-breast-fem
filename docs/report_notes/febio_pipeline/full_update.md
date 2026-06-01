# FEBio Full Update

## Role in the project

FEBio remains the main biomechanical reference and the main place where staged model improvements are judged.

## Current stage interpretation

- stage 1: outer geometry variation
- stage 2: material calibration
- stage 3: posterior support refinement
- stage 4: nipple or anterior stabilization before stronger asymmetry

## Main outcome so far

The FEBio work is now much clearer than before:

- stage 1 proved that controlled geometry changes matter mechanically
- stage 2 showed that the old soft baseline was likely too compliant
- stage 3 showed that support-domain refinement is feasible, even if the current effect is still modest
- stage 4 is now framed as anterior realism first, asymmetry second

## Best current FEBio baseline

The safest working reference is still the stage-2 intermediate material direction, with later stage-3 and stage-4 refinements judged against that core.

## Architectural choice

The shared Python or FEBio preparation route should stay the source of truth for reportable geometry changes.
COMSOL can inform ideas, but should not define the main model geometry on its own.

## Current priorities

1. keep the stage line interpretable
2. refine support and anterior realism before overselling asymmetry
3. continue realism improvements in the shared source model first
