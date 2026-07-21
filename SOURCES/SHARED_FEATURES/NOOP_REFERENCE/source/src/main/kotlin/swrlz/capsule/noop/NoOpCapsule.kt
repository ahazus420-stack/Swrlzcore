package swrlz.capsule.noop

import swrlz.capsule.api.*

class NoOpCapsule(
    val descriptor: CapsuleDescriptor = DEFAULT_DESCRIPTOR
) {
    private var state: LifecycleState = LifecycleState.NEW
    private var services: HostServices? = null

    fun validate(host: HostServices): CompatibilityResult {
        if (descriptor.capsuleId.isBlank() || descriptor.capsuleVersion.isBlank()) {
            return incompatible(CompatibilityCode.DESCRIPTOR_INVALID, "capsule identity is blank", host)
        }
        if (host.contractVersion != descriptor.contractVersion) {
            return incompatible(CompatibilityCode.CONTRACT_VERSION_UNSUPPORTED, "contract version mismatch", host)
        }
        if (host.runtimeTarget !in descriptor.runtimeTargets) {
            return incompatible(CompatibilityCode.RUNTIME_TARGET_UNSUPPORTED, "runtime target unsupported", host)
        }
        val missing = mutableListOf<String>()
        if (host.auditSink == null) missing += ServiceIds.AUDIT_SINK.value
        if (host.clock == null) missing += ServiceIds.CLOCK.value
        if (missing.isNotEmpty()) {
            return incompatible(CompatibilityCode.REQUIRED_SERVICE_MISSING, missing.joinToString(","), host)
        }
        val unsupported = descriptor.requiredServices.firstOrNull {
            host.advertisedServiceVersions[it] != 1
        }
        if (unsupported != null) {
            return incompatible(CompatibilityCode.SERVICE_VERSION_UNSUPPORTED, unsupported.value, host)
        }
        val result = CompatibilityResult(true, CompatibilityCode.COMPATIBLE, "compatible")
        host.auditSink!!.record(AuditEvent(descriptor.capsuleId, "compatibility", result.code.name, host.clock!!.now()))
        return result
    }

    fun initialize(host: HostServices): CompatibilityResult {
        if (state == LifecycleState.QUARANTINED) {
            return CompatibilityResult(false, CompatibilityCode.CAPSULE_QUARANTINED, "capsule quarantined")
        }
        val compatibility = validate(host)
        if (!compatibility.compatible) return compatibility
        if (state == LifecycleState.INITIALIZED || state == LifecycleState.STARTED) {
            host.auditSink!!.record(AuditEvent(descriptor.capsuleId, "initialize", "IDEMPOTENT", host.clock!!.now()))
            return CompatibilityResult(true, CompatibilityCode.COMPATIBLE, "already initialized")
        }
        services = host
        state = LifecycleState.INITIALIZED
        host.ephemeralState?.put(descriptor.storageNamespace, "state", state.name)
        host.auditSink!!.record(AuditEvent(descriptor.capsuleId, "initialize", "INITIALIZED", host.clock!!.now()))
        return CompatibilityResult(true, CompatibilityCode.COMPATIBLE, "initialized")
    }

    fun start(): CompatibilityResult {
        val host = services ?: return CompatibilityResult(false, CompatibilityCode.INITIALIZATION_CONFLICT, "initialize first")
        if (state == LifecycleState.QUARANTINED) return CompatibilityResult(false, CompatibilityCode.CAPSULE_QUARANTINED, "capsule quarantined")
        state = LifecycleState.STARTED
        host.ephemeralState?.put(descriptor.storageNamespace, "state", state.name)
        host.auditSink!!.record(AuditEvent(descriptor.capsuleId, "start", "STARTED", host.clock!!.now()))
        return CompatibilityResult(true, CompatibilityCode.COMPATIBLE, "started")
    }

    fun invoke(request: NoOpRequest): NoOpResult {
        val host = services ?: return NoOpResult.Rejected(CompatibilityCode.INITIALIZATION_CONFLICT, "initialize first")
        if (state != LifecycleState.INITIALIZED && state != LifecycleState.STARTED) {
            return NoOpResult.Rejected(CompatibilityCode.INITIALIZATION_CONFLICT, "invalid lifecycle state: $state")
        }
        return try {
            val timestamp = host.clock!!.now()
            host.auditSink!!.record(AuditEvent(descriptor.capsuleId, "invoke", "EXECUTED", timestamp))
            NoOpResult.Executed(descriptor.capsuleId, descriptor.capsuleVersion, timestamp, "EXECUTED")
        } catch (t: Throwable) {
            state = LifecycleState.QUARANTINED
            NoOpResult.Rejected(CompatibilityCode.CAPSULE_QUARANTINED, t::class.simpleName ?: "adapter exception")
        }
    }

    fun stop(): CompatibilityResult {
        val host = services ?: return CompatibilityResult(false, CompatibilityCode.INITIALIZATION_CONFLICT, "initialize first")
        state = LifecycleState.STOPPED
        host.ephemeralState?.clear(descriptor.storageNamespace)
        host.auditSink!!.record(AuditEvent(descriptor.capsuleId, "stop", "STOPPED", host.clock!!.now()))
        services = null
        return CompatibilityResult(true, CompatibilityCode.COMPATIBLE, "stopped")
    }

    fun lifecycleState(): LifecycleState = state

    private fun incompatible(code: CompatibilityCode, detail: String, host: HostServices): CompatibilityResult {
        host.auditSink?.let { sink -> host.clock?.let { clock -> sink.record(AuditEvent(descriptor.capsuleId, "compatibility", code.name, clock.now())) } }
        return CompatibilityResult(false, code, detail)
    }

    companion object {
        val DEFAULT_DESCRIPTOR = CapsuleDescriptor(
            capsuleId = "swrlz.reference.noop",
            capsuleVersion = "0.1.0",
            contractVersion = 1,
            runtimeTargets = setOf("kotlin-jvm"),
            requiredServices = setOf(ServiceIds.AUDIT_SINK, ServiceIds.CLOCK),
            optionalServices = setOf(ServiceIds.EPHEMERAL_STATE),
            storageNamespace = "swrlz.reference.noop",
            storageSchemaVersion = 1,
            failurePolicy = "optional-isolated",
            truthFirewallImpact = "audit-only"
        )
    }
}
