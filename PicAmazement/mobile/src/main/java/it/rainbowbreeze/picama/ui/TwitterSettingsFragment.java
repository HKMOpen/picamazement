package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AppPrefsManager;

/**
* Created by alfredomorresi on 05/12/14.
*/
public class TwitterSettingsFragment extends Fragment {
    private static final String LOG_TAG = TwitterSettingsFragment.class.getSimpleName();

    @Inject ILogFacility mLogFacility;
    @Inject AppPrefsManager mAppPrefsManager;
    private Context mAppContext;

    public TwitterSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_twitter_settings, container, false);


        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAppContext = activity.getApplicationContext();
        ((MyApp) mAppContext).inject(this);
    }
}
