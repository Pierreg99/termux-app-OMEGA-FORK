package com.termux.app.terminal;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;

/** Drawer entry point for OMEGA theme/profile presets. */
public final class OmegaProfilePresetButton extends MaterialButton {
    public OmegaProfilePresetButton(@NonNull Context context) {
        super(context);
        setText("PRESETS");
        setContentDescription("Open OMEGA theme and profile presets");
        setOnClickListener(v -> OmegaProfilePresetDialog.show(context));
    }
}
