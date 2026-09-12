# OMEGA Expansion Plan

**Repository:** `Pierreg99/termux-app-OMEGA-FORK`

## Objective

Evolve the fork into a clearly differentiated, production-oriented OMEGA experience while preserving Termux core compatibility, terminal reliability and a maintainable upstream-sync path.

## Phase 1 — OMEGA Design System — COMPLETE

- [x] Dedicated OMEGA visual language.
- [x] Central color, spacing, shape and interaction-size tokens.
- [x] Dark-first semantic terminal surfaces.
- [x] Shared component-style foundations.
- [x] Design-system documentation.
- [x] Initial terminal surface migration.

## Phase 2 — Terminal UX Upgrade — COMPLETE

- [x] Apply OMEGA spacing and touch-target tokens to the session drawer.
- [x] Move terminal toolbar background to an OMEGA semantic surface token.
- [x] Redesign session rows with explicit ACTIVE/BACKGROUND/EXITED states.
- [x] Add accessible session-state text and content descriptions.
- [x] Apply OMEGA semantic visual policy to extra keys where supported by the existing API surface.
- [x] Preserve existing `termux.properties` extra-key configuration.
- [x] Centralize bottom IME/system-bar inset resolution.
- [x] Keep inset handling outside terminal rendering through a transparent spacer.
- [x] Add P1 UI/resource tests with Robolectric.
- [x] Add P1 test matrix, changelog and interface references.

## Phase 3 — OMEGA Command Center — P2.1/P2.2 COMPLETE, P2.3 VALIDATION ACTIVE

- [x] Define Command Center architecture and command categories.
- [x] Add immutable command identifiers and registry.
- [x] Add session-aware command availability and selector generation.
- [x] Add searchable command palette.
- [x] Add keyboard-first navigation and accessibility focus handling.
- [x] Connect session actions to existing Termux session APIs.
- [x] Connect terminal actions to existing activity APIs.
- [x] Add command-registry and UI tests.
- [x] Add automated JVM regression gate to Android evidence workflow.
- [x] Add UI-tree assertions, portrait/landscape captures and evidence manifest.
- [ ] Execute Android emulator/device screenshot and accessibility evidence gate.
- [ ] Complete hardware-keyboard smoke validation.

**P2 specification:** `docs/OMEGA-COMMAND-CENTER-P2.md`

## Phase 4 — Power-User Features — P3 BASELINE IMPLEMENTED

- [x] Per-session profile model and isolated persistence store.
- [x] Built-in OMEGA theme/profile presets.
- [x] Versioned JSON import/export and validation.
- [x] P3.2 preset manager UI in the session drawer.
- [x] Persistent command favorites/pinning.
- [x] Persistent command ordering data.
- [x] Bounded recent-command recording.
- [x] Configurable OMEGA-only shortcut overrides.
- [x] Shortcut conflict detection.
- [x] P3.3 command preferences UI in the session drawer.
- [ ] Apply profiles directly to live session presentation state.
- [ ] Optional session restore under explicit user control.
- [ ] Font, cursor, scrollback, opacity/transparency and extra-key controls.
- [x] Preserve existing Termux plugin integration points.

**P3 specification:** `docs/OMEGA-POWER-USER-P3.md`

## Phase 5 — Performance & Reliability

- [ ] Reduce unnecessary UI work during heavy terminal output.
- [ ] Audit memory, teardown, background behavior and recreation paths.
- [ ] Profile large scrollback, rapid output and multiple-session workloads.
- [ ] Harden lifecycle diagnostics.

## Phase 6 — Security & Trust

- [ ] Review exported components, intents, URI handling and permission boundaries.
- [ ] Add regression tests for intent handling and input validation.

## Phase 7 — Build, CI/CD & Release Engineering

- [ ] Incremental Gradle/Android tooling modernization.
- [ ] Matrix builds for supported Android/API and ABI combinations.
- [ ] Expanded lint, unit, instrumentation and smoke-test coverage.
- [ ] Consistent OMEGA artifact naming and release notes.

## Phase 8 — Documentation & Product Surface

- [ ] OMEGA project landing section.
- [ ] Architecture, customization, build and sync documentation.
- [x] UI interface reference assets.
- [ ] Gallery of device screenshots captured from real builds.

## Tracking

**Current state:** P0/P1 complete. P2.1/P2.2 implemented; P2.3 evidence is still blocked by the current GitHub Actions execution issue, so no runtime/accessibility pass is claimed. P3.1–P3.3 baseline layers are now UI-integrated and isolated from the terminal engine. Next product work is P3.4 explicit session restore; next validation work is repairing the P2.3 Actions execution path.
