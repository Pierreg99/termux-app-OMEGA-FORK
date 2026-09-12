package com.termux.app.terminal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;

import com.google.android.material.button.MaterialButton;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class OmegaCommandPaletteTest {

    @Test
    public void commandStateWrapsSelectionAndResetsOnQuery() {
        OmegaCommandState state = new OmegaCommandState();
        state.moveSelection(-1, 3);
        assertEquals(2, state.getSelectedIndex());

        state.moveSelection(1, 3);
        assertEquals(0, state.getSelectedIndex());

        state.setQuery("terminal");
        assertEquals("terminal", state.getQuery());
        assertEquals(0, state.getSelectedIndex());
    }

    @Test
    public void registryContainsCorePaletteCommands() {
        List<OmegaCommand> commands = OmegaCommandRegistry.defaultCommands();
        assertTrue(commands.size() >= 15);
        assertTrue(contains(commands, "session.new"));
        assertTrue(contains(commands, "session.rename-current"));
        assertTrue(contains(commands, "session.close-current"));
        assertTrue(contains(commands, "terminal.keyboard"));
        assertTrue(contains(commands, "terminal.keep-screen-on"));
        assertTrue(contains(commands, "editing.paste"));
        assertFalse(contains(commands, "session.select.1"));
    }

    @Test
    public void sessionAwareRegistryAddsOnlyAvailableSessionSelectors() {
        List<OmegaCommand> commands = OmegaCommandRegistry.sessionAwareCommands(3);
        assertTrue(contains(commands, "session.select.1"));
        assertTrue(contains(commands, "session.select.2"));
        assertTrue(contains(commands, "session.select.3"));
        assertFalse(contains(commands, "session.select.4"));
    }

    @Test
    public void sessionAwareRegistryCapsSessionSelectors() {
        List<OmegaCommand> commands = OmegaCommandRegistry.sessionAwareCommands(99);
        int selectors = 0;
        for (OmegaCommand command : commands) {
            if (command.getId().startsWith("session.select.")) selectors++;
        }
        assertEquals(8, selectors);
    }

    @Test
    public void paletteLauncherHasAccessibleLabel() {
        Context context = Robolectric.buildActivity(Activity.class).setup().get();
        MaterialButton button = new OmegaCommandPaletteButton(context);
        assertEquals("COMMANDS", button.getText().toString());
        assertEquals("Open OMEGA Command Center", button.getContentDescription());
    }

    private static boolean contains(List<OmegaCommand> commands, String id) {
        for (OmegaCommand command : commands) {
            if (id.equals(command.getId())) return true;
        }
        return false;
    }
}
