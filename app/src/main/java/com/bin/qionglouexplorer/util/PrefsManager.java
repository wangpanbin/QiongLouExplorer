package com.bin.qionglouexplorer.util;

import android.content.Context;
import android.content.SharedPreferences;

/** 轻量偏好存储：种子版本、是否已完成引导。 */
public final class PrefsManager {

    private static final String PREF_NAME = "qionglou_prefs";
    private static final String KEY_SEED_VERSION = "seed_version";
    private static final String KEY_INTRO_SHOWN = "intro_shown";

    private PrefsManager() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static int getSeedVersion(Context context) {
        return prefs(context).getInt(KEY_SEED_VERSION, -1);
    }

    public static void setSeedVersion(Context context, int version) {
        prefs(context).edit().putInt(KEY_SEED_VERSION, version).apply();
    }

    public static boolean isIntroShown(Context context) {
        return prefs(context).getBoolean(KEY_INTRO_SHOWN, false);
    }

    public static void setIntroShown(Context context, boolean shown) {
        prefs(context).edit().putBoolean(KEY_INTRO_SHOWN, shown).apply();
    }
}
