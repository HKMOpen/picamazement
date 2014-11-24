package it.rainbowbreeze.picama.logic.action;

import android.content.Context;
import android.content.Intent;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.wearable.SendDataToWearService;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class SendPictureToWearAction extends ActionsManager.BaseAction {
    private static final String LOG_TAG = SendDataToWearService.class.getSimpleName();
    private Context mAppContext;
    private long mPictureId;

    protected SendPictureToWearAction(Context appContext, ILogFacility logFacility, ActionsManager actionsManager) {
        super(logFacility, actionsManager);
        mAppContext = appContext;
    }

    @Override
    protected ConcurrencyType getConcurrencyType() {
        return ConcurrencyType.MultipleInstances;
    }

    @Override
    protected String getUniqueActionId() {
        return LOG_TAG;
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
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
        Intent intent = new Intent(mAppContext, SendDataToWearService.class);
        intent.setAction(SendDataToWearService.ACTION_SENDPICTURE);
        intent.putExtra(SendDataToWearService.EXTRA_PICTUREID, mPictureId);
        mAppContext.startService(intent);
    }
}
