package it.rainbowbreeze.picama.logic.action;

import android.content.Context;
import android.content.Intent;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.ManipulatePictureService;

/**
 * Created by alfredomorresi on 16/11/14.
 */
public class HidePictureAction extends BasePictureAction {
    private static final String LOG_TAG = HidePictureAction.class.getSimpleName();
    private long mPictureId;

    public HidePictureAction(Context appContext, ILogFacility logFacility, ActionsManager actionsManager) {
        super(appContext, logFacility, actionsManager);
        mPictureId = Bag.ID_NOT_SET;
    }

    public HidePictureAction setPictureId(long pictureId) {
        mPictureId = pictureId;
        return this;
    }

    @Override
    protected boolean isDataValid() {
        return Bag.ID_NOT_SET != mPictureId;
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
        Intent intent = new Intent(mAppContext, ManipulatePictureService.class);
        intent.setAction(ManipulatePictureService.ACTION_REMOVE_PICTURE_FROM_LIST);
        intent.putExtra(ManipulatePictureService.EXTRA_PARAM_PICTURE_ID, mPictureId);
        mAppContext.startService(intent);
    }
}
