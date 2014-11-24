package it.rainbowbreeze.picama.logic.wearable;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.logic.action.ActionsManager;

/**
 * Created by alfredomorresi on 22/11/14.
 */
public class ReceiveDataFromWearService extends WearableListenerService {
    private static final String LOG_TAG = ReceiveDataFromWearService.class.getSimpleName();

    @Inject ILogFacility mLogFacility;
    @Inject ActionsManager mActionsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApp) getApplicationContext()).inject(this);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        mLogFacility.v(LOG_TAG, "Received onDataChanged event for data ");

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
                if (Bag.WEAR_PATH_AMAZINGPICTURE.equals(path)) {
                    continue;
                }

                mLogFacility.v(LOG_TAG, "Event path: " + path);
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                long pictureId = dataMapItem.getDataMap().getLong(Bag.WEAR_DATAMAPITEM_PICTUREID, Bag.ID_NOT_SET);

                if (Bag.WEAR_PATH_SAVEPICTURE.equals(path)) {
                    if (Bag.ID_NOT_SET != pictureId) {
                        mLogFacility.v(LOG_TAG, "Saving picture with id " + pictureId);
                    } else {
                        mLogFacility.i(LOG_TAG, "Cannot save the picture because id is null");
                    }

                } else if (Bag.WEAR_PATH_REMOVEPICTURE.equals(path)) {
                    if (Bag.ID_NOT_SET != pictureId) {
                        mLogFacility.v(LOG_TAG, "Removing picture with id " + pictureId);
                        mActionsManager.hidePicture()
                                .setPictureId(pictureId)
                                .executeAsync();
                    } else {
                        mLogFacility.i(LOG_TAG, "Cannot remove the picture because id is null");
                    }

                } else {
                    mLogFacility.i(LOG_TAG, "Cannot process the path, aborting");
                }
            }
        }
    }
}
