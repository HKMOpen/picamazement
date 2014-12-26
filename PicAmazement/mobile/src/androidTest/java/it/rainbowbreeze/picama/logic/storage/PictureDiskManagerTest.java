package it.rainbowbreeze.picama.logic.storage;

import android.content.Context;
import android.test.AndroidTestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import it.rainbowbreeze.picama.common.AndroidModule;
import it.rainbowbreeze.picama.common.ForApplication;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.domain.AmazingPicture;
import it.rainbowbreeze.picama.mock.MockAmazingPictureDao;

/**
 * Created by alfredomorresi on 01/12/14.
 */
public class PictureDiskManagerTest extends AndroidTestCase {
    @Inject PictureDiskManager mPictureDiskManager;
    MockAmazingPictureDao mAmazingPictureDao;

    @Module (
            includes = AndroidModule.class,
            overrides = true,
            injects = PictureDiskManagerTest.class
    )
    class TestModule {
        /**
         * @return
         */
        @Provides @Singleton
        AmazingPictureDao provideAmazingPictureDao(
                @ForApplication Context appContext,
                ILogFacility logFacility) {
            if (mAmazingPictureDao == null) {
                mAmazingPictureDao = new MockAmazingPictureDao(appContext, logFacility);
            }
            return mAmazingPictureDao;
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ObjectGraph.create(TestModule.class, new AndroidModule(getContext())).inject(this);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGenerateFileNameFrom() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(2000, Calendar.FEBRUARY, 20, 12, 23, 34);
        AmazingPicture picture = new AmazingPicture();
        assertNull("Wrong name generated", mPictureDiskManager.generateFileNameFrom(picture));
        assertNull("Wrong name generated", mPictureDiskManager.generateMetadataFileName(picture));
        picture.setId(100);
        assertNull("Wrong name generated", mPictureDiskManager.generateFileNameFrom(picture));
        assertNull("Wrong name generated", mPictureDiskManager.generateMetadataFileName(picture));
        picture.setUrl("http://www.test.com/image.jpg");
        assertNull("Wrong name generated", mPictureDiskManager.generateFileNameFrom(picture));
        assertNull("Wrong name generated", mPictureDiskManager.generateMetadataFileName(picture));
        picture.setDate(cal.getTime());
        assertEquals("Wrong name generated", "20000220-1223_0000100.jpg", mPictureDiskManager.generateFileNameFrom(picture));
        assertEquals("Wrong name generated", "20000220-1223_0000100.txt", mPictureDiskManager.generateMetadataFileName(picture));
        picture
                .setDesc("Test picture 1 desc")
                .setTitle("Test picture title")
                .setSource("Twitter");
        assertEquals("Wrong name generated", "20000220-1223_0000100.jpg", mPictureDiskManager.generateFileNameFrom(picture));
        assertEquals("Wrong name generated", "20000220-1223_0000100.txt", mPictureDiskManager.generateMetadataFileName(picture));
        picture.setUrl("http://www.test.com/image.png");
        assertEquals("Wrong name generated", "20000220-1223_0000100.png", mPictureDiskManager.generateFileNameFrom(picture));
        assertEquals("Wrong name generated", "20000220-1223_0000100.txt", mPictureDiskManager.generateMetadataFileName(picture));
    }

    public void testCreateMetadata() {
        AmazingPicture picture = getPicture1();
        String json =
                "{\n" +
                "    \"Id\": 100,\n" +
                "    \"Url\": \"http:\\/\\/www.test.com\\/image.jpg\",\n" +
                "    \"Title\": \"Test picture title\",\n" +
                "    \"Desc\": \"Test picture 1 desc\",\n" +
                "    \"Source\": \"Twitter\",\n" +
                "    \"Author\": \"Test_Author\",\n" +
                "    \"Date\": 951045814132\n" +
                "}";
        assertEquals("Wrong json generated", json, mPictureDiskManager.createMetadata(picture));
    }

    public void testSavePictureToStorage() {
        AmazingPicture picture = getPicture1();
        mAmazingPictureDao.setPicture(picture);

        mPictureDiskManager.savePictureToStorage(picture.getId());
    }

    /** Helper **/
    private AmazingPicture getPicture1() {
        AmazingPicture picture = new AmazingPicture();
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(2000, Calendar.FEBRUARY, 20, 12, 23, 34);
        cal.set(Calendar.MILLISECOND, 132);
        picture
                .setId(100)
                .setDate(cal.getTime())
                .setDesc("Test picture 1 desc")
                .setTitle("Test picture title")
                .setSource("Twitter")
                .setAuthor("Test_Author")
                .setUrl("http://www.test.com/image.jpg");
        return picture;
    }


}
