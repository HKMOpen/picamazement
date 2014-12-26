package it.rainbowbreeze.picama.logic.storage;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 29/11/14.
 */
public class PictureDiskManager {
    private static final String LOG_TAG = PictureDiskManager.class.getSimpleName();

    public static class Result extends BaseResult {
        public final File pictureFile;
        public final File metadataFile;

        public static Result Result_Not_Ok = new Result(NOT_SUCCESS, null, null);

        public Result(File pictureFile, File metadataFile) {
            this(SUCCESS, pictureFile, metadataFile);
        }

        private Result(int result, File pictureFile, File metadataFile) {
            super(result);
            this.pictureFile = pictureFile;
            this.metadataFile = metadataFile;
        }
    }

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

    public Result savePictureToStorage(long pictureId) {
        // Retrieves the picture object
        AmazingPicture picture = mAmazingPictureDao.getById(pictureId);
        if (null == picture) {
            mLogFacility.i(LOG_TAG, "Strange, picture is null for picture id " + pictureId);
            return Result.Result_Not_Ok;
        }

        // Saves the image
        mLogFacility.v(LOG_TAG, "Saving to disk picture with id " + pictureId);
        File pictureFile = persistPictureImageToFile(picture);
        // Saves metadata
        File metadataFile = (null != pictureFile)
                ? persistPictureMetadataToFile(picture)
                : null;

        return (null != metadataFile)
                ? new Result(pictureFile, metadataFile)
                : Result.Result_Not_Ok;
    }

    /**
     * Save to disk the image
     *
     * @param picture
     */
    private File persistPictureImageToFile(AmazingPicture picture) {
        File pictureFile = getFinalStorageFile(
                picture.getId(),
                generateFileNameFrom(picture),
                "image");
        if (null == pictureFile || pictureFile.exists()) {
            mLogFacility.v(LOG_TAG, "File already exists on disk or is null");
            return pictureFile;
        }
        if (!mFileDownloaderHelper.saveUrlToFile(pictureFile, picture.getUrl())) {
            mLogFacility.v(LOG_TAG, "Aborting loading of image");
            return null;
        }
        mLogFacility.v(LOG_TAG, "Saved picture image to file on local storage");
        return pictureFile;
    }

    /**
     * Saves to disk picture metadata
     * @param picture
     */
    private File persistPictureMetadataToFile(AmazingPicture picture) {
        File metadataFile = getFinalStorageFile(
                picture.getId(),
                generateMetadataFileName(picture),
                "metadata");
        if (null == metadataFile || metadataFile.exists()) {
            return metadataFile;
        }

        //TODO
        //FileOutputStream fout = new FileOutputStream(metadataFile, true);
        mLogFacility.v(LOG_TAG, "Saved picture metadata to file on local storage");
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
            mLogFacility.v(LOG_TAG, "It seems that cache directory is not available, Aborting");
            return null;
        }
        File finalFileName = new File(cache, baseFileName);
        mLogFacility.v(LOG_TAG, "Complete file path is " + finalFileName.getAbsolutePath());
        return finalFileName;
    }

    /**
     * Generates the name of the image file from the picture object
     * @param picture
     * @return
     */
    protected String generateFileNameFrom(AmazingPicture picture) {
        if (null == picture
                || TextUtils.isEmpty(picture.getUrl())) {
            return null;
        }
        //Extract file extension from the url
        String url = picture.getUrl();
        int pos = url.lastIndexOf(".");
        String extension = pos > -1
                ? url.substring(pos).trim()
                : "";
        return baseGenerateFileName(picture, extension);
    }

    /**
     * Generates the name of the metadata file from the picture object
     * @param picture
     * @return
     */
    protected String generateMetadataFileName(AmazingPicture picture) {
        return baseGenerateFileName(picture, ".txt");
    }

    private String baseGenerateFileName(AmazingPicture picture, String extension) {
        if (null == picture
                || Bag.ID_NOT_SET == picture.getId()
                || TextUtils.isEmpty(picture.getUrl())
                || 0l == picture.getDateLong()) {
            return null;
        }

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(picture.getDateLong());
        // http://developer.android.com/reference/java/text/SimpleDateFormat.html
        //SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss a zzz");
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd'-'kkmm");
        return String.format("%s_%07d%s", ft.format(cal.getTime()), picture.getId(), extension);
    }

    /**
     * Created the json string with the image metadata
     * @param picture
     * @return
     */
    protected String createMetadata(AmazingPicture picture) {
        try {
            JSONObject jsonObject = picture.toJson();
            return null != jsonObject
                    ? jsonObject.toString(4)
                    : null;
        } catch (JSONException e) {
            mLogFacility.e(LOG_TAG, "Error serializing metadata to JSON", e);
            return null;
        }
    }
}
