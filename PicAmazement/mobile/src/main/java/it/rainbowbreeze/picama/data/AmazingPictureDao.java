package it.rainbowbreeze.picama.data;

import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.provider.picture.PictureColumns;
import it.rainbowbreeze.picama.data.provider.picture.PictureContentValues;
import it.rainbowbreeze.picama.data.provider.picture.PictureCursor;
import it.rainbowbreeze.picama.data.provider.picture.PictureSelection;
import it.rainbowbreeze.picama.data.provider.picture.PictureSource;
import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 02/11/14.
 */
public class AmazingPictureDao {
    private static final String LOG_TAG = AmazingPictureDao.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final Context mAppContext;

    public AmazingPictureDao(ILogFacility logFacility, Context appContext) {
        mLogFacility = logFacility;
        mAppContext = appContext;
    }

    public void insert(AmazingPicture picture) {
        PictureContentValues values = new PictureContentValues()
                .putTitle(picture.getTitle())
                .putUrl(picture.getUrl())
                .putSource(PictureSource.Twitter)
                .putDate(picture.getDate());
        mAppContext.getContentResolver().insert(PictureColumns.CONTENT_URI, values.values());
    }

    /**
     * Finds if a Picture already exist in the local database, given is unique id
     * (generally the url of the picture)
     *
     * @param uniqueId
     * @return
     */
    public boolean pictureExists(String uniqueId) {
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
     * Get the latest picture available
     *
     * @return
     */
    public AmazingPicture getFirstPicture() {
        PictureSelection pictureSelection = new PictureSelection();
        pictureSelection.visible(true);
        PictureCursor c = pictureSelection.query(mAppContext.getContentResolver(),
                null,
                PictureColumns.DATE + " DESC");
        AmazingPicture picture = null;
        while (c.moveToNext()) {
            picture = new AmazingPicture()
                .setUrl(c.getUrl())
                .setDate(c.getDate())
                .setTitle(c.getTitle());
            break;
        }
        c.close();
        return picture;
    }

}