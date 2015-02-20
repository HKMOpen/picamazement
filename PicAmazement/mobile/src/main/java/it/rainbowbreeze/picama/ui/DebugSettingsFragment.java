package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AppPrefsManager;
import it.rainbowbreeze.picama.logic.PictureScraperManager;
import it.rainbowbreeze.picama.logic.twitter.TwitterScraperConfig;

/**
* Created by alfredomorresi on 05/12/14.
*/
public class DebugSettingsFragment extends Fragment {
    private static final String LOG_TAG = DebugSettingsFragment.class.getSimpleName();

    @Inject ILogFacility mLogFacility;
    @Inject AppPrefsManager mAppPrefsManager;
    TextView mLblSyncInProgress;
    private Context mAppContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_debug_settings, container, false);

        mLblSyncInProgress = (TextView) rootView.findViewById(R.id.debugsettings_lblSyncInProgress);
        updateSyncInProgressValue();
        Button button;
        button = (Button) rootView.findViewById(R.id.debugsettings_btnResetSyncInProgress);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAppPrefsManager.resetSyncStatus();
                Toast.makeText(getActivity(), R.string.debugsettings_msgResetSyncStatus, Toast.LENGTH_SHORT).show();
                updateSyncInProgressValue();
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

    private void updateSyncInProgressValue() {
        mLblSyncInProgress.setText("SyncInProgress value: " + mAppPrefsManager.isSyncing());
    }
}
