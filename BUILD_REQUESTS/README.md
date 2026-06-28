# SWRLZ Build Requests

This folder lets Swurlz start a GitHub Actions build after you upload source files.

Upload source ZIPs and SHA files into `SOURCES/`, then ask Swurlz to create a build request trigger.

Trigger files use the `.trigger` extension and simple key-value lines:

```text
source_zip=SOURCES/SWRLZ_CORE_2.7.5_ADMIN_REGISTRY_SOURCE.zip
sha_file=SOURCES/SWRLZ_CORE_2.7.5_ADMIN_REGISTRY_SHA256.txt
release_name=SWRLZ_CORE_2.7.5_ADMIN_REGISTRY
commit_release_artifacts=true
```

When a `.trigger` file is committed, the APK forge workflow runs, extracts the selected source into `BUILD_WORK/`, builds the APK, uploads a GitHub Actions artifact, and optionally commits milestone output to `RELEASES/` plus update manifests under `UPDATES/`.
