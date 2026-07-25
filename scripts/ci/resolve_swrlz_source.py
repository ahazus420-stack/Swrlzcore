#!/usr/bin/env python3
"""Resolve the exact Android source archive selected by a SWRLZ build event.

The repository lane chooses the build contract. The filename is transport metadata only:
any readable ``.zip`` name is accepted, Android download-copy suffixes such as ``(1)``
are ignored for logical pairing, and push builds prefer the exact ZIP changed by the
current commit instead of the numerically highest version name.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import os
import re
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path
from typing import Sequence

SHA256_RE = re.compile(r"(?i)(?<![0-9a-f])([0-9a-f]{64})(?![0-9a-f])")
DUPLICATE_SUFFIX_RE = re.compile(r"\s*\((\d+)\)$")
CHECKSUM_EXTENSIONS = (".sha256", ".sha", ".txt")
KNOWN_VERSION_RE = re.compile(r"(?i)CFv(?P<version>\d+\.\d+\.\d+)")


class ResolutionError(RuntimeError):
    """Raised when a source cannot be resolved without ambiguity."""


@dataclass(frozen=True)
class ComponentSpec:
    component: str
    lane: str


@dataclass(frozen=True)
class TransportName:
    original_name: str
    logical_stem: str
    artifact_stem: str
    duplicate_suffix: int | None
    version: tuple[int, ...] | None

    @property
    def canonical_zip_name(self) -> str:
        return f"{self.logical_stem}.zip"


@dataclass(frozen=True)
class FileCandidate:
    path: Path
    transport: TransportName
    sha256: str


COMPONENTS: dict[str, ComponentSpec] = {
    "CLIENT": ComponentSpec("CLIENT", "SOURCES/CLIENT"),
    "SERVER": ComponentSpec("SERVER", "SOURCES/SERVER"),
    "CORE_BASE": ComponentSpec("CORE_BASE", "SOURCES/CORE_BASE"),
    "KEYBOARD_BASE": ComponentSpec("KEYBOARD_BASE", "SOURCES/KEYBOARD"),
    "LAUNCHER_BASE": ComponentSpec("LAUNCHER_BASE", "SOURCES/LAUNCHER"),
}


def parse_version(stem: str) -> tuple[int, ...] | None:
    match = KNOWN_VERSION_RE.search(stem)
    if not match:
        return None
    return tuple(int(part) for part in match.group("version").split("."))


def artifact_safe_stem(stem: str) -> str:
    value = re.sub(r"\s+", "_", stem.strip())
    value = re.sub(r"[^A-Za-z0-9._-]+", "_", value)
    value = re.sub(r"_+", "_", value).strip("._-")
    return value or "android-source"


def parse_transport_name(name: str, extension: str) -> TransportName:
    if not name.lower().endswith(extension.lower()):
        raise ResolutionError(f"Unsupported extension for {name!r}; expected {extension}")
    stem = name[: -len(extension)].rstrip()
    duplicate_suffix: int | None = None
    suffix_match = DUPLICATE_SUFFIX_RE.search(stem)
    if suffix_match:
        duplicate_suffix = int(suffix_match.group(1))
        stem = stem[: suffix_match.start()].rstrip()
    if not stem:
        raise ResolutionError(f"Source filename has no usable stem: {name!r}")
    return TransportName(
        original_name=name,
        logical_stem=stem,
        artifact_stem=artifact_safe_stem(stem),
        duplicate_suffix=duplicate_suffix,
        version=parse_version(stem),
    )


def sha256_file(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for block in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(block)
    return digest.hexdigest()


def ensure_within_lane(path: Path, lane: Path) -> None:
    try:
        path.resolve().relative_to(lane.resolve())
    except ValueError as exc:
        raise ResolutionError(f"Source must remain inside {lane}: {path}") from exc
    if path.parent.resolve() != lane.resolve():
        raise ResolutionError(
            f"Source must be at the active lane root, not a subdirectory: {path}"
        )


def discover_zip_candidates(lane: Path) -> list[FileCandidate]:
    if not lane.is_dir():
        raise ResolutionError(f"Source lane does not exist: {lane}")
    candidates: list[FileCandidate] = []
    for path in sorted(lane.glob("*.zip")):
        transport = parse_transport_name(path.name, ".zip")
        candidates.append(FileCandidate(path, transport, sha256_file(path)))
    return candidates


def git_changed_paths(repo_root: Path) -> list[str]:
    if os.environ.get("GITHUB_EVENT_NAME") != "push":
        return []
    before = os.environ.get("GITHUB_EVENT_BEFORE", "")
    after = os.environ.get("GITHUB_SHA", "")
    event_path = os.environ.get("GITHUB_EVENT_PATH", "")
    if event_path and Path(event_path).is_file():
        try:
            payload = json.loads(Path(event_path).read_text(encoding="utf-8"))
            before = str(payload.get("before") or before)
            after = str(payload.get("after") or after)
        except (OSError, UnicodeDecodeError, json.JSONDecodeError):
            pass
    if not after:
        return []
    if not before or set(before) == {"0"}:
        command = ["git", "show", "--pretty=", "--name-only", after]
    else:
        command = ["git", "diff", "--name-only", before, after]
    try:
        output = subprocess.check_output(command, cwd=repo_root, text=True)
    except (OSError, subprocess.CalledProcessError):
        return []
    return [line.strip() for line in output.splitlines() if line.strip()]


def push_selected_candidate(
    repo_root: Path,
    lane: Path,
    candidates: Sequence[FileCandidate],
) -> FileCandidate | None:
    changed = git_changed_paths(repo_root)
    if not changed:
        return None
    lane_relative = lane.relative_to(repo_root).as_posix().rstrip("/") + "/"
    changed_in_lane = [p for p in changed if p.startswith(lane_relative)]
    changed_zips = [p for p in changed_in_lane if p.lower().endswith(".zip")]
    by_path = {c.path.resolve(): c for c in candidates}
    selected: list[FileCandidate] = []
    for rel in changed_zips:
        candidate = by_path.get((repo_root / rel).resolve())
        if candidate is not None:
            selected.append(candidate)
    if len(selected) == 1:
        return selected[0]
    if len(selected) > 1:
        raise ResolutionError(
            "Multiple source ZIPs changed in one build lane. Dispatch each source explicitly: "
            + ", ".join(c.path.name for c in selected)
        )

    changed_stems: set[str] = set()
    for rel in changed_in_lane:
        lower = rel.lower()
        extension = next((ext for ext in CHECKSUM_EXTENSIONS if lower.endswith(ext)), None)
        if extension:
            changed_stems.add(parse_transport_name(Path(rel).name, extension).logical_stem.casefold())
        elif lower.endswith(".manifest.json"):
            changed_stems.add(parse_transport_name(Path(rel).name, ".manifest.json").logical_stem.casefold())
    matched = [c for c in candidates if c.transport.logical_stem.casefold() in changed_stems]
    if len(matched) == 1:
        return matched[0]
    if len(matched) > 1:
        return validate_alias_group(matched)
    return None


def last_commit_timestamp(repo_root: Path, path: Path) -> int:
    rel = path.relative_to(repo_root).as_posix()
    try:
        value = subprocess.check_output(
            ["git", "log", "-1", "--format=%ct", "--", rel],
            cwd=repo_root,
            text=True,
        ).strip()
        return int(value) if value else 0
    except (OSError, ValueError, subprocess.CalledProcessError):
        return 0


def choose_latest_repository_candidate(
    repo_root: Path,
    candidates: Sequence[FileCandidate],
) -> FileCandidate:
    if not candidates:
        raise ResolutionError("No source ZIP exists in the active lane")
    ranked = sorted(
        candidates,
        key=lambda c: (
            last_commit_timestamp(repo_root, c.path),
            c.path.stat().st_mtime_ns,
            c.path.name.casefold(),
        ),
        reverse=True,
    )
    return ranked[0]


def validate_alias_group(
    candidates: Sequence[FileCandidate],
    preferred: Path | None = None,
) -> FileCandidate:
    if not candidates:
        raise ResolutionError("No source aliases were supplied")
    hashes = {candidate.sha256 for candidate in candidates}
    if len(hashes) != 1:
        details = ", ".join(f"{c.path.name}={c.sha256}" for c in candidates)
        raise ResolutionError(
            "Transport aliases map to the same logical source name but contain different bytes: "
            + details
        )
    if preferred is not None:
        for candidate in candidates:
            if candidate.path.resolve() == preferred.resolve():
                return candidate
    return sorted(
        candidates,
        key=lambda c: (
            c.transport.duplicate_suffix is not None,
            c.transport.duplicate_suffix if c.transport.duplicate_suffix is not None else -1,
            c.path.name.casefold(),
        ),
    )[0]


def parse_checksum_value(path: Path) -> str:
    match = SHA256_RE.search(path.read_text(encoding="utf-8", errors="replace"))
    if not match:
        raise ResolutionError(f"No SHA-256 value found in checksum file: {path}")
    return match.group(1).lower()


def discover_checksum_aliases(
    lane: Path,
    logical_stem: str,
) -> list[tuple[Path, str, TransportName]]:
    aliases: list[tuple[Path, str, TransportName]] = []
    for extension in CHECKSUM_EXTENSIONS:
        for path in sorted(lane.glob(f"*{extension}")):
            transport = parse_transport_name(path.name, extension)
            if transport.logical_stem.casefold() != logical_stem.casefold():
                continue
            aliases.append((path, parse_checksum_value(path), transport))
    return aliases


def choose_checksum(
    aliases: Sequence[tuple[Path, str, TransportName]],
    actual_sha256: str,
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
            item[2].duplicate_suffix if item[2].duplicate_suffix is not None else -1,
            CHECKSUM_EXTENSIONS.index(item[0].suffix.lower())
            if item[0].suffix.lower() in CHECKSUM_EXTENSIONS
            else len(CHECKSUM_EXTENSIONS),
            item[0].name.casefold(),
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
    candidates = discover_zip_candidates(lane)

    selection_reason = "repository-latest"
    preferred_path: Path | None = None
    if explicit_source:
        preferred_path = (repo_root / explicit_source).resolve()
        ensure_within_lane(preferred_path, lane)
        if not preferred_path.is_file():
            raise ResolutionError(f"Explicit source ZIP does not exist: {explicit_source}")
        selected_transport = parse_transport_name(preferred_path.name, ".zip")
        group = [
            c for c in candidates
            if c.transport.logical_stem.casefold() == selected_transport.logical_stem.casefold()
        ]
        if not any(c.path.resolve() == preferred_path for c in group):
            group.append(FileCandidate(preferred_path, selected_transport, sha256_file(preferred_path)))
        selected = validate_alias_group(group, preferred=preferred_path)
        selection_reason = "explicit-source"
    else:
        event_candidate = push_selected_candidate(repo_root, lane, candidates)
        if event_candidate is not None:
            group = [
                c for c in candidates
                if c.transport.logical_stem.casefold() == event_candidate.transport.logical_stem.casefold()
            ]
            selected = validate_alias_group(group, preferred=event_candidate.path)
            selection_reason = "current-push"
        else:
            latest = choose_latest_repository_candidate(repo_root, candidates)
            group = [
                c for c in candidates
                if c.transport.logical_stem.casefold() == latest.transport.logical_stem.casefold()
            ]
            selected = validate_alias_group(group, preferred=latest.path)

    checksum_aliases = discover_checksum_aliases(lane, selected.transport.logical_stem)
    checksum_file, checksum_names = choose_checksum(checksum_aliases, selected.sha256)
    result: dict[str, object] = {
        "schema": 2,
        "component": normalized_component,
        "lane": str(lane.relative_to(repo_root.resolve())),
        "selected_source": str(selected.path.relative_to(repo_root.resolve())),
        "uploaded_filename": selected.path.name,
        "logical_stem": selected.transport.logical_stem,
        "canonical_stem": selected.transport.artifact_stem,
        "canonical_filename": selected.transport.canonical_zip_name,
        "duplicate_suffix": selected.transport.duplicate_suffix,
        "version": ".".join(str(part) for part in selected.transport.version)
        if selected.transport.version is not None else None,
        "source_sha256": selected.sha256,
        "checksum_file": str(checksum_file.relative_to(repo_root.resolve())),
        "source_aliases": sorted(c.path.name for c in group),
        "checksum_aliases": sorted(checksum_names),
        "selection_reason": selection_reason,
        "filename_policy": "lane-and-content-authoritative",
    }
    return result


def write_github_outputs(path: Path, result: dict[str, object]) -> None:
    simple_keys = (
        "component", "lane", "selected_source", "uploaded_filename", "logical_stem",
        "canonical_stem", "canonical_filename", "duplicate_suffix", "version",
        "source_sha256", "checksum_file", "selection_reason",
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
    parser.add_argument("--source-zip", default="", help="Optional explicit ZIP path inside the selected source lane")
    parser.add_argument("--github-output", default="", help="Optional GITHUB_OUTPUT file")
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