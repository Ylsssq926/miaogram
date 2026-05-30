/*
 * MiaoGram custom code.
 * Channel-reader screen (MIAO_AC_4).
 *
 * Treats the current account's broadcast channels like an RSS reader: the user
 * grades each channel MUST-READ / SCAN / ARCHIVED (long-press a row) and this
 * screen groups them into collapsible sections so dozens of channels stop being
 * one flat wall of unread badges.
 *
 * Must-read is always expanded; scan / archived / ungraded are collapsible.
 * Rows reuse upstream DialogCell (current account only, so the default
 * constructor account is correct). Tapping a channel opens it.
 *
 * Pure local: grades live in MiaoSharedPrefs via ChannelGrading; no server
 * calls, no mutation of Telegram data.
 */
package com.miaogram.miao.ui.reader;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miaogram.miao.feature.reader.ChannelGrading;
import com.miaogram.miao.feature.reader.ChannelReaderCollector;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Cells.GraySectionCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.DialogsActivity;

import java.util.ArrayList;
import java.util.List;

public class ChannelReaderActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {

    private static final int VIEW_TYPE_CHANNEL = 0;
    private static final int VIEW_TYPE_SECTION = 1;

    // Collapsed state per section (must-read is never collapsed).
    private boolean scanCollapsed = false;
    private boolean archivedCollapsed = true;
    private boolean ungradedCollapsed = false;

    private RecyclerListView listView;
    private ListAdapter adapter;
    private TextView emptyView;

    private ChannelReaderCollector.Result data;
    private final List<Row> rows = new ArrayList<>();

    private final int[] observedEvents = {
            NotificationCenter.dialogsNeedReload,
            NotificationCenter.updateInterfaces,
            NotificationCenter.dialogsUnreadCounterChanged,
            NotificationCenter.notificationsCountUpdated,
    };

    /** A display row: either a section header or a channel item. */
    private static final class Row {
        final boolean isSection;
        final int section;            // ChannelGrading.LEVEL_* for section rows
        final CharSequence title;     // section title
        final ChannelReaderCollector.Item item; // channel rows

        static Row section(int level, CharSequence title) {
            return new Row(true, level, title, null);
        }

        static Row channel(ChannelReaderCollector.Item item) {
            return new Row(false, 0, null, item);
        }

        private Row(boolean isSection, int section, CharSequence title, ChannelReaderCollector.Item item) {
            this.isSection = isSection;
            this.section = section;
            this.title = title;
            this.item = item;
        }
    }

    @Override
    public boolean onFragmentCreate() {
        NotificationCenter nc = NotificationCenter.getInstance(currentAccount);
        for (int event : observedEvents) {
            nc.addObserver(this, event);
        }
        return super.onFragmentCreate();
    }

    @Override
    public void onFragmentDestroy() {
        NotificationCenter nc = NotificationCenter.getInstance(currentAccount);
        for (int event : observedEvents) {
            nc.removeObserver(this, event);
        }
        super.onFragmentDestroy();
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString(R.string.MiaoChannelReader));
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

        emptyView = new TextView(context);
        emptyView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        emptyView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 15);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setText(LocaleController.getString(R.string.MiaoChannelReaderEmpty));
        emptyView.setVisibility(View.GONE);
        frameLayout.addView(emptyView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER));

        listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        adapter = new ListAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((view, position) -> {
            if (position < 0 || position >= rows.size()) {
                return;
            }
            Row row = rows.get(position);
            if (row.isSection) {
                toggleSection(row.section);
            } else {
                openChannel(row.item);
            }
        });
        listView.setOnItemLongClickListener((view, position) -> {
            if (position < 0 || position >= rows.size()) {
                return false;
            }
            Row row = rows.get(position);
            if (!row.isSection) {
                showGradePicker(row.item);
                return true;
            }
            return false;
        });
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        reload();
        return fragmentView;
    }

    private void toggleSection(int level) {
        if (level == ChannelGrading.LEVEL_SCAN) {
            scanCollapsed = !scanCollapsed;
        } else if (level == ChannelGrading.LEVEL_ARCHIVED) {
            archivedCollapsed = !archivedCollapsed;
        } else if (level == ChannelGrading.LEVEL_UNSET) {
            ungradedCollapsed = !ungradedCollapsed;
        }
        rebuildRows();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void reload() {
        if (ChannelReaderCollector.isEnabled()) {
            data = ChannelReaderCollector.collect(currentAccount);
        } else {
            data = null;
        }
        rebuildRows();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (emptyView != null) {
            emptyView.setVisibility(rows.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void rebuildRows() {
        rows.clear();
        if (data == null) {
            return;
        }
        appendSection(ChannelGrading.LEVEL_MUST_READ, data.mustRead, false,
                LocaleController.getString(R.string.MiaoChannelMustRead));
        appendSection(ChannelGrading.LEVEL_SCAN, data.scan, scanCollapsed,
                LocaleController.getString(R.string.MiaoChannelScan));
        appendSection(ChannelGrading.LEVEL_UNSET, data.ungraded, ungradedCollapsed,
                LocaleController.getString(R.string.MiaoChannelUngraded));
        appendSection(ChannelGrading.LEVEL_ARCHIVED, data.archived, archivedCollapsed,
                LocaleController.getString(R.string.MiaoChannelArchived));
    }

    private void appendSection(int level, List<ChannelReaderCollector.Item> items, boolean collapsed, String title) {
        if (items.isEmpty()) {
            return;
        }
        rows.add(Row.section(level, title + "  (" + items.size() + ")"));
        if (!collapsed) {
            for (int i = 0; i < items.size(); i++) {
                rows.add(Row.channel(items.get(i)));
            }
        }
    }

    private void openChannel(ChannelReaderCollector.Item item) {
        if (item == null || item.dialog == null) {
            return;
        }
        Bundle args = new Bundle();
        args.putLong("chat_id", -item.dialog.id);
        presentFragment(new ChatActivity(args));
    }

    private void showGradePicker(ChannelReaderCollector.Item item) {
        if (getParentActivity() == null || item == null) {
            return;
        }
        CharSequence[] options = new CharSequence[]{
                LocaleController.getString(R.string.MiaoChannelMustRead),
                LocaleController.getString(R.string.MiaoChannelScan),
                LocaleController.getString(R.string.MiaoChannelArchived),
                LocaleController.getString(R.string.MiaoChannelUngraded),
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.MiaoChannelSetLevel));
        builder.setItems(options, (dialog, which) -> {
            int level;
            switch (which) {
                case 0: level = ChannelGrading.LEVEL_MUST_READ; break;
                case 1: level = ChannelGrading.LEVEL_SCAN; break;
                case 2: level = ChannelGrading.LEVEL_ARCHIVED; break;
                default: level = ChannelGrading.LEVEL_UNSET; break;
            }
            ChannelGrading.setLevel(item.account, item.dialog.id, level);
            reload();
        });
        builder.show();
    }

    @Override
    public void didReceivedNotification(int id, int account, Object... args) {
        if (account != currentAccount) {
            return;
        }
        AndroidUtilities.runOnUIThread(this::reload);
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private final Context context;

        ListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return true;
        }

        @Override
        public int getItemCount() {
            return rows.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position < 0 || position >= rows.size()) {
                return VIEW_TYPE_CHANNEL;
            }
            return rows.get(position).isSection ? VIEW_TYPE_SECTION : VIEW_TYPE_CHANNEL;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == VIEW_TYPE_SECTION) {
                view = new GraySectionCell(context);
            } else {
                DialogCell cell = new DialogCell(null, context, false, false, currentAccount, null);
                cell.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, AndroidUtilities.dp(72)));
                view = cell;
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position < 0 || position >= rows.size()) {
                return;
            }
            Row row = rows.get(position);
            if (row.isSection) {
                GraySectionCell cell = (GraySectionCell) holder.itemView;
                CharSequence right;
                if (row.section == ChannelGrading.LEVEL_MUST_READ) {
                    right = "";
                } else {
                    boolean collapsed = isCollapsed(row.section);
                    right = LocaleController.getString(collapsed ? R.string.MiaoChannelExpand : R.string.MiaoChannelCollapse);
                }
                cell.setText(row.title, right, null);
            } else {
                DialogCell cell = (DialogCell) holder.itemView;
                cell.setDialog(row.item.dialog, DialogsActivity.DIALOGS_TYPE_DEFAULT, 0);
            }
        }

        private boolean isCollapsed(int level) {
            if (level == ChannelGrading.LEVEL_SCAN) return scanCollapsed;
            if (level == ChannelGrading.LEVEL_ARCHIVED) return archivedCollapsed;
            if (level == ChannelGrading.LEVEL_UNSET) return ungradedCollapsed;
            return false;
        }
    }
}
