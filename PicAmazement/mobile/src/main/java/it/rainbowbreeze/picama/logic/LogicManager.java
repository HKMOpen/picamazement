package it.rainbowbreeze.picama.logic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AppPrefsManager;

/**
 * Misc class for different pieces of logic, not easy to group elsewhere
 *
 * Created by alfredomorresi on 14/12/14.
 */
public class LogicManager {
    private static final String LOG_TAG = LogicManager.class.getSimpleName();
    public static final int REQUEST_SYNC = 10120;
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
                mLogFacility.v(LOG_TAG, "Next sync will happen in " + nextSyncInterval + " milliseconds from now");
             }
        }

        if (nextSyncInterval <= 0) {
            //ok... add some more seconds, useful during reboot, for example...
            //nextSyncInterval = 60000L;
        }

        long nextFinalDateInterval = currentTime + nextSyncInterval;
        mLogFacility.v(LOG_TAG, "Scheduling next sync at " + getFormattedDate(nextFinalDateInterval) +
                " and repeating every " + syncIntervalInPref + " milliseconds");
        PendingIntent pendingIntent = createSyncPendingIntent(appContext);
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        /**
         * {@link android.app.AlarmManager#setInexactRepeating(int, long, long, android.app.PendingIntent)}
         * can be used only with pre-defined intervals: INTERVAL_DAY, INTERVAL_HALF_DAY,
         * INTERVAL_HOUR, INTERVAL_HALF_HOUR, INTERVAL_FIFTEEN_MINUTES. Otherwise, use
         * {@link android.app.AlarmManager#setRepeating(int, long, long, android.app.PendingIntent)}
         *
         * Source: https://developer.android.com/training/scheduling/alarms.html
         */
        if (AlarmManager.INTERVAL_DAY == syncIntervalInPref
                || AlarmManager.INTERVAL_HALF_DAY == syncIntervalInPref
                || AlarmManager.INTERVAL_HOUR == syncIntervalInPref
                || AlarmManager.INTERVAL_HALF_HOUR == syncIntervalInPref
                || AlarmManager.INTERVAL_FIFTEEN_MINUTES == syncIntervalInPref) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    nextFinalDateInterval,
                    syncIntervalInPref,
                    pendingIntent);
            mLogFacility.v(LOG_TAG, "Using an inexact repeating alarm");
        } else {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    nextFinalDateInterval,
                    syncIntervalInPref,
                    pendingIntent);
            mLogFacility.v(LOG_TAG, "Using an exact repeating alarm");
        }
        mAppPrefsManager.setRepeatingSyncTime(nextFinalDateInterval);
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
        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        mAppPrefsManager.resetRepeatingSyncTime();
    }

    private PendingIntent createSyncPendingIntent(Context appContext) {
        Intent intent = new Intent(appContext, RefreshPicturesService.class);
        intent.setAction(RefreshPicturesService.ACTION_REFRESH_PICTURES);
        return PendingIntent.getService(appContext, REQUEST_SYNC, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public boolean isSyncPendingIntentActive(Context appContext) {
        /**
         * {@link android.app.PendingIntent#FLAG_NO_CREATE}:
         *  Flag indicating that if the described PendingIntent does not already exist,
         *  then simply return null instead of creating it.
         */
        Intent intent = new Intent(appContext, RefreshPicturesService.class);
        intent.setAction(RefreshPicturesService.ACTION_REFRESH_PICTURES);
        return null == PendingIntent.getService(appContext, REQUEST_SYNC, intent, PendingIntent.FLAG_NO_CREATE)
                ? false  // null means that the PendingIntent doesn't exist
                : true;
    }

    /**
     * Returns a formatted date from an EPOC time
     * @param timeToFormat
     * @return
     */
    public String getFormattedDate(long timeToFormat) {
        if (timeToFormat > 0) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(timeToFormat);
            // http://developer.android.com/reference/java/text/SimpleDateFormat.html
            SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd',' HH:mm:ss");
            return ft.format(cal.getTime());
        } else {
            return "-";
        }
    }

}
