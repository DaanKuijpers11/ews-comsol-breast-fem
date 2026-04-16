import argparse
from pathlib import Path

from ews_fem_clean.pipeline import run_pipeline
from ews_fem_clean.io.config_loader import load_config
from ews_fem_clean.geometry.breast_geometry import create_geometry
from ews_fem_clean.meshing.mesh_generator import generate_mesh

def main():
    parser = argparse.ArgumentParser(description="EWS FEM Clean Pipeline")

    parser.add_argument(
        "command",
        choices=["geometry", "mesh", "run"],
        help="Which step to run"
    )

    parser.add_argument(
        "config",
        type=Path,
        help="Path to config.toml"
    )

    args = parser.parse_args()

    settings = load_config(args.config)

    if args.command == "geometry":
        create_geometry(settings)

    elif args.command == "mesh":
        geometry = create_geometry(settings)
        generate_mesh(geometry, settings)

    elif args.command == "run":
        run_pipeline(args.config)


if __name__ == "__main__":
    main()