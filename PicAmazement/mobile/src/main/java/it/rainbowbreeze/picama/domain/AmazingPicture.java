package it.rainbowbreeze.picama.domain;

import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;

import java.util.Date;

import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.data.provider.picture.PictureCursor;

/**
 * Created by alfredomorresi on 09/11/14.
 */
public class AmazingPicture extends BaseAmazingPicture {
    public AmazingPicture() {
        super();
    }

    public void fillDataMap(DataMap dataMap) {
        dataMap.putLong(FIELD_ID, getId());
        dataMap.putString(FIELD_TITLE, getTitle());
        dataMap.putString(FIELD_SOURCE, getSource());
    }

    public AmazingPicture fromCursor(PictureCursor c) {
        this
                .setId(c.getId())
                .setUrl(c.getUrl())
                .setDate(c.getDate())
                .setTitle(c.getTitle())
                .setSource(c.getSource().toString());
        return this;
    }
}
