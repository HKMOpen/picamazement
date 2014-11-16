package it.rainbowbreeze.picama.data;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.ProviderTestCase2;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;
import it.rainbowbreeze.picama.common.AndroidModule;
import it.rainbowbreeze.picama.common.ForApplication;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.provider.PictureProvider;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class AmazingPictureDaoTest extends ProviderTestCase2<PictureProvider> {

    @Inject AmazingPictureDao mAmazingPictureDao;

    @Module (
            includes = AndroidModule.class,
            overrides = true,
            injects = AmazingPictureDaoTest.class
    )
    protected class TestModule {
    }
    /**
     * Constructor.
     */
    public AmazingPictureDaoTest() {
        super(PictureProvider.class, PictureProvider.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ObjectGraph
                .create(
                        new TestModule(),
                        new AndroidModule(getContext().getApplicationContext()))
                .inject(this);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEmpty () {
        assertNull(mAmazingPictureDao.getFirst());
    }
}
