# INTEGRATION-FIX-011T — temporary SERVER source candidate evidence

This directory retains the deterministic SERVER source successor for the first accepted paired-LAN bootstrap slice.

## Authority

```text
Exact base: SERVER_CFv1.1.0_SWRLZ.zip
Base SHA-256: f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f
Base entries: 69

Materialized candidate: SERVER_CFv1.1.1_SWRLZ.zip
Candidate SHA-256: 63ef4e92e4a582be8a9a81dcc193fc0608f9b8e14362f9a688be72668f4211c5
Candidate size: 156982 bytes
Candidate entries: 72
Internal source delta: exactly 8 paths
```

The candidate implements paired local-LAN request classification, canonical pairing/request headers, exact raw-body digest capture, duplicate security-header rejection, strict UTF-8 decoding, HMAC-SHA256 device-proof verification infrastructure, bounded timestamp/nonce/replay controls, and non-mutating `POST /devices/resolve` classification.

## Fail-closed proof-key boundary

SERVER v1.1.0 stores salted PBKDF2 device-key verifiers and cannot recover the original key needed for HMAC verification. Because this checkpoint authorizes neither registration changes nor a registry migration, production wiring remains `DeviceProofKeyResolver.UNBOUND`. Existing known records therefore return `ACTION_REQUIRED` without device or membership details until a later separately approved key-binding slice.

The candidate does not use pairing as device proof, does not accept a raw device key in `/devices/resolve`, and does not use the PBKDF2 verifier as an HMAC key.

## Transport

The temporary branch stores a deterministic, lossless replacement bundle as ordered text:

```text
transport/part-*.b64
Decoded bundle SHA-256: 396613db4e1d4e660382c4d63fd5a8f45aad3cadafa7b552a25d11590af22d79
```

Run `materialize_server_v111_011t.py` against the exact v1.1.0 base. The script rejects a wrong base, unsafe bundle paths, replacement mismatch, non-deterministic output, invalid ZIP, or incorrect entry count.

## Validation boundary

Static source-contract checks, pure Kotlin type-stub checks, runtime-integration type-stub checks, and executable authorization behavior tests passed. Gradle, Android dependency resolution, APK assembly, signing, workflow execution, installation, release, deployment, merge, rebase, and `main` promotion were not performed.
