package it.rainbowbreeze.picama.logic.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.rainbowbreeze.picama.common.ILogFacility;

/**
 * Created by alfredomorresi on 01/12/14.
 */
public class CloudStorageManager {
    private static final String LOG_TAG = CloudStorageManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final PictureDiskManager mPictureDiskManager;
    private final List<BaseCloudProvider> mCloudProviders;

    public CloudStorageManager(ILogFacility logFacility, PictureDiskManager pictureDiskManager) {
        mLogFacility = logFacility;
        mPictureDiskManager = pictureDiskManager;
        mCloudProviders = new ArrayList<BaseCloudProvider>();
    }

    /**
     * Retrieves the list of picture to saves and save them, updating
     * the list of saved picture at the end
     */
    public void savePicturesToStorage() {

    }


    public void saveToStorage(long pictureId) {
        // Saves to local storage the picture
        mPictureDiskManager.savePictureToStorage(pictureId);
        File imageFile = null;
        File metadataFile = null;
        //TODO

        // Save the files to cloud
        for (BaseCloudProvider cloudProvider : mCloudProviders) {
            boolean result;
            result = cloudProvider.save(imageFile);
            result = cloudProvider.save(metadataFile);

            // Record saving process progress
            //TODO
        }
    }
}
