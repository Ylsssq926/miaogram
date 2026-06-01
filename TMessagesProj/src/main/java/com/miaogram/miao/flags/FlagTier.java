/*
 * MiaoGram custom code.
 * Static access-tier metadata for feature flags.
 *
 * This enum describes what tier a feature belongs to. It does not store or
 * verify any user state; runtime access decisions live in the entitlement
 * facade so future providers can be wired without changing each call site.
 */
package com.miaogram.miao.flags;

public enum FlagTier {
    FREE,
    PRO
}
