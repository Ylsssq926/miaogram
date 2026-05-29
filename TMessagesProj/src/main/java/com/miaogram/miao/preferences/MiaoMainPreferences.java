/*
 * MiaoGram custom code.
 * Main settings page (the entry point users land on after tapping
 * "MiaoGram Settings" in Telegram's Profile/Me page).
 *
 * Hosts MiaoGram feature toggles, grouped into sections.
 * Built with a RecyclerListView + TextCheckCell, matching upstream settings pages.
 *
 * Adding a new toggle:
 *   1. Add a row-id field + assign it in buildRows().
 *   2. Map it to its Flag in flagForRow().
 *   3. Render its title/subtitle in onBindViewHolder.
 */
package com.miaogram.miao.preferences;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miaogram.miao.MiaoConfig;
import com.miaogram.miao.flags.Flag;
import com.miaogram.miao.flags.Flags;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MiaoMainPreferences extends MiaoBasePreferencesEntry {

    // Row ids
    private int rowCount;
    private int accountHeaderRow;
    private int maxAccountsRow;
    private int accountRemarkRow;
    private int accountInfoRow;
    private int folderHeaderRow;
    private int hideAllChatsRow;
    private int folderInfoRow;

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
            Flag flag = flagForRow(position);
            if (flag != null && view instanceof TextCheckCell) {
                boolean newValue = !flag.isEnabled();
                MiaoConfig.setFeatureEnabled(flag, newValue);
                ((TextCheckCell) view).setChecked(newValue);
            }
        });

        FrameLayout container = new FrameLayout(context);
        container.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        return container;
    }

    private void buildRows() {
        rowCount = 0;
        accountHeaderRow = rowCount++;
        maxAccountsRow = rowCount++;
        accountRemarkRow = rowCount++;
        accountInfoRow = rowCount++;
        folderHeaderRow = rowCount++;
        hideAllChatsRow = rowCount++;
        folderInfoRow = rowCount++;
    }

    /** Maps a toggle row to its backing Flag, or null for non-toggle rows. */
    @Nullable
    private Flag flagForRow(int position) {
        if (position == maxAccountsRow) return Flags.MIAO_AC_1;
        if (position == accountRemarkRow) return Flags.MIAO_AC_2;
        if (position == hideAllChatsRow) return Flags.MIAO_UI_3;
        return null;
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private static final int TYPE_CHECK = 0;
        private static final int TYPE_INFO = 1;
        private static final int TYPE_HEADER = 2;

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
            if (flagForRow(position) != null) {
                return TYPE_CHECK;
            }
            if (position == accountHeaderRow || position == folderHeaderRow) {
                return TYPE_HEADER;
            }
            return TYPE_INFO;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == TYPE_CHECK) {
                view = new TextCheckCell(context);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else if (viewType == TYPE_HEADER) {
                view = new HeaderCell(context);
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else {
                view = new TextInfoPrivacyCell(context);
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case TYPE_CHECK: {
                    TextCheckCell cell = (TextCheckCell) holder.itemView;
                    if (position == maxAccountsRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoMaxAccounts),
                                LocaleController.getString(R.string.MiaoMaxAccountsInfo),
                                Flags.MIAO_AC_1.isEnabled(), true, true);
                    } else if (position == accountRemarkRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoAccountRemark),
                                LocaleController.getString(R.string.MiaoAccountRemarkInfo),
                                Flags.MIAO_AC_2.isEnabled(), true, false);
                    } else if (position == hideAllChatsRow) {
                        cell.setTextAndValueAndCheck(
                                LocaleController.getString(R.string.MiaoHideAllChats),
                                LocaleController.getString(R.string.MiaoHideAllChatsInfo),
                                Flags.MIAO_UI_3.isEnabled(), true, false);
                    }
                    break;
                }
                case TYPE_HEADER: {
                    HeaderCell cell = (HeaderCell) holder.itemView;
                    if (position == accountHeaderRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoAccountSectionInfo));
                    } else if (position == folderHeaderRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoFolderSectionInfo));
                    }
                    break;
                }
                default: {
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == accountInfoRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoMaxAccountsHint));
                    } else if (position == folderInfoRow) {
                        cell.setText(LocaleController.getString(R.string.MiaoHideAllChatsHint));
                    }
                    break;
                }
            }
        }
    }
}
