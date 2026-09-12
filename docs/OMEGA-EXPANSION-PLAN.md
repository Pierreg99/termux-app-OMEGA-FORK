# OMEGA Expansion Plan

**Repository:** `Pierreg99/termux-app-OMEGA-FORK`

## Objective

Evolve the fork into a clearly differentiated, production-oriented OMEGA experience while preserving Termux core compatibility, terminal reliability and a maintainable upstream-sync path.

## Baseline

The repository currently uses the `master` branch and retains the legacy Termux app architecture, including `TermuxActivity`, `RunCommandService`, terminal-view and termux-shared modules. The Android app configuration is still based on a legacy `0.118.x` baseline with Java 8 source/target settings and Material Components already available. These are migration constraints, not a reason for a full rewrite.

## Phase 1 — OMEGA Design System — IN PROGRESS / BASELINE IMPLEMENTED

- [x] Establish a dedicated OMEGA visual language for the Android terminal UI.
- [x] Centralize initial color, spacing, shape and interaction-size tokens.
- [x] Implement dark-first OMEGA surfaces and terminal-safe semantic colors.
- [x] Wire primary, background, drawer, extra-key and accent styling through semantic resources.
- [x] Add shared component-style foundations and document the token contract.
- [ ] Modernize toolbar, session controls, dialogs and preferences using the token layer.
- [ ] Add light/high-contrast and dynamic-color variants where practical.
- [ ] Complete accessibility validation and larger-text checks.

**Current implementation:** `app/src/main/res/values/colors.xml`, `dimens.xml`, `themes.xml`, `styles.xml`, and `docs/OMEGA-DESIGN-SYSTEM.md`.

## Phase 2 — Terminal UX Upgrade

- Improve session/tab switching and lifecycle visibility.
- Provide clearer active/background/closed session states.
- Upgrade extra-key handling with configurable profiles and customization.
- Improve selection, copy/paste, URL detection and contextual actions.
- Add app-action search without replacing shell input.
- Improve keyboard/insets behavior for modern Android navigation.

## Phase 3 — OMEGA Command Center

- Add an optional OMEGA command surface for high-frequency app actions.
- Provide command palette/search for navigation and settings.
- Add quick actions for new session, session navigation, preferences and help.
- Make the command surface keyboard- and accessibility-friendly.

## Phase 4 — Power-User Features

- Add per-session profiles.
- Add theme/profile presets with import/export.
- Expand font, cursor, scrollback, opacity/transparency and extra-key controls.
- Improve observability and validation around external command intents.
- Add optional session restore under explicit user control.
- Preserve compatibility with existing Termux plugin integration points.

## Phase 5 — Performance & Reliability

- Reduce unnecessary UI work during heavy terminal output.
- Audit memory, session teardown, background behavior and recreation/rotation paths.
- Profile large scrollback, rapid output and multiple-session workloads.
- Harden crash-prone lifecycle paths and diagnostics.
- Avoid visual effects that materially reduce performance on lower-end devices.

## Phase 6 — Security & Trust

- Review exported components, intents, URI handling and permission boundaries.
- Preserve secure defaults for command execution and inter-app communication.
- Separate development/test artifacts from trusted release artifacts.
- Add regression tests for intent handling and input validation.

## Phase 7 — Build, CI/CD & Release Engineering

- Modernize Gradle/Android tooling incrementally with compatibility checkpoints.
- Add matrix builds for supported Android/API and ABI combinations.
- Expand lint, unit, instrumentation and smoke-test coverage.
- Validate APK metadata, artifact naming and release consistency.
- Establish OMEGA release notes and migration notes.

## Phase 8 — Documentation & Product Surface

- Add an OMEGA-specific project landing section while preserving upstream attribution and licensing clarity.
- Document architecture, customization, build variants, development workflow and sync strategy.
- Add UI screenshots/design references.
- Maintain `ROADMAP.md` plus focused documents under `docs/`.

## Recommended Order

1. Baseline and architecture audit
2. Design tokens and core theme surfaces
3. Terminal/session UX
4. Power-user preferences and profiles
5. Performance/reliability hardening
6. Security review
7. Toolchain and CI modernization
8. Documentation and release polish

## Acceptance Criteria

- OMEGA UI uses a reusable design system rather than one-off styling.
- Existing terminal functionality remains operational throughout the upgrade.
- Session management and keyboard/touch UX are clearer on current Android devices.
- No major regression in startup time, rendering, input latency or heavy-output stability.
- CI builds and tests supported variants consistently.
- Security-sensitive intent/exported surfaces have documented behavior and regression coverage.
- OMEGA changes remain sufficiently isolated for future upstream synchronization.

## Deliverables

- `docs/OMEGA-DESIGN-SYSTEM.md`
- `docs/OMEGA-ARCHITECTURE.md`
- `docs/OMEGA-ROADMAP.md`
- `ROADMAP.md`
- OMEGA theme/token implementation
- upgraded session/terminal UX
- test and CI improvements
- release and migration documentation

## Non-Goals

- Do not replace the terminal engine solely for visual modernization.
- Do not add heavy UI frameworks without a concrete technical requirement.
- Do not weaken Termux interoperability for cosmetic features.
- Do not expose or reuse development signing credentials as release trust material.

## Tracking

This document is the master tracking specification for the OMEGA fork's design, UX and platform-upgrade program. Phase 1 has a working baseline; the next implementation target is terminal/session UX using the new semantic token layer.
