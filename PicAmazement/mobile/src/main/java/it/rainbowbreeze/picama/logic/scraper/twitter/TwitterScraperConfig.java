package it.rainbowbreeze.picama.logic.scraper.twitter;

import android.content.Context;

import java.util.Set;
import java.util.TreeSet;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.scraper.BasePictureScraperConfig;
import it.rainbowbreeze.picama.logic.scraper.BaseScraperPrefsManager;

/**
 * Created by alfredomorresi on 01/11/14.
 */
public class TwitterScraperConfig extends BasePictureScraperConfig<TwitterScraperConfig.PrefsManager> {
    private static final String LOG_TAG = TwitterScraperConfig.class.getSimpleName();

    public TwitterScraperConfig(Context appContext, ILogFacility logFacility) {
        super();
        initPrefsManager(new PrefsManager(appContext, logFacility));
    }

    public Set<String> getUserNames() {
        return mPrefsManager.getTwitterUserNames();
    }
    public void setUserNames(Set<String> userNames) {
        mPrefsManager.setTwitterUserNames(userNames);
    }

    /**
     * Manages Twitter specific configuration
     */
    protected class PrefsManager extends BaseScraperPrefsManager {
        private static final String PREF_FILENAME = "TwitterPrefs";

        public PrefsManager(Context appContext, ILogFacility logFacility) {
            super(appContext, PREF_FILENAME, logFacility);
        }

        @Override
        protected void setDefaultValuesInternal() {
            Set<String> userNames = new TreeSet<>();
            userNames.add("EarthBeauties");
            //userNames.add("_Paisajes_");
            userNames.add("HighFromAbove");
            userNames.add("HistoryInPics");
            userNames.add("LuoghiDalMondo");
            userNames.add("FotoFavolose");
            userNames.add("Globe_Pics");
            userNames.add("MeredithFrost");
            userNames.add("planetepics");
            setTwitterUserNames(userNames);
            setEnabled(true);
        }

        private static final String PREF_TWITTERUSERNAMES = "pref_twitterUserNames";
        public Set<String> getTwitterUserNames() {
            Set<String> result = mAppPreferences.getStringSet(PREF_TWITTERUSERNAMES, null);
            return null == result
                    ? new TreeSet<String>()
                    : result;
        }
        public PrefsManager setTwitterUserNames(Set<String> newValue) {
            openSharedEditor();
            mSharedEditor.putStringSet(PREF_TWITTERUSERNAMES, newValue);
            saveIfNeeded();
            return this;
        }
    }
}
