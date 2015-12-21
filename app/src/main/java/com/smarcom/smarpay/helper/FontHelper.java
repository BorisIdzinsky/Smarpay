package com.smarcom.smarpay.helper;

import android.content.Context;
import android.graphics.Typeface;

public class FontHelper {

    public static Typeface getBoldFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/MissionGothicBold.otf");
    }

    public static Typeface getRegularFont(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/MissionGothicRegular.otf");
    }
}
