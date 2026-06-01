# FEBio Stage 1 Geometry

## Goal

Test whether modest outer-envelope changes in the shared FEBio source model:

- run stably
- produce interpretable displacement and stress changes
- keep acceptable `J` behavior

## Main completed comparison cases

- `baseline_stage1_reference`
- `elliptic_mild_projection`
- `elliptic_projected_compact_pole`
- `elliptic_mild_projection_order2_probe`

## Main findings

### Geometry clearly matters

Both successful elliptic variants increased peak displacement relative to the baseline.
The more compact or projected profile gave the strongest displacement response.

### Stress is more sensitive than displacement

The geometry changes increased stress more strongly than displacement, especially in the mild-projection case.

### Stability stayed acceptable

The successful comparison cases remained solver-stable and did not show inverted elements.
The most aggressive projected case did push `J` lower, so stronger shape changes should still be treated carefully.

### Order-2 was useful as a probe, not yet as the main comparison set

The order-2 probe ran successfully, but its stress values should not yet be treated as directly interchangeable with the order-1 results.

## Practical conclusion

Stage 1 successfully established a reportable outer-geometry variation line in the shared FEBio pipeline.
That made it reasonable to continue later stages from a cleaner and more explicit geometry base.
