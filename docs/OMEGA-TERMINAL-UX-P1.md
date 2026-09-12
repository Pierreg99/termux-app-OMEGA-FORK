# OMEGA Terminal UX — P1

## Scope

P1 migrates the primary terminal interaction surfaces onto the OMEGA Design System v1 while preserving the terminal engine and existing Termux interaction model.

## Interface targets

1. **Session drawer** — clearer hierarchy, active-session emphasis, 48dp touch targets.
2. **Session actions** — New session, keyboard toggle and settings use shared OMEGA spacing and component tokens.
3. **Terminal toolbar** — OMEGA surface/background token, stable height and high-contrast controls.
4. **Dialogs** — use OMEGA semantic colors, accessible action contrast and consistent spacing.
5. **Keyboard/insets** — preserve current inset handling while preparing a centralized P1 controller.

## Migration rules

- Terminal output remains the highest visual priority.
- No gradients, blur or animation in the terminal rendering path.
- Touch targets should remain at least 48dp where practical.
- Semantic colors must come from OMEGA resources instead of literal colors.
- Session state must be distinguishable without color alone.
- Existing plugin intents and terminal engine behavior remain unchanged.

## P1 implementation status

- [x] OMEGA tokens available to terminal surfaces.
- [x] Drawer/action controls aligned with spacing and touch-target tokens.
- [x] Toolbar surface moved to an OMEGA semantic token.
- [x] Interface mockups added under `docs/interfaces/`.
- [x] Test matrix and manual QA checklist added.
- [ ] Full session-card redesign.
- [ ] Extra-keys profile editor.
- [ ] Centralized modern-window-insets controller.
- [ ] Instrumented UI tests for session switching.

## Acceptance criteria

- Existing sessions remain selectable and usable.
- New-session and keyboard actions remain functional.
- Toolbar remains readable in dark mode and system navigation configurations.
- No terminal rendering dependency on new UI effects.
- P1-specific tests cover state, accessibility and lifecycle regressions.
