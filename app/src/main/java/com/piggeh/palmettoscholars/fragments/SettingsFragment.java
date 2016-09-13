package com.piggeh.palmettoscholars.fragments;

import android.os.Bundle;
/*import android.preference.PreferenceFragment;*/
import android.support.v7.preference.PreferenceFragmentCompat;
/*import android.support.v4.preference.PreferenceFragment;*/

import com.piggeh.palmettoscholars.R;

/**
 * Created by peter on 9/8/2016.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }*/

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences_compat, rootKey);
    }
}