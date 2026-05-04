import com.comsol.model.*;
import com.comsol.model.util.*;

public class full_baseline_reference_simple_gland_static_baseline_comsol_builder {
  public static Model run() {
    ModelUtil.initStandalone(true);
    Model model = ModelUtil.create("Model");
    model.label("full_baseline_reference_simple_gland_static_baseline_generated.mph");
    model.modelPath("C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_baseline_reference_simple_gland_static_baseline/build");

    model.param().set("breast_radius", "0.07[m]");
    model.param().set("chest_thickness", "0.002[m]");
    model.param().set("skin_shell_thickness", "0.0001000000[m]");
    model.param().set("chest_curve_depth", "0.0007000000[m]");
    model.param().set("chest_curve_radius", "3.5003500000[m]");
    model.param().set("chest_curve_center_y", "-3.4996500000[m]");
    model.param().set("mesh_density_hint", "140.0");
    model.param().set("skin_density", "1100.0[kg/m^3]");
    model.param().set("adipose_density", "911.0[kg/m^3]");
    model.param().set("glandular_density", "911.0[kg/m^3]");
    model.param().set("chest_density", "1050.000000000000[kg/m^3]");
    model.param().set("g_acc", "9.81[m/s^2]");
    model.param().set("skin_bulk_modulus", "480000.000000000000[Pa]");
    model.param().set("skin_c10", "1200.000000000000[Pa]");
    model.param().set("skin_c01", "1200.000000000000[Pa]");
    model.param().set("adipose_bulk_modulus", "425000.000000000000[Pa]");
    model.param().set("adipose_c10", "109.000000000000[Pa]");
    model.param().set("adipose_c01", "106.000000000000[Pa]");
    model.param().set("glandular_bulk_modulus", "425000.000000000000[Pa]");
    model.param().set("glandular_c10", "230.000000000000[Pa]");
    model.param().set("glandular_c01", "195.000000000000[Pa]");
    model.param().set("skin_E", "14352.159468438538[Pa]");
    model.param().set("skin_nu", "0.495016611296");
    model.param().set("adipose_E", "1289.565087852724[Pa]");
    model.param().set("adipose_nu", "0.499000000000");
    model.param().set("glandular_E", "2548.301132578281[Pa]");
    model.param().set("glandular_nu", "0.499000000000");
    model.param().set("chest_E", "10000.000000000000[Pa]");
    model.param().set("chest_nu", "0.490000000000");

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

    model.component("comp1").geom("geom1").create("nipple_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("nipple_cap").set("semiaxes", "0.00882000 0.00882000 0.00350000");
    model.component("comp1").geom("geom1").feature("nipple_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("nipple_cap").set("pos", "0 0.07182000 0");
    model.component("comp1").geom("geom1").feature("nipple_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("nipple_cap").set("selresultshow", "all");

    String[] breastBaseObjs;
    if (false) {
      model.component("comp1").geom("geom1").create("thorax_keep_blk", "Block");
      model.component("comp1").geom("geom1").feature("thorax_keep_blk").set("size", "2*breast_radius 2*breast_radius 2*breast_radius");
      model.component("comp1").geom("geom1").feature("thorax_keep_blk").set("pos", "-breast_radius 0 -breast_radius");
      model.component("comp1").geom("geom1").feature("thorax_keep_blk").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("thorax_keep_blk").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("thorax_keep", "Cylinder");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("axistype", "x");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("r", "chest_curve_radius");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("h", "2*breast_radius");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("pos", "-breast_radius chest_curve_center_y 0");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("thorax_keep").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("thorax_keep_reg", "Difference");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").selection("input").set("thorax_keep_blk");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").selection("input2").set("thorax_keep");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("thorax_keep_reg").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("thorax_keep_reg");

      model.component("comp1").geom("geom1").create("breast_base", "Intersection");
      model.component("comp1").geom("geom1").feature("breast_base").selection("input").set("sph_outer", "thorax_keep_reg");
      model.component("comp1").geom("geom1").feature("breast_base").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("breast_base").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("breast_base").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("breast_base").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("breast_base");
      breastBaseObjs = model.component("comp1").geom("geom1").feature("breast_base").objectNames();
    } else {
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
      breastBaseObjs = model.component("comp1").geom("geom1").feature("breast_base").objectNames();
    }

    model.component("comp1").geom("geom1").create("breast_outer", "Union");
    model.component("comp1").geom("geom1").feature("breast_outer").selection("input").set(breastBaseObjs[0], "nipple_cap");
    model.component("comp1").geom("geom1").feature("breast_outer").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("breast_outer").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("breast_outer");
    String[] breastOuterObjs = model.component("comp1").geom("geom1").feature("breast_outer").objectNames();

    String[] chestObjs;
    if (false) {
      model.component("comp1").geom("geom1").create("thorax_outer", "Cylinder");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("axistype", "x");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("r", "chest_curve_radius+chest_thickness");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("h", "2*breast_radius");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("pos", "-breast_radius chest_curve_center_y 0");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("thorax_outer").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("chest_trim_blk", "Block");
      model.component("comp1").geom("geom1").feature("chest_trim_blk").set("size", "2*breast_radius chest_thickness+chest_curve_depth breast_radius*2");
      model.component("comp1").geom("geom1").feature("chest_trim_blk").set("pos", "-breast_radius -chest_thickness -breast_radius");
      model.component("comp1").geom("geom1").feature("chest_trim_blk").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("chest_trim_blk").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("chest_band", "Difference");
      model.component("comp1").geom("geom1").feature("chest_band").selection("input").set("thorax_outer");
      model.component("comp1").geom("geom1").feature("chest_band").selection("input2").set("thorax_keep");
      model.component("comp1").geom("geom1").feature("chest_band").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("chest_band").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("chest_band").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("chest_band").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("chest_band");

      model.component("comp1").geom("geom1").create("chest_cyl", "Intersection");
      model.component("comp1").geom("geom1").feature("chest_cyl").selection("input").set("chest_band", "chest_trim_blk");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("chest_cyl");
      chestObjs = model.component("comp1").geom("geom1").feature("chest_cyl").objectNames();
    } else {
      model.component("comp1").geom("geom1").create("chest_cyl", "Cylinder");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("axistype", "y");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("r", "breast_radius");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("h", "chest_thickness");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("pos", "0 -chest_thickness 0");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("chest_cyl").set("selresultshow", "all");
      model.component("comp1").geom("geom1").run("chest_cyl");
      chestObjs = model.component("comp1").geom("geom1").feature("chest_cyl").objectNames();
    }

    if (false) {
      model.component("comp1").geom("geom1").create("gland_keep_blk", "Block");
      model.component("comp1").geom("geom1").feature("gland_keep_blk").set("size", "2*breast_radius 2*breast_radius 2*breast_radius");
      model.component("comp1").geom("geom1").feature("gland_keep_blk").set("pos", "-breast_radius 0 -breast_radius");
      model.component("comp1").geom("geom1").feature("gland_keep_blk").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("gland_keep_blk").set("selresultshow", "all");

      model.component("comp1").geom("geom1").create("gland_keep_anterior", "Difference");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").selection("input").set("gland_keep_blk");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").selection("input2").set("thorax_keep");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("intbnd", "on");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("propagatesel", "on");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresultshow", "all");
    } else {
      model.component("comp1").geom("geom1").create("gland_keep_anterior", "Block");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("size", "2*breast_radius 2*breast_radius 2*breast_radius");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("pos", "-breast_radius 0 -breast_radius");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresult", "on");
      model.component("comp1").geom("geom1").feature("gland_keep_anterior").set("selresultshow", "all");
    }
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

    model.component("comp1").geom("geom1").create("gland_nipple_core", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("gland_nipple_core").set("semiaxes", "0.00793800 0.00793800 0.00336000");
    model.component("comp1").geom("geom1").feature("gland_nipple_core").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("gland_nipple_core").set("pos", "0 0.07154560 0");
    model.component("comp1").geom("geom1").feature("gland_nipple_core").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_nipple_core").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_nipple_core");

    model.component("comp1").geom("geom1").create("gland_seed_with_nipple", "Union");
    model.component("comp1").geom("geom1").feature("gland_seed_with_nipple").selection("input").set("gland_seed", "gland_nipple_core");
    model.component("comp1").geom("geom1").feature("gland_seed_with_nipple").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("gland_seed_with_nipple").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("gland_seed_with_nipple").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_seed_with_nipple").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_seed_with_nipple");
    String[] glandSeedWithNippleObjs = model.component("comp1").geom("geom1").feature("gland_seed_with_nipple").objectNames();


    model.component("comp1").geom("geom1").create("gland_clip", "Intersection");
    model.component("comp1").geom("geom1").feature("gland_clip").selection("input").set(glandSeedWithNippleObjs[0], breastOuterObjs[0]);
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
    // - Build plan JSON: C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_baseline_reference_simple_gland_static_baseline/prepare/full_baseline_reference_simple_gland_static_baseline_comsol_build_plan.json
    // - Selection hints JSON: C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_baseline_reference_simple_gland_static_baseline/build/full_baseline_reference_simple_gland_static_baseline_comsol_selection_hints.json
    // - Lobule primitives in plan: 0
    // - Anatomical lobe groups interpreted in COMSOL: 0
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
    // - chest density (COMSOL explicit): 1050.0
    // - chest E (COMSOL explicit): 10000.0
    // - chest nu (COMSOL explicit): 0.49
    //
    // Physics scaffold:
    model.component("comp1").material().create("mat_chest", "Common");
    model.component("comp1").material("mat_chest").label("ChestWall");
    model.component("comp1").material("mat_chest").selection().named("geom1_chest_cyl_dom");
    model.component("comp1").material("mat_chest").propertyGroup("def").set("density", new String[] { "chest_density" });
    model.component("comp1").material("mat_chest").propertyGroup("def").set("youngsmodulus", new String[] { "chest_E" });
    model.component("comp1").material("mat_chest").propertyGroup("def").set("poissonsratio", new String[] { "chest_nu" });

    model.component("comp1").material().create("mat_skin_shell", "Common");
    model.component("comp1").material("mat_skin_shell").label("SkinShellScaffold");
    model.component("comp1").material("mat_skin_shell").selection().named("geom1_breast_outer_bnd");
    model.component("comp1").material("mat_skin_shell").propertyGroup("def").set("density", new String[] { "skin_density" });
    model.component("comp1").material("mat_skin_shell").propertyGroup("def").set("youngsmodulus", new String[] { "skin_E" });
    model.component("comp1").material("mat_skin_shell").propertyGroup("def").set("poissonsratio", new String[] { "skin_nu" });

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

    StringBuilder hyperelasticNotes = new StringBuilder();
    boolean solidHyperelasticReady = true;
    solidHyperelasticReady = tryCreateMooneyRivlinFeature(
      model,
      "solid",
      "hmat_adipose",
      3,
      "geom1_adipose_diff_dom",
      "adipose_density",
      "adipose_c10",
      "adipose_c01",
      "adipose_bulk_modulus",
      hyperelasticNotes
    ) && solidHyperelasticReady;
    solidHyperelasticReady = tryCreateMooneyRivlinFeature(
      model,
      "solid",
      "hmat_glandular",
      3,
      "geom1_gland_clip_dom",
      "glandular_density",
      "glandular_c10",
      "glandular_c01",
      "glandular_bulk_modulus",
      hyperelasticNotes
    ) && solidHyperelasticReady;
    if (solidHyperelasticReady) {
      tryRestrictLinearElasticFeature(model, "solid", "geom1_chest_cyl_dom", hyperelasticNotes);
    } else {
      hyperelasticNotes.append("Solid Mooney-Rivlin scaffold incomplete; keeping default linear elastic fallback on solid.\n");
    }

    StringBuilder shellScaffoldNotes = new StringBuilder();
    String shellPhysicsTag = tryCreatePhysics(model, "shell1", new String[] { "Shell", "shell" }, "geom1", shellScaffoldNotes);
    if (shellPhysicsTag != null) {
      model.component("comp1").physics(shellPhysicsTag).selection().named("geom1_breast_outer_bnd");
      tryConfigureShellThickness(model, shellPhysicsTag, "skin_shell_thickness", shellScaffoldNotes);
      boolean shellHyperelasticReady = tryCreateMooneyRivlinFeature(
        model,
        shellPhysicsTag,
        "hmat_skin",
        2,
        "geom1_breast_outer_bnd",
        "skin_density",
        "skin_c10",
        "skin_c01",
        "skin_bulk_modulus",
        hyperelasticNotes
      );
      if (shellHyperelasticReady) {
        tryDeactivateLinearElasticFeatures(model, shellPhysicsTag, hyperelasticNotes);
      } else {
        hyperelasticNotes.append("Shell Mooney-Rivlin scaffold incomplete; leaving default shell constitutive fallback active.\n");
      }
    }
    if (true && shellPhysicsTag != null) {
      tryCreateSolidThinStructureConnection(
        model,
        "sthin1",
        new String[] { "SolidThinStructureConnection", "SolidShellConnection", "solidthin", "sthin" },
        "geom1",
        "geom1_breast_outer_bnd",
        "solid",
        shellPhysicsTag,
        shellScaffoldNotes
      );
    }
    // Do not store free-form debug notes in COMSOL parameters; they are parsed as expressions.


    // Current builder scope:
    // 1) build a COMSOL-native outer breast, glandular core, and chest-wall support
    // 2) expose stable finalized geometry selections for the main regions
    // 3) attach a separate linear chest-wall material and Mooney-Rivlin FEBio-derived
    //    hyperelastic scaffolds for adipose, glandular, and optional skin shell
    // 4) optionally scaffold a COMSOL Shell interface and a first Solid-Thin Structure Connection attempt
    // 5) run and save MPH
    //
    // Note:
    // This file is still a scaffold. It now creates real geometry, a separate chest
    // wall material, and a best-effort Mooney-Rivlin hyperelastic setup from the
    // FEBio inputs, but dynamic motion and heterogeneous field mapping still need
    // further work.

    model.component("comp1").geom("geom1").run("breast_union");
    model.component("comp1").mesh("mesh1").run();
    return model;
  }

  public static void main(String[] args) throws Exception {
    Model model = run();
    model.save("C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_baseline_reference_simple_gland_static_baseline/build/full_baseline_reference_simple_gland_static_baseline_generated.mph");
    ModelUtil.disconnect();
  }
  private static String tryCreatePhysics(Model model, String tag, String[] candidateIds, String geomTag, StringBuilder notes) {
    for (String candidateId : candidateIds) {
      try {
        model.component("comp1").physics().create(tag, candidateId, geomTag);
        notes.append("Created physics ").append(tag).append(" with id ").append(candidateId).append("\n");
        return tag;
      } catch (Exception ex) {
        notes.append("Physics id ").append(candidateId).append(" failed: ").append(ex.getMessage()).append("\n");
      }
    }
    return null;
  }

  private static void tryConfigureShellThickness(Model model, String physicsTag, String thicknessExpr, StringBuilder notes) {
    String[] candidateFeatureTags = new String[] { "thick1", "thk1", "to1", "t1" };
    for (String featureTag : candidateFeatureTags) {
      try {
        model.component("comp1").physics(physicsTag).feature(featureTag).set("d0", thicknessExpr);
        notes.append("Assigned shell thickness on feature ").append(featureTag).append("\n");
        return;
      } catch (Exception ignored) {
      }
      try {
        model.component("comp1").physics(physicsTag).feature(featureTag).set("thickness", thicknessExpr);
        notes.append("Assigned shell thickness on feature ").append(featureTag).append(" via thickness property\n");
        return;
      } catch (Exception ignored) {
      }
    }
    try {
      for (String featureTag : model.component("comp1").physics(physicsTag).feature().tags()) {
        try {
          model.component("comp1").physics(physicsTag).feature(featureTag).set("d0", thicknessExpr);
          notes.append("Assigned shell thickness on discovered feature ").append(featureTag).append("\n");
          return;
        } catch (Exception ignored) {
        }
        try {
          model.component("comp1").physics(physicsTag).feature(featureTag).set("thickness", thicknessExpr);
          notes.append("Assigned shell thickness on discovered feature ").append(featureTag).append(" via thickness property\n");
          return;
        } catch (Exception ignored) {
        }
      }
    } catch (Exception ex) {
      notes.append("Could not inspect shell features for thickness assignment: ").append(ex.getMessage()).append("\n");
      return;
    }
    notes.append("Shell physics was created, but no thickness feature accepted skin_shell_thickness automatically.\n");
  }

  private static String tryCreateSolidThinStructureConnection(
    Model model,
    String tag,
    String[] candidateIds,
    String geomTag,
    String selectionName,
    String solidPhysicsTag,
    String shellPhysicsTag,
    StringBuilder notes
  ) {
    for (String candidateId : candidateIds) {
      try {
        model.multiphysics().create(tag, candidateId, geomTag);
        try {
          model.multiphysics(tag).selection().named(selectionName);
        } catch (Exception selectionEx) {
          notes.append("Created ").append(tag).append(" but selection binding failed: ").append(selectionEx.getMessage()).append("\n");
        }
        trySetStringProperties(model, tag, new String[] { "solid", "solidphys", "solidtag", "solidphysics" }, solidPhysicsTag, notes);
        trySetStringProperties(model, tag, new String[] { "shell", "thinstructure", "shellphys", "shelltag", "shellphysics" }, shellPhysicsTag, notes);
        notes.append("Created multiphysics ").append(tag).append(" with id ").append(candidateId).append("\n");
        return tag;
      } catch (Exception ex) {
        notes.append("Multiphysics id ").append(candidateId).append(" failed: ").append(ex.getMessage()).append("\n");
      }
    }
    return null;
  }

  private static void trySetStringProperties(Model model, String multiphysicsTag, String[] keys, String value, StringBuilder notes) {
    for (String key : keys) {
      try {
        model.multiphysics(multiphysicsTag).set(key, value);
        notes.append("Set ").append(multiphysicsTag).append(".").append(key).append("=").append(value).append("\n");
        return;
      } catch (Exception ignored) {
      }
      try {
        model.multiphysics(multiphysicsTag).set(key, new String[] { value });
        notes.append("Set ").append(multiphysicsTag).append(".").append(key).append("=[").append(value).append("]\n");
        return;
      } catch (Exception ignored) {
      }
    }
  }

  private static boolean tryCreateMooneyRivlinFeature(
    Model model,
    String physicsTag,
    String featureTag,
    int entityDim,
    String selectionName,
    String densityExpr,
    String c10Expr,
    String c01Expr,
    String bulkExpr,
    StringBuilder notes
  ) {
    String[] candidateIds = new String[] { "HyperelasticMaterial", "Hyperelastic", "hyperelastic" };
    for (String candidateId : candidateIds) {
      try {
        model.component("comp1").physics(physicsTag).create(featureTag, candidateId, entityDim);
        bindFeatureSelection(model, physicsTag, featureTag, selectionName, notes);
        trySetFeatureProperty(model, physicsTag, featureTag, new String[] { "materialmodel", "MaterialModel", "model" }, new String[] { "MooneyRivlin2", "MooneyRivlin", "Mooney-Rivlin, Two Parameters" }, notes);
        trySetFeatureProperty(model, physicsTag, featureTag, new String[] { "compressibility", "Compressibility", "comp" }, new String[] { "NearlyIncompressible", "nearlyincompressible", "Nearly incompressible" }, notes);
        trySetFeatureProperty(model, physicsTag, featureTag, new String[] { "c10", "C10" }, new String[] { c10Expr }, notes);
        trySetFeatureProperty(model, physicsTag, featureTag, new String[] { "c01", "C01" }, new String[] { c01Expr }, notes);
        trySetFeatureProperty(model, physicsTag, featureTag, new String[] { "kappa", "K", "bulkmodulus", "bulkModulus" }, new String[] { bulkExpr }, notes);
        trySetFeatureProperty(model, physicsTag, featureTag, new String[] { "rho", "density" }, new String[] { densityExpr }, notes);
        trySetFeatureProperty(model, physicsTag, featureTag, new String[] { "usemixedformulation", "mixedformulation", "mixed" }, new String[] { "Pressure formulation", "PressureFormulation", "pressure" }, notes);
        notes.append("Created Mooney-Rivlin hyperelastic feature ").append(featureTag).append(" on physics ").append(physicsTag).append(" with id ").append(candidateId).append("\n");
        return true;
      } catch (Exception ex) {
        notes.append("Hyperelastic feature id ").append(candidateId).append(" failed on ").append(physicsTag).append(": ").append(ex.getMessage()).append("\n");
      }
    }
    return false;
  }

  private static void bindFeatureSelection(Model model, String physicsTag, String featureTag, String selectionName, StringBuilder notes) {
    try {
      model.component("comp1").physics(physicsTag).feature(featureTag).selection().named(selectionName);
      notes.append("Bound ").append(featureTag).append(" to selection ").append(selectionName).append("\n");
    } catch (Exception ex) {
      notes.append("Selection binding failed for ").append(featureTag).append(": ").append(ex.getMessage()).append("\n");
    }
  }

  private static void trySetFeatureProperty(
    Model model,
    String physicsTag,
    String featureTag,
    String[] keys,
    String[] values,
    StringBuilder notes
  ) {
    for (String key : keys) {
      for (String value : values) {
        try {
          model.component("comp1").physics(physicsTag).feature(featureTag).set(key, value);
          notes.append("Set ").append(featureTag).append(".").append(key).append("=").append(value).append("\n");
          return;
        } catch (Exception ignored) {
        }
        try {
          model.component("comp1").physics(physicsTag).feature(featureTag).set(key, new String[] { value });
          notes.append("Set ").append(featureTag).append(".").append(key).append("=[").append(value).append("]\n");
          return;
        } catch (Exception ignored) {
        }
      }
    }
  }

  private static void tryRestrictLinearElasticFeature(Model model, String physicsTag, String selectionName, StringBuilder notes) {
    String[] candidateTags = new String[] { "lemm1", "lemm", "linel1", "linel" };
    for (String candidateTag : candidateTags) {
      try {
        model.component("comp1").physics(physicsTag).feature(candidateTag).selection().named(selectionName);
        notes.append("Restricted linear elastic feature ").append(candidateTag).append(" to ").append(selectionName).append("\n");
        return;
      } catch (Exception ignored) {
      }
    }
    try {
      for (String featureTag : model.component("comp1").physics(physicsTag).feature().tags()) {
        String normalized = featureTag.toLowerCase();
        if (!(normalized.contains("lemm") || normalized.contains("linel"))) {
          continue;
        }
        try {
          model.component("comp1").physics(physicsTag).feature(featureTag).selection().named(selectionName);
          notes.append("Restricted discovered linear elastic feature ").append(featureTag).append(" to ").append(selectionName).append("\n");
          return;
        } catch (Exception ignored) {
        }
      }
    } catch (Exception ex) {
      notes.append("Could not inspect linear elastic features on ").append(physicsTag).append(": ").append(ex.getMessage()).append("\n");
      return;
    }
    notes.append("Could not automatically restrict default linear elastic feature on ").append(physicsTag).append(".\n");
  }

  private static void tryDeactivateLinearElasticFeatures(Model model, String physicsTag, StringBuilder notes) {
    String[] candidateTags = new String[] { "lemm1", "lemm", "linel1", "linel" };
    for (String candidateTag : candidateTags) {
      if (tryDeactivateFeature(model, physicsTag, candidateTag, notes)) {
        return;
      }
    }
    try {
      for (String featureTag : model.component("comp1").physics(physicsTag).feature().tags()) {
        String normalized = featureTag.toLowerCase();
        if (!(normalized.contains("lemm") || normalized.contains("linel"))) {
          continue;
        }
        if (tryDeactivateFeature(model, physicsTag, featureTag, notes)) {
          return;
        }
      }
    } catch (Exception ex) {
      notes.append("Could not inspect shell linear elastic features on ").append(physicsTag).append(": ").append(ex.getMessage()).append("\n");
      return;
    }
    notes.append("Could not automatically deactivate default linear elastic feature on ").append(physicsTag).append(".\n");
  }

  private static boolean tryDeactivateFeature(Model model, String physicsTag, String featureTag, StringBuilder notes) {
    try {
      model.component("comp1").physics(physicsTag).feature(featureTag).active(false);
      notes.append("Deactivated feature ").append(featureTag).append(" on ").append(physicsTag).append("\n");
      return true;
    } catch (Exception ignored) {
    }
    return false;
  }


}
