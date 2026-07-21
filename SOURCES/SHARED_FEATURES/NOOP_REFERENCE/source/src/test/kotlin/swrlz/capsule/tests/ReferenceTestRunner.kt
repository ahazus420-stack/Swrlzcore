package swrlz.capsule.tests

import swrlz.capsule.api.*
import swrlz.capsule.noop.NoOpCapsule
import swrlz.host.androidref.AndroidReferenceAdapter
import swrlz.host.jvmref.JvmReferenceAdapter

object ReferenceTestRunner {
    private var passed = 0
    private fun test(name: String, block: () -> Unit) {
        try { block(); passed++; println("PASS $name") } catch (t: Throwable) { println("FAIL $name: ${t.message}"); throw t }
    }
    @JvmStatic fun main(args: Array<String>) {
        test("android host deterministic invocation") {
            val adapter = AndroidReferenceAdapter(); val capsule = NoOpCapsule()
            check(capsule.initialize(adapter.exposeServices()).compatible)
            check(capsule.initialize(adapter.exposeServices()).compatible)
            check(capsule.start().compatible)
            val result = capsule.invoke(NoOpRequest("a")) as NoOpResult.Executed
            check(result.capsuleId == "swrlz.reference.noop")
            check(result.hostTimestamp == 1700000000005L)
            check(capsule.stop().compatible)
        }
        test("jvm host deterministic invocation without optional state") {
            val adapter = JvmReferenceAdapter(); val capsule = NoOpCapsule()
            check(capsule.initialize(adapter.exposeServices()).compatible)
            val result = capsule.invoke(NoOpRequest("b")) as NoOpResult.Executed
            check(result.hostTimestamp == 1800000000000L)
            check(capsule.stop().compatible)
        }
        test("missing required service rejects capsule only") {
            val result = NoOpCapsule().validate(JvmReferenceAdapter().exposeServices(includeAudit = false))
            check(!result.compatible && result.code == CompatibilityCode.REQUIRED_SERVICE_MISSING)
        }
        test("unsupported service version is explicit") {
            val result = NoOpCapsule().validate(JvmReferenceAdapter().exposeServices(serviceVersion = 2))
            check(!result.compatible && result.code == CompatibilityCode.SERVICE_VERSION_UNSUPPORTED)
        }
        test("runtime mismatch is explicit") {
            val host = JvmReferenceAdapter().exposeServices().copy(runtimeTarget = "android-framework")
            val result = NoOpCapsule().validate(host)
            check(result.code == CompatibilityCode.RUNTIME_TARGET_UNSUPPORTED)
        }
        test("contract mismatch is explicit") {
            val host = JvmReferenceAdapter().exposeServices().copy(contractVersion = 2)
            val result = NoOpCapsule().validate(host)
            check(result.code == CompatibilityCode.CONTRACT_VERSION_UNSUPPORTED)
        }
        test("malformed descriptor rejects capsule only") {
            val bad = NoOpCapsule.DEFAULT_DESCRIPTOR.copy(capsuleId = "")
            val result = NoOpCapsule(bad).validate(JvmReferenceAdapter().exposeServices())
            check(result.code == CompatibilityCode.DESCRIPTOR_INVALID)
        }
        test("invocation before initialization is rejected") {
            val result = NoOpCapsule().invoke(NoOpRequest("x"))
            check(result is NoOpResult.Rejected && result.reasonCode == CompatibilityCode.INITIALIZATION_CONFLICT)
        }
        test("adapter exception quarantines capsule") {
            val capsule = NoOpCapsule(); val adapter = JvmReferenceAdapter(failOnInvoke = true)
            check(capsule.initialize(adapter.exposeServices()).compatible)
            val result = capsule.invoke(NoOpRequest("x"))
            check(result is NoOpResult.Rejected && result.reasonCode == CompatibilityCode.CAPSULE_QUARANTINED)
            check(capsule.lifecycleState() == LifecycleState.QUARANTINED)
        }
        test("no permissions components or routes declared") {
            val d = NoOpCapsule.DEFAULT_DESCRIPTOR
            check(d.permissions.isEmpty() && d.components.isEmpty() && d.networkRoutes.isEmpty())
        }
        println("RESULT $passed/10 passed")
    }
}
