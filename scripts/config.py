"""
Configuration file for breast biomechanics analysis pipeline.

This file defines:
- Project paths (robust across machines)
- Input/output structure for simulation runs
- Model definitions for comparison
- Analysis parameters (steps, landmarks)
- Visualization settings
- Literature reference values for validation

IMPORTANT:
All paths are relative to the project root to avoid OS/user-specific issues.
"""

from pathlib import Path

# =========================
# PROJECT ROOT
# =========================
# This assumes:
#   config.py is in scripts/
#   project root is one level up
BASE_DIR = Path(__file__).resolve().parent.parent


# =========================
# INPUT / OUTPUT STRUCTURE
# =========================
# Where your FEM runs are stored (IMPORTANT for your overnight runs)
RUNS_DIR = BASE_DIR / "runs"

# Default output folder for processed results (figures, csv, etc.)
ANALYSIS_OUTPUT_DIR = BASE_DIR / "analysis_output"

# Optional: central figures directory
FIGURES_DIR = ANALYSIS_OUTPUT_DIR / "figures"


# =========================
# MODEL INPUTS
# =========================
# Each model points to its VTK output directory

MODELS_TO_COMPARE = {
    "sweep_config": RUNS_DIR / "sweep_config/output",
    #"overnight_hetero_zwaar": RUNS_DIR / "overnight_hetero_zwaar/output",
}


# =========================
# ANALYSIS PARAMETERS
# =========================
STEP_MIN = 0          # first timestep included
STEP_MAX = 20         # last timestep (exclusive)
STEP = 3              # landmark evaluation timestep
TARGET_Y = 0.039      # anatomical slice height for landmarks


# =========================
# NUMERICAL / QUALITY FLAGS
# =========================
# You can later use this for config scoring
QUALITY_METRICS_ENABLED = True
TRACK_INVERTED_ELEMENTS = True
TRACK_NEGATIVE_J = True
TRACK_J_DISTRIBUTION = True


# =========================
# TISSUE LABELS
# =========================
# Mapping from FEBio part IDs → human-readable names
TISSUE_LABELS = {
    1: "Glandular",
    2: "Adipose"
}


# =========================
# LABELS (ONLY ACTIVE MODELS)
# =========================
MODEL_LABELS = {
    "sweep_config": "Sweep Config Model",
    # "overnight_hetero_zwaar": "Heavy Heterogeneous Model",
}

MODEL_COLORS = {
    "sweep_config": "#d62728",
    # "overnight_hetero_zwaar": "#1f77b4",
}



# =========================
# LITERATURE REFERENCES
# =========================
LITERATURE_REFS = {
    "Chen_2025_Running_6kmh": {
        "Adipose_max_kPa": 10.56,
        "Glandular_max_kPa": 3.89,
        "displacement_vertical_cm": (4.2, 11.0),
    },
    "Chen_2025_Jumping": {
        "Adipose_max_kPa": 16.24,
        "Glandular_max_kPa": 4.22,
    },
    "General_Literature": {
        "vertical_displacement_cm": (4.2, 11.0),
        "medial_lateral_cm": (1.8, 6.2),
        "anterior_posterior_cm": (2.2, 5.9),
        "vertical_acceleration_g": (2.8, 4.87),
    }
}


# =========================
# OUTPUT STRUCTURE HELPERS
# =========================
def get_model_output_path(model_name: str) -> Path:
    """Return output directory for a model."""
    return MODELS_TO_COMPARE[model_name]


def get_figures_path(run_name: str = "default") -> Path:
    """Return figure output directory for a specific run."""
    path = FIGURES_DIR / run_name
    path.mkdir(parents=True, exist_ok=True)
    return path


def get_summary_csv_path(model_name: str) -> Path:
    """Return path for cached summary statistics."""
    return get_model_output_path(model_name).parent / "summary_statistics.csv"