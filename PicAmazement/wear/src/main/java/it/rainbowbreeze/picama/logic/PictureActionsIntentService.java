package it.rainbowbreeze.picama.logic;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import javax.inject.Inject;

import dagger.Module;
import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PictureActionsIntentService extends IntentService {
    private static final String LOG_TAG = PictureActionsIntentService.class.getSimpleName();
    @Inject ILogFacility mLogFacility;

    public PictureActionsIntentService() {
        super("PictureActionsIntentService");
    }

    @Override
    public void onCreate() {
        ((MyApp)getApplicationContext()).inject(this);
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mLogFacility.v(LOG_TAG, "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (Bag.INTENT_ACTION_REMOVEPICTURE.equals(action)) {
                long pictureId = intent.getLongExtra(Bag.INTENT_EXTRA_PICTUREID, -1);
                mLogFacility.i(LOG_TAG, "Need to remove picture with id " + pictureId);

                // Sends the dataitem with the item to cancel

                // Removes the notification

            } else if (Bag.INTENT_ACTION_SAVEPICTURE.equals(action)) {
                long pictureId = intent.getLongExtra(Bag.INTENT_EXTRA_PICTUREID, -1);
                mLogFacility.i(LOG_TAG, "Need to save picture with id " + pictureId);
            }
        }
    }
}
