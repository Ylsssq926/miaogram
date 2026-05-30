/*
 * MiaoGram custom code.
 * Strips tracking query parameters from outbound URLs.
 *
 * When enabled (Flags.MIAO_UI_7, default ON), known tracking params like
 * utm_*, fbclid, gclid, igshid etc. are removed before opening external links.
 *
 * Only applied to EXTERNAL urls (the caller must skip internal t.me/tg:// links).
 * Purely local, no ToS risk.
 */
package com.miaogram.miao.feature;

import android.net.Uri;
import android.text.TextUtils;

import com.miaogram.miao.flags.Flags;

import java.util.Set;

public final class UrlSanitizer {

    private UrlSanitizer() {
        // utility class
    }

    // Exact-match tracking params to drop.
    private static final Set<String> EXACT = new java.util.HashSet<>(java.util.Arrays.asList(
            "fbclid", "gclid", "dclid", "gbraid", "wbraid",
            "msclkid", "igshid", "mc_eid", "mc_cid",
            "yclid", "_openstat", "twclid", "ttclid", "scid",
            "vero_id", "oly_anon_id", "oly_enc_id", "rb_clickid",
            "s_cid", "wickedid", "hsa_cam", "hsa_grp", "ref_src", "ref_url"
    ));

    // Prefix-match tracking params to drop (e.g. utm_source, utm_medium...).
    private static final String[] PREFIX = {
            "utm_", "pk_", "mtm_", "matomo_", "piwik_", "ga_", "_hs"
    };

    public static boolean isEnabled() {
        return Flags.MIAO_UI_7.isEnabled();
    }

    private static boolean isTracking(String key) {
        if (key == null) return false;
        String k = key.toLowerCase(java.util.Locale.ROOT).trim();
        if (EXACT.contains(k)) return true;
        for (String p : PREFIX) {
            if (k.startsWith(p)) return true;
        }
        return false;
    }

    /**
     * Returns a copy of the uri with tracking params removed.
     * If nothing to strip / feature off / uri has no query, returns the original uri.
     */
    public static Uri sanitize(Uri uri) {
        if (!isEnabled() || uri == null || uri.isOpaque()) {
            return uri;
        }
        String query = uri.getQuery();
        if (TextUtils.isEmpty(query)) {
            return uri;
        }
        Set<String> names;
        try {
            names = uri.getQueryParameterNames();
        } catch (Exception e) {
            return uri;
        }
        if (names.isEmpty()) {
            return uri;
        }
        boolean changed = false;
        Uri.Builder builder = uri.buildUpon().clearQuery();
        for (String name : names) {
            if (isTracking(name)) {
                changed = true;
                continue;
            }
            for (String value : uri.getQueryParameters(name)) {
                builder.appendQueryParameter(name, value);
            }
        }
        return changed ? builder.build() : uri;
    }
}
