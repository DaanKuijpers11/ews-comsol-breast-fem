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

    occ = gmsh.model.occ
    mesh = gmsh.model.mesh

    #################################################
    # 1. 2D PROFILE
    #################################################

    r = settings.model.geometry.radius
    lc = settings.model.mesh.ls

    p1 = occ.addPoint(0, 0, 0, lc)
    p2 = occ.addPoint(0, r, 0, lc)
    p3 = occ.addPoint(0, 0, r, lc)

    l1 = occ.addLine(p1, p2)
    l2 = occ.addCircleArc(p2, p1, p3)
    l3 = occ.addLine(p3, p1)

    loop = occ.addCurveLoop([l1, l2, l3])
    surf = occ.addPlaneSurface([loop])

    occ.synchronize()

    #################################################
    # 2. REVOLVE (SAFE)
    #################################################

    out = occ.revolve(
        [(2, surf)],
        0, 0, 0,
        0, 1, 0,
        2 * math.pi
    )

    occ.synchronize()

    #################################################
    # 3. MESH CONTROL
    #################################################

    curves = occ.getEntities(1)

    for _, ctag in curves:
        length = occ.getMass(1, ctag)
        n = max(2, int(settings.model.mesh.density * length))
        mesh.setTransfiniteCurve(ctag, n)

    #################################################
    # 4. MESH GENERATION
    #################################################

    mesh.generate(3)
    mesh.setOrder(settings.model.mesh.order)

    if settings.model.mesh.optimize:
        if settings.model.mesh.order > 1:
            mesh.optimize("HighOrder")
        else:
            mesh.optimize()

    #################################################
    # 5. NODES (NO REORDER ASSUMPTION)
    #################################################

    node_tags, node_coords, _ = mesh.getNodes()
    node_coords = node_coords.reshape(-1, 3)

    mesh_parts.nodes.tags = node_tags
    mesh_parts.nodes.coords = node_coords

    tag_to_index = {tag: i for i, tag in enumerate(node_tags)}

    #################################################
    # 6. ELEMENTS
    #################################################

    elem_types, elem_tags, elem_nodes = mesh.getElements(3)

    #################################################
    # 7. SAFE TISSUE INIT (IMPORTANT)
    #################################################

    tissues = mesh_parts.tissue_parts

    for name in tissues.model_fields:
        obj = getattr(tissues, name)
        obj.elements = np.array([], dtype=int)
        obj.nodes = np.array([], dtype=int)

    gland_elems = []
    gland_nodes = []

    fat_elems = []
    fat_nodes = []

    #################################################
    # 8. ELEMENT CLASSIFICATION (ROBUST)
    #################################################

    for tags, nodes, etype in zip(elem_tags, elem_nodes, elem_types):

        # aantal nodes per element type
        n_per_elem = gmsh.model.mesh.getElementProperties(etype)[3]

        nodes = np.asarray(nodes, dtype=int)

        # reshape → per element
        try:
            nodes = nodes.reshape(-1, n_per_elem)
        except ValueError:
            continue

        for e_tag, e_nodes in zip(tags, nodes):

            try:
                idx = [tag_to_index[tag] for tag in e_nodes]
            except KeyError:
                continue

            coords = mesh_parts.nodes.coords[idx]

            if coords.ndim != 2 or coords.shape[1] != 3:
                continue

            center = np.mean(coords, axis=0)

            if center[2] > 0:
                gland_elems.append(e_tag)
                gland_nodes.append(e_nodes)
            else:
                fat_elems.append(e_tag)
                fat_nodes.append(e_nodes)

    print("Gland elements:", len(gland_elems))
    print("Fat elements:", len(fat_elems))
    print("Gland nodes shape:", np.array(gland_nodes).shape)
    print("Fat nodes shape:", np.array(fat_nodes).shape)

    tissues.glandular.elements = np.array(gland_elems, dtype=int)
    tissues.glandular.nodes = np.array(gland_nodes, dtype=int)

    tissues.adipose.elements = np.array(fat_elems, dtype=int)
    tissues.adipose.nodes = np.array(fat_nodes, dtype=int)

    #################################################
    # 9. FINAL SAFETY (XML FIX PREVENTION)
    #################################################

    for name in tissues.model_fields:
        obj = getattr(tissues, name)

        if obj.elements is None:
            obj.elements = np.array([], dtype=int)

        if obj.nodes is None:
            obj.nodes = np.array([], dtype=int)

    #################################################
    # 10. DEBUG
    #################################################

    if settings.model.mesh.debug_view:
        gmsh.fltk.run()

    gmsh.finalize()

    return mesh_parts