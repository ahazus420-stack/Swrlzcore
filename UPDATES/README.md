# SWRLZ Updates

This folder is for update manifests used by the client APK and future Node Host APK.

Two update modes are planned:

## 1. APK install update

Used for code changes. The app checks a manifest, downloads the APK or APK bundle, verifies SHA-256, and opens the Android installer. Normal Android requires the user to approve app installation or update.

## 2. Hot-patch asset update

Used for non-code files that the app/server reads as data, such as theme packs, safe command definitions, route templates, docs, rules, prompts, public configuration, and other runtime assets.

Hot patches should be downloaded, hash-verified, staged, applied, and rollback-capable. Hot patches must not replace compiled Kotlin/Java app code inside the APK.

Current workflow output, when release artifacts are committed, writes:

```text
UPDATES/<release>_manifest.json
UPDATES/latest_client.json
```
