/*
 * MiaoGram custom code.
 * Base class for all MiaoGram settings pages.
 *
 * Extends Telegram's BaseFragment to inherit the standard fragment lifecycle
 * (back button handling, action bar, theme, transitions).
 *
 * Subclasses should:
 *   - Override getFragmentTitle() to provide the action bar title.
 *   - Override createView(Context) to build their UI (or override buildRows()
 *     once we have a generic preference adapter, see preferences/adapter/).
 *
 * Why a thin base class instead of using BaseFragment directly?
 *   - Centralizes the action bar setup (back button, title) so each page
 *     doesn't repeat boilerplate.
 *   - Provides a single place to add cross-cutting concerns later
 *     (e.g., MiaoGram-specific telemetry, theme overrides).
 */
package com.miaogram.miao.preferences;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public abstract class MiaoBasePreferencesEntry extends BaseFragment {

    /** Subclasses provide the action bar title. */
    @NonNull
    protected abstract String getFragmentTitle();

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(org.telegram.messenger.R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(getFragmentTitle());
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        fragmentView = frameLayout;

        View body = createBody(context);
        if (body != null) {
            frameLayout.addView(body, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        }

        return fragmentView;
    }

    /**
     * Subclasses build the page body here. May return null for a blank page
     * (useful as a placeholder during scaffolding).
     */
    protected View createBody(Context context) {
        return null;
    }
}
