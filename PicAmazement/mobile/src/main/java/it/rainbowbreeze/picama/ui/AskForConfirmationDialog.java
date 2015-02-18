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
    private static final String ARG_QUESTION_TYPE = "question_type";

    @Inject ILogFacility mLogFacility;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AskForConfirmationDialog newInstance() {
        AskForConfirmationDialog fragment = new AskForConfirmationDialog();
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.common_lblAreYouSure)
                .setPositiveButton(R.string.common_btnYes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mLogFacility.v(LOG_TAG, "Clicked on the positive button of the dialog, executing the relative action");
                        // Send the positive button event back to the host activity
                        // Wondering why?
                        //  http://stackoverflow.com/questions/13238959/how-to-get-button-clicks-in-host-fragment-from-dialog-fragment
                        //  or
                        //  http://stackoverflow.com/questions/10905312/receive-result-from-dialogfragment
                        //  http://stackoverflow.com/questions/13733304/callback-to-a-fragment-from-a-dialogfragment
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
                    }
                })
                .setNegativeButton(R.string.common_btnCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null);
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
