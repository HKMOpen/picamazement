package it.rainbowbreeze.picama.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import it.rainbowbreeze.picama.R;

public class DropboxSettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_generic_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DropboxSettingsFragment())
                    .commit();
        }
    }
}
