package com.termux.app.terminal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import com.google.android.material.button.MaterialButton;

import org.junit.Test;
import org.junit.runner.RunWith;
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
        assertTrue(commands.size() >= 12);
        assertTrue(contains(commands, "session.new"));
        assertTrue(contains(commands, "terminal.keyboard"));
        assertTrue(contains(commands, "navigation.settings"));
    }

    @Test
    public void paletteLauncherHasAccessibleLabel() {
        Context context = RuntimeEnvironment.getApplication();
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
