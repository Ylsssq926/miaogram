/*
 * MiaoGram custom code.
 * Metadata + accessor for a single feature flag.
 *
 * A Flag is an immutable description: which key it lives under, what its
 * default value is, and where its value comes from.
 *
 * The actual value lookup is delegated to FlagRegistry, so adding remote
 * config later doesn't change Flag's API.
 *
 * Construct only via Flags.define(...) which auto-registers with FlagRegistry.
 */
package com.miaogram.miao.flags;

import androidx.annotation.NonNull;

public final class Flag {

    @NonNull private final String key;
    private final boolean defaultValue;
    @NonNull private final FlagSource source;

    /** Package-private: only Flags.define(...) should call this. */
    Flag(@NonNull String key, boolean defaultValue, @NonNull FlagSource source) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.source = source;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }

    @NonNull
    public FlagSource getSource() {
        return source;
    }

    /** Convenience: query current value via the registry. */
    public boolean isEnabled() {
        return FlagRegistry.isEnabled(this);
    }

    /**
     * Convenience: write current value via the registry.
     * For REMOTE_ONLY flags this is a no-op (with a logger warning).
     */
    public void setEnabled(boolean enabled) {
        FlagRegistry.set(this, enabled);
    }

    @Override
    public String toString() {
        return "Flag{" + key + ", default=" + defaultValue + ", source=" + source + "}";
    }
}
