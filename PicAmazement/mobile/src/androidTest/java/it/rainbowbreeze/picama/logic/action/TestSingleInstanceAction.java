package it.rainbowbreeze.picama.logic.action;

import it.rainbowbreeze.picama.common.ILogFacility;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class TestSingleInstanceAction extends ActionsManager.BaseAction {
    private static final String LOG_TAG = TestSingleInstanceAction.class.getSimpleName();
    private static long mProgressId = 0;

    protected TestSingleInstanceAction(ILogFacility logFacility, ActionsManager actionManager) {
        super(logFacility, actionManager);
    }

    @Override
    protected boolean isDataValid() {
        return true;
    }

    @Override
    protected void doYourStuff() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        mLogFacility.v(LOG_TAG, "Incrementing counter");
        mProgressId++;
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
}
