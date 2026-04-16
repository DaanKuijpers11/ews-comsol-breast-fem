
class BreastGeometry:
    def __init__(self, radius, thickness_chest_wall):
        self.radius = radius
        self.thickness_chest_wall = thickness_chest_wall

def create_geometry(settings):
    print("Creating geometry...")

    geom = settings["model"]["geometry"]

    geometry = BreastGeometry(
        radius=geom["radius"],
        thickness_chest_wall=geom["thickness_chest_wall"],
    )

    return geometry