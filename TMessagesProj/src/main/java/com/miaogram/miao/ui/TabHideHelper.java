/*
 * MiaoGram custom code.
 * Helper for the "hide All Chats tab" feature.
 *
 * The upstream DialogsActivity always adds the default ("All Chats") folder tab.
 * When MIAO_UI_3 is on AND the user has at least one other folder, we skip adding
 * that tab so the folder bar starts directly with custom folders.
 *
 * Safety: if the user has no other folders (filters.size() <= 1), we MUST still show
 * the default tab, otherwise the tab bar would be empty and the user couldn't navigate.
 * This is handled by the caller checking shouldHideDefaultTab() which already requires
 * otherFolderCount > 0.
 *
 * See ADR-006 (batch2-features-design.md / architecture-design.md) for the rationale
 * on why this lives behind a flag and ships OFF by default.
 */
package com.miaogram.miao.ui;

import com.miaogram.miao.flags.Flags;

public final class TabHideHelper {

    private TabHideHelper() {
        // utility class
    }

    /** Whether the hide-All-Chats feature is enabled by the user. */
    public static boolean isEnabled() {
        return Flags.MIAO_UI_3.isEnabled();
    }

    /**
     * Whether the default ("All Chats") tab should be hidden in the current state.
     *
     * @param totalFilterCount filters.size() — total number of folders including the default one
     * @return true only when the feature is on AND there is at least one non-default folder
     *         to fall back to (so the tab bar is never left empty).
     */
    public static boolean shouldHideDefaultTab(int totalFilterCount) {
        return isEnabled() && totalFilterCount > 1;
    }
}
