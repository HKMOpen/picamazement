package it.rainbowbreeze.picama.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;

/**
 * Created by rainbowbreeze on 17/05/15.
 */
public class InjectableActivity extends AppCompatActivity {
    // Why AppCompactActivity and not ActionBarActivity
    //  http://android-developers.blogspot.in/2015/04/android-support-library-221.html

    @Inject protected ILogFacility mLogFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplicationContext()).inject(this);
    }
}
