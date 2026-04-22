import gmsh
import numpy as np
import logging

logger = logging.getLogger(__name__)

def add_anatomy_features(settings, mesh_parts):
    """
    Adds anatomical structures on top of an existing gmsh model.
    Does NOT modify base geometry construction logic.
    """

    if settings.model.geometry.asymmetry.enabled:
        add_asymmetry_field(settings)

    if getattr(settings.material.glandular.hetero, "enabled", False):
        add_gland_lobules(settings)

    if getattr(settings.material, "ligaments", None):
        add_cooper_ligaments(settings)

    return mesh_parts

def add_gland_lobules(settings):
    lobules = settings.material.glandular.hetero.lobules

    lc = settings.model.mesh.ls

    for i, l in enumerate(lobules):
        cx, cy, cz = l.center
        r = l.width

        tag = 1000 + i
        gmsh.model.occ.addSphere(cx, cy, cz, r, tag)

    gmsh.model.occ.synchronize()

    # IMPORTANT: prevent size collapse
    gmsh.model.mesh.setSize(gmsh.model.getEntities(0), lc)

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

def add_asymmetry_field(settings):
    asym = settings.model.geometry.asymmetry

    if not asym.enabled:
        return

    gmsh.model.mesh.field.add("MathEval", 1)
    gmsh.model.mesh.field.setString(1, "F", f"x + {asym.scale_y}*y + {asym.scale_z}*z")

    gmsh.model.mesh.field.setAsBackgroundMesh(1)