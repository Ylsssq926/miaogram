/*
 * MiaoGram custom code.
 * Per-account local remark (nickname) storage.
 *
 * Lets the user attach a custom local label to each logged-in account, to tell
 * apart multiple accounts (especially ones with the same display name).
 *
 * Stored in MiaoGram's own SharedPreferences under "account_remark_<accountId>".
 * Purely local — never sent to any server, never affects the Telegram profile.
 *
 * Gated by Flags.MIAO_AC_2 (default ON; harmless enhancement).
 */
package com.miaogram.miao.account;

import androidx.annotation.Nullable;

import com.miaogram.miao.flags.Flags;
import com.miaogram.miao.utils.MiaoSharedPrefs;

public final class AccountRemark {

    private static final String KEY_PREFIX = "account_remark_";

    private AccountRemark() {
        // utility class
    }

    private static String key(int accountId) {
        return KEY_PREFIX + accountId;
    }

    /** Whether the account-remark feature is enabled. */
    public static boolean isEnabled() {
        return Flags.MIAO_AC_2.isEnabled();
    }

    /**
     * Returns the user-set remark for an account, or null if none / feature disabled.
     */
    @Nullable
    public static String getRemark(int accountId) {
        if (!isEnabled()) {
            return null;
        }
        String value = MiaoSharedPrefs.getString(key(accountId), null);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value;
    }

    /**
     * Set (or clear, if null/blank) the remark for an account.
     */
    public static void setRemark(int accountId, @Nullable String remark) {
        if (remark == null || remark.trim().isEmpty()) {
            MiaoSharedPrefs.remove(key(accountId));
        } else {
            MiaoSharedPrefs.putString(key(accountId), remark.trim());
        }
    }

    /**
     * Returns the remark if set, otherwise the supplied fallback (usually the
     * Telegram display name). Convenience for display call sites.
     */
    public static CharSequence getDisplayName(int accountId, CharSequence fallback) {
        String remark = getRemark(accountId);
        return remark != null ? remark : fallback;
    }
}
