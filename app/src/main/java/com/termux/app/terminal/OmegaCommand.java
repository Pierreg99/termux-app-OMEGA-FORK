package com.termux.app.terminal;

import androidx.annotation.NonNull;

/** Immutable command metadata used by the OMEGA Command Center. */
public final class OmegaCommand {

    public enum Category {
        SESSIONS,
        TERMINAL,
        NAVIGATION,
        EDITING,
        DIAGNOSTICS
    }

    private final String id;
    private final Category category;
    private final String title;
    private final String shortcut;

    public OmegaCommand(@NonNull String id, @NonNull Category category, @NonNull String title, String shortcut) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.shortcut = shortcut;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public Category getCategory() {
        return category;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public String getShortcut() {
        return shortcut;
    }
}
