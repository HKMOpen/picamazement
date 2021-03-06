package it.rainbowbreeze.picama.common;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.libs.logic.ClipboardManagerCompat;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.data.AppPrefsManager;
import it.rainbowbreeze.picama.logic.BootCompletedReceiver;
import it.rainbowbreeze.picama.logic.LogicManager;
import it.rainbowbreeze.picama.logic.ManipulatePictureService;
import it.rainbowbreeze.picama.logic.RefreshPicturesService;
import it.rainbowbreeze.picama.logic.StatusChangeNotifier;
import it.rainbowbreeze.picama.logic.UploadPictureService;
import it.rainbowbreeze.picama.logic.scraper.onebigphoto.OneBigPhotoScraper;
import it.rainbowbreeze.picama.logic.scraper.onebigphoto.OneBigPhotoScraperConfig;
import it.rainbowbreeze.picama.logic.storage.CloudStorageManager;
import it.rainbowbreeze.picama.logic.storage.DropboxCloudProvider;
import it.rainbowbreeze.picama.logic.storage.FileDownloaderHelper;
import it.rainbowbreeze.picama.logic.storage.PictureDiskManager;
import it.rainbowbreeze.picama.logic.scraper.PictureScraperManager;
import it.rainbowbreeze.picama.logic.scraper.PictureScraperManagerConfig;
import it.rainbowbreeze.picama.logic.scraper.twitter.TwitterScraper;
import it.rainbowbreeze.picama.logic.scraper.twitter.TwitterScraperConfig;
import it.rainbowbreeze.picama.logic.wearable.SendDataToWearService;
import it.rainbowbreeze.picama.logic.wearable.WearManager;
import it.rainbowbreeze.picama.logic.action.ActionsManager;
import it.rainbowbreeze.picama.logic.wearable.ReceiveDataFromWearService;
import it.rainbowbreeze.picama.ui.AskForConfirmationDialog;
import it.rainbowbreeze.picama.ui.OneBigPhotoSettingsFragment;
import it.rainbowbreeze.picama.ui.PlugSettingsActivity;
import it.rainbowbreeze.picama.ui.DebugSettingsFragment;
import it.rainbowbreeze.picama.ui.DropboxSettingsFragment;
import it.rainbowbreeze.picama.ui.FullscreenPictureActivity;
import it.rainbowbreeze.picama.ui.MainActivity;
import it.rainbowbreeze.picama.ui.PicturesListFragment;
import it.rainbowbreeze.picama.ui.SettingsFragment;
import it.rainbowbreeze.picama.ui.TwitterSettingsFragment;
import it.rainbowbreeze.picama.ui.UserInputDialog;

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
                PicturesListFragment.class,
                FullscreenPictureActivity.class,
                DropboxSettingsFragment.class,
                TwitterSettingsFragment.class,
                OneBigPhotoSettingsFragment.class,
                DebugSettingsFragment.class,
                SettingsFragment.class,
                AskForConfirmationDialog.class,
                UserInputDialog.class,
                PlugSettingsActivity.class,

                SendDataToWearService.class,
                ManipulatePictureService.class,
                UploadPictureService.class,
                ReceiveDataFromWearService.class,
                RefreshPicturesService.class,

                BootCompletedReceiver.class,
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

    @Provides @Singleton ClipboardManagerCompat ClipboardManagerCompat(
            ILogFacility logFacility) {
        return new ClipboardManagerCompat(logFacility, Bag.APP_NAME_LOG);
    }

    /**
     * There is a provide method because the class have to be configure
     * before working. Otherwise a simple Inject the requiring class
     * could have been used.
     * @return
     */
    @Provides @Singleton public PictureScraperManagerConfig providePictureScraperManagerConfig (
            ILogFacility logFacility,
            TwitterScraperConfig twitterScraperConfig,
            OneBigPhotoScraperConfig oneBigPhotoScraperConfig) {
        TwitterScraper twitterScraper = new TwitterScraper(logFacility, twitterScraperConfig);
        OneBigPhotoScraper oneBigPhotoScraper = new OneBigPhotoScraper(logFacility, oneBigPhotoScraperConfig);

        PictureScraperManagerConfig config = new PictureScraperManagerConfig(
                twitterScraper,
                oneBigPhotoScraper
        );

        return config;
    }

    @Provides @Singleton public TwitterScraperConfig provideTwitterScraperConfig(
            @ForApplication Context appContext,
            ILogFacility logFacility
    ) {
        return new TwitterScraperConfig(appContext, logFacility);
    }

    @Provides @Singleton public OneBigPhotoScraperConfig provideOneBigPhotoScraperConfig(
            @ForApplication Context appContext,
            ILogFacility logFacility
    ) {
        return new OneBigPhotoScraperConfig(appContext, logFacility);
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
            AppPrefsManager appPrefsManager,
            StatusChangeNotifier statusChangeNotifier
    ) {
        return new PictureScraperManager(
                logFacility,
                pictureScraperManagerConfig,
                amazingPictureDao,
                appPrefsManager,
                statusChangeNotifier
                );
    }

    /**
     * @return
     */
    @Provides @Singleton
    public ActionsManager provideActionsManager(
            @ForApplication Context appContext,
            ILogFacility logFacility,
            PictureScraperManager pictureScraperManager) {
        return new ActionsManager(appContext, logFacility, pictureScraperManager);
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
            PictureDiskManager pictureDiskManager,
            AmazingPictureDao amazingPictureDao,
            DropboxCloudProvider dropboxCloudProvider) {
        return new CloudStorageManager(
                logFacility,
                pictureDiskManager,
                amazingPictureDao,
                dropboxCloudProvider);
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
