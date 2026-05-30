/*
 * MiaoGram custom code.
 * A single entry in the cross-account unified inbox.
 *
 * Wraps an upstream TLRPC.Dialog together with the account it belongs to and a
 * pre-computed unread count, so the UI layer can render and sort entries from
 * multiple accounts in one list without re-querying per-account controllers.
 */
package com.miaogram.miao.feature.unified;

import org.telegram.tgnet.TLRPC;

public final class UnifiedInboxEntry {

    public final int account;
    public final TLRPC.Dialog dialog;
    public final int unreadCount;
    public final long date;

    public UnifiedInboxEntry(int account, TLRPC.Dialog dialog, int unreadCount, long date) {
        this.account = account;
        this.dialog = dialog;
        this.unreadCount = unreadCount;
        this.date = date;
    }

    public long getDialogId() {
        return dialog != null ? dialog.id : 0;
    }
}
