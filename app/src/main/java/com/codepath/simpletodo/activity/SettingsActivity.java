package com.codepath.simpletodo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.codepath.simpletodo.R;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFERENCE_NAME = "com.codepath.simpletodo.PREFS_SCREEN";
    public static final String PREFERENCE_SCREEN_TYPE = "preference_screen_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.
            PreferenceManager.setDefaultValues(getActivity(), PREFERENCE_NAME, MODE_PRIVATE, R.xml.preferences, false);

            // Sets the name of the SharedPreferences file
            getPreferenceManager().setSharedPreferencesName(PREFERENCE_NAME);

            addPreferencesFromResource(R.xml.preferences);

            updateListPrefScreenTypeSummary();
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (PREFERENCE_SCREEN_TYPE.equals(key)) {
                updateListPrefScreenTypeSummary();
            }
        }

        private void updateListPrefScreenTypeSummary() {
            ListPreference screenTypePref = (ListPreference) findPreference(PREFERENCE_SCREEN_TYPE);
            screenTypePref.setSummary(screenTypePref.getEntry());
        }
    }
}
