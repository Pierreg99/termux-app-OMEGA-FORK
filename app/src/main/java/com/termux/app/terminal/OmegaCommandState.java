package com.termux.app.terminal;

import androidx.annotation.NonNull;

/** Mutable, UI-independent state for the OMEGA command palette. */
public final class OmegaCommandState {
    private String query = "";
    private int selectedIndex = 0;

    @NonNull
    public String getQuery() {
        return query;
    }

    public void setQuery(@NonNull String query) {
        this.query = query;
        selectedIndex = 0;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void moveSelection(int delta, int itemCount) {
        if (itemCount <= 0) {
            selectedIndex = 0;
            return;
        }
        int next = (selectedIndex + delta) % itemCount;
        if (next < 0) next += itemCount;
        selectedIndex = next;
    }
}
