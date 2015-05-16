package it.rainbowbreeze.picama.common;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.picama.logic.StatusChangeNotifier;
import it.rainbowbreeze.picama.logic.storage.FileDownloaderHelper;

/**
 * Dagger modules for classes that don't need an Application context
 * Created by alfredomorresi on 31/10/14.
 */
@Module (
        complete = true,
        library = true
)
public class MobileModule {
    @Provides @Singleton public ILogFacility provideLogFacility () {
        return new LogFacility();
    }

    @Provides @Singleton
    FileDownloaderHelper provideFileDownloaderHelper(ILogFacility logFacility) {
        return new FileDownloaderHelper(logFacility);
    }

    @Provides @Singleton
    public StatusChangeNotifier provideStatusChangeNotifier(ILogFacility logFacility) {
        return new StatusChangeNotifier(logFacility);
    }

}
