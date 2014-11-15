package it.rainbowbreeze.picama.logic;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.concurrent.TimeUnit;

import it.rainbowbreeze.picama.common.ILogFacility;


/**
 * Base Service to manage Google API Client requests.
 *
 * Extends {@link it.rainbowbreeze.picama.logic.GoogleApiClientBaseService#doYourStaff(android.content.Intent)}
 * with the code requested by your application
 *
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public abstract class GoogleApiClientBaseService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = GoogleApiClientBaseService.class.getSimpleName();
    private final ILogFacility mLogFacility;
    protected GoogleApiClient mGoogleApiClient;

    public GoogleApiClientBaseService(String serviceName, ILogFacility logFacility) {
        super(serviceName);
        mLogFacility = logFacility;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = createGoogleApiClient().build();
    }

    /**
     * Returns the required Google API Client API to connect with.
     * Could be Drive.API or Plus.API or Wearable.API
     * @return
     */
    public abstract Api getApi();
    public abstract void doYourStaff(Intent intent);

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isValidIntent(intent)) {
            mLogFacility.i(LOG_TAG, "Intent received is not valid, aborting");
            return;
        }
        ConnectionResult connectionResult = connectToGoogleApi();
        if (mGoogleApiClient.isConnected()) {
            doYourStaff(intent);
            disconnectFromGoogleApi();
        } else {
            mLogFacility.i(LOG_TAG, "Google Api Client isn't connected, aborting");
        }
    }

    /**
     * Checks if given intent is valid and can be processed by {@link it.rainbowbreeze.picama.logic.GoogleApiClientBaseService#onHandleIntent(android.content.Intent)}.
     * Base implementation only checks for null intent
     *
     * Change this method and add your own custom validation logic
     *
     * @param intent
     * @return true if the intent is valid, otherwise false
     */
    protected boolean isValidIntent(Intent intent) {
        return (null != intent);
    }

    /**
     * Google API Client Connection time before giving an error, in seconds
     * Default is 30.
     * @return
     */
    protected int getMaxConnectionTime() {
        return 30;
    }
    protected GoogleApiClient.Builder createGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .addApi(getApi())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this);
    }

    /**
     *
     */
    protected ConnectionResult connectToGoogleApi() {
        return mGoogleApiClient.blockingConnect(
                getMaxConnectionTime(),
                TimeUnit.SECONDS);
    }

    protected void disconnectFromGoogleApi() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLogFacility.v(LOG_TAG, "Google Api Client connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mLogFacility.i(LOG_TAG, "Google Api Client connection suspended with reason " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mLogFacility.i(LOG_TAG, "Connection to Google Api client failed with error " +
                connectionResult.getErrorCode());
    }
}
