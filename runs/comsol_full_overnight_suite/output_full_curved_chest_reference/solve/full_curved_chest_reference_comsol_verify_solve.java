import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.File;

public class full_curved_chest_reference_comsol_verify_solve {
  private static boolean hasPhysics(Model model, String tag) {
    try {
      model.component("comp1").physics(tag);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  private static boolean hasPhysicsFeature(Model model, String physicsTag, String featureTag) {
    try {
      model.component("comp1").physics(physicsTag).feature(featureTag);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  private static boolean hasMaterial(Model model, String tag) {
    try {
      model.component("comp1").material(tag);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  private static boolean hasMultiphysics(Model model, String tag) {
    try {
      model.multiphysics(tag);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  private static String paramOrEmpty(Model model, String name) {
    try {
      String value = model.param().get(name);
      return value == null ? "" : value.replace("\\", "/");
    } catch (Exception ex) {
      return "";
    }
  }

  private static String chooseModelPath() {
    String candidate1 = "C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_curved_chest_reference/solve/full_curved_chest_reference_result.mph";
    if (new File(candidate1).exists()) {
      return candidate1;
    }
    String candidate2 = "C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_curved_chest_reference/build/full_curved_chest_reference_generated.mph";
    if (new File(candidate2).exists()) {
      return candidate2;
    }
    String candidate3 = "C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_curved_chest_reference/build/full_curved_chest_reference_generated_Model.mph";
    if (new File(candidate3).exists()) {
      return candidate3;
    }
    return "C:/Users/20223231/ews_fem_clean/runs/comsol_full_overnight_suite/output_full_curved_chest_reference/solve/full_curved_chest_reference_result.mph";
  }

  public static Model run() throws Exception {
    ModelUtil.initStandalone(true);
    String modelPath = chooseModelPath();
    Model model = ModelUtil.load("VerifyModel", modelPath);

    boolean hasSolid = hasPhysics(model, "solid");
    boolean hasShell = hasPhysics(model, "shell1");
    boolean hasHmatAdipose = hasPhysicsFeature(model, "solid", "hmat_adipose");
    boolean hasHmatGlandular = hasPhysicsFeature(model, "solid", "hmat_glandular");
    boolean hasHmatSkin = hasShell && hasPhysicsFeature(model, "shell1", "hmat_skin");
    boolean hasSthin = hasMultiphysics(model, "sthin1");

    StringBuilder json = new StringBuilder();
    json.append("{\n");
    json.append("  \"case_name\": \"full_curved_chest_reference\",\n");
    json.append("  \"loaded_model_path\": \"").append(modelPath.replace("\\", "/")).append("\",\n");
    json.append("  \"loaded_model_role\": \"").append(modelPath.endsWith("_result.mph") ? "solve_result" : "generated_build_fallback").append("\",\n");
    json.append("  \"physics\": {\n");
    json.append("    \"solid\": ").append(hasSolid).append(",\n");
    json.append("    \"shell1\": ").append(hasShell).append(",\n");
    json.append("    \"sthin1\": ").append(hasSthin).append("\n");
    json.append("  },\n");
    json.append("  \"hyperelastic_features\": {\n");
    json.append("    \"hmat_adipose\": ").append(hasHmatAdipose).append(",\n");
    json.append("    \"hmat_glandular\": ").append(hasHmatGlandular).append(",\n");
    json.append("    \"hmat_skin\": ").append(hasHmatSkin).append("\n");
    json.append("  },\n");
    json.append("  \"materials\": {\n");
    json.append("    \"mat_chest\": ").append(hasMaterial(model, "mat_chest")).append(",\n");
    json.append("    \"mat_adipose\": ").append(hasMaterial(model, "mat_adipose")).append(",\n");
    json.append("    \"mat_glandular\": ").append(hasMaterial(model, "mat_glandular")).append(",\n");
    json.append("    \"mat_skin_shell\": ").append(hasMaterial(model, "mat_skin_shell")).append("\n");
    json.append("  },\n");
    json.append("  \"source_parameters\": {\n");
    json.append("    \"skin_c10\": \"").append(paramOrEmpty(model, "skin_c10")).append("\",\n");
    json.append("    \"skin_c01\": \"").append(paramOrEmpty(model, "skin_c01")).append("\",\n");
    json.append("    \"skin_bulk_modulus\": \"").append(paramOrEmpty(model, "skin_bulk_modulus")).append("\",\n");
    json.append("    \"adipose_c10\": \"").append(paramOrEmpty(model, "adipose_c10")).append("\",\n");
    json.append("    \"adipose_c01\": \"").append(paramOrEmpty(model, "adipose_c01")).append("\",\n");
    json.append("    \"adipose_bulk_modulus\": \"").append(paramOrEmpty(model, "adipose_bulk_modulus")).append("\",\n");
    json.append("    \"glandular_c10\": \"").append(paramOrEmpty(model, "glandular_c10")).append("\",\n");
    json.append("    \"glandular_c01\": \"").append(paramOrEmpty(model, "glandular_c01")).append("\",\n");
    json.append("    \"glandular_bulk_modulus\": \"").append(paramOrEmpty(model, "glandular_bulk_modulus")).append("\",\n");
    json.append("    \"chest_E\": \"").append(paramOrEmpty(model, "chest_E")).append("\",\n");
    json.append("    \"chest_nu\": \"").append(paramOrEmpty(model, "chest_nu")).append("\"\n");
    json.append("  }\n");
    json.append("}");

    System.out.println("COMSOL_VERIFICATION_JSON_BEGIN");
    System.out.println(json.toString());
    System.out.println("COMSOL_VERIFICATION_JSON_END");
    return model;
  }

  public static void main(String[] args) throws Exception {
    run();
    ModelUtil.disconnect();
  }
}
