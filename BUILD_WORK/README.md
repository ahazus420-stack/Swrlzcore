# SWRLZ Build Work

This folder is reserved for temporary source extraction and build work during GitHub Actions runs.

The workflow extracts uploaded source ZIPs under:

```text
BUILD_WORK/<release>/extracted/
```

Build output is then copied to GitHub Actions artifacts and, when requested, into `RELEASES/`.

Do not manually store permanent source or APK files here.
