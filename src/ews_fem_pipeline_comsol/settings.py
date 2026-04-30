from __future__ import annotations

import copy
import tomllib
from dataclasses import asdict, dataclass, field
from pathlib import Path
from typing import Any


@dataclass
class ComsolSettings:
    enabled: bool = True
    batch_executable: str | None = None
    comsol_executable: str | None = None
    configuration_dir: str | None = None
    mph_file: str | None = None
    study: str = "std1"
    execute: bool = True
    auto_build_from_java: bool = True
    java_compile_first: bool = False
    jdk_root: str | None = None
    extra_args: list[str] = field(default_factory=list)
    java_compile_timeout_s: int | None = 300
    java_build_timeout_s: int | None = 1800
    solve_timeout_s: int | None = 7200
    postprocess_timeout_s: int | None = 600
    enable_skin_shell_physics: bool = False
    enable_skin_solid_coupling_scaffold: bool = False
    skin_shell_thickness_m: float = 0.0001
    enable_curved_chestwall: bool = False
    chestwall_curve_depth_m: float = 0.0007
    compact_output: bool = False


@dataclass
class PipelineSettings:
    model_name: str = "breast_model_comsol"
    output_subdir: str = "output"


@dataclass
class SourceSettings:
    base_case_toml: str = ""
    reuse_febio_prepare: bool = True
    export_mesh_npz: bool = True
    export_mesh_csv: bool = True
    export_lobules_json: bool = True
    notes: str = ""


@dataclass
class Settings:
    pipeline: PipelineSettings = field(default_factory=PipelineSettings)
    comsol: ComsolSettings = field(default_factory=ComsolSettings)
    source: SourceSettings = field(default_factory=SourceSettings)


def default_settings() -> Settings:
    return Settings()


def _deep_merge(base: dict[str, Any], override: dict[str, Any]) -> dict[str, Any]:
    result = copy.deepcopy(base)
    for key, value in override.items():
        if key in result and isinstance(result[key], dict) and isinstance(value, dict):
            result[key] = _deep_merge(result[key], value)
        else:
            result[key] = value
    return result


def _to_dict(settings: Settings) -> dict[str, Any]:
    return asdict(settings)


def _from_dict(data: dict[str, Any]) -> Settings:
    pipeline = PipelineSettings(**data.get("pipeline", {}))
    comsol = ComsolSettings(**data.get("comsol", {}))
    source = SourceSettings(**data.get("source", {}))
    return Settings(pipeline=pipeline, comsol=comsol, source=source)


def load_settings_from_toml(filepath: Path) -> Settings:
    assert filepath.suffix == ".toml", "Input file must have .toml extension."
    with open(filepath, "rb") as handle:
        user_data = tomllib.load(handle)

    merged = _deep_merge(_to_dict(default_settings()), user_data)
    return _from_dict(merged)


def _format_toml_value(value: Any) -> str:
    if value is None:
        return '""'
    if isinstance(value, bool):
        return "true" if value else "false"
    if isinstance(value, int):
        return str(value)
    if isinstance(value, float):
        return repr(value)
    if isinstance(value, str):
        escaped = value.replace("\\", "\\\\").replace('"', '\\"')
        return f'"{escaped}"'
    if isinstance(value, list):
        return "[ " + ", ".join(_format_toml_value(item) for item in value) + " ]"
    raise TypeError(f"Unsupported TOML value type: {type(value)!r}")


def _write_dict(lines: list[str], prefix: str, data: dict[str, Any]) -> None:
    scalars: list[tuple[str, Any]] = []
    tables: list[tuple[str, dict[str, Any]]] = []
    for key, value in data.items():
        if value is None:
            continue
        if isinstance(value, dict):
            tables.append((key, value))
        else:
            scalars.append((key, value))

    if prefix:
        lines.append(f"[{prefix}]")
    for key, value in scalars:
        lines.append(f"{key} = {_format_toml_value(value)}")
    if scalars and tables:
        lines.append("")

    for index, (key, table_data) in enumerate(tables):
        nested_prefix = f"{prefix}.{key}" if prefix else key
        _write_dict(lines, nested_prefix, table_data)
        if index != len(tables) - 1:
            lines.append("")


def write_settings_to_toml(filepath: Path, settings: Settings) -> None:
    assert filepath.suffix == ".toml", "Output file must have .toml extension."
    filepath.parent.mkdir(parents=True, exist_ok=True)
    payload = _to_dict(settings)
    lines: list[str] = []
    _write_dict(lines, "", payload)
    filepath.write_text("\n".join(lines).rstrip() + "\n", encoding="utf-8")
