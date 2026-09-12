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
        List<OmegaSessionProfile> ordered = new ArrayList<>(profiles);
        Collections.sort(ordered, Comparator.comparing(OmegaSessionProfile::getId));

        StringBuilder json = new StringBuilder("{\"version\":1,\"profiles\":[");
        for (int i = 0; i < ordered.size(); i++) {
            if (i > 0) json.append(',');
            appendProfile(json, ordered.get(i));
        }
        return json.append("]}").toString();
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

    private static void appendProfile(@NonNull StringBuilder json, @NonNull OmegaSessionProfile profile) {
        json.append('{')
            .append("\"id\":").append(quote(profile.getId()))
            .append(",\"displayName\":").append(quote(profile.getDisplayName()))
            .append(",\"fontSize\":").append(profile.getFontSize())
            .append(",\"themePreset\":").append(quote(profile.getThemePreset()))
            .append(",\"cursorStyle\":").append(quote(profile.getCursorStyle()))
            .append(",\"scrollbackLines\":").append(profile.getScrollbackLines())
            .append(",\"keepScreenOn\":").append(profile.isKeepScreenOn())
            .append('}');
    }

    @NonNull
    private static String quote(@NonNull String value) {
        StringBuilder quoted = new StringBuilder(value.length() + 2).append('"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"': quoted.append("\\\""); break;
                case '\\': quoted.append("\\\\"); break;
                case '\b': quoted.append("\\b"); break;
                case '\f': quoted.append("\\f"); break;
                case '\n': quoted.append("\\n"); break;
                case '\r': quoted.append("\\r"); break;
                case '\t': quoted.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        quoted.append(String.format("\\u%04x", (int) c));
                    } else {
                        quoted.append(c);
                    }
            }
        }
        return quoted.append('"').toString();
    }

    @NonNull
    private static OmegaSessionProfile fromJson(@NonNull JSONObject object) throws Exception {
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
