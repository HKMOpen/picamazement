package it.rainbowbreeze.picama.common;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.logic.PictureScraperManager;
import it.rainbowbreeze.picama.logic.PictureScraperManagerConfig;
import it.rainbowbreeze.picama.logic.twitter.TwitterScraper;
import it.rainbowbreeze.picama.logic.twitter.TwitterScraperConfig;

/**
 * Dagger modules for classes that don't need an Application context
 * Created by alfredomorresi on 31/10/14.
 */
@Module (
        complete = true,
        library = true
)
public class MobileModule {
    @Provides @Singleton public ILogFacility provideLogFacility () {
        return new LogFacility();
    }

    @Provides @Singleton public PictureScraperManagerConfig providePictureScraperManagerConfig (
            ILogFacility logFacility) {
        // Creates Twitter Scraper
        TwitterScraperConfig twitterConfig = new TwitterScraperConfig();
        TwitterScraper twitterScraper = new TwitterScraper(logFacility, twitterConfig);

        PictureScraperManagerConfig config = new PictureScraperManagerConfig(
                twitterScraper
        );

        return config;
    }

    /**
     * Just a test class that need application context to be initialized
     * @return
     */
    @Provides @Singleton
    AmazingPictureDao provideAmazingPictureDao(
            ILogFacility logFacility) {
        return new AmazingPictureDao(logFacility);
    }

    /**
     * There is a provide method because the class have to be configure
     * before working. Otherwise a simple Inject the requiring class
     * could have been used.
     * @return
     */
    @Provides @Singleton public PictureScraperManager providePictureScrapeManager (
            ILogFacility logFacility,
            PictureScraperManagerConfig pictureScraperManagerConfig,
            AmazingPictureDao amazingPictureDao) {
        //creates config for the scraper
        return new PictureScraperManager(logFacility, pictureScraperManagerConfig, amazingPictureDao);
    }
}
