package it.rainbowbreeze.picama.logic.storage;

import android.content.Context;
import android.text.TextUtils;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AppPrefsManager;

/**
 * Created by alfredomorresi on 01/12/14.
 */
public class DropboxCloudProvider extends BaseCloudProvider {
    private static final String LOG_TAG = DropboxCloudProvider.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final AppPrefsManager mAppPrefsManager;
    private DropboxAPI<AndroidAuthSession> mDBApi;
    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.DROPBOX;
    private boolean mIsInAuthFlow = false;
    private boolean mIsInitialed = false;

    public DropboxCloudProvider(ILogFacility logFacility, AppPrefsManager appPrefsManager) {
        mLogFacility = logFacility;
        mAppPrefsManager = appPrefsManager;
    }

    public void initDropboxApi() {
        if (mIsInitialed) return;
        mLogFacility.v(LOG_TAG, "Initializing internal Dropbox object");
        AppKeyPair appKeys = new AppKeyPair(Bag.DROPBOX_APP_KEY, Bag.DROPBOX_APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys, ACCESS_TYPE);
        if (mAppPrefsManager.isDropboxAuthorized()) {
            session.setOAuth2AccessToken(mAppPrefsManager.getDropboxOAuth2AccessToken());
        }
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mIsInitialed = true;
    }

    /**
     * Starts
     * @param context
     */
    public void startDropboxAuthFlow(Context context) {
        mLogFacility.v(LOG_TAG, "Starting Dropbox auth flow");
        mIsInAuthFlow = true;
        // MyActivity below should be your activity class name
        mDBApi.getSession().startOAuth2Authentication(context);
    }

    /**
     * Checks the result of the authentication flow with Dropbox.
     *
     * @return true if the user is authenticated, otherwise false
     */
    public boolean finishDropboxAuthFlow() {
        if (!mIsInAuthFlow) return true;

        mIsInAuthFlow = false;
        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                mLogFacility.v(LOG_TAG, "Finishing Dropbox auth flow, authentication successful!");
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                /**
                 * From Dropbox reference of AndroidAuthSession at https://www.dropbox.com/static/developers/dropbox-android-sdk-1.6.1-docs/index.html
                 *  AccessTokenPair tokens = session.getAccessTokenPair();
                 *  ... now Store tokens.key, tokens.secret somewhere ...
                 *  But, unfortunately, tokens is null
                 *
                 * From Dropbox tutorial at https://www.dropbox.com/developers/core/start/android
                 *  String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                 */
                mLogFacility.v(LOG_TAG, "Storing Dropbox token for later access");
                String token = mDBApi.getSession().getOAuth2AccessToken();
                mAppPrefsManager.setDropboxAuthToken(token);
                return true;
            } catch (IllegalStateException e) {
                mLogFacility.e(LOG_TAG, "Error authenticating to Dropbox", e);
                mAppPrefsManager.resetDropboxAuthToken();
                return false;
            }
        } else {
            mLogFacility.v(LOG_TAG, "Finishing Dropbox auth flow, but authentication was unsuccessful");
            mAppPrefsManager.resetDropboxAuthToken();
            return false;
        }
    }

    public boolean isAuthRequired() {
        return !mAppPrefsManager.isDropboxAuthorized();
    }

    @Override
    public String getName() {
        return "Dropbox";
    }

    @Override
    public boolean isSavePossible() {
        return mAppPrefsManager.isDropboxEnabled() && mAppPrefsManager.isDropboxAuthorized();
    }

    @Override
    public boolean save(File dataFile) {
        // Just in case...
        initDropboxApi();

        mLogFacility.v(LOG_TAG, "Saving to Dropbox file " + dataFile.getAbsolutePath());
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(dataFile);
        } catch (FileNotFoundException e) {
            mLogFacility.e(LOG_TAG, "Cannot open the file to upload", e);
            return false;
        }

        String uploadFileName = dataFile.getName();
        if (!TextUtils.isEmpty(mAppPrefsManager.getDropboxSavePath()) &&
                mAppPrefsManager.getDropboxSavePath() != "null") {
            File uploadFile = new File(mAppPrefsManager.getDropboxSavePath(), uploadFileName);
            uploadFileName = uploadFile.getAbsolutePath();
        }
        DropboxAPI.Entry response = null;
        try {
            response = mDBApi.putFile("/" + uploadFileName, inputStream,
                    dataFile.length(), null, null);
        } catch (DropboxException e) {
            mLogFacility.e(LOG_TAG, "Cannot upload file to Dropbox", e);
            return false;
        }
        mLogFacility.v(LOG_TAG, "File upload to path " + uploadFileName + " with review " + response.rev);
        return true;
    }

}
