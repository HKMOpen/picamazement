package it.rainbowbreeze.picama.data;

import android.content.Context;

import java.util.Calendar;

import it.rainbowbreeze.libs.data.RainbowAppPrefsManager;
import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;

/**
 *
 * Remember: Key names have to be equal to the ones in the xml file, otherwise two different
 *  settings are managed
 *
 * Created by alfredomorresi on 05/12/14.
 */
public class AppPrefsManager extends RainbowAppPrefsManager {
    private static final String LOG_TAG = AppPrefsManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    public static final String PREFS_FILE_NAME = "PicAmazementPrefs";
    private static final String NULL_STRING = "null";

    public AppPrefsManager(Context appContext, ILogFacility logFacility) {
        super(appContext, PREFS_FILE_NAME, R.xml.pref_general, logFacility);
        mLogFacility = logFacility;
    }

    @Override
    protected void setDefaultValuesInternal() {
        mLogFacility.v(LOG_TAG, "Setting default values of preferences");
        setDropboxEnabled(false);
        setDropboxAuthToken(NULL_STRING);
    }

    private static final String PREF_LASTSYNCTIME = "pref_lastSyncTime";
    public long getLastSyncTime() {
        return mAppPreferences.getLong(PREF_LASTSYNCTIME, 0);
    }
    public AppPrefsManager setLastSyncTime(long newValue) {
        openSharedEditor();
        mSharedEditor.putLong(PREF_LASTSYNCTIME, newValue);
        saveIfNeeded();
        return this;
    }

    // Is refreshing picture list
    private static final String PREF_ISSYNCING = "pref_isSyncing";
    public boolean isSyncing() {
        return mAppPreferences.getBoolean(PREF_ISSYNCING, false);
    }
    public AppPrefsManager resetSyncStatus() {
        openSharedEditor();
        mSharedEditor.putBoolean(PREF_ISSYNCING, false);
        saveIfNeeded();
        return this;
    }
    public AppPrefsManager startSync() {
        openSharedEditor();
        mSharedEditor.putBoolean(PREF_ISSYNCING, true);
        saveIfNeeded();
        return this;
    }
    public AppPrefsManager stopSync() {
        openSharedEditor();
        mSharedEditor.putBoolean(PREF_ISSYNCING, false);
        mSharedEditor.putLong(PREF_LASTSYNCTIME, Calendar.getInstance().getTimeInMillis());

        saveIfNeeded();
        return this;
    }

    private static final String PREF_DROPBOXENABLED = "pref_dropboxEnabled";
    public boolean isDropboxEnabled() {
        return mAppPreferences.getBoolean(PREF_DROPBOXENABLED, false);
    }
    public AppPrefsManager setDropboxEnabled(boolean newValue) {
        openSharedEditor();
        mSharedEditor.putBoolean(PREF_DROPBOXENABLED, newValue);
        saveIfNeeded();
        return this;
    }

    private static final String PREF_DROPBOX_OAUTH2_ACCESS_TOKEN = "pref_dropboxOAuth2AccessToken";
    public String getDropboxOAuth2AccessToken() {
        return mAppPreferences.getString(PREF_DROPBOX_OAUTH2_ACCESS_TOKEN, NULL_STRING);
    }
    public AppPrefsManager setDropboxAuthToken(String OAuth2AccessToken) {
        openSharedEditor();
        mSharedEditor.putString(PREF_DROPBOX_OAUTH2_ACCESS_TOKEN, OAuth2AccessToken);
        saveIfNeeded();
        return this;
    }
    public boolean isDropboxAuthorized() {
        return !NULL_STRING.equals(getDropboxOAuth2AccessToken());
    }
    public void resetDropboxAuthToken() {
        setDropboxAuthToken(NULL_STRING);
    }

    private static final String PREF_DROPBOX_SAVE_PATH = "pref_dropboxSavePath";
    public String getDropboxSavePath() {
        return mAppPreferences.getString(PREF_DROPBOX_SAVE_PATH, NULL_STRING);
    }
    public AppPrefsManager setDropboxSavePath(String newValue) {
        openSharedEditor();
        mSharedEditor.putString(PREF_DROPBOX_SAVE_PATH, newValue);
        saveIfNeeded();
        return this;
    }

    // Look at the xml file!
    private static final String PREF_SYNCENABLED = "pref_enableBackgroundSync";
    public boolean isSyncEnabled() {
        return mAppPreferences.getBoolean(PREF_SYNCENABLED, false);
    }

    // Look at the xml file!
    private static final String PREF_SYNCFREQUENCY = "pref_syncFrequency";
    public String getSyncFrequency() {
        return mAppPreferences.getString(PREF_SYNCFREQUENCY, "0");
    }
}
