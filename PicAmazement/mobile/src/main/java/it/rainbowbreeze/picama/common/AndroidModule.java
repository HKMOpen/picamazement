package it.rainbowbreeze.picama.common;

import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.logic.storage.PictureDiskManager;
import it.rainbowbreeze.picama.logic.PictureScraperManager;
import it.rainbowbreeze.picama.logic.PictureScraperManagerConfig;
import it.rainbowbreeze.picama.logic.wearable.SendDataToWearService;
import it.rainbowbreeze.picama.logic.UpdatePictureFieldsService;
import it.rainbowbreeze.picama.logic.wearable.WearManager;
import it.rainbowbreeze.picama.logic.action.ActionsManager;
import it.rainbowbreeze.picama.logic.wearable.ReceiveDataFromWearService;
import it.rainbowbreeze.picama.ui.FullscreenPictureActivity2;
import it.rainbowbreeze.picama.ui.PictureListActivity;

/**
 * A Dagger module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 *
 * Created by alfredomorresi on 31/10/14.
 */
@Module (
        injects = {
                PictureListActivity.class,
                FullscreenPictureActivity2.class,
                SendDataToWearService.class,
                UpdatePictureFieldsService.class,
                ReceiveDataFromWearService.class,
        },
        includes = MobileModule.class,
        // True because it declares @Provides not used inside the class, but outside.
        // Once the code is finished, it should be possible to set to false and have
        //  all the consuming classes in the injects statement
        library = true,
        // Forces validates modules and injections at compile time.
        // If true, includes also additional modules that will complete the dependency graph
        //  using the includes statement for the class included in the injects statement
        complete = true
)
public class AndroidModule {
    private final Context mAppContent;

    public AndroidModule(Context appContent) {
        mAppContent = appContent;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton @ForApplication public Context provideApplicationContext () {
        return mAppContent;
    }

    /**
     * The application context can be read from the local app or passing it a parameters
     * TODO: remove this class
     * @return
     */
    @Provides @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) mAppContent.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * It works because {@link it.rainbowbreeze.picama.common.ILogFacility} is provided
     * by another modules, included by this one
     * @param logFacility
     * @param appContext
     * @return
     */
    @Provides @Singleton WearManager provideWearManager(
            @ForApplication Context appContext,
            ILogFacility logFacility) {
        return new WearManager(appContext, logFacility);
    }

    /**
     * @return
     */
    @Provides @Singleton AmazingPictureDao provideAmazingPictureDao(
            @ForApplication Context appContext,
            ILogFacility logFacility) {
        return new AmazingPictureDao(appContext, logFacility);
    }


    @Provides @Singleton public PictureScraperManager providePictureScrapeManager (
            ILogFacility logFacility,
            PictureScraperManagerConfig pictureScraperManagerConfig,
            AmazingPictureDao amazingPictureDao) {
        return new PictureScraperManager(logFacility, pictureScraperManagerConfig, amazingPictureDao);
    }

    /**
     * @return
     */
    @Provides @Singleton
    ActionsManager provideActionsManager(
            @ForApplication Context appContext,
            ILogFacility logFacility,
            AmazingPictureDao amazingPictureDao,
            PictureScraperManager pictureScraperManager) {
        return new ActionsManager(appContext, logFacility, amazingPictureDao, pictureScraperManager);
    }

    @Provides @Singleton
    PictureDiskManager providePictureDiskManager(
           @ForApplication Context appContext,
           ILogFacility logFacility,
           AmazingPictureDao amazingPictureDao) {
        return new PictureDiskManager(appContext, logFacility, amazingPictureDao);
    }
}
