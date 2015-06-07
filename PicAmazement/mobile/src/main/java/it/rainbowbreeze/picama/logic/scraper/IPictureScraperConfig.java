package it.rainbowbreeze.picama.logic.scraper;

/**
 * Basic interface to configure a scraper. The idea is that this class can
 * be saved and loaded from persistent storage
 *
 * Created by alfredomorresi on 01/11/14.
 */
public interface IPictureScraperConfig {
    /**
     * The scraper is enabled
     * @return
     */
    boolean isEnabled();

    /**
     * Sets if the scraper is enabled or not
     * @param isEnabled
     */
    void setEnabled(boolean isEnabled);
}
