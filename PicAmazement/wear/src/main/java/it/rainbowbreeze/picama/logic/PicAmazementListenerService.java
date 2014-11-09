package it.rainbowbreeze.picama.logic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
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

    /**
     * Another way to use Dagger: @Inject the object directly as a field.
     * Default null constructor is called and the field is injected
     */
    @Inject ILogFacility mLogFacility;
    @Inject WearManager mWearManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApp) getApplication()).inject(this);
        mLogFacility.v(LOG_TAG, "onCreated");

        mWearManager.init();
        mWearManager.onStartASync();
    }


    @Override
    public void onDestroy() {
        mWearManager.onStop();
        super.onDestroy();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(Bag.WEAR_MESSAGE_SIMPLE)) {
            mLogFacility.v(LOG_TAG, "Received simple message");
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        mLogFacility.v(LOG_TAG, "onDataChanged: " + dataEvents);

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();

        // Reads data from the DataApi
        String title = null;
        Asset bitmapAsset = null;
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (Bag.WEAR_DATAMAP_AMAZINGPICTURE.equals(path)) {
                    // Get the data out of the event
                    DataMapItem dataMapItem =
                            DataMapItem.fromDataItem(event.getDataItem());
                    title = dataMapItem.getDataMap().getString(AmazingPicture.FIELD_TITLE);
                    bitmapAsset = dataMapItem.getDataMap().getAsset(AmazingPicture.FIELD_IMAGE);
                }else {
                    mLogFacility.e(LOG_TAG, "Unrecognized path " + path);
                }
            }
        }

        if (TextUtils.isEmpty(title)) {
            return;
        }

        // Prepares the notification to open the new activity
        Intent startIntent = new Intent(this, PictureActivity.class);
        startIntent.putExtra(PictureActivity.INTENT_EXTRA_TITLE, title);
        startIntent.putExtra(PictureActivity.INTENT_EXTRA_IMAGEASSET, bitmapAsset);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // And starts the activity
        // startActivity(startIntent);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                this,
                0,
                startIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.WearableExtender wearableExtender = new Notification.WearableExtender()
                .setDisplayIntent(notificationPendingIntent);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(title)
                .extend(wearableExtender);
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(
                Bag.NOTIFICATION_ID_NEWIMAGE,
                notificationBuilder.build());

        mLogFacility.v(LOG_TAG, "Sent notification for picture " + title);
    }
}
