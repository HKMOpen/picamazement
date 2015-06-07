package it.rainbowbreeze.picama.logic.scraper;

import android.content.Context;
import android.text.TextUtils;

import java.util.List;

import it.rainbowbreeze.libs.network.NetworkUtils;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.data.AppPrefsManager;
import it.rainbowbreeze.picama.domain.AmazingPicture;
import it.rainbowbreeze.picama.logic.StatusChangeNotifier;

/**
 * Created by alfredomorresi on 01/11/14.
 */
public class PictureScraperManager {
    private static final String LOG_TAG = PictureScraperManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final PictureScraperManagerConfig mConfig;
    private final List<IPictureScraper> mPictureScrapers;
    private final AmazingPictureDao mAmazingPictureDao;
    private final AppPrefsManager mAppPrefsManager;
    private final StatusChangeNotifier mStatusChangeNotifier;

    public PictureScraperManager(
            ILogFacility logFacility,
            PictureScraperManagerConfig config,
            AmazingPictureDao amazingPictureDao,
            AppPrefsManager appPrefsManager,
            StatusChangeNotifier statusChangeNotifier
    ) {
        mLogFacility = logFacility;
        mConfig = config;
        mAppPrefsManager = appPrefsManager;
        mPictureScrapers = mConfig.getPictureScrapers();
        mAmazingPictureDao = amazingPictureDao;
        mStatusChangeNotifier = statusChangeNotifier;
    }

    /**
     * TODO: add a callback and make it async
     * @param appContext
     * @return true if at least one new picture has been found
     */
    public boolean searchForNewImage(Context appContext, boolean forceSync) {
        mLogFacility.v(LOG_TAG, "Starting pictures update and force: " + forceSync);

        if (!forceSync && mAppPrefsManager.isSyncing()) {
            mLogFacility.v(LOG_TAG, "Syncing already in progress, aborting");
            return false;
        }

        if (!NetworkUtils.isNetworkAvailable(appContext)) {
            mLogFacility.v(LOG_TAG, "Aborting the search because the network is not available");
            return false;
        }

        mAppPrefsManager.startSync();
        mStatusChangeNotifier.refreshPicturesStarted();

        boolean foundNewPictures = false;
        for (IPictureScraper scraper : mPictureScrapers) {
            if (!scraper.isEnabled()) {
                mLogFacility.v(LOG_TAG, "Skip scraping for disabled provider " + scraper.getSourceName());
                continue;
            }
            mLogFacility.v(LOG_TAG, "Start scraping for provider " + scraper.getSourceName());
            List<AmazingPicture> newPictures = scraper.getNewPictures();
            mLogFacility.v(LOG_TAG, "Found " + newPictures.size() + " new pictures");

            foundNewPictures = foundNewPictures || addOnlyNewPictures(newPictures);
        }
        mAppPrefsManager.stopSync();
        mStatusChangeNotifier.refreshPicturesFinished();
        return foundNewPictures;
    }

    /**
     * Adds only new picture to the local database
     *
     * @param newPictures
     */
    private boolean addOnlyNewPictures(List<AmazingPicture> newPictures) {
        boolean foundNewPictures = false;
        for (AmazingPicture picture : newPictures) {
            if (TextUtils.isEmpty(picture.getUrl())) {
                mLogFacility.i(LOG_TAG, "No URL for picture " + picture.getDesc());
            }
            if (!mAmazingPictureDao.exists(picture.getUrl())) {
                mAmazingPictureDao.insert(picture);
                foundNewPictures = true;
                mLogFacility.v(LOG_TAG, "Added new picture in the DB at url " + picture.getUrl());
            } else {
                mLogFacility.v(LOG_TAG, "Skipped picture with url " + picture.getUrl());
            }
        }
        return foundNewPictures;
    }
}
