/*
 * MiaoGram custom code.
 * Multi-account limit override.
 *
 * See ADR-005 in architecture-design.md for the design rationale.
 *
 * Strategy:
 *   - UserConfig.MAX_ACCOUNT_COUNT is raised to 10 at the source level (so arrays /
 *     loops are sized for 10). Empty account slots are harmless (isClientActivated()
 *     returns false for them).
 *   - This class gates the *UI* ceiling: when the MIAO_AC_1 flag is OFF, the app behaves
 *     exactly like upstream (max 4 addable accounts); when ON, it opens up to 10.
 *
 * Usage in upstream hooks (replacing "count < UserConfig.MAX_ACCOUNT_COUNT"):
 *     if (UserConfig.getActivatedAccountsCount() < MaxAccountsOverride.getEffectiveMax()) { ... }
 */
package com.miaogram.miao.account;

import com.miaogram.miao.flags.Flags;

public final class MaxAccountsOverride {

    /** The official ceiling, used when the feature flag is off. */
    public static final int OFFICIAL_MAX = 4;

    /**
     * The expanded ceiling when the flag is on. MUST be <= UserConfig.MAX_ACCOUNT_COUNT,
     * otherwise we'd index past the backing arrays. Keep these two in sync.
     */
    public static final int EXPANDED_MAX = 10;

    private MaxAccountsOverride() {
        // utility class
    }

    /**
     * Returns the effective maximum number of accounts the user is allowed to add.
     * OFF -> 4 (official behavior), ON -> 10.
     */
    public static int getEffectiveMax() {
        return Flags.MIAO_AC_1.isEnabled() ? EXPANDED_MAX : OFFICIAL_MAX;
    }

    /**
     * Whether the expanded multi-account feature is enabled.
     * Used by upstream hooks to decide whether to bypass the Premium account-slot
     * gate (the "add account" flow normally deducts non-premium slots and shows a
     * paywall; when expanded accounts is on, we skip that).
     */
    public static boolean isExpandedEnabled() {
        return Flags.MIAO_AC_1.isEnabled();
    }
}
