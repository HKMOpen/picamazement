package it.rainbowbreeze.picama.logic;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.logic.storage.CloudStorageManager;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class UploadPictureService extends IntentService {
    private static final String LOG_TAG = UploadPictureService.class.getSimpleName();
    @Inject ILogFacility mLogFacility;
    @Inject AmazingPictureDao mAmazingPictureDao;
    @Inject CloudStorageManager mCloudStorageManager;

    public static final String ACTION_UPLOAD_PICTURE = Bag.INTENT_ACTION_UPLOADPICTURE;
    public static final String EXTRA_PARAM_PICTURE_ID = Bag.INTENT_EXTRA_PICTUREID;

    public UploadPictureService() {
        super(UploadPictureService.class.getSimpleName());
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

        if (ACTION_UPLOAD_PICTURE.equals(intent.getAction())) {
            long pictureId = intent.getLongExtra(EXTRA_PARAM_PICTURE_ID, Bag.ID_NOT_SET);
            if (Bag.ID_NOT_SET == pictureId) {
                mLogFacility.i(LOG_TAG, "No valid parameter for action " + intent.getAction());
                return;
            }
            mLogFacility.v(LOG_TAG, "Uploading to cloud(s) picture with id " + pictureId);
            mCloudStorageManager.saveToCloudStorages(pictureId, true);

        } else {
            mLogFacility.e(LOG_TAG, "Cannot process the requested action");
        }
    }
}
