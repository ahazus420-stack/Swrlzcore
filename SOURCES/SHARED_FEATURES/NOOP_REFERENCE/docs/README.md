# SWRLZ No-Op Reference Capsule

This bounded reference implementation proves one canonical Kotlin/JVM capsule can attach to two independent hosts through distinct adapters. It performs no networking, privileged operations, identity mutation, trust decision, entitlement decision, or remote fallback.

## Constitutional relationships

- hosts **expose** services;
- adapters **translate** host services;
- projects **attach** or **compose** the capsule;
- runtimes **invoke** behavior;
- packages **preserve** lineage.

## Build and test

The evidence checkpoint compiles the source with `kotlinc`, produces a single test JAR, runs ten deterministic tests, and creates the canonical source ZIP and sibling SHA-256.
