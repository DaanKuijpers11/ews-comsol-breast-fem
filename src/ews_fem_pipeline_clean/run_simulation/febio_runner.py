import os
import shutil
import subprocess
import multiprocessing
import logging
import re
import textwrap
from enum import Enum
from functools import partial
from multiprocessing.pool import ThreadPool
from pathlib import Path

from tqdm import tqdm

from ews_fem_pipeline_clean.febio_settings import Settings

logger = logging.getLogger(__name__)


class FEBioRunner:
    febio_executable: Path | None = None

    def __init__(self):
        self.resolve_febio_executable()

    def resolve_febio_executable(self):
        # Look for environment variable FEBIO_PATH: if that exists, it should point towards the executable (or folder with executable); if it doesn't exist, look for it on the path, or some other default locations
        if "FEBIO_PATH" in os.environ:
            febio_path = Path(os.environ["FEBIO_PATH"])
            logger.debug(f"Looking for febio on the FEBIO_PATH: {febio_path}.")

            if febio_path.is_dir():
                febio_path /= Settings.febio_default_exe_name

            self.febio_executable = febio_path


        # Look on path (extended by some default search directories)
        else:
            logger.debug(f"Looking for febio on the (extended) system path.")
            search_path = os.environ["PATH"]
            search_path = os.pathsep.join([search_path, *Settings.febio_search_path_extension])
            febio_path = shutil.which("febio4", path=search_path)

            if febio_path is not None:
                self.febio_executable = Path(febio_path)

        if self.febio_executable is not None:
            logger.debug(f"Found FEBio executable: {self.febio_executable}.")
        else:
            logger.error("Did not find FEBio executable.")
            raise FileNotFoundError("Did not find FEBio executable.")

    def run(self, input_files: tuple[Path], n_processes: int = 1):
        assert all(f.is_file() for f in input_files)
        assert n_processes >= 1

        run_fn = partial(self.run_simulation, allow_OMP=(n_processes == 1))
        progbar = partial(tqdm, total=len(input_files), ncols=80)

        if n_processes == 1:
            list(progbar(map(run_fn, input_files)))
        else:
            with ThreadPool(n_processes) as pool:
                list(progbar(pool.imap(run_fn, input_files)))

        logger.info("\n# Finished, checking all result files")

        output_files = []
        # Check all resulting files
        for input_file in input_files:
            state, total_time = self.check_termination(input_file)
            logger.info(state.format(name=input_file.name, time=total_time))

            if state == FEBioRunner.TERMINATIONSTATES.NORMALTERMINATION:
                output_files.append(input_file)
        return tuple(output_files)

    @staticmethod
    def _write_solver_config(config_path: Path, linear_solver: str) -> None:
        config_text = textwrap.dedent(
            f"""\
            <?xml version="1.0" encoding="ISO-8859-1"?>
            <febio_config version="3.0">
              <default_linear_solver type="{linear_solver}"/>
            </febio_config>
            """
        )
        config_path.write_text(config_text, encoding="ISO-8859-1")

    def run_simulation(self, input_file: Path, allow_OMP: bool = True):
        run_logger = logging.getLogger(__name__)
        env = os.environ.copy()
        input_file = Path(input_file).resolve()
        logfile = input_file.with_suffix(".log")
        config_file = input_file.with_suffix(".febio_config.xml")

        # Conservative default for FEBio stability on Windows/PARDISO.
        # Users can override with FEBIO_OMP_THREADS, e.g. set FEBIO_OMP_THREADS=4.
        requested_threads = env.get("FEBIO_OMP_THREADS")
        if requested_threads is not None:
            env["OMP_NUM_THREADS"] = requested_threads
        else:
            env["OMP_NUM_THREADS"] = "1"

        linear_solver = env.get("FEBIO_LINEAR_SOLVER", "skyline")
        self._write_solver_config(config_file, linear_solver)

        run_logger.info(f"Started running {input_file.name}")
        run_logger.info("Using OMP_NUM_THREADS=%s", env["OMP_NUM_THREADS"])
        run_logger.info("Using FEBio linear solver=%s (via config)", linear_solver)

        # Use explicit FEBio CLI flags so invocation is stable across FEBio versions.
        proc_args = [
            str(self.febio_executable),
            "-config",
            str(config_file),
            "-i",
            str(input_file),
            "-o",
            str(logfile),
        ]
        run_logger.debug(f" " + " ".join(proc_args))

        result = subprocess.run(
            proc_args,
            env=env,
            cwd=str(input_file.parent),
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
            encoding="utf-8",
            errors="replace",
        )

        if result.returncode != 0:
            debug_log = input_file.with_suffix(".runner_debug.log")
            debug_log.write_text(
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
            run_logger.error(
                "FEBio exited with code %s for %s. Debug info: %s",
                result.returncode,
                input_file.name,
                debug_log,
            )

    # Info for reading end of the log file
    TAIL_LENGTH = 4096  # bytes to read from end of file

    class TERMINATIONSTATES(str, Enum):
        NORMALTERMINATION = "{name} terminated successfully in {time} seconds."
        ERRORTERMINATION = "{name} terminated unsuccessfully in {time} seconds."
        NOTERMINATION = "{name} terminated unexpectedly (no termination state found)."
        NOLOGFILE = "{name} most likely didn't run (no log file found)."

    TERMINATIONPATTERNS = {
        TERMINATIONSTATES.NORMALTERMINATION: re.compile(r"N\s*O\s*R\s*M\s*A\s*L\s*T\s*E\s*R\s*M\s*I\s*N\s*A\s*T\s*I\s*O\s*N", re.IGNORECASE),
        TERMINATIONSTATES.ERRORTERMINATION: re.compile(r"E\s*R\s*R\s*O\s*R\s*T\s*E\s*R\s*M\s*I\s*N\s*A\s*T\s*I\s*O\s*N", re.IGNORECASE),
    }

    TIMEPATTERN = re.compile(r"Total elapsed time [.]* : [\d:]* \(([\d.]*) sec\)")

    def check_termination(self, input_file: Path) -> tuple[TERMINATIONSTATES, float]:
        logfile = input_file.with_suffix(".log")

        total_time = -1
        term_state = self.TERMINATIONSTATES.NOTERMINATION

        if not logfile.is_file():
            term_state = self.TERMINATIONSTATES.NOLOGFILE
            return term_state, total_time

        with open(logfile, "rb") as file:
            # Read 100 byes from end of file
            # file.seek(-self.TAIL_LENGTH, 2)

            file.seek(0, 2)
            file_size = file.tell()
            file.seek(max(0, file_size - self.TAIL_LENGTH), 0)

            tail = file.read(self.TAIL_LENGTH).decode("utf-8", errors="replace")

            for state, pattern in self.TERMINATIONPATTERNS.items():
                if pattern.search(tail):
                    term_state = state
                    break

            if match := self.TIMEPATTERN.search(tail):
                total_time = float(match.group(1))

        # If termination text is missing but VTK files exist, treat as successful output.
        if term_state == self.TERMINATIONSTATES.NOTERMINATION:
            vtk_dir = input_file.parent / "output"
            vtk_files = list(vtk_dir.glob(f"{input_file.stem}.*.vtk"))
            input_mtime = input_file.stat().st_mtime if input_file.exists() else 0.0
            fresh_vtks = [vtk for vtk in vtk_files if vtk.stat().st_mtime >= input_mtime]
            if fresh_vtks:
                term_state = self.TERMINATIONSTATES.NORMALTERMINATION

        return term_state, total_time
