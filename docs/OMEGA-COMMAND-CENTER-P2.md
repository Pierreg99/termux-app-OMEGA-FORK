# OMEGA Command Center — P2

## Status

**P2.2 implemented — complete session and terminal action layer**

P2 adds an optional command surface above the terminal without replacing shell input or the existing Termux session engine.

## Product goal

Create one fast, keyboard-accessible control layer for the actions users perform around the terminal:

- session navigation, selection, creation and naming
- session termination with explicit confirmation
- keyboard/toolbar controls
- screen-on and terminal reset controls
- settings, help, URL selection, paste and diagnostics

## Interaction model

The Command Center is optional and dismissible. The terminal remains the primary workspace.

### Entry points

1. Dedicated `COMMANDS` action in the session drawer.
2. Future toolbar action when toolbar integration is expanded.
3. Future global hardware-key shortcut for direct palette opening.
4. Future app shortcut/deep-link entry points where compatible.

### Command categories

| Category | Examples |
|---|---|
| Sessions | New, next, previous, select, rename, kill |
| Terminal | Keyboard, toolbar, keep screen on, reset |
| Navigation | Drawer, settings, help |
| Editing | Paste, select URL |
| Diagnostics | Report issue |

## Architecture

```text
OmegaCommandRegistry
├── OmegaCommand
├── OmegaCommandState
├── OmegaCommandPalette
├── OmegaCommandActionAdapter
└── OmegaCommandPaletteButton
```

`OmegaCommandActionAdapter` invokes existing Termux activity/session APIs instead of duplicating session lifecycle logic. The command palette owns only filtering, selection, focus and execution dispatch.

## P2.1 — Command palette

Implemented:

- immutable command metadata and categories
- query state with selection reset
- case-insensitive searchable filtering
- keyboard-first Up/Down/Enter/Esc handling
- click-to-execute rows
- selected-row scrolling/focus
- accessible title, search field, results and command rows
- OMEGA-styled palette surface

## P2.2 — Session and terminal actions

Implemented:

- [x] New session through `TermuxTerminalSessionActivityClient`.
- [x] Next/previous session through the existing session client.
- [x] Direct selection for currently available sessions, capped at the existing eight-session UI limit.
- [x] Rename current session through the existing rename dialog and session client.
- [x] Kill current session with the existing SIGKILL path and an explicit confirmation dialog.
- [x] Toggle soft keyboard.
- [x] Toggle terminal toolbar.
- [x] Toggle keep-screen-on through the activity window flag.
- [x] Reset current terminal through the existing session reset path.
- [x] Settings/help navigation.
- [x] Clipboard paste through the existing terminal session client clipboard path.
- [x] URL selection and issue reporting.

## P2.3 — Validation

Source-level and JVM coverage:

- [x] Command state wrap/reset.
- [x] Core registry coverage.
- [x] Dynamic session-selector coverage and eight-session cap.
- [x] Accessible command-launcher coverage.
- [x] Static review against existing session/activity APIs.

Device validation remains a separate evidence stage:

- [ ] Accessibility navigation on a real Android device.
- [ ] Hardware-keyboard smoke test on a real device.
- [ ] Real Android screenshot capture and visual review.
- [ ] Screenshot evidence committed to the repository.

## Security and compatibility

The Command Center does not create a new shell or plugin command execution pathway. Session creation, switching, renaming and termination are delegated to existing Termux activity/service/session APIs. Plugin intent contracts remain unchanged.

## Acceptance criteria

- Command search never replaces or corrupts shell input.
- Selection is deterministic and wraps around the filtered command list.
- Session selectors only appear for currently available sessions.
- Destructive session termination always presents an explicit confirmation.
- Existing session/service lifecycle remains authoritative.
- Command Center can remain unopened without affecting terminal startup.
- Palette navigation works without pointer/touch input once the palette is opened.
- Accessibility labels are exposed for the palette launcher and command rows.

## QA evidence plan

The remaining validation must be executed on an Android runtime rather than inferred from JVM tests. The evidence set should cover portrait and landscape, software and hardware keyboard input, TalkBack/focus traversal, session creation/selection/rename/kill, keep-screen-on, paste, reset, and screenshot capture.

## Next autonomous stage

**P2.3 Accessibility + hardware-keyboard device QA → real Android screenshots → visual regression evidence → P3 Power User Features.**

## Roadmap position

**P0 Design System → P1 Terminal UX → P2 OMEGA Command Center → P3 Power User Features**
