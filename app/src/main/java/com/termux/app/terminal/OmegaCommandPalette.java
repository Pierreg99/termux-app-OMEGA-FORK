package com.termux.app.terminal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.termux.app.TermuxActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** Searchable keyboard-first command palette for OMEGA. */
public final class OmegaCommandPalette extends Dialog {
    private final TermuxActivity activity;
    private final OmegaCommandActionAdapter actionAdapter;
    private final OmegaCommandPreferenceStore preferences;
    private final OmegaCommandState state = new OmegaCommandState();
    private final EditText search;
    private final LinearLayout results;
    private final ScrollView resultsScroll;
    private List<OmegaCommand> filtered = new ArrayList<>();

    public OmegaCommandPalette(@NonNull Context context) {
        super(context);
        if (!(context instanceof TermuxActivity)) {
            throw new IllegalArgumentException("OmegaCommandPalette requires TermuxActivity context");
        }
        activity = (TermuxActivity) context;
        actionAdapter = new OmegaCommandActionAdapter(activity);
        preferences = new OmegaCommandPreferenceStore(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(12), dp(16), dp(12));
        root.setBackground(roundBackground(0xEE101820, 18));

        TextView title = new TextView(context);
        title.setText("OMEGA COMMAND CENTER");
        title.setContentDescription("OMEGA Command Center");
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setTextSize(12);
        title.setLetterSpacing(0.08f);
        title.setPadding(0, 0, 0, dp(8));
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        search = new EditText(context);
        search.setSingleLine(true);
        search.setHint("Search commands…");
        search.setContentDescription("Search OMEGA commands");
        search.setTextSize(16);
        search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        search.setPadding(dp(12), 0, dp(12), 0);
        search.setSelectAllOnFocus(false);
        root.addView(search, new LinearLayout.LayoutParams(-1, dp(48)));

        TextView hint = new TextView(context);
        hint.setText("↑ ↓ navigate   Enter execute   Long-press favorite   Esc close");
        hint.setTextSize(11);
        hint.setAlpha(0.7f);
        hint.setPadding(0, dp(8), 0, 0);

        resultsScroll = new ScrollView(context);
        resultsScroll.setContentDescription("OMEGA command results");
        results = new LinearLayout(context);
        results.setOrientation(LinearLayout.VERTICAL);
        resultsScroll.addView(results, new ScrollView.LayoutParams(-1, -2));
        LinearLayout.LayoutParams resultsParams = new LinearLayout.LayoutParams(-1, 0, 1f);
        resultsParams.topMargin = dp(8);
        root.addView(resultsScroll, resultsParams);
        root.addView(hint, new LinearLayout.LayoutParams(-1, -2));

        setContentView(root);
        refresh();

        search.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                state.setQuery(s.toString());
                refresh();
            }
            @Override public void afterTextChanged(android.text.Editable s) { }
        });
        search.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) return false;
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                state.moveSelection(1, filtered.size());
                refreshSelection();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                state.moveSelection(-1, filtered.size());
                refreshSelection();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                executeSelected();
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_ESCAPE) {
                dismiss();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Window window = getWindow();
        if (window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = Math.min((int) (activity.getResources().getDisplayMetrics().widthPixels * 0.92f), dp(640));
        params.height = Math.min((int) (activity.getResources().getDisplayMetrics().heightPixels * 0.82f), dp(720));
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params.y = dp(48);
        window.setAttributes(params);
        search.requestFocus();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void refresh() {
        String query = state.getQuery().trim().toLowerCase(Locale.ROOT);
        int sessionCount = activity.getTermuxService() == null ? 0 : activity.getTermuxService().getTermuxSessionsSize();
        List<OmegaCommand> catalog = OmegaCommandRegistry.sessionAwareCommands(sessionCount);
        filtered = new ArrayList<>();
        for (OmegaCommand command : catalog) {
            String haystack = (command.getId() + " " + command.getTitle() + " " + command.getCategory().name()).toLowerCase(Locale.ROOT);
            if (query.isEmpty() || haystack.contains(query)) filtered.add(command);
        }
        final List<String> order = preferences.orderedCommandIds();
        final List<String> favorites = preferences.favoriteCommandIds();
        filtered.sort(Comparator
            .comparing((OmegaCommand c) -> !favorites.contains(c.getId()))
            .thenComparingInt(c -> {
                int index = order.indexOf(c.getId());
                return index < 0 ? Integer.MAX_VALUE : index;
            })
            .thenComparing(OmegaCommand::getTitle));
        if (state.getSelectedIndex() >= filtered.size()) state.resetSelection();

        results.removeAllViews();
        for (int i = 0; i < filtered.size(); i++) {
            OmegaCommand command = filtered.get(i);
            TextView item = new TextView(getContext());
            boolean favorite = favorites.contains(command.getId());
            String shortcut = preferences.getCustomShortcut(command.getId());
            if (shortcut.isEmpty()) shortcut = command.getShortcut();
            String marker = favorite ? "★ " : "";
            String label = marker + command.getTitle() + (shortcut == null ? "" : "    " + shortcut);
            item.setText(label);
            item.setContentDescription((favorite ? "Favorite, " : "") + command.getTitle() + " command");
            item.setTextSize(15);
            item.setGravity(Gravity.CENTER_VERTICAL);
            item.setFocusable(true);
            item.setClickable(true);
            item.setLongClickable(true);
            item.setPadding(dp(12), 0, dp(12), 0);
            final int index = i;
            item.setOnClickListener(v -> {
                state.resetSelection();
                state.moveSelection(index, filtered.size());
                executeSelected();
            });
            item.setOnLongClickListener(v -> {
                preferences.setFavorite(command.getId(), !preferences.isFavorite(command.getId()));
                Toast.makeText(getContext(), preferences.isFavorite(command.getId())
                    ? "Favorite added: " + command.getTitle()
                    : "Favorite removed: " + command.getTitle(), Toast.LENGTH_SHORT).show();
                refresh();
                return true;
            });
            results.addView(item, new LinearLayout.LayoutParams(-1, dp(48)));
        }
        refreshSelection();
    }

    private void refreshSelection() {
        for (int i = 0; i < results.getChildCount(); i++) {
            View child = results.getChildAt(i);
            child.setSelected(i == state.getSelectedIndex());
            child.setBackground(i == state.getSelectedIndex() ? roundBackground(0x663B82F6, 10) : null);
        }
        if (state.getSelectedIndex() < results.getChildCount()) {
            results.getChildAt(state.getSelectedIndex()).requestFocus();
            resultsScroll.smoothScrollTo(0, state.getSelectedIndex() * dp(48));
        }
    }

    private void executeSelected() {
        if (filtered.isEmpty()) return;
        int index = Math.max(0, Math.min(state.getSelectedIndex(), filtered.size() - 1));
        OmegaCommand command = filtered.get(index);
        preferences.recordRecent(command.getId());
        if (actionAdapter.execute(command)) dismiss();
    }

    private GradientDrawable roundBackground(int color, int radiusDp) {
        GradientDrawable background = new GradientDrawable();
        background.setColor(color);
        background.setCornerRadius(dp(radiusDp));
        return background;
    }

    private int dp(int value) {
        return Math.round(value * getContext().getResources().getDisplayMetrics().density);
    }
}
