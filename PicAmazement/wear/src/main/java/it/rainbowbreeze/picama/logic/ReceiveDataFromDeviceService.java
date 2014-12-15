package it.rainbowbreeze.picama.logic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

import java.io.InputStream;
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

public class ReceiveDataFromDeviceService extends WearableListenerService {
    private static final String LOG_TAG = ReceiveDataFromDeviceService.class.getSimpleName();

    /**
     * Another way to use Dagger: @Inject the object directly as a field.
     * Default null constructor is called and the field is injected
     */
    @Inject ILogFacility mLogFacility;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApp) getApplication()).inject(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(Bag.WEAR_PATH_OPENPICTURE)) {
            mLogFacility.v(LOG_TAG, "Received simple message");
        }
    }

    /**
     * An important thing to know is than onDataChanged events are received sequentially,
     *  so even if the sender (the device) fires different events at once, the method
     *  is called once per every data event, is executed and, when the execution finishes,
     *  the method is called again with the following data event received, and so on.
     * For this reason, is possible to use a connect and disconnect calls of GoogleApiClient
     *  without the fear of concurrent access to the same GoogleApiClient object.
     *
     * @param dataEvents
     */
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        mLogFacility.v(LOG_TAG, "Received DataChanged event");

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();

        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();

                /**
                 * From the doc:
                 * onDataChanged() - Called when data item objects are created,
                 * changed, or deleted. An event on one side of a connection
                 * triggers this callback on both sides.
                 *
                 * So, ignoring the events triggered by this device.
                 */
                if (Bag.WEAR_PATH_REMOVEPICTURE.equals(path) || Bag.WEAR_PATH_SAVEPICTURE.equals(path) || Bag.WEAR_PATH_OPENPICTURE.equals(path)) {
                    continue;
                }

                if (Bag.WEAR_PATH_AMAZINGPICTURE.equals(path)) {
                    sendNewPictureNotification(event);

                }else {
                    mLogFacility.e(LOG_TAG, "Unrecognized path " + path);
                }
            }
        }

    }

    private void sendNewPictureNotification(DataEvent event) {
        // Get the data out of the event
        DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
        // Reads data from the DataApi and creates the picture to show
        AmazingPicture picture = new AmazingPicture();
        picture.fromDataMap(dataMapItem);

        if (null == picture || Bag.ID_NOT_SET == picture.getId()) {
            mLogFacility.i(LOG_TAG, "It seems that the picture transmitted has no id, aborting");
            return;
        }

        mLogFacility.v(LOG_TAG, "Data retrieved for picture with id " + picture.getId() + ", sending notification");
        Context appContext = getApplicationContext();

        // Read the image from the asset
        Bitmap pictureBitmap = loadBitmapFromAsset(picture.getAssetPicture());
        if (null == pictureBitmap) {
            mLogFacility.i(LOG_TAG, "Bitmap is null, something wrong has happened, notification aborted");
            return;
        }

        // And finally creates the notification
        Bag.putPictureBitmap(pictureBitmap);

        // Prepares the notification to open the new activity
        Intent fullscreenIntent = new Intent(appContext, PictureActivity.class);
        fullscreenIntent.putExtra(PictureActivity.INTENT_EXTRA_TITLE, picture.getDesc());
        fullscreenIntent.putExtra(PictureActivity.INTENT_EXTRA_IMAGEASSET, picture.getAssetPicture());
        fullscreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(
                appContext,
                0,
                fullscreenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // And starts the activity
        //appContext.startActivity(startIntent);

        // Page to display information about the picture
        Notification infoPage = new Notification.Builder(appContext)
                .setContentTitle(picture.getTitle())
                .setContentText(picture.getDesc())
                .build();

        // Action to remove the picture from the stream
        Intent removePicIntent = new Intent(Bag.INTENT_ACTION_REMOVEPICTURE);
        removePicIntent.putExtra(Bag.INTENT_EXTRA_PICTUREID, picture.getId());
        removePicIntent.putExtra(Bag.INTENT_EXTRA_NOTIFICATIONID, Bag.NOTIFICATION_ID_NEWIMAGE);
        // The flag FLAG_UPDATE_CURRENT avoids weird pictureId and other extras values
        // (read the PendingIntent notification for that).
        // If two or more PendingIntent need to be active at the same time, add
        // some extra value to them (again, read docs)
        PendingIntent removePicPendingIntent = PendingIntent.getService(
                appContext,
                0,
                removePicIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action removePicAction = new Notification.Action(
                R.drawable.ic_action_delete,
                appContext.getString(R.string.common_removePicture),
                removePicPendingIntent);

        // Action to save the picture of the stream
        Intent savePicIntent = new Intent(Bag.INTENT_ACTION_SAVEPICTURE);
        savePicIntent.putExtra(Bag.INTENT_EXTRA_PICTUREID, picture.getId());
        savePicIntent.putExtra(Bag.INTENT_EXTRA_NOTIFICATIONID, Bag.NOTIFICATION_ID_NEWIMAGE);
        PendingIntent savePicPendingIntent = PendingIntent.getService(
                appContext,
                0,
                savePicIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action savePicAction = new Notification.Action(
                R.drawable.ic_action_save,
                appContext.getString(R.string.common_savePicture),
                savePicPendingIntent);

        // Action to open the picture on the phone
        Intent openPicIntent = new Intent(Bag.INTENT_ACTION_OPENONDEVICE);
        openPicIntent.putExtra(Bag.INTENT_EXTRA_PICTUREID, picture.getId());
        openPicIntent.putExtra(Bag.INTENT_EXTRA_NOTIFICATIONID, Bag.NOTIFICATION_ID_NEWIMAGE);
        PendingIntent openPicPendingIntent = PendingIntent.getService(
                appContext,
                0,
                openPicIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action openPicAction = new Notification.Action(
                android.support.wearable.R.drawable.go_to_phone_00156,
                appContext.getString(R.string.common_open_on_phone),
                openPicPendingIntent);

        Notification.WearableExtender wearableExtender = new Notification.WearableExtender()
                .setDisplayIntent(fullScreenPendingIntent)
                .setCustomSizePreset(Notification.WearableExtender.SIZE_FULL_SCREEN)
                        //.setGravity(Gravity.CENTER)
                        //.setHintShowBackgroundOnly(true) // Boh?
                .setBackground(pictureBitmap)
                .addPage(infoPage);
        Notification.Builder notificationBuilder = new Notification.Builder(appContext)
                //.setOngoing(true)  // Notification cannot be swiped right
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(picture.getSource())
                .addAction(removePicAction)
                .addAction(savePicAction)
                .addAction(openPicAction)
                .extend(wearableExtender);
        NotificationManager nm = ((NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE));
        nm.notify(
                Bag.NOTIFICATION_ID_NEWIMAGE,
                notificationBuilder.build());

        mLogFacility.v(LOG_TAG, "Sent notification for picture " + picture.getDesc());

    }

    private Bitmap loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }

        // Connects to Wearable APIs
        ConnectionResult result = mGoogleApiClient.blockingConnect(
                Bag.GOOGLE_API_CLIENT_TIMEOUT,
                TimeUnit.SECONDS);
        if (!result.isSuccess()) {
            if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
                mLogFacility.i(LOG_TAG, "Connection to Google Api successful, but API not supported");
            } else {
                mLogFacility.i(LOG_TAG, "Connection to Google Api client failed with error " +
                        result.getErrorCode());
            }
            return null;
        }

        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                mGoogleApiClient, asset).await().getInputStream();
        Bitmap bitmap = null;
        if (assetInputStream == null) {
            mLogFacility.i(LOG_TAG, "Requested an unknown Asset");
        } else {
            // decode the stream into a bitmap
            bitmap = BitmapFactory.decodeStream(assetInputStream);
        }
        mGoogleApiClient.disconnect();

        return bitmap;
    }
}
