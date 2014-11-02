package it.rainbowbreeze.picama.logic;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

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
    private GoogleApiClient mGoogleAppClient;

    public WearManager(ILogFacility logFacility, @ForApplication Context appContext) {
        mLogFacility = logFacility;
        mAppContext = appContext;
    }

    public void init() {
        synchronized (mSync) {
            mGoogleAppClient = new GoogleApiClient.Builder(mAppContext)
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
    }

    public void transferAmazingPicture(AmazingPicture picture) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(Bag.DATAMAP_AMAZING_PICTURE);
        dataMap.getDataMap().putString(AmazingPicture.FIELD_TITLE, picture.getTitle());
        dataMap.getDataMap().putString(AmazingPicture.FIELD_URL, picture.getUrl());
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(
                mGoogleAppClient, request);
    }

}
