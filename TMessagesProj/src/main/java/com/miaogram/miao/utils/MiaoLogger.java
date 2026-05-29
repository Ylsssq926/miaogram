/*
 * MiaoGram custom code.
 * Logger facade.
 *
 * Thin wrapper around the upstream FileLog (org.telegram.messenger.FileLog)
 * that prefixes every message with [MIAO] for easy log filtering.
 *
 * Use this for any logging from com.miaogram.miao.* code, NOT FileLog directly.
 * In debug builds: messages are also written to logcat via FileLog.
 * In release builds: only error-level messages are persisted.
 */
package com.miaogram.miao.utils;

import org.telegram.messenger.FileLog;

public final class MiaoLogger {

    private static final String PREFIX = "[MIAO] ";

    private MiaoLogger() {
        // utility class
    }

    public static void d(String message) {
        FileLog.d(PREFIX + message);
    }

    public static void d(String tag, String message) {
        FileLog.d(PREFIX + tag + ": " + message);
    }

    public static void w(String message) {
        FileLog.w(PREFIX + message);
    }

    public static void w(String tag, String message) {
        FileLog.w(PREFIX + tag + ": " + message);
    }

    public static void e(String message) {
        FileLog.e(PREFIX + message);
    }

    public static void e(String message, Throwable throwable) {
        FileLog.e(PREFIX + message, throwable);
    }

    public static void e(Throwable throwable) {
        FileLog.e(throwable);
    }
}
