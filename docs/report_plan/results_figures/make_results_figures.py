from __future__ import annotations

import csv
import json
import math
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable

from PIL import Image, ImageDraw, ImageFont
from reportlab.lib.utils import ImageReader
from reportlab.pdfgen import canvas


ROOT = Path(__file__).resolve().parents[3]
OUT_DIR = ROOT / "docs" / "report_plan" / "results_figures"


@dataclass(frozen=True)
class Case:
    key: str
    label: str
    summary: Path
    time_series: Path
    color: str


def p(*parts: str) -> Path:
    return ROOT.joinpath(*parts)


SKIN_CASES = [
    Case(
        "ref_15mm_soft",
        "Reference 1.5 mm soft skin",
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_volskin_15mm_femke_skin_soft_interior_125g_solve_only_preview", "solve", "stage5_scout_simple_gland_volskin_15mm_femke_skin_soft_interior_125g_solve_only_preview_ews_surface_summary.json"),
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_volskin_15mm_femke_skin_soft_interior_125g_solve_only_preview", "solve", "stage5_scout_simple_gland_volskin_15mm_femke_skin_soft_interior_125g_solve_only_preview_ews_surface_time_series.csv"),
        "#2f4f4f",
    ),
    Case(
        "no_skin",
        "No skin",
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_no_skin_soft_interior_125g_solve_only_preview", "solve", "stage5_scout_simple_gland_no_skin_soft_interior_125g_solve_only_preview_ews_surface_summary.json"),
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_no_skin_soft_interior_125g_solve_only_preview", "solve", "stage5_scout_simple_gland_no_skin_soft_interior_125g_solve_only_preview_ews_surface_time_series.csv"),
        "#9a3412",
    ),
    Case(
        "skin_01mm_soft",
        "0.1 mm soft skin",
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_volskin_01mm_softskin_soft_interior_125g", "solve", "stage5_scout_simple_gland_volskin_01mm_softskin_soft_interior_125g_solve_ews_surface_summary.json"),
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_volskin_01mm_softskin_soft_interior_125g", "solve", "stage5_scout_simple_gland_volskin_01mm_softskin_soft_interior_125g_solve_ews_surface_time_series.csv"),
        "#c2410c",
    ),
    Case(
        "skin_15mm_mid",
        "1.5 mm intermediate skin",
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_volskin_15mm_mid_skin088kpa_soft_interior_125g_solve_only_preview", "solve", "stage5_scout_simple_gland_volskin_15mm_mid_skin088kpa_soft_interior_125g_solve_only_preview_ews_surface_summary.json"),
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_volskin_15mm_mid_skin088kpa_soft_interior_125g_solve_only_preview", "solve", "stage5_scout_simple_gland_volskin_15mm_mid_skin088kpa_soft_interior_125g_solve_only_preview_ews_surface_time_series.csv"),
        "#0f766e",
    ),
    Case(
        "skin_15mm_stiff",
        "1.5 mm stiff skin",
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_volskin_15mm_stiff_skin_soft_interior_125g_solve_only_preview", "solve", "stage5_scout_simple_gland_volskin_15mm_stiff_skin_soft_interior_125g_solve_only_preview_ews_surface_summary.json"),
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_volskin_15mm_stiff_skin_soft_interior_125g_solve_only_preview", "solve", "stage5_scout_simple_gland_volskin_15mm_stiff_skin_soft_interior_125g_solve_only_preview_ews_surface_time_series.csv"),
        "#155e75",
    ),
]


COOPER_CASES = [
    Case(
        "ref_no_cooper",
        "No Cooper reference",
        SKIN_CASES[0].summary,
        SKIN_CASES[0].time_series,
        "#2f4f4f",
    ),
    Case(
        "cooper_mild",
        "Mild support",
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_cooper_mild_volskin_femke_skin_soft_interior_125g_solve_only_preview", "solve", "stage5_scout_simple_gland_cooper_mild_volskin_femke_skin_soft_interior_125g_solve_only_preview_ews_surface_summary.json"),
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_cooper_mild_volskin_femke_skin_soft_interior_125g_solve_only_preview", "solve", "stage5_scout_simple_gland_cooper_mild_volskin_femke_skin_soft_interior_125g_solve_only_preview_ews_surface_time_series.csv"),
        "#6d28d9",
    ),
    Case(
        "cooper_g2s_low",
        "Skin-gland support, low area",
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_cooper_g2skin_area006_volskin_femke_skin_soft_interior_125g", "solve", "stage5_scout_simple_gland_cooper_g2skin_area006_volskin_femke_skin_soft_interior_125g_solve_ews_surface_summary.json"),
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_cooper_g2skin_area006_volskin_femke_skin_soft_interior_125g", "solve", "stage5_scout_simple_gland_cooper_g2skin_area006_volskin_femke_skin_soft_interior_125g_solve_ews_surface_time_series.csv"),
        "#7c3aed",
    ),
    Case(
        "cooper_g2s_ref",
        "Skin-gland support, reference area",
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_cooper_g2skin_area012_volskin_femke_skin_soft_interior_125g", "solve", "stage5_scout_simple_gland_cooper_g2skin_area012_volskin_femke_skin_soft_interior_125g_solve_ews_surface_summary.json"),
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_cooper_g2skin_area012_volskin_femke_skin_soft_interior_125g", "solve", "stage5_scout_simple_gland_cooper_g2skin_area012_volskin_femke_skin_soft_interior_125g_solve_ews_surface_time_series.csv"),
        "#8b5cf6",
    ),
    Case(
        "cooper_n2cw",
        "Nipple-chestwall support",
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_cooper_nipple_to_chestwall_volskin_femke_skin_soft_interior_125g", "solve", "stage5_scout_simple_gland_cooper_nipple_to_chestwall_volskin_femke_skin_soft_interior_125g_solve_ews_surface_summary.json"),
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_cooper_nipple_to_chestwall_volskin_femke_skin_soft_interior_125g", "solve", "stage5_scout_simple_gland_cooper_nipple_to_chestwall_volskin_femke_skin_soft_interior_125g_solve_ews_surface_time_series.csv"),
        "#a855f7",
    ),
    Case(
        "cooper_dense",
        "Dense skin-gland support",
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_cooper_dense_network_volskin_femke_skin_soft_interior_125g", "solve", "stage5_scout_simple_gland_cooper_dense_network_volskin_femke_skin_soft_interior_125g_solve_ews_surface_summary.json"),
        p("runs", "comsol_runs", "geometry_stage5", "outputs", "output_stage5_scout_simple_gland_cooper_dense_network_volskin_femke_skin_soft_interior_125g", "solve", "stage5_scout_simple_gland_cooper_dense_network_volskin_femke_skin_soft_interior_125g_solve_ews_surface_time_series.csv"),
        "#c084fc",
    ),
]


TUMOR_EWS_SUMMARY = p("runs", "comsol_runs", "geometry_stage6", "outputs", "output_stage6_scout_simple_gland_medium_upper_outer_softskin_soft_interior_125g", "solve", "stage6_scout_simple_gland_medium_upper_outer_softskin_soft_interior_125g_solve_ews_surface_summary.json")
TUMOR_INTERNAL_SUMMARY = p("runs", "comsol_runs", "geometry_stage6", "outputs", "output_stage6_scout_simple_gland_medium_upper_outer_softskin_soft_interior_125g", "solve", "stage6_scout_simple_gland_medium_upper_outer_softskin_soft_interior_125g_solve_internal_tumor_summary.json")
TUMOR_INTERNAL_TS = p("runs", "comsol_runs", "geometry_stage6", "outputs", "output_stage6_scout_simple_gland_medium_upper_outer_softskin_soft_interior_125g", "solve", "stage6_scout_simple_gland_medium_upper_outer_softskin_soft_interior_125g_solve_internal_tumor_time_series.csv")


def read_json(path: Path) -> dict:
    with path.open("r", encoding="utf-8") as handle:
        return json.load(handle, parse_constant=lambda value: math.nan)


def read_csv_rows(path: Path) -> list[dict[str, float]]:
    rows: list[dict[str, float]] = []
    with path.open("r", encoding="utf-8", newline="") as handle:
        reader = csv.DictReader(handle)
        for row in reader:
            parsed = {}
            for key, value in row.items():
                try:
                    parsed[key] = float(value)
                except (TypeError, ValueError):
                    parsed[key] = math.nan
            rows.append(parsed)
    return rows


def finite(value: float | int | None) -> bool:
    return value is not None and not math.isnan(float(value)) and math.isfinite(float(value))


def pct_change(value: float, reference: float) -> float:
    return 100.0 * (value - reference) / reference if reference else math.nan


def safe_summary_value(summary: dict, key: str, scale: float = 1.0) -> float:
    value = summary.get(key, math.nan)
    return float(value) * scale if finite(value) else math.nan


def configure_style() -> None:
    return None


def font(size: int, bold: bool = False) -> ImageFont.FreeTypeFont | ImageFont.ImageFont:
    candidates = [
        "C:/Windows/Fonts/arialbd.ttf" if bold else "C:/Windows/Fonts/arial.ttf",
        "C:/Windows/Fonts/calibrib.ttf" if bold else "C:/Windows/Fonts/calibri.ttf",
    ]
    for candidate in candidates:
        try:
            return ImageFont.truetype(candidate, size=size)
        except OSError:
            continue
    return ImageFont.load_default()


def text_size(draw: ImageDraw.ImageDraw, text: str, fnt: ImageFont.ImageFont) -> tuple[int, int]:
    box = draw.textbbox((0, 0), text, font=fnt)
    return box[2] - box[0], box[3] - box[1]


def nice_limits(values: list[float], include_zero: bool = True) -> tuple[float, float]:
    finite_values = [float(v) for v in values if finite(v)]
    if include_zero:
        finite_values.append(0.0)
    if not finite_values:
        return 0.0, 1.0
    lo = min(finite_values)
    hi = max(finite_values)
    if math.isclose(lo, hi):
        pad = abs(hi) * 0.1 if hi else 1.0
        return lo - pad, hi + pad
    pad = 0.08 * (hi - lo)
    return lo - pad, hi + pad


def save_pdf_from_png(png_path: Path, pdf_path: Path, width: int, height: int) -> None:
    pdf = canvas.Canvas(str(pdf_path), pagesize=(width, height))
    pdf.drawImage(ImageReader(str(png_path)), 0, 0, width=width, height=height)
    pdf.showPage()
    pdf.save()


def svg_header(width: int, height: int) -> list[str]:
    return [
        f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}" viewBox="0 0 {width} {height}">',
        '<rect width="100%" height="100%" fill="white"/>',
        '<style>text{font-family:Arial,Helvetica,sans-serif;fill:#111827}.small{font-size:17px}.label{font-size:19px}.title{font-size:24px;font-weight:700}</style>',
    ]


def escape(text: str) -> str:
    return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")


def write_svg(path: Path, lines: list[str]) -> None:
    path.write_text("\n".join(lines + ["</svg>", ""]), encoding="utf-8")


def save_chart_exports(stem: str, image: Image.Image, svg_lines: list[str]) -> None:
    png_path = OUT_DIR / f"{stem}.png"
    image.save(png_path)
    save_pdf_from_png(png_path, OUT_DIR / f"{stem}.pdf", image.width, image.height)
    write_svg(OUT_DIR / f"{stem}.svg", svg_lines)


def draw_rotated_ylabel(
    image: Image.Image,
    draw: ImageDraw.ImageDraw,
    svg: list[str],
    ylabel: str,
    plot_top: int,
    plot_h: int,
) -> None:
    label_font = font(20)
    tw, th = text_size(draw, ylabel, label_font)
    label_image = Image.new("RGBA", (tw + 8, th + 8), (255, 255, 255, 0))
    label_draw = ImageDraw.Draw(label_image)
    label_draw.text((4, 4), ylabel, font=label_font, fill="#111827")
    rotated = label_image.rotate(90, expand=True)
    x = 4
    y = int(plot_top + plot_h / 2 - rotated.height / 2)
    image.paste(rotated, (x, y), rotated)
    svg.append(
        f'<text class="label" transform="translate(28 {plot_top + plot_h / 2:.2f}) rotate(-90)" text-anchor="middle">{escape(ylabel)}</text>'
    )


def draw_line_chart(
    stem: str,
    title: str,
    xlabel: str,
    ylabel: str,
    series: list[tuple[str, list[float], list[float], str]],
    width: int = 1400,
    height: int = 760,
) -> None:
    margin = {"left": 145, "right": 420, "top": 80, "bottom": 90}
    plot_w = width - margin["left"] - margin["right"]
    plot_h = height - margin["top"] - margin["bottom"]
    image = Image.new("RGB", (width, height), "white")
    draw = ImageDraw.Draw(image)
    title_font = font(26, bold=True)
    label_font = font(20)
    small_font = font(17)
    svg = svg_header(width, height)

    all_x = [x for _, xs, _, _ in series for x in xs if finite(x)]
    all_y = [y for _, _, ys, _ in series for y in ys if finite(y)]
    x_min, x_max = nice_limits(all_x, include_zero=True)
    y_min, y_max = nice_limits(all_y, include_zero=True)

    def sx(x: float) -> float:
        return margin["left"] + (x - x_min) / (x_max - x_min) * plot_w

    def sy(y: float) -> float:
        return margin["top"] + plot_h - (y - y_min) / (y_max - y_min) * plot_h

    draw.text((margin["left"], 26), title, font=title_font, fill="#111827")
    svg.append(f'<text class="title" x="{margin["left"]}" y="46">{escape(title)}</text>')

    for i in range(6):
        frac = i / 5
        y_val = y_min + frac * (y_max - y_min)
        y = sy(y_val)
        draw.line((margin["left"], y, margin["left"] + plot_w, y), fill="#e5e7eb", width=1)
        draw.text((58, y - 10), f"{y_val:.1f}", font=small_font, fill="#374151")
        svg.append(f'<line x1="{margin["left"]}" y1="{y:.2f}" x2="{margin["left"] + plot_w}" y2="{y:.2f}" stroke="#e5e7eb"/>')
        svg.append(f'<text class="small" x="58" y="{y + 6:.2f}">{y_val:.1f}</text>')

    for i in range(6):
        frac = i / 5
        x_val = x_min + frac * (x_max - x_min)
        x = sx(x_val)
        draw.line((x, margin["top"], x, margin["top"] + plot_h), fill="#f3f4f6", width=1)
        draw.text((x - 18, margin["top"] + plot_h + 12), f"{x_val:.1f}", font=small_font, fill="#374151")
        svg.append(f'<line x1="{x:.2f}" y1="{margin["top"]}" x2="{x:.2f}" y2="{margin["top"] + plot_h}" stroke="#f3f4f6"/>')
        svg.append(f'<text class="small" x="{x - 18:.2f}" y="{margin["top"] + plot_h + 31}">{x_val:.1f}</text>')

    draw.line((margin["left"], margin["top"], margin["left"], margin["top"] + plot_h), fill="#111827", width=2)
    draw.line((margin["left"], margin["top"] + plot_h, margin["left"] + plot_w, margin["top"] + plot_h), fill="#111827", width=2)
    svg.append(f'<line x1="{margin["left"]}" y1="{margin["top"]}" x2="{margin["left"]}" y2="{margin["top"] + plot_h}" stroke="#111827" stroke-width="2"/>')
    svg.append(f'<line x1="{margin["left"]}" y1="{margin["top"] + plot_h}" x2="{margin["left"] + plot_w}" y2="{margin["top"] + plot_h}" stroke="#111827" stroke-width="2"/>')

    for label, xs, ys, color in series:
        pts = [(sx(x), sy(y)) for x, y in zip(xs, ys) if finite(x) and finite(y)]
        if len(pts) > 1:
            draw.line(pts, fill=color, width=3)
            point_str = " ".join(f"{x:.2f},{y:.2f}" for x, y in pts)
            svg.append(f'<polyline points="{point_str}" fill="none" stroke="{color}" stroke-width="3"/>')

    draw.text((margin["left"] + plot_w / 2 - 35, height - 42), xlabel, font=label_font, fill="#111827")
    svg.append(f'<text class="label" x="{margin["left"] + plot_w / 2 - 35:.2f}" y="{height - 22}">{escape(xlabel)}</text>')
    draw_rotated_ylabel(image, draw, svg, ylabel, margin["top"], plot_h)

    legend_x = margin["left"] + plot_w + 30
    legend_y = margin["top"]
    for idx, (label, _, _, color) in enumerate(series):
        y = legend_y + idx * 38
        draw.line((legend_x, y + 10, legend_x + 28, y + 10), fill=color, width=4)
        draw.text((legend_x + 38, y), label, font=small_font, fill="#111827")
        svg.append(f'<line x1="{legend_x}" y1="{y + 10}" x2="{legend_x + 28}" y2="{y + 10}" stroke="{color}" stroke-width="4"/>')
        svg.append(f'<text class="small" x="{legend_x + 38}" y="{y + 17}">{escape(label)}</text>')

    save_chart_exports(stem, image, svg)


def draw_bar_chart(
    stem: str,
    title: str,
    ylabel: str,
    labels: list[str],
    values: list[float],
    colors: list[str],
    percent_labels: bool = False,
    width: int = 1300,
    height: int = 760,
) -> None:
    margin = {"left": 145, "right": 60, "top": 85, "bottom": 205}
    plot_w = width - margin["left"] - margin["right"]
    plot_h = height - margin["top"] - margin["bottom"]
    image = Image.new("RGB", (width, height), "white")
    draw = ImageDraw.Draw(image)
    title_font = font(26, bold=True)
    label_font = font(20)
    small_font = font(16)
    svg = svg_header(width, height)

    y_min, y_max = nice_limits(values, include_zero=True)

    def sy(y: float) -> float:
        return margin["top"] + plot_h - (y - y_min) / (y_max - y_min) * plot_h

    draw.text((margin["left"], 26), title, font=title_font, fill="#111827")
    svg.append(f'<text class="title" x="{margin["left"]}" y="46">{escape(title)}</text>')

    for i in range(6):
        frac = i / 5
        y_val = y_min + frac * (y_max - y_min)
        y = sy(y_val)
        draw.line((margin["left"], y, margin["left"] + plot_w, y), fill="#e5e7eb", width=1)
        draw.text((58, y - 10), f"{y_val:.1f}", font=small_font, fill="#374151")
        svg.append(f'<line x1="{margin["left"]}" y1="{y:.2f}" x2="{margin["left"] + plot_w}" y2="{y:.2f}" stroke="#e5e7eb"/>')
        svg.append(f'<text class="small" x="58" y="{y + 6:.2f}">{y_val:.1f}</text>')

    zero_y = sy(0.0)
    draw.line((margin["left"], zero_y, margin["left"] + plot_w, zero_y), fill="#111827", width=2)
    draw.line((margin["left"], margin["top"], margin["left"], margin["top"] + plot_h), fill="#111827", width=2)
    svg.append(f'<line x1="{margin["left"]}" y1="{zero_y:.2f}" x2="{margin["left"] + plot_w}" y2="{zero_y:.2f}" stroke="#111827" stroke-width="2"/>')
    svg.append(f'<line x1="{margin["left"]}" y1="{margin["top"]}" x2="{margin["left"]}" y2="{margin["top"] + plot_h}" stroke="#111827" stroke-width="2"/>')

    slot = plot_w / max(1, len(values))
    bar_w = min(100, slot * 0.65)
    for idx, (label, value, color) in enumerate(zip(labels, values, colors)):
        x_mid = margin["left"] + slot * idx + slot / 2
        x0 = x_mid - bar_w / 2
        x1 = x_mid + bar_w / 2
        y0 = sy(max(0.0, value))
        y1 = sy(min(0.0, value))
        draw.rectangle((x0, y0, x1, y1), fill=color)
        svg.append(f'<rect x="{x0:.2f}" y="{y0:.2f}" width="{bar_w:.2f}" height="{abs(y1 - y0):.2f}" fill="{color}"/>')
        label_text = f"{value:.1f}%" if percent_labels else f"{value:.2f}"
        ty = y0 - 24 if value >= 0 else y1 + 6
        tw, _ = text_size(draw, label_text, small_font)
        draw.text((x_mid - tw / 2, ty), label_text, font=small_font, fill="#111827")
        svg.append(f'<text class="small" x="{x_mid - tw / 2:.2f}" y="{ty + 17:.2f}">{escape(label_text)}</text>')

        wrapped = label.replace(", ", ",\n").replace(" support", "\nsupport").replace(" skin", "\nskin")
        for line_idx, line in enumerate(wrapped.split("\n")):
            tw, _ = text_size(draw, line, small_font)
            draw.text((x_mid - tw / 2, margin["top"] + plot_h + 18 + line_idx * 20), line, font=small_font, fill="#111827")
            svg.append(f'<text class="small" x="{x_mid - tw / 2:.2f}" y="{margin["top"] + plot_h + 35 + line_idx * 20}">{escape(line)}</text>')

    draw_rotated_ylabel(image, draw, svg, ylabel, margin["top"], plot_h)
    save_chart_exports(stem, image, svg)


def write_csv(path: Path, rows: list[dict[str, object]]) -> None:
    if not rows:
        return
    with path.open("w", encoding="utf-8", newline="") as handle:
        writer = csv.DictWriter(handle, fieldnames=list(rows[0].keys()))
        writer.writeheader()
        writer.writerows(rows)


def write_markdown_table(path: Path, rows: list[dict[str, object]]) -> None:
    if not rows:
        return
    headers = list(rows[0].keys())
    lines = [
        "| " + " | ".join(headers) + " |",
        "| " + " | ".join("---" for _ in headers) + " |",
    ]
    for row in rows:
        lines.append("| " + " | ".join(str(row.get(header, "")) for header in headers) + " |")
    path.write_text("\n".join(lines) + "\n", encoding="utf-8")


def round_or_na(value: float, digits: int = 3) -> str:
    return "n/a" if not finite(value) else f"{float(value):.{digits}f}"


def validate_cases(cases: Iterable[Case]) -> None:
    missing = []
    for case in cases:
        if not case.summary.exists():
            missing.append(str(case.summary))
        if not case.time_series.exists():
            missing.append(str(case.time_series))
    for path in (TUMOR_EWS_SUMMARY, TUMOR_INTERNAL_SUMMARY, TUMOR_INTERNAL_TS):
        if not path.exists():
            missing.append(str(path))
    if missing:
        raise FileNotFoundError("Missing required postprocess files:\n" + "\n".join(missing))


def make_sensitivity_table(cases: list[Case], reference: Case) -> list[dict[str, object]]:
    ref_summary = read_json(reference.summary)
    ref_peak = safe_summary_value(ref_summary, "max_displacement_breast_mm")
    ref_review = safe_summary_value(ref_summary, "review_surface_disp_mag_max_mm")
    rows = []
    for case in cases:
        summary = read_json(case.summary)
        peak = safe_summary_value(summary, "max_displacement_breast_mm")
        mean = safe_summary_value(summary, "avg_displacement_breast_mm")
        vm_kpa = safe_summary_value(summary, "max_von_mises_breast_pa", 1e-3)
        review = safe_summary_value(summary, "review_surface_disp_mag_max_mm")
        rows.append(
            {
                "label": case.label,
                "peak_breast_displacement_mm": round_or_na(peak),
                "mean_breast_displacement_mm": round_or_na(mean),
                "peak_von_mises_kpa": round_or_na(vm_kpa),
                "review_surface_max_displacement_mm": round_or_na(review),
                "peak_displacement_change_percent": round_or_na(pct_change(peak, ref_peak), 1),
                "review_surface_change_percent": round_or_na(pct_change(review, ref_review), 1),
            }
        )
    return rows


def plot_time_series(cases: list[Case], stem: str, title: str) -> None:
    series = []
    for case in cases:
        rows = read_csv_rows(case.time_series)
        series.append(
            (
                case.label,
                [row["time_s"] for row in rows],
                [row["disp_max_mm"] for row in rows],
                case.color,
            )
        )
    draw_line_chart(stem, title, "Time (s)", "Peak breast displacement (mm)", series)


def plot_change_bars(rows: list[dict[str, object]], stem: str, title: str, field: str, ylabel: str, skip_reference: bool = False) -> None:
    plot_rows = rows[1:] if skip_reference else rows
    labels = [str(row["label"]) for row in plot_rows]
    values = [float(row[field]) if row[field] != "n/a" else math.nan for row in plot_rows]
    colors = ["#737373" if v >= 0 else "#2563eb" for v in values]
    draw_bar_chart(stem, title, ylabel, labels, values, colors, percent_labels=True)


def make_skin_figures() -> list[dict[str, object]]:
    rows = make_sensitivity_table(SKIN_CASES, SKIN_CASES[0])
    write_csv(OUT_DIR / "skin_material_sensitivity_summary.csv", rows)
    write_markdown_table(OUT_DIR / "skin_material_sensitivity_summary.md", rows)
    plot_time_series(SKIN_CASES, "skin_material_peak_displacement_timeseries", "Skin/material sensitivity")
    plot_change_bars(
        rows,
        "skin_material_peak_displacement_change",
        "Skin/material effect on peak displacement",
        "peak_displacement_change_percent",
        "Change relative to reference (%)",
    )
    plot_change_bars(
        rows,
        "skin_material_review_surface_change",
        "Skin/material effect at review surface time",
        "review_surface_change_percent",
        "Review surface change relative to reference (%)",
    )
    return rows


def make_cooper_figures() -> list[dict[str, object]]:
    rows = make_sensitivity_table(COOPER_CASES, COOPER_CASES[0])
    write_csv(OUT_DIR / "cooper_support_sensitivity_summary.csv", rows)
    write_markdown_table(OUT_DIR / "cooper_support_sensitivity_summary.md", rows)
    plot_time_series(COOPER_CASES, "cooper_peak_displacement_timeseries", "Cooper-like support sensitivity")
    plot_change_bars(
        rows,
        "cooper_peak_displacement_change",
        "Cooper-like support effect on peak displacement",
        "peak_displacement_change_percent",
        "Change relative to no-Cooper reference (%)",
        skip_reference=True,
    )
    plot_change_bars(
        rows,
        "cooper_review_surface_change",
        "Cooper-like support effect at review surface time",
        "review_surface_change_percent",
        "Review surface change relative to no-Cooper reference (%)",
        skip_reference=True,
    )
    return rows


def make_tumor_figures() -> list[dict[str, object]]:
    ews = read_json(TUMOR_EWS_SUMMARY)
    internal = read_json(TUMOR_INTERNAL_SUMMARY)
    rows = read_csv_rows(TUMOR_INTERNAL_TS)

    time = [row["time_s"] for row in rows]
    draw_line_chart(
        "tumor_breast_vs_tumor_displacement_timeseries",
        "Simple-gland tumor response",
        "Time (s)",
        "Peak displacement (mm)",
        [
            ("Breast peak displacement", time, [row["disp_max_mm"] for row in rows], "#1f2937"),
            ("Tumor-region peak displacement", time, [row["tumor_disp_max_mm"] for row in rows], "#b91c1c"),
        ],
    )

    tumor_rows = [
        {
            "quantity": "Tumor volume",
            "value": round_or_na(safe_summary_value(internal, "tumor_volume_ml"), 3),
            "unit": "ml",
        },
        {
            "quantity": "Peak breast displacement",
            "value": round_or_na(safe_summary_value(ews, "max_displacement_breast_mm"), 3),
            "unit": "mm",
        },
        {
            "quantity": "Peak tumor-region displacement",
            "value": round_or_na(safe_summary_value(internal, "max_displacement_tumor_mm"), 3),
            "unit": "mm",
        },
        {
            "quantity": "Peak tumor von Mises stress",
            "value": round_or_na(safe_summary_value(internal, "max_von_mises_tumor_pa", 1e-3), 3),
            "unit": "kPa",
        },
        {
            "quantity": "Review surface max displacement",
            "value": round_or_na(safe_summary_value(ews, "review_surface_disp_mag_max_mm"), 3),
            "unit": "mm",
        },
    ]
    write_csv(OUT_DIR / "tumor_sensitivity_summary.csv", tumor_rows)
    write_markdown_table(OUT_DIR / "tumor_sensitivity_summary.md", tumor_rows)

    values = [float(row["value"]) for row in tumor_rows if row["value"] != "n/a"]
    labels = [f"{row['quantity']} ({row['unit']})" for row in tumor_rows if row["value"] != "n/a"]
    draw_bar_chart(
        "tumor_summary_metrics",
        "Simple-gland tumor summary metrics",
        "Metric value",
        labels,
        values,
        ["#737373", "#1f2937", "#b91c1c", "#ef4444", "#2563eb"],
        percent_labels=False,
    )

    return tumor_rows


def make_overall_summary(skin_rows: list[dict[str, object]], cooper_rows: list[dict[str, object]], tumor_rows: list[dict[str, object]]) -> list[dict[str, object]]:
    skin_changes = [float(row["peak_displacement_change_percent"]) for row in skin_rows[1:] if row["peak_displacement_change_percent"] != "n/a"]
    cooper_changes = [float(row["peak_displacement_change_percent"]) for row in cooper_rows[1:] if row["peak_displacement_change_percent"] != "n/a"]
    tumor_peak_breast = next(float(row["value"]) for row in tumor_rows if row["quantity"] == "Peak breast displacement")
    tumor_peak_region = next(float(row["value"]) for row in tumor_rows if row["quantity"] == "Peak tumor-region displacement")
    ref_peak = safe_summary_value(read_json(SKIN_CASES[0].summary), "max_displacement_breast_mm")
    tumor_global_delta = pct_change(tumor_peak_breast, ref_peak)

    rows = [
        {
            "route": "Skin/material variation",
            "main_metric": "Peak breast displacement",
            "observed_range": f"{min(skin_changes):.1f}% to {max(skin_changes):.1f}%",
            "results_message": "Strong response sensitivity to volumetric skin thickness/stiffness.",
        },
        {
            "route": "Cooper-like support",
            "main_metric": "Peak breast displacement",
            "observed_range": f"{min(cooper_changes):.1f}% to {max(cooper_changes):.1f}%",
            "results_message": "Small global response change in the tested simplified support variants.",
        },
        {
            "route": "Tumor overlay",
            "main_metric": "Global vs tumor-region displacement",
            "observed_range": f"global {tumor_global_delta:.1f}% vs ref; tumor-region peak {tumor_peak_region:.1f} mm",
            "results_message": "Small global surface effect, but local tumor-mask response is present.",
        },
    ]
    write_csv(OUT_DIR / "overall_results_summary.csv", rows)
    write_markdown_table(OUT_DIR / "overall_results_summary.md", rows)

    labels = [row["route"] for row in rows]
    values = [max(abs(v) for v in skin_changes), max(abs(v) for v in cooper_changes), abs(tumor_global_delta)]
    draw_bar_chart(
        "overall_global_effect_size_summary",
        "Relative size of main global response effects",
        "Largest absolute global peak-displacement change (%)",
        labels,
        values,
        ["#0f766e", "#7c3aed", "#b91c1c"],
        percent_labels=True,
    )
    return rows


def write_readme(
    skin_rows: list[dict[str, object]],
    cooper_rows: list[dict[str, object]],
    tumor_rows: list[dict[str, object]],
    overall_rows: list[dict[str, object]],
) -> None:
    figure_files = sorted(path.name for path in OUT_DIR.glob("*.png"))
    table_files = sorted(path.name for path in OUT_DIR.glob("*.csv"))
    inputs = []
    for case in SKIN_CASES + COOPER_CASES:
        inputs.extend([case.summary, case.time_series])
    inputs.extend([TUMOR_EWS_SUMMARY, TUMOR_INTERNAL_SUMMARY, TUMOR_INTERNAL_TS])
    unique_inputs = []
    seen = set()
    for path in inputs:
        rel = path.relative_to(ROOT).as_posix()
        if rel not in seen:
            seen.add(rel)
            unique_inputs.append(rel)

    text = [
        "# Results figures inventory",
        "",
        "Generated from existing COMSOL postprocess JSON/CSV outputs only. No COMSOL solve or postprocess command was run by this plotting script.",
        "",
        "## Created figures",
        "",
    ]
    text.extend(f"- `{name}`" for name in figure_files)
    text.extend(["", "PDF and SVG versions with the same stems were also exported for report use.", ""])
    text.extend(["## Created tables", ""])
    text.extend(f"- `{name}`" for name in table_files)
    text.extend(["", "Markdown versions of the main tables are also present for quick inspection.", ""])
    text.extend(["## Input files", ""])
    text.extend(f"- `{path}`" for path in unique_inputs)
    text.extend(
        [
            "",
            "## Suggested report use",
            "",
            "- `skin_material_peak_displacement_timeseries` and `skin_material_peak_displacement_change`: use for the skin/material sensitivity Results subsection.",
            "- `cooper_peak_displacement_timeseries` and `cooper_peak_displacement_change`: use for the Cooper-support subsection; the bar plot is the clearest because the effects are small.",
            "- `tumor_breast_vs_tumor_displacement_timeseries` and `tumor_summary_metrics`: use for the tumor-overlay subsection.",
            "- `overall_global_effect_size_summary`: use only as a compact summary figure if there is space; otherwise the table may be enough.",
            "",
            "## Short interpretations",
            "",
        ]
    )
    text.extend(f"- {row['route']}: {row['results_message']} ({row['observed_range']})." for row in overall_rows)
    text.extend(
        [
            "",
            "## Caveats for Results wording",
            "",
            "- The Stage 5 reference label in the raw file names contains internal development wording; the figures use cleaned report labels.",
            "- The tumor comparison uses the available simple-gland tumor route and should not be overclaimed as a fully matched realistic-gland tumor comparison.",
            "- The newest realistic-gland/volumetric-skin tumor case has only lightweight global/tumor-displacement output; full surface/stress postprocessing was not practical for the large result MPH.",
            "- Cooper-like support is a simplified restoring-support approximation, not an anatomical ligament reconstruction.",
            "- Higher von Mises values in stiffer skin cases should be described as model stress concentrations, not validated physiological tissue stresses.",
            "- The `review_surface_max_displacement` values are review-time surface metrics from the summary JSON, not dense full-field surface maps.",
            "",
            "## Caption suggestions",
            "",
            "- Skin/material figure: Time-dependent peak breast displacement and relative peak-displacement changes for the volumetric-skin sensitivity cases.",
            "- Cooper figure: Time-dependent peak breast displacement and relative peak-displacement changes for simplified Cooper-like support variants.",
            "- Tumor figure: Breast and tumor-mask displacement response for the simple-gland medium upper-outer tumor case.",
            "- Overall summary: Relative magnitude of the main global peak-displacement effects across skin/material, Cooper-support and tumor-overlay routes.",
            "",
        ]
    )
    (OUT_DIR / "README.md").write_text("\n".join(text), encoding="utf-8")


def main() -> None:
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    configure_style()
    validate_cases(SKIN_CASES + COOPER_CASES)
    skin_rows = make_skin_figures()
    cooper_rows = make_cooper_figures()
    tumor_rows = make_tumor_figures()
    overall_rows = make_overall_summary(skin_rows, cooper_rows, tumor_rows)
    write_readme(skin_rows, cooper_rows, tumor_rows, overall_rows)
    print(f"Wrote results figures and tables to: {OUT_DIR}")


if __name__ == "__main__":
    main()
