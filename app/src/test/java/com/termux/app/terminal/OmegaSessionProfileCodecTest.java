package com.termux.app.terminal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OmegaSessionProfileCodecTest {

    @Test
    public void exportIsVersionedAndDeterministic() {
        OmegaSessionProfile beta = new OmegaSessionProfile(
            "beta", "Beta", 15, "omega-blue", "bar", 3000, false);
        OmegaSessionProfile alpha = new OmegaSessionProfile(
            "alpha", "Alpha", 17, "omega-neon", "underline", 5000, true);

        String json = OmegaSessionProfileCodec.exportProfiles(Arrays.asList(beta, alpha));

        assertEquals(
            "{\"version\":1,\"profiles\":["
                + "{\"id\":\"alpha\",\"displayName\":\"Alpha\",\"fontSize\":17,"
                + "\"themePreset\":\"omega-neon\",\"cursorStyle\":\"underline\","
                + "\"scrollbackLines\":5000,\"keepScreenOn\":true},"
                + "{\"id\":\"beta\",\"displayName\":\"Beta\",\"fontSize\":15,"
                + "\"themePreset\":\"omega-blue\",\"cursorStyle\":\"bar\","
                + "\"scrollbackLines\":3000,\"keepScreenOn\":false}]}"
            , json);
    }

    @Test
    public void importRoundTripsProfiles() {
        OmegaSessionProfile profile = new OmegaSessionProfile(
            "work", "Work", 16, "omega-neon", "underline", 4096, true);

        List<OmegaSessionProfile> imported = OmegaSessionProfileCodec.importProfiles(
            OmegaSessionProfileCodec.exportProfiles(Arrays.asList(profile)));

        assertEquals(1, imported.size());
        OmegaSessionProfile actual = imported.get(0);
        assertEquals(profile.getId(), actual.getId());
        assertEquals(profile.getDisplayName(), actual.getDisplayName());
        assertEquals(profile.getFontSize(), actual.getFontSize());
        assertEquals(profile.getThemePreset(), actual.getThemePreset());
        assertEquals(profile.getCursorStyle(), actual.getCursorStyle());
        assertEquals(profile.getScrollbackLines(), actual.getScrollbackLines());
        assertEquals(profile.isKeepScreenOn(), actual.isKeepScreenOn());
    }

    @Test
    public void unsupportedVersionIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
            OmegaSessionProfileCodec.importProfiles(
                "{\"version\":2,\"profiles\":[]}"));
    }

    @Test
    public void malformedExportIsRejected() {
        assertThrows(IllegalArgumentException.class, () ->
            OmegaSessionProfileCodec.importProfiles("not-json"));
    }
}
