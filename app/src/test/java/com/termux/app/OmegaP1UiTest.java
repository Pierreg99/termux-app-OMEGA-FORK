package com.termux.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.termux.R;
import com.termux.app.terminal.OmegaSessionState;
import com.termux.app.terminal.OmegaWindowInsets;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class OmegaP1UiTest {

    @Test
    public void sessionStateModel_isExplicitAndColorIndependent() {
        Assert.assertEquals(OmegaSessionState.ACTIVE, OmegaSessionState.resolve(true, true, 0));
        Assert.assertEquals(OmegaSessionState.BACKGROUND, OmegaSessionState.resolve(false, true, 0));
        Assert.assertEquals(OmegaSessionState.EXITED, OmegaSessionState.resolve(false, false, 1));
        Assert.assertEquals("ACTIVE", OmegaSessionState.ACTIVE.getLabel());
        Assert.assertEquals("BACKGROUND", OmegaSessionState.BACKGROUND.getLabel());
        Assert.assertEquals("EXITED", OmegaSessionState.EXITED.getLabel());
    }

    @Test
    public void windowInsetsPolicy_usesLargestBottomInset() {
        WindowInsetsCompat insets = new WindowInsetsCompat.Builder()
            .setInsets(WindowInsetsCompat.Type.systemBars(), Insets.of(0, 0, 0, 24))
            .setInsets(WindowInsetsCompat.Type.ime(), Insets.of(0, 0, 0, 180))
            .build();

        Assert.assertEquals(180, OmegaWindowInsets.resolveBottomInset(insets));
    }

    @Test
    public void sessionRowLayout_exposesStateAndAccessibleTouchSurface() {
        View row = LayoutInflater.from(RuntimeEnvironment.getApplication())
            .inflate(R.layout.item_terminal_sessions_list, null, false);

        Assert.assertNotNull(row.findViewById(R.id.session_title));
        Assert.assertNotNull(row.findViewById(R.id.session_state));
        Assert.assertTrue(row.getMinimumHeight() >= 72);
    }

    @Test
    public void activityLayout_containsP1TerminalSurfaces() {
        View root = LayoutInflater.from(RuntimeEnvironment.getApplication())
            .inflate(R.layout.activity_termux, null, false);

        Assert.assertNotNull(root.findViewById(R.id.drawer_layout));
        Assert.assertNotNull(root.findViewById(R.id.terminal_view));
        Assert.assertNotNull(root.findViewById(R.id.terminal_sessions_list));
        Assert.assertNotNull(root.findViewById(R.id.toggle_keyboard_button));
        Assert.assertNotNull(root.findViewById(R.id.new_session_button));
        Assert.assertNotNull(root.findViewById(R.id.terminal_toolbar_view_pager));
        Assert.assertTrue(root.findViewById(R.id.activity_termux_bottom_space_view) instanceof ViewGroup == false);
    }
}
