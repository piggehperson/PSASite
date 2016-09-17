package com.piggeh.palmettoscholars.fragments;

import android.os.Bundle;
/*import android.preference.PreferenceFragment;*/
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;
/*import android.support.v4.preference.PreferenceFragment;*/

import com.google.firebase.messaging.FirebaseMessaging;
import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.utils.PreferenceKeys;

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

        SwitchPreferenceCompat announcements = (SwitchPreferenceCompat) findPreference(PreferenceKeys.notifAnnouncements);
        SwitchPreferenceCompat newsletters = (SwitchPreferenceCompat) findPreference(PreferenceKeys.notifNewsletters);

        announcements.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean)newValue){
                    Log.d("SettingsFragment", "Subscribing to Announcements");
                    FirebaseMessaging.getInstance().subscribeToTopic("announcements");
                } else{
                    Log.d("SettingsFragment", "Unsubscribing from Announcements");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("announcements");
                }
                return true;
            }
        });
        newsletters.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((Boolean)newValue){
                    Log.d("SettingsFragment", "Subscribing to Newsletters");
                    FirebaseMessaging.getInstance().subscribeToTopic("newsletters");
                } else{
                    Log.d("SettingsFragment", "Unsubscribing from Newsletters");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("newsletters");
                }
                return true;
            }
        });
    }
}