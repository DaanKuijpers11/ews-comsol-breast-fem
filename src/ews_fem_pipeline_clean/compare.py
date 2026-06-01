from __future__ import annotations

import csv
import math
import re
from pathlib import Path

from ews_fem_pipeline_clean.evaluation import resolve_case_paths, workspace_root


def _read_summary(summary_csv: Path) -> list[dict[str, str]]:
    with open(summary_csv, newline="", encoding="utf-8") as handle:
        return list(csv.DictReader(handle))


def _peak(rows: list[dict[str, str]], key: str) -> float:
    return max(float(row[key]) for row in rows)


def _minimum(rows: list[dict[str, str]], key: str) -> float:
    return min(float(row[key]) for row in rows)


def _runtime_seconds(log_path: Path) -> float | None:
    if not log_path.exists():
        return None
    text = log_path.read_text(encoding="utf-8", errors="ignore")
    match = re.search(r"Total elapsed time [.]* : [\d:]* \(([\d.]+) sec\)", text)
    return float(match.group(1)) if match else None


def _safe_delta(value: float, baseline: float) -> float:
    if math.isclose(baseline, 0.0):
        return 0.0
    return 100.0 * (value - baseline) / baseline


def _write_csv(path: Path, fieldnames: list[str], rows: list[dict[str, object]]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with open(path, "w", newline="", encoding="utf-8") as handle:
        writer = csv.DictWriter(handle, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)


def _write_markdown(path: Path, rows: list[dict[str, object]], baseline_name: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    headers = [
        "case",
        "runtime_sec",
        "peak_vm_max",
        "peak_disp_max_mm",
        "min_J",
        "delta_vm_pct",
        "delta_disp_pct",
        "delta_min_J_pct",
    ]
    with open(path, "w", encoding="utf-8") as handle:
        handle.write(f"# Compare Summary\n\nBaseline: `{baseline_name}`\n\n")
        handle.write("| " + " | ".join(headers) + " |\n")
        handle.write("|" + "|".join(["---"] * len(headers)) + "|\n")
        for row in rows:
            handle.write("| " + " | ".join(str(row[h]) for h in headers) + " |\n")


def _write_plot(path: Path, rows: list[dict[str, object]], baseline_name: str) -> None:
    try:
        import matplotlib.pyplot as plt
        import numpy as np
    except Exception:
        return

    labels = [str(row["case"]) for row in rows]
    vm = np.array([float(row["delta_vm_pct"]) for row in rows])
    disp = np.array([float(row["delta_disp_pct"]) for row in rows])
    min_j = np.array([float(row["delta_min_J_pct"]) for row in rows])

    x = np.arange(len(labels))
    width = 0.25

    fig, ax = plt.subplots(figsize=(12, 6))
    ax.axhline(0, color="black", linewidth=1)
    ax.bar(x - width, vm, width, label="Peak stress delta (%)")
    ax.bar(x, disp, width, label="Peak displacement delta (%)")
    ax.bar(x + width, min_j, width, label="Min J delta (%)")

    ax.set_xticks(x)
    ax.set_xticklabels(labels, rotation=20, ha="right")
    ax.set_ylabel("Delta versus baseline (%)")
    ax.set_title(f"Model comparison versus {baseline_name}")
    ax.legend()
    fig.tight_layout()
    path.parent.mkdir(parents=True, exist_ok=True)
    fig.savefig(path, dpi=300)
    plt.close(fig)


def compare_cases(
    case_inputs: tuple[str | Path, ...],
    baseline: str | None = None,
    output_dir: str | Path | None = None,
) -> Path:
    root = workspace_root()
    metrics = []

    for case_input in case_inputs:
        run_name, vtk_dir, feb_path = resolve_case_paths(case_input)
        run_dir = feb_path.parent
        summary_csv = run_dir / f"{run_name}_summary_statistics.csv"
        log_path = run_dir / f"{run_name}.log"

        if not summary_csv.exists():
            raise FileNotFoundError(f"Missing summary CSV for {run_name}: {summary_csv}")

        rows = _read_summary(summary_csv)
        if not rows:
            raise ValueError(f"Summary CSV is empty for {run_name}: {summary_csv}")

        metrics.append(
            {
                "case": run_name,
                "summary_csv": str(summary_csv),
                "vtk_dir": str(vtk_dir),
                "runtime_sec": _runtime_seconds(log_path),
                "peak_vm_max": _peak(rows, "vm_max"),
                "peak_disp_max_mm": 1000.0 * _peak(rows, "disp_max"),
                "min_J": _minimum(rows, "J_min"),
                "peak_glandular_vm": _peak(rows, "part1_vm_max") if "part1_vm_max" in rows[0] else None,
                "peak_adipose_vm": _peak(rows, "part2_vm_max") if "part2_vm_max" in rows[0] else None,
            }
        )

    baseline_name = baseline or str(metrics[0]["case"])
    baseline_row = next((row for row in metrics if row["case"] == baseline_name), None)
    if baseline_row is None:
        raise ValueError(f"Baseline '{baseline_name}' not found in compared cases.")

    output_rows = []
    for row in metrics:
        output_rows.append(
            {
                **row,
                "delta_vm_pct": round(_safe_delta(float(row["peak_vm_max"]), float(baseline_row["peak_vm_max"])), 2),
                "delta_disp_pct": round(
                    _safe_delta(float(row["peak_disp_max_mm"]), float(baseline_row["peak_disp_max_mm"])), 2
                ),
                "delta_min_J_pct": round(_safe_delta(float(row["min_J"]), float(baseline_row["min_J"])), 2),
            }
        )

    resolved_output_dir = (
        Path(output_dir)
        if output_dir is not None
        else root / "analysis_output" / "figures" / "comparison_all_models"
    )
    if not resolved_output_dir.is_absolute():
        resolved_output_dir = root / resolved_output_dir

    csv_path = resolved_output_dir / "compare_summary.csv"
    md_path = resolved_output_dir / "compare_summary.md"
    plot_path = resolved_output_dir / "compare_summary.png"

    fieldnames = [
        "case",
        "runtime_sec",
        "peak_vm_max",
        "peak_disp_max_mm",
        "min_J",
        "peak_glandular_vm",
        "peak_adipose_vm",
        "delta_vm_pct",
        "delta_disp_pct",
        "delta_min_J_pct",
        "summary_csv",
        "vtk_dir",
    ]
    _write_csv(csv_path, fieldnames, output_rows)
    _write_markdown(md_path, output_rows, baseline_name)
    _write_plot(plot_path, output_rows, baseline_name)
    return csv_path
