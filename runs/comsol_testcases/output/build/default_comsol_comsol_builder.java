import com.comsol.model.*;
import com.comsol.model.util.*;

public class default_comsol_comsol_builder {
  public static Model run() {
    ModelUtil.initStandalone(true);
    Model model = ModelUtil.create("Model");
    model.label("default_comsol_generated.mph");
    model.modelPath("C:/Users/20223231/ews_fem_clean/runs/comsol_testcases/output/build");

    model.param().set("breast_radius", "0.07[m]");
    model.param().set("chest_thickness", "0.002[m]");
    model.param().set("mesh_density_hint", "140.0");
    model.param().set("skin_density", "1100.0[kg/m^3]");
    model.param().set("adipose_density", "911.0[kg/m^3]");
    model.param().set("glandular_density", "911.0[kg/m^3]");
    model.param().set("g_acc", "9.81[m/s^2]");
    model.param().set("skin_E", "14352.159468438538[Pa]");
    model.param().set("skin_nu", "0.495016611296");
    model.param().set("adipose_E", "1289.565087852724[Pa]");
    model.param().set("adipose_nu", "0.499000000000");
    model.param().set("glandular_E", "2548.301132578281[Pa]");
    model.param().set("glandular_nu", "0.499000000000");

    // Base component/geometry
    model.component().create("comp1", true);
    model.component("comp1").geom().create("geom1", 3);
    model.component("comp1").mesh().create("mesh1");
    model.component("comp1").geom("geom1").lengthUnit("m");

    // Minimal study scaffold so COMSOL batch can target std1.
    model.study().create("std1");
    model.study("std1").create("stat", "Stationary");

    // Geometry scaffold matching the current FEBio-style baseline more closely.
    model.component("comp1").geom("geom1").create("sph_outer", "Sphere");
    model.component("comp1").geom("geom1").feature("sph_outer").set("r", "breast_radius");
    model.component("comp1").geom("geom1").feature("sph_outer").set("pos", "0 0 0");
    model.component("comp1").geom("geom1").feature("sph_outer").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("sph_outer").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("blk_half", "Block");
    model.component("comp1").geom("geom1").feature("blk_half").set("size", "2*breast_radius breast_radius 2*breast_radius");
    model.component("comp1").geom("geom1").feature("blk_half").set("pos", "-breast_radius 0 -breast_radius");
    model.component("comp1").geom("geom1").feature("blk_half").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("blk_half").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("breast_base", "Intersection");
    model.component("comp1").geom("geom1").feature("breast_base").selection("input").set("sph_outer", "blk_half");
    model.component("comp1").geom("geom1").feature("breast_base").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("breast_base").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("breast_base").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("breast_base").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("breast_base");
    String[] breastBaseObjs = model.component("comp1").geom("geom1").feature("breast_base").objectNames();

    model.component("comp1").geom("geom1").create("nipple_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("nipple_cap").set("semiaxes", "0.00630000 0.00525000 0.00350000");
    model.component("comp1").geom("geom1").feature("nipple_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("nipple_cap").set("pos", "0 0.07157500 0");
    model.component("comp1").geom("geom1").feature("nipple_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("nipple_cap").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("nipple_cap");
    String[] nippleCapObjs = model.component("comp1").geom("geom1").feature("nipple_cap").objectNames();

    model.component("comp1").geom("geom1").create("breast_outer", "Union");
    model.component("comp1").geom("geom1").feature("breast_outer").selection("input").set(breastBaseObjs[0], nippleCapObjs[0]);
    model.component("comp1").geom("geom1").feature("breast_outer").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("breast_outer");
    String[] breastOuterObjs = model.component("comp1").geom("geom1").feature("breast_outer").objectNames();

    model.component("comp1").geom("geom1").create("chest_cyl", "Cylinder");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("r", "breast_radius");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("h", "chest_thickness");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("pos", "0 -chest_thickness 0");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("chest_cyl").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("chest_cyl");
    String[] chestObjs = model.component("comp1").geom("geom1").feature("chest_cyl").objectNames();

    model.component("comp1").geom("geom1").create("gland_keep_anterior", "Block");
    model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("size", "2*breast_radius 2*breast_radius 2*breast_radius");
    model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("pos", "-breast_radius 0 -breast_radius");
    model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_keep_anterior");
    String[] glandKeepAnteriorObjs = model.component("comp1").geom("geom1").feature("gland_keep_anterior").objectNames();

    model.component("comp1").geom("geom1").create("gland_seed", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("gland_seed").set("semiaxes", "0.02415000 0.02310000 0.07350000");
    model.component("comp1").geom("geom1").feature("gland_seed").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("gland_seed").set("pos", "0 0.00000000 0.00000000");
    model.component("comp1").geom("geom1").feature("gland_seed").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_seed").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_seed");
    String[] glandSeedObjs = model.component("comp1").geom("geom1").feature("gland_seed").objectNames();

    model.component("comp1").geom("geom1").create("gland_clip", "Intersection");
    model.component("comp1").geom("geom1").feature("gland_clip").selection("input").set(glandSeedObjs[0], breastOuterObjs[0], glandKeepAnteriorObjs[0]);
    model.component("comp1").geom("geom1").feature("gland_clip").set("keep", "on");
    model.component("comp1").geom("geom1").feature("gland_clip").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("gland_clip").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("gland_clip").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_clip").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_clip");
    String[] glandClipObjs = model.component("comp1").geom("geom1").feature("gland_clip").objectNames();

    model.component("comp1").geom("geom1").create("adipose_diff", "Difference");
    model.component("comp1").geom("geom1").feature("adipose_diff").selection("input").set(breastOuterObjs);
    model.component("comp1").geom("geom1").feature("adipose_diff").selection("input2").set(glandClipObjs);
    model.component("comp1").geom("geom1").feature("adipose_diff").set("keepsubtract", "on");
    model.component("comp1").geom("geom1").feature("adipose_diff").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("adipose_diff").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("adipose_diff").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("adipose_diff").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("adipose_diff");
    String[] adiposeObjs = model.component("comp1").geom("geom1").feature("adipose_diff").objectNames();

    model.component("comp1").geom("geom1").create("breast_union", "Union");
    String[] unionInput = new String[] { adiposeObjs[0], glandClipObjs[0], chestObjs[0] };
    model.component("comp1").geom("geom1").feature("breast_union").selection("input").set(unionInput);
    model.component("comp1").geom("geom1").feature("breast_union").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("breast_union").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("breast_union").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("breast_union").set("selresultshow", "all");

    // Auto-generated pointers:
    // - Build plan JSON: C:/Users/20223231/ews_fem_clean/runs/comsol_testcases/output/prepare/default_comsol_comsol_build_plan.json
    // - Selection hints JSON: C:/Users/20223231/ews_fem_clean/runs/comsol_testcases/output/build/default_comsol_comsol_selection_hints.json
    // - Lobules in plan: 36
    //
    // Source geometry summary:
    // - radius: 0.07
    // - chest-wall thickness: 0.002
    // - asymmetry enabled: False
    //
    // Source mesh summary:
    // - density: 140.0
    // - order: 2
    //
    // Source material summary:
    // - skin density: 1100.0
    // - adipose density: 911.0
    // - glandular density: 911.0
    //
    // Physics scaffold:
    model.component("comp1").material().create("mat_chest", "Common");
    model.component("comp1").material("mat_chest").label("ChestWall");
    model.component("comp1").material("mat_chest").selection().named("geom1_chest_cyl_dom");
    model.component("comp1").material("mat_chest").propertyGroup("def").set("density", new String[] { "skin_density" });
    model.component("comp1").material("mat_chest").propertyGroup("def").set("youngsmodulus", new String[] { "skin_E" });
    model.component("comp1").material("mat_chest").propertyGroup("def").set("poissonsratio", new String[] { "skin_nu" });

    model.component("comp1").material().create("mat_adipose", "Common");
    model.component("comp1").material("mat_adipose").label("Adipose");
    model.component("comp1").material("mat_adipose").selection().named("geom1_adipose_diff_dom");
    model.component("comp1").material("mat_adipose").propertyGroup("def").set("density", new String[] { "adipose_density" });
    model.component("comp1").material("mat_adipose").propertyGroup("def").set("youngsmodulus", new String[] { "adipose_E" });
    model.component("comp1").material("mat_adipose").propertyGroup("def").set("poissonsratio", new String[] { "adipose_nu" });

    model.component("comp1").material().create("mat_glandular", "Common");
    model.component("comp1").material("mat_glandular").label("Glandular");
    model.component("comp1").material("mat_glandular").selection().named("geom1_gland_clip_dom");
    model.component("comp1").material("mat_glandular").propertyGroup("def").set("density", new String[] { "glandular_density" });
    model.component("comp1").material("mat_glandular").propertyGroup("def").set("youngsmodulus", new String[] { "glandular_E" });
    model.component("comp1").material("mat_glandular").propertyGroup("def").set("poissonsratio", new String[] { "glandular_nu" });

    model.component("comp1").physics().create("solid", "SolidMechanics", "geom1");
    model.component("comp1").physics("solid").selection().named("geom1_breast_union_dom");
    model.component("comp1").physics("solid").create("fix1", "Fixed", 2);
    model.component("comp1").physics("solid").feature("fix1").selection().named("geom1_chest_cyl_bnd");
    model.component("comp1").physics("solid").create("gacc1", "GravityAcceleration", -1);
    model.component("comp1").physics("solid").feature("gacc1").set("g", new String[] { "0", "0", "-g_acc" });

    // Current builder scope:
    // 1) build a COMSOL-native outer breast, glandular core, and chest-wall support
    // 2) expose stable finalized geometry selections for the main regions
    // 3) attach initial linearized materials and solid mechanics
    // 4) run and save MPH
    //
    // Note:
    // This file is still a scaffold. It now creates real geometry, materials, and
    // a first solid mechanics solve, but it does not yet reproduce the full FEBio
    // hyperelastic material law or dynamic motion pipeline automatically.

    model.component("comp1").geom("geom1").run("breast_union");
    model.component("comp1").mesh("mesh1").run();
    return model;
  }

  public static void main(String[] args) throws Exception {
    Model model = run();
    model.save("C:/Users/20223231/ews_fem_clean/runs/comsol_testcases/output/build/default_comsol_generated.mph");
    ModelUtil.disconnect();
  }
}
