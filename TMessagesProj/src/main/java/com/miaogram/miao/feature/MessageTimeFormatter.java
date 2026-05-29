/*
 * MiaoGram custom code.
 * Picks the message-bubble time formatter.
 *
 * When MIAO_UI_9 is enabled, message timestamps show seconds (HH:mm:ss),
 * reusing upstream's built-in getFormatterDayWithSeconds(). Otherwise the
 * normal HH:mm formatter is returned, so behaviour is identical to upstream
 * when the flag is off.
 *
 * Only the chat-bubble time should route through here (4 call sites in
 * ChatMessageCell). Other formatterDay consumers (last-seen, subscriptions,
 * call log, etc.) are intentionally left on the plain formatter.
 *
 * Purely local display, no ToS risk.
 */
package com.miaogram.miao.feature;

import com.miaogram.miao.flags.Flags;

import org.telegram.messenger.LocaleController;

public final class MessageTimeFormatter {

    private MessageTimeFormatter() {
        // utility class
    }

    /**
     * Returns the formatter to use for chat-bubble timestamps.
     * Falls back to the plain formatter if the seconds flag is off, or if the
     * seconds formatter is unavailable for any reason.
     */
    public static org.telegram.messenger.time.FastDateFormat formatter() {
        LocaleController lc = LocaleController.getInstance();
        if (Flags.MIAO_UI_9.isEnabled()) {
            try {
                org.telegram.messenger.time.FastDateFormat withSeconds = lc.getFormatterDayWithSeconds();
                if (withSeconds != null) {
                    return withSeconds;
                }
            } catch (Throwable ignore) {
                // fall through to plain formatter
            }
        }
        return lc.getFormatterDay();
    }
}
