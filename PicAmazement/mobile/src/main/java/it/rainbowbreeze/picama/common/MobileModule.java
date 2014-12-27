package it.rainbowbreeze.picama.common;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.picama.logic.storage.FileDownloaderHelper;
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

    /**
     * There is a provide method because the class have to be configure
     * before working. Otherwise a simple Inject the requiring class
     * could have been used.
     * @return
     */
    @Provides @Singleton public PictureScraperManagerConfig providePictureScraperManagerConfig (
            ILogFacility logFacility,
            TwitterScraperConfig twitterScraperConfig) {
        TwitterScraper twitterScraper = new TwitterScraper(logFacility, twitterScraperConfig);

        PictureScraperManagerConfig config = new PictureScraperManagerConfig(
                twitterScraper
        );

        return config;
    }

    @Provides @Singleton public TwitterScraperConfig provideTwitterScraperConfig() {
        return new TwitterScraperConfig();
    }

    @Provides @Singleton
    FileDownloaderHelper provideFileDownloaderHelper(ILogFacility logFacility) {
        return new FileDownloaderHelper(logFacility);
    }
}
