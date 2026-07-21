# swrlz.discovery.contract

Project-agnostic, stateless Kotlin/JVM discovery wire contract and codec.

This capsule validates and serializes discovery claims. It does not perform networking, create identity, establish pairing or trust, authorize missions, select routes, persist endpoints, or modify host state.

The public API exposes domain models and typed results only. JSON-library types and exceptions remain internal.
