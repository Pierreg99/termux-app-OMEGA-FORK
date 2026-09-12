package com.termux.app.terminal;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Versioned JSON codec for OMEGA session/theme profiles. */
public final class OmegaSessionProfileCodec {
    public static final int CURRENT_VERSION = 1;
    private static final String ROOT_VERSION = "version";
    private static final String ROOT_PROFILES = "profiles";

    private OmegaSessionProfileCodec() { }

    @NonNull
    public static String exportProfiles(@NonNull Collection<OmegaSessionProfile> profiles) {
        try {
            List<OmegaSessionProfile> ordered = new ArrayList<>(profiles);
            Collections.sort(ordered, Comparator.comparing(OmegaSessionProfile::getId));

            JSONObject root = new JSONObject();
            root.put(ROOT_VERSION, CURRENT_VERSION);
            JSONArray entries = new JSONArray();
            for (OmegaSessionProfile profile : ordered) {
                entries.put(toJson(profile));
            }
            root.put(ROOT_PROFILES, entries);
            return root.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to export OMEGA profiles", e);
        }
    }

    @NonNull
    public static List<OmegaSessionProfile> importProfiles(@NonNull String json) {
        try {
            JSONObject root = new JSONObject(json);
            int version = root.getInt(ROOT_VERSION);
            if (version != CURRENT_VERSION) {
                throw new IllegalArgumentException("Unsupported OMEGA profile version: " + version);
            }
            JSONArray entries = root.getJSONArray(ROOT_PROFILES);
            List<OmegaSessionProfile> profiles = new ArrayList<>();
            for (int i = 0; i < entries.length(); i++) {
                profiles.add(fromJson(entries.getJSONObject(i)));
            }
            return profiles;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid OMEGA profile export", e);
        }
    }

    @NonNull
    private static JSONObject toJson(@NonNull OmegaSessionProfile profile) throws Exception {
        JSONObject object = new JSONObject();
        object.put("id", profile.getId());
        object.put("displayName", profile.getDisplayName());
        object.put("fontSize", profile.getFontSize());
        object.put("themePreset", profile.getThemePreset());
        object.put("cursorStyle", profile.getCursorStyle());
        object.put("scrollbackLines", profile.getScrollbackLines());
        object.put("keepScreenOn", profile.isKeepScreenOn());
        return object;
    }

    @NonNull
    private static OmegaSessionProfile fromJson(@NonNull JSONObject object) {
        String id = object.getString("id");
        String displayName = object.getString("displayName");
        int fontSize = object.getInt("fontSize");
        String themePreset = object.getString("themePreset");
        String cursorStyle = object.getString("cursorStyle");
        int scrollbackLines = object.getInt("scrollbackLines");
        boolean keepScreenOn = object.getBoolean("keepScreenOn");
        return new OmegaSessionProfile(
            id, displayName, fontSize, themePreset, cursorStyle, scrollbackLines, keepScreenOn);
    }
}
