package it.rainbowbreeze.picama.logic.scraper;

import android.content.Context;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.libs.data.RainbowAppPrefsManager;

/**
 * Basic config for all the scrapers, like enable/disable status etc
 * In addition, configs are persisted in a preference file
 *
 * Created by alfredomorresi on 07/06/15.
 */
public abstract class BaseScraperPrefsManager extends RainbowAppPrefsManager {
    private static final String PREF_ENABLED = "pref_Enabled";

    public BaseScraperPrefsManager(Context appContext, String prefsFileName, IRainbowLogFacility logFacility) {
        super(appContext, prefsFileName, 0, logFacility);
    }

    public boolean isEnabled() {
        return mAppPreferences.getBoolean(PREF_ENABLED, true);
    }

    public BaseScraperPrefsManager setEnabled(boolean newValue) {
        openSharedEditor();
        mSharedEditor.putBoolean(PREF_ENABLED, newValue);
        saveIfNeeded();
        return this;
    }
}
