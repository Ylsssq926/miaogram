/*
 * MiaoGram custom code.
 * Facade for MiaoGram's built-in translation enhancement.
 *
 * Telegram FOSS already ships everything we need:
 *   - A free, non-official Google endpoint translator: TranslateAlert2.alternativeTranslate(...)
 *   - A provider switch via MessagesController.translationsManualEnabled / translationsAutoEnabled
 *     (values: "enabled" | "alternative" | "system" | "disabled"). When a value is "alternative",
 *     both the single-message popup (TranslateAlert2.translate) and dialog-level translation
 *     (TranslateController.pushToTranslate) route through alternativeTranslate.
 *   - The "translate whole dialog" feature is gated behind Premium in three places.
 *
 * So this class does NOT implement a translation engine. It only decides, on behalf of
 * the few upstream hook points, whether MiaoGram's enhancement is active and:
 *   1. which provider method upstream should use (Google free vs Telegram official), and
 *   2. whether the Premium-gated dialog translation should be unlocked.
 *
 * Unlocking is purely client-side: the underlying request hits the Google endpoint, so no
 * Premium-only request is ever sent to Telegram's servers. Turning the flag off restores
 * stock upstream behavior (Premium gate back, source back to official).
 */
package com.miaogram.miao.feature.translate;

import androidx.annotation.NonNull;

import com.miaogram.miao.flags.Flags;
import com.miaogram.miao.utils.MiaoSharedPrefs;

public final class MiaoTranslate {

    /** Upstream provider method that routes through the free Google endpoint. */
    private static final String METHOD_ALTERNATIVE = "alternative";

    /** Source preference values. */
    public static final String SOURCE_GOOGLE = "google";
    public static final String SOURCE_TELEGRAM = "telegram";

    /** SharedPreferences key storing the user's translation source choice. */
    private static final String KEY_SOURCE = "miao_tr_source";

    private MiaoTranslate() {
        // utility class
    }

    /** Whether MiaoGram's translation enhancement is enabled by the user. */
    public static boolean isEnhancedEnabled() {
        return Flags.MIAO_TR_1.isEnabled();
    }

    /** Returns the user's translation source: SOURCE_GOOGLE (default) or SOURCE_TELEGRAM. */
    @NonNull
    public static String getSource() {
        return SOURCE_GOOGLE.equals(MiaoSharedPrefs.getString(KEY_SOURCE, SOURCE_GOOGLE))
                ? SOURCE_GOOGLE : SOURCE_TELEGRAM;
    }

    /** Persists the user's translation source choice. */
    public static void setSource(@NonNull String source) {
        MiaoSharedPrefs.putString(KEY_SOURCE,
                SOURCE_TELEGRAM.equals(source) ? SOURCE_TELEGRAM : SOURCE_GOOGLE);
    }

    /** True when enhancement is on and the user picked the free Google source. */
    private static boolean useGoogleSource() {
        return isEnhancedEnabled() && SOURCE_GOOGLE.equals(getSource());
    }

    /**
     * Resolves the effective provider method for single-message / context translation.
     * When enhancement is on and Google source is chosen, force "alternative"; otherwise
     * pass the upstream (possibly server-pushed) value through unchanged.
     */
    @NonNull
    public static String preferredManualMethod(@NonNull String upstreamValue) {
        return useGoogleSource() ? METHOD_ALTERNATIVE : upstreamValue;
    }

    /** Same as preferredManualMethod but for dialog-level auto translation. */
    @NonNull
    public static String preferredAutoMethod(@NonNull String upstreamValue) {
        return useGoogleSource() ? METHOD_ALTERNATIVE : upstreamValue;
    }

    /**
     * Whether the Premium-gated "translate whole dialog" feature should be unlocked.
     * True only while the enhancement is enabled.
     */
    public static boolean unlockChatTranslate() {
        return isEnhancedEnabled();
    }
}
