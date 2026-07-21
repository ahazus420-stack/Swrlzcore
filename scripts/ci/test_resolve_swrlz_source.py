#!/usr/bin/env python3
from __future__ import annotations

import hashlib
import tempfile
import unittest
from pathlib import Path

from resolve_swrlz_source import COMPONENTS, ResolutionError, parse_transport_name, resolve_source


def digest(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


class ResolveSwrlzSourceTests(unittest.TestCase):
    def setUp(self) -> None:
        self.tempdir = tempfile.TemporaryDirectory()
        self.root = Path(self.tempdir.name)
        for spec in COMPONENTS.values():
            (self.root / spec.lane).mkdir(parents=True, exist_ok=True)

    def tearDown(self) -> None:
        self.tempdir.cleanup()

    def write_pair(
        self,
        component: str,
        zip_name: str,
        data: bytes,
        checksum_name: str | None = None,
    ) -> Path:
        spec = COMPONENTS[component]
        lane = self.root / spec.lane
        source = lane / zip_name
        source.write_bytes(data)
        if checksum_name:
            (lane / checksum_name).write_text(
                f"{digest(data)}  canonical-name-does-not-control-verification.zip\n",
                encoding="utf-8",
            )
        return source

    def test_transport_suffix_normalization_all_components(self) -> None:
        examples = {
            "CLIENT": "CLIENT_CFv1.2.3_SWRLZ (204).zip",
            "SERVER": "SERVER_CFv4.5.6_SWRLZ(7).zip",
            "CORE_BASE": "SWRLZ_CORE_ANDROID_CORE_REDUCE_003_SOURCE (2).zip",
            "KEYBOARD_BASE": "SWRLZ_KEYBOARD_BASE_CFv1.0.1(12).zip",
            "LAUNCHER_BASE": "SWRLZ_LAUNCHER_BASE_CFv1.0.1 (4).zip",
        }
        for component, name in examples.items():
            with self.subTest(component=component):
                parsed = parse_transport_name(name, COMPONENTS[component], ".zip")
                self.assertIsNotNone(parsed.duplicate_suffix)
                self.assertNotIn("(", parsed.canonical_zip_name)
                self.assertTrue(parsed.canonical_zip_name.endswith(".zip"))

    def test_duplicate_number_does_not_override_semantic_version(self) -> None:
        self.write_pair(
            "CLIENT",
            "CLIENT_CFv1.0.1_SWRLZ(99).zip",
            b"old",
            "CLIENT_CFv1.0.1_SWRLZ.sha256",
        )
        self.write_pair(
            "CLIENT",
            "CLIENT_CFv1.0.2_SWRLZ(1).zip",
            b"new",
            "CLIENT_CFv1.0.2_SWRLZ (8).sha256",
        )
        result = resolve_source(self.root, "CLIENT")
        self.assertEqual(result["canonical_stem"], "CLIENT_CFv1.0.2_SWRLZ")
        self.assertEqual(result["duplicate_suffix"], 1)

    def test_unsuffixed_alias_is_preferred_when_bytes_match(self) -> None:
        data = b"same-client-source"
        self.write_pair(
            "CLIENT",
            "CLIENT_CFv1.0.1_SWRLZ.zip",
            data,
            "CLIENT_CFv1.0.1_SWRLZ.sha256",
        )
        self.write_pair("CLIENT", "CLIENT_CFv1.0.1_SWRLZ(2).zip", data)
        result = resolve_source(self.root, "CLIENT")
        self.assertEqual(result["uploaded_filename"], "CLIENT_CFv1.0.1_SWRLZ.zip")
        self.assertIsNone(result["duplicate_suffix"])
        self.assertEqual(len(result["source_aliases"]), 2)

    def test_explicit_suffixed_alias_is_preserved_in_provenance(self) -> None:
        data = b"same-server-source"
        self.write_pair(
            "SERVER",
            "SERVER_CFv1.0.3_SWRLZ.zip",
            data,
            "SERVER_CFv1.0.3_SWRLZ.sha256",
        )
        selected = self.write_pair("SERVER", "SERVER_CFv1.0.3_SWRLZ (2).zip", data)
        result = resolve_source(
            self.root,
            "SERVER",
            str(selected.relative_to(self.root)),
        )
        self.assertEqual(result["uploaded_filename"], selected.name)
        self.assertEqual(result["canonical_filename"], "SERVER_CFv1.0.3_SWRLZ.zip")
        self.assertEqual(result["duplicate_suffix"], 2)

    def test_checksum_suffix_can_differ_from_zip_suffix(self) -> None:
        data = b"keyboard"
        self.write_pair(
            "KEYBOARD_BASE",
            "SWRLZ_KEYBOARD_BASE_CFv1.0.1 (17).zip",
            data,
            "SWRLZ_KEYBOARD_BASE_CFv1.0.1(3).sha256",
        )
        result = resolve_source(self.root, "KEYBOARD_BASE")
        self.assertEqual(result["source_sha256"], digest(data))
        self.assertTrue(str(result["checksum_file"]).endswith("(3).sha256"))

    def test_conflicting_alias_bytes_fail_closed(self) -> None:
        self.write_pair(
            "LAUNCHER_BASE",
            "SWRLZ_LAUNCHER_BASE_CFv1.0.1.zip",
            b"one",
            "SWRLZ_LAUNCHER_BASE_CFv1.0.1.sha256",
        )
        self.write_pair(
            "LAUNCHER_BASE",
            "SWRLZ_LAUNCHER_BASE_CFv1.0.1(1).zip",
            b"two",
        )
        with self.assertRaisesRegex(ResolutionError, "different bytes"):
            resolve_source(self.root, "LAUNCHER_BASE")

    def test_missing_checksum_fails_closed(self) -> None:
        self.write_pair(
            "CORE_BASE",
            "SWRLZ_CORE_ANDROID_CORE_REDUCE_003_SOURCE(1).zip",
            b"core",
        )
        with self.assertRaisesRegex(ResolutionError, "No matching checksum"):
            resolve_source(self.root, "CORE_BASE")

    def test_checksum_mismatch_fails_closed(self) -> None:
        spec = COMPONENTS["SERVER"]
        lane = self.root / spec.lane
        (lane / "SERVER_CFv1.0.3_SWRLZ(1).zip").write_bytes(b"actual")
        (lane / "SERVER_CFv1.0.3_SWRLZ.sha256").write_text(
            f"{digest(b'wrong')}  SERVER_CFv1.0.3_SWRLZ.zip\n",
            encoding="utf-8",
        )
        with self.assertRaisesRegex(ResolutionError, "checksum mismatch"):
            resolve_source(self.root, "SERVER")


if __name__ == "__main__":
    unittest.main(verbosity=2)
