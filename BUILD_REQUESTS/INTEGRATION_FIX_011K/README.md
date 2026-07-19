# INTEGRATION-FIX-011K — temporary SERVER candidate evidence

This directory retains the deterministic SERVER-first implementation candidate for accepted contract `SWRLZ-PRESENCE-WRITE-V1`, protocol/schema `1 / 1`.

## Authority

```text
Canonical base: SERVER_CFv1.0.3_SWRLZ.zip
Base SHA-256: 127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5

Materialized candidate: SERVER_CFv1.1.0_SWRLZ.zip
Candidate SHA-256: f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f
Candidate size: 143721 bytes
Candidate entries: 69
Internal source delta: exactly 11 paths
```

## Transport

The temporary branch stores a deterministic, lossless replacement bundle as text:

```text
transport/part-*.b64 (ordered concatenation)
Decoded bundle SHA-256: 8e2f07bd5fc2632dbf22280b95198c1a3cf15e975818f2e5b076880ae40108c6
```

Run `materialize_server_v110_011k.py` against the canonical SERVER v1.0.3 ZIP. The script rejects a wrong base, unsafe bundle paths, any replacement-path/content mismatch, a non-deterministic output identity, an invalid ZIP, or an incorrect entry count.

The branch transport does not replace or promote the canonical source lane. A later approval would be required to materialize and place the binary successor in `SOURCES/SERVER/` on `main`.

## Validation boundary

Source-contract validation and local Kotlin core/runtime compile-stub checks passed. Gradle, Android SDK resolution, KSP, Hilt, Compose, APK assembly, signing, installation, release, and deployment were not run.
