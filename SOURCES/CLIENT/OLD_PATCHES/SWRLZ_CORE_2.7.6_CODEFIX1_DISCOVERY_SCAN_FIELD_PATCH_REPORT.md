# SWRLZ-Core 2.7.6 CODEFIX1 - Discovery Scan Field Priority

## Field evidence

Network Discovery was successful when using TEST SIGNATURE against the typed node URL, but SCAN LOCAL could report no valid node found without clearly trying the current Node URL field.

## Fix

- SCAN LOCAL now captures the current Node URL field immediately.
- The typed/current Node URL is always the first candidate.
- The saved Core Node URL is second.
- Same-subnet LAN candidates are inferred from the typed IP when possible.
- Common defaults remain as fallback candidates.
- Timeout increased from 900ms to 1200ms for LAN probes.
- Result text now says the current field is tried first.

## Build identity

- versionName: `0.2.7.6-codefix1-discovery-scan`
- versionCode: `23`
- Patch: `2.7.6-CODEFIX1-DISCOVERY-SCAN-FIELD`
- Source: `SWRLZ_CORE_2.7.6_CODEFIX1_DISCOVERY_SCAN_FIELD_SOURCE.zip`
