package it.rainbowbreeze.picama.common;

import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.data.AppPrefsManager;
import it.rainbowbreeze.picama.logic.LogicManager;
import it.rainbowbreeze.picama.logic.RefreshPictureReceiver;
import it.rainbowbreeze.picama.logic.RefreshPictureService;
import it.rainbowbreeze.picama.logic.storage.CloudStorageManager;
import it.rainbowbreeze.picama.logic.storage.DropboxCloudProvider;
import it.rainbowbreeze.picama.logic.storage.FileDownloaderHelper;
import it.rainbowbreeze.picama.logic.storage.PictureDiskManager;
import it.rainbowbreeze.picama.logic.PictureScraperManager;
import it.rainbowbreeze.picama.logic.PictureScraperManagerConfig;
import it.rainbowbreeze.picama.logic.wearable.SendDataToWearService;
import it.rainbowbreeze.picama.logic.UpdatePictureFieldsService;
import it.rainbowbreeze.picama.logic.wearable.WearManager;
import it.rainbowbreeze.picama.logic.action.ActionsManager;
import it.rainbowbreeze.picama.logic.wearable.ReceiveDataFromWearService;
import it.rainbowbreeze.picama.ui.DropboxSettingsFragment;
import it.rainbowbreeze.picama.ui.FullscreenPictureActivity;
import it.rainbowbreeze.picama.ui.MainActivity;
import it.rainbowbreeze.picama.ui.PicturesListFragment;
import it.rainbowbreeze.picama.ui.SettingsFragment;
import it.rainbowbreeze.picama.ui.old.PicturesRecyclerActivity;

/**
 * A Dagger module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 *
 * Created by alfredomorresi on 31/10/14.
 */
@Module (
        injects = {
                MyApp.class,
                MainActivity.class,
                PicturesRecyclerActivity.class,
                PicturesListFragment.class,
                FullscreenPictureActivity.class,
                DropboxSettingsFragment.class,
                SettingsFragment.class,

                SendDataToWearService.class,
                UpdatePictureFieldsService.class,
                ReceiveDataFromWearService.class,
                RefreshPictureService.class,

                RefreshPictureReceiver.class,
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
            AmazingPictureDao amazingPictureDao,
            AppPrefsManager appPrefsManager) {
        return new PictureScraperManager(
                logFacility, pictureScraperManagerConfig,
                amazingPictureDao, appPrefsManager);
    }

    /**
     * @return
     */
    @Provides @Singleton
    public ActionsManager provideActionsManager(
            @ForApplication Context appContext,
            ILogFacility logFacility,
            AmazingPictureDao amazingPictureDao,
            PictureScraperManager pictureScraperManager) {
        return new ActionsManager(appContext, logFacility, amazingPictureDao, pictureScraperManager);
    }

    @Provides @Singleton
    public PictureDiskManager providePictureDiskManager(
            @ForApplication Context appContext,
            ILogFacility logFacility,
            AmazingPictureDao amazingPictureDao,
            FileDownloaderHelper fileDownloaderHelper) {
        return new PictureDiskManager(appContext, logFacility, amazingPictureDao, fileDownloaderHelper);
    }

    @Provides @Singleton
    public CloudStorageManager provideCloudStorageManager(
            ILogFacility logFacility,
            PictureDiskManager pictureDiskManager) {
        return new CloudStorageManager(logFacility, pictureDiskManager);
    }

    @Provides @Singleton
    public AppPrefsManager provideAppPrefsManager(
            @ForApplication Context appContext,
            ILogFacility logFacility) {
        return new AppPrefsManager(appContext, logFacility);
    }

    @Provides @Singleton
    public LogicManager provideLogicManager(
            ILogFacility logFacility,
            AppPrefsManager appPrefsManager) {
        return new LogicManager(logFacility, appPrefsManager);
    }

    @Provides @Singleton
    public DropboxCloudProvider provideDropboxCloudProvider(
            ILogFacility logFacility,
            AppPrefsManager appPrefsManager) {
        return new DropboxCloudProvider(logFacility, appPrefsManager);
    }

}
