from dataclasses import dataclass, field


# =========================
# FEBIO INTERNAL MODEL
# =========================

@dataclass
class FEBMaterial:
    id: int
    name: str
    type: str
    params: dict


@dataclass
class FEBMesh:
    nodes: dict
    elements: dict


@dataclass
class FEBModel:
    materials: list[FEBMaterial] = field(default_factory=list)
    mesh: FEBMesh = None


# =========================
# MATERIAL TRANSLATOR
# =========================

def translate_material(mat_dict, mat_id, name):

    mat_type = mat_dict.get("type", "Mooney-Rivlin")

    if mat_type == "Mooney-Rivlin":
        return FEBMaterial(
            id=mat_id,
            name=name,
            type="Mooney-Rivlin",
            params={
                "k": mat_dict["k"],
                "pressure_model": mat_dict.get("pressure_model", "default"),
                "density": mat_dict["density"],
                "c1": mat_dict["c1"],
                "c2": mat_dict["c2"],
            }
        )

    raise ValueError(f"Unsupported material type: {mat_type}")


# =========================
# MODEL BUILDER
# =========================

def build_feb_model(settings, mesh):
    """
    Converts:
    settings + mesh → FEBModel
    """

    model = FEBModel()

    # -------------------------
    # MATERIALS
    # -------------------------
    mat_id = 1

    for name, mat in settings["model"]["material"].items():
        model.materials.append(
            translate_material(mat, mat_id, name)
        )
        mat_id += 1

    # -------------------------
    # MESH
    # -------------------------
    model.mesh = FEBMesh(
        nodes={
            "tags": mesh.nodes.tags,
            "coords": mesh.nodes.coords
        },
        elements=mesh.elements
    )

    return model