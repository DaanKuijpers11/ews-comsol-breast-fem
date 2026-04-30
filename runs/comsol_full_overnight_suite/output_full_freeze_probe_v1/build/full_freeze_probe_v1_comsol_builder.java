import com.comsol.model.*;
import com.comsol.model.util.*;

public class full_freeze_probe_v1_comsol_builder {
  public static Model run() {
    ModelUtil.initStandalone(true);
    Model model = ModelUtil.create("Model");
    model.label("full_freeze_probe_v1_generated.mph");
    model.modelPath("C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_freeze_probe_v1/build");

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
    // - Build plan JSON: C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_freeze_probe_v1/prepare/full_freeze_probe_v1_comsol_build_plan.json
    // - Selection hints JSON: C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_freeze_probe_v1/build/full_freeze_probe_v1_comsol_selection_hints.json
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
    model.save("C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_freeze_probe_v1/build/full_freeze_probe_v1_generated.mph");
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
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("pos", "0.01109420 0.03953753 0.00004679");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("pos", "0.01346107 0.03770945 0.00106527");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("pos", "0.01582889 0.03588136 0.00186110");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("pos", "0.01819840 0.03405327 0.00225573");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("pos", "0.02057001 0.03222519 0.00215007");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("pos", "0.02294374 0.03039710 0.00154411");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("pos", "0.02531916 0.02856902 0.00053695");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("pos", "0.02769552 0.02674093 -0.00069286");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("pos", "0.02074818 0.03502571 0.00019441");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_01_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("pos", "0.00866523 0.02893463 0.00003555");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("pos", "0.01024009 0.04276183 0.00011808");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("pos", "0.00941591 0.04410578 0.00018504");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("pos", "0.00862166 0.04539745 0.00024718");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("pos", "0.00785735 0.04663685 0.00030449");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("pos", "0.00712298 0.04782398 0.00035697");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("pos", "0.00641853 0.04895884 0.00040463");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("pos", "0.00574402 0.05004143 0.00044746");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("pos", "0.00509944 0.05107175 0.00048546");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("pos", "0.00448480 0.05204980 0.00051863");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("pos", "0.00390009 0.05297557 0.00054698");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("pos", "0.00334531 0.05384907 0.00057050");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("pos", "0.00282047 0.05467030 0.00058920");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("pos", "0.00232556 0.05543927 0.00060307");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("pos", "0.00186058 0.05615596 0.00061211");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("pos", "0.00142553 0.05682037 0.00061632");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("pos", "0.00102042 0.05743252 0.00061571");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("pos", "0.00064524 0.05799240 0.00061027");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("pos", "0.00030000 0.05850000 0.00060000");
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
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("pos", "0.00558961 0.03953753 0.00949895");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("pos", "0.00592298 0.03770945 0.01205399");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("pos", "0.00644824 0.03588136 0.01449612");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("pos", "0.00731929 0.03405327 0.01673478");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("pos", "0.00862152 0.03222519 0.01871971");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("pos", "0.01035493 0.03039710 0.02045091");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("pos", "0.01243412 0.02856902 0.02197864");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("pos", "0.01470521 0.02674093 0.02339344");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("pos", "0.01036216 0.03502571 0.01792896");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_02_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("pos", "0.00435774 0.02893463 0.00720128");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("pos", "0.00509322 0.04276461 0.00871266");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("pos", "0.00461627 0.04411101 0.00805123");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("pos", "0.00415878 0.04540481 0.00741252");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("pos", "0.00372073 0.04664602 0.00679655");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("pos", "0.00330214 0.04783462 0.00620332");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("pos", "0.00290300 0.04897062 0.00563281");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("pos", "0.00252330 0.05005403 0.00508503");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("pos", "0.00216306 0.05108484 0.00455999");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("pos", "0.00182227 0.05206305 0.00405768");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("pos", "0.00150092 0.05298866 0.00357810");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("pos", "0.00119903 0.05386167 0.00312125");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("pos", "0.00091659 0.05468208 0.00268714");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("pos", "0.00065360 0.05544990 0.00227575");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("pos", "0.00041005 0.05616512 0.00188710");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("pos", "0.00018596 0.05682774 0.00152118");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("pos", "-0.00001868 0.05743776 0.00117799");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("pos", "-0.00020387 0.05799518 0.00085753");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("pos", "-0.00036962 0.05850000 0.00055981");
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
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("pos", "-0.00552700 0.03953753 0.00958580");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("pos", "-0.00758507 0.03770945 0.01113622");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("pos", "-0.00945024 0.03588136 0.01279786");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("pos", "-0.01096784 0.03405327 0.01465989");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("pos", "-0.01205203 0.03222519 0.01677183");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("pos", "-0.01270281 0.03039710 0.01913366");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("pos", "-0.01300601 0.02856902 0.02169590");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("pos", "-0.01311633 0.02674093 0.02436935");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("pos", "-0.01043306 0.03502571 0.01788248");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_03_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("pos", "-0.00431372 0.02893463 0.00727625");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("pos", "-0.00516636 0.04276295 0.00871799");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("pos", "-0.00481639 0.04410788 0.00798060");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("pos", "-0.00447710 0.04540041 0.00727099");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("pos", "-0.00414850 0.04664053 0.00658915");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("pos", "-0.00383058 0.04782826 0.00593508");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("pos", "-0.00352334 0.04896358 0.00530879");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("pos", "-0.00322678 0.05004649 0.00471028");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("pos", "-0.00294090 0.05107701 0.00413954");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("pos", "-0.00266570 0.05205512 0.00359658");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("pos", "-0.00240119 0.05298083 0.00308139");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("pos", "-0.00214736 0.05385413 0.00259398");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("pos", "-0.00190421 0.05467504 0.00213434");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("pos", "-0.00167174 0.05544354 0.00170248");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("pos", "-0.00144995 0.05615964 0.00129840");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("pos", "-0.00123884 0.05682333 0.00092209");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("pos", "-0.00103842 0.05743462 0.00057355");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("pos", "-0.00084868 0.05799351 0.00025279");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("pos", "-0.00066962 0.05850000 -0.00004019");
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
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("pos", "-0.01113587 0.03953753 0.00004853");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("pos", "-0.01351139 0.03770945 -0.00094962");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("pos", "-0.01588595 0.03588136 -0.00172512");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("pos", "-0.01825875 0.03405327 -0.00209941");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("pos", "-0.02062938 0.03222519 -0.00197342");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("pos", "-0.02299782 0.03039710 -0.00134713");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("pos", "-0.02536452 0.02856902 -0.00031963");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("pos", "-0.02773024 0.02674093 0.00093051");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("pos", "-0.02079819 0.03502571 -0.00007827");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_04_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("pos", "-0.00870691 0.02893463 0.00003691");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("pos", "-0.01027957 0.04276024 -0.00002724");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("pos", "-0.00945320 0.04410278 -0.00009003");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("pos", "-0.00865674 0.04539324 -0.00014036");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("pos", "-0.00789020 0.04663161 -0.00017822");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("pos", "-0.00715357 0.04781790 -0.00020363");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("pos", "-0.00644687 0.04895210 -0.00021658");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("pos", "-0.00577008 0.05003422 -0.00021706");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("pos", "-0.00512321 0.05106426 -0.00020509");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("pos", "-0.00450626 0.05204221 -0.00018065");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("pos", "-0.00391922 0.05296808 -0.00014376");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("pos", "-0.00336211 0.05384186 -0.00009440");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("pos", "-0.00283491 0.05466356 -0.00003258");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("pos", "-0.00233763 0.05543318 0.00004170");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("pos", "-0.00187027 0.05615071 0.00012844");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("pos", "-0.00143282 0.05681616 0.00022764");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("pos", "-0.00102530 0.05742952 0.00033930");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("pos", "-0.00064769 0.05799080 0.00046342");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("pos", "-0.00030000 0.05850000 0.00060000");
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
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("pos", "-0.00552839 0.03953753 -0.00955346");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("pos", "-0.00584314 0.03770945 -0.01211087");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("pos", "-0.00635060 0.03588136 -0.01455676");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("pos", "-0.00720531 0.03405327 -0.01680171");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("pos", "-0.00849304 0.03222519 -0.01879607");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("pos", "-0.01021380 0.03039710 -0.02053985");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("pos", "-0.01228181 0.02856902 -0.02208268");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("pos", "-0.01454253 0.02674093 -0.02351400");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("pos", "-0.01028209 0.03502571 -0.01798599");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_05_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("pos", "-0.00431181 0.02893463 -0.00724600");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("pos", "-0.00504008 0.04276399 -0.00876110");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("pos", "-0.00457746 0.04410985 -0.00809041");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("pos", "-0.00414054 0.04540317 -0.00743884");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("pos", "-0.00372930 0.04664397 -0.00680640");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("pos", "-0.00334376 0.04783225 -0.00619307");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("pos", "-0.00298390 0.04896800 -0.00559887");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("pos", "-0.00264974 0.05005122 -0.00502378");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("pos", "-0.00234127 0.05108192 -0.00446782");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("pos", "-0.00205850 0.05206009 -0.00393097");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("pos", "-0.00180141 0.05298574 -0.00341325");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("pos", "-0.00157001 0.05385886 -0.00291465");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("pos", "-0.00136431 0.05467946 -0.00243517");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("pos", "-0.00118430 0.05544753 -0.00197481");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("pos", "-0.00102998 0.05616307 -0.00153357");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("pos", "-0.00090135 0.05682609 -0.00111144");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("pos", "-0.00079841 0.05743659 -0.00070845");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("pos", "-0.00072117 0.05799456 -0.00032457");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("pos", "-0.00066962 0.05850000 0.00004019");
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
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("pos", "0.00548469 0.03953753 -0.00965236");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("pos", "0.00753295 0.03770945 -0.01121570");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("pos", "0.00938762 0.03588136 -0.01288905");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("pos", "0.01089347 0.03405327 -0.01476061");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("pos", "0.01196434 0.03222519 -0.01687933");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("pos", "0.01260023 0.03039710 -0.01924521");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("pos", "0.01288729 0.02856902 -0.02180931");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("pos", "0.01298078 0.02674093 -0.02448340");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("pos", "0.01038051 0.03502571 -0.01796220");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_06_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("pos", "0.00428468 0.02893463 -0.00733446");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("pos", "0.00512450 0.04276155 -0.00878174");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("pos", "0.00476842 0.04410524 -0.00804570");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("pos", "0.00441645 0.04539669 -0.00734120");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("pos", "0.00406860 0.04663591 -0.00666825");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("pos", "0.00372486 0.04782289 -0.00602686");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("pos", "0.00338523 0.04895763 -0.00541701");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("pos", "0.00304971 0.05004013 -0.00483872");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("pos", "0.00271830 0.05107040 -0.00429197");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("pos", "0.00239101 0.05204843 -0.00377678");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("pos", "0.00206782 0.05297422 -0.00329314");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("pos", "0.00174875 0.05384778 -0.00284104");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("pos", "0.00143379 0.05466909 -0.00242050");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("pos", "0.00112294 0.05543817 -0.00203151");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("pos", "0.00081621 0.05615501 -0.00167407");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("pos", "0.00051358 0.05681961 -0.00134818");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("pos", "0.00021507 0.05743198 -0.00105384");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("pos", "-0.00007933 0.05799211 -0.00079105");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("pos", "-0.00036962 0.05850000 -0.00055981");
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
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("pos", "0.02471643 0.02441159 -0.00001283");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("pos", "0.02745914 0.02242607 0.00125674");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("pos", "0.03020171 0.02044056 0.00224354");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("pos", "0.03294402 0.01845504 0.00272080");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("pos", "0.03568599 0.01646953 0.00256269");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("pos", "0.03842763 0.01448401 0.00176920");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("pos", "0.04116901 0.01249850 0.00046618");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("pos", "0.04391025 0.01051298 -0.00111961");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("pos", "0.03442101 0.01962052 0.00007750");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_07_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("pos", "0.02236610 0.01289560 -0.00001148");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("pos", "0.02361522 0.02796402 0.00011680");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("pos", "0.02248530 0.02955642 0.00023775");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("pos", "0.02132664 0.03117430 0.00035007");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("pos", "0.02013927 0.03281765 0.00045377");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("pos", "0.01892317 0.03448649 0.00054884");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("pos", "0.01767834 0.03618080 0.00063529");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("pos", "0.01640479 0.03790060 0.00071311");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("pos", "0.01510252 0.03964588 0.00078232");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("pos", "0.01377153 0.04141663 0.00084289");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("pos", "0.01241181 0.04321286 0.00089485");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("pos", "0.01102337 0.04503458 0.00093817");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("pos", "0.00960620 0.04688177 0.00097288");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("pos", "0.00816031 0.04875444 0.00099896");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("pos", "0.00668569 0.05065260 0.00101642");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("pos", "0.00518236 0.05257623 0.00102525");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("pos", "0.00365029 0.05452534 0.00102546");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("pos", "0.00208951 0.05649993 0.00101704");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("pos", "0.00050000 0.05850000 0.00100000");
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
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("pos", "0.02129617 0.02441159 0.01231190");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("pos", "0.02303393 0.02242607 0.01478465");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("pos", "0.02491321 0.02044056 0.01701260");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("pos", "0.02704751 0.01845504 0.01879943");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("pos", "0.02949983 0.01646953 0.02003619");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("pos", "0.03227015 0.01448401 0.02072288");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("pos", "0.03529550 0.01249850 0.02096846");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("pos", "0.03846237 0.01051298 0.02096923");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("pos", "0.02967734 0.01962052 0.01724413");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_08_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("pos", "0.01926141 0.01289560 0.01101490");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("pos", "0.02028142 0.02796671 0.01182047");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("pos", "0.01924642 0.02956147 0.01136730");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("pos", "0.01819118 0.03118140 0.01089206");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("pos", "0.01711569 0.03282649 0.01039477");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("pos", "0.01601996 0.03449675 0.00987541");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("pos", "0.01490397 0.03619217 0.00933399");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("pos", "0.01376775 0.03791276 0.00877050");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("pos", "0.01261127 0.03965851 0.00818495");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("pos", "0.01143456 0.04142942 0.00757734");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("pos", "0.01023759 0.04322549 0.00694767");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("pos", "0.00902038 0.04504673 0.00629593");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("pos", "0.00778292 0.04689314 0.00562213");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("pos", "0.00652522 0.04876471 0.00492627");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("pos", "0.00524727 0.05066144 0.00420835");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("pos", "0.00394907 0.05258333 0.00346836");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("pos", "0.00263063 0.05453039 0.00270631");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("pos", "0.00129195 0.05650261 0.00192220");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("pos", "-0.00006699 0.05850000 0.00111603");
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
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("pos", "0.01229593 0.02441159 0.02138559");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("pos", "0.01256085 0.02242607 0.02439626");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("pos", "0.01307091 0.02044056 0.02726597");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("pos", "0.01402270 0.01845504 0.02988172");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("pos", "0.01552531 0.01646953 0.03218076");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("pos", "0.01757873 0.01448401 0.03416310");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("pos", "0.02007389 0.01249850 0.03589146");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("pos", "0.02281418 0.01051298 0.03747888");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("pos", "0.01710596 0.01962052 0.02983592");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_09_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("pos", "0.01112441 0.01289560 0.01913906");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("pos", "0.01163602 0.02796512 0.02040352");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("pos", "0.01096935 0.02955848 0.01949619");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("pos", "0.01029593 0.03117720 0.01855911");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("pos", "0.00961575 0.03282126 0.01759228");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("pos", "0.00892881 0.03449068 0.01659571");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("pos", "0.00823513 0.03618545 0.01556938");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("pos", "0.00753468 0.03790556 0.01451330");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("pos", "0.00682749 0.03965103 0.01342748");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("pos", "0.00611353 0.04142185 0.01231190");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("pos", "0.00539282 0.04321802 0.01116658");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("pos", "0.00466536 0.04503954 0.00999151");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("pos", "0.00393114 0.04688641 0.00878668");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("pos", "0.00319017 0.04875863 0.00755211");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("pos", "0.00244244 0.05065620 0.00628779");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("pos", "0.00168796 0.05257913 0.00499372");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("pos", "0.00092672 0.05452740 0.00366990");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("pos", "0.00015872 0.05650103 0.00231633");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("pos", "-0.00061603 0.05850000 0.00093301");
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
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("pos", "0.00002536 0.02441159 0.02468241");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("pos", "-0.00124281 0.02242607 0.02742577");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("pos", "-0.00222822 0.02044056 0.03016884");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("pos", "-0.00270409 0.01845504 0.03291139");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("pos", "-0.00254458 0.01646953 0.03565328");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("pos", "-0.00174970 0.01448401 0.03839452");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("pos", "-0.00044528 0.01249850 0.04113524");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("pos", "0.00114190 0.01051298 0.04387566");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("pos", "-0.00001556 0.01962052 0.03438752");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_10_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("pos", "0.00002294 0.01289560 0.02209102");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("pos", "-0.00010477 0.02796480 0.02347473");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("pos", "-0.00022629 0.02955788 0.02235822");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("pos", "-0.00033920 0.03117635 0.02121234");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("pos", "-0.00044351 0.03282021 0.02003709");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("pos", "-0.00053921 0.03448946 0.01883247");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("pos", "-0.00062630 0.03618410 0.01759849");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("pos", "-0.00070478 0.03790412 0.01633514");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("pos", "-0.00077465 0.03964953 0.01504242");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("pos", "-0.00083592 0.04142033 0.01372033");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("pos", "-0.00088858 0.04321652 0.01236888");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("pos", "-0.00093264 0.04503810 0.01098805");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("pos", "-0.00096808 0.04688506 0.00957786");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("pos", "-0.00099492 0.04875741 0.00813830");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("pos", "-0.00101315 0.05065515 0.00666938");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("pos", "-0.00102277 0.05257828 0.00517109");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("pos", "-0.00102379 0.05452680 0.00364342");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("pos", "-0.00101620 0.05650071 0.00208640");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("pos", "-0.00100000 0.05850000 0.00050000");
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
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("pos", "-0.01217128 0.02441159 0.02130302");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("pos", "-0.01463513 0.02242607 0.02305336");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("pos", "-0.01685346 0.02044056 0.02494399");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("pos", "-0.01862938 0.01845504 0.02708738");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("pos", "-0.01985361 0.01646953 0.02954597");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("pos", "-0.02052616 0.01448401 0.03231976");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("pos", "-0.02075630 0.01249850 0.03534632");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("pos", "-0.02074091 0.01051298 0.03851316");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("pos", "-0.01705376 0.01962052 0.02967040");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_11_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("pos", "-0.01100532 0.01289560 0.01905297");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("pos", "-0.01173942 0.02796817 0.02019387");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("pos", "-0.01128611 0.02956423 0.01916857");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("pos", "-0.01081136 0.03118528 0.01812245");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("pos", "-0.01031515 0.03283133 0.01705552");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("pos", "-0.00979749 0.03450236 0.01596777");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("pos", "-0.00925839 0.03619838 0.01485921");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("pos", "-0.00869783 0.03791940 0.01372984");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("pos", "-0.00811583 0.03966541 0.01257965");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("pos", "-0.00751237 0.04143641 0.01140865");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("pos", "-0.00688746 0.04323240 0.01021684");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("pos", "-0.00624111 0.04505338 0.00900421");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("pos", "-0.00557330 0.04689935 0.00777076");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("pos", "-0.00488405 0.04877031 0.00651651");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("pos", "-0.00417334 0.05066627 0.00524144");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("pos", "-0.00344119 0.05258721 0.00394555");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("pos", "-0.00268758 0.05453315 0.00262885");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("pos", "-0.00191253 0.05650408 0.00129134");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("pos", "-0.00111603 0.05850000 -0.00006699");
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
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("pos", "-0.02141097 0.02441159 0.01240738");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("pos", "-0.02442072 0.02242607 0.01268251");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("pos", "-0.02728869 0.02044056 0.01320231");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("pos", "-0.02990119 0.01845504 0.01416296");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("pos", "-0.03219512 0.01646953 0.01567336");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("pos", "-0.03417048 0.01448401 0.01773351");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("pos", "-0.03589037 0.01249850 0.02023451");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("pos", "-0.03746848 0.01051298 0.02298017");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("pos", "-0.02983605 0.01962052 0.01718419");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_12_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("pos", "-0.01937740 0.01289560 0.01110809");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("pos", "-0.02052189 0.02796334 0.01168918");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("pos", "-0.01960357 0.02955514 0.01102408");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("pos", "-0.01865600 0.03117250 0.01035163");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("pos", "-0.01767918 0.03281542 0.00967184");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("pos", "-0.01667312 0.03448389 0.00898472");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("pos", "-0.01563782 0.03617793 0.00829025");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("pos", "-0.01457326 0.03789753 0.00758843");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("pos", "-0.01347947 0.03964268 0.00687928");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("pos", "-0.01235643 0.04141339 0.00616279");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("pos", "-0.01120414 0.04320967 0.00543895");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("pos", "-0.01002261 0.04503150 0.00470777");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("pos", "-0.00881183 0.04687889 0.00396926");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("pos", "-0.00757181 0.04875185 0.00322340");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("pos", "-0.00630254 0.05065036 0.00247019");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("pos", "-0.00500403 0.05257443 0.00170965");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("pos", "-0.00367627 0.05452406 0.00094177");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("pos", "-0.00231926 0.05649925 0.00016654");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("pos", "-0.00093301 0.05850000 -0.00061603");
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
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("pos", "-0.02465833 0.02441159 -0.00005721");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("pos", "-0.02739743 0.02242607 -0.00133456");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("pos", "-0.03013719 0.02044056 -0.00232914");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("pos", "-0.03287813 0.01845504 -0.00281419");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("pos", "-0.03562054 0.01646953 -0.00266386");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("pos", "-0.03836442 0.01448401 -0.00187816");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("pos", "-0.04110949 0.01249850 -0.00058292");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("pos", "-0.04385522 0.01051298 0.00099508");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("pos", "-0.03436024 0.01962052 -0.00015420");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_13_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("pos", "-0.02230801 0.01289560 -0.00005120");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("pos", "-0.02355890 0.02796535 -0.00017728");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("pos", "-0.02243092 0.02955891 -0.00027657");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("pos", "-0.02127439 0.03117781 -0.00035478");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("pos", "-0.02008930 0.03282202 -0.00041194");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("pos", "-0.01887566 0.03449156 -0.00044802");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("pos", "-0.01763346 0.03618642 -0.00046304");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("pos", "-0.01636271 0.03790661 -0.00045699");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("pos", "-0.01506341 0.03965211 -0.00042987");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("pos", "-0.01373556 0.04142295 -0.00038168");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("pos", "-0.01237915 0.04321910 -0.00031243");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("pos", "-0.01099420 0.04504058 -0.00022211");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("pos", "-0.00958068 0.04688739 -0.00011073");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("pos", "-0.00813862 0.04875951 0.00002173");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("pos", "-0.00666800 0.05065696 0.00017525");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("pos", "-0.00516883 0.05257974 0.00034983");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("pos", "-0.00364111 0.05452783 0.00054549");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("pos", "-0.00208483 0.05650126 0.00076221");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("pos", "-0.00050000 0.05850000 0.00100000");
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
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("pos", "-0.02129731 0.02441159 -0.01228439");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("pos", "-0.02303752 0.02242607 -0.01475541");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("pos", "-0.02491901 0.02044056 -0.01698150");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("pos", "-0.02705508 0.01845504 -0.01876621");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("pos", "-0.02950862 0.01646953 -0.02000053");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("pos", "-0.03227963 0.01448401 -0.02068448");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("pos", "-0.03530522 0.01249850 -0.02092705");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("pos", "-0.03847209 0.01051298 -0.02092468");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("pos", "-0.02968045 0.01962052 -0.01721508");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_14_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("pos", "-0.01926138 0.01289560 -0.01098961");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("pos", "-0.02028545 0.02796700 -0.01178883");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("pos", "-0.01925953 0.02956202 -0.01132078");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("pos", "-0.01821955 0.03118217 -0.01082003");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("pos", "-0.01716552 0.03282746 -0.01028657");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("pos", "-0.01609742 0.03449787 -0.00972040");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("pos", "-0.01501527 0.03619341 -0.00912153");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("pos", "-0.01391907 0.03791408 -0.00848996");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("pos", "-0.01280880 0.03965988 -0.00782567");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("pos", "-0.01168448 0.04143081 -0.00712868");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("pos", "-0.01054610 0.04322687 -0.00639898");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("pos", "-0.00939367 0.04504806 -0.00563658");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("pos", "-0.00822717 0.04689437 -0.00484147");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("pos", "-0.00704662 0.04876582 -0.00401365");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("pos", "-0.00585202 0.05066240 -0.00315313");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("pos", "-0.00464335 0.05258410 -0.00225990");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("pos", "-0.00342063 0.05453094 -0.00133397");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("pos", "-0.00218385 0.05650291 -0.00037532");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("pos", "-0.00093301 0.05850000 0.00061603");
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
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("pos", "-0.01237178 0.02441159 -0.02134047");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("pos", "-0.01264747 0.02242607 -0.02435017");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("pos", "-0.01316779 0.02044056 -0.02721804");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("pos", "-0.01412893 0.01845504 -0.02983037");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("pos", "-0.01563975 0.01646953 -0.03212402");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("pos", "-0.01770026 0.01448401 -0.03409900");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("pos", "-0.02020158 0.01249850 -0.03581843");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("pos", "-0.02294753 0.01051298 -0.03739603");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("pos", "-0.01719077 0.01962052 -0.02978966");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_15_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("pos", "-0.01119298 0.01289560 -0.01909858");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("pos", "-0.01171387 0.02796514 -0.02035748");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("pos", "-0.01105979 0.02955853 -0.01944290");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("pos", "-0.01040955 0.03117727 -0.01849247");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("pos", "-0.00976314 0.03282135 -0.01750618");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("pos", "-0.00912056 0.03449078 -0.01648403");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("pos", "-0.00848182 0.03618556 -0.01542602");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("pos", "-0.00784692 0.03790568 -0.01433215");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("pos", "-0.00721585 0.03965116 -0.01320243");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("pos", "-0.00658861 0.04142198 -0.01203685");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("pos", "-0.00596521 0.04321814 -0.01083541");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("pos", "-0.00534564 0.04503966 -0.00959811");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("pos", "-0.00472991 0.04688652 -0.00832496");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("pos", "-0.00411801 0.04875873 -0.00701594");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("pos", "-0.00350994 0.05065629 -0.00567107");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("pos", "-0.00290571 0.05257920 -0.00429035");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("pos", "-0.00230531 0.05452745 -0.00287376");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("pos", "-0.00170875 0.05650105 -0.00142131");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("pos", "-0.00111603 0.05850000 0.00006699");
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
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("pos", "0.00001517 0.02441159 -0.02463267");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("pos", "0.00128785 0.02242607 -0.02737395");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("pos", "0.00227776 0.02044056 -0.03011539");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("pos", "0.00275814 0.01845504 -0.03285715");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("pos", "0.00260314 0.01646953 -0.03559930");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("pos", "0.00181276 0.01448401 -0.03834185");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("pos", "0.00051285 0.01249850 -0.04108470");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("pos", "-0.00106983 0.01051298 -0.04382774");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("pos", "0.00005995 0.01962052 -0.03433616");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_16_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("pos", "0.00001372 0.01289560 -0.02204128");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("pos", "0.00013734 0.02796593 -0.02342652");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("pos", "0.00023851 0.02956002 -0.02231167");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("pos", "0.00031866 0.03117936 -0.02116760");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("pos", "0.00037781 0.03282396 -0.01999431");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("pos", "0.00041595 0.03449381 -0.01879180");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("pos", "0.00043308 0.03618891 -0.01756007");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("pos", "0.00042920 0.03790927 -0.01629912");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("pos", "0.00040431 0.03965488 -0.01500894");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("pos", "0.00035842 0.04142575 -0.01368954");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("pos", "0.00029152 0.04322187 -0.01234092");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("pos", "0.00020361 0.04504324 -0.01096308");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("pos", "0.00009469 0.04688988 -0.00955602");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("pos", "-0.00003524 0.04876176 -0.00811974");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("pos", "-0.00018618 0.05065890 -0.00665423");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("pos", "-0.00035812 0.05258129 -0.00515951");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("pos", "-0.00055107 0.05452894 -0.00363556");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("pos", "-0.00076503 0.05650184 -0.00208239");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("pos", "-0.00100000 0.05850000 -0.00050000");
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
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("pos", "0.01237224 0.02441159 -0.02132769");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("pos", "0.01484756 0.02242607 -0.02306179");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("pos", "0.01707828 0.02044056 -0.02493777");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("pos", "0.01886826 0.01845504 -0.02706943");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("pos", "0.02010865 0.01646953 -0.02951991");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("pos", "0.02079944 0.01448401 -0.03228922");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("pos", "0.02104948 0.01249850 -0.03531420");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("pos", "0.02105493 0.01051298 -0.03848107");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("pos", "0.01726503 0.01962052 -0.02968172");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_17_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("pos", "0.01119288 0.01289560 -0.01908615");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("pos", "0.01192700 0.02796539 -0.02022231");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("pos", "0.01144925 0.02955900 -0.01920635");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("pos", "0.01093899 0.03117792 -0.01817555");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("pos", "0.01039622 0.03282216 -0.01712991");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("pos", "0.00982094 0.03449172 -0.01606944");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("pos", "0.00921315 0.03618660 -0.01499413");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("pos", "0.00857286 0.03790680 -0.01390398");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("pos", "0.00790005 0.03965232 -0.01279899");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("pos", "0.00719473 0.04142315 -0.01167916");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("pos", "0.00645691 0.04321931 -0.01054450");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("pos", "0.00568658 0.04504078 -0.00939499");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("pos", "0.00488373 0.04688757 -0.00823065");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("pos", "0.00404838 0.04875968 -0.00705147");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("pos", "0.00318052 0.05065711 -0.00585746");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("pos", "0.00228015 0.05257985 -0.00464860");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("pos", "0.00134726 0.05452792 -0.00342491");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("pos", "0.00038187 0.05650130 -0.00218638");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("pos", "-0.00061603 0.05850000 -0.00093301");
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
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("pos", "0.02139509 0.02441159 -0.01234935");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("pos", "0.02440530 0.02242607 -0.01261934");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("pos", "0.02727416 0.02044056 -0.01313423");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("pos", "0.02988830 0.01845504 -0.01409042");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("pos", "0.03218481 0.01646953 -0.01559689");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("pos", "0.03416369 0.01448401 -0.01765365");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("pos", "0.03588785 0.01249850 -0.02015171");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("pos", "0.03747065 0.01051298 -0.02289467");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("pos", "0.02982078 0.01962052 -0.01712184");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_18_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("pos", "0.01935951 0.01289560 -0.01105390");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("pos", "0.02050312 0.02796432 -0.01163892");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("pos", "0.01957582 0.02955698 -0.01099221");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("pos", "0.01861318 0.03117509 -0.01034897");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("pos", "0.01761520 0.03281864 -0.00970919");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("pos", "0.01658188 0.03448763 -0.00907287");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("pos", "0.01551322 0.03618207 -0.00844002");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("pos", "0.01440923 0.03790195 -0.00781063");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("pos", "0.01326990 0.03964728 -0.00718471");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("pos", "0.01209523 0.04141805 -0.00656225");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("pos", "0.01088523 0.04321427 -0.00594326");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("pos", "0.00963988 0.04503593 -0.00532773");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("pos", "0.00835920 0.04688303 -0.00471566");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("pos", "0.00704318 0.04875558 -0.00410706");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("pos", "0.00569182 0.05065358 -0.00350193");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("pos", "0.00430513 0.05257702 -0.00290025");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("pos", "0.00288309 0.05452590 -0.00230205");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("pos", "0.00142572 0.05650023 -0.00170730");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("pos", "-0.00006699 0.05850000 -0.00111603");
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
