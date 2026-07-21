# KEYBOARD-IMP-011A Source Manifest

## Identity

```text
namespace:     com.swrlz.keyboard.app
applicationId: com.swrlz.keyboard.app
label:         SWRLZ Keyboard
surfaceType:   keyboard
versionCode:   1
versionName:   0.1.0
```

## Toolchain profile

```text
Android Gradle Plugin: 8.2.2
Kotlin Android plugin: 1.9.24
compileSdk:            34
targetSdk:             34
minSdk:                24
Java/JVM target:       17
```

## Source files

```text
settings.gradle.kts
build.gradle.kts
gradle.properties
README.md
app/build.gradle.kts
app/proguard-rules.pro
app/src/main/AndroidManifest.xml
app/src/main/java/com/swrlz/keyboard/app/KeyboardImeService.kt
app/src/main/java/com/swrlz/keyboard/app/KeyboardSetupActivity.kt
app/src/main/java/com/swrlz/keyboard/app/policy/EditorContextClassifier.kt
app/src/main/res/drawable/ic_keyboard.xml
app/src/main/res/values/strings.xml
app/src/main/res/xml/method.xml
verification/EditorContextClassifierVerification.kt
verification/README.md
verification/verify-static.py
```

## Implemented behavior

- Android `InputMethodService` declaration and metadata;
- setup activity linking to Android input-method settings and picker;
- lowercase Latin character input;
- space, backspace, and enter behavior;
- protected-editor classification seam;
- fail-closed unknown-context classification;
- ordinary typing remains independent of SWRLZ enrollment or network availability.

## Not implemented

- CLIENT enrollment or IPC;
- NODE_HOST attachment or discovery;
- remote or local AI transformations;
- mission execution;
- clipboard history;
- voice capture;
- content telemetry;
- dictionaries, autocorrect, suggestions, shift/caps, symbols, or localization beyond the initial `en_US` subtype;
- APK build or runtime/device verification.
