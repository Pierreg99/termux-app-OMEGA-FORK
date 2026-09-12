# OMEGA Expansion Plan

**Repository:** `Pierreg99/termux-app-OMEGA-FORK`

## Objective

Evolve the fork into a clearly differentiated, production-oriented OMEGA experience while preserving Termux core compatibility, terminal reliability and a maintainable upstream-sync path.

## Phase 1 — OMEGA Design System — BASELINE IMPLEMENTED

- [x] Dedicated OMEGA visual language.
- [x] Central color, spacing, shape and interaction-size tokens.
- [x] Dark-first semantic terminal surfaces.
- [x] Shared component-style foundations.
- [x] Design-system documentation.
- [x] Initial terminal surface migration.

## Phase 2 — Terminal UX Upgrade — ACTIVE

- [x] Apply OMEGA spacing and touch-target tokens to the session drawer.
- [x] Move terminal toolbar background to an OMEGA semantic surface token.
- [x] Add P1 interface reference pictures as repository SVG assets.
- [x] Add P1 test matrix and manual QA checklist.
- [x] Add P1 changelog.
- [ ] Redesign session rows/cards with explicit active/background/closed states.
- [ ] Upgrade extra-key handling with configurable profiles and customization.
- [ ] Improve selection, copy/paste, URL detection and contextual actions.
- [ ] Add app-action search without replacing shell input.
- [ ] Centralize modern keyboard/window-insets handling.
- [ ] Add instrumented session-switching tests.

**P1 documentation:** `docs/OMEGA-TERMINAL-UX-P1.md`  
**P1 tests:** `docs/OMEGA-P1-TEST-MATRIX.md`  
**Changelog:** `docs/OMEGA-CHANGELOG.md`  
**Interface references:** `docs/interfaces/*.svg`

## Phase 3 — OMEGA Command Center

- [ ] Add optional OMEGA command surface.
- [ ] Command palette/search for navigation and settings.
- [ ] Quick actions for new session, session navigation, preferences and help.
- [ ] Keyboard- and accessibility-friendly command surface.

## Phase 4 — Power-User Features

- [ ] Per-session profiles.
- [ ] Theme/profile presets with import/export.
- [ ] Font, cursor, scrollback, opacity/transparency and extra-key controls.
- [ ] Optional session restore under explicit user control.
- [ ] Preserve existing Termux plugin integration points.

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

The current implementation target is **P1 Terminal UX**. Visual references in this phase are vector interface pictures; real PNG screenshots are a follow-up output of device/emulator QA so they represent the actual running build rather than static concept art.
