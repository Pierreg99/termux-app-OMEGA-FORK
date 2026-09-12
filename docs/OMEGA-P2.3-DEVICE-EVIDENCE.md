# OMEGA P2.3 — Device Evidence Gate

This document defines the release evidence required beyond source/JVM coverage.

## Automated emulator evidence

The workflow `.github/workflows/p2-3-android-emulator-qa.yml` is responsible for:

- building the debug APK
- running `testDebugUnitTest`
- installing and launching the APK on an API 35 emulator
- opening the drawer and locating the `COMMANDS` launcher
- asserting the command-center title and search field in the UI tree
- capturing command-center portrait, filtered-search and landscape screenshots
- capturing UI-tree XML dumps
- writing `DEVICE.txt` and `EVIDENCE.txt`

A successful run must publish the evidence artifact `omega-p2.3-android-evidence`.

## Manual accessibility gate

Before release, validate on a physical Android device:

1. Enable TalkBack.
2. Open the session drawer and enter OMEGA Command Center.
3. Confirm logical traversal order: title → search → command results → footer.
4. Confirm selected command state is conveyed without relying on color alone.
5. Filter for `session` and verify focused results remain actionable.
6. Execute a non-destructive command with TalkBack and confirm the resulting state.

Record device model, Android version, API level and test date in the release evidence.

## Manual hardware-keyboard gate

With a physical keyboard connected:

1. Open the command palette.
2. Verify Up/Down changes selection with visible focus.
3. Verify Enter/DPAD Center executes the selected command.
4. Verify Escape closes the palette.
5. Verify no tested OMEGA shortcut shadows an existing Termux default shortcut.

## Current status

Source and JVM coverage are implemented. Automated emulator evidence has been strengthened, but the current GitHub Actions P2.3 runs are ending with workflow-level `failure` and zero reported jobs, so they are not accepted as runtime evidence until a run executes successfully. Physical TalkBack and hardware-keyboard evidence remains outstanding.
