import com.comsol.model.*;
import com.comsol.model.util.*;

public class default_comsol_comsol_builder {
  public static Model run() {
    ModelUtil.initStandalone(true);
    Model model = ModelUtil.create("Model");
    model.label("default_comsol_generated.mph");

    // Base component/geometry
    model.component().create("comp1", true);
    model.component("comp1").geom().create("geom1", 3);
    model.component("comp1").mesh().create("mesh1");

    // Auto-generated pointers:
    // - Build plan JSON: C:\Users\20223231\ews_fem_clean\runs\comsol_testcases\output\default_comsol_comsol_build_plan.json
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
    // Current builder scope:
    // 1) create/import breast geometry or mesh in geom1/mesh1
    // 2) create selections for skin, adipose, glandular, and chest wall
    // 3) assign materials and studies matching the FEBio baseline
    // 4) run and save MPH
    //
    // Note:
    // This file is still a scaffold. It does not yet reproduce the full FEBio model automatically.

    model.component("comp1").geom("geom1").run();
    return model;
  }

  public static void main(String[] args) throws Exception {
    Model model = run();
    model.save("C:/Users/20223231/ews_fem_clean/runs/comsol_testcases/output/default_comsol_generated.mph");
    ModelUtil.disconnect();
  }
}
