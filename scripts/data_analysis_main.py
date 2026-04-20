"""
Main analysis pipeline for VTK-based tissue mechanics data.

- Builds summary statistics from VTK runs
- Generates stress + displacement visualizations
- Extracts landmarks and spatial comparisons
- Produces model comparison outputs

Outputs:
  - figures per model run
  - comparison figures
  - CSV summaries
"""

from pathlib import Path
import pandas as pd

import helper_functions as helper
import visualization as viz
import config


if __name__ == "__main__":

    # ============================================================
    # SELECT RUN (ONLY CHANGE THIS FOR DIFFERENT EXPERIMENTS)
    # ============================================================

    run_name = "overnight_hetero"

    vtk_dir = config.MODELS_TO_COMPARE[run_name]
    feb_path = vtk_dir.parent / f"{run_name}.feb"

    # Output dirs
    fig_dir = config.get_figures_path(run_name)
    all_models_fig_dir = config.FIGURES_DIR / "comparison_all_models"
    all_models_fig_dir.mkdir(parents=True, exist_ok=True)

    print(f"\n=== Running analysis for: {run_name} ===")

    # ============================================================
    # SURFACE + LANDMARK EXTRACTION
    # ============================================================

    print("\n[1/13] Extracting surface nodes")
    surface_nodes = helper.get_surface_nodes(feb_path)
    print(f"Surface nodes: {len(surface_nodes)}")

    print("\n[2/13] Extracting landmarks")
    landmarks = helper.extract_landmarks(feb_path, target_y=config.TARGET_Y)

    if not landmarks:
        raise ValueError("No landmarks found in FEB file.")

    print(f"Landmarks: {list(landmarks.keys())}")

    # ============================================================
    # LANDMARK DISPLACEMENTS
    # ============================================================

    print("\n[3/13] Landmark displacement extraction")

    df_landmarks = helper.extract_landmark_displacements(
        config.MODELS_TO_COMPARE,
        landmarks,
        surface_nodes,
        config.STEP
    )

    if df_landmarks.empty:
        print("WARNING: No landmark displacement data found.")

    df_landmarks.to_csv(
        all_models_fig_dir / f"landmarks_step{config.STEP}.csv",
        index=False
    )

    # ============================================================
    # TIME BASE
    # ============================================================

    print("\n[4/13] Computing time axis")

    times = helper.get_common_times(
        vtk_dir,
        config.STEP_MIN,
        config.STEP_MAX
    )

    # ============================================================
    # MAIN LOOP: PER MODEL ANALYSIS
    # ============================================================

    for model_name, model_path in config.MODELS_TO_COMPARE.items():

        print(f"\n===== Processing model: {model_name} =====")

        model_fig_dir = config.get_figures_path(model_name)

        # --------------------------------------------------------
        # SUMMARY TABLE
        # --------------------------------------------------------

        print("[5/13] Building summary table")

        df = helper.build_summary_table(
            model_path,
            config.STEP_MIN,
            config.STEP_MAX
        )

        print(f"Steps processed: {len(df)}")

        summary_csv = model_path.parent / "summary_statistics.csv"
        df.to_csv(summary_csv, index=False)

        # --------------------------------------------------------
        # STRESS PLOTS
        # --------------------------------------------------------

        print("[6/13] Stress evolution plot")

        viz.plot_stress_evolution(
            df,
            model_fig_dir,
            times,
            model_name,
            config.MODEL_LABELS
        )

        print("[7/13] Tissue comparison plot")

        viz.plot_tissue_comparison(
            df,
            config.TISSUE_LABELS,
            model_fig_dir,
            times,
            model_name,
            config.MODEL_LABELS
        )

        # free memory
        del df

        # --------------------------------------------------------
        # DISPLACEMENT EVOLUTION
        # --------------------------------------------------------

        print("[8/13] Surface displacement evolution")

        viz.plot_surface_displacement_evolution(
            model_path,
            surface_nodes,
            config.STEP_MIN,
            config.STEP_MAX,
            model_fig_dir,
            times,
            config.MODEL_LABELS,
            model_name
        )

        # --------------------------------------------------------
        # LANDMARK VISUALS
        # --------------------------------------------------------

        print("[9/13] Landmark spatial plots")

        viz.plot_landmark_spatial(
            model_path,
            landmarks,
            surface_nodes,
            config.STEP,
            model_fig_dir,
            config.MODEL_LABELS,
            model_name
        )

        viz.plot_landmark_comparison(
            df_landmarks,
            all_models_fig_dir,
            config.MODEL_LABELS,
            config.MODEL_COLORS
        )

    # ============================================================
    # CROSS-MODEL ANALYSIS
    # ============================================================

    print("\n[10/13] Stress + displacement aggregation")

    stress_results = {}

    for model_name, model_path in config.MODELS_TO_COMPARE.items():

        summary_csv = model_path.parent / "summary_statistics.csv"

        if summary_csv.exists():
            df = pd.read_csv(summary_csv)
        else:
            df = helper.build_summary_table(
                model_path,
                config.STEP_MIN,
                config.STEP_MAX
            )
            df.to_csv(summary_csv, index=False)

        stress_results[model_name] = {
            "stress": helper.extract_peak_stress_by_tissue(df),
            "displacement": helper.extract_displacement_metrics(
                model_path,
                feb_path,
                config.STEP_MIN,
                config.STEP_MAX
            )
        }

    # ============================================================
    # GLOBAL COMPARISONS
    # ============================================================

    print("\n[11/13] Spatial displacement comparison")

    viz.plot_spatial_displacement_comparison(
        config.MODELS_TO_COMPARE,
        surface_nodes,
        config.STEP,
        all_models_fig_dir,
        config.MODEL_LABELS
    )

    print("\n[12/13] Scalar comparison plots")

    viz.plot_stress_comparison(
        stress_results,
        all_models_fig_dir,
        config.MODEL_LABELS,
        config.MODEL_COLORS,
        config.TISSUE_LABELS,
        config.LITERATURE_REFS
    )

    print("\n[13/13] Stress evolution comparison")

    viz.plot_stress_evolution_comparison(
        config.MODELS_TO_COMPARE,
        all_models_fig_dir,
        config.STEP_MIN,
        config.STEP_MAX,
        config.MODEL_LABELS,
        config.MODEL_COLORS,
        times
    )

    print("\n=== Analysis complete ===")