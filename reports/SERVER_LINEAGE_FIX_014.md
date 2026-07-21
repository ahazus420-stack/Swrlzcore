# SERVER-LINEAGE-FIX-014

## Purpose

Canonicalize the verified SERVER v1.0.4 successor that was uploaded with Android/browser duplicate-download suffixes, then preserve SERVER v1.0.3 as archived lineage.

## Approval

`Approve SERVER-LINEAGE-FIX-014 — verify and canonicalize the v1.0.4 source pair, then archive v1.0.3 on a checkpoint branch only without building, publishing, promoting main, or closing pull requests`

## Base

- Repository: `ahazus420-stack/Swrlzcore`
- Base branch: `main`
- Base commit: `e6621447b1e666a5c121a6a69045be8e87c506ab`
- Checkpoint branch: `checkpoint/server-lineage-fix-014`

## v1.0.4 input transport names

- `SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ(2).zip`
  - Git blob: `281db43117a18a2d4cd38fe6af5944d29a973a63`
- `SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ(1).sha256`
  - Git blob: `31cff21854abdd29deb885eb5bf3a5b980c1e4cd`
  - Declared canonical target: `SERVER_CFv1.0.4_SWRLZ.zip`
  - Declared SHA-256: `32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6`

The `(1)` and `(2)` suffixes are treated as duplicate-download transport metadata, not source-version identity.

## Verification basis

Existing accepted evidence for the `32114c2658a315dddc86d2ce2f0f790cc7d7e6a5bea506e1ef909aea404125e6` v1.0.4 candidate records:

- ZIP CRC/integrity: PASS;
- duplicate entries: none;
- deterministic ordering and timestamps: PASS;
- exactly seven approved changed paths;
- no unapproved content changes;
- canonical v1.0.3 input checksum: PASS;
- no APK or workflow execution during static archive validation.

This checkpoint does not rebuild or rewrite the binary. It reuses the existing repository Git blob byte-for-byte at the canonical path and reuses the existing checksum blob byte-for-byte. The connector exposes the Git blob identity but not a separate server-calculated SHA-256 field; the SHA-256 binding therefore relies on the existing checksum file and accepted static validation evidence rather than claiming a new independent binary rehash in this checkpoint.

## Canonical v1.0.4 output

- `SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ.zip`
- `SOURCES/SERVER/SERVER_CFv1.0.4_SWRLZ.sha256`

Both canonical paths reuse the original suffixed-path blobs. No ZIP or checksum contents are modified.

## Archived v1.0.3 lineage

- `SOURCES/SERVER/OLD_PATCHES/SERVER_CFv1.0.3_SWRLZ.zip`
  - Git blob: `925546a6ed513b463603a56b41836f43608ed8b0`
- `SOURCES/SERVER/OLD_PATCHES/SERVER_CFv1.0.3_SWRLZ.sha256`
  - Git blob: `92c28cc5686d9d73fd79e7047e38bd8c9e702848`
  - Declared SHA-256: `127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5`

The v1.0.3 blobs are moved without modification and remain recoverable as explicit predecessor lineage.

## Filename normalization contract

A terminal duplicate-download suffix matching `(<positive integer>)` immediately before the extension may be ignored for candidate-name resolution only. Normalization must fail closed unless:

1. the normalized canonical name is valid;
2. ZIP integrity passes;
3. checksum association is resolved after applying the same normalization rule;
4. the calculated ZIP digest matches the declared digest;
5. ambiguous candidates are rejected unless byte identity is proven.

Canonical repository paths remain unsuffixed.

## Explicitly untouched

- `.github/workflows/build-swrlz-server-apk.yml` and all other workflows;
- CLIENT files;
- pull requests, including stale open PRs;
- release artifacts and update manifests;
- APKs and build outputs;
- `main`;
- checkpoint and temporary branch deletion.

## Operation guarantees

- No build performed.
- No workflow dispatched.
- No publication, release, deployment, or installation performed.
- No pull request closed or merged.
- No update to `main` performed.
