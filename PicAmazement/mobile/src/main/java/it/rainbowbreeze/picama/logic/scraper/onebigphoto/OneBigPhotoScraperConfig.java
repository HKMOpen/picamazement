package it.rainbowbreeze.picama.logic.scraper.onebigphoto;

import android.content.Context;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.logic.scraper.BasePictureScraperConfig;
import it.rainbowbreeze.picama.logic.scraper.BaseScraperPrefsManager;
import it.rainbowbreeze.picama.logic.scraper.IPictureScraperConfig;

/**
 * Created by alfredomorresi on 10/03/15.
 */
public class OneBigPhotoScraperConfig extends BasePictureScraperConfig<OneBigPhotoScraperConfig.PrefsManager> {
    private static final String LOG_TAG = OneBigPhotoScraperConfig.class.getSimpleName();

    public OneBigPhotoScraperConfig(Context appContext, ILogFacility logFacility) {
        super();
        initPrefsManager(new PrefsManager(appContext, logFacility));
    }

    protected class PrefsManager extends BaseScraperPrefsManager {
        private static final String PREF_FILENAME = "OneBigPhotoPrefs";

        public PrefsManager(Context appContext, ILogFacility logFacility) {
            super(appContext, PREF_FILENAME, logFacility);
        }

        @Override
        protected void setDefaultValuesInternal() {
            setEnabled(true);
        }
    }
}
