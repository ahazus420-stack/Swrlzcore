# INTEGRATION-FIX-011T-B source candidate transport

This temporary-branch checkpoint stores a text-safe deterministic replacement
bundle for the approved SERVER v1.1.1 source candidate.

## Base

```text
SERVER_CFv1.1.0_SWRLZ.zip
f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f
```

Materialize the exact base through `INTEGRATION_FIX_011K` first when the ZIP is
not already present.

## Materialize

```bash
python BUILD_REQUESTS/INTEGRATION_FIX_011T_B/materialize_server_v111_011tb.py \
  --base SERVER_CFv1.1.0_SWRLZ.zip \
  --output SERVER_CFv1.1.1_SWRLZ.zip
```

Expected output:

```text
762d72e445a3a9fcb48da11905dbc0261b206060b55760dfa96fefbf1e9486e4
159733 bytes
73 entries
```

## Validate without Gradle

Extract the candidate and run:

```bash
python SWRLZ_NODE_HOST/scripts/test_device_proof_011tb.py \
  SWRLZ_NODE_HOST \
  --base <extracted SERVER v1.1.0 SWRLZ_NODE_HOST directory>
```

The transparent replacement transport is stored as ordered UTF-8 unified-diff files under `patches/`.

No workflow, Gradle task, APK build, installation, release, or deployment is
triggered by this transport or materializer.
