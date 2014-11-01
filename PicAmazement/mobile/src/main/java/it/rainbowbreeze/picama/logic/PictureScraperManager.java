package it.rainbowbreeze.picama.logic;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.rainbowbreeze.picama.common.ILogFacility;

/**
 * Created by alfredomorresi on 01/11/14.
 */
public class PictureScraperManager {
    private static final String LOG_TAG = PictureScraperManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final PictureScraperManagerConfig mConfig;
    private final List<IPictureScraper> mPictureScrapers;

    public PictureScraperManager(ILogFacility logFacility, PictureScraperManagerConfig config) {
        mLogFacility = logFacility;
        mConfig = config;
        mPictureScrapers = mConfig.getPictureScrapers();
    }

    /**
     * TODO: add a callback and make it async
     */
    public void searchForNewImage() {
        for (IPictureScraper scraper : mPictureScrapers) {
            mLogFacility.v(LOG_TAG, scraper.getName());
        }
    }
}
