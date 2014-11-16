package it.rainbowbreeze.picama.data.provider;

import android.test.ProviderTestCase2;

import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.data.provider.PictureProvider;
import it.rainbowbreeze.picama.data.provider.picture.PictureColumns;
import it.rainbowbreeze.picama.data.provider.picture.PictureCursor;
import it.rainbowbreeze.picama.data.provider.picture.PictureSelection;
import it.rainbowbreeze.picama.domain.AmazingPicture;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class PictureProviderTest extends ProviderTestCase2<PictureProvider> {
    public PictureProviderTest() {
        super(PictureProvider.class, PictureProvider.AUTHORITY);
    }

    public void testMockContext() {
        PictureSelection pictureSelection = new PictureSelection();
        PictureCursor c = pictureSelection.query(getMockContext().getContentResolver(),
                null,
                PictureColumns.DATE + " DESC");
        while (c.moveToNext()) {
            fail("Shouldn't have pictures");
            break;
        }
        c.close();
    }
}
