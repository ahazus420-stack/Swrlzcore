# INT-PKG-022B + CLIENT CFv2.0.54 Build Repair

**Recorded:** 2026-07-24  
**Status:** Repository CI contract patched; CLIENT repair package prepared and package-verified; GitHub build and device acceptance pending.

## Scope

This checkpoint records two independent failures reported from the latest CLIENT/SERVER test cycle:

1. the shared Source Package Integrity workflow rejected valid ZIP+SHA package pairs because it still required a manifest;
2. CLIENT `CFv2.0.53` reached Kotlin compilation but failed in the new CLIENT bubble surface because of an invalid explicit Compose `weight` import.

SERVER `CFv2.0.37` testing was still in progress when this record was created and is not declared successful or failed here.

## 1. Source Package Integrity failure

### Log-verified symptom

The workflow selected the changed source ZIP and terminated with a missing-manifest error before checksum comparison.

The previous verifier unconditionally executed:

```python
manifest = args.manifest or zip_path.with_suffix('.manifest.json')
if not manifest.is_file():
    raise SystemExit(f'Missing manifest: {manifest}')
```

It also required checksum files to contain two whitespace-separated fields, while current Forge-generated sibling `.sha256` files may contain only the 64-character digest.

### Repository repair

`scripts/ci/verify_swrlz_package_pair.py` now:

- requires the ZIP;
- requires the exact sibling SHA file;
- accepts either `<sha256>` or `<sha256>  <filename>` checksum syntax;
- validates a declared filename when present;
- validates SHA-256 syntax and digest equality;
- treats `<base>.manifest.json` as optional;
- cross-checks manifest fields only when the manifest exists;
- reports the manifest as `null` when absent.

Repository commit:

```text
ef20ac6d49364d28def0c20298ffb5ae0e83da36
```

`.github/workflows/source-package-integrity.yml` now:

- resolves changed `.zip`, `.sha256`, and `.manifest.json` files back to the logical ZIP package;
- verifies checksum-only or optional-manifest-only updates instead of silently selecting no ZIP;
- labels the verification step as checksum plus optional manifest.

Repository commit:

```text
94c81671db364a35992d47b163493e19e945759e
```

### Local verifier evidence

The patched verifier accepted the existing hash-only, manifest-free CLIENT `CFv2.0.53` package and the prepared CLIENT `CFv2.0.54` repair package. This is local static/package evidence, not GitHub Actions acceptance.

## 2. CLIENT CFv2.0.53 build failure

### Log-verified compiler diagnostic

The APK Router reached `:app:compileDebugKotlin` and failed with:

```text
ClientBubbleActivity.kt:22:43 Cannot access
'val RowColumnParentData?.weight: Float': it is internal in file.
```

The build completed Android resource and manifest processing before the Kotlin compiler stopped. This is not a routing, SDK-license, checksum, or Gradle-wrapper failure.

### Root cause

`ClientBubbleActivity.kt` explicitly imported:

```kotlin
import androidx.compose.foundation.layout.weight
```

In the active Compose/Kotlin toolchain, that import resolved to an internal row/column parent-data property. The valid `Modifier.weight(...)` calls are contextual `RowScope` or `ColumnScope` APIs and do not require this explicit import. Existing project screens already use that contextual form.

### CLIENT CFv2.0.54 repair

The bounded repair:

- removes the invalid explicit `foundation.layout.weight` import;
- preserves every `Modifier.weight(...)` call inside its Row or Column receiver scope;
- preserves the separate CLIENT bubble-interface overhaul;
- preserves the visible CLIENT version footer;
- advances Android identity to `versionCode 81`;
- sets `versionName` to `2.0.54-client-bubble-build-repair-v1`;
- updates patch label, source ZIP identity, and patch notes.

Prepared package:

```text
CLIENT_CFv2.0.54_SWRLZ.zip
SHA-256: a52abc7ee219b2f4c84d0bdb4d918d2d2e8a6f3635e5f3d6f33555c76ffb68da
```

The ZIP passed compressed-data integrity testing and the patched package verifier locally. It remains pending Forge upload, GitHub APK Router execution, artifact production, installation, and device rendering.

## Evidence classification

- Integrity verifier implementation: repository-committed.
- Integrity workflow implementation: repository-committed.
- CLIENT compiler cause: log-verified.
- CLIENT source repair: static/source-verified.
- CLIENT repair ZIP/SHA: package-verified locally.
- CLIENT clean GitHub build: pending.
- CLIENT device/runtime acceptance: pending.
- SERVER `CFv2.0.37` build result: pending user report.

## Next evidence gate

1. Upload CLIENT `CFv2.0.54` ZIP and SHA through Forge.
2. Confirm the Source Package Integrity workflow succeeds without a manifest.
3. Confirm the APK Router passes `:app:compileDebugKotlin` and produces a CLIENT APK artifact.
4. Install over the prior same-certificate CLIENT build and verify the visible footer reports `CLIENT · CFv2.0.54`.
5. Record the in-progress SERVER `CFv2.0.37` result separately when its workflow completes.
