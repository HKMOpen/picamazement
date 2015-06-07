package it.rainbowbreeze.picama.common;

import it.rainbowbreeze.picama.BuildConfig;

/**
 * Created by alfredomorresi on 31/10/14.
 */
public class Bag extends SharedBag {
    public final static String TWITTER_CONSUMER_KEY = BuildConfig.TWITTER_CONSUMER_KEY;
    public final static String TWITTER_CONSUMER_SECRET = BuildConfig.TWITTER_CONSUMER_SECRET;

    public static boolean wearAvailable = false;

    //See the settings XML and manifest declaretion for the values of these actions
    public static final String ACTION_SETTINGS_DEBUG = "action_StartDebugSettings";
    public static final String ACTION_SETTINGS_DROPBOX = "action_StartDropboxSettings";
    public static final String ACTION_SETTINGS_TWITTER = "action_StartTwitterSettings";
    public static final String ACTION_SETTINGS_ONEBIGPICTURE = "action_StartOneBigPictureSettings";

    public final static String DROPBOX_APP_KEY = BuildConfig.DROPBOX_APP_KEY;
    public final static String DROPBOX_APP_SECRET = BuildConfig.DROPBOX_APP_SECRET;

    public final static int JOBID_SCHEDULE_RETRIEVE = 100;

}
