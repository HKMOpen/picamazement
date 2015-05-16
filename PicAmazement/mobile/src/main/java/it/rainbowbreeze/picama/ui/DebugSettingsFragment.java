package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AppPrefsManager;
import it.rainbowbreeze.picama.logic.LogicManager;

/**
* Created by alfredomorresi on 05/12/14.
*/
public class DebugSettingsFragment extends Fragment {
    private static final String LOG_TAG = DebugSettingsFragment.class.getSimpleName();

    @Inject ILogFacility mLogFacility;
    @Inject AppPrefsManager mAppPrefsManager;
    @Inject LogicManager mLogicManager;
    TextView mLblSyncInProgress;
    private Context mAppContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_debug_settings, container, false);

        mLblSyncInProgress = (TextView) rootView.findViewById(R.id.debugsettings_lblSyncInProgress);
        updateSyncInProgressValue();
        TextView lbl = (TextView) rootView.findViewById(R.id.debugsettings_lblLastSyncAt);
        lbl.setText("Last sync at " +
                mLogicManager.getFormattedDate(mAppPrefsManager.getLastSyncTime()));
        lbl = (TextView) rootView.findViewById(R.id.debugsettings_lblNextSyncAt);
        lbl.setText("First repeating sync scheduled at:  " +
                mLogicManager.getFormattedDate(mAppPrefsManager.getRepeatingSyncTime()));
        lbl = (TextView) rootView.findViewById(R.id.debugsettings_lblPendingIntentExists);
        lbl.setText("Pending intent for scheduled sync exists:  " + mLogicManager.isSyncPendingIntentActive(mAppContext));

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
