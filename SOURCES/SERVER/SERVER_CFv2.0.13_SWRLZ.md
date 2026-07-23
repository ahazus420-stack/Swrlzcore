# SERVER CFv2.0.13 SWURLZER Delivery Receipt

Checkpoint: INT-FIX-024B  
Baseline: SERVER CFv2.0.12  
Target: SERVER CFv2.0.13  
ZIP: SERVER_CFv2.0.13_SWRLZ.zip  
SHA-256: 0424629319e55e856b0daf6fe3f7bee9725063616dc907514a0fd0b14a573e72  
Size: 18853749 bytes

## Corrections

- Made `GroupProtocol.handle(...)` internal so it no longer exposes internal discovery HTTP request/response types through a public API.
- Added the missing `SwurlzerThemeCatalog` import to SERVER UI preferences.
- Advanced Android identity to `versionCode 15` / `versionName 2.0.13`.

## Verification

- TARGETED STATIC VERIFICATION PASS
- ZIP INTEGRITY PASS
- CHECKSUM PAIR VERIFIED
- MANIFEST CROSS-VERIFIED
- APK/GRADLE BUILD NOT RUN
- RUNTIME TEST NOT RUN
- GITHUB NOT MODIFIED
