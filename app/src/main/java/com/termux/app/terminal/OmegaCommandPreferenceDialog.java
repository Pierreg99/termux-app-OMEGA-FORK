package com.termux.app.terminal;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

/** OMEGA-only shortcut and ordering editor; existing Termux commands remain immutable. */
public final class OmegaCommandPreferenceDialog {
    private OmegaCommandPreferenceDialog() { }

    public static void show(@NonNull Context context) {
        OmegaCommandPreferenceStore store = new OmegaCommandPreferenceStore(context);
        List<OmegaCommand> commands = OmegaCommandRegistry.defaultCommands();
        String[] labels = new String[commands.size()];
        for (int i = 0; i < commands.size(); i++) labels[i] = commands.get(i).getTitle();

        new AlertDialog.Builder(context)
            .setTitle("OMEGA COMMAND PREFERENCES")
            .setItems(labels, (dialog, which) -> editShortcut(context, store, commands, which))
            .setNegativeButton("Close", null)
            .show();
    }

    private static void editShortcut(@NonNull Context context,
                                     @NonNull OmegaCommandPreferenceStore store,
                                     @NonNull List<OmegaCommand> commands,
                                     int index) {
        OmegaCommand command = commands.get(index);
        EditText input = new EditText(context);
        input.setSingleLine(true);
        input.setHint("Example: O + K");
        input.setText(store.getCustomShortcut(command.getId()));
        input.setSelectAllOnFocus(true);

        LinearLayout container = new LinearLayout(context);
        container.setPadding(32, 8, 32, 0);
        container.addView(input, new LinearLayout.LayoutParams(-1, -2));

        new AlertDialog.Builder(context)
            .setTitle(command.getTitle())
            .setMessage("Set an OMEGA-only shortcut. Leave blank to remove the override.")
            .setView(container)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Save", (dialog, which) -> {
                String shortcut = input.getText().toString().trim();
                if (shortcut.isEmpty()) {
                    store.setCustomShortcut(command.getId(), "");
                    toast(context, "Shortcut override removed");
                    return;
                }
                if (!store.isShortcutAvailable(command.getId(), shortcut, commands)) {
                    toast(context, "Shortcut conflicts with an existing OMEGA/Termux shortcut");
                    return;
                }
                store.setCustomShortcut(command.getId(), shortcut);
                toast(context, "Shortcut saved: " + shortcut);
            })
            .show();
    }

    private static void toast(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
