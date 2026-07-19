# INTEGRATION-FIX-011K validation evidence

## Authority and identity

- Canonical SERVER input SHA-256: `127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5`
- Regenerated SERVER v1.1.0 candidate SHA-256: `f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f`
- Size: `143721` bytes
- ZIP entries: `69`
- Successor delta: exact 11 paths listed in `candidate-manifest.json`

The earlier interrupted workspace did not preserve its claimed candidate bytes. This evidence records the newly regenerated deterministic candidate and does not claim byte identity with that lost workspace artifact.

## Passed checks

1. Canonical base checksum equals repository authority.
2. Canonical base ZIP CRC/integrity passes for all 64 entries.
3. Two independent deterministic generations are byte-identical.
4. Candidate checksum and 69-entry count match generator constants.
5. Candidate ZIP CRC/integrity passes.
6. Candidate-to-base delta is exactly the approved 11 successor paths.
7. Generated-archive source contract validator passes.
8. Local Kotlin core/runtime compile-stub type check passes.
9. Pairing secrets and group/device keys are absent from ordinary response and registry serialization patterns.
10. LAN writes fail closed; public-internet and paid fallback paths are absent.

## Unproven boundary

- Gradle was not invoked.
- Android SDK dependency resolution was not run.
- KSP, Hilt, Compose, manifest/resource merge, and packaging were not run.
- No APK was assembled, signed, installed, or exercised on a device.
- No GitHub Actions workflow was used for candidate preparation.

A separate checkpoint is required to prove Android compilation and APK assembly.

## Temporary-branch transport note

The branch retains the exact 11 replacement files in a deterministic tar.gz bundle encoded as ordered base64 text chunks plus a checksum-gated materializer. Local materialization reproduced the candidate ZIP SHA-256 exactly. The binary ZIP is not promoted into the canonical `SOURCES/SERVER/` lane by this checkpoint.
