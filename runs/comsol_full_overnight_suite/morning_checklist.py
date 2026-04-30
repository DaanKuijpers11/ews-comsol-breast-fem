from __future__ import annotations

from pathlib import Path


ROOT = Path(r"C:\Users\20223231\ews_fem_clean")
SUITE_DIR = ROOT / "runs" / "comsol_full_overnight_suite"
CASES = (
    "full_baseline_reference",
    "full_curved_chest_reference",
    "full_freeze_probe_v1",
    "full_freeze_probe_v2_curved",
    "full_adipose_gradient_stress",
)


def _bool_text(value: bool) -> str:
    return "True" if value else "False"


def main() -> int:
    headers = (
        "Case",
        "Toml",
        "ResolvedCase",
        "BuildVerification",
        "MetricsJson",
        "ResultMph",
        "ComsolLog",
    )
    rows: list[tuple[str, ...]] = []

    for case in CASES:
        output_dir = SUITE_DIR / f"output_{case}"
        rows.append(
            (
                case,
                _bool_text((SUITE_DIR / f"{case}.toml").exists()),
                _bool_text((output_dir / f"{case}_resolved_case.toml").exists()),
                _bool_text((output_dir / "build" / f"{case}_build_verification.json").exists()),
                _bool_text((output_dir / "solve" / f"{case}_metrics.json").exists()),
                _bool_text((output_dir / "solve" / f"{case}_result.mph").exists()),
                _bool_text((output_dir / "logs" / f"{case}_comsol.log").exists()),
            )
        )

    widths = [len(header) for header in headers]
    for row in rows:
        for idx, value in enumerate(row):
            widths[idx] = max(widths[idx], len(value))

    print("\nMorning checklist for COMSOL full overnight suite\n")
    print(" ".join(header.ljust(widths[idx]) for idx, header in enumerate(headers)))
    print(" ".join("-" * widths[idx] for idx in range(len(headers))))
    for row in rows:
        print(" ".join(value.ljust(widths[idx]) for idx, value in enumerate(row)))

    print("\nOpen these first if something failed:")
    print("1. logs/*_comsol.log")
    print("2. build/*_build_verification.json")
    print("3. *_resolved_case.toml")
    print("\nIf all MetricsJson values are True, the suite is ready for compare-metrics.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
