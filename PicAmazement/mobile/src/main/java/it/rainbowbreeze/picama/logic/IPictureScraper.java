package it.rainbowbreeze.picama.logic;

import java.util.List;

import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public interface IPictureScraper <Config extends IPictureScraperConfig>{

    /**
     * Get new pictures for this scraper
     * @return
     */
    List<AmazingPicture> getNewPictures();

    /**
     * Returns the scraper internal name, for example "Twitter", "OneBigPicture" ecc
     * @return
     */
    String getSourceName();

    /**
     * Apply a new config to the scraper
     * @param newConfig
     * @return false if the configuration is not appropriate for the scraper, otherwise true
     */
    boolean applyConfig(IPictureScraperConfig newConfig);
}
