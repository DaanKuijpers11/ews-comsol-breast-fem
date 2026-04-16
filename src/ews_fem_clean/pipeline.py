from pathlib import Path

from ews_fem_clean.io.config_loader import load_config
from ews_fem_clean.geometry.breast_geometry import create_geometry
from ews_fem_clean.meshing.mesh_generator import generate_mesh
from ews_fem_clean.fem.feb_writer import write_feb
from ews_fem_clean.fem.model_builder import build_feb_model
from ews_fem_clean.fem.runner import run_febio


def run_pipeline(config_path: Path):
    print(">>> FULL PIPELINE")

    settings = load_config(config_path)

    geometry = create_geometry(settings)
    
    mesh = generate_mesh(geometry, settings)

    model = build_feb_model(settings, mesh)
    
    feb_file = write_feb(model, output_name=config_path.stem)

    run_febio(feb_file)