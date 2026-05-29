/*
 * MiaoGram custom code.
 * Keyword-based notification muting.
 *
 * When MIAO_MS_1 is enabled, incoming messages whose text matches any
 * user-configured keyword do not trigger a notification. This is a purely
 * local notification-layer filter:
 *   - the message still appears in the chat list and conversation,
 *   - unread counts are untouched,
 *   - nothing is sent to / faked toward the server.
 *
 * Deliberately scoped to notifications only (see batch4 recon): hiding
 * messages from the chat list or dialog preview would deeply couple the
 * message lifecycle (messagesDict / grouped / date headers / unread), the
 * same trap as Anti-recall. That is left for a future "render-layer mask"
 * approach, not list removal.
 *
 * Matching uses the raw server text (messageOwner.message) plus caption,
 * never the localized messageText (which is rewritten for action messages).
 *
 * Thread model: shouldMute() is called from NotificationsController's single
 * notificationsQueue thread; the keyword snapshot is held in a volatile field
 * so reloads from the UI thread are visible without tearing.
 */
package com.miaogram.miao.feature;

import android.text.TextUtils;

import com.miaogram.miao.flags.Flags;
import com.miaogram.miao.utils.MiaoSharedPrefs;

import org.telegram.messenger.MessageObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class KeywordFilter {

    /** SharedPreferences key holding the newline-separated keyword list. */
    private static final String PREF_KEY = "miao_ms_keywords";

    /** Immutable snapshot of lowercase keywords; swapped atomically on reload. */
    private static volatile List<String> keywords = null;

    private KeywordFilter() {
        // utility class
    }

    /** Whether keyword notification muting is enabled. */
    public static boolean isEnabled() {
        return Flags.MIAO_MS_1.isEnabled();
    }

    /** Returns the raw multi-line keyword text the user has configured. */
    public static String getRawText() {
        return MiaoSharedPrefs.getString(PREF_KEY, "");
    }

    /** Persists the raw multi-line keyword text and refreshes the cache. */
    public static void setRawText(String raw) {
        MiaoSharedPrefs.putString(PREF_KEY, raw != null ? raw : "");
        keywords = parse(raw);
    }

    /** Number of configured keywords (for settings summary). */
    public static int count() {
        return snapshot().size();
    }

    /**
     * Decides whether a message should be muted (no notification).
     * Returns false unless the feature is on, keywords exist, and the
     * message's server text / caption contains at least one keyword.
     */
    public static boolean shouldMute(MessageObject messageObject) {
        if (!isEnabled() || messageObject == null || messageObject.messageOwner == null) {
            return false;
        }
        List<String> keys = snapshot();
        if (keys.isEmpty()) {
            return false;
        }
        String haystack = buildHaystack(messageObject);
        if (TextUtils.isEmpty(haystack)) {
            return false;
        }
        String lower = haystack.toLowerCase(Locale.ROOT);
        for (int i = 0; i < keys.size(); i++) {
            if (lower.contains(keys.get(i))) {
                return true;
            }
        }
        return false;
    }

    private static String buildHaystack(MessageObject messageObject) {
        String text = messageObject.messageOwner.message;
        CharSequence caption = messageObject.caption;
        if (TextUtils.isEmpty(text)) {
            return caption == null ? null : caption.toString();
        }
        if (caption == null || caption.length() == 0) {
            return text;
        }
        return text + "\n" + caption;
    }

    private static List<String> snapshot() {
        List<String> local = keywords;
        if (local == null) {
            synchronized (KeywordFilter.class) {
                local = keywords;
                if (local == null) {
                    local = parse(getRawText());
                    keywords = local;
                }
            }
        }
        return local;
    }

    /** Splits the raw text into a normalized, de-duplicated keyword list. */
    private static List<String> parse(String raw) {
        if (TextUtils.isEmpty(raw)) {
            return Collections.emptyList();
        }
        ArrayList<String> result = new ArrayList<>();
        String[] lines = raw.split("\\r?\\n");
        for (String line : lines) {
            if (line == null) {
                continue;
            }
            String k = line.trim().toLowerCase(Locale.ROOT);
            if (!k.isEmpty() && !result.contains(k)) {
                result.add(k);
            }
        }
        return Collections.unmodifiableList(result);
    }
}
