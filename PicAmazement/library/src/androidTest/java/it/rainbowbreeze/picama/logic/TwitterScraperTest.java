package it.rainbowbreeze.picama.logic;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.List;

import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public class TwitterScraperTest extends AndroidTestCase {
    TwitterScraper mTwitterScraper;

    @Override
    protected void setUp() throws Exception {
        mTwitterScraper = new TwitterScraper();
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPictures() {
        List<AmazingPicture> pictures = mTwitterScraper.getNewPictures();
        Assert.assertEquals(null, pictures);
    }
}
