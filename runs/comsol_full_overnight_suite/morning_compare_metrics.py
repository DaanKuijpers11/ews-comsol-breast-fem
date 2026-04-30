from __future__ import annotations

import subprocess
from pathlib import Path


ROOT = Path(r"C:\Users\20223231\ews_fem_clean")
PYTHON_EXE = Path(r"C:\Users\20223231\.conda\envs\ews-fem\python.exe")
CASES = (
    "runs/comsol_full_overnight_suite/full_baseline_reference.toml",
    "runs/comsol_full_overnight_suite/full_curved_chest_reference.toml",
    "runs/comsol_full_overnight_suite/full_freeze_probe_v1.toml",
    "runs/comsol_full_overnight_suite/full_freeze_probe_v2_curved.toml",
    "runs/comsol_full_overnight_suite/full_adipose_gradient_stress.toml",
)


def main() -> int:
    python_exe = PYTHON_EXE.resolve()
    cmd = [
        str(python_exe),
        "-m",
        "ews_fem_pipeline_comsol",
        "compare-metrics",
        *CASES,
        "--baseline",
        "full_baseline_reference",
    ]
    subprocess.run(cmd, cwd=str(ROOT), check=True)
    print("\nMetrics comparison written to:")
    print(r"C:\Users\20223231\ews_fem_clean\analysis_output\metrics_compare\solver_metrics_compare.csv")
    print(r"C:\Users\20223231\ews_fem_clean\analysis_output\metrics_compare\solver_metrics_compare.md")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
