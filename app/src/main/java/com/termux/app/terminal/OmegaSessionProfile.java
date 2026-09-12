package com.termux.app.terminal;

import androidx.annotation.NonNull;

/** Immutable optional presentation profile for an OMEGA terminal session. */
public final class OmegaSessionProfile {
    public static final int DEFAULT_FONT_SIZE = 14;
    public static final int DEFAULT_SCROLLBACK = 2000;

    private final String id;
    private final String displayName;
    private final int fontSize;
    private final String themePreset;
    private final String cursorStyle;
    private final int scrollbackLines;
    private final boolean keepScreenOn;

    public OmegaSessionProfile(@NonNull String id,
                               @NonNull String displayName,
                               int fontSize,
                               @NonNull String themePreset,
                               @NonNull String cursorStyle,
                               int scrollbackLines,
                               boolean keepScreenOn) {
        if (id.trim().isEmpty()) throw new IllegalArgumentException("Profile id must not be empty");
        if (fontSize < 1) throw new IllegalArgumentException("Font size must be positive");
        if (scrollbackLines < 0) throw new IllegalArgumentException("Scrollback must not be negative");
        this.id = id;
        this.displayName = displayName;
        this.fontSize = fontSize;
        this.themePreset = themePreset;
        this.cursorStyle = cursorStyle;
        this.scrollbackLines = scrollbackLines;
        this.keepScreenOn = keepScreenOn;
    }

    @NonNull public String getId() { return id; }
    @NonNull public String getDisplayName() { return displayName; }
    public int getFontSize() { return fontSize; }
    @NonNull public String getThemePreset() { return themePreset; }
    @NonNull public String getCursorStyle() { return cursorStyle; }
    public int getScrollbackLines() { return scrollbackLines; }
    public boolean isKeepScreenOn() { return keepScreenOn; }

    @NonNull
    public static OmegaSessionProfile omegaDefault() {
        return new OmegaSessionProfile("omega-default", "OMEGA Default", DEFAULT_FONT_SIZE,
            "omega-dark", "block", DEFAULT_SCROLLBACK, false);
    }
}
