# OMEGA P1 Test Matrix

## Automated checks

| Area | Check | Expected |
|---|---|---|
| Resources | XML resource compilation | No duplicate or unresolved OMEGA resources |
| Layout | `activity_termux.xml` inflation | Layout loads without missing IDs/resources |
| Theme | Day/Night resource resolution | No missing color/style references |
| Accessibility | Touch target audit | Interactive drawer actions meet the OMEGA target token |
| Regression | Session services | No changes to service/session contracts |

## Manual QA

### Session drawer
- Open drawer from the terminal.
- Confirm active session is visually identifiable.
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
- Verify hardware keyboard and soft keyboard interaction.

### Keyboard / insets
- Test 3-button navigation and gesture navigation.
- Open and dismiss the soft keyboard repeatedly.
- Toggle full-screen mode where supported.
- Verify the terminal is not covered by the bottom controls.

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

A P1 change is not considered complete when it introduces terminal rendering regressions, broken session selection, broken keyboard behavior, unresolved resources, or plugin/session lifecycle failures.
