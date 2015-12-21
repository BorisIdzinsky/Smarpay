package com.smarcom.smarpay.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheHelper {

    private static final String PREFERENCES_FILENAME = "com.smarcom.smarpay.PREFERENCE_FILE_KEY";
    private static final String CACHE_COMPLETE_INIT = "CacheComplete";

    public static boolean isClearCache(Context context) {
        return (context.getCacheDir().listFiles().length == 0);
    }

    public static void setIsInitCache(Context context, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CACHE_COMPLETE_INIT, value);
        editor.apply();
    }

    public static boolean isInitCache(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE).getBoolean(CACHE_COMPLETE_INIT, false);
    }
}
