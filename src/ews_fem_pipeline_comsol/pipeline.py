from __future__ import annotations

import json
from pathlib import Path

from ews_fem_pipeline_comsol.paths import ensure_output_tree
from ews_fem_pipeline_comsol.prepare_from_febio import prepare_case_from_febio
from ews_fem_pipeline_comsol.script_builder import generate_comsol_java_builder
from ews_fem_pipeline_comsol.settings import (
    Settings,
    load_settings_from_toml,
    write_settings_to_toml,
)


def generate_cases(input_files: tuple[Path, ...]) -> tuple[Path, ...]:
    generated: list[Path] = []
    for filepath in input_files:
        settings = load_settings_from_toml(filepath)
        output_paths = ensure_output_tree(filepath.parent, settings)
        output_root = output_paths["root"]
        prepare_dir = output_paths["prepare"]
        build_dir = output_paths["build"]

        write_settings_to_toml(output_root / f"{filepath.stem}_all_settings.toml", settings)
        prepare_artefacts = prepare_case_from_febio(
            case_name=filepath.stem,
            comsol_case_toml=filepath,
            output_dir=prepare_dir,
            settings=settings,
        )
        script_artefacts = generate_comsol_java_builder(
            case_name=filepath.stem,
            output_dir=build_dir,
            prepare_artefacts=prepare_artefacts,
        )
        prepare_artefacts.update(script_artefacts)

        payload = {
            "case_name": filepath.stem,
            "case_dir": str(filepath.parent.resolve()),
            "settings_file": str(filepath.resolve()),
            "model_name": settings.pipeline.model_name,
            "prepare_artefacts": prepare_artefacts,
            "output_layout": {name: str(path.resolve()) for name, path in output_paths.items()},
        }
        json_file = build_dir / f"{filepath.stem}_comsol_input.json"
        json_file.write_text(json.dumps(payload, indent=2), encoding="utf-8")
        manifest_file = output_root / f"{filepath.stem}_output_manifest.json"
        manifest_file.write_text(json.dumps(payload, indent=2), encoding="utf-8")
        generated.append(json_file)

    return tuple(generated)


def solve_cases(input_files: tuple[Path, ...], settings_map: dict[Path, Settings] | None = None) -> tuple[Path, ...]:
    from ews_fem_pipeline_comsol.run_simulation import COMSOLRunner

    for filepath in input_files:
        assert filepath.suffix == ".json", "Input file must be a generated COMSOL JSON input."

    if settings_map is None:
        settings_map = {}
        for filepath in input_files:
            payload = json.loads(filepath.read_text(encoding="utf-8"))
            source_toml = Path(payload["settings_file"])
            settings_map[filepath] = load_settings_from_toml(source_toml)

    return COMSOLRunner().run(input_files, settings_map=settings_map)


def run_full_pipeline(input_files: tuple[Path, ...]) -> tuple[Path, ...]:
    generated = generate_cases(input_files)
    settings_map: dict[Path, Settings] = {}
    for source_toml, generated_json in zip(input_files, generated):
        settings_map[generated_json] = load_settings_from_toml(source_toml)
    return solve_cases(generated, settings_map=settings_map)


def sweep_cases(input_files: tuple[Path, ...]) -> tuple[Path, ...]:
    return run_full_pipeline(input_files)


def check_license(settings_file: Path) -> bool:
    from ews_fem_pipeline_comsol.run_simulation import COMSOLRunner

    settings = load_settings_from_toml(settings_file)
    ok, message = COMSOLRunner().check_license(settings=settings, workdir=settings_file.parent.resolve())
    print(message)
    return ok
