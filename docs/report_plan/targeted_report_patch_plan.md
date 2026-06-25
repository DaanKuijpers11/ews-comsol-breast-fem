# Targeted report patch plan

Scope: based on the uploaded PDF only. The source repository at `C:\Users\20223231\ews_fem_clean` is not mounted in this workspace, so these are exact manual patches rather than direct edits.

## Critical compile/layout issue

### Results Figure 7

The PDF shows the text `Figures/results/reference_surface_signed_vertical_response.png` inside a framed figure box instead of the actual image. Fix the Overleaf file path or remove this figure until the image exists.

Recommended quick fix if the file exists:

```latex
\begin{figure}[H]
\centering
\includegraphics[width=0.82\linewidth]{Figures/results/reference_surface_signed_vertical_response.png}
\caption{Example of the signed vertical surface-response output generated for the anatomical reference route. The figure illustrates the type of surface-motion metric used for later comparison between model variants.}
\label{reference_surface_response_example}
\end{figure}
```

If the image does not exist or is not good enough, remove Figure 7 and replace the preceding sentence with:

```latex
The generated analysis outputs included displacement fields, signed vertical surface response, displacement statistics and von Mises stress fields. These outputs were used to check that the model produced a measurable dynamic response under the fixed-support acceleration input before the separate model variants were compared.
```

## Materials and Methods

### Section 3.3.5 Tumor representation

Add this compact paragraph after the existing tumor material paragraph:

```latex
The tumor-overlay route was prepared for different tumor sizes and locations. The reported tumor cases included a medium upper-outer tumor in a simple-gland model with full \texttt{ews\_surface} and \texttt{internal\_tumor} output, and a corresponding realistic/volumetric-skin tumor route with lightweight global output. Additional build-level placements were generated for central, subareolar, posterior and surface-proximal positions. These cases were used to test whether the analytic mask could be placed and evaluated consistently, while the complete dynamic tumor-overlay sensitivity study was left for future work.
```

Rationale: this makes M&M consistent with the new Results without overloading Methods with all numeric output.

### Section 3.6 Post-processing and evaluation metrics

Add a short animation sentence near the end of the first paragraph or after the stress paragraph:

```latex
COMSOL animations were also generated for visual inspection of time-dependent deformation. These animations were used only as qualitative checks; the quantitative Results are based on the exported displacement, stress and surface-response metrics.
```

If a landmark/surface selection image exists and is visually clear, add it in Section 3.6. If not, do not add a placeholder. Recommended caption:

```latex
\caption{Example of the free-surface and landmark-patch selections used for surface-based post-processing. Patch-averaged landmark quantities were used to reduce sensitivity to individual mesh nodes.}
```

## Results

### Section 4.3 Effect of material-parameter variation

Replace the final paragraph with:

```latex
The material and skin-parameter variants produced large differences in peak displacement. Relative to the volumetric-skin reference case, the tested variants changed peak maximum displacement by approximately $+83.8\%$ to $+912.3\%$, depending on the selected internal and skin stiffness settings. When reported relative to the broader skin/material reference set, the peak displacement changes ranged from approximately $+35.0\%$ to $-75.6\%$. Peak maximum von Mises stress remained highest in the stiff skin-enabled cases and should be interpreted as a model stress-concentration metric rather than as a directly observable EWS output.
```

Check the exact reference used for the `+35.0% to -75.6%` range before final submission. If this is from a new postprocess table, cite that table/figure in the text.

### Section 4.4 Effect of volumetric skin representation

Keep the current table, but replace the last sentence with:

```latex
The higher maximum von Mises stresses in the skin-enabled cases were mainly used as indicators of local model stress concentrations, especially near stiff material interfaces and the posterior support region.
```

If adding the von Mises figure, keep it in 4.4 after the table. Current Figure 8 placement is appropriate.

### Section 4.5 Effect of Cooper-like support

Replace the last paragraph with:

```latex
In the simple-gland scout, the Cooper-like support variants produced only small global peak-displacement changes relative to the no-Cooper baseline. Across the tested support selections, peak displacement changed by approximately $-1.4\%$ to $+0.1\%$. These values describe the global response of the simplified support implementation and should not be interpreted as anatomical validation of Cooper-ligament mechanics.
```

### Section 4.6 Effect of dynamic loading route

The current 4.6.2 is consistent with M&M. No major change required. If adding a time-series plot, add it to 4.6 only if it is a clean dynamic-loading figure. Otherwise place the displacement time-series in 4.4 if it supports the skin/material comparison.

Recommended 4.6.2 addition, only if needed:

```latex
This route was evaluated as a separate motion variant and was not directly compared with the fixed-support acceleration cases, because the model basis and imposed motion definition were different.
```

### Section 4.7 Tumor-overlay sensitivity

Replace the current build-only text after Figure 9 with:

```latex
In addition to the build-level placement examples, two tumor-output routes were available for reporting. The simple-gland medium upper-outer tumor case included both \texttt{ews\_surface} and \texttt{internal\_tumor} outputs. The realistic/volumetric-skin tumor route produced lightweight global tumor-output metrics, but complete surface and stress post-processing for the larger realistic result model was not practical within the available local computation time.

\begin{table}[H]
\centering
\small
\caption{Tumor-overlay output metrics for the available solved tumor cases.}
\label{tumor_overlay_output_metrics_table}
\begin{tabular}{
>{\raggedright\arraybackslash}p{0.36\linewidth}
>{\raggedleft\arraybackslash}p{0.13\linewidth}
>{\raggedleft\arraybackslash}p{0.15\linewidth}
>{\raggedleft\arraybackslash}p{0.15\linewidth}
>{\raggedleft\arraybackslash}p{0.13\linewidth}
}
\toprule
Case & Tumor volume & Peak breast disp. & Peak tumor-region disp. & Peak tumor VM \\
& $(\mathrm{mL})$ & $(\mathrm{mm})$ & $(\mathrm{mm})$ & $(\mathrm{kPa})$ \\
\midrule
Simple gland, medium upper-outer & 0.897 & 45.486 & 37.294 & 13.070 \\
Realistic/volumetric skin, lightweight output & 0.912 & 10.916 & 8.496 & -- \\
\bottomrule
\end{tabular}
\end{table}

For the simple-gland tumor case, the review surface maximum displacement was $11.674~\mathrm{mm}$. The available tumor outputs therefore show that the analytic tumor mask could be solved and evaluated locally, while the complete realistic tumor-overlay post-processing remains a future computational step.
```

If the table becomes too wide, split into two tables or remove `Peak tumor VM` from the realistic row caption.

### Section 4.8 Summary of main result trends

Replace the section with:

```latex
\subsection{Summary of main result trends}
\label{summary_main_result_trends}

The strongest quantitative sensitivity was observed for material and skin-parameter variation. Across the skin/material result set, peak displacement changed by approximately $+35.0\%$ to $-75.6\%$ relative to the selected comparison cases. The volumetric skin layer reduced displacement compared with the no-skin case at the same $1.25g$ input, while the stiff skin-enabled variants also produced higher local von Mises stress concentrations.

The simplified Cooper-like support variants produced a much smaller global response change. In the available scout results, global peak displacement changed by approximately $-1.4\%$ to $+0.1\%$ relative to the no-Cooper baseline. The dynamic-loading results showed that the fixed-support acceleration route and prescribed support-displacement route could both be evaluated, but they should be interpreted as separate motion definitions.

The tumor-overlay route was successfully implemented and evaluated for selected cases. The simple-gland medium upper-outer tumor case produced local tumor-region output, while the realistic/volumetric-skin tumor route was limited to lightweight global output within the available computation time.
```

## Discussion

### Section 5.3

Add one sentence after the first paragraph:

```latex
This is consistent with the Results, where the material and skin-parameter variants produced the largest peak-displacement changes among the reported model routes.
```

### Section 5.4

Replace the final paragraph with:

```latex
The current dynamic results should therefore be interpreted as model-development outputs rather than as a fully realistic motion experiment. The displacement time-series and surface-response metrics show that the model can generate measurable dynamic motion, but the shape of the response curves still depends on the simplified excitation, damping, support assumptions and solver settings. A future validation study should compare these simulated response curves with motion measured in an EWS-like setup.
```

### Section 5.5

Replace the Cooper paragraph with:

```latex
The Cooper-like support implementation should also be interpreted as an approximation. In the available scout results, the simplified support route produced only small global peak-displacement changes, suggesting that its current implementation mainly served as a controllable support-sensitivity test. Future versions should investigate whether a more anatomically guided support representation produces a stronger or more localised surface-response effect.
```

### Section 5.6

Replace the final paragraph with:

```latex
The new tumor-output metrics show that the analytic tumor mask can produce local tumor-region response values, but the reported global surface changes remain limited and should not be interpreted as detection evidence. Future tumor studies should therefore run larger solved case sets with matched no-tumor controls, complete tumor-mask post-processing and systematic variation of tumor geometry, stiffness contrast and location.
```

### Section 5.7

Keep the current computational limitation section, but change the last sentence to:

```latex
Future work should preserve this level of traceability, although the exact repository structure can be adapted as the simulation workflow expands.
```

## General terminology fixes

Apply these consistently:

- Use `COMSOL`, not `Comsol`.
- Use `tumor overlay` or `analytic tumor overlay`, not `separate tumor domain`.
- Use `Cooper-like support approximation`, not anatomical Cooper-ligament reconstruction.
- Use `posterior chestwall support` when referring to the model boundary/support and avoid implying a full anatomical thoracic wall.
- Use `stiffness scale` or `small-strain equivalent stiffness` when discussing converted `E` values from Mooney--Rivlin parameters.
- Use `surface metrics` for exported averages/landmarks; avoid implying dense full-field optical maps unless that is what the output contains.

## Items not changed directly

- No direct source edits were made because the `.tex`, `.bib`, and result-inventory files were not mounted in the workspace.
- No new COMSOL runs should be made.
- No existing outputs should be removed.
- Do not add extra Results figures until Figure 7 is fixed and the `docs/report_plan/results_figures/` contents are checked in the actual local repository.
