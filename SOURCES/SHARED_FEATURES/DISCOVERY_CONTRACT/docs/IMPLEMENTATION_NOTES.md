# Discovery Contract Capsule Implementation Notes

## Scope

`swrlz.discovery.contract` version `0.1.0` implements the portable discovery protocol-v1/schema-v1 wire contract only.

The capsule contains:

- immutable public input, wire, validated, and error models;
- exact protocol and schema constants;
- typed reason codes and warning codes;
- deterministic success and error-body encoding;
- strict structured parsing and compatibility evaluation;
- canonical positive, negative, producer-validation, and determinism vectors;
- an internal `DiscoveryJsonBackend` abstraction;
- a `kotlinx.serialization` JSON-tree backend;
- a strict portable test backend used to verify domain behavior without host or Android attachment.

## Serializer boundary

The public API does not expose `JsonElement`, serializer descriptors, serializer exceptions, or Android types.

The selected production coordinates are:

```text
Kotlin JVM plugin: 1.9.22
kotlinx-serialization-json: 1.6.3
JVM toolchain: 17
```

The implementation uses the JSON-tree API rather than generated serializers, so no serialization compiler plugin is required. Wire trust values decode as raw strings and are then validated into typed fail-closed results.

## Non-authority statement

A compatible result means only that the body satisfies the supported discovery wire contract. It does not prove source authenticity, identity ownership, pairing, trust, authorization, safe mission execution, or route safety.

## Host boundary

The capsule receives or returns no URL, address, HTTP request, socket, Android `Context`, identity store, preference store, token, proof key, trust grant, mission, or observation timestamp.
