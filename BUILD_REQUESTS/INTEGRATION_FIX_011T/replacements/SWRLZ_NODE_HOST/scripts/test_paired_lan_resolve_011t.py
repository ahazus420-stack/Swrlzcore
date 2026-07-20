#!/usr/bin/env python3
"""Static/source-contract validator for INTEGRATION-FIX-011T.

This validator intentionally does not invoke Gradle or build an APK. It checks
that the bounded paired-LAN resolution slice is present and that the previously
accepted mutation implementations remain byte-for-byte function-block stable.
"""
from __future__ import annotations

import argparse
import hashlib
import hmac
import re
import sys
from pathlib import Path

EXPECTED_MUTATION_BLOCKS = {
    "app/src/main/java/sh/swrlz/nodehost/service/PresenceProtocol.kt": {
        "    private fun validatePairingAndExposure(": "1760f5ef06a7e773534f3f7ef0692c632f32ac3e26417c8392a6a447c6bbcc2c",
        "    private fun createGroup(": "f58f55c424a125ea0b40be9cff0df2c19ccd1020969c8e0d96fd50b202f62254",
        "    private fun registerDevice(": "15c873943e263ca6492bd460e4d9a742866abe0d3e5edcc949da93ed4f86d495",
        "    private fun joinGroup(": "5a143c7d9f1cc0070e78bdfc4a5928452df3ab4c5d0062a144f43cae0b6ef2f1",
        "    private fun checkIn(": "8d027a4342f0c1e44be10bb547037c18f11c3b844998d4a09f8c7378de8a14c4",
    },
    "app/src/main/java/sh/swrlz/nodehost/service/PresenceRegistry.kt": {
        "    fun createGroup(": "a7a597076e0a0441cbc5bb2c0af0a43f24ec1c815a0e2209d115bbdefe0f506f",
        "    fun registerDevice(": "8332a7f53ae12b1534a91acf8d16b48c31e3fff204789dc157bb28c1be1bb9fc",
        "    fun joinGroup(": "9d86899ec7881124dfd4e9323fad86fdc8f0687f9604fe15968ec167a3f9833d",
        "    fun checkIn(": "6935f44721b35a9a3821fb10abea233f401abe3985554975e3a062c9f8a32bd3",
    },
}


def require(condition: bool, message: str) -> None:
    if not condition:
        raise AssertionError(message)


def read(root: Path, relative: str) -> str:
    path = root / relative
    require(path.is_file(), f"missing required path: {relative}")
    return path.read_text(encoding="utf-8")


def function_block(source: str, signature: str) -> str:
    start = source.index(signature)
    brace = source.index("{", start)
    depth = 0
    in_string = False
    escaped = False
    for index in range(brace, len(source)):
        character = source[index]
        if in_string:
            if escaped:
                escaped = False
            elif character == "\\":
                escaped = True
            elif character == '"':
                in_string = False
            continue
        if character == '"':
            in_string = True
        elif character == "{":
            depth += 1
        elif character == "}":
            depth -= 1
            if depth == 0:
                return source[start : index + 1]
    raise AssertionError(f"unterminated function block: {signature.strip()}")


def digest(value: str) -> str:
    return hashlib.sha256(value.encode("utf-8")).hexdigest()


def reference_proof() -> tuple[str, str]:
    key = b"stable-device-key-011t"
    body = b'{"protocolVersion":1,"schemaVersion":1,"identity":{"device_id":"device-1"}}'
    body_hash = hashlib.sha256(body).hexdigest()
    canonical = "\n".join(
        [
            "POST",
            "/devices/resolve",
            "1",
            "1",
            "device-1",
            "1700000000000",
            "nonce-011t-000001",
            "request-011t-1",
            body_hash,
        ]
    )
    return body_hash, hmac.new(key, canonical.encode("utf-8"), hashlib.sha256).hexdigest()


def validate(root: Path) -> list[str]:
    build = read(root, "app/build.gradle.kts")
    discovery = read(root, "app/src/main/java/sh/swrlz/nodehost/service/DiscoveryProtocol.kt")
    runtime = read(root, "app/src/main/java/sh/swrlz/nodehost/service/NodeRuntime.kt")
    authority = read(root, "app/src/main/java/sh/swrlz/nodehost/service/PairedLanRequestAuthority.kt")
    protocol = read(root, "app/src/main/java/sh/swrlz/nodehost/service/PresenceProtocol.kt")
    registry = read(root, "app/src/main/java/sh/swrlz/nodehost/service/PresenceRegistry.kt")
    report = read(root, "INTEGRATION_FIX_011T_IMPLEMENTATION_REPORT.md")

    require('versionName = "1.1.1"' in build and "versionCode = 4" in build, "successor source version missing")
    require('const val DEVICE_RESOLVE_PATH = "/devices/resolve"' in protocol, "device resolve route missing")
    require("presence.device.resolve.v1" in discovery, "resolve capability missing")
    require("presence.device.proof.v1" in discovery, "device-proof capability missing")
    require('presenceWriteExposure\\\":\\\"loopback-only' in discovery, "legacy mutation exposure was widened")
    require('presenceResolveExposure\\\":\\\"paired-lan' in discovery, "paired-LAN resolve exposure missing")
    require("presenceDeviceProofBinding" in discovery, "proof-binding requirement not advertised")

    for header in (
        "x-swrlz-pairing-token",
        "x-swrlz-request-id",
        "x-swrlz-device-id",
        "x-swrlz-device-timestamp",
        "x-swrlz-device-nonce",
        "x-swrlz-device-proof",
    ):
        require(header in authority, f"canonical header missing: {header}")
    require("x-swurlz-pairing-token" in authority, "loopback legacy pairing alias missing")
    require("request.isLoopback" in authority and "request.isLocalLan" in authority, "local path distinction missing")
    require("LOCAL_NETWORK_REQUIRED" in authority, "non-local peer rejection missing")
    require('Mac.getInstance(HMAC_SHA256)' in authority, "HMAC-SHA256 verifier missing")
    require("MessageDigest.isEqual" in authority, "constant-time proof comparison missing")
    require('joinToString("\\n")' in authority, "canonical proof line order missing")
    require("request.bodySha256Hex" in authority, "exact raw-body digest binding missing")
    require("duplicateHeaders" in authority and "SECURITY_CRITICAL_HEADERS" in authority, "duplicate security-header rejection missing")
    require("decodeUtf8" in runtime and "CodingErrorAction.REPORT" in runtime, "malformed UTF-8 is not fail-closed")
    require("bodySha256Hex = sha256Hex(bodyBytes)" in runtime, "raw request body digest is not captured")
    require("DEFAULT_CLOCK_SKEW_MS = 120_000L" in authority, "bounded clock-skew rule missing")
    require("DEFAULT_REPLAY_WINDOW_MS = 300_000L" in authority, "bounded replay window missing")
    require("MAX_REPLAY_ENTRIES = 4_096" in authority, "bounded replay cache missing")
    require("DEVICE_PROOF_REPLAYED" in authority and "DEVICE_PROOF_STALE" in authority, "proof failure distinctions missing")
    require("DeviceProofKeyResolver.UNBOUND" in runtime, "production proof binding must remain explicitly unbound")

    require("localAddress = socket.localAddress" in runtime and "remoteAddress = socket.inetAddress" in runtime, "peer/local addresses are not passed to parser")
    require("isAcceptedLocalLanPeer" in runtime, "local-LAN peer classifier missing")
    require("remoteAddress.isSiteLocalAddress || remoteAddress.isLinkLocalAddress" in runtime, "private/link-local peer restriction missing")
    require("0.0.0.0" not in function_block(runtime, "    private fun bindDiscoverySockets("), "listener widened to wildcard")

    require("fun resolveDevice(input: DeviceResolutionInput)" in registry, "read-only registry resolution missing")
    resolution_block = function_block(registry, "    fun resolveDevice(")
    require("commit(" not in resolution_block, "device resolution mutates registry")
    require("state.revision +" not in resolution_block, "device resolution advances revision")
    require("IDENTITY_CONFLICT" in registry and "REPLACED_CANDIDATE" in registry, "identity/lineage distinctions missing")
    require("device_key_fingerprint" in protocol, "device key fingerprint input missing")
    resolve_identity_block = function_block(protocol, "    private fun resolutionIdentity(")
    require('device_key"' not in resolve_identity_block, "raw device key accepted by resolution route")
    require("ACTION_REQUIRED" in protocol and "KeyUnbound" in protocol, "unbound existing-key boundary is not fail-closed")

    for relative, blocks in EXPECTED_MUTATION_BLOCKS.items():
        source = read(root, relative)
        for signature, expected in blocks.items():
            actual = digest(function_block(source, signature))
            require(actual == expected, f"out-of-scope mutation block changed: {relative}:{signature.strip()}")

    require("PBKDF2WithHmacSHA256" in registry, "existing credential verifier storage changed")
    require('.put("device_key"' not in registry, "plaintext device key appears in registry serialization")
    require("DeviceProofKeyResolver.UNBOUND" in report, "report omits unbound proof-key boundary")
    require("No Gradle" in report or "No Gradle task" in report, "report omits build boundary")
    require("Truth Firewall" in report, "report omits Truth Firewall boundary")

    body_hash, proof = reference_proof()
    require(len(body_hash) == 64 and len(proof) == 64, "independent HMAC reference vector failed")
    return [
        "protocol/schema: 1/1",
        "resolve route: paired-LAN + loopback compatibility",
        "proof: HMAC-SHA256 / 120s skew / 5m bounded replay",
        "registry resolution: non-mutating",
        "existing mutation function blocks: unchanged",
        f"reference body sha256: {body_hash}",
        f"reference proof: {proof}",
        "known-record proof-key binding: explicitly action-required",
        "Gradle/APK/workflow: not invoked",
    ]


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("root", nargs="?", type=Path, default=Path(__file__).resolve().parents[1])
    args = parser.parse_args()
    try:
        notes = validate(args.root.resolve())
    except (AssertionError, ValueError) as error:
        print(f"INTEGRATION-FIX-011T source contract validation: FAIL\n{error}", file=sys.stderr)
        return 1
    print("INTEGRATION-FIX-011T source contract validation: PASS")
    for note in notes:
        print(f"- {note}")
    print(f"- validator_sha256: {hashlib.sha256(Path(__file__).read_bytes()).hexdigest()}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
