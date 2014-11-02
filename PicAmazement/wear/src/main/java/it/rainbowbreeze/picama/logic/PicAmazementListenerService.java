package it.rainbowbreeze.picama.logic;

import android.content.Intent;
import android.graphics.Picture;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.domain.AmazingPicture;
import it.rainbowbreeze.picama.ui.PictureActivity;

/**
 * Created by alfredomorresi on 02/11/14.
 */

public class PicAmazementListenerService extends WearableListenerService {
    private static final String LOG_TAG = PicAmazementListenerService.class.getSimpleName();

    @Inject
    private ILogFacility mLogFacility;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApp) getApplication()).inject(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        mLogFacility.v(LOG_TAG, "onDataChanged: " + dataEvents);

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();

        if (!mGoogleApiClient.isConnected()) {
            ConnectionResult connectionResult = mGoogleApiClient
                    .blockingConnect(30, TimeUnit.SECONDS);
            if (!connectionResult.isSuccess()) {
                mLogFacility.e(LOG_TAG, "Service failed to connect to GoogleApiClient.");
                return;
            }
        }

        String title = null;
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (Bag.DATAMAP_AMAZING_PICTURE.equals(path)) {
                    // Get the data out of the event
                    DataMapItem dataMapItem =
                            DataMapItem.fromDataItem(event.getDataItem());

                    title = dataMapItem.getDataMap().getString(AmazingPicture.FIELD_TITLE);
                    //Asset asset = dataMapItem.getDataMap().getAsset(KEY_IMAGE);
                }else {
                    mLogFacility.e(LOG_TAG, "Unrecognized path " + path);
                }
            }
        }

        if (TextUtils.isEmpty(title)) {
            return;
        }

        Intent startIntent = new Intent(this, PictureActivity.class);
        startIntent.putExtra(PictureActivity.INTENT_EXTRA_TITLE, title);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startIntent);
    }
}
