package it.rainbowbreeze.picama.logic;

import android.content.Intent;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

import javax.inject.Inject;

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
        if (Bag.INTENT_ACTION_SAVEPICTURE.equals(action)) {
            mLogFacility.v(LOG_TAG, "Picture id here is " + pictureId);
            sendDataMessageForDevice(Bag.WEAR_PATH_SAVEPICTURE, pictureId);

        } else if (Bag.INTENT_ACTION_REMOVEPICTURE.equals(action)) {
            mLogFacility.v(LOG_TAG, "Picture id here is " + pictureId);
            sendDataMessageForDevice(Bag.WEAR_PATH_REMOVEPICTURE, pictureId);

        } else {
            mLogFacility.i(LOG_TAG, "Command not recognized: " + action);
        }
    }

    private void sendDataMessageForDevice(String path, long pictureId) {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(path);
        dataMapRequest.getDataMap().putLong(Bag.WEAR_DATAMAPITEM_PICTUREID, pictureId);
        dataMapRequest.getDataMap().putLong(Bag.WEAR_DATAMAPITEM_TIMESTAMP, (new Date()).getTime());
        PutDataRequest request = dataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(
                mGoogleApiClient, request);
        pendingResult.await();
        mLogFacility.v(LOG_TAG, "Sent to device path " + path + " for picture id " + pictureId);
    }
}
