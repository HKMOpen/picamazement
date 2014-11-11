package it.rainbowbreeze.picama.domain;

import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataMapItem;

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

    public AmazingPicture fromDataMap(DataMapItem dataMapItem) {
        this
                .setAssetPicture(dataMapItem.getDataMap().getAsset(BaseAmazingPicture.FIELD_IMAGE))
                .setId(dataMapItem.getDataMap().getLong(BaseAmazingPicture.FIELD_ID))
                .setTitle(dataMapItem.getDataMap().getString(BaseAmazingPicture.FIELD_TITLE))
                .setSource(dataMapItem.getDataMap().getString(BaseAmazingPicture.FIELD_SOURCE));
        return this;
    }
}
