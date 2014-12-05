package it.rainbowbreeze.picama.ui;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.provider.picture.PictureColumns;
import it.rainbowbreeze.picama.data.provider.picture.PictureCursor;
import it.rainbowbreeze.picama.data.provider.picture.PictureSelection;
import it.rainbowbreeze.picama.logic.action.ActionsManager;
import it.rainbowbreeze.picama.ui.util.SystemUiHider;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenPictureActivity2 extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    public static final String LOG_TAG = FullscreenPictureActivity2.class.getSimpleName();
    @Inject ILogFacility mLogFacility;
    @Inject ActionsManager mActionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).inject(this);
        mLogFacility.logStartOfActivity(LOG_TAG, PicturesListActivity.class, savedInstanceState);

        setContentView(R.layout.act_fullscreenpicture);

        final View contentView = findViewById(R.id.fullscreen_imgPicture);

        /**
        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });
        */

        long pictureId = getIntent().getLongExtra(Bag.INTENT_EXTRA_PICTUREID, -1);
        if (-1 == pictureId) {
            mLogFacility.i(LOG_TAG, "Cannot find image to load, aborting");
            finish();
            return;
        }

        mActionsManager.sendPictureToWear()
                .setPictureId(pictureId)
                .executeAsync();
        PictureSelection where = new PictureSelection();
        where.visible(true).and().id(pictureId);
        Cursor c = getApplicationContext().getContentResolver().query(PictureColumns.CONTENT_URI, null,
                where.sel(), where.args(), null);
        PictureCursor pictureCursor = where.query(getContentResolver());
        pictureCursor.moveToNext();
        String url = pictureCursor.getUrl();
        mLogFacility.v(LOG_TAG, "Loading picture at " + url);

        // Retrieves the image URL
        // Uses Picasso built-in methods to measure the size of the containing ImageView and then
        //  resize the image to fit the ImageView. Memory efficient
        // Only one drawback: the image have to be present full-size in the cache, otherwise
        //  the scaled one is scaled or stretched again to fit inside this new ImageView
        ImageView imageView = (ImageView) findViewById(R.id.fullscreen_imgPicture);
        Picasso.with(getApplicationContext())
                .load(url)
                //.placeholder()
                //.error()
                .fit()
                .centerInside()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) findViewById(R.id.fullscreen_imgPicture);
                Toast.makeText(FullscreenPictureActivity2.this, "W: " + imageView.getWidth() + ", H: " + imageView.getHeight(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }
    **/


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
