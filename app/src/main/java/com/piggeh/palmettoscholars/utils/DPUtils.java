package com.piggeh.palmettoscholars.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by peter on 5/25/2016.
 */
public class DPUtils {
    public static int convertDpToPx(float dp){
        // Get the screen's density scale
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        // Convert dps to pixels, based on density scale
        int px = (int) (dp * scale + 0.5f);
        return px;
    }
    public static float convertPxToDp(float px){
        //Get display metrics
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        //Convert pixels to dps
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}