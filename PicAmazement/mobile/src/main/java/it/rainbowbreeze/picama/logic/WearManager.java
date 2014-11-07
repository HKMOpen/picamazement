package it.rainbowbreeze.picama.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
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
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ForApplication;
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
    private final ILogFacility mLogFacility;
    private final Context mAppContext;
    private Object mSync;
    private GoogleApiClient mGoogleApiClient;

    public WearManager(ILogFacility logFacility, @ForApplication Context appContext) {
        mLogFacility = logFacility;
        mAppContext = appContext;
    }

    public void init() {
        mGoogleApiClient = new GoogleApiClient.Builder(mAppContext)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        mLogFacility.v(LOG_TAG, "onConnected: " + connectionHint);
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
                    }
                })
                .addApi(Wearable.API)
                .build();
    }

    public void onStart() {
        mGoogleApiClient.connect();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
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
     * Prepares and send a picture to Android Wear, async
     *
     * @param context
     * @param picture
     */
    public void transferAmazingPicture(Context context, final AmazingPicture picture) {
        mLogFacility.v(LOG_TAG, "Sending to Wear picture " + picture.getTitle());
        Collection<String> nodes = getNodes();
        if (nodes.isEmpty()) {
            mLogFacility.v(LOG_TAG, "Unfortunately, no nodes were found");
            return;
        }

        // Test
        for(String node:nodes) {
            mLogFacility.v(LOG_TAG, "Sending data to node " + node);
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, node, Bag.WEAR_MESSAGE_SIMPLE, null).await();
            if (!result.getStatus().isSuccess()) {
                mLogFacility.e(LOG_TAG, "ERROR: failed to send Message: " + result.getStatus());
            }
        }

        // Launches Picasso to retrieve the image
        Bitmap image = null;
        try {
            image = Picasso.with(mAppContext)
                    .load(picture.getUrl())
                    .resize(640, 400) // Allows parallax scrolling
                    .get();
        } catch (IOException e) {
            mLogFacility.e(LOG_TAG, e);
        }

        if (null == image) {
            mLogFacility.v(LOG_TAG, "No image to transfer to Wear, aborting");
            return;
        }
        mLogFacility.v(LOG_TAG, "Valid bitmap, Wear DataRequest creation");
        // Prepares the DataMapRequest
        PutDataMapRequest dataMap = PutDataMapRequest.create(Bag.WEAR_DATAMAP_AMAZINGPICTURE);
        dataMap.getDataMap().putString(AmazingPicture.FIELD_TITLE, picture.getTitle());
        dataMap.getDataMap().putAsset(AmazingPicture.FIELD_IMAGE, createAssetFromBitmap(image));
        dataMap.getDataMap().putLong(AmazingPicture.FIELD_TIMESTAMP, (new Date()).getTime());
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(
                mGoogleApiClient, request);
        pendingResult.await();
        mLogFacility.v(LOG_TAG, "Transferred to Wear");
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

}
