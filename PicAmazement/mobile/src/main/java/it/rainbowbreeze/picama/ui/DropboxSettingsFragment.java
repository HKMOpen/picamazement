package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class DropboxSettingsFragment extends Fragment {
    private static final String LOG_TAG = DropboxSettingsFragment.class.getSimpleName();

    private static final int DBX_CHOOSER_REQUEST = 0;  // You can change this if needed


    @Inject ILogFacility mLogFacility;
    @Inject DropboxCloudProvider mDropboxCloudProvider;
    @Inject AppPrefsManager mAppPrefsManager;
    private DbxChooser mChooser;
    private Context mAppContext;
    private Button mBtnAuthorize;
    private View mLayParameters;
    private TextView mLblSavePath;

    public DropboxSettingsFragment() {
        mChooser = new DbxChooser(BuildConfig.DROPBOX_APP_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_dropbox_settings, container, false);
        mLayParameters = rootView.findViewById(R.id.dropboxsettings_layParameters);
        mBtnAuthorize = (Button) rootView.findViewById(R.id.dropboxsettings_btnStartAuth);
        mBtnAuthorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropboxCloudProvider.startDropboxAuthFlow(mAppContext);
            }
        });

        Button button = (Button) rootView.findViewById(R.id.dropboxsettings_btnSelectSaveDir);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChooser.forResultType(DbxChooser.ResultType.DIRECT_LINK)
                        .launch(getActivity(), DBX_CHOOSER_REQUEST);
                /*
                 Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/myFolder/");
                 Intent intent = new Intent(Intent.ACTION_VIEW);
                 intent.setDataAndType(selectedUri, "resource/folder");
                 */
            }
        });

        mLblSavePath = (TextView) rootView.findViewById(R.id.dropboxsettings_lblSaveDir);
        mLblSavePath.setText(mAppPrefsManager.getDropboxSavePath());

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(data);
                mLogFacility.v(LOG_TAG, "Link to selected file: " + result.getLink());

                mLblSavePath.setText(result.getName().toString(), TextView.BufferType.NORMAL);
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
        mAppContext = activity.getApplicationContext();
        ((MyApp) mAppContext).inject(this);

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
        mLayParameters.setVisibility(
                mDropboxCloudProvider.isAuthRequired()
                        ? View.GONE
                        : View.VISIBLE
        );
    }
}
