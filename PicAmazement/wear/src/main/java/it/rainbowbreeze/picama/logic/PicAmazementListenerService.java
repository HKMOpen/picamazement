package it.rainbowbreeze.picama.logic;

import android.text.TextUtils;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.domain.AmazingPicture;
import it.rainbowbreeze.picama.domain.BaseAmazingPicture;

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
        mLogFacility.v(LOG_TAG, "Received DataChanged event");

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();

        // Reads data from the DataApi and creates the picture to show
        AmazingPicture picture = new AmazingPicture();;
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (Bag.WEAR_DATAMAP_AMAZINGPICTURE.equals(path)) {
                    // Get the data out of the event
                    DataMapItem dataMapItem =
                            DataMapItem.fromDataItem(event.getDataItem());
                    picture
                            .setAssetPicture(dataMapItem.getDataMap().getAsset(BaseAmazingPicture.FIELD_IMAGE))
                            .setTitle(dataMapItem.getDataMap().getString(BaseAmazingPicture.FIELD_TITLE))
                            .setSource(dataMapItem.getDataMap().getString(BaseAmazingPicture.FIELD_SOURCE));
                }else {
                    mLogFacility.e(LOG_TAG, "Unrecognized path " + path);
                }
            }
        }

        if (TextUtils.isEmpty(picture.getTitle())) {
            mLogFacility.i(LOG_TAG, "It seems that the picture transmitted has no title, aborting");
        } else {
            mLogFacility.v(LOG_TAG, "Data retrieved, sending notification");
            mWearManager.sendNewPictureNotificationASync(picture);
        }
    }
}
