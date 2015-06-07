package it.rainbowbreeze.picama.logic.scraper;

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
     * Returns if the scraper is enabled or not
     * @return
     */
    boolean isEnabled();

    /**
     * Returns the scraper internal name, for example "Twitter", "OneBigPicture" ecc
     * @return
     */
    String getSourceName();
}
