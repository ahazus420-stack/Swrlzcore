rootProject.name = "swrlz-noop-reference-capsule"
include(":capsule", ":host-android-reference", ":host-jvm-reference")
project(":capsule").projectDir = file(".")
project(":host-android-reference").projectDir = file("../integrations/android-reference")
project(":host-jvm-reference").projectDir = file("../integrations/jvm-reference")
