package it.rainbowbreeze.picama.logic.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 01/12/14.
 */
public class CloudStorageManager {
    private static final String LOG_TAG = CloudStorageManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final PictureDiskManager mPictureDiskManager;
    private final AmazingPictureDao mAmazingPictureDao;
    private final List<BaseCloudProvider> mCloudProviders;

    public CloudStorageManager(
            ILogFacility logFacility,
            PictureDiskManager pictureDiskManager,
            AmazingPictureDao mAmazingPictureDao, BaseCloudProvider... cloudProviders) {
        mLogFacility = logFacility;
        mPictureDiskManager = pictureDiskManager;
        this.mAmazingPictureDao = mAmazingPictureDao;
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
    public void saveToCloudStorages(long pictureId, boolean force) {
        mLogFacility.v(LOG_TAG, "Uploading to cloud picture with id " + pictureId);
        AmazingPicture picture = mAmazingPictureDao.getById(pictureId);

        if (null == picture) {
            mLogFacility.e(LOG_TAG, "Strange, picture is null for id " + pictureId);
            return;
        }
        if (!force && picture.isUploadDone()) {
            mLogFacility.v(LOG_TAG, "Picture has been already uploaded to the cloud");
            return;
        }

        // Saves the picture to local storage
        PictureDiskManager.Result result = mPictureDiskManager.savePictureToStorage(pictureId);
        if (result.isNotSuccess()) {
            mLogFacility.e(LOG_TAG, "Saving to local storage failed, aborting...");
            return;
        }
        File imageFile = result.pictureFile;
        File metadataFile = result.metadataFile;

        // Save the file to cloud providers
        for (BaseCloudProvider cloudProvider : mCloudProviders) {
            if (!cloudProvider.isSavePossible()) {
                mLogFacility.v(LOG_TAG, "Skipping cloud provider " + cloudProvider.getName());
                continue;
            }

            //TODO: Improve the logic for storing upload progress when more than
            //       one cloud provider is involved
            mLogFacility.v(LOG_TAG, "Uploading picture to cloud storage " + cloudProvider.getName());
            boolean cloudResult = false;
            if (force || picture.isUploadOfImageRequired()) {
                cloudResult = cloudProvider.save(imageFile);
                if (cloudResult) {
                    mLogFacility.v(LOG_TAG, "Uploaded image file " + imageFile.getAbsolutePath());
                    picture.setUploadProgress(
                            mAmazingPictureDao.setUploadOfImageDone(pictureId, picture.getUploadProgress()));
                } else {
                    mLogFacility.e(LOG_TAG, "Error uploading image file " + imageFile.getAbsolutePath());
                    break;
                }
            }
            if (force || picture.isUploadOfMetadataRequired()) {
                cloudResult = cloudProvider.save(metadataFile);
                if (cloudResult) {
                    mLogFacility.v(LOG_TAG, "Uploaded metadata file " + metadataFile.getAbsolutePath());
                    picture.setUploadProgress(
                            mAmazingPictureDao.setUploadOfMetadataDone(pictureId, picture.getUploadProgress()));
                } else {
                    mLogFacility.e(LOG_TAG, "Error uploading metadata file " + metadataFile.getAbsolutePath());
                    break;
                }
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
