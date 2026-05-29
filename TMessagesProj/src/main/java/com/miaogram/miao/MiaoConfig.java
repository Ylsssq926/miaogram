/*
 * MiaoGram custom code.
 * Top-level facade for MiaoGram configuration.
 *
 * This is the single public entry point for the rest of the codebase to query
 * MiaoGram-specific settings and feature flags.
 *
 * Design notes:
 * - This class is intentionally THIN. It delegates to FlagRegistry / MiaoSharedPrefs.
 * - Why a facade? So when we add a new flag, business code only imports MiaoConfig
 *   and never the FlagRegistry implementation. This keeps consumers decoupled from
 *   how flags are stored (local prefs now, possibly remote config later).
 * - It's deliberately a static-method holder rather than a singleton instance, because
 *   ApplicationLoader.applicationContext is already a process-wide global; wrapping it
 *   in another singleton adds no value but adds boilerplate.
 *
 * Usage:
 *   if (MiaoConfig.isFeatureEnabled(Flags.MIAO_AC_1)) { ... }
 *   MiaoConfig.setFeatureEnabled(Flags.MIAO_AC_1, true);
 *
 *   // String-key form (when iterating over arbitrary flags):
 *   if (MiaoConfig.isFeatureEnabled("miao_ac_1")) { ... }
 *
 * Future expansion (not in Stage 6):
 * - reload() method to re-fetch remote config
 * - registerListener() for live flag changes
 */
package com.miaogram.miao;

import androidx.annotation.NonNull;

import com.miaogram.miao.flags.Flag;
import com.miaogram.miao.flags.FlagRegistry;
import com.miaogram.miao.utils.MiaoLogger;
import com.miaogram.miao.utils.MiaoSharedPrefs;

public final class MiaoConfig {

    private MiaoConfig() {
        // facade class, do not instantiate
    }

    /** Type-safe form: query a specific Flag instance. */
    public static boolean isFeatureEnabled(@NonNull Flag flag) {
        return FlagRegistry.isEnabled(flag);
    }

    /**
     * String-key form: look up a flag by key. Returns false if the key is not registered
     * (with a warning) — callers should prefer the Flag-instance form when possible.
     */
    public static boolean isFeatureEnabled(@NonNull String flagKey) {
        Flag flag = FlagRegistry.find(flagKey);
        if (flag == null) {
            MiaoLogger.w("MiaoConfig",
                    "isFeatureEnabled called with unknown flag key: " + flagKey);
            return false;
        }
        return FlagRegistry.isEnabled(flag);
    }

    /** Type-safe form: set a Flag instance's value. */
    public static void setFeatureEnabled(@NonNull Flag flag, boolean enabled) {
        FlagRegistry.set(flag, enabled);
    }

    /**
     * String-key form: set a flag by key. No-op (with warning) if the key is not registered.
     */
    public static void setFeatureEnabled(@NonNull String flagKey, boolean enabled) {
        Flag flag = FlagRegistry.find(flagKey);
        if (flag == null) {
            MiaoLogger.w("MiaoConfig",
                    "setFeatureEnabled called with unknown flag key: " + flagKey);
            return;
        }
        FlagRegistry.set(flag, enabled);
    }

    /**
     * Returns true if any MiaoGram-specific config exists (e.g., user has visited the settings page).
     * Used by upstream hooks to skip MiaoGram-specific code paths cheaply on fresh installs.
     */
    public static boolean hasAnyConfig() {
        return MiaoSharedPrefs.get() != null && !MiaoSharedPrefs.get().getAll().isEmpty();
    }
}

