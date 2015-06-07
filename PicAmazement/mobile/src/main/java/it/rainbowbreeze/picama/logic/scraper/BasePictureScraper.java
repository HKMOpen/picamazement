package it.rainbowbreeze.picama.logic.scraper;

import it.rainbowbreeze.picama.logic.scraper.IPictureScraper;
import it.rainbowbreeze.picama.logic.scraper.IPictureScraperConfig;

/**
 * Created by alfredomorresi on 20/02/15.
 */
public abstract class BasePictureScraper<Config extends IPictureScraperConfig> implements IPictureScraper<Config> {
    protected Config mConfig;

    protected BasePictureScraper(Config newConfig) {
        mConfig = newConfig;
    }


    @Override
    public boolean isEnabled() {
        // Gets this value from the config
        return mConfig.isEnabled();
    }
}
