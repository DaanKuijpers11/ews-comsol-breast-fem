import com.comsol.model.*;
import com.comsol.model.util.*;

public class full_freeze_probe_v2_curved_comsol_builder {
  public static Model run() {
    ModelUtil.initStandalone(true);
    Model model = ModelUtil.create("Model");
    model.label("full_freeze_probe_v2_curved_generated.mph");
    model.modelPath("C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_freeze_probe_v2_curved/build");

    model.param().set("breast_radius", "0.07[m]");
    model.param().set("chest_thickness", "0.002[m]");
    model.param().set("skin_shell_thickness", "0.0001000000[m]");
    model.param().set("chest_curve_depth", "0.0012000000[m]");
    model.param().set("chest_curve_radius", "2.0422666667[m]");
    model.param().set("chest_curve_center_y", "-2.0410666667[m]");
    model.param().set("mesh_density_hint", "150.0");
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
    if (true) {
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
    if (true) {
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

    if (true) {
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
    // - Build plan JSON: C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_freeze_probe_v2_curved/prepare/full_freeze_probe_v2_curved_comsol_build_plan.json
    // - Selection hints JSON: C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_freeze_probe_v2_curved/build/full_freeze_probe_v2_curved_comsol_selection_hints.json
    // - Lobule primitives in plan: 18
    // - Anatomical lobe groups interpreted in COMSOL: 18
    //
    // Source geometry summary:
    // - radius: 0.07
    // - chest-wall thickness: 0.002
    // - asymmetry enabled: False
    //
    // Source mesh summary:
    // - density: 150.0
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
    model.save("C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_freeze_probe_v2_curved/build/full_freeze_probe_v2_curved_generated.mph");
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
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("pos", "0.01109420 0.04253753 0.00004679");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("pos", "0.01346107 0.04070945 0.00106527");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("pos", "0.01582889 0.03888136 0.00186110");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("pos", "0.01819840 0.03705327 0.00225573");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("pos", "0.02057001 0.03522519 0.00215007");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("pos", "0.02294374 0.03339710 0.00154411");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("pos", "0.02531916 0.03156902 0.00053695");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("pos", "0.02769552 0.02974093 -0.00069286");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("pos", "0.02074818 0.03802571 0.00019441");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_01_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("pos", "0.00866523 0.03193463 0.00003555");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("pos", "0.01010570 0.04579680 0.00012977");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("pos", "0.00916294 0.04717160 0.00020704");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("pos", "0.00826593 0.04849001 0.00027812");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("pos", "0.00741466 0.04975204 0.00034299");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("pos", "0.00660913 0.05095768 0.00040166");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("pos", "0.00584935 0.05210694 0.00045413");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("pos", "0.00513531 0.05319981 0.00050040");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("pos", "0.00446702 0.05423630 0.00054046");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("pos", "0.00384447 0.05521640 0.00057432");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("pos", "0.00326767 0.05614012 0.00060198");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("pos", "0.00273660 0.05700745 0.00062344");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("pos", "0.00225129 0.05781840 0.00063870");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("pos", "0.00181171 0.05857296 0.00064775");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("pos", "0.00141788 0.05927114 0.00065061");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("pos", "0.00106980 0.05991293 0.00064726");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("pos", "0.00076745 0.06049834 0.00063771");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("pos", "0.00051085 0.06102736 0.00062195");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_01_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_01_duct_bead_18").set("pos", "0.00030000 0.06150000 0.00060000");
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
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("pos", "0.00558961 0.04253753 0.00949895");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("pos", "0.00592298 0.04070945 0.01205399");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("pos", "0.00644824 0.03888136 0.01449612");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("pos", "0.00731929 0.03705327 0.01673478");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("pos", "0.00862152 0.03522519 0.01871971");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("pos", "0.01035493 0.03339710 0.02045091");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("pos", "0.01243412 0.03156902 0.02197864");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("pos", "0.01470521 0.02974093 0.02339344");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("pos", "0.01036216 0.03802571 0.01792896");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_02_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("pos", "0.00435774 0.03193463 0.00720128");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("pos", "0.00501458 0.04579680 0.00860386");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("pos", "0.00446824 0.04717160 0.00784643");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("pos", "0.00395061 0.04849001 0.00712453");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("pos", "0.00346168 0.04975204 0.00643816");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("pos", "0.00300145 0.05095768 0.00578732");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("pos", "0.00256993 0.05210694 0.00517201");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("pos", "0.00216710 0.05319981 0.00459224");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("pos", "0.00179298 0.05423630 0.00404799");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("pos", "0.00144756 0.05521640 0.00353928");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("pos", "0.00113084 0.05614012 0.00306610");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("pos", "0.00084283 0.05700745 0.00262845");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("pos", "0.00058352 0.05781840 0.00222634");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("pos", "0.00035291 0.05857296 0.00185975");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("pos", "0.00015100 0.05927114 0.00152870");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("pos", "-0.00002221 0.05991293 0.00123318");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("pos", "-0.00016671 0.06049834 0.00097319");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("pos", "-0.00028252 0.06102736 0.00074873");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_02_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_02_duct_bead_18").set("pos", "-0.00036962 0.06150000 0.00055981");
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
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("pos", "-0.00552700 0.04253753 0.00958580");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("pos", "-0.00758507 0.04070945 0.01113622");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("pos", "-0.00945024 0.03888136 0.01279786");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("pos", "-0.01096784 0.03705327 0.01465989");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("pos", "-0.01205203 0.03522519 0.01677183");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("pos", "-0.01270281 0.03339710 0.01913366");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("pos", "-0.01300601 0.03156902 0.02169590");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("pos", "-0.01311633 0.02974093 0.02436935");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("pos", "-0.01043306 0.03802571 0.01788248");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_03_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("pos", "-0.00431372 0.03193463 0.00727625");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("pos", "-0.00511013 0.04579680 0.00859567");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("pos", "-0.00471055 0.04717160 0.00775034");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("pos", "-0.00432827 0.04849001 0.00694718");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("pos", "-0.00396329 0.04975204 0.00618619");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("pos", "-0.00361560 0.05095768 0.00546736");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("pos", "-0.00328520 0.05210694 0.00479071");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("pos", "-0.00297211 0.05319981 0.00415621");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("pos", "-0.00267631 0.05423630 0.00356389");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("pos", "-0.00239781 0.05521640 0.00301373");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("pos", "-0.00213660 0.05614012 0.00250574");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("pos", "-0.00189269 0.05700745 0.00203991");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("pos", "-0.00166607 0.05781840 0.00161626");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("pos", "-0.00145676 0.05857296 0.00123476");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("pos", "-0.00126474 0.05927114 0.00089544");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("pos", "-0.00109001 0.05991293 0.00059828");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("pos", "-0.00093258 0.06049834 0.00034329");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("pos", "-0.00079245 0.06102736 0.00013047");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_03_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_03_duct_bead_18").set("pos", "-0.00066962 0.06150000 -0.00004019");
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
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("pos", "-0.01113587 0.04253753 0.00004853");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("pos", "-0.01351139 0.04070945 -0.00094962");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("pos", "-0.01588595 0.03888136 -0.00172512");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("pos", "-0.01825875 0.03705327 -0.00209941");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("pos", "-0.02062938 0.03522519 -0.00197342");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("pos", "-0.02299782 0.03339710 -0.00134713");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("pos", "-0.02536452 0.03156902 -0.00031963");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("pos", "-0.02773024 0.02974093 0.00093051");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("pos", "-0.02079819 0.03802571 -0.00007827");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_04_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("pos", "-0.00870691 0.03193463 0.00003691");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("pos", "-0.01014474 0.04579680 -0.00003988");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("pos", "-0.00919940 0.04717160 -0.00011383");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("pos", "-0.00829983 0.04849001 -0.00017383");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("pos", "-0.00744605 0.04975204 -0.00021988");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("pos", "-0.00663804 0.05095768 -0.00025198");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("pos", "-0.00587582 0.05210694 -0.00027013");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("pos", "-0.00515937 0.05319981 -0.00027434");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("pos", "-0.00448871 0.05423630 -0.00026459");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("pos", "-0.00386383 0.05521640 -0.00024090");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("pos", "-0.00328472 0.05614012 -0.00020326");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("pos", "-0.00275140 0.05700745 -0.00015167");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("pos", "-0.00226386 0.05781840 -0.00008614");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("pos", "-0.00182210 0.05857296 -0.00000665");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("pos", "-0.00142612 0.05927114 0.00008678");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("pos", "-0.00107592 0.05991293 0.00019416");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("pos", "-0.00077150 0.06049834 0.00031549");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("pos", "-0.00051286 0.06102736 0.00045077");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_04_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_04_duct_bead_18").set("pos", "-0.00030000 0.06150000 0.00060000");
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
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("pos", "-0.00552839 0.04253753 -0.00955346");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("pos", "-0.00584314 0.04070945 -0.01211087");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("pos", "-0.00635060 0.03888136 -0.01455676");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("pos", "-0.00720531 0.03705327 -0.01680171");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("pos", "-0.00849304 0.03522519 -0.01879607");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("pos", "-0.01021380 0.03339710 -0.02053985");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("pos", "-0.01228181 0.03156902 -0.02208268");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("pos", "-0.01454253 0.02974093 -0.02351400");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("pos", "-0.01028209 0.03802571 -0.01798599");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_05_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("pos", "-0.00431181 0.03193463 -0.00724600");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("pos", "-0.00496225 0.04579680 -0.00865160");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("pos", "-0.00443095 0.04717160 -0.00788429");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("pos", "-0.00393450 0.04849001 -0.00714899");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("pos", "-0.00347290 0.04975204 -0.00644569");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("pos", "-0.00304615 0.05095768 -0.00577439");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("pos", "-0.00265425 0.05210694 -0.00513510");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("pos", "-0.00229720 0.05319981 -0.00452781");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("pos", "-0.00197499 0.05423630 -0.00395252");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("pos", "-0.00168763 0.05521640 -0.00340923");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("pos", "-0.00143513 0.05614012 -0.00289795");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("pos", "-0.00121747 0.05700745 -0.00241867");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("pos", "-0.00103466 0.05781840 -0.00197140");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("pos", "-0.00088669 0.05857296 -0.00155612");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("pos", "-0.00077358 0.05927114 -0.00117285");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("pos", "-0.00069532 0.05991293 -0.00082159");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("pos", "-0.00065190 0.06049834 -0.00050232");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("pos", "-0.00064333 0.06102736 -0.00021506");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_05_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_05_duct_bead_18").set("pos", "-0.00066962 0.06150000 0.00004019");
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
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("pos", "0.00548469 0.04253753 -0.00965236");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("semiaxes", "0.00831944 0.00792102 0.01201313");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("pos", "0.00753295 0.04070945 -0.01121570");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("semiaxes", "0.00927728 0.00868922 0.01159529");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("pos", "0.00938762 0.03888136 -0.01288905");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("semiaxes", "0.00980885 0.00911554 0.01117744");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("pos", "0.01089347 0.03705327 -0.01476061");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("semiaxes", "0.00980885 0.00911554 0.01075959");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("pos", "0.01196434 0.03522519 -0.01687933");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("semiaxes", "0.00927728 0.00868922 0.01034174");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("pos", "0.01260023 0.03339710 -0.01924521");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("semiaxes", "0.00831944 0.00792102 0.00992389");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("pos", "0.01288729 0.03156902 -0.02180931");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("semiaxes", "0.00712502 0.00696309 0.00950605");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("pos", "0.01298078 0.02974093 -0.02448340");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("semiaxes", "0.00850145 0.00866140 0.00877481");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("pos", "0.01038051 0.03802571 -0.01796220");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_06_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("semiaxes", "0.00842048 0.00815191 0.01133413");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("pos", "0.00428468 0.03193463 -0.00733446");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("pos", "0.00506866 0.04579680 -0.00865875");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("pos", "0.00466332 0.04717160 -0.00781417");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("pos", "0.00426865 0.04849001 -0.00701562");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("pos", "0.00388467 0.04975204 -0.00626309");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("pos", "0.00351137 0.05095768 -0.00555658");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("pos", "0.00314874 0.05210694 -0.00489609");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("pos", "0.00279680 0.05319981 -0.00428162");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("pos", "0.00245554 0.05423630 -0.00371317");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("pos", "0.00212496 0.05521640 -0.00319074");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("pos", "0.00180507 0.05614012 -0.00271433");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("pos", "0.00149585 0.05700745 -0.00228394");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("pos", "0.00119731 0.05781840 -0.00189957");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("pos", "0.00090945 0.05857296 -0.00156123");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("pos", "0.00063228 0.05927114 -0.00126890");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("pos", "0.00036578 0.05991293 -0.00102260");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("pos", "0.00010997 0.06049834 -0.00082231");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("pos", "-0.00013516 0.06102736 -0.00066805");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_06_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_06_duct_bead_18").set("pos", "-0.00036962 0.06150000 -0.00055981");
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
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("pos", "0.02471643 0.02841159 -0.00001283");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("pos", "0.02745914 0.02642607 0.00125674");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("pos", "0.03020171 0.02444056 0.00224354");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("pos", "0.03294402 0.02245504 0.00272080");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("pos", "0.03568599 0.02046953 0.00256269");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("pos", "0.03842763 0.01848401 0.00176920");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("pos", "0.04116901 0.01649850 0.00046618");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("pos", "0.04391025 0.01451298 -0.00111961");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("pos", "0.03442101 0.02362052 0.00007750");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_07_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("pos", "0.02236610 0.01689560 -0.00001148");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("pos", "0.02339515 0.03217061 0.00011692");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("pos", "0.02207104 0.03393875 0.00023796");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("pos", "0.02074409 0.03570154 0.00035037");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("pos", "0.01941432 0.03745896 0.00045414");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("pos", "0.01808171 0.03921102 0.00054928");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("pos", "0.01674626 0.04095772 0.00063577");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("pos", "0.01540799 0.04269906 0.00071363");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("pos", "0.01406688 0.04443504 0.00078285");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("pos", "0.01272294 0.04616566 0.00084344");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("pos", "0.01137617 0.04789092 0.00089538");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("pos", "0.01002656 0.04961082 0.00093869");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("pos", "0.00867412 0.05132535 0.00097336");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("pos", "0.00731885 0.05303453 0.00099940");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("pos", "0.00596074 0.05473835 0.00101679");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("pos", "0.00459981 0.05643680 0.00102555");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("pos", "0.00323604 0.05812989 0.00102567");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("pos", "0.00186944 0.05981763 0.00101715");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_07_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_07_duct_bead_18").set("pos", "0.00050000 0.06150000 0.00100000");
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
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("pos", "0.02129617 0.02841159 0.01231190");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("pos", "0.02303393 0.02642607 0.01478465");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("pos", "0.02491321 0.02444056 0.01701260");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("pos", "0.02704751 0.02245504 0.01879943");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("pos", "0.02949983 0.02046953 0.02003619");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("pos", "0.03227015 0.01848401 0.02072288");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("pos", "0.03529550 0.01649850 0.02096846");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("pos", "0.03846237 0.01451298 0.02096923");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("pos", "0.02967734 0.02362052 0.01724413");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_08_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("pos", "0.01926141 0.01689560 0.01101490");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("pos", "0.02009130 0.03217378 0.01171056");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("pos", "0.01888855 0.03394472 0.01116040");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("pos", "0.01768792 0.03570993 0.01060112");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("pos", "0.01648941 0.03746940 0.01003270");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("pos", "0.01529302 0.03922314 0.00945515");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("pos", "0.01409876 0.04097115 0.00886847");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("pos", "0.01290661 0.04271342 0.00827266");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("pos", "0.01171659 0.04444996 0.00766771");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("pos", "0.01052869 0.04618077 0.00705364");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("pos", "0.00934291 0.04790584 0.00643043");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("pos", "0.00815925 0.04962518 0.00579809");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("pos", "0.00697771 0.05133878 0.00515662");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("pos", "0.00579829 0.05304665 0.00450601");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("pos", "0.00462099 0.05474879 0.00384628");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("pos", "0.00344581 0.05644519 0.00317741");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("pos", "0.00227276 0.05813586 0.00249942");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("pos", "0.00110183 0.05982080 0.00181229");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_08_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_08_duct_bead_18").set("pos", "-0.00006699 0.06150000 0.00111603");
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
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("pos", "0.01229593 0.02841159 0.02138559");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("pos", "0.01256085 0.02642607 0.02439626");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("pos", "0.01307091 0.02444056 0.02726597");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("pos", "0.01402270 0.02245504 0.02988172");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("pos", "0.01552531 0.02046953 0.03218076");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("pos", "0.01757873 0.01848401 0.03416310");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("pos", "0.02007389 0.01649850 0.03589146");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("pos", "0.02281418 0.01451298 0.03747888");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("pos", "0.01710596 0.02362052 0.02983592");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_09_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("pos", "0.01112441 0.01689560 0.01913906");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("pos", "0.01152642 0.03217190 0.02021290");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("pos", "0.01076304 0.03394119 0.01913737");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("pos", "0.01000581 0.03570496 0.01805453");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("pos", "0.00925471 0.03746322 0.01696435");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("pos", "0.00850976 0.03921597 0.01586686");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("pos", "0.00777094 0.04096320 0.01476204");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("pos", "0.00703826 0.04270493 0.01364990");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("pos", "0.00631172 0.04444113 0.01253044");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("pos", "0.00559132 0.04617183 0.01140365");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("pos", "0.00487706 0.04789701 0.01026954");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("pos", "0.00416894 0.04961668 0.00912810");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("pos", "0.00346695 0.05133084 0.00797935");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("pos", "0.00277111 0.05303948 0.00682326");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("pos", "0.00208140 0.05474261 0.00565986");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("pos", "0.00139784 0.05644023 0.00448913");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("pos", "0.00072041 0.05813233 0.00331108");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("pos", "0.00004912 0.05981892 0.00212571");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_09_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_09_duct_bead_18").set("pos", "-0.00061603 0.06150000 0.00093301");
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
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("pos", "0.00002536 0.02841159 0.02468241");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("pos", "-0.00124281 0.02642607 0.02742577");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("pos", "-0.00222822 0.02444056 0.03016884");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("pos", "-0.00270409 0.02245504 0.03291139");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("pos", "-0.00254458 0.02046953 0.03565328");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("pos", "-0.00174970 0.01848401 0.03839452");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("pos", "-0.00044528 0.01649850 0.04113524");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("pos", "0.00114190 0.01451298 0.04387566");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("pos", "-0.00001556 0.02362052 0.03438752");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_10_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("pos", "0.00002294 0.01689560 0.02209102");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("pos", "-0.00010499 0.03217153 0.02325480");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("pos", "-0.00022671 0.03394048 0.02194422");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("pos", "-0.00033980 0.03570397 0.02063015");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("pos", "-0.00044425 0.03746198 0.01931259");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("pos", "-0.00054007 0.03921453 0.01799153");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("pos", "-0.00062725 0.04096161 0.01666698");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("pos", "-0.00070580 0.04270322 0.01533894");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("pos", "-0.00077572 0.04443936 0.01400741");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("pos", "-0.00083700 0.04617003 0.01267239");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("pos", "-0.00088965 0.04789524 0.01133387");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("pos", "-0.00093366 0.04961497 0.00999186");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("pos", "-0.00096904 0.05132924 0.00864636");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("pos", "-0.00099578 0.05303804 0.00729736");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("pos", "-0.00101390 0.05474137 0.00594487");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("pos", "-0.00102337 0.05643923 0.00458889");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("pos", "-0.00102422 0.05813162 0.00322942");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("pos", "-0.00101643 0.05981855 0.00186646");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_10_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_10_duct_bead_18").set("pos", "-0.00100000 0.06150000 0.00050000");
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
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("pos", "-0.01217128 0.02841159 0.02130302");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("pos", "-0.01463513 0.02642607 0.02305336");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("pos", "-0.01685346 0.02444056 0.02494399");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("pos", "-0.01862938 0.02245504 0.02708738");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("pos", "-0.01985361 0.02046953 0.02954597");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("pos", "-0.02052616 0.01848401 0.03231976");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("pos", "-0.02075630 0.01649850 0.03534632");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("pos", "-0.02074091 0.01451298 0.03851316");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("pos", "-0.01705376 0.02362052 0.02967040");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_11_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("pos", "-0.01100532 0.01689560 0.01905297");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("pos", "-0.01163061 0.03217551 0.02000342");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("pos", "-0.01108129 0.03394798 0.01881007");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("pos", "-0.01052332 0.03571452 0.01761830");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("pos", "-0.00995670 0.03747511 0.01642814");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("pos", "-0.00938144 0.03922977 0.01523956");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("pos", "-0.00879752 0.04097849 0.01405258");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("pos", "-0.00820496 0.04272127 0.01286719");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("pos", "-0.00760376 0.04445812 0.01168339");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("pos", "-0.00699390 0.04618902 0.01050119");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("pos", "-0.00637539 0.04791399 0.00932057");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("pos", "-0.00574824 0.04963303 0.00814156");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("pos", "-0.00511244 0.05134612 0.00696413");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("pos", "-0.00446799 0.05305328 0.00578829");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("pos", "-0.00381489 0.05475450 0.00461405");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("pos", "-0.00315315 0.05644978 0.00344140");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("pos", "-0.00248276 0.05813912 0.00227035");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("pos", "-0.00180371 0.05982253 0.00110088");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_11_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_11_duct_bead_18").set("pos", "-0.00111603 0.06150000 -0.00006699");
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
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("pos", "-0.02141097 0.02841159 0.01240738");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("pos", "-0.02442072 0.02642607 0.01268251");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("pos", "-0.02728869 0.02444056 0.01320231");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("pos", "-0.02990119 0.02245504 0.01416296");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("pos", "-0.03219512 0.02046953 0.01567336");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("pos", "-0.03417048 0.01848401 0.01773351");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("pos", "-0.03589037 0.01649850 0.02023451");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("pos", "-0.03746848 0.01451298 0.02298017");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("pos", "-0.02983605 0.02362052 0.01718419");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_12_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("pos", "-0.01937740 0.01689560 0.01110809");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("pos", "-0.02033137 0.03216981 0.01157878");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("pos", "-0.01924495 0.03393724 0.01081626");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("pos", "-0.01815169 0.03569941 0.01005939");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("pos", "-0.01705160 0.03745632 0.00930817");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("pos", "-0.01594468 0.03920795 0.00856259");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("pos", "-0.01483093 0.04095433 0.00782266");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("pos", "-0.01371034 0.04269543 0.00708838");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("pos", "-0.01258293 0.04443127 0.00635975");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("pos", "-0.01144868 0.04616184 0.00563676");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("pos", "-0.01030760 0.04788715 0.00491942");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("pos", "-0.00915969 0.04960719 0.00420772");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("pos", "-0.00800494 0.05132196 0.00350167");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("pos", "-0.00684337 0.05303146 0.00280127");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("pos", "-0.00567496 0.05473570 0.00210652");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("pos", "-0.00449972 0.05643468 0.00141741");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("pos", "-0.00331765 0.05812839 0.00073395");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("pos", "-0.00212875 0.05981683 0.00005614");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_12_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_12_duct_bead_18").set("pos", "-0.00093301 0.06150000 -0.00061603");
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
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("pos", "-0.02465833 0.02841159 -0.00005721");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("pos", "-0.02739743 0.02642607 -0.00133456");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("pos", "-0.03013719 0.02444056 -0.00232914");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("pos", "-0.03287813 0.02245504 -0.00281419");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("pos", "-0.03562054 0.02046953 -0.00266386");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("pos", "-0.03836442 0.01848401 -0.00187816");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("pos", "-0.04110949 0.01649850 -0.00058292");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("pos", "-0.04385522 0.01451298 0.00099508");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("pos", "-0.03436024 0.02362052 -0.00015420");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_13_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("pos", "-0.02230801 0.01689560 -0.00005120");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("pos", "-0.02333906 0.03217217 -0.00017677");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("pos", "-0.02201710 0.03394170 -0.00027561");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("pos", "-0.02069245 0.03570568 -0.00035343");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("pos", "-0.01936511 0.03746412 -0.00041025");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("pos", "-0.01803508 0.03921701 -0.00044607");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("pos", "-0.01670236 0.04096436 -0.00046088");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("pos", "-0.01536696 0.04270616 -0.00045468");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("pos", "-0.01402886 0.04444241 -0.00042747");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("pos", "-0.01268808 0.04617312 -0.00037925");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("pos", "-0.01134460 0.04789829 -0.00031003");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("pos", "-0.00999844 0.04961791 -0.00021980");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("pos", "-0.00864959 0.05133199 -0.00010857");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("pos", "-0.00729804 0.05304052 0.00002368");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("pos", "-0.00594381 0.05474351 0.00017693");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("pos", "-0.00458689 0.05644095 0.00035118");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("pos", "-0.00322728 0.05813284 0.00054645");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("pos", "-0.00186499 0.05981919 0.00076272");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_13_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_13_duct_bead_18").set("pos", "-0.00050000 0.06150000 0.00100000");
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
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("pos", "-0.02129731 0.02841159 -0.01228439");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("pos", "-0.02303752 0.02642607 -0.01475541");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("pos", "-0.02491901 0.02444056 -0.01698150");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("pos", "-0.02705508 0.02245504 -0.01876621");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("pos", "-0.02950862 0.02046953 -0.02000053");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("pos", "-0.03227963 0.01848401 -0.02068448");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("pos", "-0.03530522 0.01649850 -0.02092705");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("pos", "-0.03847209 0.01451298 -0.02092468");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("pos", "-0.02968045 0.02362052 -0.01721508");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_14_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("pos", "-0.01926138 0.01689560 -0.01098961");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("pos", "-0.02009526 0.03217412 -0.01167913");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("pos", "-0.01890153 0.03394537 -0.01111429");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("pos", "-0.01771612 0.03571084 -0.01052965");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("pos", "-0.01653902 0.03747054 -0.00992521");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("pos", "-0.01537025 0.03922446 -0.00930096");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("pos", "-0.01420978 0.04097261 -0.00865692");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("pos", "-0.01305764 0.04271499 -0.00799308");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("pos", "-0.01191382 0.04445159 -0.00730944");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("pos", "-0.01077831 0.04618241 -0.00660599");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("pos", "-0.00965112 0.04790746 -0.00588275");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("pos", "-0.00853224 0.04962674 -0.00513970");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("pos", "-0.00742169 0.05134024 -0.00437686");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("pos", "-0.00631945 0.05304797 -0.00359421");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("pos", "-0.00522552 0.05474993 -0.00279177");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("pos", "-0.00413992 0.05644611 -0.00196952");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("pos", "-0.00306263 0.05813651 -0.00112747");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("pos", "-0.00199366 0.05982114 -0.00026562");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_14_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_14_duct_bead_18").set("pos", "-0.00093301 0.06150000 0.00061603");
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
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("pos", "-0.01237178 0.02841159 -0.02134047");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("pos", "-0.01264747 0.02642607 -0.02435017");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("pos", "-0.01316779 0.02444056 -0.02721804");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("pos", "-0.01412893 0.02245504 -0.02983037");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("pos", "-0.01563975 0.02046953 -0.03212402");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("pos", "-0.01770026 0.01848401 -0.03409900");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("pos", "-0.02020158 0.01649850 -0.03581843");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("pos", "-0.02294753 0.01451298 -0.03739603");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("pos", "-0.01719077 0.02362052 -0.02978966");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_15_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("pos", "-0.01119298 0.01689560 -0.01909858");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("pos", "-0.01160359 0.03217193 -0.02016725");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("pos", "-0.01085221 0.03394125 -0.01908483");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("pos", "-0.01011763 0.03570504 -0.01798893");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("pos", "-0.00939987 0.03746332 -0.01687956");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("pos", "-0.00869891 0.03921609 -0.01575670");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("pos", "-0.00801476 0.04096334 -0.01462036");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("pos", "-0.00734742 0.04270507 -0.01347055");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("pos", "-0.00669689 0.04444128 -0.01230726");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("pos", "-0.00606316 0.04617198 -0.01113049");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("pos", "-0.00544625 0.04789716 -0.00994024");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("pos", "-0.00484614 0.04961682 -0.00873651");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("pos", "-0.00426284 0.05133097 -0.00751930");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("pos", "-0.00369635 0.05303960 -0.00628862");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("pos", "-0.00314667 0.05474271 -0.00504445");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("pos", "-0.00261380 0.05644031 -0.00378681");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("pos", "-0.00209773 0.05813239 -0.00251569");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("pos", "-0.00159847 0.05981895 -0.00123109");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_15_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_15_duct_bead_18").set("pos", "-0.00111603 0.06150000 0.00006699");
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
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("pos", "0.00001517 0.02841159 -0.02463267");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("pos", "0.00128785 0.02642607 -0.02737395");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("pos", "0.00227776 0.02444056 -0.03011539");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("pos", "0.00275814 0.02245504 -0.03285715");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("pos", "0.00260314 0.02046953 -0.03559930");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("pos", "0.00181276 0.01848401 -0.03834185");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("pos", "0.00051285 0.01649850 -0.04108470");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("pos", "-0.00106983 0.01451298 -0.04382774");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("pos", "0.00005995 0.02362052 -0.03433616");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_16_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("pos", "0.00001372 0.01689560 -0.02204128");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("pos", "0.00013721 0.03217287 -0.02320678");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("pos", "0.00023825 0.03394301 -0.02189804");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("pos", "0.00031830 0.03570752 -0.02058594");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("pos", "0.00037736 0.03746641 -0.01927046");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("pos", "0.00041543 0.03921966 -0.01795162");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("pos", "0.00043251 0.04096730 -0.01662940");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("pos", "0.00042859 0.04270930 -0.01530382");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("pos", "0.00040368 0.04444568 -0.01397487");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("pos", "0.00035778 0.04617643 -0.01264254");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("pos", "0.00029088 0.04790156 -0.01130685");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("pos", "0.00020299 0.04962106 -0.00996779");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("pos", "0.00009411 0.05133493 -0.00862536");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("pos", "-0.00003576 0.05304317 -0.00727955");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("pos", "-0.00018662 0.05474579 -0.00593038");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("pos", "-0.00035848 0.05644278 -0.00457784");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("pos", "-0.00055133 0.05813415 -0.00322193");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("pos", "-0.00076517 0.05981989 -0.00186265");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_16_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_16_duct_bead_18").set("pos", "-0.00100000 0.06150000 -0.00050000");
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
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("pos", "0.01237224 0.02841159 -0.02132769");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("pos", "0.01484756 0.02642607 -0.02306179");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("pos", "0.01707828 0.02444056 -0.02493777");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("pos", "0.01886826 0.02245504 -0.02706943");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("pos", "0.02010865 0.02046953 -0.02951991");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("pos", "0.02079944 0.01848401 -0.03228922");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("pos", "0.02104948 0.01649850 -0.03531420");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("pos", "0.02105493 0.01451298 -0.03848107");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("pos", "0.01726503 0.02362052 -0.02968172");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_17_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("pos", "0.01119288 0.01689560 -0.01908615");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("pos", "0.01181669 0.03217223 -0.02003215");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("pos", "0.01124160 0.03394180 -0.01884841");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("pos", "0.01064699 0.03570582 -0.01767220");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("pos", "0.01003284 0.03746429 -0.01650352");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("pos", "0.00939917 0.03921721 -0.01534237");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("pos", "0.00874596 0.04096457 -0.01418876");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("pos", "0.00807322 0.04270639 -0.01304268");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("pos", "0.00738094 0.04444265 -0.01190413");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("pos", "0.00666914 0.04617337 -0.01077312");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("pos", "0.00593780 0.04789853 -0.00964964");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("pos", "0.00518694 0.04961814 -0.00853370");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("pos", "0.00441654 0.05133221 -0.00742528");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("pos", "0.00362661 0.05304072 -0.00632441");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("pos", "0.00281714 0.05474367 -0.00523106");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("pos", "0.00198815 0.05644108 -0.00414525");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("pos", "0.00113962 0.05813294 -0.00306697");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("pos", "0.00027156 0.05981925 -0.00199622");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_17_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_17_duct_bead_18").set("pos", "-0.00061603 0.06150000 -0.00093301");
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
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("pos", "0.02139509 0.02841159 -0.01234935");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("semiaxes", "0.00805005 0.00768282 0.01304767");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("pos", "0.02440530 0.02642607 -0.01261934");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("semiaxes", "0.00897688 0.00842792 0.01259384");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("pos", "0.02727416 0.02444056 -0.01313423");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("semiaxes", "0.00949124 0.00884142 0.01214001");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("pos", "0.02988830 0.02245504 -0.01409042");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("semiaxes", "0.00949124 0.00884142 0.01168618");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("pos", "0.03218481 0.02046953 -0.01559689");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("semiaxes", "0.00897688 0.00842792 0.01123234");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("pos", "0.03416369 0.01848401 -0.01765365");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("semiaxes", "0.00805005 0.00768282 0.01077851");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("pos", "0.03588785 0.01649850 -0.02015171");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_seg_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("semiaxes", "0.00689431 0.00675369 0.01032468");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("pos", "0.03747065 0.01451298 -0.02289467");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_seg_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_petal_wing", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("semiaxes", "0.00908796 0.00922456 0.00953047");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("pos", "0.02982078 0.02362052 -0.01712184");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_petal_wing").set("selresultshow", "all");
    model.component("comp1").geom("geom1").create("lobe_18_posterior_cap", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("semiaxes", "0.00814782 0.00790676 0.01231019");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("pos", "0.01935951 0.01689560 -0.01105390");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_posterior_cap").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("semiaxes", "0.00109861 0.00109861 0.00137472");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("pos", "0.02031256 0.03217096 -0.01152893");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("semiaxes", "0.00107722 0.00107722 0.00134944");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("pos", "0.01921712 0.03393942 -0.01078517");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("semiaxes", "0.00105583 0.00105583 0.00132417");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("pos", "0.01810876 0.03570247 -0.01005782");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("semiaxes", "0.00103444 0.00103444 0.00129889");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("pos", "0.01698748 0.03746012 -0.00934687");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("semiaxes", "0.00101306 0.00101306 0.00127361");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("pos", "0.01585328 0.03921237 -0.00865232");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("semiaxes", "0.00099167 0.00099167 0.00124833");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("pos", "0.01470616 0.04095922 -0.00797418");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("semiaxes", "0.00097028 0.00097028 0.00122306");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("pos", "0.01354612 0.04270066 -0.00731244");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("semiaxes", "0.00094889 0.00094889 0.00119778");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("pos", "0.01237316 0.04443670 -0.00666711");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("semiaxes", "0.00092750 0.00092750 0.00117250");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("pos", "0.01118729 0.04616734 -0.00603818");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("semiaxes", "0.00090611 0.00090611 0.00114722");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("pos", "0.00998849 0.04789258 -0.00542566");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("semiaxes", "0.00088472 0.00088472 0.00112194");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("pos", "0.00877677 0.04961241 -0.00482954");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("semiaxes", "0.00086333 0.00086333 0.00109667");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("pos", "0.00755214 0.05132685 -0.00424982");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("semiaxes", "0.00084194 0.00084194 0.00107139");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("pos", "0.00631458 0.05303588 -0.00368651");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("semiaxes", "0.00082056 0.00082056 0.00104611");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("pos", "0.00506411 0.05473951 -0.00313960");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("semiaxes", "0.00079917 0.00079917 0.00102083");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("pos", "0.00380071 0.05643773 -0.00260910");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("semiaxes", "0.00077778 0.00077778 0.00099556");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("pos", "0.00252440 0.05813056 -0.00209501");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("semiaxes", "0.00075639 0.00075639 0.00097028");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("pos", "0.00123517 0.05981798 -0.00159731");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobe_18_duct_bead_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("semiaxes", "0.00073500 0.00073500 0.00094500");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobe_18_duct_bead_18").set("pos", "-0.00006699 0.06150000 -0.00111603");
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
