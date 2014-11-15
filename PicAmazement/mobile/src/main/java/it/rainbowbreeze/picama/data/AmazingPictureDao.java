package it.rainbowbreeze.picama.data;

import android.content.Context;

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

    private final Context mAppContext;
    private final ILogFacility mLogFacility;

    public AmazingPictureDao(Context appContext, ILogFacility logFacility) {
        mAppContext = appContext;
        mLogFacility = logFacility;
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
    public AmazingPicture getPictureById(long pictureId) {
        PictureSelection pictureSelection = new PictureSelection();
        pictureSelection.id(pictureId).and().visible(true);
        return queryAndCreatePicture(pictureSelection);
    }

    /**
     * Get the latest picture available
     *
     * @return
     */
    public AmazingPicture getFirstPicture() {
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
            picture = new AmazingPicture().fromCursor(c);
            break;
        }
        c.close();
        return picture;
    }

}