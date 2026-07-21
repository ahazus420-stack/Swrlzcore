package swrlz.host.jvmref

import swrlz.capsule.api.*
import swrlz.capsule.noop.NoOpCapsule

class JvmReferenceAuditSink(private val failOnInvoke: Boolean = false) : AuditSink {
    val events = mutableListOf<AuditEvent>()
    override fun record(event: AuditEvent) {
        if (failOnInvoke && event.eventType == "invoke") error("simulated audit adapter failure")
        events += event
    }
}
class JvmReferenceClock : HostClock { override fun now(): Long = 1800000000000L }
class JvmReferenceAdapter(private val failOnInvoke: Boolean = false) {
    val audit = JvmReferenceAuditSink(failOnInvoke)
    fun exposeServices(includeAudit: Boolean = true, serviceVersion: Int = 1): HostServices = HostServices(
        runtimeTarget = "kotlin-jvm",
        contractVersion = 1,
        auditSink = if (includeAudit) audit else null,
        clock = JvmReferenceClock(),
        ephemeralState = null,
        advertisedServiceVersions = mapOf(ServiceIds.AUDIT_SINK to serviceVersion, ServiceIds.CLOCK to 1)
    )
}

object JvmReferenceMain {
    @JvmStatic fun main(args: Array<String>) {
        val adapter = JvmReferenceAdapter()
        val capsule = NoOpCapsule()
        check(capsule.initialize(adapter.exposeServices()).compatible)
        val result = capsule.invoke(NoOpRequest("jvm-reference"))
        check(result is NoOpResult.Executed)
        check(capsule.stop().compatible)
        println(result)
    }
}
