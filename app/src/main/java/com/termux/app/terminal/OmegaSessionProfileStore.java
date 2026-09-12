package com.termux.app.terminal;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Map;

/** Small persistence boundary for optional OMEGA session profiles. */
public final class OmegaSessionProfileStore {
    private static final String PREFS = "omega_session_profiles";
    private static final String KEY_PREFIX = "profile_";

    private final SharedPreferences preferences;

    public OmegaSessionProfileStore(@NonNull Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(@NonNull OmegaSessionProfile profile) {
        preferences.edit()
            .putString(key(profile.getId(), "name"), profile.getDisplayName())
            .putInt(key(profile.getId(), "font"), profile.getFontSize())
            .putString(key(profile.getId(), "theme"), profile.getThemePreset())
            .putString(key(profile.getId(), "cursor"), profile.getCursorStyle())
            .putInt(key(profile.getId(), "scrollback"), profile.getScrollbackLines())
            .putBoolean(key(profile.getId(), "keep_screen_on"), profile.isKeepScreenOn())
            .apply();
    }

    @NonNull
    public OmegaSessionProfile load(@NonNull String profileId) {
        if (!contains(profileId)) return OmegaSessionProfile.omegaDefault();
        return new OmegaSessionProfile(
            profileId,
            preferences.getString(key(profileId, "name"), profileId),
            preferences.getInt(key(profileId, "font"), OmegaSessionProfile.DEFAULT_FONT_SIZE),
            preferences.getString(key(profileId, "theme"), "omega-dark"),
            preferences.getString(key(profileId, "cursor"), "block"),
            preferences.getInt(key(profileId, "scrollback"), OmegaSessionProfile.DEFAULT_SCROLLBACK),
            preferences.getBoolean(key(profileId, "keep_screen_on"), false)
        );
    }

    public boolean contains(@NonNull String profileId) {
        return preferences.contains(key(profileId, "name"));
    }

    public void delete(@NonNull String profileId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key(profileId, "name"));
        editor.remove(key(profileId, "font"));
        editor.remove(key(profileId, "theme"));
        editor.remove(key(profileId, "cursor"));
        editor.remove(key(profileId, "scrollback"));
        editor.remove(key(profileId, "keep_screen_on"));
        editor.apply();
    }

    @NonNull
    public String[] profileIds() {
        String prefix = KEY_PREFIX;
        java.util.ArrayList<String> ids = new java.util.ArrayList<>();
        for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(prefix) || !key.endsWith("_name")) continue;
            String id = key.substring(prefix.length(), key.length() - "_name".length());
            if (!id.isEmpty()) ids.add(id);
        }
        return ids.toArray(new String[0]);
    }

    private static String key(@NonNull String profileId, @NonNull String field) {
        return KEY_PREFIX + profileId + "_" + field;
    }
}
