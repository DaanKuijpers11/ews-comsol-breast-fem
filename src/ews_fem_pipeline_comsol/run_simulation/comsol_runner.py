from __future__ import annotations

import json
import logging
import os
import shutil
import subprocess
from pathlib import Path

from ews_fem_pipeline_comsol.paths import ensure_output_tree
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

    @staticmethod
    def _resolve_generated_mph_candidate(generated_mph: Path) -> Path | None:
        if generated_mph.exists():
            return generated_mph.resolve()
        fallback = generated_mph.with_name(f"{generated_mph.stem}_Model{generated_mph.suffix}")
        if fallback.exists():
            return fallback.resolve()
        return None

    def check_license(self, settings: Settings, workdir: Path) -> tuple[bool, str]:
        """
        Fast COMSOL license probe.
        Returns (ok, message).
        """
        batch_executable = self._resolve_batch_executable(settings)
        if not batch_executable:
            return False, "COMSOL batch executable not found."

        output_paths = ensure_output_tree(workdir, settings)
        build_dir = output_paths["build"]
        logs_dir = output_paths["logs"]
        configuration_dir = self._resolve_configuration_dir(settings, build_dir)
        log_file = logs_dir / "comsol_license_check.log"
        debug_file = logs_dir / "comsol_license_check_debug.log"

        # Intentionally pass a non-existing input file:
        # - If license is down: COMSOL returns license error (-15 etc.)
        # - If license is up: COMSOL proceeds further and reports file/read issue.
        dummy_input = build_dir / "__license_probe_input__.mph"
        args = [
            str(batch_executable),
            "-configuration",
            str(configuration_dir),
            "-inputfile",
            str(dummy_input),
            "-batchlog",
            str(log_file),
        ]
        code, out, err = self._run_logged_command(args, workdir, debug_file, timeout_s=40)
        text_parts = [out, err]
        if log_file.exists():
            text_parts.append(log_file.read_text(encoding="utf-8", errors="replace"))
        combined = "\n".join(text_parts).lower()

        if self._detect_license_error(combined):
            return False, "License check failed: COMSOL cannot reach a valid license."

        if code == 124:
            return False, "License check timed out (possible environment/VPN/session issue)."

        # No license error found; treat as license reachable.
        return True, "License check passed: no COMSOL license error detected."

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
    def _resolve_javac_executable(jdk_root: str | None) -> str | None:
        if not jdk_root:
            return None
        candidate = Path(jdk_root) / "bin" / "javac.exe"
        if candidate.exists():
            return str(candidate)
        return None

    @staticmethod
    def _run_logged_command(
        proc_args: list[str],
        cwd: Path,
        debug_path: Path,
        timeout_s: int = 120,
    ) -> tuple[int, str, str]:
        try:
            result = subprocess.run(
                proc_args,
                cwd=str(cwd),
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
                text=True,
                encoding="utf-8",
                errors="replace",
                timeout=timeout_s,
            )
            code = result.returncode
            stdout = result.stdout or ""
            stderr = result.stderr or ""
        except subprocess.TimeoutExpired as exc:
            code = 124
            stdout = (exc.stdout or "") if isinstance(exc.stdout, str) else ""
            stderr = (exc.stderr or "") if isinstance(exc.stderr, str) else ""
            stderr = (stderr + "\nCommand timed out.").strip()

        debug_path.write_text(
            "\n".join(
                [
                    f"Command: {' '.join(proc_args)}",
                    f"Return code: {code}",
                    "",
                    "=== STDOUT ===",
                    stdout or "<empty>",
                    "",
                    "=== STDERR ===",
                    stderr or "<empty>",
                ]
            ),
            encoding="utf-8",
        )
        return code, stdout, stderr

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
        javac_executable = self._resolve_javac_executable(jdk_root)
        multiphysics_root = Path(batch_executable).resolve().parents[2]
        plugins_dir = multiphysics_root / "plugins"

        # Preferred route: plain javac with COMSOL plugin jars on classpath.
        if javac_executable and plugins_dir.exists():
            javac_cp = str(plugins_dir / "*")
            javac_args = [
                str(javac_executable),
                "-proc:none",
                "-cp",
                javac_cp,
                "-d",
                str(builder_java.parent.resolve()),
                str(builder_java.resolve()),
            ]
            javac_debug = output_dir / f"{case_name}_javac_compile_debug.log"
            javac_code, javac_out, javac_err = self._run_logged_command(javac_args, case_dir, javac_debug)
            if javac_code == 0 and class_file.exists():
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
                generated_candidate = self._resolve_generated_mph_candidate(generated_mph)
                if class_code == 0 and generated_candidate is not None:
                    return True, ""
                if self._detect_license_error(class_text):
                    return False, "COMSOL license error during class-based MPH build (license server unreachable)."
                if "model file is damaged or not valid" in class_text.lower():
                    return False, "COMSOL rejected compiled class input. Check class execution route."
                return False, "Class-based build executed but no MPH file was produced."

            javac_text = "\n".join([javac_out, javac_err])
            if "error:" in javac_text.lower():
                return False, "javac compile failed. See *_javac_compile_debug.log for details."

        # Secondary route: COMSOL's own compiler.
        if comsolcompile_executable:
            compile_args = [str(comsolcompile_executable)]
            if jdk_root:
                compile_args.extend(["-jdkroot", str(jdk_root)])
            compile_args.append(str(builder_java.resolve()))
            compile_debug = output_dir / f"{case_name}_comsol_compile_debug.log"
            compile_code, compile_out, compile_err = self._run_logged_command(compile_args, case_dir, compile_debug)
            compile_text = "\n".join([compile_out, compile_err])
            if self._detect_license_error(compile_text):
                return False, "COMSOL license error during Java compile step (license server unreachable)."
            if compile_code == 124:
                return False, "comsolcompile timed out. Check *_comsol_compile_debug.log."
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
                generated_candidate = self._resolve_generated_mph_candidate(generated_mph)
                if class_code == 0 and generated_candidate is not None:
                    return True, ""
                if self._detect_license_error(class_text):
                    return False, "COMSOL license error during class-based MPH build (license server unreachable)."
                return False, "Class-based build after comsolcompile did not produce MPH."

        if settings.comsol.java_compile_first:
            if not comsolcompile_executable and not javac_executable:
                return False, "No Java compiler available. Install JDK and set JAVA_HOME (or comsol.jdk_root)."
            if jdk_root and not javac_executable:
                return False, "JDK root configured but javac.exe not found under jdk_root/bin."
            return False, "Java compile failed: no .class generated. Check compile debug logs."

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
        generated_candidate = self._resolve_generated_mph_candidate(generated_mph)
        if code == 0 and generated_candidate is not None:
            return True, ""
        if self._detect_license_error(direct_text):
            return False, "COMSOL license error during Java->MPH build (license server unreachable)."
        if "model file is damaged or not valid" in direct_text.lower():
            return False, "Direct Java input is not accepted by this COMSOL batch setup. Use comsolcompile + class route."

        return False, "Java builder ran but no MPH file was produced."

    def _run_aux_java_class(
        self,
        *,
        case_name: str,
        case_dir: Path,
        logs_dir: Path,
        configuration_dir: Path,
        batch_executable: str,
        java_file: Path,
        settings: Settings,
    ) -> tuple[bool, str, str]:
        class_file = java_file.with_suffix(".class")
        jdk_root = settings.comsol.jdk_root or os.environ.get("JAVA_HOME")
        javac_executable = self._resolve_javac_executable(jdk_root)
        multiphysics_root = Path(batch_executable).resolve().parents[2]
        plugins_dir = multiphysics_root / "plugins"

        if not javac_executable or not plugins_dir.exists():
            return False, "No Java compiler available for COMSOL postprocess step.", ""

        javac_args = [
            str(javac_executable),
            "-proc:none",
            "-cp",
            str(plugins_dir / "*"),
            "-d",
            str(java_file.parent.resolve()),
            str(java_file.resolve()),
        ]
        javac_debug = logs_dir / f"{case_name}_{java_file.stem}_javac_debug.log"
        javac_code, javac_out, javac_err = self._run_logged_command(javac_args, case_dir, javac_debug)
        if javac_code != 0 or not class_file.exists():
            return False, f"Failed to compile auxiliary COMSOL Java class {java_file.name}.", ""

        run_log = logs_dir / f"{case_name}_{java_file.stem}.log"
        class_args = [
            str(batch_executable),
            "-configuration",
            str(configuration_dir),
            "-inputfile",
            str(class_file.resolve()),
            "-outputfile",
            str(class_file.with_name(f"{java_file.stem}_output.mph").resolve()),
            "-batchlog",
            str(run_log.resolve()),
            *settings.comsol.extra_args,
        ]
        class_debug = logs_dir / f"{case_name}_{java_file.stem}_debug.log"
        class_code, class_out, class_err = self._run_logged_command(class_args, case_dir, class_debug)
        run_log_text = run_log.read_text(encoding="utf-8", errors="replace") if run_log.exists() else ""
        run_text = "\n".join([class_out, class_err, run_log_text])
        if class_code != 0:
            return False, f"Auxiliary COMSOL Java class {java_file.name} failed.", class_out
        if self._detect_license_error(run_text):
            return False, "COMSOL license error during postprocess metrics export.", class_out
        return True, "", class_out

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
        output_paths = ensure_output_tree(case_dir, settings)
        build_dir = output_paths["build"]
        solve_dir = output_paths["solve"]
        logs_dir = output_paths["logs"]
        command_preview_file = logs_dir / f"{case_name}.comsol_command.txt"
        prepare_artefacts = payload.get("prepare_artefacts", {})
        configuration_dir = self._resolve_configuration_dir(settings, build_dir)

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
        build_attempted = False
        build_succeeded = False

        if source_mph is None and settings.comsol.auto_build_from_java and builder_java and generated_mph_target:
            build_attempted = settings.comsol.execute
            build_log = logs_dir / f"{case_name}_comsol_build.log"
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
                    output_dir=logs_dir,
                    configuration_dir=configuration_dir,
                    batch_executable=batch_executable,
                    comsol_executable=comsol_executable,
                    comsolcompile_executable=comsolcompile_executable,
                    builder_java=builder_java,
                    generated_mph=generated_mph_target,
                    settings=settings,
                )
                if built:
                    build_succeeded = True
                    source_mph = self._resolve_generated_mph_candidate(generated_mph_target)
                else:
                    build_failure_reason = reason

        if source_mph is None and configured_mph and configured_mph.exists():
            source_mph = configured_mph
        if source_mph is None and generated_mph_target and not build_attempted:
            source_mph = self._resolve_generated_mph_candidate(generated_mph_target)
        if source_mph is None and generated_mph_target and build_attempted and build_succeeded:
            source_mph = self._resolve_generated_mph_candidate(generated_mph_target)
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

        output_mph = solve_dir / f"{case_name}_result.mph"
        log_file = logs_dir / f"{case_name}_comsol.log"

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
        debug_path = logs_dir / f"{case_name}_comsol_runner_debug.log"
        code, _, _ = self._run_logged_command(proc_args, case_dir, debug_path)
        if code != 0:
            logger.error("COMSOL failed for %s. Debug: %s", case_name, debug_path)
            return False

        postprocess_java = (
            Path(prepare_artefacts.get("comsol_postprocess_java", ""))
            if prepare_artefacts.get("comsol_postprocess_java")
            else None
        )
        if postprocess_java and postprocess_java.exists():
            ok, reason, aux_stdout = self._run_aux_java_class(
                case_name=case_name,
                case_dir=case_dir,
                logs_dir=logs_dir,
                configuration_dir=configuration_dir,
                batch_executable=batch_executable,
                java_file=postprocess_java,
                settings=settings,
            )
            if not ok:
                logger.warning("%s: %s", case_name, reason)
            else:
                metrics_target = (
                    Path(prepare_artefacts.get("comsol_metrics_json_target", ""))
                    if prepare_artefacts.get("comsol_metrics_json_target")
                    else None
                )
                if metrics_target:
                    begin_marker = "COMSOL_METRICS_JSON_BEGIN"
                    end_marker = "COMSOL_METRICS_JSON_END"
                    start_idx = aux_stdout.find(begin_marker)
                    end_idx = aux_stdout.find(end_marker)
                    if start_idx != -1 and end_idx != -1 and end_idx > start_idx:
                        metrics_json = aux_stdout[start_idx + len(begin_marker):end_idx].strip()
                        metrics_target.parent.mkdir(parents=True, exist_ok=True)
                        metrics_target.write_text(metrics_json + "\n", encoding="utf-8")
                    else:
                        logger.warning("%s: postprocess ran but did not emit metrics JSON markers.", case_name)

        return True
