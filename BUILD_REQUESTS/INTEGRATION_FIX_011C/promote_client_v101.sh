#!/usr/bin/env bash
set -euo pipefail

MINIMUM_BASE_SHA="ceb3660a46e85f069c3e47dc9dc7cb091e9deaee"
REQUEST_DIR="BUILD_REQUESTS/INTEGRATION_FIX_011C"
BASE_ZIP="SOURCES/CLIENT/CLIENT_CFv1.0.0_SWRLZ.zip"
OUTPUT_ZIP="CLIENT_CFv1.0.1_SWRLZ.zip"
EXPECTED_SOURCE_SHA="9f567523ad184bfc14751d1aeaf527233a41a8e2a3e82378a68292a2e6a922f7"
SOURCE_PATH="SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.zip"
CHECKSUM_PATH="SOURCES/CLIENT/CLIENT_CFv1.0.1_SWRLZ.sha256"
CONTRACT_PATH="docs/contracts/CLIENT_VERIFIED_ADMIN_ROUTE_POLICY_V1.md"
ROADMAP_PATH="docs/roadmap/SWRLZ_CORE_2.7.6_NETWORK_DISCOVERY.md"
REPORT_PATH="reports/RPT_CF8_276_ADMIN_FALLBACK.md"
TEMP="${RUNNER_TEMP:?}/swrlz-client-v101-recovery"

cat "$REQUEST_DIR"/patch/patch-*.txt > "$RUNNER_TEMP/client-v101.patch"
echo "63720b4bc213481b9f61346561ab26d017c75b9349b5b4e5e5ba9b844badcc6f  $RUNNER_TEMP/client-v101.patch" | sha256sum -c -
echo "90f296e25ead1d0b7a0fb674e37705792ef964dc626c67c9b0deb269f253ac18  $REQUEST_DIR/repack_client_v101.py" | sha256sum -c -
echo "3384372546964a843b2b488f3fc7da3f9be2837b96c1647dfe2f659dfc0cb7ca  $REQUEST_DIR/payload/CLIENT_VERIFIED_ADMIN_ROUTE_POLICY_V1.md" | sha256sum -c -
echo "4a4c77463a74d1090a175e9a08ae6b6480b202fb8131c3b7c18177a46c6174e6  $REQUEST_DIR/payload/SWRLZ_CORE_2.7.6_NETWORK_DISCOVERY.md" | sha256sum -c -
echo "ecc53cc5c4be6642f80d0cd76370889996448b15004dd7d643e4f64a88226ab3  $REQUEST_DIR/payload/RPT_CF8_276_ADMIN_FALLBACK.md" | sha256sum -c -

rm -rf "$TEMP"
mkdir -p "$TEMP"
python3 "$REQUEST_DIR/repack_client_v101.py" --base "$BASE_ZIP" --patch "$RUNNER_TEMP/client-v101.patch" --output "$TEMP/$OUTPUT_ZIP"
test "$(sha256sum "$TEMP/$OUTPUT_ZIP" | awk '{print $1}')" = "$EXPECTED_SOURCE_SHA"
unzip -tq "$TEMP/$OUTPUT_ZIP"
cp "$REQUEST_DIR/payload/CLIENT_CFv1.0.1_SWRLZ.sha256" "$TEMP/CLIENT_CFv1.0.1_SWRLZ.sha256"
test "$(awk 'NF {print $1; exit}' "$TEMP/CLIENT_CFv1.0.1_SWRLZ.sha256")" = "$EXPECTED_SOURCE_SHA"

printf '%s\n' "$CHECKSUM_PATH" "$CONTRACT_PATH" "$REPORT_PATH" "$ROADMAP_PATH" "$SOURCE_PATH" | sort > "$RUNNER_TEMP/expected-paths.txt"
git config user.name "github-actions[bot]"
git config user.email "41898282+github-actions[bot]@users.noreply.github.com"

PROMOTED_SHA=""
for attempt in 1 2 3; do
  git fetch origin main
  CURRENT_MAIN="$(git rev-parse origin/main)"
  git merge-base --is-ancestor "$MINIMUM_BASE_SHA" "$CURRENT_MAIN"
  CHANGED_TARGETS="$(git diff --name-only "$MINIMUM_BASE_SHA..$CURRENT_MAIN" -- "$SOURCE_PATH" "$CHECKSUM_PATH" "$CONTRACT_PATH" "$ROADMAP_PATH" "$REPORT_PATH")"
  test -z "$CHANGED_TARGETS" || { echo "Authorized target path changed; refusing overwrite:" >&2; echo "$CHANGED_TARGETS" >&2; exit 1; }

  git checkout -B recovery-main "$CURRENT_MAIN"
  mkdir -p "$(dirname "$SOURCE_PATH")" "$(dirname "$CONTRACT_PATH")" "$(dirname "$ROADMAP_PATH")" "$(dirname "$REPORT_PATH")"
  cp "$TEMP/$OUTPUT_ZIP" "$SOURCE_PATH"
  cp "$TEMP/CLIENT_CFv1.0.1_SWRLZ.sha256" "$CHECKSUM_PATH"
  cp "$REQUEST_DIR/payload/CLIENT_VERIFIED_ADMIN_ROUTE_POLICY_V1.md" "$CONTRACT_PATH"
  cp "$REQUEST_DIR/payload/SWRLZ_CORE_2.7.6_NETWORK_DISCOVERY.md" "$ROADMAP_PATH"
  cp "$REQUEST_DIR/payload/RPT_CF8_276_ADMIN_FALLBACK.md" "$REPORT_PATH"
  git add -- "$SOURCE_PATH" "$CHECKSUM_PATH" "$CONTRACT_PATH" "$ROADMAP_PATH" "$REPORT_PATH"
  git diff --cached --name-only | sort > "$RUNNER_TEMP/actual-paths.txt"
  diff -u "$RUNNER_TEMP/expected-paths.txt" "$RUNNER_TEMP/actual-paths.txt"
  test "$(sha256sum "$SOURCE_PATH" | awk '{print $1}')" = "$EXPECTED_SOURCE_SHA"
  unzip -tq "$SOURCE_PATH"
  git commit -m "client: promote v1.0.1 verified admin fallback"
  if git push origin HEAD:main; then
    PROMOTED_SHA="$(git rev-parse HEAD)"
    break
  fi
  sleep 4
done

test -n "$PROMOTED_SHA"
echo "PROMOTED_SHA=$PROMOTED_SHA" >> "$GITHUB_ENV"
echo "promoted_sha=$PROMOTED_SHA" >> "$GITHUB_OUTPUT"
