package it.rainbowbreeze.picama.logic.action;

import android.content.Context;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.PictureScraperManager;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class SearchForNewImagesAction extends ActionsManager.BaseAction {
    private static final String LOG_TAG = SearchForNewImagesAction.class.getSimpleName();
    protected final Context mAppContext;
    private final PictureScraperManager mPictureScraperManager;

    public SearchForNewImagesAction(Context appContext, ILogFacility logFacility, ActionsManager actionsManager, PictureScraperManager pictureScraperManager) {
        super(logFacility, actionsManager);
        mAppContext = appContext;
        this.mPictureScraperManager = pictureScraperManager;
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
        mLogFacility.v(LOG_TAG, "Searching for new images");
        mPictureScraperManager.searchForNewImage(mAppContext);
    }
}
