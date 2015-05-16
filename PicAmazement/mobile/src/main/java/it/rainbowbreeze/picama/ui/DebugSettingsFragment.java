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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AppPrefsManager;
import it.rainbowbreeze.picama.logic.LogicManager;
import it.rainbowbreeze.picama.logic.action.ActionsManager;

/**
* Created by alfredomorresi on 05/12/14.
*/
public class DebugSettingsFragment extends Fragment {
    private static final String LOG_TAG = DebugSettingsFragment.class.getSimpleName();
    private static final int REQUEST_DELETE_ALL_PICTURES = 100;

    @Inject ILogFacility mLogFacility;
    @Inject AppPrefsManager mAppPrefsManager;
    @Inject LogicManager mLogicManager;
    @Inject ActionsManager mActionsManager;
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


        button = (Button) rootView.findViewById(R.id.debugsettings_btnClearDatabase);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                askToDeleteDatabase();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mLogFacility.v(LOG_TAG, "onActivityResult with request code " + requestCode + " and result code " + resultCode);
        switch (requestCode) {
            case REQUEST_DELETE_ALL_PICTURES:
                if (Activity.RESULT_OK == resultCode) {
                    mActionsManager.deleteAllPictures()
                            .executeAsync();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    private void askToDeleteDatabase() {
        DialogFragment newFragment = AskForConfirmationDialog.newInstance();
        newFragment.setTargetFragment(this, REQUEST_DELETE_ALL_PICTURES);
        newFragment.show(getFragmentManager(), "DeleteAllPictures");
    }

    private void updateSyncInProgressValue() {
        mLblSyncInProgress.setText("SyncInProgress value: " + mAppPrefsManager.isSyncing());
    }
}
