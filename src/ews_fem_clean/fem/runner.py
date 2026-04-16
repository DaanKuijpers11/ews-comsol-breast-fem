import subprocess


def run_febio(feb_file):
    print(f"Running FEBio on {feb_file}")

    subprocess.run(["febio4", str(feb_file)])