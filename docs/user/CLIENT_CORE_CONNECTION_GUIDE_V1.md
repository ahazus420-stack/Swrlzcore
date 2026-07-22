# CLIENT Core Connection Guide V1

## Auto-connect

`AUTO-CONNECT ON APP LAUNCH` defaults to enabled and is persisted locally.

When enabled, the modern User Mode Core shell:

1. tries the saved SERVER;
2. tries the last-good SERVER;
3. runs approved discovery when needed;
4. verifies the SWRLZ discovery signature and status surface;
5. registers the CLIENT;
6. begins heartbeat presence.

Auto-connect does not bypass identity, proof, trust, protocol compatibility, or Truth Firewall gates.

## Controls

### Disconnected

- `CONNECT`
- `DISCOVERY`

### Connecting

- full-width `CONNECTING…`
- conflicting actions disabled

### Connected

- `DISCONNECT`
- `SERVER DETAILS`

## Disconnect behavior

Disconnect stops heartbeat and changes live presence to OFFLINE. It retains the durable SERVER registration and saved SERVER selection.

Disconnect does not forget the SERVER, unregister the CLIENT, delete lineage, revoke trust, or remove proof records.

## Device identity

A privacy-preserving binding distinguishes the physical device from an app installation. Reinstalling or regenerating installation identity should update lineage rather than create a second active device card.
