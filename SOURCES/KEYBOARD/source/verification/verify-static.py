#!/usr/bin/env python3
from pathlib import Path
import hashlib
import re
import sys
import xml.etree.ElementTree as ET

root = Path(__file__).resolve().parents[1]
failures = []
passes = []

def require(condition: bool, name: str) -> None:
    (passes if condition else failures).append(name)

manifest = root / "app/src/main/AndroidManifest.xml"
method = root / "app/src/main/res/xml/method.xml"
build_file = root / "app/build.gradle.kts"
service_file = root / "app/src/main/java/com/swrlz/keyboard/app/KeyboardImeService.kt"
classifier_file = root / "app/src/main/java/com/swrlz/keyboard/app/policy/EditorContextClassifier.kt"

for path in [manifest, method, build_file, service_file, classifier_file]:
    require(path.is_file(), f"required file exists: {path.relative_to(root)}")

ET.parse(manifest)
ET.parse(method)
passes.append("XML parses")

manifest_text = manifest.read_text()
build_text = build_file.read_text()
service_text = service_file.read_text()
kotlin_text = "\n".join(path.read_text(errors="ignore") for path in root.rglob("*.kt") if path.is_file())

require('applicationId = "com.swrlz.keyboard.app"' in build_text, "unique applicationId")
require('namespace = "com.swrlz.keyboard.app"' in build_text, "unique namespace")
require('versionCode = 1' in build_text, "versionCode 1")
require('versionName = "0.1.0"' in build_text, "versionName 0.1.0")
require('android.permission.BIND_INPUT_METHOD' in manifest_text, "BIND_INPUT_METHOD service permission")
require('android.view.InputMethod' in manifest_text, "InputMethod action")
require('android.view.im' in manifest_text, "IME metadata")
require('uses-permission' not in manifest_text, "no manifest permissions requested")
require('android.permission.INTERNET' not in manifest_text, "no INTERNET permission")
require('com.swrlz.core.app' not in kotlin_text and 'com.swrlz.core.app' not in build_text and 'com.swrlz.core.app' not in manifest_text, "Core app identity not copied")
require('commitText(character.toString(), 1)' in service_text, "character input")
require('commitText(" ", 1)' in service_text, "space input")
require('deleteSurroundingText(1, 0)' in service_text, "backspace input")
require('commitText("\\n", 1)' in service_text, "enter input")
require('EditorContextClassifier.classify' in service_text, "protected-editor classification seam")
require(not re.search(r'\b(Log\.|println\(|print\()', service_text), "no service logging")
require(not re.search(r'ClipboardManager|getPrimaryClip|setPrimaryClip', kotlin_text), "no clipboard feature")
require('SpeechRecognizer' not in kotlin_text and 'RECORD_AUDIO' not in manifest_text, "no voice capture")
require(not re.search(r'java\.net|okhttp|retrofit|HttpUrl|Socket\(|URL\(', kotlin_text), "no network implementation")
require(not re.search(r'import .*client|import .*nodehost|ClientBridge|NodeHost', kotlin_text, re.IGNORECASE), "no host attachment code")

for name in passes:
    print(f"PASS {name}")
for name in failures:
    print(f"FAIL {name}")

print(f"RESULT {len(passes)} passed / {len(failures)} failed")
sys.exit(1 if failures else 0)
