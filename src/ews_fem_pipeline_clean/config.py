from pathlib import Path


def default_settings():
    from ews_fem_pipeline_clean.prepare_simulation import Settings

    return Settings()


def load_settings(filepath: Path):
    from ews_fem_pipeline_clean.prepare_simulation import load_settings_from_toml

    return load_settings_from_toml(filepath=filepath)


def write_settings(filepath: Path, settings) -> None:
    from ews_fem_pipeline_clean.prepare_simulation import write_settings_to_toml

    write_settings_to_toml(filepath=filepath, settings=settings)
