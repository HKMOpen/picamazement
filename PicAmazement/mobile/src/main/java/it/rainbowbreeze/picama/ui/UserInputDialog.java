package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;

/**
 * Created by rainbowbreeze on 17/02/15.
 */
public class UserInputDialog extends DialogFragment {
    private static final String LOG_TAG = UserInputDialog.class.getSimpleName();
    private static final String ARG_PROMPT_RESOURCEID = "prompt_resourceId";
    private static final String ARG_TIP_RESOURCEID = "tip_resourceId";
    public static final String INTENT_EXTRA_USERINPUT = "userinput";

    @Inject ILogFacility mLogFacility;

    /**
     * Returns a new instance of this dialog, with a customized question message
     *
     * @param promptResourceId: resource id of the question to ask
     */
    public static UserInputDialog newInstance(int promptResourceId, int textTipResourceId) {
        UserInputDialog fragment = new UserInputDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_PROMPT_RESOURCEID, promptResourceId);
        args.putInt(ARG_TIP_RESOURCEID, textTipResourceId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int promptResourceId = getArguments().getInt(ARG_PROMPT_RESOURCEID, R.string.common_lblAreYouSure);
        int tipResourceId = getArguments().getInt(ARG_TIP_RESOURCEID, R.string.common_lblInputTextTip);

        // Set an EditText view to get user input
        final EditText input = new EditText(getActivity());

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setMessage(promptResourceId)
                .setView(input)
                .setPositiveButton(R.string.common_btnOk, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mLogFacility.v(LOG_TAG, "Clicked on the positive button of the dialog, executing the relative action");
                        // Send the positive button event back to the host activity
                        // Wondering why?
                        //  http://stackoverflow.com/questions/13238959/how-to-get-button-clicks-in-host-fragment-from-dialog-fragment
                        //  or
                        //  http://stackoverflow.com/questions/10905312/receive-result-from-dialogfragment
                        //  http://stackoverflow.com/questions/13733304/callback-to-a-fragment-from-a-dialogfragment
                        Intent intent = new Intent();
                        intent.putExtra(INTENT_EXTRA_USERINPUT, input.getText().toString());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
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
