import math
import numpy as np
import gmsh
import logging
from pathlib import Path

logger = logging.getLogger(__name__)


class MeshParts:
    def __init__(self):
        self.nodes = type("Nodes", (), {})()
        self.elements = {}

        # Placeholder tissue structure (later uitbreiden)
        self.tissue_parts = type("Tissues", (), {})()
        self.tissue_parts.skin = type("Part", (), {})()
        self.tissue_parts.adipose = type("Part", (), {})()
        self.tissue_parts.glandular = type("Part", (), {})()
        self.tissue_parts.chest = type("Part", (), {})()


def generate_mesh(geometry, settings) -> MeshParts:
    """
    Generate a clean, stable tetrahedral mesh using GMSH.

    geometry: BreastGeometry object
    settings: dict (from TOML)
    """

    print("\nStarting mesh generation...")
    logger.info("Initializing GMSH")

    mesh_parts = MeshParts()

    gmsh.initialize()
    gmsh.model.add("breast")

    gmsh.option.setNumber("Mesh.SaveAll", 1)
    gmsh.option.setNumber("General.Verbosity", 2)

    occ = gmsh.model.occ
    mesh = gmsh.model.mesh

    # =========================
    # PARAMETERS
    # =========================
    r = geometry.radius
    lc = settings["model"]["mesh"]["ls"]

    print(f"Radius: {r}")
    print(f"Mesh size (lc): {lc}")

    # =========================
    # 1. 2D QUARTER PROFILE
    # =========================
    p1 = occ.addPoint(0, 0, 0, lc)
    p2 = occ.addPoint(0, r, 0, lc)
    p3 = occ.addPoint(0, 0, r, lc)

    l1 = occ.addLine(p1, p2)
    l2 = occ.addCircleArc(p2, p1, p3)
    l3 = occ.addLine(p3, p1)

    loop = occ.addCurveLoop([l1, l2, l3])
    surf = occ.addPlaneSurface([loop])

    occ.synchronize()

    # =========================
    # 2. REVOLVE → 3D VOLUME
    # =========================
    occ.revolve([(2, surf)], 0, 0, 0, 0, 1, 0, 2 * math.pi)
    occ.synchronize()

    # =========================
    # 3. MESH GENERATION
    # =========================
    mesh.generate(3)

    node_tags, node_coords, _ = gmsh.model.mesh.getNodes()
    node_coords = np.array(node_coords).reshape(-1, 3)

    # Sort nodes (VERY IMPORTANT for FEBio)
    sorted_idx = np.argsort(node_tags)

    mesh_parts.nodes.tags = np.array(node_tags)[sorted_idx]
    mesh_parts.nodes.coords = node_coords[sorted_idx]

    print(f"Number of nodes: {len(node_tags)}")

    # =========================
    # 4. ELEMENT EXTRACTION
    # =========================
    try:
        elem_types, elem_tags, elem_nodes = mesh.getElements(3)

        mesh_parts.elements = {
            "types": elem_types,
            "tags": elem_tags,
            "connectivity": elem_nodes
        }

    except Exception as e:
        logger.warning(f"Element extraction failed: {e}")
        mesh_parts.elements = {}

    # =========================
    # 5. QUALITY CHECK
    # =========================
    logger.info("MESH QUALITY CHECK")

    try:
        elem_types, elem_tags, _ = mesh.getElements(3)
        jac, _, _ = gmsh.model.mesh.getJacobians(elem_types[0], elem_tags[0])

        min_jac = float(np.min(jac))
        logger.info(f"Min Jacobian: {min_jac}")

        if min_jac < -0.1:
            raise ValueError(f"Invalid mesh: strong inversion ({min_jac})")
        elif min_jac < 0:
            logger.warning(f"Minor inversion detected: {min_jac}")

    except Exception as e:
        logger.warning(f"Quality check failed: {e}")

    # =========================
    # 6. EXPORT
    # =========================
    output_dir = Path("output")
    output_dir.mkdir(exist_ok=True)

    msh_path = output_dir / "test_mesh.msh"
    gmsh.write(str(msh_path))

    print(f"Mesh written to: {msh_path}")

    gmsh.finalize()

    # =========================
    # 7. SAFE EMPTY TISSUES
    # =========================
    mesh_parts.tissue_parts.skin.elements = np.array([])
    mesh_parts.tissue_parts.skin.nodes = np.array([])

    mesh_parts.tissue_parts.adipose.elements = np.array([])
    mesh_parts.tissue_parts.adipose.nodes = np.array([])

    mesh_parts.tissue_parts.glandular.elements = np.array([])
    mesh_parts.tissue_parts.glandular.nodes = np.array([])

    mesh_parts.tissue_parts.chest.elements = np.array([])
    mesh_parts.tissue_parts.chest.nodes = np.array([])

    return mesh_parts