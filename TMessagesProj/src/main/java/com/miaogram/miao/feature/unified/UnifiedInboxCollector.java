/*
 * MiaoGram custom code.
 * Collects unread / mention dialogs across all logged-in accounts.
 *
 * This is the data layer of the cross-account "Unified Inbox" feature
 * (MIAO_AC_3) — MiaoGram's flagship differentiator built on top of the
 * already-raised account limit. It is a read-only aggregation:
 *   - iterates every activated account,
 *   - takes a snapshot copy of that account's dialog list (folder 0),
 *   - keeps dialogs with unread messages (or unread mark), or unread
 *     mentions, depending on the requested mode,
 *   - sorts the merged result by last message date descending.
 *
 * Nothing here mutates account data or talks to the server. All reads go
 * through public MessagesController APIs (getDialogs / getDialogUnreadCount)
 * rather than protected fields, because this class lives outside the
 * org.telegram.messenger package.
 *
 * Threading: callers should invoke collect() on the UI thread (dialog lists
 * are UI-thread structures); we copy each list into a new ArrayList before
 * iterating to avoid concurrent-modification surprises, mirroring upstream
 * NotificationsController.getTotalAllUnreadCount().
 */
package com.miaogram.miao.feature.unified;

import com.miaogram.miao.flags.Flags;

import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class UnifiedInboxCollector {

    /** Aggregation mode. */
    public static final int MODE_UNREAD = 0;
    public static final int MODE_MENTIONS = 1;

    private UnifiedInboxCollector() {
        // utility class
    }

    /** Whether the unified inbox feature is enabled. */
    public static boolean isEnabled() {
        return Flags.MIAO_AC_3.isEnabled();
    }

    /** Number of currently activated accounts (entries always span these). */
    public static int activatedAccountCount() {
        int n = 0;
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                n++;
            }
        }
        return n;
    }

    /**
     * Collects entries across all activated accounts for the given mode,
     * sorted by last message date descending.
     */
    public static List<UnifiedInboxEntry> collect(int mode) {
        List<UnifiedInboxEntry> out = new ArrayList<>();
        for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
            if (!UserConfig.getInstance(a).isClientActivated()) {
                continue;
            }
            MessagesController controller = MessagesController.getInstance(a);
            ArrayList<TLRPC.Dialog> snapshot = new ArrayList<>(controller.getDialogs(0));
            for (int i = 0; i < snapshot.size(); i++) {
                TLRPC.Dialog dialog = snapshot.get(i);
                if (dialog == null || dialog instanceof TLRPC.TL_dialogFolder) {
                    continue;
                }
                if (mode == MODE_MENTIONS) {
                    if (dialog.unread_mentions_count > 0) {
                        out.add(new UnifiedInboxEntry(a, dialog, dialog.unread_mentions_count, dialog.last_message_date));
                    }
                } else {
                    int unread = controller.getDialogUnreadCount(dialog);
                    if (unread > 0 || dialog.unread_mark) {
                        out.add(new UnifiedInboxEntry(a, dialog, unread, dialog.last_message_date));
                    }
                }
            }
        }
        Collections.sort(out, Comparator.comparingLong((UnifiedInboxEntry e) -> e.date).reversed());
        return out;
    }
}
