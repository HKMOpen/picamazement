package it.rainbowbreeze.picama.logic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.rainbowbreeze.picama.logic.twitter.TwitterScraper;
import it.rainbowbreeze.picama.logic.twitter.TwitterScraperConfig;

/**
 * Class to configure {@link it.rainbowbreeze.picama.logic.PictureScraperManager} class
 * Created by alfredomorresi on 01/11/14.
 */
public class PictureScraperManagerConfig {
    public final List<IPictureScraper> mPictureScrapers;

    public PictureScraperManagerConfig(IPictureScraper... pictureScrapers) {
        List<IPictureScraper> scrapers;
        scrapers = new ArrayList<IPictureScraper>();
        if (null != pictureScrapers) {
            for(IPictureScraper scraper : pictureScrapers)
            scrapers.add(scraper);
        }
        this.mPictureScrapers = scrapers;
    }

    public List<IPictureScraper> getPictureScrapers() {
        return mPictureScrapers;
    }
}
