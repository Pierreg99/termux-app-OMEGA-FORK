package com.termux.app.terminal;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/** Persistent, OMEGA-only decoration state for the immutable command catalog. */
public final class OmegaCommandPreferenceStore {
    private static final String PREFS = "omega_command_preferences";
    private static final String FAVORITES = "favorites";
    private static final String RECENTS = "recents";
    private static final String ORDER = "order";
    private static final String SHORTCUT_PREFIX = "shortcut_";

    private final SharedPreferences preferences;

    public OmegaCommandPreferenceStore(@NonNull Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public boolean isFavorite(@NonNull String commandId) {
        return favorites().contains(commandId);
    }

    public void setFavorite(@NonNull String commandId, boolean favorite) {
        LinkedHashSet<String> values = favorites();
        if (favorite) values.add(commandId); else values.remove(commandId);
        putList(FAVORITES, values);
    }

    public void recordRecent(@NonNull String commandId) {
        LinkedHashSet<String> values = recents();
        values.remove(commandId);
        values.add(commandId);
        while (values.size() > 12) values.remove(values.iterator().next());
        putList(RECENTS, values);
    }

    @NonNull
    public List<String> recentCommandIds() { return new ArrayList<>(recents()); }

    public void setOrder(@NonNull List<String> commandIds) {
        putList(ORDER, new LinkedHashSet<>(commandIds));
    }

    @NonNull
    public List<String> orderedCommandIds() { return new ArrayList<>(readList(ORDER)); }

    public void setCustomShortcut(@NonNull String commandId, @NonNull String shortcut) {
        String normalized = shortcut.trim();
        if (normalized.isEmpty()) {
            preferences.edit().remove(SHORTCUT_PREFIX + commandId).apply();
        } else {
            preferences.edit().putString(SHORTCUT_PREFIX + commandId, normalized).apply();
        }
    }

    @NonNull
    public String getCustomShortcut(@NonNull String commandId) {
        return preferences.getString(SHORTCUT_PREFIX + commandId, "");
    }

    public boolean isShortcutAvailable(@NonNull String commandId,
                                       @NonNull String shortcut,
                                       @NonNull List<OmegaCommand> catalog) {
        String normalized = shortcut.trim();
        if (normalized.isEmpty()) return false;
        for (OmegaCommand command : catalog) {
            if (command.getId().equals(commandId)) continue;
            if (normalized.equalsIgnoreCase(command.getShortcut() == null ? "" : command.getShortcut())) return false;
            if (normalized.equalsIgnoreCase(getCustomShortcut(command.getId()))) return false;
        }
        return true;
    }

    @NonNull
    public List<String> favoriteCommandIds() { return new ArrayList<>(favorites()); }

    private LinkedHashSet<String> favorites() { return new LinkedHashSet<>(readList(FAVORITES)); }
    private LinkedHashSet<String> recents() { return new LinkedHashSet<>(readList(RECENTS)); }

    @NonNull
    private List<String> readList(@NonNull String key) {
        String encoded = preferences.getString(key, "");
        ArrayList<String> values = new ArrayList<>();
        if (encoded == null || encoded.isEmpty()) return values;
        for (String item : encoded.split("\\n", -1)) if (!item.isEmpty()) values.add(item);
        return values;
    }

    private void putList(@NonNull String key, @NonNull LinkedHashSet<String> values) {
        StringBuilder encoded = new StringBuilder();
        for (String value : values) {
            if (value.indexOf('\n') >= 0) continue;
            if (encoded.length() > 0) encoded.append('\n');
            encoded.append(value);
        }
        preferences.edit().putString(key, encoded.toString()).apply();
    }
}
