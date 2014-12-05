package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import it.rainbowbreeze.picama.R;

/**
 * Created by alfredomorresi on 05/12/14.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String PREFKEY_SYNC_FREQUENCY = "pref_syncFrequency";
    private Preference mPreSyncFrequency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
        mPreSyncFrequency = findPreference(PREFKEY_SYNC_FREQUENCY);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // See http://developer.android.com/reference/android/preference/PreferenceManager.html#KEY_HAS_SET_DEFAULT_VALUES
        PreferenceManager.setDefaultValues(activity, R.xml.pref_general, false);
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
        if (PREFKEY_SYNC_FREQUENCY.equals(key)) {
            Preference preference = findPreference(key);
            mPreSyncFrequency.setSummary(R.string.pref_storage_summary);
        }
    }
}
