# OMEGA P1 Test Matrix

## Automated checks

| Area | Check | Expected |
|---|---|---|
| Session model | `OmegaSessionState` transitions | ACTIVE/BACKGROUND/EXITED are explicit and deterministic |
| Resources | XML resource compilation | No duplicate or unresolved OMEGA resources |
| Layout | Session-row inflation | Title/state views exist and minimum touch surface is present |
| Layout | `activity_termux.xml` inflation | Critical drawer, terminal, toolbar, keyboard and new-session views exist |
| Insets | `OmegaWindowInsets.resolveBottomInset()` | Largest system-bar/IME bottom inset is selected |
| Extra keys | OMEGA visual policy | Text, active text, background and active background use semantic OMEGA colors |
| Accessibility | Session content description | State is available without relying on color alone |
| Regression | Session services | No changes to service/session contracts |

## Implemented UI test suite

`app/src/test/java/com/termux/app/OmegaP1UiTest.java`

Coverage includes session state semantics, inset resolution, session-row layout and critical terminal activity surfaces using the repository's existing Robolectric test stack.

## Manual QA

### Session drawer
- Open drawer from the terminal.
- Confirm `ACTIVE`, `BACKGROUND` and `EXITED` states are visually and textually distinct.
- Create a new session.
- Select an existing session.
- Rename a session.
- Close/kill a session.
- Re-open drawer after rotation/recreation.

### Toolbar and extra keys
- Toggle terminal toolbar.
- Confirm toolbar remains visible and readable against the terminal.
- Press primary extra keys repeatedly.
- Confirm active/inactive key states remain distinguishable.
- Verify configured extra-key definitions continue to load from `termux.properties`.
- Verify hardware keyboard and soft keyboard interaction.

### Keyboard / insets
- Test 3-button navigation and gesture navigation.
- Open and dismiss the soft keyboard repeatedly.
- Toggle full-screen mode where supported.
- Verify the terminal is not covered by the bottom controls.
- Verify the transparent OMEGA inset spacer expands with IME/system-bar insets.

### Dialogs
- Open URL/select-text dialogs.
- Open session rename/new-session dialogs.
- Confirm primary actions have visible focus/pressed states.
- Verify text is readable with increased system font size.

## Device matrix

- Android 8/9 legacy navigation path where supported.
- Android 10+ gesture/3-button navigation.
- Small phone viewport.
- Large phone viewport.
- Tablet viewport.
- Light/system mode and dark mode.
- Hardware keyboard present/absent.

## Regression gates

A P1 change is not considered release-ready when it introduces terminal rendering regressions, broken session selection, broken keyboard behavior, unresolved resources, plugin/session lifecycle failures or an unverified device-specific inset regression.
