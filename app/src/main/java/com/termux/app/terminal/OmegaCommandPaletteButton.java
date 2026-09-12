package com.termux.app.terminal;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

/** Small native entry point for the OMEGA command palette. */
public final class OmegaCommandPaletteButton extends MaterialButton {
    public OmegaCommandPaletteButton(Context context) {
        super(context);
        init();
    }

    public OmegaCommandPaletteButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OmegaCommandPaletteButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setText("COMMANDS");
        setAllCaps(false);
        setContentDescription("Open OMEGA Command Center");
        setOnClickListener(v -> new OmegaCommandPalette(getContext()).show());
    }
}
