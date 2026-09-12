# OMEGA Command Center — P2

## Status

**P2.1 implemented — palette foundation and UI launcher**

P2 adds an optional command surface above the terminal without replacing shell input or the existing Termux session engine.

## Product goal

Create one fast, keyboard-accessible control layer for the actions users perform around the terminal:

- session navigation and creation
- keyboard/toolbar controls
- settings and help
- terminal display actions
- future automation and power-user commands

## Interaction model

The Command Center is optional and dismissible. The terminal remains the primary workspace.

### Entry points

1. Dedicated `COMMANDS` action in the session drawer.
2. Future toolbar action when the toolbar integration is expanded.
3. Future hardware-key shortcut for direct palette opening.
4. Future app shortcut/deep-link entry points where compatible.

### Command categories

| Category | Examples |
|---|---|
| Sessions | New session, next, previous, select session |
| Terminal | Reset, clear, toggle keyboard, keep screen on |
| Navigation | Open drawer, settings, help |
| Editing | Copy selection, paste, select URL |
| Diagnostics | Report issue, debug information |

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
- action adapter for session, terminal, navigation, URL selection and diagnostics commands
- accessible launcher content description
- OMEGA-styled palette surface

The `editing.paste` command remains intentionally reserved for the terminal-view adapter until the existing clipboard path is exposed through a stable public API; it is not implemented through a parallel paste mechanism.

## P2.2 — Session actions

- [x] New session.
- [x] Next/previous session through the existing session list.
- [ ] Direct session selection.
- [ ] Rename session.
- [ ] Close/kill session with confirmation where required.

## P2.3 — Terminal actions

- [x] Keyboard toggle.
- [x] Toolbar toggle.
- [x] Reset terminal.
- [ ] Keep screen on.
- [x] Settings/help.

## P2.4 — Validation

- [x] JVM/Robolectric command-state and registry coverage.
- [x] Command palette launcher UI coverage.
- [ ] Accessibility navigation checks on device.
- [ ] Hardware-keyboard smoke test on device.
- [ ] Device screenshot evidence.

## Security and compatibility

The Command Center does not create a new shell or plugin command execution pathway. It only dispatches to existing trusted activity/session APIs. Plugin intent contracts remain unchanged.

## Acceptance criteria

- Command search never replaces or corrupts shell input.
- Selection is deterministic and wraps around the filtered command list.
- Existing session/service lifecycle remains authoritative.
- Command Center can remain unopened without affecting terminal startup.
- Palette navigation works without pointer/touch input once the palette is opened.

## Next autonomous stage

**P2.2 Session/Terminal Actions → P2.3 accessibility/device validation → screenshot evidence → hardware keyboard QA.**

## Roadmap position

**P0 Design System → P1 Terminal UX → P2 OMEGA Command Center → P3 Power User Features**
