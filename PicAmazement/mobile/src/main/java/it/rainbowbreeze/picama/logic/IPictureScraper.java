package it.rainbowbreeze.picama.logic;

import java.util.List;

import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public interface IPictureScraper <Config extends IPictureScraperConfig>{
    List<AmazingPicture> getNewPictures();

    /**
     * Initialize the scraper with a particular config
     * @param config
     */
    void initialize(Config config);

    String getName();
}
