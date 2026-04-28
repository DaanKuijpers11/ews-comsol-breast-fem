from __future__ import annotations

import json
import re
from pathlib import Path


def _safe_java_identifier(text: str) -> str:
    identifier = re.sub(r"[^0-9a-zA-Z_]", "_", text)
    if not identifier:
        identifier = "comsol_case"
    if identifier[0].isdigit():
        identifier = f"case_{identifier}"
    return identifier


def generate_comsol_java_builder(
    *,
    case_name: str,
    output_dir: Path,
    prepare_artefacts: dict[str, str],
) -> dict[str, str]:
    """
    Generate COMSOL Java API scaffolding from exported prepare artefacts.
    """
    output_dir.mkdir(parents=True, exist_ok=True)
    class_name = _safe_java_identifier(f"{case_name}_comsol_builder")

    build_plan_path = prepare_artefacts.get("comsol_build_plan_json", "")
    build_plan_summary = {"lobule_count": 0}
    if build_plan_path and Path(build_plan_path).exists():
        plan = json.loads(Path(build_plan_path).read_text(encoding="utf-8"))
        build_plan_summary["lobule_count"] = len(plan.get("lobules", []))

    script_path = output_dir / f"{class_name}.java"
    result_mph = (output_dir / f"{case_name}_generated.mph").resolve()

    java_source = f"""import com.comsol.model.*;
import com.comsol.model.util.*;

public class {class_name} {{
  public static Model run() {{
    ModelUtil.initStandalone(true);
    Model model = ModelUtil.create("Model");
    model.label("{result_mph.name}");

    // Base component/geometry
    model.component().create("comp1", true);
    model.component("comp1").geom().create("geom1", 3);
    model.component("comp1").mesh().create("mesh1");

    // Auto-generated pointers:
    // - Build plan JSON: {build_plan_path}
    // - Lobules in plan: {build_plan_summary["lobule_count"]}
    //
    // Next step in this script:
    // 1) create/import breast geometry in geom1
    // 2) map glandular/adipose regions from build plan
    // 3) assign materials and studies
    // 4) run and save MPH

    model.component("comp1").geom("geom1").run();
    return model;
  }}

  public static void main(String[] args) throws Exception {{
    Model model = run();
    model.save("{result_mph.as_posix()}");
    ModelUtil.disconnect();
  }}
}}
"""
    script_path.write_text(java_source, encoding="utf-8")

    readme_path = output_dir / f"{case_name}_comsol_builder_README.txt"
    readme_path.write_text(
        "\n".join(
            [
                "Generated COMSOL builder scaffold",
                f"Java source: {script_path}",
                f"Build plan: {build_plan_path}",
                f"Target MPH: {result_mph}",
                "",
                "Typical next step:",
                "1) Open/compile this Java file via COMSOL Java API tooling",
                "2) Fill geometry/material/study steps based on build plan JSON",
                "3) Run to create generated MPH",
            ]
        )
        + "\n",
        encoding="utf-8",
    )

    return {
        "comsol_builder_java": str(script_path.resolve()),
        "comsol_builder_readme": str(readme_path.resolve()),
        "comsol_generated_mph_target": str(result_mph.resolve()),
    }
