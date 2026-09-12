package com.termux.app.terminal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;

@RunWith(RobolectricTestRunner.class)
public class OmegaCommandPreferenceStoreTest {
    @Test
    public void favoritesAndShortcutsRoundTrip() {
        Context context = RuntimeEnvironment.getApplication();
        OmegaCommandPreferenceStore store = new OmegaCommandPreferenceStore(context);
        store.setFavorite("session.new", true);
        assertTrue(store.isFavorite("session.new"));
        store.setFavorite("session.new", false);
        assertFalse(store.isFavorite("session.new"));

        store.setCustomShortcut("session.new", "OMEGA+N");
        assertTrue("OMEGA+N".equals(store.getCustomShortcut("session.new")));
        store.setCustomShortcut("session.new", "");
        assertTrue(store.getCustomShortcut("session.new").isEmpty());
    }

    @Test
    public void shortcutConflictIsDetected() {
        Context context = RuntimeEnvironment.getApplication();
        OmegaCommandPreferenceStore store = new OmegaCommandPreferenceStore(context);
        java.util.List<OmegaCommand> catalog = OmegaCommandRegistry.defaultCommands();
        store.setCustomShortcut("session.new", "CUSTOM-X");

        assertFalse(store.isShortcutAvailable("session.previous", "CUSTOM-X", catalog));
        assertFalse(store.isShortcutAvailable("session.previous", "Alt+N", catalog));
        assertTrue(store.isShortcutAvailable("session.previous", "OMEGA+P", catalog));
    }
}
