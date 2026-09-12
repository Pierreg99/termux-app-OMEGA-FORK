package com.termux.app.terminal;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Default searchable command catalog for the OMEGA Command Center. */
public final class OmegaCommandRegistry {

    private OmegaCommandRegistry() { }

    @NonNull
    public static List<OmegaCommand> defaultCommands() {
        List<OmegaCommand> commands = new ArrayList<>();

        commands.add(new OmegaCommand("session.new", OmegaCommand.Category.SESSIONS, "New session", "Ctrl+Shift+N"));
        commands.add(new OmegaCommand("session.next", OmegaCommand.Category.SESSIONS, "Next session", "Ctrl+Tab"));
        commands.add(new OmegaCommand("session.previous", OmegaCommand.Category.SESSIONS, "Previous session", "Ctrl+Shift+Tab"));
        commands.add(new OmegaCommand("terminal.keyboard", OmegaCommand.Category.TERMINAL, "Toggle keyboard", null));
        commands.add(new OmegaCommand("terminal.toolbar", OmegaCommand.Category.TERMINAL, "Toggle toolbar", null));
        commands.add(new OmegaCommand("terminal.reset", OmegaCommand.Category.TERMINAL, "Reset terminal", null));
        commands.add(new OmegaCommand("navigation.drawer", OmegaCommand.Category.NAVIGATION, "Open session drawer", "Ctrl+Alt+D"));
        commands.add(new OmegaCommand("navigation.settings", OmegaCommand.Category.NAVIGATION, "Open settings", null));
        commands.add(new OmegaCommand("navigation.help", OmegaCommand.Category.NAVIGATION, "Open help", null));
        commands.add(new OmegaCommand("editing.paste", OmegaCommand.Category.EDITING, "Paste clipboard", "Ctrl+Shift+V"));
        commands.add(new OmegaCommand("editing.select-url", OmegaCommand.Category.EDITING, "Select URL", null));
        commands.add(new OmegaCommand("diagnostics.report", OmegaCommand.Category.DIAGNOSTICS, "Report issue", null));

        return Collections.unmodifiableList(commands);
    }
}
