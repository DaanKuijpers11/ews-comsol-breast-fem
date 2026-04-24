from __future__ import annotations

import json
import logging
import os
import shutil
import subprocess
from pathlib import Path

from ews_fem_pipeline_comsol.settings import Settings

logger = logging.getLogger(__name__)


class COMSOLRunner:
    @staticmethod
    def _detect_license_error(text: str) -> bool:
        lower = text.lower()
        return "license error" in lower or "cannot connect to license server" in lower

    def _resolve_configuration_dir(self, settings: Settings, output_dir: Path) -> Path:
        if settings.comsol.configuration_dir:
            candidate = Path(settings.comsol.configuration_dir)
            if not candidate.is_absolute():
                candidate = (output_dir / candidate).resolve()
            else:
                candidate = candidate.resolve()
        elif "COMSOL_CONFIGURATION_DIR" in os.environ:
            candidate = Path(os.environ["COMSOL_CONFIGURATION_DIR"]).resolve()
        else:
            candidate = (output_dir / "comsol_configuration").resolve()
        candidate.mkdir(parents=True, exist_ok=True)
        return candidate

    def _resolve_batch_executable(self, settings: Settings) -> str | None:
        if settings.comsol.batch_executable:
            return settings.comsol.batch_executable
        if "COMSOL_BATCH_EXE" in os.environ:
            return os.environ["COMSOL_BATCH_EXE"]

        for candidate in ("comsolbatch", "comsol"):
            resolved = shutil.which(candidate)
            if resolved:
                return resolved
        return None

    def _resolve_comsol_executable(self, settings: Settings, batch_executable: str | None) -> str | None:
        if settings.comsol.comsol_executable:
            return settings.comsol.comsol_executable
        if "COMSOL_EXE" in os.environ:
            return os.environ["COMSOL_EXE"]
        resolved = shutil.which("comsol")
        if resolved:
            return resolved
        if batch_executable:
            batch_path = Path(batch_executable)
            candidate = batch_path.with_name("comsol.exe")
            if candidate.exists():
                return str(candidate)
        return None

    def _resolve_comsolcompile_executable(self, batch_executable: str | None) -> str | None:
        resolved = shutil.which("comsolcompile")
        if resolved:
            return resolved
        if batch_executable:
            batch_path = Path(batch_executable)
            candidate = batch_path.with_name("comsolcompile.exe")
            if candidate.exists():
                return str(candidate)
        return None

    @staticmethod
    def _run_logged_command(proc_args: list[str], cwd: Path, debug_path: Path) -> tuple[int, str, str]:
        result = subprocess.run(
            proc_args,
            cwd=str(cwd),
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
            encoding="utf-8",
            errors="replace",
        )
        debug_path.write_text(
            "\n".join(
                [
                    f"Command: {' '.join(proc_args)}",
                    f"Return code: {result.returncode}",
                    "",
                    "=== STDOUT ===",
                    result.stdout or "<empty>",
                    "",
                    "=== STDERR ===",
                    result.stderr or "<empty>",
                ]
            ),
            encoding="utf-8",
        )
        return result.returncode, result.stdout or "", result.stderr or ""

    def _try_build_mph_from_java(
        self,
        *,
        case_name: str,
        case_dir: Path,
        output_dir: Path,
        configuration_dir: Path,
        batch_executable: str,
        comsol_executable: str | None,
        comsolcompile_executable: str | None,
        builder_java: Path,
        generated_mph: Path,
        settings: Settings,
    ) -> tuple[bool, str]:
        build_log = output_dir / f"{case_name}_comsol_build.log"
        class_file = builder_java.with_suffix(".class")
        jdk_root = settings.comsol.jdk_root or os.environ.get("JAVA_HOME")

        # Preferred route: compile Java source to class, then run class via comsolbatch.
        if comsolcompile_executable:
            compile_args = [str(comsolcompile_executable)]
            if jdk_root:
                compile_args.extend(["-jdkroot", str(jdk_root)])
            compile_args.append(str(builder_java.resolve()))
            compile_debug = output_dir / f"{case_name}_comsol_compile_debug.log"
            compile_code, compile_out, compile_err = self._run_logged_command(compile_args, case_dir, compile_debug)
            compile_text = "\n".join([compile_out, compile_err])

            if compile_code == 0 and class_file.exists():
                class_args = [
                    str(batch_executable),
                    "-configuration",
                    str(configuration_dir),
                    "-inputfile",
                    str(class_file.resolve()),
                    "-outputfile",
                    str(generated_mph.resolve()),
                    "-batchlog",
                    str(build_log.resolve()),
                    *settings.comsol.extra_args,
                ]
                class_debug = output_dir / f"{case_name}_comsol_build_class_debug.log"
                class_code, class_out, class_err = self._run_logged_command(class_args, case_dir, class_debug)
                class_log_text = build_log.read_text(encoding="utf-8", errors="replace") if build_log.exists() else ""
                class_text = "\n".join([class_out, class_err, class_log_text])
                if class_code == 0 and generated_mph.exists():
                    return True, ""
                if self._detect_license_error(class_text):
                    return False, "COMSOL license error during class-based MPH build (license server unreachable)."
                if "model file is damaged or not valid" in class_text.lower():
                    return False, "COMSOL could not execute compiled class input. Check class generation and COMSOL batch syntax."
                return False, "Class-based build executed but no MPH file was produced."

            if not class_file.exists():
                if not jdk_root:
                    return False, "Java compile failed: no JDK detected. Install a JDK and set JAVA_HOME (or comsol.jdk_root)."
                return False, "Java compile failed: no .class generated. Check compile debug log."
            if self._detect_license_error(compile_text):
                return False, "COMSOL license error during Java compile step (license server unreachable)."
            return False, "Java compile step failed before class execution."

        if settings.comsol.java_compile_first:
            return False, "comsolcompile executable not found. Cannot auto-build from Java without compiler."

        # Fallback route: direct Java source as input file (often unsupported).
        direct_args = [
            str(batch_executable),
            "-configuration",
            str(configuration_dir),
            "-inputfile",
            str(builder_java.resolve()),
            "-outputfile",
            str(generated_mph.resolve()),
            "-batchlog",
            str(build_log.resolve()),
            *settings.comsol.extra_args,
        ]
        direct_debug = output_dir / f"{case_name}_comsol_build_direct_debug.log"
        code, stdout, stderr = self._run_logged_command(direct_args, case_dir, direct_debug)
        build_log_text = build_log.read_text(encoding="utf-8", errors="replace") if build_log.exists() else ""
        direct_text = "\n".join([stdout, stderr, build_log_text])
        if code == 0 and generated_mph.exists():
            return True, ""
        if self._detect_license_error(direct_text):
            return False, "COMSOL license error during Java->MPH build (license server unreachable)."
        if "model file is damaged or not valid" in direct_text.lower():
            return False, "Direct Java input is not accepted by this COMSOL batch setup. Use comsolcompile + class route."

        return False, "Java builder ran but no MPH file was produced."

    def run(self, input_files: tuple[Path, ...], settings_map: dict[Path, Settings]) -> tuple[Path, ...]:
        completed: list[Path] = []
        for input_file in input_files:
            settings = settings_map[input_file]
            if self.run_case(input_file, settings):
                completed.append(input_file)
        return tuple(completed)

    def run_case(self, input_file: Path, settings: Settings) -> bool:
        assert input_file.suffix == ".json", "COMSOL runner expects JSON case input files."
        payload = json.loads(input_file.read_text(encoding="utf-8"))
        case_name = payload["case_name"]
        case_dir = Path(payload["case_dir"])
        output_dir = case_dir / settings.pipeline.output_subdir
        output_dir.mkdir(parents=True, exist_ok=True)
        command_preview_file = output_dir / f"{case_name}.comsol_command.txt"
        prepare_artefacts = payload.get("prepare_artefacts", {})
        configuration_dir = self._resolve_configuration_dir(settings, output_dir)

        batch_executable = self._resolve_batch_executable(settings)
        comsol_executable = self._resolve_comsol_executable(settings, batch_executable)
        comsolcompile_executable = self._resolve_comsolcompile_executable(batch_executable)
        mph_file = settings.comsol.mph_file or ""

        if not batch_executable:
            logger.warning("Skipping %s: COMSOL batch executable not found.", case_name)
            return False

        configured_mph = Path(mph_file).resolve() if mph_file else None
        builder_java = Path(prepare_artefacts.get("comsol_builder_java", "")) if prepare_artefacts.get("comsol_builder_java") else None
        generated_mph_target = Path(prepare_artefacts.get("comsol_generated_mph_target", "")) if prepare_artefacts.get("comsol_generated_mph_target") else None

        source_mph = configured_mph if configured_mph and configured_mph.exists() else None
        planned_commands: list[str] = []
        build_failure_reason = ""

        if source_mph is None and settings.comsol.auto_build_from_java and builder_java and generated_mph_target:
            build_log = output_dir / f"{case_name}_comsol_build.log"
            planned_build_cmd = " ".join(
                [
                    str(batch_executable),
                    "-configuration",
                    str(configuration_dir),
                    "-inputfile",
                    str(builder_java.resolve()),
                    "-outputfile",
                    str(generated_mph_target.resolve()),
                    "-batchlog",
                    str(build_log.resolve()),
                    *settings.comsol.extra_args,
                ]
            )
            planned_commands.append(planned_build_cmd)
            if settings.comsol.java_compile_first and comsol_executable:
                planned_commands.append(f"{comsol_executable} compile {builder_java.resolve()}")

            if settings.comsol.execute:
                logger.info("Building MPH from Java scaffold for %s", case_name)
                built, reason = self._try_build_mph_from_java(
                    case_name=case_name,
                    case_dir=case_dir,
                    output_dir=output_dir,
                    configuration_dir=configuration_dir,
                    batch_executable=batch_executable,
                    comsol_executable=comsol_executable,
                    comsolcompile_executable=comsolcompile_executable,
                    builder_java=builder_java,
                    generated_mph=generated_mph_target,
                    settings=settings,
                )
                if built:
                    source_mph = generated_mph_target.resolve()
                else:
                    build_failure_reason = reason

        if source_mph is None and configured_mph and configured_mph.exists():
            source_mph = configured_mph
        if source_mph is None and generated_mph_target and generated_mph_target.exists():
            source_mph = generated_mph_target.resolve()
        if source_mph is None:
            if build_failure_reason:
                logger.warning("%s: %s", case_name, build_failure_reason)
            if settings.comsol.execute:
                logger.warning(
                    "Skipping %s: no readable MPH available. Configure comsol.mph_file or enable Java auto-build with generated artefacts.",
                    case_name,
                )
            else:
                logger.info(
                    "Prepared build command for %s (execute=false, MPH not built yet).",
                    case_name,
                )
            if planned_commands:
                command_preview_file.write_text("\n".join(planned_commands) + "\n", encoding="utf-8")
            return not settings.comsol.execute

        output_mph = output_dir / f"{case_name}_result.mph"
        log_file = output_dir / f"{case_name}_comsol.log"

        proc_args = [
            str(batch_executable),
            "-configuration",
            str(configuration_dir),
            "-inputfile",
            str(source_mph),
            "-outputfile",
            str(output_mph.resolve()),
            "-study",
            settings.comsol.study,
            "-batchlog",
            str(log_file.resolve()),
            *settings.comsol.extra_args,
        ]
        planned_commands.append(" ".join(proc_args))
        command_preview_file.write_text("\n".join(planned_commands) + "\n", encoding="utf-8")

        if not settings.comsol.execute:
            logger.info("Prepared COMSOL command for %s (execute=false).", case_name)
            return True

        logger.info("Running COMSOL for %s", case_name)
        debug_path = output_dir / f"{case_name}_comsol_runner_debug.log"
        code, _, _ = self._run_logged_command(proc_args, case_dir, debug_path)
        if code != 0:
            logger.error("COMSOL failed for %s. Debug: %s", case_name, debug_path)
            return False

        return True
