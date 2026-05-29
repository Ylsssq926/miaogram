/*
 * MiaoGram custom code.
 * Build configuration facade.
 *
 * Wraps the auto-generated BuildConfig class from the upstream library module
 * and exposes Miao-specific build-time constants.
 *
 * Why a facade instead of importing BuildConfig directly?
 * 1. The library module BuildConfig only exposes DEBUG / LIBRARY_PACKAGE_NAME / etc.;
 *    application-level constants like APPLICATION_ID are not available here.
 *    We expose them via runtime Context lookups.
 * 2. Centralizing access prevents accidental imports of the wrong BuildConfig.
 * 3. Future BUILD_TYPE expansion (community / play / fdroid flavors) goes here.
 */
package com.miaogram.miao.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildConfig;

public final class MiaoBuildConfig {

    private MiaoBuildConfig() {
        // utility class
    }

    /** Whether the current build is a debug build (BuildConfig.DEBUG). */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /** The applicationId of the running app, e.g. "com.miaogram.app" or "com.miaogram.app.beta". */
    public static String getApplicationId() {
        Context ctx = ApplicationLoader.applicationContext;
        return ctx != null ? ctx.getPackageName() : "com.miaogram.app";
    }

    /** Library module package name. Always "org.telegram.messenger". */
    public static String getLibraryPackageName() {
        return BuildConfig.LIBRARY_PACKAGE_NAME;
    }

    /** Returns versionName declared in gradle.properties, e.g. "12.7.3". */
    public static String getVersionName() {
        Context ctx = ApplicationLoader.applicationContext;
        if (ctx == null) {
            return "unknown";
        }
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return pi.versionName != null ? pi.versionName : "unknown";
        } catch (PackageManager.NameNotFoundException e) {
            return "unknown";
        }
    }

    /** Returns versionCode declared in gradle.properties, e.g. 67509. */
    public static long getVersionCode() {
        Context ctx = ApplicationLoader.applicationContext;
        if (ctx == null) {
            return 0L;
        }
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P
                    ? pi.getLongVersionCode()
                    : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0L;
        }
    }
}
