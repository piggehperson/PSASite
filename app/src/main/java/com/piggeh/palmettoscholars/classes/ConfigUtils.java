package com.piggeh.palmettoscholars.classes;

import android.content.Context;

import com.piggeh.palmettoscholars.R;

/**
 * Created by peter on 9/12/2016.
 */
public class ConfigUtils {
    public static boolean isTablet(Context context){
        return context.getResources().getBoolean(R.bool.configuration_istablet);
    }
}
