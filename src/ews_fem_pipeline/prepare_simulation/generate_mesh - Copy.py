import math
import numpy as np
import logging
import gmsh

from ews_fem_pipeline.prepare_simulation import MeshParts, Settings

logger = logging.getLogger(__name__)


def generate_mesh(settings: Settings):

    mesh_parts = MeshParts()

    gmsh.initialize()
    gmsh.model.add("breast")

    gmsh.option.setNumber("Mesh.SaveAll", 1)
    gmsh.option.setNumber("General.Verbosity", 0)

    dim0, dim1, dim2, dim3 = 0, 1, 2, 3

    build = gmsh.model.occ
    mesh = gmsh.model.mesh

    #################################
    # 1. GEOMETRY (UNCHANGED)
    #################################

    p1 = build.addPoint(0, 0, 0, settings.model.mesh.ls, 1)
    p2 = build.addPoint(0, settings.model.geometry.radius, 0, settings.model.mesh.ls, 2)
    p3 = build.addPoint(0, 0, settings.model.geometry.radius, settings.model.mesh.ls, 3)

    l1 = build.addLine(p1, p2, 1)
    l2 = build.addCircleArc(p2, p1, p3, 2)
    l3 = build.addLine(p3, p1, 3)

    loop1 = build.addCurveLoop([l1, l2, l3], 1)
    s1 = build.addPlaneSurface([loop1], 1)

    p4 = build.addPoint(0, -settings.model.geometry.left_position_ellipse, 0, settings.model.mesh.ls, 4)
    p5 = build.addPoint(0, settings.model.geometry.radius + settings.model.geometry.position_nipple, 0, settings.model.mesh.ls, 5)
    p6 = build.addPoint(
        0,
        (settings.model.geometry.radius + settings.model.geometry.position_nipple
         - settings.model.geometry.left_position_ellipse) / 2,
        -settings.model.geometry.position_center_ellipse,
        settings.model.mesh.ls,
        6
    )

    l4 = build.addEllipseArc(p4, p6, p5, p5, 4)
    l5 = build.addLine(p4, p5, 5)

    loop2 = build.addCurveLoop([l4, l5], 2)
    s2 = build.addPlaneSurface([loop2], 2)

    p7 = build.addPoint(
        0,
        -settings.model.geometry.thickness_chest_wall,
        settings.model.geometry.radius,
        settings.model.mesh.ls,
        7
    )
    p8 = build.addPoint(
        0,
        -settings.model.geometry.thickness_chest_wall,
        0,
        settings.model.mesh.ls,
        8
    )

    l6 = build.addLine(p3, p7, 6)
    l7 = build.addLine(p7, p8, 7)
    l8 = build.addLine(p8, p1, 8)

    loop3 = build.addCurveLoop([l8, l3, l6, l7])
    s3 = build.addPlaneSurface([loop3])

    #################################
    # FRAGMENTATION
    #################################

    build.fragment([(dim2, s1), (dim2, s2)], [(dim2, s3)])
    build.synchronize()

    #################################
    # REVOLVE TO 3D
    #################################

    all_lines = build.getEntities(dim1)
    build.revolve(all_lines, 0, 0, 0, 0, 1, 0, 2 * math.pi)

    build.synchronize()

    tissues = mesh_parts.tissue_parts

    #################################
    # STABLE TISSUE LABELING (FIX)
    #################################

    def safe_assign(tissue, tags):
        if tags is None or len(tags) == 0:
            tissue.tags = []
            tissue.elements = []
            tissue.nodes = np.empty((0, 0))
            return
        tissue.tags = tags

    surfaces = build.getEntities(dim2)

    skin_tags = []
    gland_tags = []
    chest_tags = []

    # Geometry-based classification (robust replacement for IDs)
    for s in surfaces:
        com = build.getCenterOfMass(2, s[1])
        x, y, z = com

        # Heuristic but stable grouping
        if z > 0.6 * settings.model.geometry.radius:
            skin_tags.append(s[1])
        elif z > 0:
            gland_tags.append(s[1])
        else:
            chest_tags.append(s[1])

    safe_assign(tissues.skin, skin_tags)
    safe_assign(tissues.glandular, gland_tags)
    safe_assign(tissues.chest, chest_tags)

    #################################
    # MESH GENERATION
    #################################

    mesh.setOrder(settings.model.mesh.order)
    mesh.generate(dim3)

    if settings.model.mesh.optimize:
        if settings.model.mesh.order == 1:
            mesh.optimize()
        else:
            mesh.optimize("HighOrder")

    print("Element types:", mesh.getElements(dim3)[0])

    #################################
    # MESH QUALITY CHECK
    #################################

    try:
        elem_types, elem_tags, _ = mesh.getElements(dim3)

        qualities = []

        for etype, tags in zip(elem_types, elem_tags):
            if len(tags) == 0:
                continue
            q = gmsh.model.mesh.getElementQualities(tags, "minSJ")
            qualities.extend(q)

        qualities = np.array(qualities)

        if len(qualities) > 0:
            logger.info("Mesh quality check:")
            logger.info(f"  Elements: {len(qualities)}")
            logger.info(f"  Min SJ: {qualities.min():.4f}")
            logger.info(f"  Mean SJ: {qualities.mean():.4f}")
            logger.info(f"  Inverted: {np.sum(qualities < 0)}")

            if np.any(qualities < 0):
                logger.warning("Inverted elements detected (mesh may be unstable)")

    except Exception as e:
        logger.warning(f"Mesh quality check failed: {e}")

    #################################
    # NODE EXTRACTION
    #################################

    node_tags, node_coords = gmsh.model.mesh.getNodes(returnParametricCoord=False)[0:2]
    node_coords = np.reshape(node_coords, (-1, 3))

    sorted_ids = node_tags.argsort()

    mesh_parts.nodes.tags = node_tags[sorted_ids]
    mesh_parts.nodes.coords = node_coords[sorted_ids]

    gmsh.finalize()

    return mesh_parts