package it.rainbowbreeze.picama.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import it.rainbowbreeze.picama.R;

/**
 * Created by alfredomorresi on 05/12/14.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
    }
}
