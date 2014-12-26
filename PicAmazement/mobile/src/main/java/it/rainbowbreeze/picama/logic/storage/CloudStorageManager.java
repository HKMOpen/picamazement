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

    public CloudStorageManager(
            ILogFacility logFacility,
            PictureDiskManager pictureDiskManager,
            BaseCloudProvider... cloudProviders) {
        mLogFacility = logFacility;
        mPictureDiskManager = pictureDiskManager;
        mCloudProviders = new ArrayList<BaseCloudProvider>();
        for (BaseCloudProvider cloudProvider : cloudProviders) {
            mCloudProviders.add(cloudProvider);
        }
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

        // Save the file to cloud providers
        for (BaseCloudProvider cloudProvider : mCloudProviders) {
            boolean cloudResult;
            mLogFacility.v(LOG_TAG, "Uploading picture id " + pictureId + " to cloud storage " + cloudProvider.getName());
            cloudResult = cloudProvider.save(imageFile);
            if (cloudResult) {
                mLogFacility.v(LOG_TAG, "Uploaded image file " + imageFile.getAbsolutePath());
                //TODO: update saving of picture file
            } else {
                mLogFacility.e(LOG_TAG, "Error uploading image file " + imageFile.getAbsolutePath());
                break;
            }
            cloudResult = cloudProvider.save(metadataFile);
            if (cloudResult) {
                mLogFacility.v(LOG_TAG, "Uploaded metadata file " + metadataFile.getAbsolutePath());
                //TODO: update saving of metadata file
            } else {
                mLogFacility.e(LOG_TAG, "Error uploading metadata file " + metadataFile.getAbsolutePath());
                break;
            }
        }
    }

    /**
     * Checks all configured providers, searching if it is possible to save files
     * in at least one of them
     * @return
     */
    public boolean isCloudSavePossible() {
        for (BaseCloudProvider cloudProvider : mCloudProviders) {
            if (cloudProvider.isSavePossible()) return true;
        }
        return false;
    }
}
