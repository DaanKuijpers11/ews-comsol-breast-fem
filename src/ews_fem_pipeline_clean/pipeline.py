from pathlib import Path


def _resolve_jobs(input_files: tuple[Path, ...], jobs: int) -> int:
    if jobs != 0:
        return jobs
    return 1 if len(input_files) == 1 else 4


def generate_cases(input_files: tuple[Path, ...]) -> tuple[Path, ...]:
    from ews_fem_pipeline_clean.prepare_simulation import (
        generate_mesh,
        load_settings_from_toml,
        write_settings_to_toml,
        write_to_feb,
    )

    feb_files = []
    for filepath in input_files:
        settings = load_settings_from_toml(filepath=filepath)

        output_directory = filepath.parent / "output"
        output_directory.mkdir(parents=True, exist_ok=True)

        write_settings_to_toml(
            filepath=output_directory / f"{filepath.stem}_all_settings{filepath.suffix}",
            settings=settings,
        )

        mesh = generate_mesh(settings=settings)
        write_to_feb(filepath=filepath, mesh=mesh, settings=settings)
        feb_files.append(filepath.with_suffix(".feb"))

    return tuple(feb_files)


def run_febio_cases(input_files: tuple[Path, ...], jobs: int = 0) -> tuple[Path, ...]:
    from ews_fem_pipeline_clean.run_simulation import FEBioRunner

    for filepath in input_files:
        assert filepath.suffix == ".feb", f"The input file does not have the correct file extension. Must be .feb"

    return FEBioRunner().run(input_files, _resolve_jobs(input_files, jobs))


def convert_cases(input_files: tuple[Path, ...]) -> tuple[Path, ...]:
    from ews_fem_pipeline_clean.convert_simulation import feb_to_blender

    for filepath in input_files:
        feb_to_blender(filepath)

    return input_files


def run_full_pipeline(input_files: tuple[Path, ...], jobs: int = 0) -> tuple[Path, ...]:
    feb_files = generate_cases(input_files)
    output_files = run_febio_cases(feb_files, jobs=jobs)
    convert_cases(output_files)
    return output_files


def evaluate_cases(input_files: tuple[Path, ...]) -> tuple[str, ...]:
    from ews_fem_pipeline_clean.evaluation import evaluate_runs

    return evaluate_runs(input_files)


def sweep_cases(input_files: tuple[Path, ...], jobs: int = 0, evaluate: bool = False) -> tuple[Path, ...]:
    """
    Run multiple model configurations as one batch.
    """
    output_files = run_full_pipeline(input_files, jobs=jobs)
    if evaluate:
        evaluate_cases(input_files)
    return output_files
