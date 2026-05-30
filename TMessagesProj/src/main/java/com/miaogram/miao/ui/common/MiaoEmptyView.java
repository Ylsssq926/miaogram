/*
 * MiaoGram custom code.
 * Builds a consistent empty-state view (icon + title + subtitle) for the
 * MiaoGram feature screens, so they don't fall back to a single bare line of
 * gray text.
 */
package com.miaogram.miao.ui.common;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public final class MiaoEmptyView {

    private MiaoEmptyView() {
        // utility class
    }

    /**
     * Creates a centered empty-state view: an icon, a bold title and a
     * multi-line subtitle, themed to the current palette.
     */
    public static LinearLayout create(Context context, int iconRes, CharSequence title, CharSequence subtitle) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(AndroidUtilities.dp(36), 0, AndroidUtilities.dp(36), 0);

        ImageView icon = new ImageView(context);
        icon.setImageResource(iconRes);
        icon.setColorFilter(new PorterDuffColorFilter(
                Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon), PorterDuff.Mode.SRC_IN));
        icon.setAlpha(0.5f);
        layout.addView(icon, LayoutHelper.createLinear(72, 72, Gravity.CENTER_HORIZONTAL));

        TextView titleView = new TextView(context);
        titleView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        titleView.setTypeface(AndroidUtilities.bold());
        titleView.setGravity(Gravity.CENTER);
        titleView.setText(title);
        layout.addView(titleView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL, 0, 12, 0, 0));

        TextView subtitleView = new TextView(context);
        subtitleView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        subtitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        subtitleView.setGravity(Gravity.CENTER);
        subtitleView.setText(subtitle);
        layout.addView(subtitleView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT,
                Gravity.CENTER_HORIZONTAL, 0, 6, 0, 0));

        return layout;
    }
}
