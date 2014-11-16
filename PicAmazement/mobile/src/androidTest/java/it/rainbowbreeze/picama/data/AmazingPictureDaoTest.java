package it.rainbowbreeze.picama.data;

import android.test.InstrumentationTestCase;
import android.test.IsolatedContext;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

import javax.inject.Inject;

import dagger.Module;
import dagger.ObjectGraph;
import it.rainbowbreeze.picama.common.AndroidModule;
import it.rainbowbreeze.picama.data.provider.PictureProvider;

/**
 * Created by alfredomorresi on 15/11/14.
 */
public class AmazingPictureDaoTest extends InstrumentationTestCase {

    @Inject AmazingPictureDao mAmazingPictureDao;

    @Module (
            includes = AndroidModule.class,
            overrides = true,
            injects = AmazingPictureDaoTest.class
    )
    protected class TestModule {
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        IsolatedContext providerContext;
        MockContentResolver resolver;

        //created the mock context for the provider
        resolver = new MockContentResolver();
        final String filenamePrefix = "test.";
        RenamingDelegatingContext targetContextWrapper = new RenamingDelegatingContext(
                new MockContext(), // The context that most methods are delegated to
                getInstrumentation().getTargetContext(), // The context that file methods are delegated to
                filenamePrefix);
        providerContext = new IsolatedContext(resolver, targetContextWrapper);

        PictureProvider pictureProvider = new PictureProvider();
        pictureProvider.attachInfo(providerContext, null);
        assertNotNull(pictureProvider);
        resolver.addProvider(PictureProvider.AUTHORITY, pictureProvider);

        ObjectGraph
                .create(
                        new TestModule(),
                        new AndroidModule(providerContext))
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
