package it.rainbowbreeze.picama.common;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.picama.ui.ListActivity;

/**
 * Dagger modules for classes that don't need an Application context
 * Created by alfredomorresi on 31/10/14.
 */
@Module (
        injects = ListActivity.class,
        //complete = false,
        library = true
)
public class MobileModule {
    @Provides @Singleton public IRainbowLogFacility provideLogFacility () {
        return new LogFacility();
    }
}
