# INT-PKG-022A Implementation Record

**Original date:** 2026-07-22  
**Policy amendment:** 2026-07-24

## Original objective

Replace the previously delivered CLIENT `CFv2.0.10` package with a new versioned packaging-integrity reissue and establish immutable ZIP/checksum/manifest verification for that reissue.

## Original source lineage

- Baseline: CLIENT `CFv2.0.10`
- Target: CLIENT `CFv2.0.11`
- Application behavior: preserved from `INT-CONNECT-021A`
- Change type: packaging-integrity reissue

## Original implementation

- Advanced CLIENT version identity to `CFv2.0.11` / versionCode 42.
- Finalized the ZIP before hashing.
- Generated SHA-256 from the final ZIP bytes.
- Generated an exact-basename checksum file.
- Generated a manifest containing component, version, checkpoint, ZIP basename, SHA-256, size, timestamp, and verified state.
- Re-read checksum and manifest and independently recalculated the final ZIP hash.
- Verified the ZIP size and modification time remained unchanged after hashing.
- Added `scripts/ci/verify_swrlz_package_pair.py` to validate the ZIP/checksum/manifest triple used by this reissue.

## Original evidence

- SOURCE VERSION ADVANCED
- STATIC VERIFICATION PASS
- ZIP INTEGRITY PASS
- CHECKSUM PAIR VERIFIED
- MANIFEST CROSS-VERIFIED
- ZIP UNCHANGED AFTER HASHING
- BUILD NOT RUN
- RUNTIME NOT TESTED

## 2026-07-24 operational policy amendment

The triple-verification record above remains valid historical evidence for CLIENT `CFv2.0.11`. It does not remain the mandatory package contract for every later CFv2.0.x Forge delivery.

### Active required package contract

A complete required source package contains:

1. `<base>.zip`
2. `<base>.sha256`

A sibling `<base>.manifest.json` is optional and is validated when present.

### Rationale

- The current Forge staging and auto-matching flow treats ZIP and SHA as one logical package.
- The current authoritative CLIENT `CFv2.0.51` and SERVER `CFv2.0.36` deliveries are ZIP+SHA pairs.
- A mandatory manifest should be introduced only when it has a defined downstream function such as routing, version enforcement, provenance, release metadata, or policy attestation.
- Mandatory metadata without a defined consumer adds a failure surface without improving the cryptographic verification already provided by the exact-basename SHA pair.

### Required verifier behavior

`verify_swrlz_package_pair.py` and the Source Package Integrity workflow must implement:

```text
ZIP present                    required
exact-basename SHA present     required
SHA syntax valid               required
ZIP digest matches SHA         required
manifest present               optional
manifest cross-check           required only when manifest exists
```

A missing optional manifest must not be reported as a ZIP hash failure.

## Log-verified contract mismatch

The supplied GitHub Actions logs selected:

```text
SOURCES/CLIENT/CLIENT_CFv2.0.50_SWRLZ.zip
```

The workflow then failed with:

```text
Missing manifest: SOURCES/CLIENT/CLIENT_CFv2.0.50_SWRLZ.manifest.json
```

The logs do not show a digest mismatch. The verified defect is that the workflow still enforced the superseded mandatory-manifest rule while current Forge delivery supplied the documented ZIP+SHA pair.

## Exclusions

- This documentation amendment does not itself modify the verification script or workflow.
- It does not weaken checksum verification.
- It does not prohibit manifests.
- It does not declare CLIENT or SERVER runtime acceptance.