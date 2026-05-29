/*
 * MiaoGram custom code.
 * Main settings page (the entry point users land on after tapping
 * "MiaoGram Settings" in Telegram's Profile/Me page).
 *
 * Stage 7: blank page with just the action bar — proves the navigation hook works.
 * Stage 8+: will host the first real toggle (multi-account ≥10), then grow
 *           into a list of categories (Account / Chat / UI / About).
 */
package com.miaogram.miao.preferences;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;

public class MiaoMainPreferences extends MiaoBasePreferencesEntry {

    @NonNull
    @Override
    protected String getFragmentTitle() {
        return LocaleController.getString(R.string.MiaoSettings);
    }

    @Override
    protected View createBody(Context context) {
        // Stage 7 placeholder: a centered "coming soon" hint.
        // Will be replaced by a RecyclerListView in Stage 8 when the first toggle is added.
        TextView hint = new TextView(context);
        hint.setText(LocaleController.getString(R.string.MiaoSettingsComingSoon));
        hint.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        hint.setTextSize(15);
        hint.setGravity(Gravity.CENTER);
        hint.setPadding(48, 48, 48, 48);
        return hint;
    }
}
