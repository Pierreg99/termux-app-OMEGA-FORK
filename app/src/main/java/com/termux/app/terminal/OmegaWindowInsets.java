package com.termux.app.terminal;

import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/** Central OMEGA policy for system-bar/IME bottom insets. */
public final class OmegaWindowInsets {

    private OmegaWindowInsets() { }

    public static int resolveBottomInset(WindowInsetsCompat insets) {
        if (insets == null) return 0;
        int systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
        int ime = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
        return Math.max(systemBars, ime);
    }

    /**
     * Applies only the additional bottom inset to an existing bottom spacer.
     * The spacer remains transparent and outside terminal rendering.
     */
    public static void installBottomSpacerPolicy(final View spacer, final int baseHeightPx) {
        if (spacer == null) return;
        ViewCompat.setOnApplyWindowInsetsListener(spacer, (view, insets) -> {
            int bottom = resolveBottomInset(insets);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params != null) {
                int requestedHeight = baseHeightPx + bottom;
                if (params.height != requestedHeight) {
                    params.height = requestedHeight;
                    view.setLayoutParams(params);
                }
            }
            return insets;
        });
        ViewCompat.requestApplyInsets(spacer);
    }
}
