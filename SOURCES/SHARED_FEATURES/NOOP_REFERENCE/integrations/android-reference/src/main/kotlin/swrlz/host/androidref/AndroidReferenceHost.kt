package swrlz.host.androidref

import swrlz.capsule.api.*
import swrlz.capsule.noop.NoOpCapsule

class AndroidReferenceAuditSink : AuditSink {
    val events = mutableListOf<AuditEvent>()
    override fun record(event: AuditEvent) { events += event }
}

class AndroidReferenceClock(private var current: Long = 1700000000000L) : HostClock {
    override fun now(): Long = current++
}

class AndroidReferenceState : EphemeralState {
    private val values = mutableMapOf<String, String>()
    override fun put(namespace: String, key: String, value: String) { values["$namespace:$key"] = value }
    override fun get(namespace: String, key: String): String? = values["$namespace:$key"]
    override fun clear(namespace: String) { values.keys.filter { it.startsWith("$namespace:") }.toList().forEach(values::remove) }
}

class AndroidReferenceAdapter {
    val audit = AndroidReferenceAuditSink()
    val clock = AndroidReferenceClock()
    val state = AndroidReferenceState()
    fun exposeServices(): HostServices = HostServices(
        runtimeTarget = "kotlin-jvm",
        contractVersion = 1,
        auditSink = audit,
        clock = clock,
        ephemeralState = state,
        advertisedServiceVersions = mapOf(ServiceIds.AUDIT_SINK to 1, ServiceIds.CLOCK to 1, ServiceIds.EPHEMERAL_STATE to 1)
    )
}

object AndroidReferenceMain {
    @JvmStatic fun main(args: Array<String>) {
        val adapter = AndroidReferenceAdapter()
        val capsule = NoOpCapsule()
        check(capsule.initialize(adapter.exposeServices()).compatible)
        check(capsule.start().compatible)
        val result = capsule.invoke(NoOpRequest("android-reference"))
        check(result is NoOpResult.Executed)
        check(capsule.stop().compatible)
        println(result)
    }
}
