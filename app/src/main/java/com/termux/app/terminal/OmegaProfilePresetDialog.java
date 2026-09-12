package com.termux.app.terminal;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

/** Small OMEGA preset manager using clipboard-safe JSON import/export. */
public final class OmegaProfilePresetDialog {
    private OmegaProfilePresetDialog() { }

    public static void show(@NonNull Context context) {
        final OmegaSessionProfileStore store = new OmegaSessionProfileStore(context);
        final List<OmegaSessionProfile> presets = OmegaThemePresets.builtIns();
        String[] labels = new String[presets.size() + 4];
        for (int i = 0; i < presets.size(); i++) {
            labels[i] = "Apply: " + presets.get(i).getDisplayName();
        }
        labels[presets.size()] = "Export profiles to clipboard";
        labels[presets.size() + 1] = "Import profiles from clipboard";
        labels[presets.size() + 2] = "Reset to OMEGA Default";
        labels[presets.size() + 3] = "Cancel";

        AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle("OMEGA PRESETS")
            .setItems(labels, null)
            .create();
        dialog.setOnShowListener(d -> {
            android.widget.ListView list = dialog.getListView();
            list.setOnItemClickListener((parent, view, position, id) -> {
                if (position < presets.size()) {
                    OmegaSessionProfile profile = presets.get(position);
                    store.save(profile);
                    toast(context, profile.getDisplayName() + " saved");
                    dialog.dismiss();
                } else if (position == presets.size()) {
                    String json = OmegaSessionProfileCodec.exportProfiles(presetsToExport(store, presets));
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setPrimaryClip(android.content.ClipData.newPlainText("OMEGA profiles", json));
                    toast(context, "OMEGA profiles exported");
                    dialog.dismiss();
                } else if (position == presets.size() + 1) {
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    if (clipboard == null || !clipboard.hasPrimaryClip()) {
                        toast(context, "Clipboard is empty");
                        return;
                    }
                    CharSequence text = clipboard.getPrimaryClip().getItemAt(0).coerceToText(context);
                    try {
                        List<OmegaSessionProfile> imported = OmegaSessionProfileCodec.importProfiles(text == null ? "" : text.toString());
                        for (OmegaSessionProfile profile : imported) store.save(profile);
                        toast(context, imported.size() + " profile(s) imported");
                        dialog.dismiss();
                    } catch (IllegalArgumentException error) {
                        toast(context, "Invalid OMEGA profile export");
                    }
                } else if (position == presets.size() + 2) {
                    new AlertDialog.Builder(context)
                        .setTitle("Reset OMEGA profiles?")
                        .setMessage("Saved OMEGA profiles will be removed and the default preset restored.")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Reset", (ignored, which) -> {
                            for (String id : store.profileIds()) store.delete(id);
                            store.save(OmegaThemePresets.defaultProfile());
                            toast(context, "OMEGA Default restored");
                        })
                        .show();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            });
        });
        dialog.show();
    }

    @NonNull
    private static java.util.List<OmegaSessionProfile> presetsToExport(
        @NonNull OmegaSessionProfileStore store,
        @NonNull List<OmegaSessionProfile> builtIns) {
        java.util.ArrayList<OmegaSessionProfile> result = new java.util.ArrayList<>();
        for (OmegaSessionProfile preset : builtIns) result.add(preset);
        for (String id : store.profileIds()) {
            OmegaSessionProfile profile = store.load(id);
            boolean duplicate = false;
            for (OmegaSessionProfile existing : result) {
                if (existing.getId().equals(profile.getId())) { duplicate = true; break; }
            }
            if (!duplicate) result.add(profile);
        }
        return result;
    }

    private static void toast(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
