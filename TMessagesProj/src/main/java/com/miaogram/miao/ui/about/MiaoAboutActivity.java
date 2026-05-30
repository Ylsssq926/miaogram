/*
 * MiaoGram custom code.
 * About / legal screen.
 *
 * Required for store compliance with the Telegram API terms: a third-party
 * client must clearly identify itself as unofficial and attribute the Telegram
 * API. Shows version info, the unofficial-client disclaimer, the
 * "powered by Telegram API" attribution, the GPL notice and a source link.
 */
package com.miaogram.miao.ui.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miaogram.miao.utils.MiaoBuildConfig;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MiaoAboutActivity extends BaseFragment {

    /** Public source/home page shown on the About screen. */
    private static final String SOURCE_URL = "https://github.com/Ylsssq926/miaogram";

    private static final int TYPE_SETTINGS = 1;
    private static final int TYPE_INFO = 2;

    private int rowCount;
    private int versionRow;
    private int sourceRow;
    private int unofficialInfoRow;
    private int poweredByInfoRow;
    private int licenseInfoRow;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString(R.string.MiaoAbout));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        buildRows();

        RecyclerListView listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(new ListAdapter(context));
        listView.setOnItemClickListener((view, position) -> {
            if (position == versionRow) {
                copyVersion();
            } else if (position == sourceRow) {
                openSource();
            }
        });

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        fragmentView = frameLayout;
        return fragmentView;
    }

    private void buildRows() {
        rowCount = 0;
        versionRow = rowCount++;
        sourceRow = rowCount++;
        unofficialInfoRow = rowCount++;
        poweredByInfoRow = rowCount++;
        licenseInfoRow = rowCount++;
    }

    private void copyVersion() {
        String version = LocaleController.formatString(R.string.MiaoAboutVersion,
                MiaoBuildConfig.getVersionName(), MiaoBuildConfig.getVersionCode());
        AndroidUtilities.addToClipboard(version);
        BulletinFactory.of(this).createCopyBulletin(
                LocaleController.getString(R.string.MiaoAboutVersionCopied)).show();
    }

    private void openSource() {
        if (getParentActivity() == null) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SOURCE_URL));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getParentActivity().startActivity(intent);
        } catch (Exception ignore) {
            // no browser available
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private final Context context;

        ListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return holder.getItemViewType() == TYPE_SETTINGS;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == versionRow || position == sourceRow) {
                return TYPE_SETTINGS;
            }
            return TYPE_INFO;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == TYPE_SETTINGS) {
                view = new TextSettingsCell(context);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else {
                view = new TextInfoPrivacyCell(context);
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == TYPE_SETTINGS) {
                TextSettingsCell cell = (TextSettingsCell) holder.itemView;
                if (position == versionRow) {
                    cell.setTextAndValue(
                            LocaleController.getString(R.string.MiaoAbout),
                            LocaleController.formatString(R.string.MiaoAboutVersion,
                                    MiaoBuildConfig.getVersionName(), MiaoBuildConfig.getVersionCode()),
                            true);
                } else if (position == sourceRow) {
                    cell.setText(LocaleController.getString(R.string.MiaoAboutSource), false);
                }
            } else {
                TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                if (position == unofficialInfoRow) {
                    cell.setText(LocaleController.getString(R.string.MiaoAboutUnofficial));
                } else if (position == poweredByInfoRow) {
                    cell.setText(LocaleController.getString(R.string.MiaoAboutPoweredBy));
                } else if (position == licenseInfoRow) {
                    cell.setText(LocaleController.getString(R.string.MiaoAboutLicense));
                }
            }
        }
    }
}
