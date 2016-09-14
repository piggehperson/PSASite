package com.piggeh.palmettoscholars.classes;

import android.content.Context;
import android.content.pm.PackageManager;

import com.piggeh.palmettoscholars.R;

public class ConfigUtils {
    public static boolean isLarge(Context context){
        return context.getResources().getBoolean(R.bool.configuration_islarge);
    }
    public static boolean hasTouch(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN);
    }
}
