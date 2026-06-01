/*
 * MiaoGram custom code.
 * Central entitlement facade for feature access decisions.
 *
 * Stage bootstrap intentionally grants access to every tier so local testing and
 * existing feature behavior remain unchanged. Future providers should be wired
 * here; business code should continue to ask Flag / MiaoConfig rather than
 * checking provider-specific state directly.
 */
package com.miaogram.miao.entitlement;

import androidx.annotation.NonNull;

import com.miaogram.miao.flags.Flag;
import com.miaogram.miao.flags.FlagTier;

public final class MiaoEntitlements {

    private static final EntitlementProvider PROVIDER = new AllUnlockedEntitlementProvider();

    private MiaoEntitlements() {
        // facade class, do not instantiate
    }

    /** Returns whether the given access tier is currently available. */
    public static boolean isUnlocked(@NonNull FlagTier tier) {
        return PROVIDER.isUnlocked(tier);
    }

    /**
     * Returns whether the current install has access to PRO-tier features.
     * Bootstrap builds are intentionally all-access until a real provider exists.
     */
    public static boolean isProUnlocked() {
        return isUnlocked(FlagTier.PRO);
    }

    /** Returns whether a feature can be used under the current entitlement state. */
    public static boolean canUse(@NonNull Flag flag) {
        return isUnlocked(flag.getTier());
    }
}
