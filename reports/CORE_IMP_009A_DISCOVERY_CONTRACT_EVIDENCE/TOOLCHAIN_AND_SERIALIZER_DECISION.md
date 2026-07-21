# Toolchain and Serializer Decision

## Selected source configuration

- Kotlin JVM Gradle plugin: `1.9.22`
- Kotlinx Serialization JSON: `1.6.3`
- JVM toolchain: `17`
- Public API exposure of serializer-library types: none
- Serialization compiler plugin: not required

## Runtime boundary

`DiscoveryContractCodec` references only public discovery domain models. The internal `DiscoveryJsonBackend` translates between codec logic and a neutral JSON tree. `KotlinxSerializationJsonBackend` is the canonical production backend.

## JSON behavior

```text
ignoreUnknownKeys = true
explicitNulls = false
isLenient = false
coerceInputValues = false
encodeDefaults = true
```

Unknown trust-policy and mission-authorization strings are preserved as raw JSON strings until explicit validation, then fail closed through typed reason codes.

## Verification environment

- Local JDK: OpenJDK 21.0.10
- Local Kotlin compiler: kotlinc-jvm 1.9.0

The domain codec, strict parser, canonical vectors, and all test logic compiled and executed locally. The complete production source surface also passed a compile-time API-shape check against temporary local stubs matching the used `kotlinx.serialization` JSON-tree surface.

The official Maven artifact could not be downloaded in the execution environment, so an official dependency-backed Gradle resolution/build was not claimed. The repository declares the exact production coordinate and preserves this limitation for a later independently approved verification environment.
