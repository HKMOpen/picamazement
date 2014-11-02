package it.rainbowbreeze.picama.logic;

import android.content.Context;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.ForApplication;
import it.rainbowbreeze.picama.common.ILogFacility;

/**
 * Created by alfredomorresi on 02/11/14.
 */
public class WearManager {
    /**
     * Dagger example, the class doesn't need a direct declaration on a module.
     * @Inject annotates the constructor, so the fields are injected, otherwise
     *  every field can be decorated with @Inject and there is no need to decorate
     *  the constructor, that can be without parameters
     *
    private final ILogFacility mLogFacility;
    private final Context mAppContect;
     @Inject
     public WearManager(ILogFacility logFacility, @ForApplication Context appContext) {
         mLogFacility = logFacility;
         mAppContect = appContext;
         mLogFacility.v(LOG_TAG, "Starting Wear Manager");
     }
     */

    private static final String LOG_TAG = WearManager.class.getSimpleName();
    private final ILogFacility mLogFacility;
    private final Context mAppContect;

    public WearManager(ILogFacility logFacility, @ForApplication Context appContext) {
        mLogFacility = logFacility;
        mAppContect = appContext;
    }

}
