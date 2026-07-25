# Documentation Evidence Model v1

**Status:** Accepted documentation-governance baseline for CFv2.1.x and future SWRLZ Brain ingestion.
**Recorded:** 2026-07-25

## Evidence levels

- `SOURCE_VERIFIED` — directly present in referenced source.
- `STATIC_VERIFIED` — syntax/resource/schema/static checks passed.
- `COMPILE_VERIFIED` — relevant project compiled successfully.
- `APK_BUILT` — installable artifact produced.
- `DEVICE_VERIFIED` — behavior observed on real hardware.
- `INTEGRATION_VERIFIED` — cross-component path exercised end to end.
- `DOCUMENTED` — described but not independently verified.
- `INFERRED` — derived from surrounding evidence and must not be presented as fact.
- `UNKNOWN` — evidence insufficient.

## Rules

1. Source presence is not equivalent to compile success.
2. Compile success is not equivalent to device/runtime acceptance.
3. Architecture decisions are not implementation evidence.
4. Workflow transfer at 100% is not commit/build success.
5. Historical source trees under `.reference` remain evidence baselines unless explicitly promoted as current authority.
6. Every future Brain Pack fact should retain provenance, version scope, evidence level, confidence, and supersession relationships.

## Package accounting

Future handoffs must separately report workspace source counts, packaged source counts, documentation counts, exclusions, and archive entry counts. Do not reuse extraction-workspace counts as distributed-package counts.
