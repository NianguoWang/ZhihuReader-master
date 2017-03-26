package com.wangng.zhihureader.data.local;

import android.content.Context;

/**
 * Created by 小爱 on 2017/2/25.
 */

public class PreferenceUtil {

    private PreferenceUtil() {}

    public static void putBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences("setting_options", Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public static void putInt(Context context, String key, int value) {
        context.getSharedPreferences("setting_options", Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    public static void putString(Context context, String key, String value) {
        context.getSharedPreferences("setting_options", Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static boolean getBoolean(Context context,String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return context.getSharedPreferences("setting_options", Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return context.getSharedPreferences("setting_options", Context.MODE_PRIVATE).getInt(key, defaultValue);
    }

    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return context.getSharedPreferences("setting_options", Context.MODE_PRIVATE).getString(key, defaultValue);
    }
}
