package it.rainbowbreeze.picama.logic;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class RefreshPictureService extends IntentService {
    private static final String LOG_TAG = RefreshPictureService.class.getSimpleName();
    @Inject ILogFacility mLogFacility;
    @Inject PictureScraperManager mPictureScraperManager;

    public static final String ACTION_REFRESH_PICTURES = "it.rainbowbreeze.picama.Action.Picture.Refresh";

    public RefreshPictureService() {
        super(RefreshPictureService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApp) getApplicationContext()).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (TextUtils.isEmpty(intent.getAction())) {
            mLogFacility.i(LOG_TAG, "No action for the service, aborting");
            return;
        }

        if (ACTION_REFRESH_PICTURES.equals(intent.getAction())) {
            mLogFacility.v(LOG_TAG, "Refreshing pictures");
            mPictureScraperManager.searchForNewImage(getApplicationContext());

        } else {
            mLogFacility.e(LOG_TAG, "Cannot process the requested action");
        }
    }
}