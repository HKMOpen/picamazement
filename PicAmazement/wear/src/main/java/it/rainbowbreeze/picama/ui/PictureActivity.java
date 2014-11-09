package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.text.TextUtils;
import android.widget.TextView;

import it.rainbowbreeze.picama.R;

public class PictureActivity extends Activity {

    public static final String INTENT_EXTRA_TITLE = "Title";
    public static final String INTENT_EXTRA_IMAGEASSET = "ImageAsset";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_picture);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                Intent intent = getIntent();
                String title = intent.getStringExtra(INTENT_EXTRA_TITLE);
                mTextView.setText(TextUtils.isEmpty(title) ? "Nothing arrived" : title);
            }
        });
    }
}
