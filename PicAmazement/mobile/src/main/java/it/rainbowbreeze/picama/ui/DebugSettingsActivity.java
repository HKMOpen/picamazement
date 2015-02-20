package it.rainbowbreeze.picama.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import it.rainbowbreeze.picama.R;

public class DebugSettingsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_generic_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DebugSettingsFragment())
                    .commit();
        }
    }


}
