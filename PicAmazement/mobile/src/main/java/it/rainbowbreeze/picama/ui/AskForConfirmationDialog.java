package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;

/**
 * Created by rainbowbreeze on 17/02/15.
 */
public class AskForConfirmationDialog extends DialogFragment {
    private static final String LOG_TAG = AskForConfirmationDialog.class.getSimpleName();

    @Inject ILogFacility mLogFacility;

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface DialogAction {
        public void onDialogButtonClick(DialogFragment dialog);
    }

    private final DialogAction mPositiveAction;
    private final DialogAction mNegativeAction;

    public AskForConfirmationDialog(DialogAction positiveAction, DialogAction negativeAction) {
        if (null == positiveAction) {
            throw new IllegalArgumentException("The action passed cannot be null");
        }
        mPositiveAction = positiveAction;
        mNegativeAction = negativeAction;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.common_lblAreYouSure)
                .setPositiveButton(R.string.common_btnYes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mLogFacility.v(LOG_TAG, "Clicked on the positive button of the dialog, executing the relative action");
                        mPositiveAction.onDialogButtonClick(AskForConfirmationDialog.this);
                    }
                })
                .setNegativeButton(R.string.common_btnCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (null != mNegativeAction) {
                            mNegativeAction.onDialogButtonClick(AskForConfirmationDialog.this);
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Context mAppContext = activity.getApplicationContext();
        ((MyApp) mAppContext).inject(this);
    }
}
