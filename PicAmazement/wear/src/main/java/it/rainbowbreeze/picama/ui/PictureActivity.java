package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.wearable.Asset;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.LogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.logic.WearManager;

public class PictureActivity extends Activity {
    private static final String LOG_TAG = PictureActivity.class.getSimpleName();
    public static final String INTENT_EXTRA_TITLE = "Title";
    public static final String INTENT_EXTRA_IMAGEASSET = "ImageAsset";

    @Inject ILogFacility mLogFacility;
    @Inject WearManager mWearManager;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).inject(this);
        mLogFacility.logStartOfActivity(LOG_TAG, getClass(), savedInstanceState);
        mWearManager.init();
        mWearManager.onStartASync();

        setContentView(R.layout.act_fullscreen_picture);
        ImageView imgPicture = (ImageView) findViewById(R.id.fullscreen_imgPicture);
        imgPicture.setImageBitmap(Bag.getPictureBitmap());

        /*
        setContentView(R.layout.act_picture);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Intent intent = getIntent();
                String title = intent.getStringExtra(INTENT_EXTRA_TITLE);
                //Asset pictureAsset = intent.getParcelableExtra(INTENT_EXTRA_IMAGEASSET);
                Bitmap pictureBitmap = Bag.getPictureBitmap();

                mTextView = (TextView) stub.findViewById(R.id.picture_lblPicTitle);
                mTextView.setText(TextUtils.isEmpty(title) ? "Nothing arrived" : title);
                if (null != pictureBitmap) {
                    ViewGroup layout = (ViewGroup) findViewById(R.id.picture_layContainer);
                    BitmapDrawable drawable = new BitmapDrawable(pictureBitmap);
                    layout.setBackground(drawable);
                }
            }
        });
        */
    }

    @Override
    protected void onStop() {
        mWearManager.onStop();
        super.onStop();
    }
}
