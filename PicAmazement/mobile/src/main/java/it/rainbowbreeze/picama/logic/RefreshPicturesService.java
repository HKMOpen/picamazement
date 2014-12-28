package it.rainbowbreeze.picama.logic;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.data.AppPrefsManager;
import it.rainbowbreeze.picama.domain.AmazingPicture;
import it.rainbowbreeze.picama.logic.action.ActionsManager;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class RefreshPicturesService extends IntentService {
    private static final String LOG_TAG = RefreshPicturesService.class.getSimpleName();
    @Inject ILogFacility mLogFacility;
    @Inject PictureScraperManager mPictureScraperManager;
    @Inject ActionsManager mActionsManager;
    @Inject AmazingPictureDao mAmazingPictureDao;
    @Inject LogicManager mLogicManager;
    @Inject AppPrefsManager mAppPrefsManager;

    public static final String ACTION_REFRESH_PICTURES = "it.rainbowbreeze.picama.Action.Picture.Refresh";
    public static final String ACTION_SCHEDULE_REFRESH = "it.rainbowbreeze.picama.Action.Picture.ScheduleRefresh";

    public RefreshPicturesService() {
        super(RefreshPicturesService.class.getSimpleName());
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
            boolean foundNewPictures = mPictureScraperManager.searchForNewImage(getApplicationContext(), false);

            if (foundNewPictures) {
                AmazingPicture picture = mAmazingPictureDao.getFirst();
                if (null != picture)  // Just in case...
                    mActionsManager.sendPictureToWear()
                            .setPictureId(picture.getId())
                            .execute();
            }

        } else if (ACTION_SCHEDULE_REFRESH.equals(intent.getAction())) {
            mLogFacility.v(LOG_TAG, "Scheduling pictures refresh");
            mAppPrefsManager.resetSyncStatus();  // In case a sync has been interrupted
            mLogicManager.schedulePicturesRefresh(getApplicationContext());

        } else {
            mLogFacility.e(LOG_TAG, "Cannot process the requested action");
        }
    }
}
