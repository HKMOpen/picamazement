package it.rainbowbreeze.picama.logic.action;

import android.content.Context;

import it.rainbowbreeze.picama.common.ILogFacility;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class ActionsManager {
    private final Context mAppContext;
    private final ILogFacility mLogFacility;

    public ActionsManager(Context appContext, ILogFacility logFacility) {
        mAppContext = appContext;
        mLogFacility = logFacility;
    }

    public SendPictureToWearAction sendPictureToWear() {
        return new SendPictureToWearAction(mAppContext, mLogFacility);
    }

    /**
     * Base interface for an action
     */
    public static abstract class BaseAction {
        protected final ILogFacility mLogFacility;

        protected BaseAction(ILogFacility logFacility) {
            mLogFacility = logFacility;
        }

        /**
         * Call this method to fire the action and return
         */
        public void executeAsync() {
            if (!isDataValid()) {
                throw new IllegalArgumentException("Some data is missing in your action, aborting");
            }
            doYourStuff();
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
         * Checks valid data for the action
         * @return
         */
        protected abstract boolean isDataValid();
    }
}
