package com.termux.app.terminal;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
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
        if (!contains(profileId)) return defaultForId(profileId);
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

    @NonNull
    public String exportAll() {
        ArrayList<OmegaSessionProfile> profiles = new ArrayList<>();
        for (String id : profileIds()) profiles.add(load(id));
        Collections.sort(profiles, java.util.Comparator.comparing(OmegaSessionProfile::getId));
        return OmegaSessionProfileCodec.exportProfiles(profiles);
    }

    public void importAll(@NonNull String json, boolean replaceExisting) {
        java.util.List<OmegaSessionProfile> profiles = OmegaSessionProfileCodec.importProfiles(json);
        if (replaceExisting) {
            for (String id : profileIds()) delete(id);
        }
        for (OmegaSessionProfile profile : profiles) save(profile);
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
        ArrayList<String> ids = new ArrayList<>();
        for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
            String preferenceKey = entry.getKey();
            if (!preferenceKey.startsWith(prefix) || !preferenceKey.endsWith("_name")) continue;
            String id = preferenceKey.substring(prefix.length(), preferenceKey.length() - "_name".length());
            if (!id.isEmpty()) ids.add(id);
        }
        Collections.sort(ids);
        return ids.toArray(new String[0]);
    }

    @NonNull
    private static OmegaSessionProfile defaultForId(@NonNull String profileId) {
        if (profileId.trim().isEmpty()) throw new IllegalArgumentException("Profile id must not be empty");
        return new OmegaSessionProfile(
            profileId,
            profileId,
            OmegaSessionProfile.DEFAULT_FONT_SIZE,
            "omega-dark",
            "block",
            OmegaSessionProfile.DEFAULT_SCROLLBACK,
            false);
    }

    private static String key(@NonNull String profileId, @NonNull String field) {
        return KEY_PREFIX + profileId + "_" + field;
    }
}
