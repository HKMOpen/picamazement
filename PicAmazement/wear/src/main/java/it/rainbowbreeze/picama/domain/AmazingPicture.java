package it.rainbowbreeze.picama.domain;

import com.google.android.gms.wearable.Asset;

/**
 * Created by alfredomorresi on 09/11/14.
 */
public class AmazingPicture extends BaseAmazingPicture {
    private Asset mAssetPicture;

    public Asset getAssetPicture() {
        return mAssetPicture;
    }
    public BaseAmazingPicture setAssetPicture(Asset newValue) {
        mAssetPicture = newValue;
        return this;
    }
}
