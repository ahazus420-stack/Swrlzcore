#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat >&2 <<'EOF'
Usage: build_swrlz_component.sh COMPONENT SOURCE_ZIP CANONICAL_STEM VARIANT WORK_DIR ARTIFACT_DIR

Components: CLIENT SERVER CORE_BASE KEYBOARD_BASE LAUNCHER_BASE
Variants: debug release
EOF
  exit 64
}

[[ $# -eq 6 ]] || usage

COMPONENT="${1^^}"
SOURCE_ZIP="$2"
CANONICAL_STEM="$3"
VARIANT="${4,,}"
WORK_DIR="$5"
ARTIFACT_DIR="$6"

case "$COMPONENT" in
  CLIENT|SERVER|CORE_BASE|KEYBOARD_BASE|LAUNCHER_BASE) ;;
  *) echo "Unsupported component: $COMPONENT" >&2; exit 64 ;;
esac

case "$VARIANT" in
  debug) GRADLE_TASK=':app:assembleDebug' ;;
  release) GRADLE_TASK=':app:assembleRelease' ;;
  *) echo "Unsupported build variant: $VARIANT" >&2; exit 64 ;;
esac

[[ -f "$SOURCE_ZIP" ]] || { echo "Source ZIP not found: $SOURCE_ZIP" >&2; exit 66; }

rm -rf "$WORK_DIR" "$ARTIFACT_DIR"
mkdir -p "$WORK_DIR/extracted" "$ARTIFACT_DIR"
unzip -q "$SOURCE_ZIP" -d "$WORK_DIR/extracted"

PROJECT_ROOT=''
BUILD_MODE='gradle-wrapper'

if [[ "$COMPONENT" == 'CLIENT' ]]; then
  BUILD_SCRIPT="$(find "$WORK_DIR/extracted" -maxdepth 10 -type f -path '*/scripts/install_android_sdk_and_build.sh' -print | sort | head -n 1 || true)"
  [[ -n "$BUILD_SCRIPT" ]] || {
    echo 'CLIENT archive does not contain scripts/install_android_sdk_and_build.sh' >&2
    exit 65
  }
  PROJECT_ROOT="$(dirname "$(dirname "$BUILD_SCRIPT")")"
  chmod +x "$BUILD_SCRIPT"
  BUILD_MODE='client-install-script'

  [[ -n "${OPENAI_API_KEY:-}" ]] || { echo 'OPENAI_API_KEY is required for CLIENT builds.' >&2; exit 65; }
  [[ -n "${SWRLZ_API_TOKEN:-}" ]] || { echo 'SWRLZ_API_TOKEN is required for CLIENT builds.' >&2; exit 65; }

  BACKEND_DIR="$(find "$PROJECT_ROOT" -maxdepth 3 -type d -name backend -print | sort | head -n 1 || true)"
  [[ -n "$BACKEND_DIR" ]] || { echo 'CLIENT backend directory was not found.' >&2; exit 65; }
  {
    echo 'SWRLZ_DATA_DIR=./data'
    echo "OPENAI_API_KEY=$OPENAI_API_KEY"
    echo 'OPENAI_MODEL=gpt-5.4-mini'
    echo "SWRLZ_API_TOKEN=$SWRLZ_API_TOKEN"
    echo 'CORS_ORIGINS=*'
  } > "$BACKEND_DIR/.env"
else
  WRAPPER="$(find "$WORK_DIR/extracted" -type f -name gradlew -not -path '*/.gradle/*' -print | sort | head -n 1 || true)"

  if [[ -z "$WRAPPER" && ( "$COMPONENT" == 'KEYBOARD_BASE' || "$COMPONENT" == 'LAUNCHER_BASE' ) ]]; then
    SETTINGS="$(find "$WORK_DIR/extracted" -maxdepth 5 -type f \( -name settings.gradle -o -name settings.gradle.kts \) -print | sort | head -n 1 || true)"
    [[ -n "$SETTINGS" ]] || { echo 'No Gradle settings file was found.' >&2; exit 65; }
    PROJECT_ROOT="$(dirname "$SETTINGS")"
    (
      cd "$PROJECT_ROOT"
      gradle wrapper --gradle-version 8.6 --distribution-type bin
    )
    WRAPPER="$PROJECT_ROOT/gradlew"
    BUILD_MODE='generated-gradle-wrapper'
  fi

  [[ -n "$WRAPPER" ]] || { echo 'No Gradle wrapper was found in the source archive.' >&2; exit 65; }
  PROJECT_ROOT="$(dirname "$WRAPPER")"
  [[ -f "$PROJECT_ROOT/settings.gradle" || -f "$PROJECT_ROOT/settings.gradle.kts" ]] || {
    echo "Gradle wrapper has no adjacent settings file: $PROJECT_ROOT" >&2
    exit 65
  }
  chmod +x "$WRAPPER"
fi

while IFS= read -r property_file; do
  [[ -n "$property_file" ]] || continue
  sed -i.bak '/^android\.aapt2FromMavenOverride=/d' "$property_file"
done < <(grep -RIl '^android\.aapt2FromMavenOverride=' "$PROJECT_ROOT" 2>/dev/null || true)

if [[ "$COMPONENT" == 'CORE_BASE' && "$CANONICAL_STEM" == *'CORE_REDUCE_003'* ]]; then
  [[ -d "$PROJECT_ROOT/app" ]] || { echo 'CORE_REDUCE_003 invariant failed: app missing.' >&2; exit 65; }
  [[ -d "$PROJECT_ROOT/core" ]] || { echo 'CORE_REDUCE_003 invariant failed: core missing.' >&2; exit 65; }
  [[ ! -e "$PROJECT_ROOT/featurehome" ]] || { echo 'CORE_REDUCE_003 invariant failed: featurehome present.' >&2; exit 65; }
  [[ ! -e "$PROJECT_ROOT/designsystem" ]] || { echo 'CORE_REDUCE_003 invariant failed: designsystem present.' >&2; exit 65; }
  if grep -RInE 'HomeScreen|FeatureRepository|CoreFeature' "$PROJECT_ROOT" --exclude-dir=.gradle --exclude-dir=build; then
    echo 'CORE_REDUCE_003 invariant failed: removed demonstration symbol found.' >&2
    exit 65
  fi
fi

BUILD_LOG="$ARTIFACT_DIR/BUILD_LOG.txt"
if [[ "$COMPONENT" == 'CLIENT' ]]; then
  (
    cd "$PROJECT_ROOT"
    export JAVA_HOME="${JAVA_HOME_17_X64:-${JAVA_HOME:-}}"
    [[ -n "${JAVA_HOME:-}" ]] && export PATH="$JAVA_HOME/bin:$PATH"
    bash scripts/install_android_sdk_and_build.sh
  ) 2>&1 | tee "$BUILD_LOG"
else
  (
    cd "$PROJECT_ROOT"
    ./gradlew --no-daemon --stacktrace clean "$GRADLE_TASK"
  ) 2>&1 | tee "$BUILD_LOG"
fi

mapfile -t APKS < <(
  {
    find "$PROJECT_ROOT" -type f -path '*/build/outputs/apk/*' -name '*.apk' -print 2>/dev/null || true
    find "$PROJECT_ROOT/APK_DOWNLOAD" -maxdepth 2 -type f -name '*.apk' -print 2>/dev/null || true
  } | sort -u
)

[[ "${#APKS[@]}" -gt 0 ]] || {
  echo 'Build completed without a discoverable APK.' >&2
  find "$PROJECT_ROOT" -type f \( -name '*.apk' -o -name '*.aab' \) -print >&2 || true
  exit 65
}

INDEX=0
for APK in "${APKS[@]}"; do
  INDEX=$((INDEX + 1))
  if [[ "${#APKS[@]}" -eq 1 ]]; then
    DEST="$ARTIFACT_DIR/${CANONICAL_STEM}_${VARIANT^^}.apk"
  else
    ORIGINAL="$(basename "$APK")"
    DEST="$ARTIFACT_DIR/${CANONICAL_STEM}_${VARIANT^^}_${INDEX}_${ORIGINAL}"
  fi
  cp "$APK" "$DEST"
  sha256sum "$DEST" > "$DEST.sha256"
done

SOURCE_SHA="$(sha256sum "$SOURCE_ZIP" | awk '{print $1}')"
PROVENANCE="$ARTIFACT_DIR/BUILD_PROVENANCE_REPORT.md"
{
  echo '# SWRLZ Unified APK Router Build Provenance'
  echo
  echo '- Status: succeeded'
  echo "- Component: $COMPONENT"
  echo "- Canonical source identity: $CANONICAL_STEM"
  echo "- Selected source path: $SOURCE_ZIP"
  echo "- Selected source SHA-256: $SOURCE_SHA"
  echo "- Build mode: $BUILD_MODE"
  echo "- Build variant: $VARIANT"
  echo "- Gradle task: $GRADLE_TASK"
  echo "- Project root: $PROJECT_ROOT"
  echo "- Repository: ${GITHUB_REPOSITORY:-local}"
  echo "- Source commit: ${GITHUB_SHA:-local}"
  echo "- Workflow: ${GITHUB_WORKFLOW:-local}"
  echo "- Workflow run: ${GITHUB_RUN_ID:-local}"
  echo "- Generated UTC: $(date -u +'%Y-%m-%dT%H:%M:%SZ')"
  echo
  echo '## APK files'
  for built in "$ARTIFACT_DIR"/*.apk; do
    echo "- $(basename "$built") — $(stat -c '%s' "$built") bytes — $(sha256sum "$built" | awk '{print $1}')"
  done
} > "$PROVENANCE"
sha256sum "$PROVENANCE" > "$PROVENANCE.sha256"

BUNDLE_NAME="${CANONICAL_STEM}_${VARIANT^^}_APK_DOWNLOAD.zip"
BUNDLE_TEMP="$(dirname "$ARTIFACT_DIR")/$BUNDLE_NAME"
rm -f "$BUNDLE_TEMP"
(
  cd "$ARTIFACT_DIR"
  zip -qr "$BUNDLE_TEMP" .
)
mv "$BUNDLE_TEMP" "$ARTIFACT_DIR/$BUNDLE_NAME"
sha256sum "$ARTIFACT_DIR/$BUNDLE_NAME" > "$ARTIFACT_DIR/$BUNDLE_NAME.sha256"

if [[ -n "${GITHUB_OUTPUT:-}" ]]; then
  {
    echo "project_root=$PROJECT_ROOT"
    echo "artifact_dir=$ARTIFACT_DIR"
    echo "bundle=$ARTIFACT_DIR/$BUNDLE_NAME"
    echo "build_mode=$BUILD_MODE"
    echo "gradle_task=$GRADLE_TASK"
  } >> "$GITHUB_OUTPUT"
fi

find "$ARTIFACT_DIR" -maxdepth 1 -type f -printf '%f\n' | sort
