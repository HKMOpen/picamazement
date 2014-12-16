package it.rainbowbreeze.picama.logic.action;

import android.content.Context;
import android.content.Intent;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.UpdatePictureFieldsService;

/**
 * Created by alfredomorresi on 16/11/14.
 */
public class SavePictureAction extends BasePictureAction {
    private static final String LOG_TAG = SavePictureAction.class.getSimpleName();
    private long mPictureId;

    public SavePictureAction(Context appContext, ILogFacility logFacility, ActionsManager actionsManager) {
        super(appContext, logFacility, actionsManager);
        mPictureId = Bag.ID_NOT_SET;
    }

    public SavePictureAction setPictureId(long pictureId) {
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
        mLogFacility.v(LOG_TAG, "Saving picture with id " + mPictureId);
        Intent intent = new Intent(mAppContext, UpdatePictureFieldsService.class);
        intent.setAction(UpdatePictureFieldsService.ACTION_SAVE_PICTURE);
        intent.putExtra(UpdatePictureFieldsService.EXTRA_PARAM_PICTURE_ID, mPictureId);
        mAppContext.startService(intent);
    }
}
