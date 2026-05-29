/*
 * MiaoGram custom code.
 * Source of a feature flag's value.
 *
 * Defines where a flag's value is read from (and whether the user can override it locally).
 * Stage 6 only implements LOCAL; REMOTE_* values are reserved for Stage 8+ when we add
 * a server-side config endpoint.
 */
package com.miaogram.miao.flags;

public enum FlagSource {

    /**
     * Flag is fully controlled locally via SharedPreferences.
     * The user toggles it from the MiaoGram settings UI.
     * No network involvement.
     */
    LOCAL,

    /**
     * Flag has a default value pushed by the server, but the user can override it locally.
     * Read order: local override (if set) -> remote default -> static default.
     * Stage 6: behaves as LOCAL until remote config is wired up.
     */
    REMOTE_OVERRIDABLE,

    /**
     * Flag is exclusively controlled by the server. Used for emergency kill switches and
     * compliance-driven gating (e.g., temporarily disabling a feature globally).
     * The user cannot toggle it from settings.
     * Stage 6: behaves as the static default (false) until remote config is wired up.
     */
    REMOTE_ONLY,
}
