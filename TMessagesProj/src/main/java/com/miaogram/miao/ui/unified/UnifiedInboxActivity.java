/*
 * MiaoGram custom code.
 * Cross-account "Unified Inbox" screen (MIAO_AC_3).
 *
 * MiaoGram's flagship differentiator: built on top of the already-raised
 * account limit, this screen merges the unread conversations (and, in a second
 * tab, the @-mentions) of EVERY logged-in account into a single list. Tapping a
 * row switches to the owning account and opens that chat.
 *
 * Design notes:
 *   - Pure read-only aggregation via UnifiedInboxCollector; no server calls,
 *     no mutation of account state.
 *   - Rows are rendered with upstream DialogCell using its 6-arg constructor so
 *     the per-account avatar / unread badge / draft / typing rendering is reused
 *     for free. The cell's account is set explicitly per row.
 *   - Cross-account navigation follows the upstream pattern used by notification
 *     intents: switchToAccount(account, true) then present ChatActivity on the
 *     fresh stack.
 */
package com.miaogram.miao.ui.unified;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miaogram.miao.feature.unified.UnifiedInboxCollector;
import com.miaogram.miao.feature.unified.UnifiedInboxEntry;
import com.miaogram.miao.ui.common.MiaoEmptyView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ScrollSlidingTextTabStrip;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;

import java.util.ArrayList;
import java.util.List;

public class UnifiedInboxActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {

    private static final int TAB_UNREAD = 0;
    private static final int TAB_MENTIONS = 1;

    private int currentMode = UnifiedInboxCollector.MODE_UNREAD;

    private RecyclerListView listView;
    private ListAdapter adapter;
    private FrameLayout emptyContainer;
    private org.telegram.ui.Components.RadialProgressView progressView;
    private ScrollSlidingTextTabStrip tabStrip;

    private final List<UnifiedInboxEntry> entries = new ArrayList<>();

    // Per-account events: posted on NotificationCenter.getInstance(account).
    private static final int[] PER_ACCOUNT_EVENTS = {
            NotificationCenter.dialogsNeedReload,
            NotificationCenter.updateInterfaces,
            NotificationCenter.dialogsUnreadCounterChanged,
    };
    // notificationsCountUpdated is posted on the GLOBAL instance, not per-account.

    // Tracks which accounts we've already subscribed to, so newly logged-in
    // accounts can be picked up on the next reload (see addObserversForActivatedAccounts).
    private final java.util.Set<Integer> observedAccounts = new java.util.HashSet<>();

    @Override
    public boolean onFragmentCreate() {
        addObserversForActivatedAccounts();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.notificationsCountUpdated);
        return super.onFragmentCreate();
    }

    @Override
    public void onFragmentDestroy() {
        for (int account : observedAccounts) {
            NotificationCenter nc = NotificationCenter.getInstance(account);
            for (int event : PER_ACCOUNT_EVENTS) {
                nc.removeObserver(this, event);
            }
        }
        observedAccounts.clear();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.notificationsCountUpdated);
        super.onFragmentDestroy();
    }

    /** Subscribes to per-account events for any activated account not yet observed. */
    private void addObserversForActivatedAccounts() {
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            if (!UserConfig.getInstance(a).isClientActivated() || observedAccounts.contains(a)) {
                continue;
            }
            NotificationCenter nc = NotificationCenter.getInstance(a);
            for (int event : PER_ACCOUNT_EVENTS) {
                nc.addObserver(this, event);
            }
            observedAccounts.add(a);
        }
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString(R.string.MiaoUnifiedInbox));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        tabStrip = new ScrollSlidingTextTabStrip(context, null);
        tabStrip.setUseSameWidth(true);
        tabStrip.addTextTab(TAB_UNREAD, LocaleController.getString(R.string.MiaoUnifiedUnread));
        tabStrip.addTextTab(TAB_MENTIONS, LocaleController.getString(R.string.MiaoUnifiedMentions));
        tabStrip.finishAddingTabs();
        tabStrip.setDelegate(new ScrollSlidingTextTabStrip.ScrollSlidingTabStripDelegate() {
            @Override
            public void onPageSelected(int page, boolean forward) {
                currentMode = (page == TAB_MENTIONS) ? UnifiedInboxCollector.MODE_MENTIONS : UnifiedInboxCollector.MODE_UNREAD;
                reload();
            }

            @Override
            public void onPageScrolled(float progress) {
            }

            @Override
            public void onSamePageSelected() {
            }
        });

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        fragmentView = frameLayout;

        frameLayout.addView(tabStrip, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 44, Gravity.TOP));

        // 1dp divider under the tab strip to avoid the tabs/list floating apart.
        View tabDivider = new View(context);
        tabDivider.setBackgroundColor(Theme.getColor(Theme.key_divider));
        frameLayout.addView(tabDivider, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 1f / AndroidUtilities.density, Gravity.TOP, 0, 44, 0, 0));

        emptyContainer = new FrameLayout(context);
        emptyContainer.setVisibility(View.GONE);
        frameLayout.addView(emptyContainer, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.CENTER, 0, 44, 0, 0));

        progressView = new org.telegram.ui.Components.RadialProgressView(context);
        progressView.setVisibility(View.GONE);
        frameLayout.addView(progressView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER, 0, 44, 0, 0));

        listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        adapter = new ListAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((view, position) -> {
            if (position < 0 || position >= entries.size()) {
                return;
            }
            openEntry(entries.get(position));
        });
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP, 0, 44, 0, 0));

        reload();
        return fragmentView;
    }

    private void reload() {
        // Pick up accounts logged in after this screen was opened.
        addObserversForActivatedAccounts();
        entries.clear();
        boolean enabled = UnifiedInboxCollector.isEnabled();
        if (enabled) {
            entries.addAll(UnifiedInboxCollector.collect(currentMode));
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        boolean empty = entries.isEmpty();
        // Show a spinner instead of "all caught up" while some account is still
        // loading its dialogs (e.g. cold start), to avoid a misleading flash.
        boolean loading = empty && enabled && !UnifiedInboxCollector.allAccountsLoaded();

        if (progressView != null) {
            progressView.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
        if (emptyContainer != null) {
            boolean showEmpty = empty && !loading;
            emptyContainer.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
            if (showEmpty) {
                emptyContainer.removeAllViews();
                emptyContainer.addView(MiaoEmptyView.create(
                        getParentActivity() != null ? getParentActivity() : emptyContainer.getContext(),
                        R.drawable.msg_discussion,
                        LocaleController.getString(R.string.MiaoUnifiedEmptyTitle),
                        LocaleController.getString(R.string.MiaoUnifiedEmpty)),
                        LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER));
            }
        }
    }

    private void openEntry(UnifiedInboxEntry entry) {
        if (entry == null || entry.dialog == null || entry.dialog.id == 0) {
            return;
        }
        final int account = entry.account;
        final long dialogId = entry.dialog.id;

        Bundle args = new Bundle();
        if (DialogObject.isEncryptedDialog(dialogId)) {
            args.putInt("enc_id", DialogObject.getEncryptedChatId(dialogId));
        } else if (DialogObject.isUserDialog(dialogId)) {
            args.putLong("user_id", dialogId);
        } else {
            args.putLong("chat_id", -dialogId);
        }

        if (account == UserConfig.selectedAccount) {
            presentFragment(new ChatActivity(args));
            return;
        }

        LaunchActivity launch = LaunchActivity.instance;
        if (launch == null) {
            return;
        }
        // Switching account rebuilds the fragment stack (this fragment is
        // destroyed). After this call only reach through LaunchActivity, never
        // through this fragment's own members. Present the chat on the next
        // frame so the new account's controllers finish (re)initializing first.
        launch.switchToAccount(account, true);
        NotificationCenter.getInstance(account).postNotificationName(NotificationCenter.closeChats);
        AndroidUtilities.runOnUIThread(() -> {
            if (LaunchActivity.instance != null && LaunchActivity.instance.getActionBarLayout() != null) {
                LaunchActivity.instance.getActionBarLayout().presentFragment(new ChatActivity(args));
            }
        });
    }

    @Override
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.dialogsNeedReload
                || id == NotificationCenter.dialogsUnreadCounterChanged
                || id == NotificationCenter.notificationsCountUpdated
                || id == NotificationCenter.updateInterfaces) {
            AndroidUtilities.runOnUIThread(this::reload);
        }
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
            return entries.size();
        }

        @Override
        public int getItemViewType(int position) {
            // Account is the view type: each DialogCell is constructed bound to a
            // specific account (its currentAccount is set in the constructor and
            // has no setter), so RecyclerView recycles cells per-account and a
            // row is only ever bound into a cell built for its own account.
            if (position < 0 || position >= entries.size()) {
                return 0;
            }
            return entries.get(position).account;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            // viewType == account: the cell's currentAccount is fixed in the
            // constructor (no setter), so RecyclerView only ever binds a row
            // into a cell built for that row's own account.
            DialogCell cell = new DialogCell(null, context, false, false, viewType, null);
            // Disable story long-press: the story viewer resolves via
            // LaunchActivity.getLastFragment() which is the current account, not
            // this row's account, so cross-account story preview would use the
            // wrong account. Plain tap (open chat) still switches account first.
            cell.storyParams.allowLongress = false;
            cell.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, AndroidUtilities.dp(72)));
            return new RecyclerListView.Holder(cell);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position < 0 || position >= entries.size()) {
                return;
            }
            UnifiedInboxEntry entry = entries.get(position);
            DialogCell cell = (DialogCell) holder.itemView;
            cell.setDialog(entry.dialog, DialogsActivity.DIALOGS_TYPE_DEFAULT, 0);
        }
    }
}
