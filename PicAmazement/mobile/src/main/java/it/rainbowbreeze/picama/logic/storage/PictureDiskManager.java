package it.rainbowbreeze.picama.logic.storage;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 29/11/14.
 */
public class PictureDiskManager {
    private static final String LOG_TAG = PictureDiskManager.class.getSimpleName();

    private final Context mAppContext;
    private final ILogFacility mLogFacility;
    private final AmazingPictureDao mAmazingPictureDao;
    private final FileDownloaderHelper mFileDownloaderHelper;

    public PictureDiskManager(Context appContext, ILogFacility logFacility, AmazingPictureDao amazingPictureDao, FileDownloaderHelper fileDownloaderHelper) {
        mAppContext = appContext;
        mLogFacility = logFacility;
        mAmazingPictureDao = amazingPictureDao;
        mFileDownloaderHelper = fileDownloaderHelper;
    }

    public void savePictureToStorage(long pictureId) {
        // Retrieves the picture object
        AmazingPicture picture = mAmazingPictureDao.getById(pictureId);
        if (null == picture) {
            mLogFacility.i(LOG_TAG, "Strange, picture is null for picture id " + pictureId);
            return;
        }

        // Saves the image
        File pictureFile = persistPictureImageToFile(picture);
        // Saves metadata
        File metadataFile = (null != pictureFile)
                ? persistPictureMetadataToFile(picture)
                : null;
    }

    /**
     * Save to disk the image
     *
     * @param picture
     */
    private File persistPictureImageToFile(AmazingPicture picture) {
        File pictureFile = getFinalStorageFile(
                picture.getId(),
                generateFileNameFrom(picture.getId(), picture.getUrl()),
                "picture");
        if (null == pictureFile || pictureFile.exists()) {
            return pictureFile;
        }
        if (mFileDownloaderHelper.saveUrlToFile(pictureFile, picture.getUrl())) {
            mLogFacility.v(LOG_TAG, "Aborting loading of image");
            return null;
        }
        return pictureFile;
    }

    /**
     * Saves to disk picture metadata
     * @param picture
     */
    private File persistPictureMetadataToFile(AmazingPicture picture) {
        File metadataFile = getFinalStorageFile(
                picture.getId(),
                generateMetadataFileName(picture.getId()),
                "metadata");
        if (null == metadataFile || metadataFile.exists()) {
            return metadataFile;
        }

        //TODO
        return metadataFile;
    }

    private File getFinalStorageFile(long pictureId, String baseFileName, String dataContent) {
        if (TextUtils.isEmpty(baseFileName)) {
            mLogFacility.e(LOG_TAG, "Final " + dataContent + " file name is empty for picture id " + pictureId);
            return null;
        }
        mLogFacility.v(LOG_TAG, "Picture " + dataContent + " will be saved on file " + baseFileName);

        // http://mbcdev.com/2013/04/13/using-androids-external-storage-effectively-and-judiciously/
        if (!Environment.MEDIA_MOUNTED
                .equals(Environment.getExternalStorageState())) {
            mLogFacility.e(LOG_TAG, "Unfortunately, external memory is not ready. Aborting");
            return null;
        }

        File cache = mAppContext.getExternalCacheDir();
        if (null == cache){
            return null;
        }
        File finalFileName = new File(cache, baseFileName);
        return finalFileName;
    }

    protected String generateFileNameFrom(long pictureId, String url) {
        if (Bag.ID_NOT_SET == pictureId || TextUtils.isEmpty(url)) {
            return null;
        }
        //Extract file extension from the url
        int pos = url.lastIndexOf(".");
        String extension = pos > -1
                ? url.substring(pos).trim()
                : "";
        return String.format("%07d", pictureId) + extension;
    }

    protected String generateMetadataFileName(long pictureId) {
        if (Bag.ID_NOT_SET == pictureId) {
            return null;
        }
        return String.format("%07d", pictureId);
    }
}
