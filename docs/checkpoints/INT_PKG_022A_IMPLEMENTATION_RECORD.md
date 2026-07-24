# INT-PKG-022A Implementation Record

**Original date:** 2026-07-22  
**Policy amendment:** 2026-07-24  
**Implementation alignment:** 2026-07-24

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

The SHA file may use either supported representation:

```text
<64-character sha256>
<64-character sha256>  <zip filename>
```

A sibling `<base>.manifest.json` is optional and is validated when present.

### Rationale

- The current Forge staging and auto-matching flow treats ZIP and SHA as one logical package.
- Current CLIENT and SERVER Forge deliveries use ZIP+SHA pairs.
- Some Forge-generated checksum siblings contain only the digest, while conventional checksum tools may include the filename.
- A mandatory manifest should be introduced only when it has a defined downstream function such as routing, version enforcement, provenance, release metadata, or policy attestation.
- Mandatory metadata without a defined consumer adds a failure surface without improving the cryptographic verification already provided by the exact-basename SHA pair.

## Required verifier behavior

`verify_swrlz_package_pair.py` and the Source Package Integrity workflow implement:

```text
ZIP present                         required
exact-basename SHA sibling present  required
SHA syntax valid                    required
hash-only SHA form                  accepted
digest-plus-filename SHA form       accepted
ZIP digest matches SHA              required
manifest present                    optional
manifest cross-check                required only when manifest exists
```

A missing optional manifest is not reported as a ZIP hash failure.

## Log-verified contract mismatch

The supplied GitHub Actions logs selected a current source ZIP and failed with a missing-manifest error before completing checksum verification. The logs did not show a digest mismatch. The verified defect was that the workflow still enforced the superseded mandatory-manifest rule while current Forge delivery supplied the documented ZIP+SHA pair.

## 2026-07-24 repository implementation alignment

The policy is now implemented in repository code:

- `scripts/ci/verify_swrlz_package_pair.py` treats the manifest as optional and accepts both supported SHA file forms.
- `.github/workflows/source-package-integrity.yml` resolves changed ZIP, SHA, or optional manifest files to the corresponding logical source ZIP.
- Checksum-only changes are no longer skipped by the workflow resolver.
- Manifest validation remains strict when a manifest is supplied.

Implementation commits:

```text
ef20ac6d49364d28def0c20298ffb5ae0e83da36
94c81671db364a35992d47b163493e19e945759e
```

Local verification accepted the hash-only, manifest-free CLIENT `CFv2.0.53` package and the prepared CLIENT `CFv2.0.54` repair package. GitHub Actions acceptance remains evidence-gated until the next package-triggered run completes successfully.

## Exclusions

- The alignment does not weaken SHA-256 verification.
- It does not prohibit manifests.
- It does not declare CLIENT or SERVER runtime acceptance.
- It does not declare the next GitHub Actions run successful before that run completes.
