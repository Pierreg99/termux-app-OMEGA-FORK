package com.termux.app.terminal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.termux.R;

/** Transparent spacer that keeps terminal content above system/IME insets. */
public final class OmegaInsetSpacerView extends android.view.View {

    public OmegaInsetSpacerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(android.graphics.Color.TRANSPARENT);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        int baseHeight = getResources().getDimensionPixelSize(R.dimen.omega_inset_spacer_base);
        OmegaWindowInsets.installBottomSpacerPolicy(this, baseHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT && params.height != ViewGroup.LayoutParams.MATCH_PARENT) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(0, params.height));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
