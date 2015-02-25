package it.rainbowbreeze.picama.logic.twitter;

import android.content.Context;

import java.util.Set;
import java.util.TreeSet;

import it.rainbowbreeze.libs.data.RainbowAppPrefsManager;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.IPictureScraperConfig;

/**
 * Created by alfredomorresi on 01/11/14.
 */
public class TwitterScraperConfig implements IPictureScraperConfig {
    private static final String LOG_TAG = TwitterScraperConfig.class.getSimpleName();
    private final TwitterPrefsManager mTwitterPrefsManager;

    public TwitterScraperConfig(Context appContext, ILogFacility logFacility) {
        mTwitterPrefsManager = new TwitterPrefsManager(appContext, logFacility);
        mTwitterPrefsManager.setDefaultValues(false);
    }

    public Set<String> getUserNames() {
        return mTwitterPrefsManager.getTwitterUserNames();
    }

    public void setUserNames(Set<String> userNames) {
        mTwitterPrefsManager.setTwitterUserNames(userNames);
    }

    private class TwitterPrefsManager extends RainbowAppPrefsManager {
        private static final String PREF_FILENAME = "TwitterPrefs";

        public TwitterPrefsManager(Context appContext, ILogFacility logFacility) {
            super(appContext, PREF_FILENAME, 0, logFacility);
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
            userNames.add("planetepics");
            setTwitterUserNames(userNames);
        }

        private static final String PREF_TWITTERUSERNAMES = "pref_twitterUserNames";
        public Set<String> getTwitterUserNames() {
            Set<String> result = mAppPreferences.getStringSet(PREF_TWITTERUSERNAMES, null);
            return null == result
                    ? new TreeSet<String>()
                    : result;
        }
        public TwitterPrefsManager setTwitterUserNames(Set<String> newValue) {
            openSharedEditor();
            mSharedEditor.putStringSet(PREF_TWITTERUSERNAMES, newValue);
            saveIfNeeded();
            return this;
        }

    }
}
