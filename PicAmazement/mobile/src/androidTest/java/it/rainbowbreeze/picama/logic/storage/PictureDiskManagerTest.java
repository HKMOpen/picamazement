package it.rainbowbreeze.picama.logic.storage;

import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;

import javax.inject.Inject;

import dagger.Module;
import dagger.ObjectGraph;
import it.rainbowbreeze.picama.common.AndroidModule;
import it.rainbowbreeze.picama.common.MobileModule;

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
        assertNull("Wrong name generated", mPictureDiskManager.generateFileNameFromPictureId(-1));
        assertEquals("Wrong name generated", "0", mPictureDiskManager.generateFileNameFromPictureId(0));
    }
}
