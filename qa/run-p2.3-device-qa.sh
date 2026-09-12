#!/usr/bin/env bash
set -euo pipefail

APK_PATH="${1:-}"
OUT_DIR="${2:-qa/screenshots/p2.3}"
PACKAGE="com.termux"

fail() {
  echo "ERROR: $1" >&2
  exit 1
}

command -v adb >/dev/null 2>&1 || fail "adb is required"
[[ -n "$APK_PATH" ]] || fail "usage: $0 <debug-apk> [output-dir]"
[[ -f "$APK_PATH" ]] || fail "APK not found: $APK_PATH"

mkdir -p "$OUT_DIR"

echo "== OMEGA P2.3 device QA =="
echo "APK: $APK_PATH"
echo "Output: $OUT_DIR"

echo
adb start-server >/dev/null
adb wait-for-device

DEVICE_COUNT="$(adb devices | awk 'NR>1 && $2=="device" {count++} END {print count+0}')"
[[ "$DEVICE_COUNT" == "1" ]] || fail "expected exactly one online adb device; found $DEVICE_COUNT"

SERIAL="$(adb devices | awk 'NR>1 && $2=="device" {print $1; exit}')"
echo "Device: $SERIAL"

adb install -r "$APK_PATH"
adb shell am force-stop "$PACKAGE"
adb shell monkey -p "$PACKAGE" 1 >/dev/null
sleep 2

capture() {
  local name="$1"
  echo "capture: $name"
  adb exec-out screencap -p > "$OUT_DIR/$name"
}

capture "command-center-portrait.png"

adb shell settings put system accelerometer_rotation 0 || true
adb shell settings put system user_rotation 1 || true
sleep 1
capture "command-center-landscape.png"

adb shell settings put system user_rotation 0 || true
sleep 1

cat > "$OUT_DIR/DEVICE.txt" <<EOF
Serial: $SERIAL
Android: $(adb shell getprop ro.build.version.release | tr -d '\r')
API: $(adb shell getprop ro.build.version.sdk | tr -d '\r')
Density: $(adb shell wm density | tr -d '\r' | head -n 1)
Model: $(adb shell getprop ro.product.model | tr -d '\r')
CapturedAtUTC: $(date -u +%Y-%m-%dT%H:%M:%SZ)
EOF

echo "Initial screenshot evidence captured."
echo "Complete TalkBack, hardware-keyboard and functional matrix manually before marking P2.3 passed."
