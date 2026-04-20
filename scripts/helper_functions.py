import re
from pathlib import Path
import numpy as np
import pandas as pd
import meshio
import gc
from typing import Dict, List


# ============================================================
# STRESS / MATH
# ============================================================

def von_mises_any(sig):
    sig = np.asarray(sig)

    if sig.ndim == 3 and sig.shape[1:] == (3, 3):
        sxx, syy, szz = sig[:, 0, 0], sig[:, 1, 1], sig[:, 2, 2]
        sxy = 0.5 * (sig[:, 0, 1] + sig[:, 1, 0])
        sxz = 0.5 * (sig[:, 0, 2] + sig[:, 2, 0])
        syz = 0.5 * (sig[:, 1, 2] + sig[:, 2, 1])

    elif sig.ndim == 2 and sig.shape[1] == 6:
        sxx, syy, szz, sxy, syz, sxz = [sig[:, i] for i in range(6)]
    else:
        raise ValueError(f"Unsupported stress shape: {sig.shape}")

    vm2 = 0.5 * (
        (sxx - syy) ** 2 +
        (syy - szz) ** 2 +
        (szz - sxx) ** 2 +
        6.0 * (sxy ** 2 + syz ** 2 + sxz ** 2)
    )

    return np.sqrt(np.maximum(vm2, 0.0))


# ============================================================
# VTK UTILITIES
# ============================================================

def list_vtks(vtk_dir: Path, step_min: int, step_max: int) -> List[Path]:
    vtks = []

    for p in vtk_dir.glob("*.vtk"):
        m = re.search(r"\.(\d+)\.vtk$", p.name)
        if m:
            step = int(m.group(1))
            if step_min <= step < step_max:
                vtks.append((step, p))

    return [p for _, p in sorted(vtks)]


def extract_time_from_vtk(vtk_path: Path) -> float:
    with open(vtk_path, "r") as f:
        for i, line in enumerate(f):
            if i > 5:
                break
            if line.startswith("time"):
                return float(line.split()[1])
    return 0.0


def get_common_times(vtk_dir: Path, step_min: int, step_max: int):
    vtks = list_vtks(vtk_dir, step_min, step_max)
    return np.array([extract_time_from_vtk(p) for p in vtks])


# ============================================================
# HEALTH METRICS (CONFIG SCORING READY)
# ============================================================

def compute_element_health_metrics(vm, J):
    vm = np.asarray(vm)
    J = np.asarray(J)

    inverted = np.sum(J < 0)
    near_inverted = np.sum(J < 0.1)

    return {
        "inverted_elements": int(inverted),
        "near_inverted_elements": int(near_inverted),
        "min_J": float(np.min(J)) if len(J) else 0.0,
        "max_J": float(np.max(J)) if len(J) else 0.0,
        "mean_J": float(np.mean(J)) if len(J) else 0.0,
        "max_vm": float(np.max(vm)) if len(vm) else 0.0,
    }


def print_health_log(step, stats):
    print(
        f"[STEP {step}] "
        f"J_min={stats['min_J']:.3e} | "
        f"inverted={stats['inverted_elements']} | "
        f"near_inv={stats['near_inverted_elements']} | "
        f"VM_max={stats['max_vm']:.3e}"
    )


# ============================================================
# SINGLE STEP CORE
# ============================================================

def read_step(vtk_path: Path) -> Dict:
    mesh = meshio.read(vtk_path)

    cell_type = "tetra10" if "tetra10" in mesh.cells_dict else list(mesh.cells_dict)[0]

    stress = mesh.cell_data_dict["stress"][cell_type]
    vm = von_mises_any(stress)

    J = mesh.cell_data_dict.get("relative_volume", {}).get(cell_type, None)
    J = J.ravel() if J is not None else np.ones_like(vm)

    pid = mesh.cell_data_dict.get("part_id", {}).get(cell_type, None)
    pid = pid.ravel().astype(int) if pid is not None else np.full(vm.size, -1)

    U = mesh.point_data.get("displacement", None)
    U_mag = np.linalg.norm(U, axis=1) if U is not None else None

    step = int(re.search(r"\.(\d+)\.vtk$", vtk_path.name).group(1))

    stats = {
        "step": step,
        "filename": vtk_path.name,

        # stress (USED BY VIZ)
        "vm_mean": float(np.mean(vm)),
        "vm_median": float(np.median(vm)),
        "vm_max": float(np.max(vm)),
        "vm_std": float(np.std(vm)),

        # volume change
        "J_mean": float(np.mean(J)),
        "J_min": float(np.min(J)),
        "J_max": float(np.max(J)),
    }

    # health metrics
    stats.update(compute_element_health_metrics(vm, J))

    # tissue breakdown
    for part_id in np.unique(pid):
        mask = (pid == part_id)
        key = f"part{part_id}"

        stats[f"{key}_vm_mean"] = float(np.mean(vm[mask]))
        stats[f"{key}_vm_max"] = float(np.max(vm[mask]))
        stats[f"{key}_J_mean"] = float(np.mean(J[mask]))
        stats[f"{key}_n_elem"] = int(np.sum(mask))

    # displacement
    if U_mag is not None:
        stats["disp_mean"] = float(np.mean(U_mag))
        stats["disp_max"] = float(np.max(U_mag))

    print_health_log(step, stats)

    return stats


# ============================================================
# SUMMARY TABLE
# ============================================================

def build_summary_table(vtk_dir: Path, step_min: int, step_max: int) -> pd.DataFrame:
    vtks = list_vtks(vtk_dir, step_min, step_max)

    print(f"Building summary for {len(vtks)} steps")

    out = []
    for i, vtk in enumerate(vtks):
        out.append(read_step(vtk))
        if i % 5 == 0:
            gc.collect()

    return pd.DataFrame(out).sort_values("step").reset_index(drop=True)


# ============================================================
# STRESS ANALYSIS
# ============================================================

def extract_peak_stress_by_tissue(df: pd.DataFrame) -> Dict:
    results = {
        "step_at_peak": {},
        "max_stress_kPa": {},
        "mean_stress_kPa": {},
    }

    cols = [c for c in df.columns if "part" in c and "_vm_" in c]

    for col in cols:
        if "_vm_max" in col:
            part = col.split("_")[0]

            results["max_stress_kPa"][part] = df[col].max() / 1000
            results["step_at_peak"][part] = df.loc[df[col].idxmax(), "step"]

    return results


# ============================================================
# SURFACE / LANDMARKS
# ============================================================

def get_surface_nodes(feb_path: Path) -> set:
    start_marker = '<Elements type="tri6" name="skin_part">'
    end_marker = "</Elements>"
    num_re = re.compile(r"\d+")

    nodes = set()
    active = False

    with feb_path.open() as f:
        for line in f:
            if start_marker in line:
                active = True
            elif end_marker in line:
                active = False

            if active:
                for n in num_re.findall(line):
                    nodes.add(int(n))

    return nodes


def extract_landmarks(feb_path: Path, target_y=0.039):
    start_marker = '<Nodes name="Object01">'
    end_marker = "</Nodes>"

    id_re = re.compile(r'id="(\d+)"')
    coord_re = re.compile(r"(?<=>)([^<]+)(?=</)")

    nodes = {}
    active = False

    with feb_path.open() as f:
        for line in f:
            if start_marker in line:
                active = True
                continue
            if end_marker in line:
                break

            if not active:
                continue

            id_match = id_re.search(line)
            coord_match = coord_re.search(line)

            if id_match and coord_match:
                nid = int(id_match.group(1))
                coords = tuple(map(float, coord_match.group(1).split(",")))
                nodes[nid] = coords

    selected = {}

    # nipple
    nipple = max(nodes.items(), key=lambda n: n[1][1])
    selected["nipple"] = {"id": nipple[0], "coords": nipple[1]}

    plane = {k: v for k, v in nodes.items() if abs(v[1] - target_y) < 0.005}
    if not plane:
        return selected

    left = min(plane.items(), key=lambda n: n[1][0])
    right = max(plane.items(), key=lambda n: n[1][0])

    top = max(plane.items(), key=lambda n: n[1][2])
    bottom = min(plane.items(), key=lambda n: n[1][2])

    selected["left"] = {"id": left[0], "coords": left[1]}
    selected["right"] = {"id": right[0], "coords": right[1]}
    selected["superior"] = {"id": top[0], "coords": top[1]}
    selected["inferior"] = {"id": bottom[0], "coords": bottom[1]}

    return selected


def extract_landmark_displacements(model_dirs: dict, landmarks: dict,
                                   surface_nodes: set, step: int):

    results = []

    for model, vtk_dir in model_dirs.items():
        vtks_all = list(vtk_dir.glob("*.vtk"))
        if not vtks_all:
            continue

        vtks_all.sort()
        vtk_path = vtks_all[-1]

        mesh = meshio.read(vtk_path)

        U = mesh.point_data.get("displacement")

        for name, info in landmarks.items():
            i = info["id"] - 1

            vec = U[i] * 1000
            mag = np.linalg.norm(vec)

            results.append({
                "model": model,
                "landmark": name,
                "disp_mm": mag,
            })

    return pd.DataFrame(results)


# ============================================================
# DISPLACEMENT METRICS (SIMPLIFIED BUT STABLE)
# ============================================================

def extract_displacement_metrics(vtk_dir: Path, feb_path: Path, step_min: int, step_max: int):
    surface = get_surface_nodes(feb_path)
    vtks = list_vtks(vtk_dir, step_min, step_max)

    all_vals = []

    for vtk in vtks:
        mesh = meshio.read(vtk)
        U = mesh.point_data.get("displacement")
        if U is None:
            continue

        idx = np.array([n - 1 for n in surface if n - 1 < len(U)])
        U_mag = np.linalg.norm(U[idx], axis=1)

        all_vals.extend(U_mag)

    return {
        "mean_disp": float(np.mean(all_vals)),
        "max_disp": float(np.max(all_vals)),
    }