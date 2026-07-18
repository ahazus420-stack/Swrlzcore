#!/usr/bin/env python3
from __future__ import annotations

import hashlib
import pathlib
import tempfile
import zipfile

SOURCE_ZIP = pathlib.Path("SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.zip")
SOURCE_SHA256 = "9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7"
OUTPUT_ZIP = pathlib.Path("SOURCES/CLIENT/CLIENT_CFv1.0.2_SWRLZ.zip")
OUTPUT_SHA_FILE = pathlib.Path("SOURCES/CLIENT/CLIENT_CFv1.0.2_SWRLZ.sha256")
REPORT_FILE = pathlib.Path("reports/INTEGRATION_FIX_011F_CLIENT_V102_SUCCESSOR.md")
TARGET_PATH = "CLIENT_CFv1.0.0_SWRLZ/android/app/src/main/java/sh/swurlz/core/net/Api.kt"
ANCHOR = "import io.ktor.client.statement.bodyAsText\n"
IMPORT_LINE = "import io.ktor.client.statement.request\n"
ROUTE_EXPRESSION = "response.request.url.encodedPath"
FIXED_ZIP_TIME = (1980, 1, 1, 0, 0, 0)


def sha256_bytes(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def sha256_file(path: pathlib.Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def archive_hashes(path: pathlib.Path) -> dict[str, str]:
    with zipfile.ZipFile(path) as archive:
        names = archive.namelist()
        if len(names) != len(set(names)):
            raise SystemExit("archive contains duplicate entry names")
        return {
            info.filename: sha256_bytes(b"" if info.is_dir() else archive.read(info.filename))
            for info in archive.infolist()
        }


def main() -> int:
    if not SOURCE_ZIP.is_file():
        raise SystemExit(f"missing canonical source: {SOURCE_ZIP}")

    source_sha = sha256_file(SOURCE_ZIP)
    if source_sha != SOURCE_SHA256:
        raise SystemExit(f"canonical v1.0.1 SHA mismatch: {source_sha}")

    with zipfile.ZipFile(SOURCE_ZIP) as source:
        bad = source.testzip()
        if bad:
            raise SystemExit(f"canonical archive integrity failure at: {bad}")

        infos = source.infolist()
        names = [info.filename for info in infos]
        if len(names) != len(set(names)):
            raise SystemExit("canonical archive contains duplicate entry names")
        if names.count(TARGET_PATH) != 1:
            raise SystemExit(f"expected exactly one target entry: {TARGET_PATH}")

        before_bytes = source.read(TARGET_PATH)
        before_text = before_bytes.decode("utf-8")
        if IMPORT_LINE.strip() in before_text:
            raise SystemExit("approved repair import is already present")
        if before_text.count(ANCHOR) != 1:
            raise SystemExit("expected import anchor was not found exactly once")
        if ROUTE_EXPRESSION not in before_text:
            raise SystemExit("actual-route error expression is missing")

        after_text = before_text.replace(ANCHOR, ANCHOR + IMPORT_LINE, 1)
        before_lines = before_text.splitlines()
        after_lines = after_text.splitlines()
        added = [line for line in after_lines if line not in before_lines]
        if added != [IMPORT_LINE.rstrip("\n")]:
            raise SystemExit(f"unexpected added lines: {added}")
        if len(after_lines) != len(before_lines) + 1:
            raise SystemExit("candidate is not exactly one added source line")
        if ROUTE_EXPRESSION not in after_text:
            raise SystemExit("actual-route error expression changed")

        OUTPUT_ZIP.parent.mkdir(parents=True, exist_ok=True)
        with tempfile.NamedTemporaryFile(
            prefix="CLIENT_CFv1.0.2_SWRLZ-",
            suffix=".zip",
            dir=OUTPUT_ZIP.parent,
            delete=False,
        ) as temp_handle:
            temp_path = pathlib.Path(temp_handle.name)

        try:
            with zipfile.ZipFile(
                temp_path,
                mode="w",
                compression=zipfile.ZIP_DEFLATED,
                compresslevel=9,
                strict_timestamps=True,
            ) as output:
                for original in sorted(infos, key=lambda item: item.filename):
                    data = b"" if original.is_dir() else source.read(original.filename)
                    if original.filename == TARGET_PATH:
                        data = after_text.encode("utf-8")

                    normalized = zipfile.ZipInfo(original.filename, FIXED_ZIP_TIME)
                    normalized.create_system = original.create_system
                    normalized.external_attr = original.external_attr
                    normalized.internal_attr = original.internal_attr
                    normalized.flag_bits = original.flag_bits & 0x800
                    normalized.compress_type = (
                        zipfile.ZIP_STORED if original.is_dir() else zipfile.ZIP_DEFLATED
                    )
                    output.writestr(
                        normalized,
                        data,
                        compress_type=normalized.compress_type,
                        compresslevel=9,
                    )
            temp_path.replace(OUTPUT_ZIP)
        finally:
            temp_path.unlink(missing_ok=True)

    with zipfile.ZipFile(OUTPUT_ZIP) as candidate:
        bad = candidate.testzip()
        if bad:
            raise SystemExit(f"candidate archive integrity failure at: {bad}")
        candidate_text = candidate.read(TARGET_PATH).decode("utf-8")
        if candidate_text.count(IMPORT_LINE.strip()) != 1:
            raise SystemExit("candidate does not contain the repair import exactly once")
        if ROUTE_EXPRESSION not in candidate_text:
            raise SystemExit("candidate lost the actual-route error expression")

    original_hashes = archive_hashes(SOURCE_ZIP)
    candidate_hashes = archive_hashes(OUTPUT_ZIP)
    changed_entries = sorted(
        name
        for name in set(original_hashes) | set(candidate_hashes)
        if original_hashes.get(name) != candidate_hashes.get(name)
    )
    if changed_entries != [TARGET_PATH]:
        raise SystemExit(f"candidate changed unexpected archive entries: {changed_entries}")

    output_sha = sha256_file(OUTPUT_ZIP)
    OUTPUT_SHA_FILE.write_text(f"{output_sha}  {OUTPUT_ZIP.name}\n", encoding="utf-8")
    if sha256_file(SOURCE_ZIP) != SOURCE_SHA256:
        raise SystemExit("canonical v1.0.1 ZIP changed during successor creation")

    REPORT_FILE.parent.mkdir(parents=True, exist_ok=True)
    REPORT_FILE.write_text(
        "\n".join(
            [
                "# INTEGRATION-FIX-011F — CLIENT CFv1.0.2 Successor",
                "",
                "## Result",
                "",
                "**PASS — deterministic one-line successor created on the temporary diagnostic branch.**",
                "",
                f"- Input ZIP: `{SOURCE_ZIP}`",
                f"- Input SHA-256: `{source_sha}`",
                f"- Output ZIP: `{OUTPUT_ZIP}`",
                f"- Output SHA-256: `{output_sha}`",
                f"- Changed archive entry: `{TARGET_PATH}`",
                f"- Added source line: `{IMPORT_LINE.strip()}`",
                "- Removed source lines: `0`",
                f"- Actual-route expression preserved: `{ROUTE_EXPRESSION}`",
                "- Other archive entry contents changed: `0`",
                "- Canonical v1.0.1 ZIP modified: `no`",
                "- `main` modified: `no`",
                "- APK built by this generator: `no`",
                "",
                "The archive filename is versioned as CFv1.0.2. Its internal project root remains unchanged to keep the repair limited to the approved Kotlin import.",
                "",
            ]
        ),
        encoding="utf-8",
    )

    print(f"output_zip={OUTPUT_ZIP}")
    print(f"output_sha_file={OUTPUT_SHA_FILE}")
    print(f"output_sha256={output_sha}")
    print(f"report={REPORT_FILE}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
