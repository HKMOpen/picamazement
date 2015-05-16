package it.rainbowbreeze.picama.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;

public class GenericSettingsActivity extends InjectableActivity {
    private static final String LOG_TAG = GenericSettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLogFacility.v(LOG_TAG, "Action invoked: " + getIntent().getAction());

        setContentView(R.layout.act_generic_settings);
        if (savedInstanceState == null) {

            Fragment fragment = null;
            String invokeAction = getIntent().getAction();
            if (Bag.ACTION_SETTINGS_DEBUG.equals(invokeAction)) {
                fragment = new DebugSettingsFragment();
                setTitle(R.string.debugsettings_actTitle);
            } else if (Bag.ACTION_SETTINGS_TWITTER.equals(invokeAction)) {
                fragment = new TwitterSettingsFragment();
                setTitle(R.string.twittersettings_actTitle);
            } else if (Bag.ACTION_SETTINGS_DROPBOX.equals(invokeAction)) {
                fragment = new DropboxSettingsFragment();
                setTitle(R.string.dropboxsettings_actTitle);
            } else {
                throw new IllegalArgumentException("Settings class action is not valid: " + invokeAction);
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }
}
