package it.rainbowbreeze.picama.logic.storage;

import android.test.AndroidTestCase;

import javax.inject.Inject;

import dagger.Module;
import dagger.ObjectGraph;
import it.rainbowbreeze.picama.common.AndroidModule;

/**
 * Created by alfredomorresi on 01/12/14.
 */
public class PictureDiskManagerTest extends AndroidTestCase {
    @Inject PictureDiskManager mPictureDiskManager;

    @Module (
            includes = AndroidModule.class,
            overrides = true,
            injects = PictureDiskManagerTest.class
    )
    static class TestModule {
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

    public void testGenerateFileNameFromPictureId() {
        assertNull("Wrong name generated", mPictureDiskManager.generateFileNameFrom(-1, "image.jpg"));
        assertEquals("Wrong name generated", "0000000.jpg", mPictureDiskManager.generateFileNameFrom(0, "image.jpg"));
        assertEquals("Wrong name generated", "0000000.png", mPictureDiskManager.generateFileNameFrom(0, "image.png"));
        assertEquals("Wrong name generated", "0000101.gif", mPictureDiskManager.generateFileNameFrom(101, "strange.image.name.gif"));
        assertEquals("Wrong name generated", "0000101", mPictureDiskManager.generateFileNameFrom(101, "strange_image_name"));
    }
}
