package it.rainbowbreeze.picama.logic;

import it.rainbowbreeze.libs.logic.RainbowStatusChangeNotifier;
import it.rainbowbreeze.picama.common.ILogFacility;

/**
 * Created by alfredomorresi on 15/05/15.
 *
 * Manages status update globally spread across all the app
 */
public class StatusChangeNotifier extends RainbowStatusChangeNotifier {
    private boolean mPictureRefreshInProgress;

    public static final String STATUSKEY_REFRESHPICTURES = "RefreshPictures";

    public StatusChangeNotifier(ILogFacility logFacility) {
        super(logFacility);
    }

    public void refreshPicturesStarted() {
        mPictureRefreshInProgress = true;
        notifyStatusChanged(STATUSKEY_REFRESHPICTURES);
    }
    public void refreshPicturesFinished() {
        mPictureRefreshInProgress = false;
        notifyStatusChanged(STATUSKEY_REFRESHPICTURES);
    }
    public boolean isRefreshPicturesInProgress() {
        return mPictureRefreshInProgress;
    }
}
