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
     * Saves the picture in the cloud
     *
     * @param pictureId
     */
    public void saveToCloudStorages(long pictureId) {
        // Saves to local storage the picture
        PictureDiskManager.Result result = mPictureDiskManager.savePictureToStorage(pictureId);
        if (result.isNotSuccess()) {
            return;
        }

        File imageFile = result.pictureFile;
        File metadataFile = result.metadataFile;
        //TODO

        // Save the files to cloud
        for (BaseCloudProvider cloudProvider : mCloudProviders) {
            boolean cloudResult;
            cloudResult = cloudProvider.save(imageFile);
            cloudResult = cloudProvider.save(metadataFile);

            // Record saving process progress
            //TODO
        }
    }
}
