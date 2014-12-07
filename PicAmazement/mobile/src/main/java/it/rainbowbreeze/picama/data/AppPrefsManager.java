package it.rainbowbreeze.picama.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    private final Context mAppContext;
    private final SharedPreferences mAppPreferences;
    private boolean mSaveInBatch;
    private SharedPreferences.Editor mSharedEditor;

    public AppPrefsManager(Context appContext, ILogFacility logFacility) {
        mAppContext = appContext;
        mLogFacility = logFacility;
        mAppPreferences = appContext.getSharedPreferences(PREFS_FILE_NAME, 0);
    }

    /**
     * Sets default values for the preferences, given the XML file
     */
    public void setDefaultValues() {
        mLogFacility.v(LOG_TAG, "Setting default preference values");
        // See http://developer.android.com/reference/android/preference/PreferenceManager.html#KEY_HAS_SET_DEFAULT_VALUES
        PreferenceManager.setDefaultValues(mAppContext, PREFS_FILE_NAME, Context.MODE_PRIVATE, R.xml.pref_general, false);
    }

    private static final String PREF_DROPBOXENABLED = "pref_dropboxEnabled";
    public boolean isDropboxEnabled() {
        return mAppPreferences.getBoolean(PREF_DROPBOXENABLED, false);
    }
    public AppPrefsManager setDropboxEnabled(boolean newValue) {
        openSharedEditor();
        mAppPreferences.getBoolean(PREF_DROPBOXENABLED, false);
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
    private boolean saveIfNeeded() {
        return !mSaveInBatch ? save() : false;
    }

}
