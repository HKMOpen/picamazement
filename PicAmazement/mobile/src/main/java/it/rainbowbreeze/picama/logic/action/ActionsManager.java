package it.rainbowbreeze.picama.logic.action;

import android.content.Context;

import it.rainbowbreeze.libs.logic.RainbowActionsManager;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.scraper.PictureScraperManager;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class ActionsManager extends RainbowActionsManager {
    private static final String LOG_TAG = ActionsManager.class.getSimpleName();

    private final Context mAppContext;
    private final ILogFacility mLogFacility;
    private final PictureScraperManager mPictureScraperManager;

    public ActionsManager(Context appContext, ILogFacility logFacility, PictureScraperManager pictureScraperManager) {
        super(logFacility, LOG_TAG);
        mLogFacility = logFacility;
        mAppContext = appContext;
        mPictureScraperManager = pictureScraperManager;
    }


    public SendPictureToWearAction sendPictureToWear() {
        return new SendPictureToWearAction(mAppContext, mLogFacility, this);
    }

    public DeleteAllPicturesAction deleteAllPictures() {
        return new DeleteAllPicturesAction(mAppContext, mLogFacility, this);
    }

    public SearchForNewImagesAction searchForNewImages() {
        return new SearchForNewImagesAction(mAppContext, mLogFacility, this, mPictureScraperManager);
    }

    public HidePictureAction hidePicture() {
        return new HidePictureAction(mAppContext, mLogFacility, this);
    }

    public UploadPictureAction uploadPicture() {
        return new UploadPictureAction(mAppContext, mLogFacility, this);
    }

    public HideAllVisibleNotUploadedPicturesAction hideAllVisibleNotUploadedPictures() {
        return new HideAllVisibleNotUploadedPicturesAction(mAppContext, mLogFacility, this);
    }

}
