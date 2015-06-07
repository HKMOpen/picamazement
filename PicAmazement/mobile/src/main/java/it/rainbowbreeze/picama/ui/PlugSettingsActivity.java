package it.rainbowbreeze.picama.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.Bag;

public class PlugSettingsActivity extends InjectableActivity {
    private static final String LOG_TAG = PlugSettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLogFacility.v(LOG_TAG, "Action invoked: " + getIntent().getAction());

        setContentView(R.layout.act_plus_settings);

        // Toolbar help:
        //  http://android-developers.blogspot.it/2014/10/appcompat-v21-material-design-for-pre.html
        Toolbar toolbar = (Toolbar) findViewById(R.id.plugsettings_toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            String invokeAction = getIntent().getAction();
            if (Bag.ACTION_SETTINGS_DEBUG.equals(invokeAction)) {
                fragment = new DebugSettingsFragment();
                setTitle(R.string.debugsettings_actTitle);

            } else if (Bag.ACTION_SETTINGS_TWITTER.equals(invokeAction)) {
                fragment = new TwitterSettingsFragment();
                setTitle(R.string.twittersettings_actTitle);

            } else if (Bag.ACTION_SETTINGS_ONEBIGPICTURE.equals(invokeAction)) {
                fragment = new OneBigPhotoSettingsFragment();
                setTitle(R.string.onebigpicturesettings_actTitle);

            } else if (Bag.ACTION_SETTINGS_DROPBOX.equals(invokeAction)) {
                fragment = new DropboxSettingsFragment();
                setTitle(R.string.dropboxsettings_actTitle);

            } else {
                throw new IllegalArgumentException("Settings class action is not valid: " + invokeAction);
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.plugsettings_container, fragment)
                    .commit();
        }
    }
}
