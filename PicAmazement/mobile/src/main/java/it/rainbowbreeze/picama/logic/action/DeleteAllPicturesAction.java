package it.rainbowbreeze.picama.logic.action;

import android.content.Context;
import android.content.Intent;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.SendPictureToWearService;
import it.rainbowbreeze.picama.logic.UpdatePictureFieldsService;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class DeleteAllPicturesAction extends BasePictureAction {
    private static final String LOG_TAG = SendPictureToWearService.class.getSimpleName();

    public DeleteAllPicturesAction(Context appContext, ILogFacility logFacility, ActionsManager actionsManager) {
        super(appContext, logFacility, actionsManager);
    }

    @Override
    protected void doYourStuff() {
        mLogFacility.v(LOG_TAG, "Deleting all pictures in the DB using a service");
        Intent intent = new Intent(mAppContext, UpdatePictureFieldsService.class);
        intent.setAction(UpdatePictureFieldsService.ACTION_REMOVE_ALL_PICTURES);
        mAppContext.startService(intent);
    }

    @Override
    protected boolean isDataValid() {
        return true;
    }
}
