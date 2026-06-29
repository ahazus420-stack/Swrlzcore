# Required Binary Upload

`BUILD_REQUESTS/000_CURRENT.request` now points at:

`SOURCES/SWRLZ_CORE_2.7.6_NETWORK_DISCOVERY_SOURCE.zip`

The SHA256 companion and patch report have been committed, but the binary ZIP itself must be uploaded through GitHub web UI, Codespaces, or a normal authenticated git push because the ChatGPT GitHub connector used here only exposes text-file writes and does not accept local binary file parameters for repository contents.

Expected ZIP SHA256:

`dc86d24f09ebd843b22deb16c572b4c1ce40a36884740e4635c809cd6f6ca30f`

After the binary ZIP is uploaded into `SOURCES/`, the current build request should work as written.
