from ews_fem_pipeline_clean.prepare_simulation.model_settings import MeshParts, MeshSettings, GeometrySettings
from ews_fem_pipeline_clean.prepare_simulation.simulation_settings import Settings, FEBElement, Constants, \
    BoundaryCondition, Loads, write_nodes_to_xml, write_elements_to_xml, write_xml
from ews_fem_pipeline_clean.prepare_simulation.toml_settings import load_settings_from_toml, write_settings_to_toml


def generate_mesh(*args, **kwargs):
    from ews_fem_pipeline_clean.prepare_simulation.generate_mesh import generate_mesh as _generate_mesh

    return _generate_mesh(*args, **kwargs)


def write_to_feb(*args, **kwargs):
    from ews_fem_pipeline_clean.prepare_simulation.write_to_feb import write_to_feb as _write_to_feb

    return _write_to_feb(*args, **kwargs)
