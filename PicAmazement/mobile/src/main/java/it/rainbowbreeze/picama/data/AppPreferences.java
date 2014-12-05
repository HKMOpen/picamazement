package it.rainbowbreeze.picama.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alfredomorresi on 05/12/14.
 */
public class AppPreferences {
    private static final String PREFS_NAME = "PicAmazementPrefs";
    private final Context mAppContext;
    private final SharedPreferences mAppPrefences;
    private boolean mSaveInBatch;
    private SharedPreferences.Editor mSharedEditor;

    public AppPreferences(Context appContext) {
        mAppContext = appContext;
        mAppPrefences = appContext.getSharedPreferences(PREFS_NAME, 0);
    }

    private static final String PREF_DROPBOXENABLED = "Pref_DropboxEnabled";
    public boolean isDropboxEnabled() {
        return mAppPrefences.getBoolean(PREF_DROPBOXENABLED, false);
    }
    public AppPreferences setDropboxEnabled(boolean newValue) {
        openSharedEditor();
        mAppPrefences.getBoolean(PREF_DROPBOXENABLED, false);
        saveIfNeeded();
        return this;
    }

    private static final String PREF_SYNCENABLED = "Pref_SyncEnabled";
    public boolean isSyncEnabled() {
        return mAppPrefences.getBoolean(PREF_SYNCENABLED, false);
    }
    public AppPreferences setSyncEnabled(boolean newValue) {
        openSharedEditor();
        mAppPrefences.getBoolean(PREF_SYNCENABLED, false);
        saveIfNeeded();
        return this;
    }


    public AppPreferences setBatchSave() {
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
    public AppPreferences cancelBatchSave() {
        mSaveInBatch = false;
        mSharedEditor = null;
        return this;
    }


    private void openSharedEditor() {
        if (null == mSharedEditor) {
            mSharedEditor = mAppPrefences.edit();
        }
    }
    private boolean saveIfNeeded() {
        return !mSaveInBatch ? save() : false;
    }


}
