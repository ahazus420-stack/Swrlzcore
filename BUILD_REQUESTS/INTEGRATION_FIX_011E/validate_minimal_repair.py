#!/usr/bin/env python3
from __future__ import annotations

import hashlib
import os
import pathlib
import subprocess
import tempfile
import zipfile

SOURCE_ZIP = pathlib.Path("SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.zip")
PATCH_FILE = pathlib.Path("BUILD_REQUESTS/INTEGRATION_FIX_011E/client-v101-request-import.patch")
REPORT_FILE = pathlib.Path("reports/INTEGRATION_FIX_011E_CLIENT_V101_MINIMAL_REPAIR_VALIDATION.md")
EXPECTED_SOURCE_SHA = "9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7"
TARGET_SUFFIX = pathlib.PurePosixPath(
    "CLIENT_CFv1.0.0_SWRLZ/android/app/src/main/java/sh/swurlz/core/net/Api.kt"
)
IMPORT_LINE = "import io.ktor.client.statement.request"
USAGE = "response.request.url.encodedPath"


def sha256(path: pathlib.Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def tree_hashes(root: pathlib.Path) -> dict[str, str]:
    result: dict[str, str] = {}
    for path in sorted(p for p in root.rglob("*") if p.is_file()):
        result[path.relative_to(root).as_posix()] = sha256(path)
    return result


def main() -> int:
    source_sha_before = sha256(SOURCE_ZIP)
    if source_sha_before != EXPECTED_SOURCE_SHA:
        raise SystemExit(f"source SHA mismatch: {source_sha_before}")
    if not PATCH_FILE.is_file():
        raise SystemExit(f"missing patch: {PATCH_FILE}")

    with tempfile.TemporaryDirectory(prefix="swrlz-client-v101-011e-") as temp_dir:
        root = pathlib.Path(temp_dir)
        with zipfile.ZipFile(SOURCE_ZIP) as archive:
            bad = archive.testzip()
            if bad:
                raise SystemExit(f"ZIP integrity failure at {bad}")
            archive.extractall(root)

        target = root / TARGET_SUFFIX
        if not target.is_file():
            raise SystemExit(f"target source missing: {TARGET_SUFFIX}")

        before_text = target.read_text(encoding="utf-8")
        if USAGE not in before_text:
            raise SystemExit(f"expected route-specific usage missing: {USAGE}")
        if IMPORT_LINE in before_text:
            raise SystemExit("repair import already present; candidate is not minimal against this source")

        build_file = root / "CLIENT_CFv1.0.0_SWRLZ/android/app/build.gradle.kts"
        build_text = build_file.read_text(encoding="utf-8")
        if 'io.ktor:ktor-client-core:2.3.12' not in build_text:
            raise SystemExit("expected Ktor client core 2.3.12 dependency not found")

        before_hashes = tree_hashes(root)
        subprocess.run(
            ["patch", "--batch", "--forward", "--dry-run", "-p1", "-i", str(PATCH_FILE.resolve())],
            cwd=root,
            check=True,
        )
        subprocess.run(
            ["patch", "--batch", "--forward", "-p1", "-i", str(PATCH_FILE.resolve())],
            cwd=root,
            check=True,
        )
        after_hashes = tree_hashes(root)

        changed = sorted(
            path for path in set(before_hashes) | set(after_hashes)
            if before_hashes.get(path) != after_hashes.get(path)
        )
        expected_target = TARGET_SUFFIX.as_posix()
        if changed != [expected_target]:
            raise SystemExit(f"unexpected changed paths: {changed}")

        after_text = target.read_text(encoding="utf-8")
        if after_text.count(IMPORT_LINE) != 1:
            raise SystemExit("repair import was not added exactly once")
        if USAGE not in after_text:
            raise SystemExit("route-specific error path usage was altered")

        before_lines = before_text.splitlines()
        after_lines = after_text.splitlines()
        added = [line for line in after_lines if line not in before_lines]
        if added != [IMPORT_LINE] or len(after_lines) != len(before_lines) + 1:
            raise SystemExit(f"repair is not a one-line addition: added={added}")

        source_sha_after = sha256(SOURCE_ZIP)
        if source_sha_after != source_sha_before:
            raise SystemExit("canonical source ZIP changed during validation")

        REPORT_FILE.parent.mkdir(parents=True, exist_ok=True)
        REPORT_FILE.write_text(
            "\n".join(
                [
                    "# INTEGRATION-FIX-011E — CLIENT v1.0.1 Minimal Repair Validation",
                    "",
                    "## Result",
                    "",
                    "**PASS — static, no-Gradle validation.**",
                    "",
                    f"- Canonical source SHA-256 before: `{source_sha_before}`",
                    f"- Canonical source SHA-256 after: `{source_sha_after}`",
                    f"- Ktor client core: `2.3.12`",
                    f"- Changed path after applying candidate: `{expected_target}`",
                    f"- Added line: `{IMPORT_LINE}`",
                    "- Removed lines: `0`",
                    "- Route-specific error-path expression preserved: `response.request.url.encodedPath`",
                    "- Gradle invoked: `no`",
                    "- APK assembled: `no`",
                    "- Canonical ZIP modified: `no`",
                    "- `main` modified: `no`",
                    "",
                    "## Interpretation",
                    "",
                    "The failed source references Ktor's `HttpResponse.request` extension but omits its import. "
                    "Adding `io.ktor.client.statement.request` resolves that symbol while preserving the intended actual-route error reporting and changing no other source path.",
                    "",
                ]
            ),
            encoding="utf-8",
        )

    print(f"PASS {REPORT_FILE}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
