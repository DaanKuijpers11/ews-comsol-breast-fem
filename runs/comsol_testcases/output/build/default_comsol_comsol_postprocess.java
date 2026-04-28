import com.comsol.model.*;
import com.comsol.model.util.*;

public class default_comsol_comsol_postprocess {
  private static double firstReal(double[][] values) {
    if (values == null || values.length == 0 || values[0].length == 0) {
      return Double.NaN;
    }
    return values[0][0];
  }

  private static double evalIntVolume(Model model, String tag, String selectionTag, String expr) {
    model.result().numerical().create(tag, "IntVolume");
    model.result().numerical(tag).selection().named(selectionTag);
    model.result().numerical(tag).set("expr", new String[] { expr });
    return firstReal(model.result().numerical(tag).getReal());
  }

  private static double evalMaxVolume(Model model, String tag, String selectionTag, String expr) {
    model.result().numerical().create(tag, "MaxVolume");
    model.result().numerical(tag).selection().named(selectionTag);
    model.result().numerical(tag).set("expr", new String[] { expr });
    return firstReal(model.result().numerical(tag).getReal());
  }

  public static Model run() throws Exception {
    ModelUtil.initStandalone(true);
    Model model = ModelUtil.load("PostModel", "C:/Users/20223231/ews_fem_clean/runs/comsol_testcases/output/solve/default_comsol_result.mph");

    double breastVolume = evalIntVolume(model, "ivBreastVol", "geom1_breast_union_dom", "1");
    double glandVolume = evalIntVolume(model, "ivGlandVol", "geom1_gland_clip_dom", "1");
    double adiposeVolume = evalIntVolume(model, "ivAdiposeVol", "geom1_adipose_diff_dom", "1");

    double maxDispBreast = evalMaxVolume(model, "mvDispBreast", "geom1_breast_union_dom", "solid.disp");
    double intDispBreast = evalIntVolume(model, "ivDispBreast", "geom1_breast_union_dom", "solid.disp");
    double avgDispBreast = breastVolume != 0.0 ? intDispBreast / breastVolume : Double.NaN;

    double maxMisesBreast = evalMaxVolume(model, "mvMisesBreast", "geom1_breast_union_dom", "solid.mises");
    double maxMisesGland = evalMaxVolume(model, "mvMisesGland", "geom1_gland_clip_dom", "solid.mises");

    String json = ""
      + "{\n"
      + "  \"case_name\": \"default_comsol\",\n"
      + "  \"source\": \"COMSOL\",\n"
      + "  \"breast_volume\": " + breastVolume + ",\n"
      + "  \"glandular_volume\": " + glandVolume + ",\n"
      + "  \"adipose_volume\": " + adiposeVolume + ",\n"
      + "  \"max_displacement_breast\": " + maxDispBreast + ",\n"
      + "  \"avg_displacement_breast\": " + avgDispBreast + ",\n"
      + "  \"max_von_mises_breast\": " + maxMisesBreast + ",\n"
      + "  \"max_von_mises_glandular\": " + maxMisesGland + "\n"
      + "}\n";

    System.out.println("COMSOL_METRICS_JSON_BEGIN");
    System.out.print(json);
    System.out.println("COMSOL_METRICS_JSON_END");
    return model;
  }

  public static void main(String[] args) throws Exception {
    run();
    ModelUtil.disconnect();
  }
}
