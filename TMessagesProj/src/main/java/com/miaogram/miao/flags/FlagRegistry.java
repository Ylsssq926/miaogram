/*
 * MiaoGram custom code.
 * Central registry for all feature flags.
 *
 * Responsibilities:
 *   - Hold a thread-safe map of flag-key -> Flag instance.
 *   - Resolve a flag's effective value based on its FlagSource.
 *   - Persist flag overrides to SharedPreferences (for LOCAL / REMOTE_OVERRIDABLE).
 *
 * Lookup logic (Stage 6, no remote yet):
 *   LOCAL:                local prefs if set, else default.
 *   REMOTE_OVERRIDABLE:   local prefs if set, else default. (same as LOCAL until remote arrives)
 *   REMOTE_ONLY:          default. (user override is rejected)
 *
 * Why a static class instead of a singleton?
 * Same reasoning as MiaoConfig: SharedPreferences is already a process-wide global,
 * adding a singleton wrapper provides no value but adds boilerplate.
 *
 * Thread safety: ConcurrentHashMap + immutable Flag instances are safe to read concurrently.
 * Writes go through SharedPreferences.apply() which is atomic.
 */
package com.miaogram.miao.flags;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miaogram.miao.utils.MiaoLogger;
import com.miaogram.miao.utils.MiaoSharedPrefs;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FlagRegistry {

    private static final Map<String, Flag> REGISTRY = new ConcurrentHashMap<>();

    private FlagRegistry() {
        // utility class
    }

    /**
     * Register a Flag. Called from Flags.define(...). Throws if the key is already registered
     * (catches typos where two Flag fields accidentally use the same key).
     */
    static Flag register(@NonNull Flag flag) {
        Flag existing = REGISTRY.putIfAbsent(flag.getKey(), flag);
        if (existing != null && existing != flag) {
            throw new IllegalStateException(
                    "Flag key collision: '" + flag.getKey() + "' already registered as " + existing
            );
        }
        return flag;
    }

    /**
     * Look up a flag by its key. Returns null if not registered.
     * Mostly useful for debugging / settings UI iteration.
     */
    @Nullable
    public static Flag find(@NonNull String key) {
        return REGISTRY.get(key);
    }

    /**
     * Snapshot of all registered flags (read-only).
     * Used by the settings UI to render the full list.
     */
    @NonNull
    public static Map<String, Flag> snapshot() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    /**
     * Resolve the effective enabled state of a flag.
     */
    public static boolean isEnabled(@NonNull Flag flag) {
        switch (flag.getSource()) {
            case LOCAL:
            case REMOTE_OVERRIDABLE:
                return MiaoSharedPrefs.getBoolean(flag.getKey(), flag.getDefaultValue());
            case REMOTE_ONLY:
                // Stage 6: no remote source yet, return static default.
                return flag.getDefaultValue();
            default:
                return flag.getDefaultValue();
        }
    }

    /**
     * Set a flag's value.
     * For REMOTE_ONLY flags this is a no-op (with a warning), since user cannot override server.
     */
    public static void set(@NonNull Flag flag, boolean enabled) {
        if (flag.getSource() == FlagSource.REMOTE_ONLY) {
            MiaoLogger.w("FlagRegistry",
                    "ignored set() on REMOTE_ONLY flag: " + flag.getKey());
            return;
        }
        MiaoSharedPrefs.putBoolean(flag.getKey(), enabled);
        MiaoLogger.d("FlagRegistry",
                "flag " + flag.getKey() + " -> " + enabled);
    }

    /**
     * Clear the local override for a flag (revert to default / remote).
     */
    public static void clearOverride(@NonNull Flag flag) {
        MiaoSharedPrefs.remove(flag.getKey());
        MiaoLogger.d("FlagRegistry", "cleared override for " + flag.getKey());
    }
}
