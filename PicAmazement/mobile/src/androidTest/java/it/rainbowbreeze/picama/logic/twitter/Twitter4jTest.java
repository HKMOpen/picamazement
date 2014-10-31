package it.rainbowbreeze.picama.logic.twitter;

import android.test.AndroidTestCase;

import java.util.Map;

import it.rainbowbreeze.picama.common.Bag;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public class Twitter4jTest extends AndroidTestCase {
    private ConfigurationBuilder builder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        builder = new ConfigurationBuilder();
        builder.setApplicationOnlyAuthEnabled(true);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void xxtestAuthWithBuildingConf2() throws Exception {
        // setup
        builder.setOAuthConsumerKey(Bag.TWITTER_CONSUMER_KEY).setOAuthConsumerSecret(Bag.TWITTER_CONSUMER_SECRET);
        Twitter twitter = new TwitterFactory(builder.build()).getInstance();

        // exercise & verify
        OAuth2Token token = twitter.getOAuth2Token();
        assertEquals("bearer", token.getTokenType());

        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
        assertNotNull(searchTweetsRateLimit);
        assertEquals(searchTweetsRateLimit.getLimit(), 450);

        assertNotNull("null statuses", twitter.getUserTimeline("picamazing100"));
    }
}
