import os
import subprocess
import sys
from pathlib import Path


def workspace_root() -> Path:
    return Path(__file__).resolve().parents[2]


def scripts_dir() -> Path:
    return workspace_root() / "scripts"


def analysis_script() -> Path:
    return scripts_dir() / "data_analysis_main.py"


def resolve_run_name(path_like: str | Path) -> str:
    path = Path(path_like)

    if path.suffix == ".toml":
        return path.stem
    if path.suffix == ".feb":
        return path.stem
    if path.name == "output" and path.parent.name:
        return path.parent.name
    if path.is_dir():
        return path.name

    return path.stem or path.name


def evaluate_runs(run_names: tuple[str, ...]) -> tuple[str, ...]:
    """
    Run the existing analysis pipeline once per run name.
    """
    script_path = analysis_script()
    root = workspace_root()

    for run_name in run_names:
        env = os.environ.copy()
        env["RUN_NAME"] = run_name
        existing_pythonpath = env.get("PYTHONPATH", "")
        extra_pythonpath = str(root / "scripts")
        env["PYTHONPATH"] = (
            extra_pythonpath if not existing_pythonpath
            else os.pathsep.join([extra_pythonpath, existing_pythonpath])
        )
        subprocess.run([sys.executable, str(script_path)], cwd=root, env=env, check=True)

    return run_names
