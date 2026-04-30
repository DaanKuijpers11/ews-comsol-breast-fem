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
    model.component("comp1").geom("geom1").feature("breast_outer").selection("input").set(breastBaseObjs);
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

    String[] glandLobuleObjs = buildGlandLobules(model);


    model.component("comp1").geom("geom1").create("gland_clip", "Intersection");
    model.component("comp1").geom("geom1").feature("gland_clip").selection("input").set(glandLobuleObjs[0], breastOuterObjs[0], glandKeepAnteriorObjs[0]);
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
    // - Lobule primitives in plan: 18
    // - Anatomical lobe groups interpreted in COMSOL: 18
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

    model.component("comp1").material().create("mat_adipose", "Common");
    model.component("comp1").material("mat_adipose").label("Adipose");
    model.component("comp1").material("mat_adipose").selection().named("geom1_adipose_diff_dom");
    model.component("comp1").material("mat_adipose").propertyGroup("def").set("density", new String[] { "adipose_density" });

    model.component("comp1").material().create("mat_glandular", "Common");
    model.component("comp1").material("mat_glandular").label("Glandular");
    model.component("comp1").material("mat_glandular").selection().named("geom1_gland_clip_dom");
    model.component("comp1").material("mat_glandular").propertyGroup("def").set("density", new String[] { "glandular_density" });

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
    model.param().set("skin_shell_scaffold_notes", shellScaffoldNotes.toString());
    model.param().set("hyperelastic_scaffold_notes", hyperelasticNotes.toString());


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
    model.save("C:/Users/20223231/ews_fem_clean/runs/comsol_testcases/output/build/default_comsol_generated.mph");
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

  private static String[] buildGlandLobules(Model model) {
    buildSharedDuctHub(model);
    buildAnatomicalLobe01(model);
    buildAnatomicalLobe02(model);
    buildAnatomicalLobe03(model);
    buildAnatomicalLobe04(model);
    buildAnatomicalLobe05(model);
    buildAnatomicalLobe06(model);
    buildAnatomicalLobe07(model);
    buildAnatomicalLobe08(model);
    buildAnatomicalLobe09(model);
    buildAnatomicalLobe10(model);
    buildAnatomicalLobe11(model);
    buildAnatomicalLobe12(model);
    buildAnatomicalLobe13(model);
    buildAnatomicalLobe14(model);
    buildAnatomicalLobe15(model);
    buildAnatomicalLobe16(model);
    buildAnatomicalLobe17(model);
    buildAnatomicalLobe18(model);

    model.component("comp1").geom("geom1").create("gland_lobules", "Union");
    model.component("comp1").geom("geom1").feature("gland_lobules").selection("input").set("anatomical_lobe_01", "anatomical_lobe_02", "anatomical_lobe_03", "anatomical_lobe_04", "anatomical_lobe_05", "anatomical_lobe_06", "anatomical_lobe_07", "anatomical_lobe_08", "anatomical_lobe_09", "anatomical_lobe_10", "anatomical_lobe_11", "anatomical_lobe_12", "anatomical_lobe_13", "anatomical_lobe_14", "anatomical_lobe_15", "anatomical_lobe_16", "anatomical_lobe_17", "anatomical_lobe_18", "duct_hub_core", "duct_hub_cap", "duct_trunk_main");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_lobules");
    return model.component("comp1").geom("geom1").feature("gland_lobules").objectNames();
  }


  private static void buildSharedDuctHub(Model model) {

    model.component("comp1").geom("geom1").create("duct_hub_core", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("duct_hub_core").set("semiaxes", "0.00143500 0.00143500 0.00210000");
    model.component("comp1").geom("geom1").feature("duct_hub_core").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("duct_hub_core").set("pos", "0 0.05610000 0");
    model.component("comp1").geom("geom1").feature("duct_hub_core").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("duct_hub_core").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("duct_hub_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("duct_hub_cap").set("semiaxes", "0.00196000 0.00196000 0.00112000");
    model.component("comp1").geom("geom1").feature("duct_hub_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("duct_hub_cap").set("pos", "0 0.05519000 0");
    model.component("comp1").geom("geom1").feature("duct_hub_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("duct_hub_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("duct_trunk_main", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("duct_trunk_main").set("semiaxes", "0.00084000 0.00084000 0.00308000");
    model.component("comp1").geom("geom1").feature("duct_trunk_main").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("duct_trunk_main").set("pos", "0 0.05400000 0");
    model.component("comp1").geom("geom1").feature("duct_trunk_main").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("duct_trunk_main").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe01(Model model) {


    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("semiaxes", "0.00712502 0.00696309 0.01243098");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("pos", "0.01109420 0.03653753 0.00004679");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("pos", "0.01346107 0.03470945 0.00106527");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("pos", "0.01582889 0.03288136 0.00186110");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("pos", "0.01819840 0.03105327 0.00225573");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("pos", "0.02057001 0.02922519 0.00215007");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("pos", "0.02294374 0.02739710 0.00154411");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("pos", "0.02531916 0.02556902 0.00053695");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("pos", "0.02769552 0.02374093 -0.00069286");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("pos", "0.02074818 0.03202571 0.00019441");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_01_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("pos", "0.00866523 0.02593463 0.00003555");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("pos", "0.01040435 0.03954926 0.00011877");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("pos", "0.00972511 0.04070565 0.00018635");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("pos", "0.00905648 0.04183477 0.00024901");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("pos", "0.00839846 0.04293663 0.00030677");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("pos", "0.00775104 0.04401122 0.00035962");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("pos", "0.00711424 0.04505855 0.00040756");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("pos", "0.00648804 0.04607862 0.00045059");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("pos", "0.00587245 0.04707142 0.00048872");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("pos", "0.00526747 0.04803697 0.00052193");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("pos", "0.00467309 0.04897524 0.00055024");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("pos", "0.00408933 0.04988626 0.00057364");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("pos", "0.00351617 0.05077001 0.00059213");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("pos", "0.00295362 0.05162650 0.00060571");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("pos", "0.00240168 0.05245573 0.00061439");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("pos", "0.00186035 0.05325769 0.00061815");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("pos", "0.00132962 0.05403239 0.00061701");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("pos", "0.00080951 0.05477983 0.00061096");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("pos", "0.00030000 0.05550000 0.00060000");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_01", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_01").selection("input").set("lobe_01_petal_seg_01", "lobe_01_petal_seg_02", "lobe_01_petal_seg_03", "lobe_01_petal_seg_04", "lobe_01_petal_seg_05", "lobe_01_petal_seg_06", "lobe_01_petal_seg_07", "lobe_01_petal_seg_08", "lobe_01_petal_wing", "lobe_01_posterior_cap", "lobe_01_duct_bead_01", "lobe_01_duct_bead_02", "lobe_01_duct_bead_03", "lobe_01_duct_bead_04", "lobe_01_duct_bead_05", "lobe_01_duct_bead_06", "lobe_01_duct_bead_07", "lobe_01_duct_bead_08", "lobe_01_duct_bead_09", "lobe_01_duct_bead_10", "lobe_01_duct_bead_11", "lobe_01_duct_bead_12", "lobe_01_duct_bead_13", "lobe_01_duct_bead_14", "lobe_01_duct_bead_15", "lobe_01_duct_bead_16", "lobe_01_duct_bead_17", "lobe_01_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_01").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_01").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_01").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe02(Model model) {


    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("semiaxes", "0.00712502 0.00696309 0.01243098");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("pos", "0.00558961 0.03653753 0.00949895");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("pos", "0.00592298 0.03470945 0.01205399");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("pos", "0.00644824 0.03288136 0.01449612");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("pos", "0.00731929 0.03105327 0.01673478");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("pos", "0.00862152 0.02922519 0.01871971");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("pos", "0.01035493 0.02739710 0.02045091");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("pos", "0.01243412 0.02556902 0.02197864");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("pos", "0.01470521 0.02374093 0.02339344");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("pos", "0.01036216 0.03202571 0.01792896");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_02_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("pos", "0.00435774 0.02593463 0.00720128");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("pos", "0.00517624 0.03955162 0.00885375");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("pos", "0.00477256 0.04071008 0.00831682");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("pos", "0.00437855 0.04184100 0.00778601");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("pos", "0.00399423 0.04294438 0.00726133");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("pos", "0.00361959 0.04402022 0.00674279");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("pos", "0.00325464 0.04506852 0.00623038");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("pos", "0.00289936 0.04608928 0.00572411");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("pos", "0.00255377 0.04708250 0.00522396");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("pos", "0.00221786 0.04804818 0.00472995");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("pos", "0.00189163 0.04898632 0.00424207");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("pos", "0.00157509 0.04989692 0.00376032");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("pos", "0.00126823 0.05077998 0.00328471");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("pos", "0.00097105 0.05163550 0.00281523");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("pos", "0.00068355 0.05246348 0.00235188");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("pos", "0.00040574 0.05326392 0.00189466");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("pos", "0.00013760 0.05403682 0.00144358");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("pos", "-0.00012085 0.05478218 0.00099863");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("pos", "-0.00036962 0.05550000 0.00055981");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_02", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_02").selection("input").set("lobe_02_petal_seg_01", "lobe_02_petal_seg_02", "lobe_02_petal_seg_03", "lobe_02_petal_seg_04", "lobe_02_petal_seg_05", "lobe_02_petal_seg_06", "lobe_02_petal_seg_07", "lobe_02_petal_seg_08", "lobe_02_petal_wing", "lobe_02_posterior_cap", "lobe_02_duct_bead_01", "lobe_02_duct_bead_02", "lobe_02_duct_bead_03", "lobe_02_duct_bead_04", "lobe_02_duct_bead_05", "lobe_02_duct_bead_06", "lobe_02_duct_bead_07", "lobe_02_duct_bead_08", "lobe_02_duct_bead_09", "lobe_02_duct_bead_10", "lobe_02_duct_bead_11", "lobe_02_duct_bead_12", "lobe_02_duct_bead_13", "lobe_02_duct_bead_14", "lobe_02_duct_bead_15", "lobe_02_duct_bead_16", "lobe_02_duct_bead_17", "lobe_02_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_02").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_02").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_02").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe03(Model model) {


    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("semiaxes", "0.00712502 0.00696309 0.01243098");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("pos", "-0.00552700 0.03653753 0.00958580");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("pos", "-0.00758507 0.03470945 0.01113622");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("pos", "-0.00945024 0.03288136 0.01279786");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("pos", "-0.01096784 0.03105327 0.01465989");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("pos", "-0.01205203 0.02922519 0.01677183");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("pos", "-0.01270281 0.02739710 0.01913366");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("pos", "-0.01300601 0.02556902 0.02169590");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("pos", "-0.01311633 0.02374093 0.02436935");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("pos", "-0.01043306 0.03202571 0.01788248");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_03_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("pos", "-0.00431372 0.02593463 0.00727625");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("pos", "-0.00524829 0.03955021 0.00886010");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("pos", "-0.00497063 0.04070743 0.00824811");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("pos", "-0.00469400 0.04183727 0.00764716");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("pos", "-0.00441842 0.04293974 0.00705728");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("pos", "-0.00414387 0.04401483 0.00647845");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("pos", "-0.00387037 0.04506255 0.00591068");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("pos", "-0.00359791 0.04608290 0.00535396");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("pos", "-0.00332649 0.04707587 0.00480830");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("pos", "-0.00305612 0.04804147 0.00427370");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("pos", "-0.00278678 0.04897969 0.00375015");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("pos", "-0.00251849 0.04989054 0.00323766");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("pos", "-0.00225124 0.05077402 0.00273623");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("pos", "-0.00198503 0.05163012 0.00224585");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("pos", "-0.00171987 0.05245884 0.00176653");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("pos", "-0.00145574 0.05326019 0.00129826");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("pos", "-0.00119266 0.05403417 0.00084106");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("pos", "-0.00093062 0.05478077 0.00039490");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("pos", "-0.00066962 0.05550000 -0.00004019");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_03", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_03").selection("input").set("lobe_03_petal_seg_01", "lobe_03_petal_seg_02", "lobe_03_petal_seg_03", "lobe_03_petal_seg_04", "lobe_03_petal_seg_05", "lobe_03_petal_seg_06", "lobe_03_petal_seg_07", "lobe_03_petal_seg_08", "lobe_03_petal_wing", "lobe_03_posterior_cap", "lobe_03_duct_bead_01", "lobe_03_duct_bead_02", "lobe_03_duct_bead_03", "lobe_03_duct_bead_04", "lobe_03_duct_bead_05", "lobe_03_duct_bead_06", "lobe_03_duct_bead_07", "lobe_03_duct_bead_08", "lobe_03_duct_bead_09", "lobe_03_duct_bead_10", "lobe_03_duct_bead_11", "lobe_03_duct_bead_12", "lobe_03_duct_bead_13", "lobe_03_duct_bead_14", "lobe_03_duct_bead_15", "lobe_03_duct_bead_16", "lobe_03_duct_bead_17", "lobe_03_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_03").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_03").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_03").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe04(Model model) {


    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("semiaxes", "0.00712502 0.00696309 0.01243098");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("pos", "-0.01113587 0.03653753 0.00004853");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("pos", "-0.01351139 0.03470945 -0.00094962");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("pos", "-0.01588595 0.03288136 -0.00172512");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("pos", "-0.01825875 0.03105327 -0.00209941");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("pos", "-0.02062938 0.02922519 -0.00197342");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("pos", "-0.02299782 0.02739710 -0.00134713");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("pos", "-0.02536452 0.02556902 -0.00031963");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("pos", "-0.02773024 0.02374093 0.00093051");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("pos", "-0.02079819 0.03202571 -0.00007827");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_04_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("pos", "-0.00870691 0.02593463 0.00003691");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("pos", "-0.01044415 0.03954792 -0.00002652");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("pos", "-0.00976299 0.04070311 -0.00008868");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("pos", "-0.00909239 0.04183120 -0.00013846");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("pos", "-0.00843234 0.04293219 -0.00017586");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("pos", "-0.00778285 0.04400607 -0.00020089");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("pos", "-0.00714391 0.04505285 -0.00021354");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("pos", "-0.00651553 0.04607252 -0.00021382");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("pos", "-0.00589770 0.04706509 -0.00020171");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("pos", "-0.00529043 0.04803055 -0.00017724");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("pos", "-0.00469372 0.04896891 -0.00014038");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("pos", "-0.00410756 0.04988016 -0.00009115");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("pos", "-0.00353195 0.05076431 -0.00002954");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("pos", "-0.00296691 0.05162135 0.00004444");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("pos", "-0.00241241 0.05245129 0.00013080");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("pos", "-0.00186848 0.05325412 0.00022953");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("pos", "-0.00133510 0.05402985 0.00034065");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("pos", "-0.00081227 0.05477848 0.00046414");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("pos", "-0.00030000 0.05550000 0.00060000");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_04", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_04").selection("input").set("lobe_04_petal_seg_01", "lobe_04_petal_seg_02", "lobe_04_petal_seg_03", "lobe_04_petal_seg_04", "lobe_04_petal_seg_05", "lobe_04_petal_seg_06", "lobe_04_petal_seg_07", "lobe_04_petal_seg_08", "lobe_04_petal_wing", "lobe_04_posterior_cap", "lobe_04_duct_bead_01", "lobe_04_duct_bead_02", "lobe_04_duct_bead_03", "lobe_04_duct_bead_04", "lobe_04_duct_bead_05", "lobe_04_duct_bead_06", "lobe_04_duct_bead_07", "lobe_04_duct_bead_08", "lobe_04_duct_bead_09", "lobe_04_duct_bead_10", "lobe_04_duct_bead_11", "lobe_04_duct_bead_12", "lobe_04_duct_bead_13", "lobe_04_duct_bead_14", "lobe_04_duct_bead_15", "lobe_04_duct_bead_16", "lobe_04_duct_bead_17", "lobe_04_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_04").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_04").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_04").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe05(Model model) {


    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("semiaxes", "0.00712502 0.00696309 0.01243098");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("pos", "-0.00552839 0.03653753 -0.00955346");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("pos", "-0.00584314 0.03470945 -0.01211087");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("pos", "-0.00635060 0.03288136 -0.01455676");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("pos", "-0.00720531 0.03105327 -0.01680171");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("pos", "-0.00849304 0.02922519 -0.01879607");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("pos", "-0.01021380 0.02739710 -0.02053985");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("pos", "-0.01228181 0.02556902 -0.02208268");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("pos", "-0.01454253 0.02374093 -0.02351400");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("pos", "-0.01028209 0.03202571 -0.01798599");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_05_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("pos", "-0.00431181 0.02593463 -0.00724600");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("pos", "-0.00512214 0.03955109 -0.00890290");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("pos", "-0.00473193 0.04070909 -0.00835733");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("pos", "-0.00435775 0.04183961 -0.00781420");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("pos", "-0.00399961 0.04294265 -0.00727351");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("pos", "-0.00365751 0.04401821 -0.00673526");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("pos", "-0.00333144 0.04506630 -0.00619944");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("pos", "-0.00302142 0.04608690 -0.00566606");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("pos", "-0.00272743 0.04708003 -0.00513512");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("pos", "-0.00244948 0.04804568 -0.00460662");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("pos", "-0.00218757 0.04898385 -0.00408056");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("pos", "-0.00194169 0.04989454 -0.00355693");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("pos", "-0.00171185 0.05077776 -0.00303574");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("pos", "-0.00149805 0.05163349 -0.00251699");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("pos", "-0.00130029 0.05246175 -0.00200068");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("pos", "-0.00111856 0.05326253 -0.00148680");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("pos", "-0.00095288 0.05403583 -0.00097537");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("pos", "-0.00080323 0.05478165 -0.00046637");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("pos", "-0.00066962 0.05550000 0.00004019");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_05", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_05").selection("input").set("lobe_05_petal_seg_01", "lobe_05_petal_seg_02", "lobe_05_petal_seg_03", "lobe_05_petal_seg_04", "lobe_05_petal_seg_05", "lobe_05_petal_seg_06", "lobe_05_petal_seg_07", "lobe_05_petal_seg_08", "lobe_05_petal_wing", "lobe_05_posterior_cap", "lobe_05_duct_bead_01", "lobe_05_duct_bead_02", "lobe_05_duct_bead_03", "lobe_05_duct_bead_04", "lobe_05_duct_bead_05", "lobe_05_duct_bead_06", "lobe_05_duct_bead_07", "lobe_05_duct_bead_08", "lobe_05_duct_bead_09", "lobe_05_duct_bead_10", "lobe_05_duct_bead_11", "lobe_05_duct_bead_12", "lobe_05_duct_bead_13", "lobe_05_duct_bead_14", "lobe_05_duct_bead_15", "lobe_05_duct_bead_16", "lobe_05_duct_bead_17", "lobe_05_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_05").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_05").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_05").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe06(Model model) {


    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("semiaxes", "0.00712502 0.00696309 0.01243098");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("pos", "0.00548469 0.03653753 -0.00965236");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("pos", "0.00753295 0.03470945 -0.01121570");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("pos", "0.00938762 0.03288136 -0.01288905");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("pos", "0.01089347 0.03105327 -0.01476061");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("pos", "0.01196434 0.02922519 -0.01687933");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("pos", "0.01260023 0.02739710 -0.01924521");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("pos", "0.01288729 0.02556902 -0.02180931");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("pos", "0.01298078 0.02374093 -0.02448340");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("pos", "0.01038051 0.03202571 -0.01796220");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_06_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("pos", "0.00428468 0.02593463 -0.00733446");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("pos", "0.00520568 0.03954902 -0.00892461");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("pos", "0.00492123 0.04070519 -0.00831462");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("pos", "0.00463134 0.04183413 -0.00771938");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("pos", "0.00433602 0.04293583 -0.00713888");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("pos", "0.00403525 0.04401029 -0.00657312");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("pos", "0.00372905 0.04505752 -0.00602210");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("pos", "0.00341741 0.04607752 -0.00548583");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("pos", "0.00310033 0.04707028 -0.00496429");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("pos", "0.00277781 0.04803581 -0.00445750");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("pos", "0.00244985 0.04897410 -0.00396546");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("pos", "0.00211645 0.04988516 -0.00348815");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("pos", "0.00177762 0.05076899 -0.00302559");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("pos", "0.00143334 0.05162557 -0.00257777");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("pos", "0.00108363 0.05245493 -0.00214469");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("pos", "0.00072848 0.05325705 -0.00172636");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("pos", "0.00036788 0.05403193 -0.00132276");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("pos", "0.00000185 0.05477958 -0.00093392");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("pos", "-0.00036962 0.05550000 -0.00055981");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_06", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_06").selection("input").set("lobe_06_petal_seg_01", "lobe_06_petal_seg_02", "lobe_06_petal_seg_03", "lobe_06_petal_seg_04", "lobe_06_petal_seg_05", "lobe_06_petal_seg_06", "lobe_06_petal_seg_07", "lobe_06_petal_seg_08", "lobe_06_petal_wing", "lobe_06_posterior_cap", "lobe_06_duct_bead_01", "lobe_06_duct_bead_02", "lobe_06_duct_bead_03", "lobe_06_duct_bead_04", "lobe_06_duct_bead_05", "lobe_06_duct_bead_06", "lobe_06_duct_bead_07", "lobe_06_duct_bead_08", "lobe_06_duct_bead_09", "lobe_06_duct_bead_10", "lobe_06_duct_bead_11", "lobe_06_duct_bead_12", "lobe_06_duct_bead_13", "lobe_06_duct_bead_14", "lobe_06_duct_bead_15", "lobe_06_duct_bead_16", "lobe_06_duct_bead_17", "lobe_06_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_06").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_06").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_06").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe07(Model model) {


    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("pos", "0.02471643 0.02041159 -0.00001283");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("pos", "0.02745914 0.01842607 0.00125674");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("pos", "0.03020171 0.01644056 0.00224354");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("pos", "0.03294402 0.01445504 0.00272080");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("pos", "0.03568599 0.01246953 0.00256269");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("pos", "0.03842763 0.01048401 0.00176920");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("pos", "0.04116901 0.00849850 0.00046618");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("pos", "0.04391025 0.00651298 -0.00111961");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("pos", "0.03442101 0.01562052 0.00007750");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_07_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("pos", "0.02236610 0.00889560 -0.00001148");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("pos", "0.02382773 0.02375054 0.00011669");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("pos", "0.02288530 0.02516111 0.00023754");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("pos", "0.02188915 0.02662881 0.00034978");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("pos", "0.02083928 0.02815364 0.00045340");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("pos", "0.01973568 0.02973560 0.00054842");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("pos", "0.01857836 0.03137469 0.00063482");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("pos", "0.01736732 0.03307092 0.00071262");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("pos", "0.01610254 0.03482427 0.00078180");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("pos", "0.01478405 0.03663475 0.00084237");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("pos", "0.01341183 0.03850237 0.00089433");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("pos", "0.01198589 0.04042712 0.00093767");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("pos", "0.01050622 0.04240899 0.00097241");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("pos", "0.00897283 0.04444800 0.00099854");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("pos", "0.00738571 0.04654414 0.00101605");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("pos", "0.00574487 0.04869741 0.00102496");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("pos", "0.00405030 0.05090781 0.00102525");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("pos", "0.00230201 0.05317534 0.00101693");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("pos", "0.00050000 0.05550000 0.00100000");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_07", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_07").selection("input").set("lobe_07_petal_seg_01", "lobe_07_petal_seg_02", "lobe_07_petal_seg_03", "lobe_07_petal_seg_04", "lobe_07_petal_seg_05", "lobe_07_petal_seg_06", "lobe_07_petal_seg_07", "lobe_07_petal_seg_08", "lobe_07_petal_wing", "lobe_07_posterior_cap", "lobe_07_duct_bead_01", "lobe_07_duct_bead_02", "lobe_07_duct_bead_03", "lobe_07_duct_bead_04", "lobe_07_duct_bead_05", "lobe_07_duct_bead_06", "lobe_07_duct_bead_07", "lobe_07_duct_bead_08", "lobe_07_duct_bead_09", "lobe_07_duct_bead_10", "lobe_07_duct_bead_11", "lobe_07_duct_bead_12", "lobe_07_duct_bead_13", "lobe_07_duct_bead_14", "lobe_07_duct_bead_15", "lobe_07_duct_bead_16", "lobe_07_duct_bead_17", "lobe_07_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_07").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_07").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_07").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe08(Model model) {


    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("pos", "0.02129617 0.02041159 0.01231190");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("pos", "0.02303393 0.01842607 0.01478465");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("pos", "0.02491321 0.01644056 0.01701260");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("pos", "0.02704751 0.01445504 0.01879943");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("pos", "0.02949983 0.01246953 0.02003619");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("pos", "0.03227015 0.01048401 0.02072288");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("pos", "0.03529550 0.00849850 0.02096846");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("pos", "0.03846237 0.00651298 0.02096923");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("pos", "0.02967734 0.01562052 0.01724413");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_08_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("pos", "0.01926141 0.00889560 0.01101490");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("pos", "0.02046498 0.02375276 0.01192659");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("pos", "0.01959194 0.02516528 0.01156705");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("pos", "0.01867707 0.02663468 0.01117297");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("pos", "0.01772035 0.02816094 0.01074434");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("pos", "0.01672179 0.02974408 0.01028116");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("pos", "0.01568139 0.03138408 0.00978343");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("pos", "0.01459915 0.03308096 0.00925116");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("pos", "0.01347507 0.03483470 0.00868434");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("pos", "0.01230915 0.03664532 0.00808297");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("pos", "0.01110139 0.03851280 0.00744705");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("pos", "0.00985178 0.04043716 0.00677659");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("pos", "0.00856034 0.04241838 0.00607158");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("pos", "0.00722705 0.04445648 0.00533202");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("pos", "0.00585193 0.04655144 0.00455792");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("pos", "0.00443496 0.04870328 0.00374927");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("pos", "0.00297615 0.05091198 0.00290607");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("pos", "0.00147550 0.05317756 0.00202832");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("pos", "-0.00006699 0.05550000 0.00111603");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_08", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_08").selection("input").set("lobe_08_petal_seg_01", "lobe_08_petal_seg_02", "lobe_08_petal_seg_03", "lobe_08_petal_seg_04", "lobe_08_petal_seg_05", "lobe_08_petal_seg_06", "lobe_08_petal_seg_07", "lobe_08_petal_seg_08", "lobe_08_petal_wing", "lobe_08_posterior_cap", "lobe_08_duct_bead_01", "lobe_08_duct_bead_02", "lobe_08_duct_bead_03", "lobe_08_duct_bead_04", "lobe_08_duct_bead_05", "lobe_08_duct_bead_06", "lobe_08_duct_bead_07", "lobe_08_duct_bead_08", "lobe_08_duct_bead_09", "lobe_08_duct_bead_10", "lobe_08_duct_bead_11", "lobe_08_duct_bead_12", "lobe_08_duct_bead_13", "lobe_08_duct_bead_14", "lobe_08_duct_bead_15", "lobe_08_duct_bead_16", "lobe_08_duct_bead_17", "lobe_08_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_08").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_08").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_08").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe09(Model model) {


    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("pos", "0.01229593 0.02041159 0.02138559");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("pos", "0.01256085 0.01842607 0.02439626");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("pos", "0.01307091 0.01644056 0.02726597");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("pos", "0.01402270 0.01445504 0.02988172");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("pos", "0.01552531 0.01246953 0.03218076");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("pos", "0.01757873 0.01048401 0.03416310");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("pos", "0.02007389 0.00849850 0.03589146");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("pos", "0.02281418 0.00651298 0.03747888");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("pos", "0.01710596 0.01562052 0.02983592");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_09_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("pos", "0.01112441 0.00889560 0.01913906");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("pos", "0.01174184 0.02375145 0.02058757");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("pos", "0.01116855 0.02516281 0.01984265");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("pos", "0.01057605 0.02663121 0.01904632");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("pos", "0.00996435 0.02815662 0.01819858");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("pos", "0.00933344 0.02973906 0.01729944");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("pos", "0.00868333 0.03137853 0.01634891");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("pos", "0.00801401 0.03307502 0.01534696");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("pos", "0.00732548 0.03482853 0.01429362");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("pos", "0.00661776 0.03663907 0.01318887");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("pos", "0.00589082 0.03850663 0.01203272");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("pos", "0.00514468 0.04043121 0.01082517");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("pos", "0.00437934 0.04241283 0.00956621");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("pos", "0.00359479 0.04445146 0.00825585");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("pos", "0.00279104 0.04654712 0.00689409");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("pos", "0.00196808 0.04869980 0.00548092");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("pos", "0.00112592 0.05090951 0.00401636");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("pos", "0.00026455 0.05317624 0.00250039");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("pos", "-0.00061603 0.05550000 0.00093301");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_09", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_09").selection("input").set("lobe_09_petal_seg_01", "lobe_09_petal_seg_02", "lobe_09_petal_seg_03", "lobe_09_petal_seg_04", "lobe_09_petal_seg_05", "lobe_09_petal_seg_06", "lobe_09_petal_seg_07", "lobe_09_petal_seg_08", "lobe_09_petal_wing", "lobe_09_posterior_cap", "lobe_09_duct_bead_01", "lobe_09_duct_bead_02", "lobe_09_duct_bead_03", "lobe_09_duct_bead_04", "lobe_09_duct_bead_05", "lobe_09_duct_bead_06", "lobe_09_duct_bead_07", "lobe_09_duct_bead_08", "lobe_09_duct_bead_09", "lobe_09_duct_bead_10", "lobe_09_duct_bead_11", "lobe_09_duct_bead_12", "lobe_09_duct_bead_13", "lobe_09_duct_bead_14", "lobe_09_duct_bead_15", "lobe_09_duct_bead_16", "lobe_09_duct_bead_17", "lobe_09_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_09").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_09").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_09").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe10(Model model) {


    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("pos", "0.00002536 0.02041159 0.02468241");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("pos", "-0.00124281 0.01842607 0.02742577");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("pos", "-0.00222822 0.01644056 0.03016884");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("pos", "-0.00270409 0.01445504 0.03291139");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("pos", "-0.00254458 0.01246953 0.03565328");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("pos", "-0.00174970 0.01048401 0.03839452");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("pos", "-0.00044528 0.00849850 0.04113524");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("pos", "0.00114190 0.00651298 0.04387566");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("pos", "-0.00001556 0.01562052 0.03438752");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_10_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("pos", "0.00002294 0.00889560 0.02209102");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("pos", "-0.00010455 0.02375118 0.02368710");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("pos", "-0.00022588 0.02516232 0.02275797");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("pos", "-0.00033862 0.02663051 0.02177448");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("pos", "-0.00044279 0.02815575 0.02073665");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("pos", "-0.00053837 0.02973805 0.01964446");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("pos", "-0.00062537 0.03137741 0.01849792");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("pos", "-0.00070379 0.03307382 0.01729703");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("pos", "-0.00077363 0.03482729 0.01604179");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("pos", "-0.00083488 0.03663781 0.01473219");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("pos", "-0.00088756 0.03850539 0.01336824");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("pos", "-0.00093165 0.04043002 0.01194995");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("pos", "-0.00096716 0.04241171 0.01047729");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("pos", "-0.00099409 0.04445045 0.00895029");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("pos", "-0.00101243 0.04654625 0.00736894");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("pos", "-0.00102220 0.04869911 0.00573323");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("pos", "-0.00102338 0.05090902 0.00404317");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("pos", "-0.00101598 0.05317598 0.00229876");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("pos", "-0.00100000 0.05550000 0.00050000");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_10", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_10").selection("input").set("lobe_10_petal_seg_01", "lobe_10_petal_seg_02", "lobe_10_petal_seg_03", "lobe_10_petal_seg_04", "lobe_10_petal_seg_05", "lobe_10_petal_seg_06", "lobe_10_petal_seg_07", "lobe_10_petal_seg_08", "lobe_10_petal_wing", "lobe_10_posterior_cap", "lobe_10_duct_bead_01", "lobe_10_duct_bead_02", "lobe_10_duct_bead_03", "lobe_10_duct_bead_04", "lobe_10_duct_bead_05", "lobe_10_duct_bead_06", "lobe_10_duct_bead_07", "lobe_10_duct_bead_08", "lobe_10_duct_bead_09", "lobe_10_duct_bead_10", "lobe_10_duct_bead_11", "lobe_10_duct_bead_12", "lobe_10_duct_bead_13", "lobe_10_duct_bead_14", "lobe_10_duct_bead_15", "lobe_10_duct_bead_16", "lobe_10_duct_bead_17", "lobe_10_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_10").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_10").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_10").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe11(Model model) {


    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("pos", "-0.01217128 0.02041159 0.02130302");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("pos", "-0.01463513 0.01842607 0.02305336");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("pos", "-0.01685346 0.01644056 0.02494399");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("pos", "-0.01862938 0.01445504 0.02708738");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("pos", "-0.01985361 0.01246953 0.02954597");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("pos", "-0.02052616 0.01048401 0.03231976");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("pos", "-0.02075630 0.00849850 0.03534632");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("pos", "-0.02074091 0.00651298 0.03851316");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("pos", "-0.01705376 0.01562052 0.02967040");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_11_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("pos", "-0.01100532 0.00889560 0.01905297");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("pos", "-0.01184447 0.02375397 0.02037774");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("pos", "-0.01148386 0.02516756 0.01951467");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("pos", "-0.01108943 0.02663788 0.01860916");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("pos", "-0.01066120 0.02816493 0.01766120");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("pos", "-0.01019916 0.02974871 0.01667080");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("pos", "-0.00970331 0.03138921 0.01563795");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("pos", "-0.00917365 0.03308644 0.01456265");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("pos", "-0.00861018 0.03484040 0.01344491");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("pos", "-0.00801291 0.03665109 0.01228472");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("pos", "-0.00738182 0.03851850 0.01108209");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("pos", "-0.00671693 0.04044264 0.00983702");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("pos", "-0.00601822 0.04242351 0.00854950");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("pos", "-0.00528571 0.04446111 0.00721953");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("pos", "-0.00451939 0.04655543 0.00584712");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("pos", "-0.00371926 0.04870648 0.00443226");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("pos", "-0.00288533 0.05091426 0.00297496");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("pos", "-0.00201758 0.05317877 0.00147521");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("pos", "-0.00111603 0.05550000 -0.00006699");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_11", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_11").selection("input").set("lobe_11_petal_seg_01", "lobe_11_petal_seg_02", "lobe_11_petal_seg_03", "lobe_11_petal_seg_04", "lobe_11_petal_seg_05", "lobe_11_petal_seg_06", "lobe_11_petal_seg_07", "lobe_11_petal_seg_08", "lobe_11_petal_wing", "lobe_11_posterior_cap", "lobe_11_duct_bead_01", "lobe_11_duct_bead_02", "lobe_11_duct_bead_03", "lobe_11_duct_bead_04", "lobe_11_duct_bead_05", "lobe_11_duct_bead_06", "lobe_11_duct_bead_07", "lobe_11_duct_bead_08", "lobe_11_duct_bead_09", "lobe_11_duct_bead_10", "lobe_11_duct_bead_11", "lobe_11_duct_bead_12", "lobe_11_duct_bead_13", "lobe_11_duct_bead_14", "lobe_11_duct_bead_15", "lobe_11_duct_bead_16", "lobe_11_duct_bead_17", "lobe_11_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_11").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_11").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_11").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe12(Model model) {


    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("pos", "-0.02141097 0.02041159 0.01240738");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("pos", "-0.02442072 0.01842607 0.01268251");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("pos", "-0.02728869 0.01644056 0.01320231");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("pos", "-0.02990119 0.01445504 0.01416296");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("pos", "-0.03219512 0.01246953 0.01567336");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("pos", "-0.03417048 0.01048401 0.01773351");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("pos", "-0.03589037 0.00849850 0.02023451");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("pos", "-0.03746848 0.00651298 0.02298017");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("pos", "-0.02983605 0.01562052 0.01718419");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_12_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("pos", "-0.01937740 0.00889560 0.01110809");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("pos", "-0.02070586 0.02374998 0.01179579");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("pos", "-0.01994986 0.02516005 0.01122475");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("pos", "-0.01914297 0.02662732 0.01063383");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("pos", "-0.01828520 0.02815179 0.01002302");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("pos", "-0.01737653 0.02973346 0.00939233");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("pos", "-0.01641698 0.03137232 0.00874176");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("pos", "-0.01540654 0.03306838 0.00807130");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("pos", "-0.01434520 0.03482163 0.00738096");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("pos", "-0.01323298 0.03663208 0.00667074");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("pos", "-0.01206988 0.03849973 0.00594063");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("pos", "-0.01085588 0.04042458 0.00519064");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("pos", "-0.00959099 0.04240662 0.00442077");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("pos", "-0.00827522 0.04444586 0.00363101");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("pos", "-0.00690855 0.04654229 0.00282137");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("pos", "-0.00549100 0.04869592 0.00199185");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("pos", "-0.00402256 0.05090675 0.00114244");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("pos", "-0.00250323 0.05317478 0.00027315");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("pos", "-0.00093301 0.05550000 -0.00061603");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_12", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_12").selection("input").set("lobe_12_petal_seg_01", "lobe_12_petal_seg_02", "lobe_12_petal_seg_03", "lobe_12_petal_seg_04", "lobe_12_petal_seg_05", "lobe_12_petal_seg_06", "lobe_12_petal_seg_07", "lobe_12_petal_seg_08", "lobe_12_petal_wing", "lobe_12_posterior_cap", "lobe_12_duct_bead_01", "lobe_12_duct_bead_02", "lobe_12_duct_bead_03", "lobe_12_duct_bead_04", "lobe_12_duct_bead_05", "lobe_12_duct_bead_06", "lobe_12_duct_bead_07", "lobe_12_duct_bead_08", "lobe_12_duct_bead_09", "lobe_12_duct_bead_10", "lobe_12_duct_bead_11", "lobe_12_duct_bead_12", "lobe_12_duct_bead_13", "lobe_12_duct_bead_14", "lobe_12_duct_bead_15", "lobe_12_duct_bead_16", "lobe_12_duct_bead_17", "lobe_12_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_12").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_12").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_12").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe13(Model model) {


    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("pos", "-0.02465833 0.02041159 -0.00005721");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("pos", "-0.02739743 0.01842607 -0.00133456");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("pos", "-0.03013719 0.01644056 -0.00232914");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("pos", "-0.03287813 0.01445504 -0.00281419");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("pos", "-0.03562054 0.01246953 -0.00266386");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("pos", "-0.03836442 0.01048401 -0.00187816");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("pos", "-0.04110949 0.00849850 -0.00058292");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("pos", "-0.04385522 0.00651298 0.00099508");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("pos", "-0.03436024 0.01562052 -0.00015420");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_13_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("pos", "-0.02230801 0.00889560 -0.00005120");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("pos", "-0.02377117 0.02375164 -0.00017777");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("pos", "-0.02283048 0.02516317 -0.00027749");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("pos", "-0.02183627 0.02663171 -0.00035609");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("pos", "-0.02078853 0.02815725 -0.00041356");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("pos", "-0.01968727 0.02973979 -0.00044990");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("pos", "-0.01853248 0.03137933 -0.00046512");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("pos", "-0.01732416 0.03307588 -0.00045922");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("pos", "-0.01606232 0.03482942 -0.00043219");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("pos", "-0.01474695 0.03663997 -0.00038403");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("pos", "-0.01337806 0.03850752 -0.00031475");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("pos", "-0.01195564 0.04043208 -0.00022434");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("pos", "-0.01047970 0.04241363 -0.00011281");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("pos", "-0.00895023 0.04445219 0.00001984");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("pos", "-0.00736723 0.04654775 0.00017362");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("pos", "-0.00573071 0.04870031 0.00034853");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("pos", "-0.00404067 0.05090987 0.00054456");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("pos", "-0.00229710 0.05317643 0.00076172");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("pos", "-0.00050000 0.05550000 0.00100000");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_13", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_13").selection("input").set("lobe_13_petal_seg_01", "lobe_13_petal_seg_02", "lobe_13_petal_seg_03", "lobe_13_petal_seg_04", "lobe_13_petal_seg_05", "lobe_13_petal_seg_06", "lobe_13_petal_seg_07", "lobe_13_petal_seg_08", "lobe_13_petal_wing", "lobe_13_posterior_cap", "lobe_13_duct_bead_01", "lobe_13_duct_bead_02", "lobe_13_duct_bead_03", "lobe_13_duct_bead_04", "lobe_13_duct_bead_05", "lobe_13_duct_bead_06", "lobe_13_duct_bead_07", "lobe_13_duct_bead_08", "lobe_13_duct_bead_09", "lobe_13_duct_bead_10", "lobe_13_duct_bead_11", "lobe_13_duct_bead_12", "lobe_13_duct_bead_13", "lobe_13_duct_bead_14", "lobe_13_duct_bead_15", "lobe_13_duct_bead_16", "lobe_13_duct_bead_17", "lobe_13_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_13").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_13").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_13").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe14(Model model) {


    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("pos", "-0.02129731 0.02041159 -0.01228439");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("pos", "-0.02303752 0.01842607 -0.01475541");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("pos", "-0.02491901 0.01644056 -0.01698150");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("pos", "-0.02705508 0.01445504 -0.01876621");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("pos", "-0.02950862 0.01246953 -0.02000053");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("pos", "-0.03227963 0.01048401 -0.02068448");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("pos", "-0.03530522 0.00849850 -0.02092705");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("pos", "-0.03847209 0.00651298 -0.02092468");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("pos", "-0.02968045 0.01562052 -0.01721508");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_14_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("pos", "-0.01926138 0.00889560 -0.01098961");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("pos", "-0.02046907 0.02375300 -0.01189474");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("pos", "-0.01960516 0.02516574 -0.01152014");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("pos", "-0.01870560 0.02663532 -0.01110038");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("pos", "-0.01777037 0.02816174 -0.01063545");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("pos", "-0.01679949 0.02974500 -0.01012536");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("pos", "-0.01579295 0.03138510 -0.00957010");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("pos", "-0.01475074 0.03308205 -0.00896967");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("pos", "-0.01367288 0.03483584 -0.00832408");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("pos", "-0.01255936 0.03664647 -0.00763332");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("pos", "-0.01141018 0.03851394 -0.00689739");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("pos", "-0.01022534 0.04043825 -0.00611629");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("pos", "-0.00900485 0.04241940 -0.00529003");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("pos", "-0.00774869 0.04445740 -0.00441861");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("pos", "-0.00645687 0.04655224 -0.00350201");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("pos", "-0.00512940 0.04870391 -0.00254025");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("pos", "-0.00376626 0.05091243 -0.00153333");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("pos", "-0.00236747 0.05317780 -0.00048123");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("pos", "-0.00093301 0.05550000 0.00061603");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_14", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_14").selection("input").set("lobe_14_petal_seg_01", "lobe_14_petal_seg_02", "lobe_14_petal_seg_03", "lobe_14_petal_seg_04", "lobe_14_petal_seg_05", "lobe_14_petal_seg_06", "lobe_14_petal_seg_07", "lobe_14_petal_seg_08", "lobe_14_petal_wing", "lobe_14_posterior_cap", "lobe_14_duct_bead_01", "lobe_14_duct_bead_02", "lobe_14_duct_bead_03", "lobe_14_duct_bead_04", "lobe_14_duct_bead_05", "lobe_14_duct_bead_06", "lobe_14_duct_bead_07", "lobe_14_duct_bead_08", "lobe_14_duct_bead_09", "lobe_14_duct_bead_10", "lobe_14_duct_bead_11", "lobe_14_duct_bead_12", "lobe_14_duct_bead_13", "lobe_14_duct_bead_14", "lobe_14_duct_bead_15", "lobe_14_duct_bead_16", "lobe_14_duct_bead_17", "lobe_14_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_14").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_14").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_14").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe15(Model model) {


    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("pos", "-0.01237178 0.02041159 -0.02134047");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("pos", "-0.01264747 0.01842607 -0.02435017");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("pos", "-0.01316779 0.01644056 -0.02721804");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("pos", "-0.01412893 0.01445504 -0.02983037");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("pos", "-0.01563975 0.01246953 -0.03212402");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("pos", "-0.01770026 0.01048401 -0.03409900");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("pos", "-0.02020158 0.00849850 -0.03581843");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("pos", "-0.02294753 0.00651298 -0.03739603");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("pos", "-0.01719077 0.01562052 -0.02978966");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_15_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("pos", "-0.01119298 0.00889560 -0.01909858");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("pos", "-0.01182035 0.02375147 -0.02054115");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("pos", "-0.01126022 0.02516285 -0.01978864");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("pos", "-0.01069141 0.02663126 -0.01897866");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("pos", "-0.01011390 0.02815669 -0.01811121");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("pos", "-0.00952769 0.02973914 -0.01718630");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("pos", "-0.00893280 0.03137862 -0.01620392");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("pos", "-0.00832921 0.03307511 -0.01516407");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("pos", "-0.00771693 0.03482863 -0.01406676");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("pos", "-0.00709596 0.03663917 -0.01291199");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("pos", "-0.00646629 0.03850673 -0.01169974");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("pos", "-0.00582793 0.04043131 -0.01043003");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("pos", "-0.00518088 0.04241292 -0.00910286");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("pos", "-0.00452514 0.04445154 -0.00771822");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("pos", "-0.00386070 0.04654719 -0.00627611");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("pos", "-0.00318757 0.04869986 -0.00477653");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("pos", "-0.00250575 0.05090955 -0.00321949");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("pos", "-0.00181523 0.05317627 -0.00160499");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("pos", "-0.00111603 0.05550000 0.00006699");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_15", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_15").selection("input").set("lobe_15_petal_seg_01", "lobe_15_petal_seg_02", "lobe_15_petal_seg_03", "lobe_15_petal_seg_04", "lobe_15_petal_seg_05", "lobe_15_petal_seg_06", "lobe_15_petal_seg_07", "lobe_15_petal_seg_08", "lobe_15_petal_wing", "lobe_15_posterior_cap", "lobe_15_duct_bead_01", "lobe_15_duct_bead_02", "lobe_15_duct_bead_03", "lobe_15_duct_bead_04", "lobe_15_duct_bead_05", "lobe_15_duct_bead_06", "lobe_15_duct_bead_07", "lobe_15_duct_bead_08", "lobe_15_duct_bead_09", "lobe_15_duct_bead_10", "lobe_15_duct_bead_11", "lobe_15_duct_bead_12", "lobe_15_duct_bead_13", "lobe_15_duct_bead_14", "lobe_15_duct_bead_15", "lobe_15_duct_bead_16", "lobe_15_duct_bead_17", "lobe_15_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_15").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_15").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_15").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe16(Model model) {


    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("pos", "0.00001517 0.02041159 -0.02463267");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("pos", "0.00128785 0.01842607 -0.02737395");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("pos", "0.00227776 0.01644056 -0.03011539");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("pos", "0.00275814 0.01445504 -0.03285715");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("pos", "0.00260314 0.01246953 -0.03559930");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("pos", "0.00181276 0.01048401 -0.03834185");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("pos", "0.00051285 0.00849850 -0.04108470");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("pos", "-0.00106983 0.00651298 -0.04382774");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("pos", "0.00005995 0.01562052 -0.03433616");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_16_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("pos", "0.00001372 0.00889560 -0.02204128");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("pos", "0.00013747 0.02375212 -0.02363868");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("pos", "0.00023875 0.02516409 -0.02271104");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("pos", "0.00031901 0.02663299 -0.02172921");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("pos", "0.00037824 0.02815885 -0.02069320");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("pos", "0.00041645 0.02974164 -0.01960301");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("pos", "0.00043363 0.03138139 -0.01845864");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("pos", "0.00042979 0.03307808 -0.01726009");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("pos", "0.00040493 0.03483171 -0.01600735");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("pos", "0.00035904 0.03664229 -0.01470044");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("pos", "0.00029213 0.03850981 -0.01333934");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("pos", "0.00020420 0.04043427 -0.01192406");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("pos", "0.00009524 0.04241569 -0.01045459");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("pos", "-0.00003474 0.04445404 -0.00893095");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("pos", "-0.00018574 0.04654935 -0.00735312");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("pos", "-0.00035777 0.04870159 -0.00572112");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("pos", "-0.00055083 0.05091078 -0.00403493");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("pos", "-0.00076490 0.05317692 -0.00229455");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("pos", "-0.00100000 0.05550000 -0.00050000");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_16", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_16").selection("input").set("lobe_16_petal_seg_01", "lobe_16_petal_seg_02", "lobe_16_petal_seg_03", "lobe_16_petal_seg_04", "lobe_16_petal_seg_05", "lobe_16_petal_seg_06", "lobe_16_petal_seg_07", "lobe_16_petal_seg_08", "lobe_16_petal_wing", "lobe_16_posterior_cap", "lobe_16_duct_bead_01", "lobe_16_duct_bead_02", "lobe_16_duct_bead_03", "lobe_16_duct_bead_04", "lobe_16_duct_bead_05", "lobe_16_duct_bead_06", "lobe_16_duct_bead_07", "lobe_16_duct_bead_08", "lobe_16_duct_bead_09", "lobe_16_duct_bead_10", "lobe_16_duct_bead_11", "lobe_16_duct_bead_12", "lobe_16_duct_bead_13", "lobe_16_duct_bead_14", "lobe_16_duct_bead_15", "lobe_16_duct_bead_16", "lobe_16_duct_bead_17", "lobe_16_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_16").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_16").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_16").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe17(Model model) {


    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("pos", "0.01237224 0.02041159 -0.02132769");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("pos", "0.01484756 0.01842607 -0.02306179");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("pos", "0.01707828 0.01644056 -0.02493777");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("pos", "0.01886826 0.01445504 -0.02706943");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("pos", "0.02010865 0.01246953 -0.02951991");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("pos", "0.02079944 0.01048401 -0.03228922");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("pos", "0.02104948 0.00849850 -0.03531420");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("pos", "0.02105493 0.00651298 -0.03848107");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("pos", "0.01726503 0.01562052 -0.02968172");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_17_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("pos", "0.01119288 0.00889560 -0.01908615");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("pos", "0.01203351 0.02375167 -0.02040591");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("pos", "0.01164973 0.02516324 -0.01955196");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("pos", "0.01122092 0.02663180 -0.01866156");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("pos", "0.01074707 0.02815737 -0.01773473");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("pos", "0.01022818 0.02973993 -0.01677145");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("pos", "0.00966425 0.03137948 -0.01577174");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("pos", "0.00905528 0.03307604 -0.01473559");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("pos", "0.00840127 0.03482959 -0.01366300");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("pos", "0.00770222 0.03664014 -0.01255398");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("pos", "0.00695813 0.03850769 -0.01140851");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("pos", "0.00616900 0.04043224 -0.01022661");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("pos", "0.00533483 0.04241378 -0.00900827");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("pos", "0.00445562 0.04445232 -0.00775349");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("pos", "0.00353137 0.04654786 -0.00646227");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("pos", "0.00256208 0.04870040 -0.00513461");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("pos", "0.00154775 0.05090994 -0.00377052");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("pos", "0.00048838 0.05317647 -0.00236998");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("pos", "-0.00061603 0.05550000 -0.00093301");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_17", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_17").selection("input").set("lobe_17_petal_seg_01", "lobe_17_petal_seg_02", "lobe_17_petal_seg_03", "lobe_17_petal_seg_04", "lobe_17_petal_seg_05", "lobe_17_petal_seg_06", "lobe_17_petal_seg_07", "lobe_17_petal_seg_08", "lobe_17_petal_wing", "lobe_17_posterior_cap", "lobe_17_duct_bead_01", "lobe_17_duct_bead_02", "lobe_17_duct_bead_03", "lobe_17_duct_bead_04", "lobe_17_duct_bead_05", "lobe_17_duct_bead_06", "lobe_17_duct_bead_07", "lobe_17_duct_bead_08", "lobe_17_duct_bead_09", "lobe_17_duct_bead_10", "lobe_17_duct_bead_11", "lobe_17_duct_bead_12", "lobe_17_duct_bead_13", "lobe_17_duct_bead_14", "lobe_17_duct_bead_15", "lobe_17_duct_bead_16", "lobe_17_duct_bead_17", "lobe_17_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_17").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_17").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_17").set("selresultshow", "all");

  }

  private static void buildAnatomicalLobe18(Model model) {


    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("semiaxes", "0.00689431 0.00675369 0.01350150");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("pos", "0.02139509 0.02041159 -0.01234935");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("pos", "0.02440530 0.01842607 -0.01261934");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("pos", "0.02727416 0.01644056 -0.01313423");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("pos", "0.02988830 0.01445504 -0.01409042");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("pos", "0.03218481 0.01246953 -0.01559689");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("pos", "0.03416369 0.01048401 -0.01765365");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("pos", "0.03588785 0.00849850 -0.02015171");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("pos", "0.03747065 0.00651298 -0.02289467");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("pos", "0.02982078 0.01562052 -0.01712184");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_18_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("pos", "0.01935951 0.00889560 -0.01105390");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("pos", "0.02068712 0.02375079 -0.01174513");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("pos", "0.01992217 0.02516157 -0.01119213");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("pos", "0.01910023 0.02662946 -0.01063010");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("pos", "0.01822131 0.02815445 -0.01005904");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("pos", "0.01728541 0.02973654 -0.00947895");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("pos", "0.01629252 0.03137574 -0.00888983");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("pos", "0.01524264 0.03307203 -0.00829168");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("pos", "0.01413578 0.03482543 -0.00768450");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("pos", "0.01297194 0.03663593 -0.00706829");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("pos", "0.01175111 0.03850353 -0.00644305");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("pos", "0.01047329 0.04042823 -0.00580878");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("pos", "0.00913849 0.04241004 -0.00516547");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("pos", "0.00774671 0.04444894 -0.00451314");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("pos", "0.00629794 0.04654495 -0.00385178");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("pos", "0.00479219 0.04869806 -0.00318139");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("pos", "0.00322945 0.05090827 -0.00250196");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("pos", "0.00160972 0.05317558 -0.00181351");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("pos", "-0.00006699 0.05550000 -0.00111603");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("selresultshow", "all");



    model.component("comp1").geom("geom1").create("anatomical_lobe_18", "Union");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_18").selection("input").set("lobe_18_petal_seg_01", "lobe_18_petal_seg_02", "lobe_18_petal_seg_03", "lobe_18_petal_seg_04", "lobe_18_petal_seg_05", "lobe_18_petal_seg_06", "lobe_18_petal_seg_07", "lobe_18_petal_seg_08", "lobe_18_petal_wing", "lobe_18_posterior_cap", "lobe_18_duct_bead_01", "lobe_18_duct_bead_02", "lobe_18_duct_bead_03", "lobe_18_duct_bead_04", "lobe_18_duct_bead_05", "lobe_18_duct_bead_06", "lobe_18_duct_bead_07", "lobe_18_duct_bead_08", "lobe_18_duct_bead_09", "lobe_18_duct_bead_10", "lobe_18_duct_bead_11", "lobe_18_duct_bead_12", "lobe_18_duct_bead_13", "lobe_18_duct_bead_14", "lobe_18_duct_bead_15", "lobe_18_duct_bead_16", "lobe_18_duct_bead_17", "lobe_18_duct_bead_18");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_18").set("intbnd", "off");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_18").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("anatomical_lobe_18").set("selresultshow", "all");

  }

}
