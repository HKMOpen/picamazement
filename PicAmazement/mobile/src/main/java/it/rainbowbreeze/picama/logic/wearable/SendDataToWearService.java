package it.rainbowbreeze.picama.logic.wearable;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.domain.AmazingPicture;
import it.rainbowbreeze.picama.logic.GoogleApiClientBaseService;
import it.rainbowbreeze.picama.logic.UpdatePictureFieldsService;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendDataToWearService extends GoogleApiClientBaseService {
    private static final String LOG_TAG = SendDataToWearService.class.getSimpleName();
    @Inject ILogFacility mLogFacility;
    @Inject AmazingPictureDao mAmazingPictureDao;

    public static final String ACTION_SENDPICTURE = "Action.Wear.SendPicture";
    public static final String EXTRA_PICTUREID = "Param.PictureId";

    public SendDataToWearService() {
        super(UpdatePictureFieldsService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApp) getApplication()).inject(this);
    }

    @Override
    public Api getApi() {
        return Wearable.API;
    }

    @Override
    public String getLogTag() {
        return LOG_TAG;
    }

    @Override
    public ILogFacility getLogFacility() {
        // Class in not null after the call to {@link #onCreate} method, where Dagger
        // injects what's required
        return mLogFacility;
    }

    @Override
    public void doYourStuff(Intent intent) {
        // All the valid checks are performed on the super method
        final String action = intent.getAction();
        if (ACTION_SENDPICTURE.equals(action)) {
            final long pictureId = intent.getLongExtra(EXTRA_PICTUREID, 0);
            sendPictureToWear(pictureId);
        }
    }

    private void sendPictureToWear(long pictureId) {
        AmazingPicture picture = mAmazingPictureDao.getById(pictureId);
        if (null == picture) {
            mLogFacility.i(LOG_TAG, "Strange, picture is null for picture id " + pictureId);
            return;
        }

        mLogFacility.v(LOG_TAG, "Sending to Wear picture id " + pictureId + " and title " + picture.getTitle());

        // Launches Picasso to retrieve the image
        Bitmap image = null;
        try {
            image = Picasso.with(getApplicationContext())
                    .load(picture.getUrl())
                    .resize(640, 400) // Allows parallax scrolling
                    .centerInside() //maintain aspect ratio
                    .get();
            mLogFacility.v(LOG_TAG, "Image for Wear size: " + image.getWidth() + "x" + image.getHeight());
        } catch (IOException e) {
            mLogFacility.e(LOG_TAG, e);
        }

        if (null == image) {
            mLogFacility.v(LOG_TAG, "No image to transfer to Wear, aborting");
            return;
        }

        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(Bag.WEAR_PATH_AMAZINGPICTURE);
        picture.fillDataMap(dataMapRequest.getDataMap());
        dataMapRequest.getDataMap().putLong(Bag.WEAR_DATAMAPITEM_TIMESTAMP, (new Date()).getTime());
        dataMapRequest.getDataMap().putAsset(AmazingPicture.FIELD_IMAGE, createAssetFromBitmap(image));
        PutDataRequest request = dataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(
                mGoogleApiClient, request);
        pendingResult.await();
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
