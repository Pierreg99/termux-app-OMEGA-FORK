package com.termux.app.terminal;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Built-in OMEGA presentation presets expressed as session profiles. */
public final class OmegaThemePresets {
    private OmegaThemePresets() { }

    @NonNull
    public static List<OmegaSessionProfile> builtIns() {
        return Collections.unmodifiableList(Arrays.asList(
            new OmegaSessionProfile("omega-default", "OMEGA Default", 14, "omega-dark", "block", 2000, false),
            new OmegaSessionProfile("omega-neon", "OMEGA Neon", 15, "omega-neon", "bar", 4096, false),
            new OmegaSessionProfile("omega-focus", "OMEGA Focus", 16, "omega-focus", "underline", 8000, true)
        ));
    }

    @NonNull
    public static OmegaSessionProfile defaultProfile() {
        return builtIns().get(0);
    }
}
