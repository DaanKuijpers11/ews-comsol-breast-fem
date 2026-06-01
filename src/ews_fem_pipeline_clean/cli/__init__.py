import argparse
from pathlib import Path

from ews_fem_pipeline_clean.__about__ import __version__


def _build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(
        prog="ews_fem_pipeline_clean",
        description="Clean FEBio breast FEM pipeline.",
    )
    parser.add_argument("--version", action="version", version=f"%(prog)s {__version__}")

    subparsers = parser.add_subparsers(dest="command", required=True)

    generate_parser = subparsers.add_parser("generate", help="Generate FEBio input files from TOML settings.")
    generate_parser.add_argument("input_files", nargs="+", type=Path)

    fem_parser = subparsers.add_parser("fem", help="Run FEBio on one or more .feb files.")
    fem_parser.add_argument("input_files", nargs="+", type=Path)
    fem_parser.add_argument("-j", "--jobs", type=int, default=0)

    convert_parser = subparsers.add_parser("convert", help="Convert FEBio output for Blender.")
    convert_parser.add_argument("input_files", nargs="+", type=Path)

    defaults_parser = subparsers.add_parser("write-default-settings", help="Write a TOML file with default settings.")
    defaults_parser.add_argument("filepath", type=Path)

    run_parser = subparsers.add_parser("run", help="Run generate, fem and convert in sequence.")
    run_parser.add_argument("input_files", nargs="+", type=Path)
    run_parser.add_argument("-j", "--jobs", type=int, default=0)

    sweep_parser = subparsers.add_parser("sweep", help="Batch-run multiple TOML model configurations.")
    sweep_parser.add_argument("input_files", nargs="+", type=Path)
    sweep_parser.add_argument("-j", "--jobs", type=int, default=0)
    sweep_parser.add_argument("--evaluate", action="store_true", help="Run analysis after the batch finishes.")

    evaluate_parser = subparsers.add_parser("evaluate", help="Evaluate one or more completed runs.")
    evaluate_parser.add_argument("input_files", nargs="+", type=Path)

    compare_parser = subparsers.add_parser("compare", help="Build a compact comparison summary for multiple cases.")
    compare_parser.add_argument("input_files", nargs="+", type=Path)
    compare_parser.add_argument("--baseline", type=str, default=None, help="Case name to use as baseline.")
    compare_parser.add_argument(
        "--output-dir",
        type=Path,
        default=None,
        help="Optional directory for compare outputs, relative to the workspace root or absolute.",
    )

    return parser


def cli_main(argv: list[str] | None = None) -> int:
    parser = _build_parser()
    args = parser.parse_args(argv)

    if args.command == "generate":
        from ews_fem_pipeline_clean.pipeline import generate_cases

        generate_cases(tuple(args.input_files))
        return 0

    if args.command == "fem":
        from ews_fem_pipeline_clean.pipeline import run_febio_cases

        run_febio_cases(tuple(args.input_files), jobs=args.jobs)
        return 0

    if args.command == "convert":
        from ews_fem_pipeline_clean.pipeline import convert_cases

        convert_cases(tuple(args.input_files))
        return 0

    if args.command == "write-default-settings":
        from ews_fem_pipeline_clean.config import default_settings, write_settings

        write_settings(args.filepath, default_settings())
        return 0

    if args.command == "run":
        from ews_fem_pipeline_clean.pipeline import run_full_pipeline

        run_full_pipeline(tuple(args.input_files), jobs=args.jobs)
        return 0

    if args.command == "sweep":
        from ews_fem_pipeline_clean.pipeline import sweep_cases

        sweep_cases(tuple(args.input_files), jobs=args.jobs, evaluate=args.evaluate)
        return 0

    if args.command == "evaluate":
        from ews_fem_pipeline_clean.pipeline import evaluate_cases

        evaluate_cases(tuple(args.input_files))
        return 0

    if args.command == "compare":
        from ews_fem_pipeline_clean.pipeline import compare_case_summaries

        compare_case_summaries(tuple(args.input_files), baseline=args.baseline, output_dir=args.output_dir)
        return 0

    parser.error(f"Unknown command: {args.command}")
    return 2
