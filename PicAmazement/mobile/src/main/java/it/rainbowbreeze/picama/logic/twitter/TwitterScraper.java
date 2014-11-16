package it.rainbowbreeze.picama.logic.twitter;

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
    private List<String> mUserNames;

    @Inject
    public TwitterScraper (ILogFacility logFacility, TwitterScraperConfig config) {
        mLogFacility = logFacility;

        mLogFacility.v(LOG_TAG, "Initializing...");

        //TODO: move to DI
        ConfigurationBuilder cb = new ConfigurationBuilder()
                .setDebugEnabled(BuildConfig.DEBUG)
                .setApplicationOnlyAuthEnabled(true)
                .setOAuthConsumerKey(Bag.TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(Bag.TWITTER_CONSUMER_SECRET);
        mTwitter = new TwitterFactory(cb.build()).getInstance();

        mUserNames = config.getUserNames();
    }

    @Override
    public String getName() {
        return "Twitter - " + mUserNames.toString();
    }

    @Override
    public List<AmazingPicture> getNewPictures() {
        ArrayList<AmazingPicture> pictures = new ArrayList<AmazingPicture>();

        List<Status> statuses;
        initToken();

        for (String userName : mUserNames) {
            mLogFacility.v(LOG_TAG, "Analyzing Twitter account " + userName);
            try {
                statuses = mTwitter.getUserTimeline(userName);
                for (Status status : statuses) {
                    for (MediaEntity mediaEntity : status.getMediaEntities()) {
                        mLogFacility.v(LOG_TAG, " MEDIA -> " + mediaEntity.getDisplayURL() + " / " + mediaEntity.getMediaURL() + " / " + mediaEntity.getType());
                        // There is a photo attached to the tweet
                        if (!"photo".equals(mediaEntity.getType())) continue;

                        AmazingPicture pic = new AmazingPicture();
                        pic
                                .setUrl(mediaEntity.getMediaURL())
                                .setDate(status.getCreatedAt())
                                .setTitle(status.getText());
                        pictures.add(pic);
                    }
                }
            } catch (TwitterException te) {
                te.printStackTrace();
                mLogFacility.e(LOG_TAG, te);
            }
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
