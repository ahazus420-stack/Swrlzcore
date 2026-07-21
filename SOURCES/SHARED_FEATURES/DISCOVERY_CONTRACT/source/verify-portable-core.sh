#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
BUILD_DIR="$ROOT/build/manual-verification"
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"
MAIN_FILES=$(find "$ROOT/src/main/kotlin" -name '*.kt' \
  ! -name 'KotlinxSerializationJsonBackend.kt' \
  ! -name 'DiscoveryContractCodecs.kt' | sort | tr '\n' ' ')
TEST_FILES=$(find "$ROOT/src/test/kotlin" -name '*.kt' | sort | tr '\n' ' ')
kotlinc $MAIN_FILES $TEST_FILES -include-runtime -d "$BUILD_DIR/discovery-contract-tests.jar"
java -cp "$BUILD_DIR/discovery-contract-tests.jar" swrlz.discovery.tests.DiscoveryContractTestRunner
