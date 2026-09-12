package com.termux.app.terminal;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.termux.R;
import com.termux.app.TermuxActivity;
import com.termux.app.activities.HelpActivity;
import com.termux.app.activities.SettingsActivity;
import com.termux.shared.activity.ActivityUtils;
import com.termux.shared.termux.interact.TextInputDialogUtils;
import com.termux.shared.logger.Logger;
import com.termux.terminal.TerminalSession;

/** Bridges command ids to existing Termux activity/session controls without duplicating them. */
public final class OmegaCommandActionAdapter {
    private static final String LOG_TAG = "OmegaCommandActions";

    private final TermuxActivity activity;

    public OmegaCommandActionAdapter(@NonNull TermuxActivity activity) {
        this.activity = activity;
    }

    public boolean execute(@NonNull OmegaCommand command) {
        return execute(command.getId());
    }

    public boolean execute(@NonNull String commandId) {
        switch (commandId) {
            case "session.new":
                if (activity.getTermuxTerminalSessionClient() == null) return false;
                activity.getTermuxTerminalSessionClient().addNewSession(false, null);
                return true;
            case "session.next":
                return switchSession(true);
            case "session.previous":
                return switchSession(false);
            case "session.rename-current":
                return renameCurrentSession();
            case "session.close-current":
                return confirmKillCurrentSession();
            case "terminal.keyboard":
                View keyboard = activity.findViewById(R.id.toggle_keyboard_button);
                if (keyboard == null) return false;
                keyboard.performClick();
                return true;
            case "terminal.toolbar":
                activity.toggleTerminalToolbar();
                return true;
            case "terminal.keep-screen-on":
                toggleKeepScreenOn();
                return true;
            case "terminal.reset":
                TerminalSession session = activity.getCurrentSession();
                if (session == null) return false;
                session.reset();
                if (activity.getTermuxTerminalSessionClient() != null)
                    activity.getTermuxTerminalSessionClient().onResetTerminalSession();
                activity.showToast(activity.getString(R.string.msg_terminal_reset), true);
                return true;
            case "navigation.drawer":
                activity.getDrawer().open();
                return true;
            case "navigation.settings":
                ActivityUtils.startActivity(activity, new Intent(activity, SettingsActivity.class));
                return true;
            case "navigation.help":
                ActivityUtils.startActivity(activity, new Intent(activity, HelpActivity.class));
                return true;
            case "editing.paste":
                if (activity.getTermuxTerminalSessionClient() == null) return false;
                activity.getTermuxTerminalSessionClient().onPasteTextFromClipboard(activity.getCurrentSession());
                return true;
            case "editing.select-url":
                if (activity.getTermuxTerminalViewClient() == null) return false;
                activity.getTermuxTerminalViewClient().showUrlSelection();
                return true;
            case "diagnostics.report":
                if (activity.getTermuxTerminalViewClient() == null) return false;
                activity.getTermuxTerminalViewClient().reportIssueFromTranscript();
                return true;
            default:
                if (commandId.startsWith("session.select.")) return selectSession(commandId);
                Logger.logVerbose(LOG_TAG, "Unknown command: " + commandId);
                return false;
        }
    }

    private boolean switchSession(boolean forward) {
        if (activity.getTermuxTerminalSessionClient() == null || activity.getTermuxService() == null) return false;
        if (activity.getTermuxService().getTermuxSessionsSize() == 0) return false;
        activity.getTermuxTerminalSessionClient().switchToSession(forward);
        return true;
    }

    private boolean selectSession(@NonNull String commandId) {
        try {
            int position = Integer.parseInt(commandId.substring("session.select.".length())) - 1;
            if (position < 0 || activity.getTermuxService() == null || position >= activity.getTermuxService().getTermuxSessionsSize()) return false;
            if (activity.getTermuxTerminalSessionClient() == null) return false;
            activity.getTermuxTerminalSessionClient().switchToSession(position);
            return true;
        } catch (NumberFormatException e) {
            Logger.logVerbose(LOG_TAG, "Invalid session selector: " + commandId);
            return false;
        }
    }

    private boolean renameCurrentSession() {
        TerminalSession session = activity.getCurrentSession();
        if (session == null || activity.getTermuxTerminalSessionClient() == null) return false;
        activity.getTermuxTerminalSessionClient().renameSession(session);
        return true;
    }

    private boolean confirmKillCurrentSession() {
        final TerminalSession session = activity.getCurrentSession();
        if (session == null) return false;
        new AlertDialog.Builder(activity)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(R.string.title_confirm_kill_process)
            .setPositiveButton(android.R.string.yes, (dialog, id) -> {
                dialog.dismiss();
                session.finishIfRunning();
            })
            .setNegativeButton(android.R.string.no, null)
            .show();
        return true;
    }

    private void toggleKeepScreenOn() {
        int keepFlag = android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        if ((activity.getWindow().getAttributes().flags & keepFlag) != 0) {
            activity.getWindow().clearFlags(keepFlag);
            activity.showToast("Keep screen on: OFF", true);
        } else {
            activity.getWindow().addFlags(keepFlag);
            activity.showToast("Keep screen on: ON", true);
        }
    }
}
