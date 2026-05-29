/*
 * MiaoGram custom code.
 * Main settings page (the entry point users land on after tapping
 * "MiaoGram Settings" in Telegram's Profile/Me page).
 *
 * Stage 8: hosts the first real toggle — multi-account limit (MIAO_AC_1).
 * Built with a RecyclerListView + TextCheckCell, matching upstream settings pages.
 */
package com.miaogram.miao.preferences;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miaogram.miao.MiaoConfig;
import com.miaogram.miao.flags.Flags;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MiaoMainPreferences extends MiaoBasePreferencesEntry {

    // Row ids
    private int rowCount;
    private int accountHeaderInfoRow;
    private int maxAccountsRow;
    private int maxAccountsInfoRow;

    @NonNull
    @Override
    protected String getFragmentTitle() {
        return LocaleController.getString(R.string.MiaoSettings);
    }

    @Override
    protected View createBody(Context context) {
        buildRows();

        RecyclerListView listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(new ListAdapter(context));
        listView.setOnItemClickListener((view, position) -> {
            if (position == maxAccountsRow) {
                boolean newValue = !Flags.MIAO_AC_1.isEnabled();
                MiaoConfig.setFeatureEnabled(Flags.MIAO_AC_1, newValue);
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(newValue);
                }
            }
        });

        FrameLayout container = new FrameLayout(context);
        container.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        return container;
    }

    private void buildRows() {
        rowCount = 0;
        accountHeaderInfoRow = rowCount++;
        maxAccountsRow = rowCount++;
        maxAccountsInfoRow = rowCount++;
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private static final int TYPE_CHECK = 0;
        private static final int TYPE_INFO = 1;

        private final Context context;

        ListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return holder.getItemViewType() == TYPE_CHECK;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == maxAccountsRow) {
                return TYPE_CHECK;
            }
            return TYPE_INFO;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            View view;
            if (viewType == TYPE_CHECK) {
                view = new TextCheckCell(context);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else {
                view = new TextInfoPrivacyCell(context);
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder.getItemViewType() == TYPE_CHECK) {
                TextCheckCell cell = (TextCheckCell) holder.itemView;
                if (position == maxAccountsRow) {
                    cell.setTextAndValueAndCheck(
                            LocaleController.getString(R.string.MiaoMaxAccounts),
                            LocaleController.getString(R.string.MiaoMaxAccountsInfo),
                            Flags.MIAO_AC_1.isEnabled(),
                            true,
                            false);
                }
            } else {
                TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                if (position == accountHeaderInfoRow) {
                    cell.setText(LocaleController.getString(R.string.MiaoAccountSectionInfo));
                } else if (position == maxAccountsInfoRow) {
                    cell.setText(LocaleController.getString(R.string.MiaoMaxAccountsHint));
                }
            }
        }
    }
}
