from pathlib import Path


def write_feb(mesh, settings, output_name="model"):
    print("Writing FEB file...")

    filename = Path(f"{output_name}.feb")

    with open(filename, "w") as f:
        f.write("<febio_spec></febio_spec>")

    return filename