package it.rainbowbreeze.picama.logic.action;

import android.content.Context;
import android.content.Intent;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.ManipulatePictureService;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class DeleteAllPicturesAction extends BasePictureAction {
    private static final String LOG_TAG = DeleteAllPicturesAction.class.getSimpleName();

    public DeleteAllPicturesAction(Context appContext, ILogFacility logFacility, ActionsManager actionsManager) {
        super(appContext, logFacility, actionsManager);
    }

    @Override
    protected ConcurrencyType getConcurrencyType() {
        return ConcurrencyType.SingleInstance;
    }

    @Override
    protected String getUniqueActionId() {
        return LOG_TAG;
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    @Override
    protected boolean isDataValid() {
        return true;
    }

    @Override
    protected void doYourStuff() {
        mLogFacility.v(LOG_TAG, "Deleting all pictures in the DB using a service");
        Intent intent = new Intent(mAppContext, ManipulatePictureService.class);
        intent.setAction(ManipulatePictureService.ACTION_REMOVE_ALL_PICTURES);
        mAppContext.startService(intent);
    }
}
