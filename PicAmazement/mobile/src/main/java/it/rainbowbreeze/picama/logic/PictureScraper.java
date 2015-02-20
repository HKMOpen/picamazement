package it.rainbowbreeze.picama.logic;

import android.text.TextUtils;

import it.rainbowbreeze.picama.logic.twitter.TwitterScraperConfig;

/**
 * Created by alfredomorresi on 20/02/15.
 */
public abstract class PictureScraper<Config extends IPictureScraperConfig> implements IPictureScraper {
    @Override
    public boolean applyConfig(IPictureScraperConfig newConfig) {
        if (null == newConfig) return false;

        Config config;
        try {
            config = (Config) newConfig;
        } catch (ClassCastException e) {
            return false;
        }
        applyConfigInternal(config);
        return true;
    }

    protected abstract void applyConfigInternal(Config newConfig);
}
