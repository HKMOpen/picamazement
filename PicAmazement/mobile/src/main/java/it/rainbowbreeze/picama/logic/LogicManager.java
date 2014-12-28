package it.rainbowbreeze.picama.logic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AppPrefsManager;

/**
 * Misc class for different pieces of logic, not easy to group elsewhere
 *
 * Created by alfredomorresi on 14/12/14.
 */
public class LogicManager {
    private static final String LOG_TAG = LogicManager.class.getSimpleName();
    private final ILogFacility mLogFacility;
    private final AppPrefsManager mAppPrefsManager;

    public LogicManager(ILogFacility logFacility, AppPrefsManager appPrefsManager) {
        mLogFacility = logFacility;
        mAppPrefsManager = appPrefsManager;
    }

    /**
     * Schedules a new picture refresh, if needed
     *
     *  AlarmManager and setInexactRepeating(..)
     * http://www.accella.net/scheduling-code-execution-on-android/pote
     */
    public void schedulePicturesRefresh(Context appContext) {
        // First, checks if the picture refresh have to be scheduled
        if (!mAppPrefsManager.isSyncEnabled()) {
            mLogFacility.v(LOG_TAG, "No need to schedule a sync because the sync is off in the settings");
            return;
        }

        // Checks last sync time
        long lastSyncTime = mAppPrefsManager.getLastSyncTime();
        long syncIntervalInPref = 24 / Integer.parseInt(mAppPrefsManager.getSyncFrequency())
                * 3600 * 1000L; // milliseconds
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long nextSyncInterval = 0;

        if (0 == lastSyncTime) {
            // Sync now
            mLogFacility.v(LOG_TAG, "Sync has never been done. Require one immediately");
            nextSyncInterval = 0;
        } else {
            long elapsedTimeFromLastSync = currentTime - lastSyncTime;

            if (elapsedTimeFromLastSync > syncIntervalInPref) {
                // Sync now if the time elapsed from last sync is greater than the expected sync interval
                mLogFacility.v(LOG_TAG, "Last sync has been done long time ago. Require one immediately");
                nextSyncInterval = 0;
            } else {
                nextSyncInterval = syncIntervalInPref - elapsedTimeFromLastSync;
                mLogFacility.v(LOG_TAG, "Schedule next sync after milliseconds " + nextSyncInterval);
            }
        }

        if (nextSyncInterval <= 0) {
            //ok... add some more seconds, useful during reboot, for example...
            nextSyncInterval = 60000L;
        }

        PendingIntent pendingIntent = createSyncPendingIntent(appContext);
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                currentTime + nextSyncInterval,
                syncIntervalInPref,
                pendingIntent);
    }

    /**
     * Disable picture refresh in the background, canceling an alarm already
     * in progress
     *
     * @param appContext
     */
    public void cancelPictureRefresh(Context appContext) {
        mLogFacility.v(LOG_TAG, "Removing background sync");
        PendingIntent pendingIntent = createSyncPendingIntent(appContext);
        //TODO: and now?
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private PendingIntent createSyncPendingIntent(Context appContext) {
        Intent intent = new Intent(appContext, RefreshPicturesService.class);
        intent.setAction(RefreshPicturesService.ACTION_REFRESH_PICTURES);
        //TODO check flags and other parameters
        return PendingIntent.getService(appContext, 100, intent, 0);
    }
}
