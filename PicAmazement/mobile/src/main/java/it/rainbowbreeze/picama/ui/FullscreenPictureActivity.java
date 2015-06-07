package it.rainbowbreeze.picama.ui;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.domain.AmazingPicture;
import it.rainbowbreeze.picama.logic.action.ActionsManager;
import it.rainbowbreeze.picama.logic.storage.CloudStorageManager;
import it.rainbowbreeze.picama.ui.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenPictureActivity extends Activity {
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

    public static final String LOG_TAG = FullscreenPictureActivity.class.getSimpleName();
    @Inject ILogFacility mLogFacility;
    @Inject ActionsManager mActionsManager;
    @Inject CloudStorageManager mCloudStorageManager;
    @Inject AmazingPictureDao mAmazingPictureDao;
    private View mLayBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).inject(this);
        mLogFacility.logStartOfActivity(LOG_TAG, this.getClass(), savedInstanceState);

        final long pictureId = getIntent().getLongExtra(Bag.INTENT_EXTRA_PICTUREID, Bag.ID_NOT_SET);
        if (Bag.ID_NOT_SET == pictureId) {
            mLogFacility.i(LOG_TAG, "Wrong picture number to show, aborting");
            finish();
            return;
        }

        setContentView(R.layout.act_fullscreen_picture);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final ImageView contentImageView = (ImageView) findViewById(R.id.fullscreen_imgPicture);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentImageView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
                Toast.makeText(FullscreenPictureActivity.this, "W: " + contentImageView.getWidth() + ", H: " + contentImageView.getHeight(), Toast.LENGTH_SHORT).show();
            }
        });


        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        Button btnUpload = (Button) findViewById(R.id.fullscreen_btnSave);
        btnUpload.setOnTouchListener(mDelayHideTouchListener);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsManager.uploadPicture()
                        .setPictureId(pictureId)
                        .execute();
                finish();
            }
        });
        btnUpload.setEnabled(mCloudStorageManager.isCloudSavePossible());

        Button btnHide = (Button) findViewById(R.id.fullscreen_btnDelete);
        btnHide.setOnTouchListener(mDelayHideTouchListener);
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsManager.hidePicture()
                        .setPictureId(pictureId)
                        .execute();
                finish();
            }
        });

        Button btnSendToWear = (Button) findViewById(R.id.fullscreen_btnSendToWear);
        btnSendToWear.setOnTouchListener(mDelayHideTouchListener);
        btnSendToWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsManager.sendPictureToWear()
                        .setPictureId(pictureId)
                        .execute();
            }
        });
        mLayBackground = findViewById(R.id.fullscreen_layBackground);

        AmazingPicture picture = mAmazingPictureDao.getById(pictureId);
        mLogFacility.v(LOG_TAG, "Loading picture at " + picture.getUrl());

        // Retrieves the image URL
        // Uses Picasso built-in methods to measure the size of the containing ImageView and then
        //  resize the image to fit the ImageView. Memory efficient
        // Only one drawback: the image have to be present full-size in the cache, otherwise
        //  the scaled one is scaled or stretched again to fit inside this new ImageView
        Picasso.with(getApplicationContext())
                .load(picture.getUrl())
                        //.placeholder()
                        //.error()
                .fit()
                .centerInside()
                // http://blog.timbremer.com/using-palette-with-picasso-and-gridview/
                .into(contentImageView, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        final Bitmap bitmap = ((BitmapDrawable) contentImageView.getDrawable()).getBitmap();
                        // New Palette builder pattern
                        //  http://android-developers.blogspot.in/2015/04/android-support-library-221.html
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                if (null != palette) {
                                    //Picasso wants a background color, otherwise it messed up and repeat the image
                                    int color = palette.getLightVibrantColor(0xFFFFFF);
                                    if (0xFFFFFF != color) mLayBackground.setBackgroundColor(palette.getLightVibrantColor(0xFFFFFF));
                                } else {
                                    mLogFacility.v(LOG_TAG, "Cannot generate palette");
                                }
                            }
                        });
                    }
                });

        TextView lblDesc = (TextView) findViewById(R.id.fullscreen_lblDesc);
        lblDesc.setText(picture.getDesc());

        this.setTitle(picture.getSource() + " - " + picture.getTitle());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


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
