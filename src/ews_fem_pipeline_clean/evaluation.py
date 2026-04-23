import os
import subprocess
import sys
from pathlib import Path
import logging


logger = logging.getLogger(__name__)


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


def resolve_case_paths(path_like: str | Path) -> tuple[str, Path, Path]:
    """
    Resolve a case name, VTK directory and FEB path from a TOML/FEB/output path.
    """
    path = Path(path_like)

    if path.suffix == ".toml":
        run_name = path.stem
        vtk_dir = path.parent / "output"
        feb_path = path.with_suffix(".feb")
        return run_name, vtk_dir, feb_path

    if path.suffix == ".feb":
        run_name = path.stem
        vtk_dir = path.parent / "output"
        feb_path = path
        return run_name, vtk_dir, feb_path

    if path.name == "output":
        run_name = path.parent.name
        vtk_dir = path
        feb_path = path.parent / f"{run_name}.feb"
        return run_name, vtk_dir, feb_path

    if path.is_dir():
        run_name = path.name
        vtk_dir = path / "output"
        feb_path = path / f"{run_name}.feb"
        return run_name, vtk_dir, feb_path

    run_name = resolve_run_name(path)
    return run_name, path.parent / "output", path.with_suffix(".feb")


def evaluate_runs(case_inputs: tuple[str | Path, ...]) -> tuple[str, ...]:
    """
    Run the existing analysis pipeline once per run name.
    """
    script_path = analysis_script()
    root = workspace_root()
    completed = []

    for case_input in case_inputs:
        run_name, vtk_dir, feb_path = resolve_case_paths(case_input)
        vtk_files = sorted(vtk_dir.glob("*.vtk"))

        if not vtk_files:
            logger.warning("Skipping evaluation for %s because no VTK files were found.", run_name)
            continue

        env = os.environ.copy()
        env["RUN_NAME"] = run_name
        env["RUN_VTK_PREFIX"] = run_name
        env["RUN_OUTPUT_DIR"] = str(vtk_dir)
        env["RUN_FEB_PATH"] = str(feb_path)
        existing_pythonpath = env.get("PYTHONPATH", "")
        extra_pythonpath = str(root / "scripts")
        env["PYTHONPATH"] = (
            extra_pythonpath if not existing_pythonpath
            else os.pathsep.join([extra_pythonpath, existing_pythonpath])
        )
        try:
            subprocess.run([sys.executable, str(script_path)], cwd=root, env=env, check=True)
            completed.append(run_name)
        except subprocess.CalledProcessError:
            logger.exception("Evaluation failed for %s.", run_name)

    return tuple(completed)
