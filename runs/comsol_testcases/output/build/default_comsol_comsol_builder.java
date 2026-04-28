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

    model.component("comp1").geom("geom1").create("breast_outer", "Union");
    model.component("comp1").geom("geom1").feature("breast_outer").selection("input").set(breastBaseObjs);
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

    
    model.component("comp1").geom("geom1").create("lobule_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_01").set("semiaxes", "0.00357210 0.00454713 0.00642978");
    model.component("comp1").geom("geom1").feature("lobule_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_01").set("pos", "0.00510238 0.05565057 0.00025215");
    model.component("comp1").geom("geom1").feature("lobule_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_02", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_02").set("semiaxes", "0.00196466 0.00250092 0.00353638");
    model.component("comp1").geom("geom1").feature("lobule_02").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_02").set("pos", "0.00242985 0.06211896 0.00012008");
    model.component("comp1").geom("geom1").feature("lobule_02").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_02").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_03").set("semiaxes", "0.00357210 0.00479616 0.00642978");
    model.component("comp1").geom("geom1").feature("lobule_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_03").set("pos", "0.00385156 0.05534445 0.00309800");
    model.component("comp1").geom("geom1").feature("lobule_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_04", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_04").set("semiaxes", "0.00196466 0.00263789 0.00353638");
    model.component("comp1").geom("geom1").feature("lobule_04").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_04").set("pos", "0.00186718 0.06186477 0.00150187");
    model.component("comp1").geom("geom1").feature("lobule_04").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_04").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_05").set("semiaxes", "0.00357210 0.00497875 0.00642978");
    model.component("comp1").geom("geom1").feature("lobule_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_05").set("pos", "0.00004295 0.05589374 0.00499435");
    model.component("comp1").geom("geom1").feature("lobule_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_06", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_06").set("semiaxes", "0.00196466 0.00273831 0.00353638");
    model.component("comp1").geom("geom1").feature("lobule_06").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_06").set("pos", "0.00001999 0.06236468 0.00232481");
    model.component("comp1").geom("geom1").feature("lobule_06").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_06").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_07").set("semiaxes", "0.00357210 0.00487553 0.00642978");
    model.component("comp1").geom("geom1").feature("lobule_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_07").set("pos", "-0.00382216 0.05629548 0.00379687");
    model.component("comp1").geom("geom1").feature("lobule_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_08", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_08").set("semiaxes", "0.00196466 0.00268154 0.00353638");
    model.component("comp1").geom("geom1").feature("lobule_08").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_08").set("pos", "-0.00174569 0.06265421 0.00173414");
    model.component("comp1").geom("geom1").feature("lobule_08").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_08").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_09").set("semiaxes", "0.00357210 0.00453945 0.00642978");
    model.component("comp1").geom("geom1").feature("lobule_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_09").set("pos", "-0.00497781 0.05637875 0.00015708");
    model.component("comp1").geom("geom1").feature("lobule_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_10", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_10").set("semiaxes", "0.00196466 0.00249670 0.00353638");
    model.component("comp1").geom("geom1").feature("lobule_10").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_10").set("pos", "-0.00222186 0.06281282 0.00007011");
    model.component("comp1").geom("geom1").feature("lobule_10").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_10").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_11").set("semiaxes", "0.00357210 0.00487639 0.00642978");
    model.component("comp1").geom("geom1").feature("lobule_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_11").set("pos", "-0.00382426 0.05612390 -0.00385772");
    model.component("comp1").geom("geom1").feature("lobule_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_12", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_12").set("semiaxes", "0.00196466 0.00268201 0.00353638");
    model.component("comp1").geom("geom1").feature("lobule_12").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_12").set("pos", "-0.00177441 0.06248962 -0.00178994");
    model.component("comp1").geom("geom1").feature("lobule_12").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_12").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_13").set("semiaxes", "0.00357210 0.00498679 0.00642978");
    model.component("comp1").geom("geom1").feature("lobule_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_13").set("pos", "0.00029516 0.05598322 -0.00506211");
    model.component("comp1").geom("geom1").feature("lobule_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_14", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_14").set("semiaxes", "0.00196466 0.00274273 0.00353638");
    model.component("comp1").geom("geom1").feature("lobule_14").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_14").set("pos", "0.00013675 0.06243256 -0.00234531");
    model.component("comp1").geom("geom1").feature("lobule_14").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_14").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_15").set("semiaxes", "0.00357210 0.00486437 0.00642978");
    model.component("comp1").geom("geom1").feature("lobule_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_15").set("pos", "0.00330674 0.05641077 -0.00358746");
    model.component("comp1").geom("geom1").feature("lobule_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_16", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_16").set("semiaxes", "0.00196466 0.00267540 0.00353638");
    model.component("comp1").geom("geom1").feature("lobule_16").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_16").set("pos", "0.00146592 0.06286236 -0.00159036");
    model.component("comp1").geom("geom1").feature("lobule_16").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_16").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_17").set("semiaxes", "0.00323190 0.00423864 0.00581742");
    model.component("comp1").geom("geom1").feature("lobule_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_17").set("pos", "0.00841559 0.04788168 0.00296001");
    model.component("comp1").geom("geom1").feature("lobule_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_18", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_18").set("semiaxes", "0.00177754 0.00233125 0.00319958");
    model.component("comp1").geom("geom1").feature("lobule_18").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_18").set("pos", "0.00573882 0.05428078 0.00201851");
    model.component("comp1").geom("geom1").feature("lobule_18").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_18").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_19", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_19").set("semiaxes", "0.00323190 0.00445930 0.00581742");
    model.component("comp1").geom("geom1").feature("lobule_19").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_19").set("pos", "0.00541286 0.04813868 0.00742591");
    model.component("comp1").geom("geom1").feature("lobule_19").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_19").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_20", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_20").set("semiaxes", "0.00177754 0.00245261 0.00319958");
    model.component("comp1").geom("geom1").feature("lobule_20").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_20").set("pos", "0.00368147 0.05449165 0.00505061");
    model.component("comp1").geom("geom1").feature("lobule_20").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_20").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_21", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_21").set("semiaxes", "0.00323190 0.00452606 0.00581742");
    model.component("comp1").geom("geom1").feature("lobule_21").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_21").set("pos", "0.00071959 0.04786344 0.00882789");
    model.component("comp1").geom("geom1").feature("lobule_21").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_21").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_22", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_22").set("semiaxes", "0.00177754 0.00248933 0.00319958");
    model.component("comp1").geom("geom1").feature("lobule_22").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_22").set("pos", "0.00049062 0.05427099 0.00601881");
    model.component("comp1").geom("geom1").feature("lobule_22").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_22").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_23", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_23").set("semiaxes", "0.00323190 0.00446987 0.00581742");
    model.component("comp1").geom("geom1").feature("lobule_23").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_23").set("pos", "-0.00556349 0.04820697 0.00766049");
    model.component("comp1").geom("geom1").feature("lobule_23").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_23").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_24", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_24").set("semiaxes", "0.00177754 0.00245843 0.00319958");
    model.component("comp1").geom("geom1").feature("lobule_24").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_24").set("pos", "-0.00378852 0.05452174 0.00521649");
    model.component("comp1").geom("geom1").feature("lobule_24").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_24").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_25", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_25").set("semiaxes", "0.00323190 0.00421542 0.00581742");
    model.component("comp1").geom("geom1").feature("lobule_25").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_25").set("pos", "-0.00859779 0.04771771 0.00250413");
    model.component("comp1").geom("geom1").feature("lobule_25").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_25").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_26", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_26").set("semiaxes", "0.00177754 0.00231848 0.00319958");
    model.component("comp1").geom("geom1").feature("lobule_26").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_26").set("pos", "-0.00588326 0.05412132 0.00171351");
    model.component("comp1").geom("geom1").feature("lobule_26").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_26").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_27", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_27").set("semiaxes", "0.00323190 0.00422337 0.00581742");
    model.component("comp1").geom("geom1").feature("lobule_27").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_27").set("pos", "-0.00834091 0.04824973 -0.00259865");
    model.component("comp1").geom("geom1").feature("lobule_27").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_27").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_28", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_28").set("semiaxes", "0.00177754 0.00232285 0.00319958");
    model.component("comp1").geom("geom1").feature("lobule_28").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_28").set("pos", "-0.00563736 0.05465140 -0.00175635");
    model.component("comp1").geom("geom1").feature("lobule_28").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_28").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_29", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_29").set("semiaxes", "0.00323190 0.00444994 0.00581742");
    model.component("comp1").geom("geom1").feature("lobule_29").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_29").set("pos", "-0.00551368 0.04807801 -0.00724195");
    model.component("comp1").geom("geom1").feature("lobule_29").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_29").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_30", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_30").set("semiaxes", "0.00177754 0.00244747 0.00319958");
    model.component("comp1").geom("geom1").feature("lobule_30").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_30").set("pos", "-0.00375154 0.05444496 -0.00492746");
    model.component("comp1").geom("geom1").feature("lobule_30").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_30").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_31", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_31").set("semiaxes", "0.00323190 0.00453817 0.00581742");
    model.component("comp1").geom("geom1").feature("lobule_31").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_31").set("pos", "0.00007348 0.04829280 -0.00892487");
    model.component("comp1").geom("geom1").feature("lobule_31").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_31").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_32", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_32").set("semiaxes", "0.00177754 0.00249599 0.00319958");
    model.component("comp1").geom("geom1").feature("lobule_32").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_32").set("pos", "0.00004970 0.05466934 -0.00603711");
    model.component("comp1").geom("geom1").feature("lobule_32").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_32").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_33", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_33").set("semiaxes", "0.00323190 0.00444657 0.00581742");
    model.component("comp1").geom("geom1").feature("lobule_33").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_33").set("pos", "0.00551818 0.04802271 -0.00718401");
    model.component("comp1").geom("geom1").feature("lobule_33").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_33").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_34", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_34").set("semiaxes", "0.00177754 0.00244561 0.00319958");
    model.component("comp1").geom("geom1").feature("lobule_34").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_34").set("pos", "0.00375721 0.05439790 -0.00489143");
    model.component("comp1").geom("geom1").feature("lobule_34").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_34").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_35", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_35").set("semiaxes", "0.00323190 0.00423220 0.00581742");
    model.component("comp1").geom("geom1").feature("lobule_35").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_35").set("pos", "0.00877162 0.04751040 -0.00288856");
    model.component("comp1").geom("geom1").feature("lobule_35").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_35").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_36", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_36").set("semiaxes", "0.00177754 0.00232771 0.00319958");
    model.component("comp1").geom("geom1").feature("lobule_36").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_36").set("pos", "0.00603959 0.05389213 -0.00198888");
    model.component("comp1").geom("geom1").feature("lobule_36").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_36").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_01", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_01").set("semiaxes", "0.00304522 0.00387642 0.00582605");
    model.component("comp1").geom("geom1").feature("lobule_bridge_01").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_01").set("pos", "0.00376612 0.05888476 0.00018612");
    model.component("comp1").geom("geom1").feature("lobule_bridge_01").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_01").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_03", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_03").set("semiaxes", "0.00304522 0.00408873 0.00585720");
    model.component("comp1").geom("geom1").feature("lobule_bridge_03").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_03").set("pos", "0.00285937 0.05860461 0.00229993");
    model.component("comp1").geom("geom1").feature("lobule_bridge_03").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_03").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_05", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_05").set("semiaxes", "0.00304522 0.00424438 0.00582757");
    model.component("comp1").geom("geom1").feature("lobule_bridge_05").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_05").set("pos", "0.00003147 0.05912921 0.00365958");
    model.component("comp1").geom("geom1").feature("lobule_bridge_05").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_05").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_07", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_07").set("semiaxes", "0.00304522 0.00415639 0.00576025");
    model.component("comp1").geom("geom1").feature("lobule_bridge_07").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_07").set("pos", "-0.00278392 0.05947484 0.00276551");
    model.component("comp1").geom("geom1").feature("lobule_bridge_07").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_07").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_09", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_09").set("semiaxes", "0.00304522 0.00386988 0.00580545");
    model.component("comp1").geom("geom1").feature("lobule_bridge_09").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_09").set("pos", "-0.00359984 0.05959578 0.00011360");
    model.component("comp1").geom("geom1").feature("lobule_bridge_09").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_09").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_11", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_11").set("semiaxes", "0.00304522 0.00415712 0.00576444");
    model.component("comp1").geom("geom1").feature("lobule_bridge_11").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_11").set("pos", "-0.00279933 0.05930676 -0.00282383");
    model.component("comp1").geom("geom1").feature("lobule_bridge_11").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_11").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_13", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_13").set("semiaxes", "0.00304522 0.00425124 0.00581461");
    model.component("comp1").geom("geom1").feature("lobule_bridge_13").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_13").set("pos", "0.00021595 0.05920789 -0.00370371");
    model.component("comp1").geom("geom1").feature("lobule_bridge_13").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_13").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_15", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_15").set("semiaxes", "0.00304522 0.00414687 0.00581596");
    model.component("comp1").geom("geom1").feature("lobule_bridge_15").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_15").set("pos", "0.00238633 0.05963657 -0.00258891");
    model.component("comp1").geom("geom1").feature("lobule_bridge_15").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_15").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_17", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_17").set("semiaxes", "0.00275519 0.00361344 0.00559923");
    model.component("comp1").geom("geom1").feature("lobule_bridge_17").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_17").set("pos", "0.00707720 0.05108123 0.00248926");
    model.component("comp1").geom("geom1").feature("lobule_bridge_17").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_17").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_19", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_19").set("semiaxes", "0.00275519 0.00380155 0.00557155");
    model.component("comp1").geom("geom1").feature("lobule_bridge_19").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_19").set("pos", "0.00454716 0.05131516 0.00623826");
    model.component("comp1").geom("geom1").feature("lobule_bridge_19").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_19").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_21", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_21").set("semiaxes", "0.00275519 0.00385846 0.00560430");
    model.component("comp1").geom("geom1").feature("lobule_bridge_21").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_21").set("pos", "0.00060510 0.05106722 0.00742335");
    model.component("comp1").geom("geom1").feature("lobule_bridge_21").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_21").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_23", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_23").set("semiaxes", "0.00275519 0.00381057 0.00554863");
    model.component("comp1").geom("geom1").feature("lobule_bridge_23").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_23").set("pos", "-0.00467601 0.05136435 0.00643849");
    model.component("comp1").geom("geom1").feature("lobule_bridge_23").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_23").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_25", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_25").set("semiaxes", "0.00275519 0.00359364 0.00560194");
    model.component("comp1").geom("geom1").feature("lobule_bridge_25").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_25").set("pos", "-0.00724053 0.05091951 0.00210882");
    model.component("comp1").geom("geom1").feature("lobule_bridge_25").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_25").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_27", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_27").set("semiaxes", "0.00275519 0.00360042 0.00560077");
    model.component("comp1").geom("geom1").feature("lobule_bridge_27").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_27").set("pos", "-0.00698914 0.05145057 -0.00217750");
    model.component("comp1").geom("geom1").feature("lobule_bridge_27").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_27").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_29", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_29").set("semiaxes", "0.00275519 0.00379357 0.00557994");
    model.component("comp1").geom("geom1").feature("lobule_bridge_29").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_29").set("pos", "-0.00463261 0.05126148 -0.00608471");
    model.component("comp1").geom("geom1").feature("lobule_bridge_29").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_29").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_31", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_31").set("semiaxes", "0.00275519 0.00386879 0.00558569");
    model.component("comp1").geom("geom1").feature("lobule_bridge_31").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_31").set("pos", "0.00006159 0.05148107 -0.00748099");
    model.component("comp1").geom("geom1").feature("lobule_bridge_31").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_31").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_33", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_33").set("semiaxes", "0.00275519 0.00379070 0.00558488");
    model.component("comp1").geom("geom1").feature("lobule_bridge_33").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_33").set("pos", "0.00463770 0.05121030 -0.00603772");
    model.component("comp1").geom("geom1").feature("lobule_bridge_33").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_33").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("lobule_bridge_35", "Ellipsoid");
    model.component("comp1").geom("geom1").feature("lobule_bridge_35").set("semiaxes", "0.00275519 0.00360795 0.00558881");
    model.component("comp1").geom("geom1").feature("lobule_bridge_35").set("axistype", "y");
    model.component("comp1").geom("geom1").feature("lobule_bridge_35").set("pos", "0.00740561 0.05070127 -0.00243872");
    model.component("comp1").geom("geom1").feature("lobule_bridge_35").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("lobule_bridge_35").set("selresultshow", "all");

    model.component("comp1").geom("geom1").create("gland_lobules", "Union");
    model.component("comp1").geom("geom1").feature("gland_lobules").selection("input").set("lobule_01", "lobule_02", "lobule_03", "lobule_04", "lobule_05", "lobule_06", "lobule_07", "lobule_08", "lobule_09", "lobule_10", "lobule_11", "lobule_12", "lobule_13", "lobule_14", "lobule_15", "lobule_16", "lobule_17", "lobule_18", "lobule_19", "lobule_20", "lobule_21", "lobule_22", "lobule_23", "lobule_24", "lobule_25", "lobule_26", "lobule_27", "lobule_28", "lobule_29", "lobule_30", "lobule_31", "lobule_32", "lobule_33", "lobule_34", "lobule_35", "lobule_36", "lobule_bridge_01", "lobule_bridge_03", "lobule_bridge_05", "lobule_bridge_07", "lobule_bridge_09", "lobule_bridge_11", "lobule_bridge_13", "lobule_bridge_15", "lobule_bridge_17", "lobule_bridge_19", "lobule_bridge_21", "lobule_bridge_23", "lobule_bridge_25", "lobule_bridge_27", "lobule_bridge_29", "lobule_bridge_31", "lobule_bridge_33", "lobule_bridge_35");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("intbnd", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("propagatesel", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("selresult", "on");
    model.component("comp1").geom("geom1").feature("gland_lobules").set("selresultshow", "all");
    model.component("comp1").geom("geom1").run("gland_lobules");
    String[] glandLobuleObjs = model.component("comp1").geom("geom1").feature("gland_lobules").objectNames();


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
