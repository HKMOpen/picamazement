package it.rainbowbreeze.picama.logic;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendPictureToWearService extends GoogleApiClientBaseService {
    private static final String LOG_TAG = SendPictureToWearService.class.getSimpleName();
    @Inject ILogFacility mLogFacility;
    @Inject WearManager mWearManager;
    @Inject AmazingPictureDao mAmazingPictureDao;


    private static final String ACTION_SENDPICTURE = "it.rainbowbreeze.picama.logic.action.FOO";
    private static final String EXTRA_PARAM_PICTURE_ID = "it.rainbowbreeze.picama.logic.extra.PICTURE_ID";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSendAmazingPicture(Context context, long pictureId) {
        Intent intent = new Intent(context, SendPictureToWearService.class);
        intent.setAction(ACTION_SENDPICTURE);
        intent.putExtra(EXTRA_PARAM_PICTURE_ID, pictureId);
        context.startService(intent);
    }

    public SendPictureToWearService() {
        super("SendPictureToWearService");
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
    public ILogFacility getLogFacility() {
        // Class in not null after the call to {@link #onCreate} method, where Dagger
        // injects what's required
        return mLogFacility;
    }

    @Override
    public void doYourStaff(Intent intent) {
        // All the valid checks are performed on the super method
        final String action = intent.getAction();
        if (ACTION_SENDPICTURE.equals(action)) {
            final long pictureId = intent.getLongExtra(EXTRA_PARAM_PICTURE_ID, 0);
            sendPictureToWear(pictureId);
        }
    }

    private void sendPictureToWear(long pictureId) {
        AmazingPicture picture = mAmazingPictureDao.getPictureById(pictureId);
        if (null == picture) {
            mLogFacility.i(LOG_TAG, "Strange, picture is null for picture id " + pictureId);
            return;
        }

        mLogFacility.v(LOG_TAG, "Sending to Wear picture " + picture.getTitle());

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
        mWearManager.transferAmazingPicture(picture, image);
    }

}
