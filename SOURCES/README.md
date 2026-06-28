# SWRLZ Sources

Upload source ZIPs and their SHA256 text files here instead of the repository root.

Recommended pattern:

```text
SOURCES/SWRLZ_CORE_2.7.5_ADMIN_REGISTRY_SOURCE.zip
SOURCES/SWRLZ_CORE_2.7.5_ADMIN_REGISTRY_SHA256.txt
SOURCES/SWRLZ_LOCAL_NODE_SERVER_0.7.2_DEVICE_LEDGER_SOURCE.zip
SOURCES/SWRLZ_LOCAL_NODE_SERVER_0.7.2_DEVICE_LEDGER_SHA256.txt
```

The GitHub Actions APK forge searches this folder first, then falls back to old root-level files for compatibility.
