package com.termux.app.terminal;

/** Semantic state model for OMEGA session rows. */
public enum OmegaSessionState {
    ACTIVE,
    BACKGROUND,
    EXITED;

    public static OmegaSessionState resolve(boolean current, boolean running, int exitStatus) {
        if (current && running) return ACTIVE;
        if (running) return BACKGROUND;
        return EXITED;
    }

    public String getLabel() {
        switch (this) {
            case ACTIVE:
                return "ACTIVE";
            case BACKGROUND:
                return "BACKGROUND";
            case EXITED:
                return "EXITED";
            default:
                return name();
        }
    }
}
