#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BUILD_SCRIPT="$SCRIPT_DIR/build_swrlz_component.sh"

TMP_ROOT="$(mktemp -d)"
trap 'rm -rf "$TMP_ROOT"' EXIT

PROJECT_DIR="$TMP_ROOT/source/SWRLZ_TEST_SERVER"
mkdir -p "$PROJECT_DIR/app"
printf "rootProject.name = 'SWRLZ_TEST_SERVER'\n" > "$PROJECT_DIR/settings.gradle"
cat > "$PROJECT_DIR/gradlew" <<'EOF'
#!/usr/bin/env bash
set -euo pipefail
mkdir -p app/build/outputs/apk/debug
printf 'synthetic-apk\n' > app/build/outputs/apk/debug/app-debug.apk
EOF
chmod +x "$PROJECT_DIR/gradlew"

(
  cd "$TMP_ROOT/source"
  zip -qr "$TMP_ROOT/source.zip" SWRLZ_TEST_SERVER
)

GITHUB_OUTPUT_FILE="$TMP_ROOT/github-output.txt"
(
  cd "$TMP_ROOT"
  RUNNER_TEMP="$TMP_ROOT/runner-temp" \
  GITHUB_OUTPUT="$GITHUB_OUTPUT_FILE" \
    bash "$BUILD_SCRIPT" \
      SERVER \
      source.zip \
      SERVER_TEST_SWRLZ \
      debug \
      relative-work \
      relative-artifacts/SERVER
)

ARTIFACT_DIR="$TMP_ROOT/relative-artifacts/SERVER"
APK="$ARTIFACT_DIR/SERVER_TEST_SWRLZ_DEBUG.apk"
BUNDLE="$ARTIFACT_DIR/SERVER_TEST_SWRLZ_DEBUG_APK_DOWNLOAD.zip"

[[ -f "$APK" ]]
[[ -f "$APK.sha256" ]]
[[ -f "$ARTIFACT_DIR/BUILD_LOG.txt" ]]
[[ -f "$ARTIFACT_DIR/BUILD_PROVENANCE_REPORT.md" ]]
[[ -f "$ARTIFACT_DIR/BUILD_PROVENANCE_REPORT.md.sha256" ]]
[[ -f "$BUNDLE" ]]
[[ -f "$BUNDLE.sha256" ]]
[[ ! -e "$ARTIFACT_DIR/relative-artifacts" ]]

unzip -l "$BUNDLE" | grep -F 'SERVER_TEST_SWRLZ_DEBUG.apk' >/dev/null
grep -F -- '- Selected source path: /' "$ARTIFACT_DIR/BUILD_PROVENANCE_REPORT.md" >/dev/null
grep -F -- "artifact_dir=$ARTIFACT_DIR" "$GITHUB_OUTPUT_FILE" >/dev/null
grep -F -- "bundle=$BUNDLE" "$GITHUB_OUTPUT_FILE" >/dev/null

echo 'PASS: relative workflow paths were normalized and the APK download ZIP was created safely.'
