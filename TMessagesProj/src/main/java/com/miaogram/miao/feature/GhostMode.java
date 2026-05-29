/*
 * MiaoGram custom code.
 * Gate for Ghost Mode (stealth browsing).
 *
 * When enabled, MiaoGram suppresses outgoing presence signals:
 *   - read receipts (completeReadTask)
 *   - typing status (sendTyping)
 *   - online status (ignoreSetOnline)
 *
 * So the user can read messages / browse without the other side seeing
 * "read", "typing...", or "online".
 *
 * Gray-area feature: ships OFF by default, intended for the community build.
 * Telegram's ToS names "ghost mode" explicitly (§1.4) but it has been tolerated
 * for years across forks (AyuGram etc). See batch3-recon.md.
 */
package com.miaogram.miao.feature;

import com.miaogram.miao.flags.Flags;

public final class GhostMode {

    private GhostMode() {
        // utility class
    }

    /** Whether ghost mode is enabled by the user. */
    public static boolean isEnabled() {
        return Flags.MIAO_PF_3.isEnabled();
    }
}
