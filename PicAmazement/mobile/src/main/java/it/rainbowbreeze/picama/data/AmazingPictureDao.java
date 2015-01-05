package it.rainbowbreeze.picama.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.provider.picture.PictureColumns;
import it.rainbowbreeze.picama.data.provider.picture.PictureContentValues;
import it.rainbowbreeze.picama.data.provider.picture.PictureCursor;
import it.rainbowbreeze.picama.data.provider.picture.PictureSelection;
import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 02/11/14.
 */
public class AmazingPictureDao {
    private static final String LOG_TAG = AmazingPictureDao.class.getSimpleName();

    private final Context mAppContext;
    private final ILogFacility mLogFacility;

    public AmazingPictureDao(Context appContext, ILogFacility logFacility) {
        mAppContext = appContext;
        mLogFacility = logFacility;
    }

    /**
     * Inserts an {@link it.rainbowbreeze.picama.domain.AmazingPicture} in the Content Provider
     * @param picture
     */
    public void insert(AmazingPicture picture) {
        PictureContentValues values = new PictureContentValues();
        picture.fillContentValues(values);
        mAppContext.getContentResolver().insert(PictureColumns.CONTENT_URI, values.values());
    }

    /**
     * Remove all pictures from the Content Provider
     */
    public void removeAll() {
        PictureSelection where = new PictureSelection();
        mAppContext.getContentResolver().delete(PictureColumns.CONTENT_URI, where.sel(), where.args());
    }

    /**
     * Returns an {@link it.rainbowbreeze.picama.domain.AmazingPicture} given its id
     *
     * @return
     */
    public AmazingPicture getById(long pictureId) {
        PictureSelection pictureSelection = new PictureSelection();
        pictureSelection.id(pictureId).and().visible(true);
        return queryAndCreatePicture(pictureSelection);
    }

    /**
     * Finds if a picture already exist in the local database, given is unique id
     * (generally the url of the picture)
     *
     * @param uniqueId
     * @return
     */
    public boolean exists(String uniqueId) {
        PictureSelection pictureSelection = new PictureSelection();
        pictureSelection.url(uniqueId);
        String projection[] = {PictureColumns._ID, PictureColumns.URL };
        PictureCursor c = pictureSelection.query(mAppContext.getContentResolver(), projection);
        boolean found = false;
        while (c.moveToNext()) {
            found = true;
            break;
        }
        c.close();
        return found;
    }

    /**
     * Returns the latest {@link it.rainbowbreeze.picama.domain.AmazingPicture} inserted
     *
     * @return
     */
    public AmazingPicture getFirst() {
        PictureSelection pictureSelection = new PictureSelection();
        pictureSelection.visible(true);
        return queryAndCreatePicture(pictureSelection);
    }

    private AmazingPicture queryAndCreatePicture(PictureSelection pictureSelection) {
        PictureCursor c = pictureSelection.query(mAppContext.getContentResolver(),
                null,
                PictureColumns.DATE + " DESC");
        AmazingPicture picture = null;
        while (c.moveToNext()) {
            picture = AmazingPicture.fromCursor(c);
            break;
        }
        c.close();
        return picture;
    }

    /**
     * Get latest pictures, visible and not uploaded to cloud storage,
     * sorted by date, from most recent
     * @param limit max number of pictures to get
     * @return
     */
    public CursorLoader getLatestVisibleAndNotUploaded(int limit) {
        //TODO implement limit
        PictureSelection where = new PictureSelection();
        where.visible(true).and().uploadprogressNot(AmazingPicture.UPLOAD_DONE_ALL);
        CursorLoader cursorLoader = new CursorLoader(
                mAppContext,
                PictureColumns.CONTENT_URI,
                null,
                where.sel(),
                where.args(),
                PictureColumns.DATE + " DESC");
        //Cursor c = mAppContext.getContentResolver().query(PictureColumns.CONTENT_URI, null,
        //        where.sel(), where.args(), PictureColumns.DATE + " DESC");
        return cursorLoader;
    }

    /**
     * Get latest pictures, visible and not uploaded to cloud storage,
     * sorted by date, from most recent
     * @param limit max number of pictures to get
     * @return
     */
    public CursorLoader getLatestUploaded(int limit) {
        //TODO implement limit
        PictureSelection where = new PictureSelection();
        where.visible(true).and().uploadprogress(AmazingPicture.UPLOAD_DONE_ALL);
        CursorLoader cursorLoader = new CursorLoader(
                mAppContext,
                PictureColumns.CONTENT_URI,
                null,
                where.sel(),
                where.args(),
                PictureColumns.DATE + " DESC");
        //Cursor c = mAppContext.getContentResolver().query(PictureColumns.CONTENT_URI, null,
        //        where.sel(), where.args(), PictureColumns.DATE + " DESC");
        return cursorLoader;
    }

    private int updateById(long pictureId, PictureContentValues values) {
        Uri pictureUri = Uri.parse(PictureColumns.CONTENT_URI + "/" + pictureId);
        return mAppContext.getContentResolver().update(
                pictureUri,
                values.values(),
                null,
                null);
    }

    /**
     * Hides a particular picture from the list
     * @param pictureId
     */
    public int hideById(long pictureId) {
        PictureContentValues values = new PictureContentValues();
        values.putVisible(false);
        return updateById(pictureId, values);
    }

    public int setUploadOfImageDone(long pictureId, int prevValue) {
        return setUploadProgress(pictureId, prevValue | AmazingPicture.UPLOAD_DONE_IMAGE);
    }
    public int setUploadOfMetadataDone(long pictureId, int prevValue) {
        return setUploadProgress(pictureId, prevValue | AmazingPicture.UPLOAD_DONE_METADATA);
    }
    private int setUploadProgress(long pictureId, int newValue) {
        PictureContentValues values = new PictureContentValues();
        values.putUploadprogress(newValue);
        updateById(pictureId, values);
        return newValue;
    }
}