plugins { kotlin("jvm") version "1.9.0" }
dependencies { implementation(project(":capsule")) }
kotlin { jvmToolchain(17) }
