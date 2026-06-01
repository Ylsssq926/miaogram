/*
 * MiaoGram custom code.
 * In-memory provider for access-tier state.
 *
 * Implementations must be cheap and side-effect free: feature checks can run on
 * UI hot paths, so this interface must not perform disk or network work.
 */
package com.miaogram.miao.entitlement;

import androidx.annotation.NonNull;

import com.miaogram.miao.flags.FlagTier;

interface EntitlementProvider {
    boolean isUnlocked(@NonNull FlagTier tier);
}
