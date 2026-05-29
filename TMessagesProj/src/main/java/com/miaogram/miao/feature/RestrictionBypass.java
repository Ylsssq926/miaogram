/*
 * MiaoGram custom code.
 * Gate for the "bypass content restrictions" (noforwards) feature.
 *
 * When enabled, MiaoGram ignores the channel/message "restrict saving content"
 * (noforwards) flag, re-enabling forward / save / screenshot for restricted content.
 *
 * The content is already downloaded locally; upstream merely disables the UI.
 * We flip the local checks. No protocol changes involved.
 *
 * Gray-area feature: ships OFF by default, intended for the community build.
 * See batch3-recon.md / SENSITIVE-CONTENT-POLICY.md.
 */
package com.miaogram.miao.feature;

import com.miaogram.miao.flags.Flags;

public final class RestrictionBypass {

    private RestrictionBypass() {
        // utility class
    }

    /** Whether the noforwards bypass is enabled by the user. */
    public static boolean isEnabled() {
        return Flags.MIAO_PF_2.isEnabled();
    }
}
