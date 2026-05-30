/*
 * MiaoGram custom code.
 * Per-channel reading priority (the "channel reader" feature, MIAO_AC_4).
 *
 * Lets the user grade each broadcast channel as MUST-READ / SCAN / ARCHIVED so
 * the channel-reader screen can group dozens of channels into a manageable,
 * collapsible list instead of one flat wall of unread badges.
 *
 * Storage: a single newline-separated string in MiaoSharedPrefs, each line
 * "account:dialogId=level". Kept tiny (only graded channels are stored; the
 * default UNSET is implicit), parsed lazily into a volatile in-memory map.
 *
 * Purely local metadata. No server calls, no mutation of Telegram data.
 */
package com.miaogram.miao.feature.reader;

import android.text.TextUtils;

import com.miaogram.miao.flags.Flags;
import com.miaogram.miao.utils.MiaoSharedPrefs;

import java.util.HashMap;
import java.util.Map;

public final class ChannelGrading {

    /** Reading levels. UNSET means the user has not graded this channel. */
    public static final int LEVEL_UNSET = 0;
    public static final int LEVEL_MUST_READ = 1;
    public static final int LEVEL_SCAN = 2;
    public static final int LEVEL_ARCHIVED = 3;

    private static final String PREF_KEY = "miao_channel_grades";

    /** key = account + "/" + dialogId, value = level. */
    private static volatile Map<String, Integer> cache = null;

    private ChannelGrading() {
        // utility class
    }

    /** Whether the channel-reader feature is enabled. */
    public static boolean isEnabled() {
        return Flags.MIAO_AC_4.isEnabled();
    }

    private static String keyOf(int account, long dialogId) {
        return account + "/" + dialogId;
    }

    /** Returns the grade for a channel, or LEVEL_UNSET if never graded. */
    public static int getLevel(int account, long dialogId) {
        Integer v = snapshot().get(keyOf(account, dialogId));
        return v != null ? v : LEVEL_UNSET;
    }

    /** Sets (or clears, when level == LEVEL_UNSET) the grade for a channel. */
    public static synchronized void setLevel(int account, long dialogId, int level) {
        Map<String, Integer> map = new HashMap<>(snapshot());
        if (level == LEVEL_UNSET) {
            map.remove(keyOf(account, dialogId));
        } else {
            map.put(keyOf(account, dialogId), level);
        }
        cache = map;
        persist(map);
    }

    private static Map<String, Integer> snapshot() {
        Map<String, Integer> local = cache;
        if (local == null) {
            synchronized (ChannelGrading.class) {
                local = cache;
                if (local == null) {
                    local = parse(MiaoSharedPrefs.getString(PREF_KEY, ""));
                    cache = local;
                }
            }
        }
        return local;
    }

    private static Map<String, Integer> parse(String raw) {
        Map<String, Integer> map = new HashMap<>();
        if (TextUtils.isEmpty(raw)) {
            return map;
        }
        String[] lines = raw.split("\\n");
        for (String line : lines) {
            int eq = line.indexOf('=');
            if (eq <= 0 || eq >= line.length() - 1) {
                continue;
            }
            try {
                String key = line.substring(0, eq);
                int level = Integer.parseInt(line.substring(eq + 1).trim());
                if (level >= LEVEL_MUST_READ && level <= LEVEL_ARCHIVED) {
                    map.put(key, level);
                }
            } catch (NumberFormatException ignore) {
                // skip malformed line
            }
        }
        return map;
    }

    private static void persist(Map<String, Integer> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            sb.append(e.getKey()).append('=').append(e.getValue()).append('\n');
        }
        MiaoSharedPrefs.putString(PREF_KEY, sb.toString());
    }
}
