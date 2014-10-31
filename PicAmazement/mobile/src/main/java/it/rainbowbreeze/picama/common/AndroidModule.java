package it.rainbowbreeze.picama.common;

import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.picama.ui.ListActivity;

/**
 * A Dagger module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 *
 * Created by alfredomorresi on 31/10/14.
 */
@Module (
        library = true
        //complete = false
)
public class AndroidModule {
    private final MyApp mApp;

    public AndroidModule(MyApp app) {
        mApp = app;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton @ForApplication public Context provideApplicationContext () {
        return mApp;
    }

    /**
     * Just a test class that need application context to be initialized
     * @return
     */
    @Provides @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) mApp.getSystemService(Context.LOCATION_SERVICE);
    }
}
