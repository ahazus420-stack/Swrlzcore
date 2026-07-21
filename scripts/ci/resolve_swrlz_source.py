#!/usr/bin/env python3
"""Resolve a canonical SWRLZ Android source archive.

Transport duplicate suffixes such as ``(1)``, `` (2)``, or ``(204)`` are
accepted for ZIP and checksum filenames, but they never participate in
software-version ordering or artifact identity.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import re
import sys
from dataclasses import dataclass
from pathlib import Path
from typing import Sequence


SHA256_RE = re.compile(r"(?i)(?<![0-9a-f])([0-9a-f]{64})(?![0-9a-f])")
DUPLICATE_SUFFIX_RE = re.compile(r"\s*\((\d+)\)$")
CHECKSUM_EXTENSIONS = (".sha256", ".sha", ".txt")


class ResolutionError(RuntimeError):
    """Raised when a source cannot be resolved without ambiguity."""


@dataclass(frozen=True)
class ComponentSpec:
    component: str
    lane: str
    stem_pattern: re.Pattern[str]


@dataclass(frozen=True)
class TransportName:
    original_name: str
    canonical_stem: str
    duplicate_suffix: int | None
    version: tuple[int, ...] | None

    @property
    def canonical_zip_name(self) -> str:
        return f"{self.canonical_stem}.zip"


@dataclass(frozen=True)
class FileCandidate:
    path: Path
    transport: TransportName
    sha256: str


COMPONENTS: dict[str, ComponentSpec] = {
    "CLIENT": ComponentSpec(
        component="CLIENT",
        lane="SOURCES/CLIENT",
        stem_pattern=re.compile(
            r"CLIENT_CFv(?P<version>\d+\.\d+\.\d+)_SWRLZ", re.IGNORECASE
        ),
    ),
    "SERVER": ComponentSpec(
        component="SERVER",
        lane="SOURCES/SERVER",
        stem_pattern=re.compile(
            r"SERVER_CFv(?P<version>\d+\.\d+\.\d+)_SWRLZ", re.IGNORECASE
        ),
    ),
    "CORE_BASE": ComponentSpec(
        component="CORE_BASE",
        lane="SOURCES/CORE_BASE",
        stem_pattern=re.compile(
            r"SWRLZ_(?:CORE_ANDROID|CORE_BASE)_[A-Za-z0-9._-]+", re.IGNORECASE
        ),
    ),
    "KEYBOARD_BASE": ComponentSpec(
        component="KEYBOARD_BASE",
        lane="SOURCES/KEYBOARD",
        stem_pattern=re.compile(
            r"SWRLZ_KEYBOARD_BASE_CFv(?P<version>\d+\.\d+\.\d+)",
            re.IGNORECASE,
        ),
    ),
    "LAUNCHER_BASE": ComponentSpec(
        component="LAUNCHER_BASE",
        lane="SOURCES/LAUNCHER",
        stem_pattern=re.compile(
            r"SWRLZ_LAUNCHER_BASE_CFv(?P<version>\d+\.\d+\.\d+)",
            re.IGNORECASE,
        ),
    ),
}


def parse_version(match: re.Match[str]) -> tuple[int, ...] | None:
    value = match.groupdict().get("version")
    if not value:
        return None
    return tuple(int(part) for part in value.split("."))


def parse_transport_name(name: str, spec: ComponentSpec, extension: str) -> TransportName:
    if not name.lower().endswith(extension.lower()):
        raise ResolutionError(f"Unsupported extension for {name!r}; expected {extension}")

    stem_with_transport = name[: -len(extension)]
    duplicate_suffix: int | None = None
    suffix_match = DUPLICATE_SUFFIX_RE.search(stem_with_transport)
    if suffix_match:
        duplicate_suffix = int(suffix_match.group(1))
        stem_with_transport = stem_with_transport[: suffix_match.start()]

    canonical_stem = stem_with_transport
    match = spec.stem_pattern.fullmatch(canonical_stem)
    if not match:
        raise ResolutionError(
            f"{name!r} does not match the {spec.component} source naming contract"
        )

    return TransportName(
        original_name=name,
        canonical_stem=canonical_stem,
        duplicate_suffix=duplicate_suffix,
        version=parse_version(match),
    )


def sha256_file(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for block in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(block)
    return digest.hexdigest()


def natural_key(value: str) -> tuple[tuple[int, object], ...]:
    parts = re.split(r"(\d+)", value.lower())
    key: list[tuple[int, object]] = []
    for part in parts:
        if not part:
            continue
        if part.isdigit():
            key.append((0, int(part)))
        else:
            key.append((1, part))
    return tuple(key)


def identity_sort_key(transport: TransportName) -> tuple[object, ...]:
    if transport.version is not None:
        return (1, *transport.version)
    return (0, natural_key(transport.canonical_stem))


def ensure_within_lane(path: Path, lane: Path) -> None:
    try:
        path.resolve().relative_to(lane.resolve())
    except ValueError as exc:
        raise ResolutionError(f"Source must remain inside {lane}: {path}") from exc
    if path.parent.resolve() != lane.resolve():
        raise ResolutionError(
            f"Source must be at the active lane root, not a subdirectory: {path}"
        )


def discover_zip_candidates(lane: Path, spec: ComponentSpec) -> list[FileCandidate]:
    candidates: list[FileCandidate] = []
    if not lane.is_dir():
        raise ResolutionError(f"Source lane does not exist: {lane}")

    for path in sorted(lane.glob("*.zip")):
        try:
            transport = parse_transport_name(path.name, spec, ".zip")
        except ResolutionError:
            continue
        candidates.append(
            FileCandidate(path=path, transport=transport, sha256=sha256_file(path))
        )
    return candidates


def choose_identity(candidates: Sequence[FileCandidate]) -> str:
    by_identity: dict[str, list[FileCandidate]] = {}
    for candidate in candidates:
        by_identity.setdefault(candidate.transport.canonical_stem, []).append(candidate)

    if not by_identity:
        raise ResolutionError("No matching source ZIP exists in the active lane")

    ranked = sorted(
        by_identity.items(),
        key=lambda item: identity_sort_key(item[1][0].transport),
        reverse=True,
    )
    best_key = identity_sort_key(ranked[0][1][0].transport)
    tied = [
        identity
        for identity, group in ranked
        if identity_sort_key(group[0].transport) == best_key
    ]
    if len(tied) > 1:
        raise ResolutionError(
            "Multiple canonical source identities have the same highest version/order: "
            + ", ".join(sorted(tied))
        )
    return ranked[0][0]


def validate_alias_group(
    candidates: Sequence[FileCandidate], preferred: Path | None = None
) -> FileCandidate:
    if not candidates:
        raise ResolutionError("No source aliases were supplied")

    hashes = {candidate.sha256 for candidate in candidates}
    if len(hashes) != 1:
        details = ", ".join(f"{c.path.name}={c.sha256}" for c in candidates)
        raise ResolutionError(
            "Transport aliases map to the same canonical identity but contain different bytes: "
            + details
        )

    if preferred is not None:
        preferred_resolved = preferred.resolve()
        for candidate in candidates:
            if candidate.path.resolve() == preferred_resolved:
                return candidate

    return sorted(
        candidates,
        key=lambda c: (
            c.transport.duplicate_suffix is not None,
            c.transport.duplicate_suffix
            if c.transport.duplicate_suffix is not None
            else -1,
            c.path.name.lower(),
        ),
    )[0]


def parse_checksum_value(path: Path) -> str:
    text = path.read_text(encoding="utf-8", errors="replace")
    match = SHA256_RE.search(text)
    if not match:
        raise ResolutionError(f"No SHA-256 value found in checksum file: {path}")
    return match.group(1).lower()


def discover_checksum_aliases(
    lane: Path, spec: ComponentSpec, canonical_stem: str
) -> list[tuple[Path, str, TransportName]]:
    aliases: list[tuple[Path, str, TransportName]] = []
    for extension in CHECKSUM_EXTENSIONS:
        for path in sorted(lane.glob(f"*{extension}")):
            try:
                transport = parse_transport_name(path.name, spec, extension)
            except ResolutionError:
                continue
            if transport.canonical_stem != canonical_stem:
                continue
            aliases.append((path, parse_checksum_value(path), transport))
    return aliases


def choose_checksum(
    aliases: Sequence[tuple[Path, str, TransportName]], actual_sha256: str
) -> tuple[Path, list[str]]:
    if not aliases:
        raise ResolutionError("No matching checksum file exists for the selected source ZIP")

    values = {value for _, value, _ in aliases}
    if len(values) != 1:
        details = ", ".join(f"{path.name}={value}" for path, value, _ in aliases)
        raise ResolutionError("Checksum aliases disagree: " + details)

    expected = next(iter(values))
    if expected != actual_sha256:
        raise ResolutionError(
            f"Source checksum mismatch: expected {expected}, calculated {actual_sha256}"
        )

    preferred = sorted(
        aliases,
        key=lambda item: (
            item[2].duplicate_suffix is not None,
            item[2].duplicate_suffix
            if item[2].duplicate_suffix is not None
            else -1,
            CHECKSUM_EXTENSIONS.index(item[0].suffix.lower())
            if item[0].suffix.lower() in CHECKSUM_EXTENSIONS
            else len(CHECKSUM_EXTENSIONS),
            item[0].name.lower(),
        ),
    )[0][0]
    return preferred, [path.name for path, _, _ in aliases]


def resolve_source(
    repo_root: Path,
    component: str,
    explicit_source: str | None = None,
) -> dict[str, object]:
    normalized_component = component.upper()
    if normalized_component not in COMPONENTS:
        raise ResolutionError(
            f"Unsupported component {component!r}; choose one of {', '.join(COMPONENTS)}"
        )

    spec = COMPONENTS[normalized_component]
    lane = (repo_root / spec.lane).resolve()
    candidates = discover_zip_candidates(lane, spec)

    preferred_path: Path | None = None
    if explicit_source:
        preferred_path = (repo_root / explicit_source).resolve()
        ensure_within_lane(preferred_path, lane)
        if not preferred_path.is_file():
            raise ResolutionError(f"Explicit source ZIP does not exist: {explicit_source}")
        explicit_transport = parse_transport_name(preferred_path.name, spec, ".zip")
        selected_identity = explicit_transport.canonical_stem
        group = [
            c for c in candidates if c.transport.canonical_stem == selected_identity
        ]
        if not any(c.path.resolve() == preferred_path for c in group):
            group.append(
                FileCandidate(
                    path=preferred_path,
                    transport=explicit_transport,
                    sha256=sha256_file(preferred_path),
                )
            )
    else:
        selected_identity = choose_identity(candidates)
        group = [
            c for c in candidates if c.transport.canonical_stem == selected_identity
        ]

    selected = validate_alias_group(group, preferred=preferred_path)
    checksum_aliases = discover_checksum_aliases(lane, spec, selected_identity)
    checksum_file, checksum_names = choose_checksum(checksum_aliases, selected.sha256)

    result: dict[str, object] = {
        "schema": 1,
        "component": normalized_component,
        "lane": str(lane.relative_to(repo_root.resolve())),
        "selected_source": str(selected.path.relative_to(repo_root.resolve())),
        "uploaded_filename": selected.path.name,
        "canonical_stem": selected.transport.canonical_stem,
        "canonical_filename": selected.transport.canonical_zip_name,
        "duplicate_suffix": selected.transport.duplicate_suffix,
        "version": ".".join(str(part) for part in selected.transport.version)
        if selected.transport.version is not None
        else None,
        "source_sha256": selected.sha256,
        "checksum_file": str(checksum_file.relative_to(repo_root.resolve())),
        "source_aliases": sorted(c.path.name for c in group),
        "checksum_aliases": sorted(checksum_names),
    }
    return result


def write_github_outputs(path: Path, result: dict[str, object]) -> None:
    simple_keys = (
        "component",
        "lane",
        "selected_source",
        "uploaded_filename",
        "canonical_stem",
        "canonical_filename",
        "duplicate_suffix",
        "version",
        "source_sha256",
        "checksum_file",
    )
    with path.open("a", encoding="utf-8") as handle:
        for key in simple_keys:
            value = result.get(key)
            handle.write(f"{key}={'' if value is None else value}\n")
        handle.write("resolution_json<<SWRLZ_RESOLUTION_JSON\n")
        handle.write(json.dumps(result, sort_keys=True))
        handle.write("\nSWRLZ_RESOLUTION_JSON\n")


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--repo-root", default=".", help="Repository root")
    parser.add_argument("--component", required=True, choices=sorted(COMPONENTS))
    parser.add_argument(
        "--source-zip",
        default="",
        help="Optional explicit source ZIP path inside the selected active lane",
    )
    parser.add_argument(
        "--github-output",
        default="",
        help="Optional GITHUB_OUTPUT file to append resolved fields to",
    )
    return parser


def main(argv: Sequence[str] | None = None) -> int:
    args = build_parser().parse_args(argv)
    try:
        result = resolve_source(
            repo_root=Path(args.repo_root).resolve(),
            component=args.component,
            explicit_source=args.source_zip or None,
        )
    except ResolutionError as exc:
        print(f"SWRLZ source resolution failed: {exc}", file=sys.stderr)
        return 2

    print(json.dumps(result, indent=2, sort_keys=True))
    if args.github_output:
        write_github_outputs(Path(args.github_output), result)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
