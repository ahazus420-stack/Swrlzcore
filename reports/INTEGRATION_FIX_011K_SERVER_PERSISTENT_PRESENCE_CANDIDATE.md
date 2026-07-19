# INTEGRATION-FIX-011K — SERVER persistent presence candidate receipt

The SERVER-first implementation candidate is retained under `BUILD_REQUESTS/INTEGRATION_FIX_011K/` on the temporary checkpoint branch only.

## Candidate identity

```text
SERVER_CFv1.1.0_SWRLZ.zip
f1066675e7bccffac55bc56b8c5a5e4666ad4511c65bf84036a0a8d4bfc8a26f
143721 bytes
69 ZIP entries
```

Canonical base:

```text
SERVER_CFv1.0.3_SWRLZ.zip
127705a321127a05bd4c556016de4bf726fbaaa4e255f256a00e7564f44095c5
64 ZIP entries
```

The successor changes exactly 11 internal source paths and implements the accepted protocol/schema `1 / 1` SERVER slice: durable registry, four pairing-gated writes, registry-backed reads, legacy atomic join, server-derived 90-second online leases, 30-second heartbeat recommendation, fail-closed LAN writes, and preserved identity/trust/Truth Firewall boundaries.

## Evidence boundary

Source contract validation and local Kotlin core/runtime type checks passed. These checks are not an Android Gradle build. No workflow, APK build, release, deployment, installation, CLIENT modification, or `main` promotion is included.
