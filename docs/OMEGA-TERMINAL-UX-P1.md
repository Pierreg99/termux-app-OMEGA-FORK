# OMEGA Terminal UX — P1

## Status

**P1 implementation complete — baseline shipped**

P1 migrates the primary terminal interaction surfaces onto the OMEGA Design System v1 while preserving the terminal engine and existing Termux interaction model.

## Implemented surfaces

1. **Session drawer** — explicit `ACTIVE`, `BACKGROUND` and `EXITED` states, stronger hierarchy and 48dp touch targets.
2. **Session actions** — New session, keyboard toggle and settings use shared OMEGA spacing and component tokens.
3. **Terminal toolbar** — OMEGA semantic surface, stable 40dp tokenized height and high-contrast controls.
4. **Extra keys** — OMEGA visual profile applies semantic text/background/active-state tokens while retaining the existing properties-based key configuration.
5. **Dialogs** — existing Termux dialog hierarchy remains intact while OMEGA semantic colors are available to the surface layer.
6. **Keyboard/insets** — a dedicated OMEGA inset spacer resolves the larger of system-bar and IME bottom insets outside the terminal renderer.
7. **UI verification** — Robolectric coverage verifies session state semantics, inset policy and critical P1 layout surfaces.

## Migration rules

- Terminal output remains the highest visual priority.
- No gradients, blur or animation in the terminal rendering path.
- Touch targets should remain at least 48dp where practical.
- Semantic colors must come from OMEGA resources instead of literal colors.
- Session state must be distinguishable without color alone.
- Existing plugin intents and terminal engine behavior remain unchanged.

## Acceptance criteria

- Existing sessions remain selectable and usable.
- Active/background/exited state is communicated by explicit state text as well as visual treatment.
- New-session and keyboard actions remain available through the existing Termux interaction model.
- Toolbar and extra keys use reusable OMEGA semantic styling.
- IME/system-bar insets reserve bottom space without covering terminal content.
- P1 UI/resource tests cover state, layout and inset regressions.
- No terminal rendering dependency is introduced by the OMEGA presentation layer.

## Validation

Automated validation is defined in `docs/OMEGA-P1-TEST-MATRIX.md` and implemented in `app/src/test/java/com/termux/app/OmegaP1UiTest.java`. A successful local Android/Gradle execution has not been claimed unless CI or a connected build verifies it.

## P2 hand-off

P1 is complete. The next track is **P2 OMEGA Command Center**: an optional command surface for session navigation, actions, settings and keyboard-accessible command search.
