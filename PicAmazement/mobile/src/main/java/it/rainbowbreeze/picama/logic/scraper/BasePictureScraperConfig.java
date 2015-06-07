package it.rainbowbreeze.picama.logic.scraper;

/**
 * A specialized picture scraper config object that saves configs in a preferences file
 *
 * Created by alfredomorresi on 07/06/15.
 */
public abstract class BasePictureScraperConfig<PrefsManager extends BaseScraperPrefsManager> implements IPictureScraperConfig {
    protected PrefsManager mPrefsManager;

    /**
     * Assign the preferences manager to this object
     * @param newValue
     */
    protected void initPrefsManager(PrefsManager newValue) {
        mPrefsManager = newValue;
        mPrefsManager.setDefaultValues(false);
    }

    /**
     * Scraper is active
     * @return
     */
    public boolean isEnabled() {
        return mPrefsManager.isEnabled();
    }

    public void setEnabled(boolean isEnabled) {
        mPrefsManager.setEnabled(isEnabled);
    }
}
