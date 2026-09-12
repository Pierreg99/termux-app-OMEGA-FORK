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

The initial spacing scale is 4/8/12/16/24/32dp. Shared touch targets use 48dp. Shape tokens provide small, medium and large corner radii.

## Current Integration

`colors.xml`, `dimens.xml`, `themes.xml` and `styles.xml` now provide the first reusable OMEGA layer. The main terminal theme maps primary, background, drawer, extra-key and accent values to semantic tokens.

## Compatibility

The design layer does not replace the existing terminal renderer, command services or plugin interfaces. Existing black/white/red/grey compatibility resources remain available while OMEGA components migrate to semantic tokens.

## Next Step

Phase P1 is Terminal UX: apply the shared tokens to session controls, terminal toolbar, keyboard/extra keys, dialogs and settings surfaces, followed by visual regression and accessibility checks.
