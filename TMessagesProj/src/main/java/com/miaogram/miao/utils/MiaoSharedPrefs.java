/*
 * MiaoGram custom code.
 * SharedPreferences facade.
 *
 * Provides read/write access to a dedicated SharedPreferences file named "miaogram_config",
 * isolated from upstream Telegram preferences (which use names like "mainconfig", "userconfig", etc.).
 *
 * Why a separate prefs file?
 * 1. Isolation: Telegram's prefs files are heavily structured; mixing in our keys risks accidental
 *    collisions with upstream keys.
 * 2. Easier reset: clearing MiaoGram-specific settings is just one prefs file deletion.
 * 3. Easier backup/export: future feature to export all MiaoGram settings as a single file.
 *
 * Thread safety: SharedPreferences is internally thread-safe for concurrent read/write,
 * but apply() vs commit() callers must handle ordering themselves.
 */
package com.miaogram.miao.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.messenger.ApplicationLoader;

public final class MiaoSharedPrefs {

    /** SharedPreferences file name. Must NOT clash with any upstream Telegram prefs file. */
    public static final String PREFS_NAME = "miaogram_config";

    private static volatile SharedPreferences instance;

    private MiaoSharedPrefs() {
        // utility class
    }

    /**
     * Returns the singleton SharedPreferences for MiaoGram config.
     * Lazy-initialized; safe to call before ApplicationLoader.applicationContext is set
     * (returns null in that rare case, callers should null-check).
     */
    public static SharedPreferences get() {
        if (instance == null) {
            synchronized (MiaoSharedPrefs.class) {
                if (instance == null) {
                    Context ctx = ApplicationLoader.applicationContext;
                    if (ctx != null) {
                        instance = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    }
                }
            }
        }
        return instance;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = get();
        return sp != null ? sp.getBoolean(key, defaultValue) : defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = get();
        return sp != null ? sp.getInt(key, defaultValue) : defaultValue;
    }

    public static long getLong(String key, long defaultValue) {
        SharedPreferences sp = get();
        return sp != null ? sp.getLong(key, defaultValue) : defaultValue;
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences sp = get();
        return sp != null ? sp.getString(key, defaultValue) : defaultValue;
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sp = get();
        if (sp != null) {
            sp.edit().putBoolean(key, value).apply();
        }
    }

    public static void putInt(String key, int value) {
        SharedPreferences sp = get();
        if (sp != null) {
            sp.edit().putInt(key, value).apply();
        }
    }

    public static void putLong(String key, long value) {
        SharedPreferences sp = get();
        if (sp != null) {
            sp.edit().putLong(key, value).apply();
        }
    }

    public static void putString(String key, String value) {
        SharedPreferences sp = get();
        if (sp != null) {
            sp.edit().putString(key, value).apply();
        }
    }

    public static boolean contains(String key) {
        SharedPreferences sp = get();
        return sp != null && sp.contains(key);
    }

    public static void remove(String key) {
        SharedPreferences sp = get();
        if (sp != null) {
            sp.edit().remove(key).apply();
        }
    }
}
