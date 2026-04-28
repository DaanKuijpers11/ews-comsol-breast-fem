from __future__ import annotations

import csv
import json
from pathlib import Path


def _workspace_root() -> Path:
    return Path(__file__).resolve().parents[2]


def _is_comsol_metrics_json(path: Path) -> bool:
    if path.suffix.lower() != ".json" or not path.exists():
        return False
    try:
        payload = json.loads(path.read_text(encoding="utf-8"))
    except Exception:
        return False
    return payload.get("source") == "COMSOL"


def _is_febio_summary_csv(path: Path) -> bool:
    return path.suffix.lower() == ".csv" and path.name.endswith("_summary_statistics.csv") and path.exists()


def _resolve_input(path_like: str | Path) -> tuple[str, Path, str]:
    path = Path(path_like)

    if path.is_dir():
        comsol_metrics = path / "output" / "solve" / f"{path.name}_metrics.json"
        if comsol_metrics.exists():
            return path.name, comsol_metrics, "COMSOL"
        febio_csv = path / f"{path.name}_summary_statistics.csv"
        if febio_csv.exists():
            return path.name, febio_csv, "FEBio"

    if path.suffix.lower() == ".toml":
        case_name = path.stem
        comsol_metrics = path.parent / "output" / "solve" / f"{case_name}_metrics.json"
        if comsol_metrics.exists():
            return case_name, comsol_metrics, "COMSOL"
        febio_csv = path.parent / f"{case_name}_summary_statistics.csv"
        if febio_csv.exists():
            return case_name, febio_csv, "FEBio"

    if _is_comsol_metrics_json(path):
        return path.stem.replace("_metrics", ""), path, "COMSOL"

    if _is_febio_summary_csv(path):
        return path.stem.replace("_summary_statistics", ""), path, "FEBio"

    raise FileNotFoundError(
        f"Could not resolve metrics input from '{path_like}'. "
        "Expected a COMSOL metrics JSON, a FEBio summary_statistics CSV, or a case directory/TOML that contains one."
    )


def _load_comsol_metrics(path: Path) -> dict[str, float | str | None]:
    payload = json.loads(path.read_text(encoding="utf-8"))
    return {
        "solver": "COMSOL",
        "peak_disp_mm": 1000.0 * float(payload.get("max_displacement_breast", 0.0)),
        "avg_disp_mm": 1000.0 * float(payload.get("avg_displacement_breast", 0.0)),
        "peak_vm_pa": float(payload.get("max_von_mises_breast", 0.0)),
        "peak_gland_vm_pa": float(payload.get("max_von_mises_glandular", 0.0)),
        "breast_volume_ml": 1_000_000.0 * float(payload.get("breast_volume", 0.0)),
        "gland_volume_ml": 1_000_000.0 * float(payload.get("glandular_volume", 0.0)),
        "adipose_volume_ml": 1_000_000.0 * float(payload.get("adipose_volume", 0.0)),
    }


def _load_febio_metrics(path: Path) -> dict[str, float | str | None]:
    with open(path, newline="", encoding="utf-8") as handle:
        rows = list(csv.DictReader(handle))
    if not rows:
        raise ValueError(f"FEBio summary CSV is empty: {path}")

    peak_disp = max(float(row.get("disp_max", 0.0)) for row in rows)
    peak_vm = max(float(row.get("vm_max", 0.0)) for row in rows)
    gland_vm = max(float(row.get("part1_vm_max", 0.0)) for row in rows) if "part1_vm_max" in rows[0] else None

    return {
        "solver": "FEBio",
        "peak_disp_mm": 1000.0 * peak_disp,
        "avg_disp_mm": None,
        "peak_vm_pa": peak_vm,
        "peak_gland_vm_pa": gland_vm,
        "breast_volume_ml": None,
        "gland_volume_ml": None,
        "adipose_volume_ml": None,
    }


def _safe_delta(value: float | None, baseline: float | None) -> float | None:
    if value is None or baseline is None or baseline == 0:
        return None
    return 100.0 * (value - baseline) / baseline


def compare_metrics(inputs: tuple[str | Path, ...], baseline: str | None = None) -> Path:
    records: list[dict[str, float | str | None]] = []

    for item in inputs:
        case_name, resolved_path, solver = _resolve_input(item)
        metrics = _load_comsol_metrics(resolved_path) if solver == "COMSOL" else _load_febio_metrics(resolved_path)
        metrics["case"] = case_name
        metrics["source_path"] = str(resolved_path)
        records.append(metrics)

    baseline_name = baseline or str(records[0]["case"])
    baseline_row = next((row for row in records if row["case"] == baseline_name), None)
    if baseline_row is None:
        raise ValueError(f"Baseline '{baseline_name}' not found in inputs.")

    output_rows: list[dict[str, float | str | None]] = []
    for row in records:
        output_rows.append(
            {
                **row,
                "delta_peak_disp_pct": _safe_delta(
                    row.get("peak_disp_mm"), baseline_row.get("peak_disp_mm")  # type: ignore[arg-type]
                ),
                "delta_peak_vm_pct": _safe_delta(
                    row.get("peak_vm_pa"), baseline_row.get("peak_vm_pa")  # type: ignore[arg-type]
                ),
                "delta_peak_gland_vm_pct": _safe_delta(
                    row.get("peak_gland_vm_pa"), baseline_row.get("peak_gland_vm_pa")  # type: ignore[arg-type]
                ),
            }
        )

    output_dir = _workspace_root() / "analysis_output" / "metrics_compare"
    output_dir.mkdir(parents=True, exist_ok=True)
    csv_path = output_dir / "solver_metrics_compare.csv"
    md_path = output_dir / "solver_metrics_compare.md"

    fieldnames = [
        "case",
        "solver",
        "peak_disp_mm",
        "avg_disp_mm",
        "peak_vm_pa",
        "peak_gland_vm_pa",
        "breast_volume_ml",
        "gland_volume_ml",
        "adipose_volume_ml",
        "delta_peak_disp_pct",
        "delta_peak_vm_pct",
        "delta_peak_gland_vm_pct",
        "source_path",
    ]

    with open(csv_path, "w", newline="", encoding="utf-8") as handle:
        writer = csv.DictWriter(handle, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(output_rows)

    headers = [
        "case",
        "solver",
        "peak_disp_mm",
        "avg_disp_mm",
        "peak_vm_pa",
        "peak_gland_vm_pa",
        "breast_volume_ml",
        "gland_volume_ml",
        "adipose_volume_ml",
        "delta_peak_disp_pct",
        "delta_peak_vm_pct",
        "delta_peak_gland_vm_pct",
    ]
    with open(md_path, "w", encoding="utf-8") as handle:
        handle.write(f"# Solver Metrics Compare\n\nBaseline: `{baseline_name}`\n\n")
        handle.write("| " + " | ".join(headers) + " |\n")
        handle.write("|" + "|".join(["---"] * len(headers)) + "|\n")
        for row in output_rows:
            handle.write("| " + " | ".join(str(row.get(h, "")) for h in headers) + " |\n")

    return csv_path
