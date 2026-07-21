plugins { kotlin("jvm") version "1.9.0" }
group = "swrlz.reference"
version = "0.1.0"
repositories { mavenCentral() }
kotlin { jvmToolchain(17) }
tasks.test { useJUnitPlatform() }
