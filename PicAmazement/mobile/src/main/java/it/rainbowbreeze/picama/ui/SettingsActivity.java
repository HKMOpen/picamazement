package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.os.Bundle;

import it.rainbowbreeze.picama.R;

/**
 * Created by alfredomorresi on 05/12/14.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
