package com.termux.app.terminal;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.termux.R;
import com.termux.app.TermuxActivity;
import com.termux.shared.termux.shell.command.runner.terminal.TermuxSession;
import com.termux.terminal.TerminalSession;

import java.util.List;

public class TermuxSessionsListViewController extends ArrayAdapter<TermuxSession> implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    final TermuxActivity mActivity;

    final StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
    final StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);

    public TermuxSessionsListViewController(TermuxActivity activity, List<TermuxSession> sessionList) {
        super(activity.getApplicationContext(), R.layout.item_terminal_sessions_list, sessionList);
        this.mActivity = activity;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View sessionRowView = convertView;
        if (sessionRowView == null) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            sessionRowView = inflater.inflate(R.layout.item_terminal_sessions_list, parent, false);
        }

        TextView sessionTitleView = sessionRowView.findViewById(R.id.session_title);
        TextView sessionStateView = sessionRowView.findViewById(R.id.session_state);

        TermuxSession termuxSession = getItem(position);
        TerminalSession sessionAtRow = termuxSession != null ? termuxSession.getTerminalSession() : null;
        if (sessionAtRow == null) {
            sessionTitleView.setText("null session");
            sessionStateView.setText(OmegaSessionState.EXITED.getLabel());
            return sessionRowView;
        }

        String name = sessionAtRow.mSessionName;
        String sessionTitle = sessionAtRow.getTitle();

        String numberPart = "[" + (position + 1) + "] ";
        String sessionNamePart = TextUtils.isEmpty(name) ? "" : name;
        String sessionTitlePart = TextUtils.isEmpty(sessionTitle)
            ? ""
            : ((sessionNamePart.isEmpty() ? "" : "\n") + sessionTitle);

        String fullSessionTitle = numberPart + sessionNamePart + sessionTitlePart;
        SpannableString fullSessionTitleStyled = new SpannableString(fullSessionTitle);
        fullSessionTitleStyled.setSpan(boldSpan, 0, numberPart.length() + sessionNamePart.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (numberPart.length() + sessionNamePart.length() < fullSessionTitle.length()) {
            fullSessionTitleStyled.setSpan(italicSpan, numberPart.length() + sessionNamePart.length(), fullSessionTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        sessionTitleView.setText(fullSessionTitleStyled);
        sessionTitleView.setPaintFlags(sessionTitleView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

        boolean sessionRunning = sessionAtRow.isRunning();
        boolean isCurrent = mActivity.getCurrentSession() == sessionAtRow;
        OmegaSessionState state = OmegaSessionState.resolve(isCurrent, sessionRunning, sessionAtRow.getExitStatus());
        sessionStateView.setText(state.getLabel());

        int stateColor;
        switch (state) {
            case ACTIVE:
                stateColor = ContextCompat.getColor(mActivity, R.color.omega_success);
                break;
            case BACKGROUND:
                stateColor = ContextCompat.getColor(mActivity, R.color.omega_warning);
                break;
            case EXITED:
            default:
                stateColor = sessionAtRow.getExitStatus() == 0
                    ? ContextCompat.getColor(mActivity, R.color.omega_on_surface)
                    : ContextCompat.getColor(mActivity, R.color.omega_error);
                sessionTitleView.setPaintFlags(sessionTitleView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
        }

        sessionStateView.setTextColor(stateColor);
        sessionTitleView.setTextColor(ContextCompat.getColor(mActivity, R.color.omega_on_background));
        sessionRowView.setContentDescription(fullSessionTitle + ", " + state.getLabel());

        return sessionRowView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TermuxSession clickedSession = getItem(position);
        if (clickedSession == null) return;
        mActivity.getTermuxTerminalSessionClient().setCurrentSession(clickedSession.getTerminalSession());
        mActivity.getDrawer().closeDrawers();
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final TermuxSession selectedSession = getItem(position);
        if (selectedSession == null) return true;
        mActivity.getTermuxTerminalSessionClient().renameSession(selectedSession.getTerminalSession());
        return true;
    }

}
