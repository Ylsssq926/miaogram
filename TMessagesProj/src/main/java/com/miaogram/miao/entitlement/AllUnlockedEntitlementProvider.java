/*
 * MiaoGram custom code.
 * Bootstrap entitlement provider: every access tier is available.
 */
package com.miaogram.miao.entitlement;

import androidx.annotation.NonNull;

import com.miaogram.miao.flags.FlagTier;

final class AllUnlockedEntitlementProvider implements EntitlementProvider {
    @Override
    public boolean isUnlocked(@NonNull FlagTier tier) {
        return true;
    }
}
