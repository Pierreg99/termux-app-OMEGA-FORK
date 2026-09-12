# OMEGA Changelog

## Unreleased — P2 OMEGA Command Center

### Added
- P2 Command Center architecture and implementation specification.
- Command categories for sessions, terminal actions, navigation, editing and diagnostics.
- Keyboard-first and accessibility requirements for the future command palette.

### Status
- Command Center implementation is initialized and queued after P1 completion.

## P1 Terminal UX — Completed baseline

### Added
- Explicit `ACTIVE`, `BACKGROUND` and `EXITED` semantic session states.
- OMEGA inset spacer and centralized bottom inset policy for system bars and IME.
- OMEGA extra-key visual policy using semantic text/background/active-state colors.
- P1 Robolectric UI/resource coverage.
- Interface references, test matrix and QA documentation.

### Changed
- Session drawer rows now expose state as accessible text rather than relying only on color.
- Extra-key controls inherit OMEGA semantic colors while retaining the existing `termux.properties` configuration model.
- Bottom safe-area handling is isolated from terminal rendering through a transparent spacer view.

### Compatibility
- Terminal renderer and session service contracts remain unchanged.
- Existing plugin integration points remain unchanged.
- Existing extra-key property configuration remains authoritative.

## P0 Design System v1

- Added OMEGA semantic color tokens.
- Added OMEGA spacing, shape and touch-target tokens.
- Established dark-first OMEGA theme resources.
