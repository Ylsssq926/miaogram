/*
 * MiaoGram custom code.
 * Collects broadcast channels for the current account, grouped by reading grade.
 *
 * Data layer of the channel-reader feature (MIAO_AC_4). Walks the current
 * account's dialog list, keeps only real broadcast channels (not megagroups,
 * groups, users or bots), and buckets them by their user-assigned grade
 * (must-read / scan / archived / ungraded).
 *
 * Read-only aggregation through public MessagesController APIs; no server
 * calls, no mutation of Telegram data.
 */
package com.miaogram.miao.feature.reader;

import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class ChannelReaderCollector {

    /** A channel row with its grade and unread count. */
    public static final class Item {
        public final int account;
        public final TLRPC.Dialog dialog;
        public final int level;
        public final int unreadCount;
        public final long date;

        Item(int account, TLRPC.Dialog dialog, int level, int unreadCount, long date) {
            this.account = account;
            this.dialog = dialog;
            this.level = level;
            this.unreadCount = unreadCount;
            this.date = date;
        }
    }

    /** Buckets of channels keyed by grade, each already sorted by date desc. */
    public static final class Result {
        public final List<Item> mustRead = new ArrayList<>();
        public final List<Item> scan = new ArrayList<>();
        public final List<Item> archived = new ArrayList<>();
        public final List<Item> ungraded = new ArrayList<>();

        public int mustReadUnread() {
            return sumUnread(mustRead);
        }

        public int totalChannels() {
            return mustRead.size() + scan.size() + archived.size() + ungraded.size();
        }

        private static int sumUnread(List<Item> list) {
            int n = 0;
            for (int i = 0; i < list.size(); i++) {
                n += list.get(i).unreadCount;
            }
            return n;
        }
    }

    private ChannelReaderCollector() {
        // utility class
    }

    /** Whether the channel-reader feature is enabled. */
    public static boolean isEnabled() {
        return ChannelGrading.isEnabled();
    }

    /** Collects and buckets the current account's channels by grade. */
    public static Result collect(int account) {
        Result result = new Result();
        // getDialogs(0) returns the live list; guard against a background thread
        // mutating it while we copy/iterate (mirrors upstream's defensive reads).
        try {
            MessagesController controller = MessagesController.getInstance(account);
            ArrayList<TLRPC.Dialog> snapshot = new ArrayList<>(controller.getDialogs(0));
            for (int i = 0; i < snapshot.size(); i++) {
                TLRPC.Dialog dialog = snapshot.get(i);
                if (dialog == null || dialog instanceof TLRPC.TL_dialogFolder) {
                    continue;
                }
                // channels are chat dialogs (negative id)
                if (dialog.id >= 0) {
                    continue;
                }
                TLRPC.Chat chat = controller.getChat(-dialog.id);
                if (chat == null || !ChatObject.isChannelAndNotMegaGroup(chat)) {
                    continue;
                }
                int unread = controller.getDialogUnreadCount(dialog);
                int level = ChannelGrading.getLevel(account, dialog.id);
                Item item = new Item(account, dialog, level, unread, dialog.last_message_date);
                switch (level) {
                    case ChannelGrading.LEVEL_MUST_READ:
                        result.mustRead.add(item);
                        break;
                    case ChannelGrading.LEVEL_SCAN:
                        result.scan.add(item);
                        break;
                    case ChannelGrading.LEVEL_ARCHIVED:
                        result.archived.add(item);
                        break;
                    default:
                        result.ungraded.add(item);
                        break;
                }
            }
        } catch (Exception e) {
            org.telegram.messenger.FileLog.e(e);
        }
        Comparator<Item> byDate = Comparator.comparingLong((Item it) -> it.date).reversed();
        Collections.sort(result.mustRead, byDate);
        Collections.sort(result.scan, byDate);
        Collections.sort(result.archived, byDate);
        Collections.sort(result.ungraded, byDate);
        return result;
    }
}
