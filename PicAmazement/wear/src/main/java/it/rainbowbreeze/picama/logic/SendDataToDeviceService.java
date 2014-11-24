package it.rainbowbreeze.picama.logic;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.wearable.activity.ConfirmationActivity;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;

/**
 * Created by alfredomorresi on 22/11/14.
 */
public class SendDataToDeviceService extends GoogleApiClientBaseService {
    private static final String LOG_TAG = SendDataToDeviceService.class.getSimpleName();
    @Inject ILogFacility mLogFacility;

    public SendDataToDeviceService() {
        super(SendDataToDeviceService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApp) getApplicationContext()).inject(this);
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    @Override
    protected Api getApi() {
        return Wearable.API;
    }

    @Override
    protected ILogFacility getLogFacility() {
        return mLogFacility;
    }

    @Override
    public void doYourStuff(Intent intent) {
        // All the valid checks are performed on the super method
        final String action = intent.getAction();
        long pictureId = intent.getLongExtra(Bag.INTENT_EXTRA_PICTUREID, Bag.ID_NOT_SET);
        int notificationId = intent.getIntExtra(Bag.INTENT_EXTRA_NOTIFICATIONID, 0);
        if (Bag.INTENT_ACTION_SAVEPICTURE.equals(action)) {
            sendDataMessageForDevice(Bag.WEAR_PATH_SAVEPICTURE, pictureId, notificationId);

        } else if (Bag.INTENT_ACTION_REMOVEPICTURE.equals(action)) {
            sendDataMessageForDevice(Bag.WEAR_PATH_REMOVEPICTURE, pictureId, notificationId);

        } else {
            mLogFacility.i(LOG_TAG, "Command not recognized: " + action);
        }
    }

    private void sendDataMessageForDevice(String path, long pictureId, int notificationId) {
        mLogFacility.v(LOG_TAG, "Sending to device path " + path + " for picture id " + pictureId);

        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(path);
        dataMapRequest.getDataMap().putLong(Bag.WEAR_DATAMAPITEM_PICTUREID, pictureId);
        dataMapRequest.getDataMap().putLong(Bag.WEAR_DATAMAPITEM_TIMESTAMP, (new Date()).getTime());
        PutDataRequest request = dataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(
                mGoogleApiClient, request);
        DataApi.DataItemResult result = pendingResult.await();

        if (result.getStatus().isSuccess()) {
            mLogFacility.v(LOG_TAG, "Sent data to device");
            // Creates the confirmation wear activity
            Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
            intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
            intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.common_done));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
            // Removes the whole notification
            NotificationManager nm = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
            nm.cancel(notificationId);

        } else {
            mLogFacility.i(LOG_TAG, "Error sending data to device, code is " + result.getStatus().getStatusCode());
        }
    }
}
