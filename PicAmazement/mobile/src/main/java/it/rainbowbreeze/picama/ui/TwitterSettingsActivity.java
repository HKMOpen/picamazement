package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.os.Bundle;

import it.rainbowbreeze.picama.R;

public class TwitterSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_twitter_settings);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TwitterSettingsFragment())
                    .commit();
        }
    }


}
