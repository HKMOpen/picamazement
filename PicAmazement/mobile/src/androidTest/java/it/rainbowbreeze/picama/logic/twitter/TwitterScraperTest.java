package it.rainbowbreeze.picama.logic.twitter;

import android.test.AndroidTestCase;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import it.rainbowbreeze.picama.common.AndroidModule;

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
                    new AndroidModule(getContext()))
                .inject(this);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
    public void testGetPictures() {
        List<AmazingPicture> pictures = mTwitterScraper.getNewPictures();
        assertNotNull(pictures);
        assertEquals(0, pictures.size());
    }

    public void testTwitter() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);

    }
    */

    public void testSanitizeText() {
        assertNull("Sanitizer didn't work",
                mTwitterScraper.sanitizeText(null));
        assertEquals("Sanitizer didn't work",
                "",
                mTwitterScraper.sanitizeText(""));
        assertEquals("Sanitizer didn't work",
                "",
                mTwitterScraper.sanitizeText("http://www.test.com/image.jpg"));
        assertEquals("Sanitizer didn't work",
                "Alucinante! esto es realmente una foto, no dos.",
                mTwitterScraper.sanitizeText("Alucinante! esto es realmente una foto, no dos. http://t.co/7ME1v7N7Gr"));
        assertEquals("Sanitizer didn't work",
                "Alucinante! esto es realmente una foto, no dos.",
                mTwitterScraper.sanitizeText("Alucinante! esto es realmente una foto, no dos. http://t.co/7ME1v7N7Gr  "));
        assertEquals("Sanitizer didn't work",
                "These Parents Apparently Failed at Parenting, Does Our Generation Is Lost..?",
                mTwitterScraper.sanitizeText("These Parents Apparently Failed at Parenting, Does Our Generation Is Lost..? http://goo.gl/P3MJd8"));
    }
}
