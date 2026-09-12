package com.termux.app.terminal;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;

/** Drawer entry point for OMEGA command preferences. */
public final class OmegaCommandPreferenceButton extends MaterialButton {
    public OmegaCommandPreferenceButton(@NonNull Context context) {
        super(context);
        setText("COMMAND PREFS");
        setContentDescription("Open OMEGA command preferences");
        setOnClickListener(v -> OmegaCommandPreferenceDialog.show(context));
    }
}
