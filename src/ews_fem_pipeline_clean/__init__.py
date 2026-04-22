import tomllib
import logging
import logging.config
from pathlib import Path


# Setup for logging
class MaxLevelFilter(logging.Filter):
    def __init__(self, level):
        self.maximum_level = getattr(logging, level)

    def filter(self, record):
        return record.levelno <= self.maximum_level


def setup_logging() -> None:
    """
    Configure package logging with a safe fallback when the file handler
    cannot be opened, for example because a log file is locked by another run.
    """
    config_path = Path(__file__).parent / "logging_config.toml"
    with open(config_path, "rb") as f:
        config = tomllib.load(f)

    try:
        logging.config.dictConfig(config)
    except Exception:
        fallback = dict(config)
        fallback["handlers"] = dict(config["handlers"])
        fallback["root"] = dict(config["root"])
        fallback["root"]["handlers"] = ["stdout", "stderr"]
        fallback["handlers"].pop("file", None)
        logging.config.dictConfig(fallback)
        logging.getLogger(__name__).warning(
            "Falling back to console logging because file logging could not be configured."
        )


setup_logging()
