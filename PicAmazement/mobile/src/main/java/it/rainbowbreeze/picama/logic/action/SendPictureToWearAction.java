package it.rainbowbreeze.picama.logic.action;

import android.content.Context;
import android.content.Intent;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.logic.SendPictureToWearService;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class SendPictureToWearAction extends ActionsManager.BaseAction {
    private static final String LOG_TAG = SendPictureToWearService.class.getSimpleName();
    private Context mAppContext;
    private long mPictureId;

    protected SendPictureToWearAction(Context appContext, ILogFacility logFacility, ActionsManager actionsManager) {
        super(logFacility, actionsManager);
        mAppContext = appContext;
    }

    public SendPictureToWearAction setPictureId(long pictureId) {
        mPictureId = pictureId;
        return this;
    }

    @Override
    protected boolean isDataValid() {
        return null != mAppContext &&
                mPictureId >= 0;
    }

    @Override
    protected void doYourStuff() {
        mLogFacility.v(LOG_TAG, "Using a Service to send to Wear amazing picture with id " + mPictureId);
        Intent intent = new Intent(mAppContext, SendPictureToWearService.class);
        intent.setAction(SendPictureToWearService.ACTION_SENDPICTURE);
        intent.putExtra(SendPictureToWearService.EXTRA_PARAM_PICTURE_ID, mPictureId);
        mAppContext.startService(intent);
    }

}
