"""Backward-compatible aliases for older COMSOL TOMLs/scripts.

New code should import from ``prepare_source_case``. This module is kept so
older local commands that referenced ``prepare_from_febio`` do not break.
"""

from ews_fem_pipeline_comsol.prepare_source_case import (
    prepare_source_case,
    resolve_source_case_toml,
)

prepare_case_from_febio = prepare_source_case
