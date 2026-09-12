# OMEGA Android QA — P2.3

## Scope

This runbook is the device-level validation layer for the OMEGA Command Center and P2.2 session/terminal actions.

## Preconditions

- Android device or emulator connected through `adb`.
- Debug APK built from the same `master` revision under test.
- USB debugging enabled for a physical device.
- Optional hardware keyboard available for keyboard-path validation.
- TalkBack available for accessibility traversal.

## Build

```bash
./gradlew :app:assembleDebug
```

Verify the generated APK path before installing; do not assume a fixed output filename when ABI/version configuration changes it.

## Install and launch

```bash
adb devices
adb install -r <debug-apk>
adb shell am force-stop com.termux
adb shell monkey -p com.termux 1
```

## P2.2 functional matrix

| Area | Action | Expected result |
|---|---|---|
| Palette | Open `COMMANDS` | Palette opens and focus lands in search |
| Search | Type `session` | Session commands filter without touching shell input |
| Keyboard nav | Up / Down | Selection wraps deterministically |
| Execute | Enter | Selected command executes |
| Sessions | New session | New session is created and selected |
| Sessions | Select session N | Existing session N becomes current |
| Sessions | Rename current | Existing rename dialog opens; title updates |
| Sessions | Kill current | Confirmation appears; `Yes` terminates the process |
| Terminal | Toggle keyboard | Existing keyboard control is invoked |
| Terminal | Toggle toolbar | Toolbar visibility changes |
| Terminal | Keep screen on | Window keep-screen-on flag toggles |
| Terminal | Reset | Current emulator resets through existing reset path |
| Editing | Paste | Clipboard content reaches the current terminal |
| Navigation | Settings / Help | Existing activities open |

## Accessibility checks

1. Enable TalkBack.
2. Open the Command Center from the drawer.
3. Verify the title is announced as `OMEGA Command Center`.
4. Verify the search field announces `Search OMEGA commands`.
5. Traverse command rows with accessibility focus; each row must announce its action label.
6. Confirm the selected row is visually distinct and focus remains inside the palette while navigating.
7. Confirm the palette can be dismissed with `Esc` on a hardware keyboard or with the device back action.

## Hardware-keyboard checks

The existing terminal hardware-shortcut path already handles session switching and other Ctrl+Alt actions. P2.1 currently requires the palette to be opened through its launcher before keyboard navigation begins.

Validate:

- physical Up/Down moves palette selection
- Enter executes the selected action
- Esc closes the palette
- typing into the search field filters commands
- terminal shell input is unchanged when the palette is closed

## Screenshot evidence

Capture at minimum:

- portrait: Command Center open with unfiltered commands
- portrait: `session` search results
- landscape: Command Center open
- session list with renamed session
- confirmation dialog for session termination
- keep-screen-on state before/after toggle

Example capture command:

```bash
mkdir -p qa/screenshots/p2.3
adb exec-out screencap -p > qa/screenshots/p2.3/command-center-portrait.png
```

Use one capture per acceptance state and review at native device resolution. Screenshots are evidence only; do not use them as a substitute for functional testing.

## Recording results

Record the Android model/emulator profile, Android version, screen density, hardware keyboard state, TalkBack state, tested commit SHA, and any failures in the QA report before marking P2.3 complete.

## Current status

P2.2 source implementation is complete. Real device validation and screenshot capture require an Android runtime/device and are intentionally not marked passed until evidence exists.
