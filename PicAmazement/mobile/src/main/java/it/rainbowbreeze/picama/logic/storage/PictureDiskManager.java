package it.rainbowbreeze.picama.logic.storage;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 29/11/14.
 */
public class PictureDiskManager {
    private static final String LOG_TAG = PictureDiskManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final AmazingPictureDao mAmazingPictureDao;
    private final Context mAppContext;

    public PictureDiskManager(Context appContext, ILogFacility logFacility, AmazingPictureDao amazingPictureDao) {
        mAppContext = appContext;
        mLogFacility = logFacility;
        mAmazingPictureDao = amazingPictureDao;
    }

    public void savePictureToStorage(long pictureId) {

        // Retrieves the picture object
        AmazingPicture picture = mAmazingPictureDao.getById(pictureId);
        if (null == picture) {
            mLogFacility.i(LOG_TAG, "Strange, picture is null for picture id " + pictureId);
            return;
        }

        mLogFacility.v(LOG_TAG, "Loading full image for picture id " + pictureId);

        // Checks if the image already exists on the local storage
        String pictureFileName = generateFileNameFromPictureId(pictureId);

        File pictureFile = new File(pictureFileName);
        if (!pictureFile.exists()) {
            // Launches Picasso to retrieve the image
            Bitmap image = null;
            try {
                image = Picasso.with(mAppContext)
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
        }

    }

    protected String generateFileNameFromPictureId(long pictureId) {
        return null;
    }
}
