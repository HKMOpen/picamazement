package it.rainbowbreeze.picama.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;

/**
 *
 * Remember: Key names have to be equal to the ones in the xml file, otherwise two different
 *  settings are managed
 * Created by alfredomorresi on 05/12/14.
 */
public class AppPrefsManager {
    private static final String LOG_TAG = AppPrefsManager.class.getSimpleName();

    private final ILogFacility mLogFacility;

    public static final String PREFS_FILE_NAME = "PicAmazementPrefs";
    private static final String NULL_STRING = "null";

    private final Context mAppContext;
    private final SharedPreferences mAppPreferences;
    private boolean mSaveInBatch;
    private SharedPreferences.Editor mSharedEditor;

    public AppPrefsManager(Context appContext, ILogFacility logFacility) {
        mAppContext = appContext;
        mLogFacility = logFacility;
        mAppPreferences = appContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    private boolean hasDefaultValuesBeenSet() {
        // See http://developer.android.com/reference/android/preference/PreferenceManager.html#KEY_HAS_SET_DEFAULT_VALUES
        SharedPreferences appPreferences = mAppContext.getSharedPreferences(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, Context.MODE_PRIVATE);
        boolean defaultValuesSet = appPreferences.getBoolean(PreferenceManager.KEY_HAS_SET_DEFAULT_VALUES, false);
        return defaultValuesSet;
    }

    /**
     * Sets default values for the preferences, given the XML file
     */
    public void setDefaultValues() {
        if (!hasDefaultValuesBeenSet()) {
            mLogFacility.v(LOG_TAG, "Setting default preference values");
            // This call sets also the system flag
            PreferenceManager.setDefaultValues(mAppContext, PREFS_FILE_NAME, Context.MODE_PRIVATE, R.xml.pref_general, false);
            // Adds customized values
            setBatchSave()
                    .setDropboxEnabled(false)
                    .setDropboxAuthToken(NULL_STRING)
                    .save();
        }
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


    /**
     * Set batch save mode. When set, remember to call {@link #save()} at the end of your changes.
     * @return
     */
    public AppPrefsManager setBatchSave() {
        mSaveInBatch = true;
        return this;
    }
    public boolean save() {
        boolean result = mSharedEditor.commit();
        if (result) {
            mSaveInBatch = false;
            mSharedEditor = null;
        }
        return result;
    }
    public AppPrefsManager cancelBatchSave() {
        mSaveInBatch = false;
        mSharedEditor = null;
        return this;
    }


    private void openSharedEditor() {
        if (null == mSharedEditor) {
            mSharedEditor = mAppPreferences.edit();
        }
    }

    /**
     * Do not save while in batch edit mode, otherwise saves preferences
     * @return
     */
    private boolean saveIfNeeded() {
        return mSaveInBatch
                ? false
                : save();
    }

}
