package com.piggeh.palmettoscholars.classes;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;

import com.piggeh.palmettoscholars.R;

public class ConfigUtils {
    public static boolean isLarge(Context context){
        return context.getResources().getBoolean(R.bool.configuration_islarge);
    }
    public static boolean hasTouch(Context context){
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN);
    }

    public static final int DATA_SAVER_OFF = 0;
    public static final int DATA_SAVER_METERED = 1;
    public static final int DATA_SAVER_WHITELISTED = 2;
    public static final int DATA_SAVER_RESTRICTED = 3;
    public static int getDataSaverState(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            ConnectivityManager connMgr = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // Checks if the device is on a metered network
            if (connMgr.isActiveNetworkMetered()) {
                // Checks user’s Data Saver settings.
                switch (connMgr.getRestrictBackgroundStatus()) {
                    case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED:
                        // Background data usage is blocked for this app. Wherever possible,
                        // the app should also use less data in the foreground.
                        return DATA_SAVER_RESTRICTED;
                    case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED:
                        // The app is whitelisted. Wherever possible,
                        // the app should use less data in the foreground and background.
                        return DATA_SAVER_WHITELISTED;
                    case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED:
                        // Data Saver is disabled. Since the device is connected to a
                        // metered network, the app should use less data wherever possible.
                        return DATA_SAVER_METERED;
                }
            } else {
                // The device is not on a metered network.
                // Use data as required to perform syncs, downloads, and updates.
                return DATA_SAVER_OFF;
            }
        } else{
            return DATA_SAVER_OFF;
        }
        return DATA_SAVER_OFF;
    }
    public static boolean shouldSaveData(Context context){
        switch (getDataSaverState(context)){
            default:
                return false;
            case DATA_SAVER_OFF:
                return false;
            case DATA_SAVER_METERED:
                return false;
            case DATA_SAVER_WHITELISTED:
                return true;
            case DATA_SAVER_RESTRICTED:
                return true;
        }
    }
}
