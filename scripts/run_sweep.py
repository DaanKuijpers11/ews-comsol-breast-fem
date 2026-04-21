import subprocess
import itertools
from pathlib import Path
import toml
import shutil

# =========================
# SETTINGS
# =========================

BASE_TOML = Path("sweep_config.toml")
RUNS_DIR = Path("runs")

scale_y_values = [0.9, 1.0, 1.1]
scale_z_values = [0.9, 1.0, 1.1]


# =========================
# HELPER
# =========================

def run_pipeline(toml_path):
    cmd = ["python", "-m", "ews_fem_pipeline", "run", str(toml_path)]
    subprocess.run(cmd, check=True)


def run_analysis(run_name):
    """
    Calls data analysis pipeline
    """
    cmd = ["python", "scripts/data_analysis_main.py"]
    
    # pass run_name via env variable (simple trick)
    env = dict(**dict(), RUN_NAME=run_name)
    
    subprocess.run(cmd, env=env, check=True)


# =========================
# MAIN LOOP
# =========================

def run_case(scale_y, scale_z):

    case_name = f"asym_y{scale_y}_z{scale_z}"
    case_dir = RUNS_DIR / case_name
    case_dir.mkdir(parents=True, exist_ok=True)

    print(f"\n=== Running case: {case_name} ===")

    # -------------------------
    # Create TOML
    # -------------------------
    config = toml.load(BASE_TOML)

    config["model"]["geometry"]["asymmetry"]["enabled"] = True
    config["model"]["geometry"]["asymmetry"]["scale_y"] = scale_y
    config["model"]["geometry"]["asymmetry"]["scale_z"] = scale_z

    toml_path = case_dir / f"{case_name}.toml"

    with open(toml_path, "w") as f:
        toml.dump(config, f)

    # -------------------------
    # Run FEM pipeline
    # -------------------------
    run_pipeline(toml_path)

    # -------------------------
    # Run analysis
    # -------------------------
    run_analysis(case_name)


def main():

    for scale_y, scale_z in itertools.product(scale_y_values, scale_z_values):
        run_case(scale_y, scale_z)


if __name__ == "__main__":
    main()