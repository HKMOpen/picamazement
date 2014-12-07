package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.logic.storage.DropboxCloudProvider;

/**
* Created by alfredomorresi on 05/12/14.
*/
public class DropboxSettingsFragment extends Fragment {
    @Inject ILogFacility mLogFacility;
    @Inject DropboxCloudProvider mDropboxCloudProvider;
    private Context mAppContext;
    private Button mBtnAuthorize;

    public DropboxSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_dropbox_settings, container, false);
        mDropboxCloudProvider.initDropboxApi();
        mBtnAuthorize = (Button) rootView.findViewById(R.id.dropboxsettings_btnStartAuth);
        mBtnAuthorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropboxCloudProvider.startDropboxAuthFlow(mAppContext);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAppContext = activity.getApplicationContext();
        ((MyApp) mAppContext).inject(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        boolean result = mDropboxCloudProvider.finishDropboxAuthFlow();
        setAuthButton();
    }

    private void setAuthButton() {
        if (!mDropboxCloudProvider.isAuthRequired()) {
            mBtnAuthorize.setText(R.string.dropboxsettings_btnAuthDone);
            mBtnAuthorize.setEnabled(false);
        }
    }
}
