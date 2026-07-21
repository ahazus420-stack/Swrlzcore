plugins {
    kotlin("jvm") version "1.9.22"
}

group = "sh.swrlz"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

kotlin {
    jvmToolchain(17)
}

tasks.register<JavaExec>("runContractTests") {
    group = "verification"
    description = "Runs deterministic discovery contract vectors without Android or host attachment."
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("swrlz.discovery.tests.DiscoveryContractTestRunner")
}
