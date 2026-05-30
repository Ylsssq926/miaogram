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

    /** ac:2 — see .private/flag-mapping.md */
    public static final Flag MIAO_AC_2 = define("miao_ac_2", true, FlagSource.LOCAL);

    /** ac:3 — see .private/flag-mapping.md */
    public static final Flag MIAO_AC_3 = define("miao_ac_3", true, FlagSource.LOCAL);

    // ----- UI domain ----------------------------------------------------------------------------

    /** ui:1 — see .private/flag-mapping.md */
    public static final Flag MIAO_UI_1 = define("miao_ui_1", false, FlagSource.LOCAL);

    /** ui:3 — see .private/flag-mapping.md (ui:2 was reserved for a dropped feature, not reused) */
    public static final Flag MIAO_UI_3 = define("miao_ui_3", false, FlagSource.LOCAL);

    /** ui:4 — see .private/flag-mapping.md */
    public static final Flag MIAO_UI_4 = define("miao_ui_4", false, FlagSource.LOCAL);

    /** ui:5 — see .private/flag-mapping.md */
    public static final Flag MIAO_UI_5 = define("miao_ui_5", false, FlagSource.LOCAL);

    /** ui:6 — see .private/flag-mapping.md */
    public static final Flag MIAO_UI_6 = define("miao_ui_6", false, FlagSource.LOCAL);

    /** ui:7 — see .private/flag-mapping.md */
    public static final Flag MIAO_UI_7 = define("miao_ui_7", true, FlagSource.LOCAL);

    /** ui:8 — see .private/flag-mapping.md */
    public static final Flag MIAO_UI_8 = define("miao_ui_8", true, FlagSource.LOCAL);

    /** ui:9 — see .private/flag-mapping.md */
    public static final Flag MIAO_UI_9 = define("miao_ui_9", false, FlagSource.LOCAL);

    // ----- Platform feature domain (gray-area, default OFF) -------------------------------------

    /** pf:1 — see .private/flag-mapping.md */
    public static final Flag MIAO_PF_1 = define("miao_pf_1", false, FlagSource.LOCAL);

    /** pf:2 — see .private/flag-mapping.md */
    public static final Flag MIAO_PF_2 = define("miao_pf_2", false, FlagSource.LOCAL);

    /** pf:3 — see .private/flag-mapping.md */
    public static final Flag MIAO_PF_3 = define("miao_pf_3", false, FlagSource.LOCAL);

    /** pf:4 — see .private/flag-mapping.md */
    public static final Flag MIAO_PF_4 = define("miao_pf_4", false, FlagSource.LOCAL);

    /** pf:5 — see .private/flag-mapping.md */
    public static final Flag MIAO_PF_5 = define("miao_pf_5", false, FlagSource.LOCAL);

    /** pf:6 — see .private/flag-mapping.md */
    public static final Flag MIAO_PF_6 = define("miao_pf_6", true, FlagSource.LOCAL);

    // ----- Message domain -----------------------------------------------------------------------

    /** ms:1 — see .private/flag-mapping.md */
    public static final Flag MIAO_MS_1 = define("miao_ms_1", false, FlagSource.LOCAL);

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
        Flag[] declared = {
                MIAO_AC_1, MIAO_AC_2, MIAO_AC_3,
                MIAO_UI_1, MIAO_UI_3, MIAO_UI_4, MIAO_UI_5, MIAO_UI_6, MIAO_UI_7, MIAO_UI_8, MIAO_UI_9,
                MIAO_PF_1, MIAO_PF_2, MIAO_PF_3, MIAO_PF_4, MIAO_PF_5, MIAO_PF_6,
                MIAO_MS_1,
        };
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
