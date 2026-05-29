/*
 * MiaoGram custom code.
 * Makes destructive confirmation dialogs default to UNCHECKED.
 *
 * Upstream pre-checks some dangerous options (e.g. the block-user dialog
 * pre-checks "report spam" + "delete chat for both sides", and the pin-message
 * dialog pre-checks "notify all members"). When MIAO_PF_6 is enabled
 * (default ON), MiaoGram returns all-false defaults so a careless tap on OK
 * does not trigger the destructive side effect.
 *
 * Note: this only changes the *initial* checkbox state. The user can still
 * tick the boxes manually. We deliberately do NOT touch flows that depend on a
 * forced default (e.g. forceDeleteForAll second-confirmation, public-channel
 * history clear), see batch4 recon.
 *
 * Purely local UX, no ToS risk.
 */
package com.miaogram.miao.feature;

import com.miaogram.miao.flags.Flags;

public final class SafeDefaults {

    private SafeDefaults() {
        // utility class
    }

    /** Whether safer (unchecked) destructive defaults are enabled. */
    public static boolean isEnabled() {
        return Flags.MIAO_PF_6.isEnabled();
    }

    /**
     * Returns a safe default for a single destructive checkbox.
     * When the feature is on, always returns false; otherwise returns the
     * upstream default unchanged.
     */
    public static boolean defaultChecked(boolean upstreamDefault) {
        return isEnabled() ? false : upstreamDefault;
    }

    /**
     * Returns safe defaults for a group of destructive checkboxes.
     * When on, returns an all-false array of the same length; otherwise
     * returns the upstream array unchanged.
     */
    public static boolean[] defaultChecks(boolean[] upstream) {
        if (!isEnabled() || upstream == null) {
            return upstream;
        }
        return new boolean[upstream.length];
    }
}
