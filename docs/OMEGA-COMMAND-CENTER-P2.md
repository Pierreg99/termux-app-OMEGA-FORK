# OMEGA Command Center — P2

## Status

**Initialized — implementation specification**

P2 adds an optional command surface above the terminal without replacing shell input or the existing Termux session engine.

## Product goal

Create one fast, keyboard-accessible control layer for the actions users perform around the terminal:

- session navigation and creation
- keyboard/toolbar controls
- settings and help
- terminal display actions
- future automation and power-user commands

## Interaction model

The Command Center must be optional and dismissible. The terminal remains the primary workspace.

### Entry points

1. Existing session drawer action.
2. Dedicated toolbar action when the toolbar is visible.
3. Hardware keyboard command shortcut.
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
OmegaCommandCenter
├── CommandRegistry
├── CommandState
├── CommandSearch
├── CommandActionAdapter
└── CommandCenterSurface
```

`CommandActionAdapter` should call existing Termux activity/session APIs rather than duplicate session lifecycle logic.

## Design constraints

- Reuse OMEGA v1 semantic tokens.
- Preserve terminal readability and input latency.
- Do not intercept ordinary shell keystrokes.
- Support hardware keyboard navigation and accessibility focus.
- Avoid mandatory animations or visual effects.
- Keep the feature isolated so it can be removed or disabled without affecting terminal sessions.

## Implementation sequence

### P2.1 — Command model

- [ ] Add immutable command identifiers.
- [ ] Add category metadata and searchable labels.
- [ ] Add enabled/disabled state resolution.
- [ ] Add execution adapter to existing Termux APIs.

### P2.2 — Command palette

- [ ] Add searchable command surface.
- [ ] Add keyboard-first navigation.
- [ ] Add accessible content descriptions and state announcements.
- [ ] Add empty/no-result state.

### P2.3 — Session actions

- [ ] New session.
- [ ] Next/previous session.
- [ ] Direct session selection.
- [ ] Rename session.
- [ ] Close/kill session with confirmation where required.

### P2.4 — Terminal actions

- [ ] Keyboard toggle.
- [ ] Toolbar toggle.
- [ ] Reset terminal.
- [ ] Keep screen on.
- [ ] Settings/help.

### P2.5 — Validation

- [ ] JVM/Robolectric command-registry tests.
- [ ] UI resource tests.
- [ ] Accessibility navigation checks.
- [ ] Hardware-keyboard smoke test.
- [ ] Device screenshot evidence.

## Security and compatibility

The Command Center must not create a new command execution pathway. It only invokes existing trusted app/session actions. Plugin intent contracts remain unchanged.

## P2 acceptance criteria

- Command search never replaces or corrupts shell input.
- Every command has a deterministic enabled/disabled state.
- Existing session/service lifecycle remains authoritative.
- Command Center can be disabled without breaking terminal startup.
- Keyboard and accessibility navigation work without pointer/touch input.

## Roadmap position

**P0 Design System → P1 Terminal UX → P2 OMEGA Command Center → P3 Power User Features**
