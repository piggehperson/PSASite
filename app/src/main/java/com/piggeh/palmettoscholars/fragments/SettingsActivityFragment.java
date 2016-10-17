package com.piggeh.palmettoscholars.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.piggeh.palmettoscholars.R;

public class SettingsActivityFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}