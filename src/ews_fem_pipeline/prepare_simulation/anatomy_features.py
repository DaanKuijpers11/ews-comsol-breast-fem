import gmsh
import numpy as np
import logging

logger = logging.getLogger(__name__)

########################################
# Function for adding anatomy features #
########################################

def add_anatomy_features(settings, mesh_parts):
    """
    Adds anatomical structures on top of an existing gmsh model.
    Does NOT modify base geometry construction logic.
    """

    # Glandular field model (no gmsh geometry)
    if getattr(settings.material.glandular.hetero, "enabled", True):
        inject_glandular_field(settings)

    if getattr(settings.material.glandular.hetero, "enabled", False):
        add_gland_lobules(settings)

    if getattr(settings.material, "ligaments", None):
        add_cooper_ligaments(settings)

    return mesh_parts

##########################################
# Glandular lobules part (heterogeneity) #
##########################################


def inject_glandular_field(settings):
    """
    Builds Chen-style anatomical glandular field
    (replaces geometric lobules)
    """

    gland = settings.material.glandular
    hetero = gland.hetero

    if not hetero.enabled:
        return

    # store parameters for FEBio field construction
    gland._anatomy_model = {
        "nipple": hetero.nipple,
        "base_rho": gland.density,
        "gland_strength": hetero.gland_strength,
        "n_lobes": hetero.n_lobes,
        "lobe_anisotropy": hetero.lobe_anisotropy,
        "radial_decay_length": hetero.radial_decay_length,
        "depth_decay_length": hetero.depth_decay_length,
    }


###########################
# Cooper's ligaments part #
###########################

def add_cooper_ligaments(settings):
    radius = settings.model.geometry.radius

    n_fibers = 30

    for i in range(n_fibers):
        theta = 2 * np.pi * i / n_fibers

        x0, y0, z0 = 0, 0, radius
        x1 = 0
        y1 = radius * np.cos(theta)
        z1 = radius * np.sin(theta)

        gmsh.model.occ.addLine((i+1)*100, x0, y0, z0, x1, y1, z1)

    gmsh.model.occ.synchronize()

########################    
# Asymmetry field part #
########################     

def add_asymmetry_field(settings):
    asym = settings.model.geometry.asymmetry

    if not asym.enabled:
        return

    gmsh.model.mesh.field.add("MathEval", 1)
    gmsh.model.mesh.field.setString(1, "F", f"x + {asym.scale_y}*y + {asym.scale_z}*z")

    gmsh.model.mesh.field.setAsBackgroundMesh(1)