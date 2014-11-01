package it.rainbowbreeze.picama.logic.twitter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.domain.AmazingPicture;
import it.rainbowbreeze.picama.logic.IPictureScraper;
import it.rainbowbreeze.picama.shared.BuildConfig;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * Application-only auth:
 *   https://github.com/yusuke/twitter4j/blob/master/twitter4j-core/src/test/java/twitter4j/auth/ApplicationOnlyAuthTest.java
 *   https://dev.twitter.com/oauth/application-only
 *
 *   picamazing100 - picamazing100
 *
 * Created by alfredomorresi on 19/10/14.
 */
public class TwitterScraper implements IPictureScraper<TwitterScraperConfig> {
    private static final String LOG_TAG = TwitterScraper.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final Twitter mTwitter;
    private OAuth2Token mTwitterToken;
    private String mUserName;

    @Inject
    public TwitterScraper (ILogFacility logFacility, TwitterScraperConfig config) {
        mLogFacility = logFacility;

        //TODO: move to DI
        ConfigurationBuilder cb = new ConfigurationBuilder()
                .setDebugEnabled(BuildConfig.DEBUG)
                .setApplicationOnlyAuthEnabled(true)
                .setOAuthConsumerKey(Bag.TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(Bag.TWITTER_CONSUMER_SECRET);
        mTwitter = new TwitterFactory(cb.build()).getInstance();
    }

    @Override
    public String getName() {
        return LOG_TAG;
    }

    @Override
    public void initialize(TwitterScraperConfig config) {
        mUserName = config.getUserName();
    }

    @Override
    public List<AmazingPicture> getNewPictures() {
        ArrayList<AmazingPicture> pictures = new ArrayList<AmazingPicture>();

        try {
            List<Status> statuses;
            initToken();
            statuses = mTwitter.getUserTimeline(mUserName);
            for (Status status : statuses) {
                Log.d("TWITTER", status.getText());
                for (MediaEntity mediaEntity : status.getMediaEntities()) {
                    Log.d("TWITTER", " MEDIA -> " + mediaEntity.getDisplayURL() + " / " + mediaEntity.getMediaURL() + " / " + mediaEntity.getType());
                    // There is a photo attached to the tweet
                    if (!"photo".equals(mediaEntity.getType())) continue;

                    AmazingPicture pic = new AmazingPicture(
                            0,
                            mediaEntity.getMediaURL());
                    pictures.add(pic);
                }

            }

        } catch (TwitterException te) {
            te.printStackTrace();
            //TODO
        }

        return pictures;
    }

    private void initToken() {
        try {
            if (null == mTwitterToken) {
                mTwitterToken = mTwitter.getOAuth2Token();
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            //TODO
        }
    }
}
