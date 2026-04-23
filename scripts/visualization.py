import math
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from pathlib import Path
from typing import Dict
import meshio
import re
import helper_functions as helper


# ============================================================
# STRESS EVOLUTION (single model)
# ============================================================

def plot_stress_evolution(df: pd.DataFrame, output_dir: Path,
                          times: np.ndarray, model_key: str,
                          model_labels: Dict):

    times = df["step"].to_numpy()   
    model_name = model_labels.get(model_key, model_key)

    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(12, 8), sharex=True)

    ax1.plot(times, df["vm_mean"], "o-", label="Mean", linewidth=2)

    # median fallback (if missing)
    if "vm_median" in df.columns:
        ax1.plot(times, df["vm_median"], "s-", label="Median", linewidth=2)

    ax1.fill_between(
        times,
        df["vm_mean"] - df.get("vm_std", 0),
        df["vm_mean"] + df.get("vm_std", 0),
        alpha=0.3,
        label="±1 SD"
    )

    ax1.set_ylabel("Von Mises Stress (Pa)")
    ax1.set_title(f"Stress evolution - {model_name}")
    ax1.legend()
    ax1.grid(True, alpha=0.3)

    ax2.plot(times, df["vm_max"], "r^-", label="Max", linewidth=2)
    ax2.set_xlabel("Time (s)")
    ax2.set_ylabel("Von Mises Stress (Pa)")
    ax2.legend()
    ax2.grid(True, alpha=0.3)

    plt.tight_layout()
    plt.savefig(output_dir / "stress_evolution.png", dpi=300)
    plt.close()


# ============================================================
# TISSUE COMPARISON
# ============================================================

def plot_tissue_comparison(df: pd.DataFrame, tissue_labels: Dict,
                           output_dir: Path, times: np.ndarray,
                           model_key: str, model_labels: Dict):

    parts_cols = [c for c in df.columns if c.startswith("part") and "_vm_mean" in c]

    if not parts_cols:
        print("No tissue data found")
        return

    times = df["step"].to_numpy()
    model_name = model_labels.get(model_key, model_key)

    fig, ax = plt.subplots(figsize=(14, 6))

    for col in parts_cols:
        part_num = int(col.split("_")[0].replace("part", ""))
        label = tissue_labels.get(part_num, f"part {part_num}")

        ax.plot(times, df[col], "o-", label=label, linewidth=2)

    ax.set_xlabel("Time (s)")
    ax.set_ylabel("Von Mises Stress (Pa)")
    ax.set_title(f"Tissue stress - {model_name}")
    ax.legend()
    ax.grid(True, alpha=0.3)

    plt.tight_layout()
    plt.savefig(output_dir / "tissue_comparison.png", dpi=300)
    plt.close()


# ============================================================
# SURFACE DISPLACEMENT
# ============================================================

def plot_surface_displacement_evolution(vtk_dir: Path, surface_nodes: set,
                                        step_min: int, step_max: int,
                                        output_dir: Path, times: np.ndarray,
                                        model_labels: Dict, model_key: str):

    vtks = [(int(re.search(r"\.(\d+)\.vtk", path.name).group(1)), path)
            for path in helper.list_vtks(vtk_dir, step_min, step_max)]

    stats = []

    for step, path in vtks:
        mesh = meshio.read(path)
        U = mesh.point_data.get("displacement")
        if U is None:
            continue

        U_mag = np.linalg.norm(U, axis=1)

        idx = np.array([n - 1 for n in surface_nodes if n - 1 < len(U)])

        surf = U_mag[idx] * 1000

        stats.append({
            "mean": np.mean(surf),
            "median": np.median(surf),
            "max": np.max(surf),
            "std": np.std(surf)
        })

    df = pd.DataFrame(stats)

    if len(times) != len(df):
        print(f"WARNING: time mismatch ({len(times)} vs {len(df)}), truncating")
        times = times[:len(df)]

    fig, (ax1, ax2) = plt.subplots(2, 1, figsize=(10, 8), sharex=True)

    ax1.plot(times, df["mean"], "o-", label="Mean")
    ax1.plot(times, df["median"], "s-", label="Median")
    ax1.fill_between(times,
                     df["mean"] - df["std"],
                     df["mean"] + df["std"],
                     alpha=0.3)

    ax1.set_ylabel("Disp (mm)")
    ax1.legend()
    ax1.grid(True)

    ax2.plot(times, df["max"], "r^-")
    ax2.set_xlabel("Time (s)")
    ax2.set_ylabel("Max Disp (mm)")
    ax2.grid(True)

    plt.tight_layout()
    plt.savefig(output_dir / "surface_disp.png", dpi=300)
    plt.close()


# ============================================================
# LANDMARK SPATIAL
# ============================================================

def plot_landmark_spatial(vtk_dir: Path, landmarks: dict,
                          surface_nodes: set, step: int,
                          output_dir: Path, model_labels: Dict,
                          model_key: str):

    model_name = model_labels.get(model_key, model_key)

    vtk_files = [path for path in helper.list_vtks(vtk_dir, step, step + 1)
                 if re.search(rf"\.{step}\.vtk$", path.name)]
    if not vtk_files:
        return

    mesh = meshio.read(vtk_files[0])
    U = mesh.point_data.get("displacement")
    if U is None:
        return

    idx = np.array([n - 1 for n in surface_nodes if n - 1 < len(U)])

    coords = mesh.points[idx]
    disp = np.linalg.norm(U[idx], axis=1) * 1000

    fig, ax = plt.subplots(figsize=(8, 8))

    sc = ax.scatter(coords[:, 0], coords[:, 2],
                    c=disp, cmap="viridis", s=5)

    for name, lm in landmarks.items():
        c = lm["coords"]
        ax.scatter(c[0], c[2], s=150, label=name)

    ax.set_title(model_name)
    ax.set_aspect("equal")
    plt.colorbar(sc)

    plt.tight_layout()
    plt.savefig(output_dir / "landmarks.png", dpi=300)
    plt.close()


# ============================================================
# LANDMARK COMPARISON (FIXED KEY)
# ============================================================

def plot_landmark_comparison(df: pd.DataFrame, output_dir: Path,
                             model_labels: Dict, model_colors: Dict):

    fig, ax = plt.subplots(figsize=(12, 6))

    landmarks = df["landmark"].unique()
    models = df["model"].unique()

    x = np.arange(len(landmarks))
    width = 0.8 / len(models)

    for i, model in enumerate(models):
        subset = df[df["model"] == model]

        values = [
            subset[subset["landmark"] == lm]["disp_mm"].values[0]
            for lm in landmarks
        ]

        ax.bar(x + i * width, values, width,
               label=model_labels.get(model, model),
               color=model_colors.get(model))

    ax.set_xticks(x)
    ax.set_xticklabels(landmarks)
    ax.set_ylabel("Disp (mm)")
    ax.set_title("Landmark comparison")
    ax.legend()

    plt.tight_layout()
    plt.savefig(output_dir / "landmarks_comparison.png", dpi=300)
    plt.close()


# ============================================================
# SPATIAL DISPLACEMENT COMPARISON
# ============================================================

def plot_spatial_displacement_comparison(model_dirs: dict,
                                         surface_nodes: set,
                                         step: int,
                                         output_dir: Path,
                                         model_labels: dict):

    fig, axes = plt.subplots(1, len(model_dirs), figsize=(15, 5))

    if len(model_dirs) == 1:
        axes = [axes]

    for ax, (name, vtk_dir) in zip(axes, model_dirs.items()):

        vtk_files = helper.list_vtks(vtk_dir, 0, 10_000)

        if not vtk_files:
            print(f"WARNING: No VTK files for {name}")
            continue

        # pak laatste timestep
        vtk_path = vtk_files[-1]

        mesh = meshio.read(vtk_path)
        U = mesh.point_data["displacement"]

        idx = np.array([n - 1 for n in surface_nodes if n - 1 < len(U)])

        coords = mesh.points[idx]
        disp = np.linalg.norm(U[idx], axis=1) * 1000

        sc = ax.scatter(coords[:, 0], coords[:, 2], c=disp, s=10)

        ax.set_title(model_labels.get(name, name))
        ax.set_aspect("equal")

    plt.colorbar(sc, ax=axes)
    plt.tight_layout()
    plt.savefig(output_dir / "spatial_comparison.png", dpi=300)
    plt.close()


# ============================================================
# STRESS COMPARISON (FIXED NAME MATCH)
# ============================================================

def plot_stress_comparison(results: dict, output_dir: Path,
                           model_labels: Dict, model_colors: Dict,
                           tissue_labels: Dict, literature_refs: Dict):

    models = list(results.keys())
    tissues = ["Tissue_1", "Tissue_2"]

    fig, ax = plt.subplots()

    x = np.arange(len(tissues))
    width = 0.8 / len(models)

    for i, m in enumerate(models):
        vals = [
            results[m]["stress"]["max_stress_kPa"].get(t, 0)
            for t in tissues
        ]

        ax.bar(x + i * width, vals,
               width,
               label=model_labels.get(m, m),
               color=model_colors.get(m))

    ax.set_xticks(x)
    ax.set_xticklabels(tissues)
    ax.set_ylabel("kPa")
    ax.set_title("Stress comparison")
    ax.legend()

    plt.tight_layout()
    plt.savefig(output_dir / "stress_comparison.png", dpi=300)
    plt.close()


# ============================================================
# EVOLUTION COMPARISON
# ============================================================

def plot_stress_evolution_comparison(model_dirs: dict,
                                     output_dir: Path,
                                     step_min: int,
                                     step_max: int,
                                     model_labels: Dict,
                                     model_colors: Dict,
                                     times: np.ndarray):

    fig, ax = plt.subplots()

    for name, vtk_dir in model_dirs.items():

        df = helper.build_summary_table(vtk_dir, step_min, step_max)

        times_local = times[:len(df)]

        ax.plot(times_local, df["vm_mean"],
                label=model_labels.get(name, name))

    ax.set_title("Stress evolution comparison")
    ax.legend()

    plt.tight_layout()
    plt.savefig(output_dir / "stress_evolution_comparison.png", dpi=300)
    plt.close()
