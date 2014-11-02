package it.rainbowbreeze.picama.common;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Provides;

/**
 * A base Dagger module for Android-specific dependencies which require a {@link android.content.Context} or
 * {@link android.app.Application} to create.
 *
 * Created by alfredomorresi on 31/10/14.
 */
public abstract class BaseAndroidModule <MyApp extends Application> {
    private final MyApp mApp;

    public BaseAndroidModule(MyApp app) {
        mApp = app;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton @ForApplication public Context provideApplicationContext () {
        return mApp;
    }
}
