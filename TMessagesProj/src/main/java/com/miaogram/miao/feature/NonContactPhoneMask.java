/*
 * MiaoGram custom code.
 * Masks phone numbers of non-contacts on the profile screen.
 *
 * When MIAO_PF_5 is enabled, the phone number of a user who is NOT in the
 * local address book (and is not a bot / not a support account / not self)
 * is shown masked, so screenshots of a stranger's profile do not leak their
 * full number.
 *
 * Upstream only shows "Unknown" when the server itself withheld user.phone.
 * This feature additionally masks numbers that ARE visible to the client
 * (e.g. via mutual-contact / "everyone can see my number" settings).
 *
 * Purely local display, no ToS risk.
 */
package com.miaogram.miao.feature;

import android.text.TextUtils;

import com.miaogram.miao.flags.Flags;

import org.telegram.messenger.ContactsController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;

public final class NonContactPhoneMask {

    private NonContactPhoneMask() {
        // utility class
    }

    /** Whether the masking feature is enabled by the user. */
    public static boolean isEnabled() {
        return Flags.MIAO_PF_5.isEnabled();
    }

    /**
     * Decides whether the phone of {@code userId} should be masked on the
     * profile screen for the given account.
     *
     * Masks only when: feature on, target is a real user (not bot), not self,
     * not a Telegram support account, and not in the local address book.
     */
    public static boolean shouldMask(int currentAccount, long userId, boolean isBot) {
        if (!isEnabled() || isBot || userId == 0) {
            return false;
        }
        if (userId == UserConfig.getInstance(currentAccount).getClientUserId()) {
            return false;
        }
        TLRPC.User user = MessagesController.getInstance(currentAccount).getUser(userId);
        if (user != null && (user.bot || user.support || MessagesController.isSupportUser(user))) {
            return false;
        }
        return !ContactsController.getInstance(currentAccount).isContact(userId);
    }

    /**
     * Masks a raw phone string, keeping only the last 4 digits.
     * e.g. "8613800138000" -> "+••••••8000". Returns a generic mask if the
     * number is too short to keep a tail.
     */
    public static String mask(String rawPhone) {
        if (TextUtils.isEmpty(rawPhone)) {
            return "+••••••••";
        }
        String digits = rawPhone.replaceAll("[^0-9]", "");
        if (digits.length() <= 4) {
            return "+••••";
        }
        String tail = digits.substring(digits.length() - 4);
        return "+••••••" + tail;
    }
}
