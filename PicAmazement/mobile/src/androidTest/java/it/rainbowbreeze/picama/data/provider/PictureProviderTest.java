package it.rainbowbreeze.picama.data.provider;

import android.test.ProviderTestCase2;
import android.util.Log;

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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testMockContext() {
        PictureSelection pictureSelection = new PictureSelection();
        PictureCursor c = pictureSelection.query(getMockContentResolver(),
                null,
                PictureColumns.DATE + " DESC");
        int pictures = 0;
        while (c.moveToNext()) {
            pictures++;
            AmazingPicture picture = new AmazingPicture().fromCursor(c);
            Log.v("PicAmazement", picture.getId() + " - " + picture.getDesc());
        }
        c.close();
        assertEquals("Shouldn't have pictures", 0, pictures);
    }
}
