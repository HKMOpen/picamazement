package it.rainbowbreeze.picama.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AppPrefsManager;

/**
 *
 * AlarmManager and setInexactRepeating(..)
 * http://www.accella.net/scheduling-code-execution-on-android/
 *
 * Created by alfredomorresi on 05/12/14.
 */
public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = BootCompletedReceiver.class.getSimpleName();

    @Inject ILogFacility mLogFacility;
    @Inject LogicManager mLogicManager;
    @Inject AppPrefsManager mAppPrefsManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        ((MyApp) context).inject(this);

        mLogFacility.v(LOG_TAG, "Scheduling pictures refresh");
        mAppPrefsManager.resetSyncStatus();  // In case a sync has been interrupted
        mLogicManager.schedulePicturesRefresh(context);
    }
}

/*
/libprocessgroup(  746): failed to open /acct/uid_10003/pid_1376/cgroup.procs: No such file or directory
D/ConnectivityService(  746): updateNetworkScore for NetworkAgentInfo [WIFI () - 101] to 56
D/ConnectivityService(  746): rematching NetworkAgentInfo [WIFI () - 101]
D/ConnectivityService(  746): Network NetworkAgentInfo [WIFI () - 101] was already satisfying request 1. No change.
D/ConnectivityService(  746): notifyType AVAILABLE for NetworkAgentInfo [WIFI () - 101]
D/ConnectivityManager.CallbackHandler(  900): CM callback handler got msg 524290
I/ActivityManager(  746): Start proc com.darshancomputing.BatteryBot.BIS for service com.darshancomputing.BatteryIndicator/.BatteryInfoService: pid=5247 uid=10123 gids={50123, 9997} abi=armeabi-v7a
I/ActivityManager(  746): Start proc it.rainbowbreeze.picama for broadcast it.rainbowbreeze.picama/.logic.RefreshPictureReceiver: pid=5264 uid=10234 gids={50234, 9997, 1028, 1015, 3003} abi=armeabi-v7a
I/ActivityManager(  746): Killing 4556:com.nianticproject.scout:remote/u0a191 (adj 15): empty #17
I/art     ( 5264): Late-enabling -Xcheck:jni
W/libprocessgroup(  746): failed to open /acct/uid_10191/pid_4556/cgroup.procs: No such file or directory
W/Settings( 5264): Setting airplane_mode_on has moved from android.provider.Settings.System to android.provider.Settings.Global, returning read-only value.
D/AndroidRuntime( 5264): Shutting down VM
--------- beginning of crash
E/AndroidRuntime( 5264): FATAL EXCEPTION: main
E/AndroidRuntime( 5264): Process: it.rainbowbreeze.picama, PID: 5264
E/AndroidRuntime( 5264): java.lang.RuntimeException: Unable to start receiver it.rainbowbreeze.picama.logic.RefreshPictureReceiver: java.lang.ClassCastException: android.app.ReceiverRestrictedContext cannot be cast to it.rainbowbreeze.picama.common.MyApp
E/AndroidRuntime( 5264): 	at android.app.ActivityThread.handleReceiver(ActivityThread.java:2586)
E/AndroidRuntime( 5264): 	at android.app.ActivityThread.access$1700(ActivityThread.java:144)
E/AndroidRuntime( 5264): 	at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1355)
E/AndroidRuntime( 5264): 	at android.os.Handler.dispatchMessage(Handler.java:102)
E/AndroidRuntime( 5264): 	at android.os.Looper.loop(Looper.java:135)
E/AndroidRuntime( 5264): 	at android.app.ActivityThread.main(ActivityThread.java:5221)
E/AndroidRuntime( 5264): 	at java.lang.reflect.Method.invoke(Native Method)
E/AndroidRuntime( 5264): 	at java.lang.reflect.Method.invoke(Method.java:372)
E/AndroidRuntime( 5264): 	at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:899)
E/AndroidRuntime( 5264): 	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:694)
E/AndroidRuntime( 5264): Caused by: java.lang.ClassCastException: android.app.ReceiverRestrictedContext cannot be cast to it.rainbowbreeze.picama.common.MyApp
E/AndroidRuntime( 5264): 	at it.rainbowbreeze.picama.logic.RefreshPictureReceiver.onReceive(RefreshPictureReceiver.java:30)
E/AndroidRuntime( 5264): 	at android.app.ActivityThread.handleReceiver(ActivityThread.java:2579)
E/AndroidRuntime( 5264): 	... 9 more
I/ActivityManager(  746): Start proc dk.tacit.android.foldersync.full for broadcast dk.tacit.android.foldersync.full/dk.tacit.android.foldersync.receivers.StartupIntentReciever: pid=5304 uid=10178 gids={50178, 9997, 1028, 1015, 3003} abi=armeabi-v7a
I/ActivityManager(  746): Killing 4721:berserker.android.apps.sshdroid/u0a81 (adj 15): empty #17
W/libprocessgroup(  746): failed to open /acct/uid_10081/pid_4721/cgroup.procs: No such file or directory
W/ResourceType( 5304): ResTable_typeSpec entry count inconsistent: given 1, previously 1426
V/GmsNetworkLocationProvi( 1705): onSetRequest: ProviderRequestUnbundled, reportLocation is true and interval is 600000
V/GmsNetworkLocationProvi( 1705): SET-REQUEST

 */