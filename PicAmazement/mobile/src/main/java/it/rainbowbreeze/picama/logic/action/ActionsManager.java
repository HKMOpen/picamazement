package it.rainbowbreeze.picama.logic.action;

import android.content.Context;
import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private final Map<String, Object> mExecutionQueue;

    public ActionsManager(Context appContext, ILogFacility logFacility, AmazingPictureDao amazingPictureDao, PictureScraperManager pictureScraperManager) {
        mAppContext = appContext;
        mLogFacility = logFacility;
        mAmazingPictureDao = amazingPictureDao;
        mPictureScraperManager = pictureScraperManager;
        mExecutionQueue = new ConcurrentHashMap<String, Object>();
    }

    protected void addNewActionIntoQueue(BaseAction action) {
        mExecutionQueue.put(action.getUniqueActionId(), action);
    }

    protected void removeActionFromQueue(BaseAction action) {
        mExecutionQueue.remove(action.getUniqueActionId());
    }

    public boolean isActionAlreadyInTheQueue(BaseAction action) {
        mLogFacility.v("Checking for action " + action.getUniqueActionId());
        mLogFacility.v("queue size is " + mExecutionQueue.size());
        return (mExecutionQueue.containsKey(action.getUniqueActionId()));
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

    /**
     * Base interface for an action
     */
    public static abstract class BaseAction {
        protected static enum ConcurrencyType {
            SingleInstance,    // Only one actions a time
            MultipleInstances  // Actions can be execute multiple times in contemporary
        }
        private final ActionsManager mActionsManager;
        protected final ILogFacility mLogFacility;

        protected BaseAction(ILogFacility logFacility, ActionsManager actionManager) {
            mLogFacility = logFacility;
            mActionsManager = actionManager;
        }

        /**
         * Checks validity of extended action data
         * @return
         */
        protected abstract boolean isDataValid();

        /**
         * Executes the commands of the action
         */
        protected abstract void doYourStuff();

        /**
         * Identifies it the action can be executed together with similar actions of the same kind
         * or only one action per time is allowed
         * @return
         */
        protected abstract ConcurrencyType getConcurrencyType();

        /**
         * Returns a unique action identifiers. If {@link it.rainbowbreeze.picama.logic.action.ActionsManager.BaseAction.ConcurrencyType}
         * is set to {@link it.rainbowbreeze.picama.logic.action.ActionsManager.BaseAction.ConcurrencyType#MultipleInstances},
         * only one action with this identifier is allowed in the execution queue
         *
         * @return
         */
        protected abstract String getUniqueActionId();

        protected abstract String getLogTag();

        /**
         * Call this method to fire the action and return
         */
        public void executeAsync() {
            checkForDataValidity();

            new Thread(new Runnable() {
                @Override
                public void run() {
                // Checks if there are other actions with same id in the execution queue
                    if (ConcurrencyType.SingleInstance == getConcurrencyType()) {
                        if (mActionsManager.isActionAlreadyInTheQueue(BaseAction.this)) {
                            mLogFacility.v(getLogTag(), "Action has been refused because another is already on the queue - " + getUniqueActionId());
                            return;
                        }
                    }
                    mActionsManager.addNewActionIntoQueue(BaseAction.this);
                    doYourStuff();
                    mActionsManager.removeActionFromQueue(BaseAction.this);
                }
            }).start();
        }

        /**
         * Call this method to fire the action and wait
         */
        public void execute() {
            checkForDataValidity();

            doYourStuff();
        }

        /**
         * Checks if the action data is valid
         */
        private void checkForDataValidity() {
            if (!isCoreDataValid() || !isDataValid()) {
                throw new IllegalArgumentException("Some data is missing in your action, aborting");
            }
        }

        /**
         * Checks validity of core action data
         * @return
         */
        protected boolean isCoreDataValid() {
            boolean uniqueIdIsCorrect;
            if (ConcurrencyType.SingleInstance == getConcurrencyType()) {
                uniqueIdIsCorrect = !TextUtils.isEmpty(getUniqueActionId());
            } else {
                // null to getConcurrencyType() means multiple instances possible
                uniqueIdIsCorrect = true;
            }
            return
                    null != mLogFacility &&
                    null != mActionsManager &&
                    uniqueIdIsCorrect;
        }
    }
}
