package it.rainbowbreeze.picama.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;

/**
 *
 * AlarmManager and setInexactRepeating(..)
 * http://www.accella.net/scheduling-code-execution-on-android/
 *
 * Created by alfredomorresi on 05/12/14.
 */
public class RefreshPictureReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = RefreshPictureReceiver.class.getSimpleName();

    @Inject ILogFacility mLogFacility;
    @Inject LogicManager mLogicManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        ((MyApp) context).inject(this);

        mLogFacility.v(LOG_TAG, "Scheduling pictures refresh");
        mLogicManager.schedulePicturesRefresh(context);
    }
}
