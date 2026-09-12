# OMEGA Design System v1

**Status:** Implemented baseline  
**Scope:** Android app visual foundations for the OMEGA fork

## Principles

1. Terminal readability is the primary visual requirement.
2. Semantic tokens replace scattered hard-coded UI values.
3. Dark-first surfaces are explicit and reusable.
4. Touch targets remain suitable for phones and tablets.
5. OMEGA-specific styling stays isolated from terminal-engine behavior.

## Token Layers

### Color

- `omega_background`: primary app background
- `omega_surface`: drawer and standard surfaces
- `omega_surface_elevated`: elevated controls and active states
- `omega_on_background`: primary foreground text
- `omega_on_surface`: secondary surface text
- `omega_primary`: primary interactive/accent color
- `omega_primary_variant`: pressed/secondary primary state
- `omega_accent`: secondary emphasis
- `omega_success`, `omega_warning`, `omega_error`: semantic status colors
- `omega_terminal_background`, `omega_terminal_foreground`, `omega_terminal_cursor`: terminal-safe rendering tokens
- `omega_divider`: structural separators

### Spacing and Shape

The initial spacing scale is 4/8/12/16/24/32dp. Shared touch targets use 48dp. Shape tokens provide small, medium and large corner radii; the terminal toolbar uses a shared 40dp height token.

## Current Integration

`colors.xml`, `dimens.xml`, `themes.xml` and `styles.xml` provide the reusable OMEGA layer. The main terminal surface now consumes semantic background and toolbar tokens, while drawer controls use shared spacing and touch-target resources.

## Interface References

P1 design references are maintained under `docs/interfaces/`. They document session drawer, toolbar, keyboard/inset and extra-key states without pretending to be screenshots of an unverified runtime build.

## Compatibility

The design layer does not replace the existing terminal renderer, command services or plugin interfaces. Existing black/white/red/grey compatibility resources remain available while OMEGA components migrate to semantic tokens.

## Verification

The P1 test matrix is maintained in `docs/OMEGA-P1-TEST-MATRIX.md`. CI/device validation is required before claiming runtime screenshot parity or release readiness.

## Next Step

Complete P1 Terminal UX: session-row redesign, extra-key profiles, contextual actions and centralized modern window-insets handling, followed by instrumented UI tests and real-device PNG screenshot capture.
