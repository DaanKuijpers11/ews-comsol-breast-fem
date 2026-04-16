import tomllib


def load_config(path):
    with open(path, "rb") as f:
        return tomllib.load(f)