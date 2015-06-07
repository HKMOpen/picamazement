package it.rainbowbreeze.picama.logic.scraper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to configure {@link PictureScraperManager} class
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
