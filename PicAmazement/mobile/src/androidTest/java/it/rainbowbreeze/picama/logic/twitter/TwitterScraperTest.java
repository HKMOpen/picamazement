package it.rainbowbreeze.picama.logic.twitter;

import android.test.AndroidTestCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import it.rainbowbreeze.picama.common.AndroidModule;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.domain.AmazingPicture;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public class TwitterScraperTest extends AndroidTestCase {
    @Inject TwitterScraper mTwitterScraper;

    /**
     * See https://github.com/square/dagger/issues/310 for "No no-args constructor on" error
     */
    @Module(
            includes = AndroidModule.class,
            overrides = true,
            injects = TwitterScraperTest.class
    )
    protected class TestModule {
       @Provides @Singleton TwitterScraperConfig provideTwitterScraperConfig() {
           return new TwitterScraperConfig();
       }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ObjectGraph
                .create(
                    new TestModule(),
                    new AndroidModule((MyApp) getContext().getApplicationContext()))
                .inject(this);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPictures() {
        List<AmazingPicture> pictures = mTwitterScraper.getNewPictures();
        assertNotNull(pictures);
        assertEquals(0, pictures.size());
    }

    public void testTwitter() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);

    }
}
