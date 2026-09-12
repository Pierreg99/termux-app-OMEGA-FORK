package com.termux.app.terminal;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.termux.R;
import com.termux.app.TermuxActivity;
import com.termux.app.activities.HelpActivity;
import com.termux.app.activities.SettingsActivity;
import com.termux.shared.activity.ActivityUtils;
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
                return moveSession(activity, 1);
            case "session.previous":
                return moveSession(activity, -1);
            case "terminal.keyboard":
                View keyboard = activity.findViewById(R.id.toggle_keyboard_button);
                if (keyboard != null) keyboard.performClick();
                return keyboard != null;
            case "terminal.toolbar":
                activity.toggleTerminalToolbar();
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
                Logger.logVerbose(LOG_TAG, "Paste command is reserved for the terminal view adapter");
                return false;
            case "editing.select-url":
                activity.getTermuxTerminalViewClient().showUrlSelection();
                return true;
            case "diagnostics.report":
                activity.getTermuxTerminalViewClient().reportIssueFromTranscript();
                return true;
            default:
                Logger.logVerbose(LOG_TAG, "Unknown command: " + commandId);
                return false;
        }
    }

    private static boolean moveSession(@NonNull TermuxActivity activity, int delta) {
        ListView list = activity.findViewById(R.id.terminal_sessions_list);
        if (list == null || list.getAdapter() == null || list.getAdapter().getCount() == 0) return false;

        int position = list.getCheckedItemPosition();
        if (position < 0 || position >= list.getAdapter().getCount()) position = list.getSelectedItemPosition();
        if (position < 0) position = 0;

        int count = list.getAdapter().getCount();
        int target = (position + delta) % count;
        if (target < 0) target += count;
        list.setSelection(target);
        return list.performItemClick(list.getAdapter().getView(target, null, list), target, list.getAdapter().getItemId(target));
    }
}
