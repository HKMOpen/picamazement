package it.rainbowbreeze.picama.logic.action;

import android.content.Context;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.logic.PictureScraperManager;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class ActionsManager {
    private final Context mAppContext;
    private final ILogFacility mLogFacility;
    private final AmazingPictureDao mAmazingPictureDao;
    private final PictureScraperManager mPictureScraperManager;

    public ActionsManager(Context appContext, ILogFacility logFacility, AmazingPictureDao amazingPictureDao, PictureScraperManager pictureScraperManager) {
        mAppContext = appContext;
        mLogFacility = logFacility;
        mAmazingPictureDao = amazingPictureDao;
        mPictureScraperManager = pictureScraperManager;
    }

    public SendPictureToWearAction sendPictureToWear() {
        return new SendPictureToWearAction(mAppContext, mLogFacility, this);
    }

    public DeleteAllPicturesAction deleteAllPictures() {
        return new DeleteAllPicturesAction(mAppContext, mLogFacility, this);
    }

    public SearchForNewImagesAction searchForNewImages() {
        return new SearchForNewImagesAction(mLogFacility, this, mPictureScraperManager);
    }


    /**
     * Base interface for an action
     */
    public static abstract class BaseAction {
        private final ActionsManager mActionsManager;
        protected final ILogFacility mLogFacility;

        protected BaseAction(ILogFacility logFacility, ActionsManager actionManager) {
            mLogFacility = logFacility;
            mActionsManager = actionManager;
        }

        /**
         * Call this method to fire the action and return
         */
        public void executeAsync() {
            if (!isCoreDataValid() && !isDataValid()) {
                throw new IllegalArgumentException("Some data is missing in your action, aborting");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doYourStuff();
                }
            }).start();
        }

        /**
         * Call this method to fire the action and wait
         */
        public void execute() {
            if (!isDataValid()) {
                throw new IllegalArgumentException("Some data is missing in your action, aborting");
            }
            doYourStuff();
        }

        /**
         * Executes the commands of the action
         */
        protected abstract void doYourStuff();

        /**
         * Checks validity of core action data
         * @return
         */
        protected boolean isCoreDataValid() {
            return null != mLogFacility;
        }

        /**
         * Checks validity of extended action data
         * @return
         */
        protected abstract boolean isDataValid();
    }
}
