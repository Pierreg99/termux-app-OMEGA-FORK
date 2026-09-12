# OMEGA Power User Features — P3

## Objective

P3 extends the OMEGA Command Center from a fixed action surface into a controlled power-user layer while preserving Termux compatibility and keeping advanced behavior opt-in.

## Design principles

- Existing Termux defaults remain authoritative unless the user explicitly opts into an OMEGA override.
- Configuration is reversible and exportable.
- Session-specific settings must not silently mutate global settings.
- Destructive operations require explicit confirmation.
- UI state must remain keyboard- and accessibility-friendly.
- Upstream synchronization should be possible without carrying opaque OMEGA state into core Termux components.

## P3.1 — Session Profiles

### Goal

Give each terminal session an optional named profile containing presentation and interaction preferences.

### Initial profile fields

- profile id and display name
- font size
- color/theme preset
- cursor style and blink policy
- scrollback limit
- terminal margin preset
- extra-key preset reference
- keep-screen-on preference

### Constraints

Profiles are metadata/configuration only. They do not replace `TerminalSession` or the session client lifecycle.

**Status: implemented as an isolated model/store layer.**

## P3.2 — Theme & Profile Presets

### Implemented

- versioned JSON export/import via `OmegaSessionProfileCodec`
- deterministic profile ordering by profile id
- validation before persistence
- unsupported schema versions rejected
- malformed imports rejected without partial writes
- non-destructive merge import by default
- explicit replace import mode
- theme preset identifiers travel with each exported profile
- missing profiles fall back to a default using the requested profile id

The codec does not directly touch terminal state. Import is therefore safe to validate before a later UI layer applies a profile to a session.

**Status: implemented at storage/serialization layer.**

### Next UI layer

The remaining P3.2 UI work is deliberately separate: preset picker, import/export entry points, confirmation for replace-mode import, and reset-to-default affordance.

## P3.3 — Command Center Power Layer

- favorite/pinned commands
- custom display ordering
- category filters
- recent-command section
- configurable OMEGA-only shortcuts
- conflict detection against existing Termux shortcuts

The existing immutable command registry remains the source catalog. User preferences should decorate the catalog rather than mutate command definitions.

## P3.4 — Explicit Session Restore

- opt-in restore after process/activity recreation
- bounded session count
- recoverable metadata only
- clear failure state when a session cannot be recreated
- no silent restore after a user-initiated session kill

## P3.5 — Advanced Terminal Controls

- font, cursor and scrollback controls
- opacity/transparency presets
- terminal margin presets
- extra-key preset selection
- accessibility-aware minimum text sizing

## Suggested architecture

```text
OMEGA Preferences
        │
        ├── SessionProfileStore
        ├── ThemePresetStore
        ├── CommandPreferenceStore
        └── SessionRestoreStore
                 │
                 ▼
        OMEGA UI / Command Center
                 │
                 ▼
       Existing Termux clients/APIs
```

Storage interfaces should remain small so later persistence changes do not leak into the terminal engine.

## Validation strategy

### JVM

- profile serialization round-trip
- invalid-import rejection
- unsupported-version rejection
- deterministic export ordering
- store merge and replace semantics
- missing-profile fallback stability
- shortcut conflict resolution
- deterministic command ordering
- restore-state eligibility

### Android runtime

- profile switching with an active session
- configuration persistence after recreation
- keyboard navigation through power-user controls
- TalkBack traversal
- portrait/landscape visual checks

## Rollout order

1. P3.1 Session Profiles — implemented
2. P3.2 Presets + import/export — storage/codec implemented; UI layer next
3. P3.3 Command favorites/shortcuts
4. P3.4 Explicit session restore
5. P3.5 Advanced terminal controls

## Compatibility gate

No P3 feature should require changes to Termux plugin intent contracts. Advanced controls remain layered over existing activity, service and terminal-session APIs.
