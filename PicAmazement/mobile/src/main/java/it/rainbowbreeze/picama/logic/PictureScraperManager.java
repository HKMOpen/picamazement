package it.rainbowbreeze.picama.logic;

import android.content.Context;
import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 01/11/14.
 */
public class PictureScraperManager {
    private static final String LOG_TAG = PictureScraperManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final PictureScraperManagerConfig mConfig;
    private final List<IPictureScraper> mPictureScrapers;
    private final AmazingPictureDao mAmazingPictureDao;

    public PictureScraperManager(ILogFacility logFacility, PictureScraperManagerConfig config,
                                 AmazingPictureDao amazingPictureDao) {
        mLogFacility = logFacility;
        mConfig = config;
        mPictureScrapers = mConfig.getPictureScrapers();
        mAmazingPictureDao = amazingPictureDao;
    }

    /**
     * TODO: add a callback and make it async
     */
    public void searchForNewImage(Context appContext) {
        mLogFacility.v(LOG_TAG, "Starting pictures update");

        for (IPictureScraper scraper : mPictureScrapers) {
            mLogFacility.v(LOG_TAG, "Start to scrape from provider " + scraper.getName());
            List<AmazingPicture> newPictures = scraper.getNewPictures();
            mLogFacility.v(LOG_TAG, "Found " + newPictures.size() + " new pictures");

            addOnlyNewPictures(appContext, newPictures);
        }
    }

    /**
     * Adds only new picture to the local database
     * @param newPictures
     */
    private void addOnlyNewPictures(Context appContext, List<AmazingPicture> newPictures) {
        for (AmazingPicture picture : newPictures) {
            if (TextUtils.isEmpty(picture.getUrl())) {
                mLogFacility.i(LOG_TAG, "No URL for picture " + picture.getTitle());
            }
            if (!mAmazingPictureDao.pictureExists(appContext, picture.getUrl())) {
                mAmazingPictureDao.insert(appContext, picture);
                mLogFacility.v(LOG_TAG, "Added new picture in the DB at url " + picture.getUrl());
            } else {
                mLogFacility.v(LOG_TAG, "Skipped picture with url " + picture.getUrl());
            }
        }
    }
}
