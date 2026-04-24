import com.comsol.model.*;
import com.comsol.model.util.*;

public class default_comsol_builder {
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
    // Next step in this script:
    // 1) create/import breast geometry in geom1
    // 2) map glandular/adipose regions from build plan
    // 3) assign materials and studies
    // 4) run and save MPH

    model.component("comp1").geom("geom1").run();
    return model;
  }

  public static void main(String[] args) {
    Model model = run();
    model.save("runs/comsol_testcases/output/default_comsol_generated.mph");
    ModelUtil.disconnect();
  }
}
