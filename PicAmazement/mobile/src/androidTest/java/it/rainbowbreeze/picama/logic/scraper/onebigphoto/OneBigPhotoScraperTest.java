package it.rainbowbreeze.picama.logic.scraper.onebigphoto;

import android.test.AndroidTestCase;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import it.rainbowbreeze.picama.common.AndroidModule;
import it.rainbowbreeze.picama.common.ILogFacility;

public class OneBigPhotoScraperTest extends AndroidTestCase {
    @Inject
    OneBigPhotoScraper mOneBigPhotoScraper;

    /**
     * See https://github.com/square/dagger/issues/310 for "No no-args constructor on" error
     */
    @Module(
            includes = AndroidModule.class,
            overrides = true,
            injects = OneBigPhotoScraperTest.class
    )
    protected class TestModule {
        @Provides @Singleton OneBigPhotoScraper provideOneBigPhotoScraper(
                ILogFacility logFacility,
                OneBigPhotoScraperConfig oneBigPhotoScraperConfig) {
            return new OneBigPhotoScraper(logFacility, oneBigPhotoScraperConfig);
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

    public void testSanitizeUrl() throws Exception {
        assertNull("Sanitizer didn't work",
                mOneBigPhotoScraper.sanitizeUrl(null));
        assertEquals("Sanitized didn't work",
                "http://onebigphoto.com/uploads/2015/03/siberian-husky-walking-on-a-frozen-lake.jpg",
                mOneBigPhotoScraper.sanitizeUrl("http://onebigphoto.com/wp-content/themes/onebigphotoNEW/timthumb.php?src=http://onebigphoto.com/uploads/2015/03/siberian-husky-walking-on-a-frozen-lake.jpg&w=280&h=270"));
        assertEquals("Sanitized didn't work",
                "http://onebigphoto.com/uploads/2015/03/geisha-make-up-kyoto-japan.jpg",
                mOneBigPhotoScraper.sanitizeUrl("http://onebigphoto.com/wp-content/themes/onebigphotoNEW/timthumb.php?src=http://onebigphoto.com/uploads/2015/03/geisha-make-up-kyoto-japan.jpg&w=280&h=270"));
        assertEquals("Sanitized didn't work",
                "http://onebigphoto.com/uploads/2011/10/red-path-of-autumn.jpg",
                mOneBigPhotoScraper.sanitizeUrl("http://onebigphoto.com/wp-content/themes/onebigphotoNEW/timthumb.php?src=http://onebigphoto.com/uploads/2011/10/red-path-of-autumn.jpg&w=280&h=270"));
    }
}