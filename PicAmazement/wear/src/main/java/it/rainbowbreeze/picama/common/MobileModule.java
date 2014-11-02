package it.rainbowbreeze.picama.common;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger modules for classes that don't need an Application context
 * Created by alfredomorresi on 31/10/14.
 */
@Module(
        complete = true,
        library = true
)
public class MobileModule {
    @Provides
    @Singleton
    public ILogFacility provideLogFacility() {
        return new LogFacility();
    }
}
