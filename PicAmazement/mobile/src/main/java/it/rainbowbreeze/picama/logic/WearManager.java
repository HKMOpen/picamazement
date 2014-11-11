package it.rainbowbreeze.picama.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 02/11/14.
 */
public class WearManager {
    /**
     * Dagger example, the class doesn't need a direct declaration on a module.
     * @Inject annotates the constructor, so the fields are injected, otherwise
     *  every field can be decorated with @Inject and there is no need to decorate
     *  the constructor, that can be without parameters
     *
    private final ILogFacility mLogFacility;
    private final Context mAppContect;
     @Inject
     public WearManager(ILogFacility logFacility, @ForApplication Context appContext) {
         mLogFacility = logFacility;
         mAppContect = appContext;
         mLogFacility.v(LOG_TAG, "Starting Wear Manager");
     }
     */

    private static final String LOG_TAG = WearManager.class.getSimpleName();
    private static final long GCLIENT_TIMEOUT = 30;
    private final ILogFacility mLogFacility;
    private final Context mAppContext;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIsWearAvailable;

    public WearManager(ILogFacility logFacility, Context appContext) {
        mLogFacility = logFacility;
        mAppContext = appContext;
    }

    public boolean isWearAvailable() {
        return mIsWearAvailable;
    }
    public boolean isWearNotAvailable() {
        return !mIsWearAvailable;
    }

    public void init() {
        mGoogleApiClient = new GoogleApiClient.Builder(mAppContext)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        mLogFacility.v(LOG_TAG, "onConnected: " + connectionHint);
                        setWearAvailability(null);
                        // Now you can use the data layer API
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        mLogFacility.v(LOG_TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        mLogFacility.v(LOG_TAG, "onConnectionFailed: " + result);
                        setWearAvailability(result);
                    }
                })
                .addApi(Wearable.API)
                .build();
    }

    /**
     * Connect to the client.
     * Do not use in the UI thread
     */
    public void onStartAwait() {
        obtainWorkingClient();
    }

    /**
     * Connect to the client
     */
    public void onStartASync() {
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
    }

    /**
     * Returns a working Google API Client.
     * Cannot run in the UI thread
     * @return
     */
    private int obtainWorkingClient() {
        ConnectionResult connectionResult = mGoogleApiClient
                .blockingConnect(GCLIENT_TIMEOUT, TimeUnit.SECONDS);
        setWearAvailability(connectionResult);

        if (!connectionResult.isSuccess()) {
            mLogFacility.e(LOG_TAG, "Service failed to connect to GoogleApiClient.");
            return connectionResult.getErrorCode();
        } else {
            return ConnectionResult.SUCCESS;
        }
    }

    private void setWearAvailability(ConnectionResult result) {
        if (null == result || result.isSuccess())
            mIsWearAvailable = true;
        else if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            mIsWearAvailable = false;
        } else {
            mIsWearAvailable = false; // Conservative approach
        }
    }

    /**
     * Search for different nodes
     * @return
     */
    private Collection<String> getNodes() {
        mLogFacility.v(LOG_TAG, "Getting connected nodes");
        HashSet<String> results= new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }
        mLogFacility.v(LOG_TAG, "Found nodes " + results.size());
        return results;
    }

    /**
     * Prepares and send a picture to Android Wear, sync
     *
     * @param picture
     */
    public void transferAmazingPicture(final AmazingPicture picture, Bitmap bitmap) {
        mLogFacility.v(LOG_TAG, "Sending to Wear picture " + picture.getTitle());
        if (isWearNotAvailable()) {
            return;
        }
        // Prepares the DataMapRequest
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(Bag.WEAR_DATAMAP_AMAZINGPICTURE);
        picture.fillDataMap(dataMapRequest.getDataMap());
        dataMapRequest.getDataMap().putAsset(AmazingPicture.FIELD_IMAGE, createAssetFromBitmap(bitmap));
        PutDataRequest request = dataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(
                mGoogleApiClient, request);
        pendingResult.await();
        mLogFacility.v(LOG_TAG, "Transferred to Wear picture with id " + picture.getId());
    }

    public boolean isPlayServiceAvailable() {
        return ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable(mAppContext);
    }

    /**
     * Transform a {@link android.graphics.Bitmap} into an {@link com.google.android.gms.wearable.Asset}
     *
     * @param bitmap
     * @return
     */
    private Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
        return Asset.createFromBytes(bs.toByteArray());
    }

    /**
     * Test for sending simple messages
     */
    private void sendMessagesTest() {
        Collection<String> nodes = getNodes();
        if (nodes.isEmpty()) {
            mLogFacility.v(LOG_TAG, "Unfortunately, no nodes were found");
            return;
        }
        for(String node:nodes) {
            mLogFacility.v(LOG_TAG, "Sending data to node " + node);
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, node, Bag.WEAR_MESSAGE_SIMPLE, null).await();
            if (!result.getStatus().isSuccess()) {
                mLogFacility.e(LOG_TAG, "ERROR: failed to send Message: " + result.getStatus());
            }
        }
    }
}
