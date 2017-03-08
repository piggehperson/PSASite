package com.piggeh.palmettoscholars.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.piggeh.palmettoscholars.R;
import com.piggeh.palmettoscholars.fragments.SettingsActivityFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content.
        if (savedInstanceState == null){
            getFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout_settingsFragment, new SettingsActivityFragment())
                    .commit();
        }
    }
}
