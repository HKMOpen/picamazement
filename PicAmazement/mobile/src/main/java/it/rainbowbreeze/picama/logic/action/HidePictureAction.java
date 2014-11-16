package it.rainbowbreeze.picama.logic.action;

import android.content.Context;
import android.content.Intent;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.UpdatePictureFieldsService;

/**
 * Created by alfredomorresi on 16/11/14.
 */
public class HidePictureAction extends BasePictureAction {
    private static final String LOG_TAG = HidePictureAction.class.getSimpleName();
    private long mPictureId;

    public HidePictureAction(Context appContext, ILogFacility logFacility, ActionsManager actionsManager) {
        super(appContext, logFacility, actionsManager);
    }

    public HidePictureAction setPictureId(long pictureId) {
        mPictureId = pictureId;
        return this;
    }

    @Override
    protected boolean isDataValid() {
        return mPictureId >= 0;
    }

    @Override
    protected ConcurrencyType getConcurrencyType() {
        return ConcurrencyType.SingleInstance;
    }

    @Override
    protected String getUniqueActionId() {
        return LOG_TAG + "/" + mPictureId;
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    @Override
    protected void doYourStuff() {
        mLogFacility.v(LOG_TAG, "Hiding picture with id " + mPictureId);
        Intent intent = new Intent(mAppContext, UpdatePictureFieldsService.class);
        intent.setAction(UpdatePictureFieldsService.ACTION_REMOVE_PICTURE_FROM_LIST);
        intent.putExtra(UpdatePictureFieldsService.EXTRA_PARAM_PICTURE_ID, mPictureId);
        mAppContext.startService(intent);
    }
}
