package com.termux.app.terminal;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Versioned JSON codec for OMEGA session/theme profiles. */
public final class OmegaSessionProfileCodec {
    public static final int CURRENT_VERSION = 1;
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
            JsonParser parser = new JsonParser(json);
            parser.expect('{');
            parser.expectString("version");
            parser.expect(':');
            int version = parser.readInt();
            if (version != CURRENT_VERSION) {
                throw new IllegalArgumentException("Unsupported OMEGA profile version: " + version);
            }
            parser.expect(',');
            parser.expectString("profiles");
            parser.expect(':');
            parser.expect('[');

            List<OmegaSessionProfile> profiles = new ArrayList<>();
            if (!parser.peek(']')) {
                do {
                    profiles.add(parser.readProfile());
                } while (parser.consume(','));
            }
            parser.expect(']');
            parser.expect('}');
            parser.ensureEnd();
            return profiles;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException e) {
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
                        quoted.append("\\u");
                        appendHex4(quoted, c);
                    } else {
                        quoted.append(c);
                    }
            }
        }
        return quoted.append('"').toString();
    }

    private static void appendHex4(@NonNull StringBuilder out, char value) {
        final char[] hex = "0123456789abcdef".toCharArray();
        out.append(hex[(value >> 12) & 0xF]);
        out.append(hex[(value >> 8) & 0xF]);
        out.append(hex[(value >> 4) & 0xF]);
        out.append(hex[value & 0xF]);
    }

    private static final class JsonParser {
        private final String input;
        private int index;

        JsonParser(@NonNull String input) {
            this.input = input;
        }

        void expect(char expected) {
            skipWhitespace();
            if (index >= input.length() || input.charAt(index) != expected) {
                throw error("Expected '" + expected + "'");
            }
            index++;
        }

        void expectString(@NonNull String expected) {
            String actual = readString();
            if (!expected.equals(actual)) throw error("Expected key '" + expected + "'");
        }

        boolean consume(char value) {
            skipWhitespace();
            if (index < input.length() && input.charAt(index) == value) {
                index++;
                return true;
            }
            return false;
        }

        boolean peek(char value) {
            skipWhitespace();
            return index < input.length() && input.charAt(index) == value;
        }

        int readInt() {
            skipWhitespace();
            int start = index;
            if (index < input.length() && input.charAt(index) == '-') index++;
            int digitStart = index;
            while (index < input.length() && Character.isDigit(input.charAt(index))) index++;
            if (digitStart == index) throw error("Expected integer");
            try {
                return Integer.parseInt(input.substring(start, index));
            } catch (NumberFormatException e) {
                throw error("Invalid integer");
            }
        }

        boolean readBoolean() {
            skipWhitespace();
            if (input.startsWith("true", index)) {
                index += 4;
                return true;
            }
            if (input.startsWith("false", index)) {
                index += 5;
                return false;
            }
            throw error("Expected boolean");
        }

        @NonNull
        String readString() {
            skipWhitespace();
            if (index >= input.length() || input.charAt(index) != '"') {
                throw error("Expected string");
            }
            index++;
            StringBuilder result = new StringBuilder();
            while (index < input.length()) {
                char c = input.charAt(index++);
                if (c == '"') return result.toString();
                if (c != '\\') {
                    if (c < 0x20) throw error("Unescaped control character");
                    result.append(c);
                    continue;
                }
                if (index >= input.length()) throw error("Incomplete escape");
                char escaped = input.charAt(index++);
                switch (escaped) {
                    case '"': result.append('"'); break;
                    case '\\': result.append('\\'); break;
                    case '/': result.append('/'); break;
                    case 'b': result.append('\b'); break;
                    case 'f': result.append('\f'); break;
                    case 'n': result.append('\n'); break;
                    case 'r': result.append('\r'); break;
                    case 't': result.append('\t'); break;
                    case 'u': result.append(readUnicode()); break;
                    default: throw error("Invalid escape");
                }
            }
            throw error("Unterminated string");
        }

        @NonNull
        OmegaSessionProfile readProfile() {
            expect('{');
            expectString("id");
            expect(':');
            String id = readString();
            expect(',');
            expectString("displayName");
            expect(':');
            String displayName = readString();
            expect(',');
            expectString("fontSize");
            expect(':');
            int fontSize = readInt();
            expect(',');
            expectString("themePreset");
            expect(':');
            String themePreset = readString();
            expect(',');
            expectString("cursorStyle");
            expect(':');
            String cursorStyle = readString();
            expect(',');
            expectString("scrollbackLines");
            expect(':');
            int scrollbackLines = readInt();
            expect(',');
            expectString("keepScreenOn");
            expect(':');
            boolean keepScreenOn = readBoolean();
            expect('}');
            return new OmegaSessionProfile(
                id, displayName, fontSize, themePreset, cursorStyle, scrollbackLines, keepScreenOn);
        }

        void ensureEnd() {
            skipWhitespace();
            if (index != input.length()) throw error("Trailing data");
        }

        private char readUnicode() {
            if (index + 4 > input.length()) throw error("Incomplete unicode escape");
            int value = 0;
            for (int i = 0; i < 4; i++) {
                char c = input.charAt(index++);
                int digit = Character.digit(c, 16);
                if (digit < 0) throw error("Invalid unicode escape");
                value = (value << 4) | digit;
            }
            return (char) value;
        }

        private void skipWhitespace() {
            while (index < input.length() && Character.isWhitespace(input.charAt(index))) index++;
        }

        @NonNull
        private IllegalArgumentException error(@NonNull String message) {
            return new IllegalArgumentException(message + " at character " + index);
        }
    }
}
