"""Rebuild compact comparison tables/plots from manual COMSOL CSV exports.

Manual COMSOL post-processing should store one case per folder under:
analysis_output/comsol_pipeline/manual_postprocess/tables/<case_id>/

Expected per-case files:
- <case_id>_avg_timeseries.csv with time_s, avg_displacement_mm, avg_vm_kpa
- <case_id>_max_timeseries.csv with time_s, max_displacement_mm, max_vm_kpa
"""

from __future__ import annotations

import argparse
import csv
from pathlib import Path

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt


ROOT = Path(__file__).resolve().parents[1]
TABLES_DIR = ROOT / "analysis_output" / "comsol_pipeline" / "manual_postprocess" / "tables"
FIGURES_DIR = ROOT / "analysis_output" / "comsol_pipeline" / "manual_postprocess" / "comparison_figures"
REPORT_FIGURES_DIR = (
    ROOT
    / "docs"
    / "Traineeship_report___Daan_Kuijpers"
    / "Figures"
    / "stage5_manual_postprocess"
)


LABELS = {
    "stage5_no_skin_100g": "No skin 1.00g",
    "stage5_no_skin_125g": "No skin 1.25g",
    "stage5_volumetric_skin_125g": "Volumetric skin 1.25g",
    "stage5_volumetric_skin_soft_interior_125g": "Vol. skin + soft interior 1.25g",
    "stage5_volumetric_skin_soft_febio_materials_125g": "Vol. skin + soft FEBio materials 1.25g",
}


CASE_ORDER = [
    "stage5_no_skin_100g",
    "stage5_no_skin_125g",
    "stage5_volumetric_skin_125g",
    "stage5_volumetric_skin_soft_interior_125g",
    "stage5_volumetric_skin_soft_febio_materials_125g",
]


def _read_csv(path: Path) -> list[dict[str, str]]:
    with path.open("r", newline="", encoding="utf-8-sig") as handle:
        return list(csv.DictReader(handle))


def _write_csv(path: Path, rows: list[dict[str, object]], fieldnames: list[str]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", newline="", encoding="utf-8") as handle:
        writer = csv.DictWriter(handle, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)


def _as_float(row: dict[str, str], key: str) -> float:
    return float(str(row[key]).replace(",", "."))


def _peak(rows: list[dict[str, str]], value_key: str) -> tuple[float, float]:
    best = max(rows, key=lambda row: _as_float(row, value_key))
    return _as_float(best, value_key), _as_float(best, "time_s")


def _available_cases(tables_dir: Path) -> list[str]:
    case_ids = [path.name for path in tables_dir.iterdir() if path.is_dir()]
    ordered = [case_id for case_id in CASE_ORDER if case_id in case_ids]
    ordered.extend(sorted(case_id for case_id in case_ids if case_id not in CASE_ORDER))
    return ordered


def rebuild_tables(tables_dir: Path) -> tuple[list[dict[str, object]], list[dict[str, object]], list[dict[str, object]]]:
    summary_rows: list[dict[str, object]] = []
    max_long_rows: list[dict[str, object]] = []
    avg_long_rows: list[dict[str, object]] = []

    for case_id in _available_cases(tables_dir):
        case_dir = tables_dir / case_id
        avg_path = case_dir / f"{case_id}_avg_timeseries.csv"
        max_path = case_dir / f"{case_id}_max_timeseries.csv"
        if not avg_path.exists() or not max_path.exists():
            print(f"[manual postprocess] skip incomplete case: {case_id}")
            continue

        avg_rows = _read_csv(avg_path)
        max_rows = _read_csv(max_path)
        if not avg_rows or not max_rows:
            print(f"[manual postprocess] skip empty case: {case_id}")
            continue

        peak_max_disp, peak_max_disp_time = _peak(max_rows, "max_displacement_mm")
        peak_max_vm, peak_max_vm_time = _peak(max_rows, "max_vm_kpa")
        peak_avg_disp, peak_avg_disp_time = _peak(avg_rows, "avg_displacement_mm")
        peak_avg_vm, peak_avg_vm_time = _peak(avg_rows, "avg_vm_kpa")

        summary_rows.append(
            {
                "case_id": case_id,
                "label": LABELS.get(case_id, case_id),
                "peak_max_displacement_mm": f"{peak_max_disp:.6f}",
                "peak_max_displacement_time_s": f"{peak_max_disp_time:.6g}",
                "peak_avg_displacement_mm": f"{peak_avg_disp:.6f}",
                "peak_avg_displacement_time_s": f"{peak_avg_disp_time:.6g}",
                "peak_max_vm_kpa": f"{peak_max_vm:.6f}",
                "peak_max_vm_time_s": f"{peak_max_vm_time:.6g}",
                "peak_avg_vm_kpa": f"{peak_avg_vm:.6f}",
                "peak_avg_vm_time_s": f"{peak_avg_vm_time:.6g}",
            }
        )

        for row in max_rows:
            max_long_rows.append(
                {
                    "case_id": case_id,
                    "time_s": row["time_s"],
                    "max_displacement_mm": row["max_displacement_mm"],
                    "max_vm_kpa": row["max_vm_kpa"],
                }
            )
        for row in avg_rows:
            avg_long_rows.append(
                {
                    "case_id": case_id,
                    "time_s": row["time_s"],
                    "avg_displacement_mm": row["avg_displacement_mm"],
                    "avg_vm_kpa": row["avg_vm_kpa"],
                }
            )

    return summary_rows, max_long_rows, avg_long_rows


def write_outputs(tables_dir: Path, summary_rows: list[dict[str, object]], max_rows: list[dict[str, object]], avg_rows: list[dict[str, object]]) -> None:
    _write_csv(
        tables_dir / "stage5_manual_postprocess_summary.csv",
        summary_rows,
        [
            "case_id",
            "label",
            "peak_max_displacement_mm",
            "peak_max_displacement_time_s",
            "peak_avg_displacement_mm",
            "peak_avg_displacement_time_s",
            "peak_max_vm_kpa",
            "peak_max_vm_time_s",
            "peak_avg_vm_kpa",
            "peak_avg_vm_time_s",
        ],
    )
    _write_csv(
        tables_dir / "stage5_manual_max_timeseries_long.csv",
        max_rows,
        ["case_id", "time_s", "max_displacement_mm", "max_vm_kpa"],
    )
    _write_csv(
        tables_dir / "stage5_manual_avg_timeseries_long.csv",
        avg_rows,
        ["case_id", "time_s", "avg_displacement_mm", "avg_vm_kpa"],
    )


def _case_color(index: int) -> str:
    colors = ["#2f78b7", "#39a34a", "#d85a2a", "#7b5ab6", "#7a7a7a", "#c27c0e"]
    return colors[index % len(colors)]


def write_plots(summary_rows: list[dict[str, object]], max_rows: list[dict[str, object]], figures_dir: Path, report_figures_dir: Path | None) -> None:
    figures_dir.mkdir(parents=True, exist_ok=True)
    labels = [str(row["label"]) for row in summary_rows]
    colors = [_case_color(index) for index, _ in enumerate(summary_rows)]

    fig, axes = plt.subplots(1, 2, figsize=(12, 6))
    for ax, key, title in [
        (axes[0], "peak_max_displacement_mm", "Peak max displacement (mm)"),
        (axes[1], "peak_max_vm_kpa", "Peak max VM stress (kPa)"),
    ]:
        values = [float(row[key]) for row in summary_rows]
        x_values = list(range(len(values)))
        ax.bar(x_values, values, color=colors, width=0.65)
        ax.set_title(title, loc="left", fontsize=11)
        ax.set_xticks(x_values)
        ax.set_xticklabels(labels, rotation=30, ha="right", fontsize=9)
        ax.spines[["top", "right"]].set_visible(False)
        ax.grid(axis="y", alpha=0.2)
        limit = max(values) * 1.18 if values else 1.0
        ax.set_ylim(0, limit)
        for x, value in zip(x_values, values):
            ax.text(x, value + limit * 0.02, f"{value:.2f}", ha="center", va="bottom", fontsize=9)
    fig.suptitle("Stage 5 manual postprocess comparison", x=0.04, ha="left", fontsize=16, fontweight="bold")
    fig.tight_layout(rect=[0, 0, 1, 0.94])
    peak_plot = figures_dir / "stage5_manual_peak_comparison.png"
    fig.savefig(peak_plot, dpi=180)
    plt.close(fig)

    rows_by_case: dict[str, list[dict[str, object]]] = {}
    for row in max_rows:
        rows_by_case.setdefault(str(row["case_id"]), []).append(row)

    fig, ax = plt.subplots(figsize=(12, 6))
    for index, summary in enumerate(summary_rows):
        case_id = str(summary["case_id"])
        rows = sorted(rows_by_case.get(case_id, []), key=lambda row: float(row["time_s"]))
        if not rows:
            continue
        ax.plot(
            [float(row["time_s"]) for row in rows],
            [float(row["max_displacement_mm"]) for row in rows],
            lw=2.2,
            color=_case_color(index),
            label=str(summary["label"]),
        )
    ax.set_title("Stage 5 max displacement time series", loc="left", fontsize=16, fontweight="bold")
    ax.set_xlabel("Time (s)")
    ax.set_ylabel("Max displacement (mm)")
    ax.grid(alpha=0.25)
    ax.spines[["top", "right"]].set_visible(False)
    ax.legend(frameon=False, loc="upper right")
    fig.tight_layout()
    timeseries_plot = figures_dir / "stage5_manual_max_displacement_timeseries.png"
    fig.savefig(timeseries_plot, dpi=180)
    plt.close(fig)

    if report_figures_dir is not None:
        report_figures_dir.mkdir(parents=True, exist_ok=True)
        for source in [peak_plot, timeseries_plot]:
            target = report_figures_dir / source.name
            target.write_bytes(source.read_bytes())


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--tables-dir", type=Path, default=TABLES_DIR)
    parser.add_argument("--figures-dir", type=Path, default=FIGURES_DIR)
    parser.add_argument(
        "--no-report-copy",
        action="store_true",
        help="Do not copy the generated PNG figures into the traineeship report figure folder.",
    )
    args = parser.parse_args()

    summary_rows, max_rows, avg_rows = rebuild_tables(args.tables_dir)
    if not summary_rows:
        raise SystemExit(f"No complete manual postprocess cases found in {args.tables_dir}")
    write_outputs(args.tables_dir, summary_rows, max_rows, avg_rows)
    write_plots(
        summary_rows,
        max_rows,
        args.figures_dir,
        None if args.no_report_copy else REPORT_FIGURES_DIR,
    )
    print(f"[manual postprocess] wrote {len(summary_rows)} cases")
    print(f"[manual postprocess] tables: {args.tables_dir}")
    print(f"[manual postprocess] figures: {args.figures_dir}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
