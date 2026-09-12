package com.termux.app.terminal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class OmegaSessionProfileTest {

    @Test
    public void defaultProfileIsStableAndValid() {
        OmegaSessionProfile profile = OmegaSessionProfile.omegaDefault();
        assertEquals("omega-default", profile.getId());
        assertEquals("OMEGA Default", profile.getDisplayName());
        assertEquals(OmegaSessionProfile.DEFAULT_FONT_SIZE, profile.getFontSize());
        assertEquals(OmegaSessionProfile.DEFAULT_SCROLLBACK, profile.getScrollbackLines());
        assertFalse(profile.isKeepScreenOn());
    }

    @Test
    public void storeRoundTripsProfileAndDeletesIt() {
        Context context = RuntimeEnvironment.getApplication();
        OmegaSessionProfileStore store = new OmegaSessionProfileStore(context);
        OmegaSessionProfile profile = new OmegaSessionProfile(
            "profile-a", "Focus", 16, "omega-neon", "underline", 5000, true);

        store.delete(profile.getId());
        assertFalse(store.contains(profile.getId()));

        store.save(profile);
        assertTrue(store.contains(profile.getId()));
        OmegaSessionProfile loaded = store.load(profile.getId());
        assertEquals(profile.getId(), loaded.getId());
        assertEquals(profile.getDisplayName(), loaded.getDisplayName());
        assertEquals(profile.getFontSize(), loaded.getFontSize());
        assertEquals(profile.getThemePreset(), loaded.getThemePreset());
        assertEquals(profile.getCursorStyle(), loaded.getCursorStyle());
        assertEquals(profile.getScrollbackLines(), loaded.getScrollbackLines());
        assertEquals(profile.isKeepScreenOn(), loaded.isKeepScreenOn());

        assertArrayEquals(new String[]{"profile-a"}, store.profileIds());
        store.delete(profile.getId());
        assertFalse(store.contains(profile.getId()));
        assertEquals(0, store.profileIds().length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyProfileIdIsRejected() {
        new OmegaSessionProfile(" ", "Invalid", 14, "omega-dark", "block", 100, false);
    }
}
