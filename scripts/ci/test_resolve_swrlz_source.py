#!/usr/bin/env python3
from __future__ import annotations

import hashlib
import json
import os
import subprocess
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

from resolve_swrlz_source import COMPONENTS, ResolutionError, parse_transport_name, resolve_source


def digest(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


class ResolveSwrlzSourceTests(unittest.TestCase):
    def setUp(self) -> None:
        self.tempdir = tempfile.TemporaryDirectory()
        self.root = Path(self.tempdir.name)
        subprocess.run(["git", "init", "-q"], cwd=self.root, check=True)
        subprocess.run(["git", "config", "user.email", "tests@example.invalid"], cwd=self.root, check=True)
        subprocess.run(["git", "config", "user.name", "SWRLZ Tests"], cwd=self.root, check=True)
        for spec in COMPONENTS.values():
            (self.root / spec.lane).mkdir(parents=True, exist_ok=True)

    def tearDown(self) -> None:
        self.tempdir.cleanup()

    def write_pair(self, component: str, zip_name: str, data: bytes, checksum_name: str | None = None) -> Path:
        lane = self.root / COMPONENTS[component].lane
        source = lane / zip_name
        source.write_bytes(data)
        checksum = checksum_name or (zip_name[:-4] + ".sha256")
        (lane / checksum).write_text(f"{digest(data)}  {zip_name}\n", encoding="utf-8")
        return source

    def commit(self, message: str) -> str:
        subprocess.run(["git", "add", "."], cwd=self.root, check=True)
        subprocess.run(["git", "commit", "-qm", message], cwd=self.root, check=True)
        return subprocess.check_output(["git", "rev-parse", "HEAD"], cwd=self.root, text=True).strip()

    def test_copy_suffix_and_arbitrary_filename_are_accepted(self) -> None:
        parsed = parse_transport_name("My Strange Android Project (204).zip", ".zip")
        self.assertEqual(parsed.logical_stem, "My Strange Android Project")
        self.assertEqual(parsed.artifact_stem, "My_Strange_Android_Project")
        self.assertEqual(parsed.duplicate_suffix, 204)

    def test_arbitrary_keyboard_name_resolves(self) -> None:
        source = self.write_pair("KEYBOARD_BASE", "Totally Custom Keyboard Build.zip", b"keyboard")
        result = resolve_source(self.root, "KEYBOARD_BASE", str(source.relative_to(self.root)))
        self.assertEqual(result["selected_source"], str(source.relative_to(self.root)))
        self.assertEqual(result["canonical_stem"], "Totally_Custom_Keyboard_Build")

    def test_current_push_selects_reuploaded_older_source(self) -> None:
        old = self.write_pair("CLIENT", "CLIENT_CFv1.0.1_SWRLZ.zip", b"old-v1")
        self.write_pair("CLIENT", "CLIENT_CFv9.9.9_SWRLZ.zip", b"newer-version")
        first = self.commit("initial sources")
        old.write_bytes(b"old-v1-reuploaded")
        (old.with_suffix(".sha256")).write_text(f"{digest(b'old-v1-reuploaded')}  {old.name}\n")
        second = self.commit("reupload older source")
        event = self.root / "event.json"
        event.write_text(json.dumps({"before": first, "after": second}), encoding="utf-8")
        with patch.dict(os.environ, {
            "GITHUB_EVENT_NAME": "push",
            "GITHUB_EVENT_PATH": str(event),
            "GITHUB_SHA": second,
        }, clear=False):
            result = resolve_source(self.root, "CLIENT")
        self.assertEqual(result["selected_source"], str(old.relative_to(self.root)))
        self.assertEqual(result["selection_reason"], "current-push")

    def test_checksum_suffix_can_differ_from_zip_suffix(self) -> None:
        data = b"server"
        source = self.write_pair(
            "SERVER", "Whatever Server Source (17).zip", data,
            "Whatever Server Source (3).sha256",
        )
        result = resolve_source(self.root, "SERVER", str(source.relative_to(self.root)))
        self.assertEqual(result["source_sha256"], digest(data))
        self.assertTrue(str(result["checksum_file"]).endswith("(3).sha256"))

    def test_conflicting_alias_bytes_fail_closed(self) -> None:
        self.write_pair("LAUNCHER_BASE", "Launcher Project.zip", b"one", "Launcher Project.sha256")
        lane = self.root / COMPONENTS["LAUNCHER_BASE"].lane
        (lane / "Launcher Project (1).zip").write_bytes(b"two")
        source = lane / "Launcher Project.zip"
        with self.assertRaisesRegex(ResolutionError, "different bytes"):
            resolve_source(self.root, "LAUNCHER_BASE", str(source.relative_to(self.root)))

    def test_missing_checksum_fails_closed(self) -> None:
        lane = self.root / COMPONENTS["CORE_BASE"].lane
        source = lane / "Unrelated Android App.zip"
        source.write_bytes(b"core")
        with self.assertRaisesRegex(ResolutionError, "No matching checksum"):
            resolve_source(self.root, "CORE_BASE", str(source.relative_to(self.root)))

    def test_checksum_mismatch_fails_closed(self) -> None:
        source = self.write_pair("SERVER", "Any Name.zip", b"actual")
        source.with_suffix(".sha256").write_text(f"{digest(b'wrong')}  {source.name}\n")
        with self.assertRaisesRegex(ResolutionError, "checksum mismatch"):
            resolve_source(self.root, "SERVER", str(source.relative_to(self.root)))


if __name__ == "__main__":
    unittest.main(verbosity=2)