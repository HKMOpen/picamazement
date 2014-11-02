package it.rainbowbreeze.picama.data;

import android.content.Context;

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

    public AmazingPictureDao(ILogFacility logFacility) {
        mLogFacility = logFacility;
        mLogFacility.v(LOG_TAG, "Test");
    }

    public void insert(Context appContext, AmazingPicture picture) {
        PictureContentValues values = new PictureContentValues()
                .putTitle(picture.getTitle())
                .putUrl(picture.getUrl())
                .putSource(PictureSource.Twitter)
                .putDate(picture.getDate());
        appContext.getContentResolver().insert(PictureColumns.CONTENT_URI, values.values());
    }

    /**
     * Finds if a Picture already exist in the local database, given is unique id
     * (generally the url of the picture)
     *
     * @param uniqueId
     * @return
     */
    public boolean pictureExists(Context appContext, String uniqueId) {
        PictureSelection pictureSelection = new PictureSelection();
        pictureSelection.url(uniqueId);
        String projection[] = {PictureColumns._ID, PictureColumns.URL };
        PictureCursor c = pictureSelection.query(appContext.getContentResolver(), projection);
        boolean found = false;
        while (c.moveToNext()) {
            found = true;
            break;
        }
        c.close();
        return found;
    }
}