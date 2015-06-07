package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dropbox.chooser.android.DbxChooser;

import javax.inject.Inject;

import it.rainbowbreeze.picama.BuildConfig;
import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AppPrefsManager;
import it.rainbowbreeze.picama.logic.storage.DropboxCloudProvider;

/**
* Created by alfredomorresi on 05/12/14.
*/
public class DropboxSettingsFragment extends InjectableFragment {
    private static final String LOG_TAG = DropboxSettingsFragment.class.getSimpleName();

    private static final int DBX_CHOOSER_REQUEST = 0;  // You can change this if needed


    @Inject DropboxCloudProvider mDropboxCloudProvider;
    @Inject AppPrefsManager mAppPrefsManager;
    private DbxChooser mChooser;
    private CheckBox mChkSaveEnabled;
    private Button mBtnAuthorize;

    public DropboxSettingsFragment() {
        mChooser = new DbxChooser(BuildConfig.DROPBOX_APP_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_dropbox_settings, container, false);

        mChkSaveEnabled = (CheckBox) rootView.findViewById(R.id.dropboxsettings_chkSaveToThisStorage);
        mChkSaveEnabled.setChecked(mAppPrefsManager.isDropboxEnabled());
        mChkSaveEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppPrefsManager.setDropboxEnabled(mChkSaveEnabled.isChecked());
            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(data);
                mLogFacility.v(LOG_TAG, "Link to selected file: " + result.getLink());

            } else {
                // Failed or was cancelled by the user.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDropboxCloudProvider.initDropboxApi();
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
