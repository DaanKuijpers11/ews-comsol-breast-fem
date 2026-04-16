import xml.etree.ElementTree as ET
from pathlib import Path

from ews_fem_pipeline.prepare_simulation.simulation_settings import (
    FEBElement,
    Constants,
    write_nodes_to_xml,
    write_elements_to_xml,
    write_xml
)

def write_to_feb(filepath: Path, mesh, settings):
    FEB_element = FEBElement()

    # =========================
    # ROOT (FEBio correct)
    # =========================
    root = ET.Element("febio_spec", version="4.0")

    # =========================
    # CORE STRUCTURE (ORDER MATTERS)
    # =========================

    # Module
    FEB_element.module.to_xml(parent=root)

    # Globals + constants
    globals_elem = FEB_element.globals.to_xml(parent=root)
    constants_elem = FEB_element.constants.to_xml(parent=globals_elem)
    Constants().to_xml(parent=constants_elem)

    # Materials
    material_elem = FEB_element.material.to_xml(parent=root)

    skin_elem = FEB_element.skin.to_xml(parent=material_elem)
    settings.material.skin.to_xml(parent=skin_elem, tumor=settings.material.tumor.skin)

    adipose_elem = FEB_element.adipose.to_xml(parent=material_elem)
    settings.material.adipose.to_xml(parent=adipose_elem, tumor=settings.material.tumor.adipose)

    glandular_elem = FEB_element.glandular.to_xml(parent=material_elem)
    settings.material.glandular.to_xml(parent=glandular_elem, tumor=settings.material.tumor.glandular)

    # =========================
    # MESH (FIXED STRUCTURE)
    # =========================
    mesh_elem = FEB_element.mesh.to_xml(parent=root)

    write_nodes_to_xml(parent=mesh_elem, mesh=mesh)
    write_elements_to_xml(parent=mesh_elem, mesh=mesh)

    FEB_element.mass_damping.to_xml(parent=mesh_elem)
    FEB_element.gravitational_acceleration.to_xml(parent=mesh_elem)

    # =========================
    # MESH DOMAINS
    # =========================
    mesh_domains_elem = FEB_element.mesh_domains.to_xml(parent=root)

    shell_elem = FEB_element.shell_domain.to_xml(parent=mesh_domains_elem)
    FEB_element.shell_thickness.to_xml(parent=shell_elem)

    FEB_element.solid_domain_glandular.to_xml(parent=mesh_domains_elem)
    FEB_element.solid_domain_adipose.to_xml(parent=mesh_domains_elem)

    # =========================
    # LOADS
    # =========================
    loads_elem = FEB_element.loads.to_xml(parent=root)

    body_load_elem = FEB_element.body_load1.to_xml(parent=loads_elem)
    FEB_element.force.to_xml(parent=body_load_elem)

    # =========================
    # STEPS
    # =========================
    step_elem = FEB_element.step.to_xml(parent=root)

    step1_elem = FEB_element.step1.to_xml(parent=step_elem)
    control1_elem = FEB_element.control.to_xml(parent=step1_elem)
    settings.simulation.control_step1.to_xml(parent=control1_elem)

    timestepper1_elem = FEB_element.time_stepper.to_xml(parent=control1_elem)
    settings.simulation.timestepper_step1.to_xml(parent=timestepper1_elem)

    solver1_elem = FEB_element.solver.to_xml(parent=control1_elem)
    settings.simulation.solver_step1.to_xml(parent=solver1_elem)

    qn1 = FEB_element.qn_method.to_xml(parent=solver1_elem)
    settings.simulation.qnmethod_step1.to_xml(parent=qn1)

    boundary1_elem = FEB_element.boundary.to_xml(parent=step1_elem)
    bc1 = FEB_element.boundary_zero_displacement.to_xml(parent=boundary1_elem)
    settings.simulation.zero_displacement.to_xml(parent=bc1)

    # STEP 2
    step2_elem = FEB_element.step2.to_xml(parent=step_elem)

    control2_elem = FEB_element.control.to_xml(parent=step2_elem)
    settings.simulation.control_step2.to_xml(parent=control2_elem)

    timestepper2_elem = FEB_element.time_stepper.to_xml(parent=control2_elem)
    settings.simulation.timestepper_step2.to_xml(parent=timestepper2_elem)

    solver2_elem = FEB_element.solver.to_xml(parent=control2_elem)
    settings.simulation.solver_step2.to_xml(parent=solver2_elem)

    qn2 = FEB_element.qn_method.to_xml(parent=solver2_elem)
    settings.simulation.qnmethod_step2.to_xml(parent=qn2)

    boundary2_elem = FEB_element.boundary.to_xml(parent=step2_elem)

    bc2a = FEB_element.boundary_parabolic_trajectory.to_xml(parent=boundary2_elem)
    settings.simulation.prescribed_displacement.to_xml(parent=bc2a)

    bc2b = FEB_element.boundary_only_z_displacement.to_xml(parent=boundary2_elem)
    settings.simulation.only_z_displacement.to_xml(parent=bc2b)

    loads2_elem = FEB_element.loads.to_xml(parent=step2_elem)
    bodyload2_elem = FEB_element.body_load2.to_xml(parent=loads2_elem)
    settings.simulation.loads.to_xml(parent=bodyload2_elem)

    # =========================
    # LOAD DATA
    # =========================
    loaddata_elem = FEB_element.load_data.to_xml(parent=root)

    settings.simulation.gravity.to_xml(parent=loaddata_elem)
    settings.simulation.parabolic_jump.to_xml(parent=loaddata_elem)
    settings.simulation.animation.to_xml(parent=loaddata_elem)

    # =========================
    # OUTPUT
    # =========================
    output_elem = FEB_element.output.to_xml(parent=root)
    settings.simulation.output.to_xml(parent=output_elem, filepath=filepath)

    # =========================
    # WRITE FILE
    # =========================
    write_xml(root=root, filepath=filepath)