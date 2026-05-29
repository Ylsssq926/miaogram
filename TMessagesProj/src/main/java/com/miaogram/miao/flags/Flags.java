/*
 * MiaoGram custom code.
 * Central declaration of all feature flags.
 *
 * Naming convention: MIAO_<DOMAIN>_<NUMBER>
 *   ac = account (multi-account, account UI)
 *   ui = user interface enhancements
 *   pf = platform feature (gray-area features that need careful handling)
 *   sf = sensitive feature (managed via .private/, see SENSITIVE-CONTENT-POLICY.md)
 *   dl = download
 *   tr = translation
 *   ms = message-related
 *
 * Numbers are sequential within a domain, never reused, never imply priority.
 *
 * The mapping from flag key to actual feature description lives in
 * .private/flag-mapping.md (NOT in git). This is by design (see ADR-002).
 *
 * Adding a new flag:
 *   1. Pick a domain prefix (ac/ui/pf/...).
 *   2. Find the next free number for that domain.
 *   3. Add a `public static final Flag MIAO_<DOM>_<N>` here using define(...).
 *   4. Update .private/flag-mapping.md with what the flag actually does.
 *   5. Use it from business code: if (Flags.MIAO_AC_1.isEnabled()) { ... }
 */
package com.miaogram.miao.flags;

public final class Flags {

    // ----- Account domain -----------------------------------------------------------------------

    /** ac:1 — see .private/flag-mapping.md */
    public static final Flag MIAO_AC_1 = define("miao_ac_1", false, FlagSource.LOCAL);

    // ----- UI domain ----------------------------------------------------------------------------

    /** ui:1 — see .private/flag-mapping.md */
    public static final Flag MIAO_UI_1 = define("miao_ui_1", false, FlagSource.LOCAL);

    // ----- (More flags will be added here as features are implemented) --------------------------

    // ============================================================================================

    private static Flag define(String key, boolean defaultValue, FlagSource source) {
        return FlagRegistry.register(new Flag(key, defaultValue, source));
    }

    /**
     * Runtime self-check. Verifies the framework is wired up correctly:
     *   1. All flag fields declared above are registered with FlagRegistry.
     *   2. SharedPreferences round-trip works (write -> read -> delete).
     *   3. Looking up an unregistered key returns null.
     *
     * Called once from ApplicationLoader on debug builds (see ADR-004 in architecture-design.md).
     * Throws if anything is wrong, so misconfiguration crashes the debug build immediately
     * instead of silently misbehaving at runtime.
     *
     * On release builds this method is still callable but should be skipped
     * to avoid overhead (caller should gate on BuildConfig.DEBUG).
     */
    public static void selfCheck() {
        // 1. Verify all known flags are registered.
        Flag[] declared = { MIAO_AC_1, MIAO_UI_1 };
        for (Flag f : declared) {
            if (FlagRegistry.find(f.getKey()) == null) {
                throw new IllegalStateException(
                        "Flags.selfCheck: " + f.getKey() + " not registered");
            }
        }

        // 2. SharedPreferences round-trip.
        String probeKey = "miao_selfcheck_probe";
        com.miaogram.miao.utils.MiaoSharedPrefs.putBoolean(probeKey, true);
        boolean roundTrip = com.miaogram.miao.utils.MiaoSharedPrefs.getBoolean(probeKey, false);
        com.miaogram.miao.utils.MiaoSharedPrefs.remove(probeKey);
        if (!roundTrip) {
            throw new IllegalStateException(
                    "Flags.selfCheck: SharedPreferences round-trip failed");
        }

        // 3. Unknown flag key returns null.
        if (FlagRegistry.find("miao_definitely_does_not_exist_xyz") != null) {
            throw new IllegalStateException(
                    "Flags.selfCheck: FlagRegistry.find returned non-null for unknown key");
        }

        com.miaogram.miao.utils.MiaoLogger.d("Flags",
                "selfCheck OK, " + FlagRegistry.snapshot().size() + " flag(s) registered");
    }

    private Flags() {
        // do not instantiate
    }
}
