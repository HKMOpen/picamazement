package it.rainbowbreeze.picama.domain;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public class AmazingPicture implements it.rainbowbreeze.libs.data.RainbowSettableId {
    private final String mUrl;
    private long mId;

    public AmazingPicture(long id, String url) {
        mId = id;
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;

    }

    @Override
    public void setId(long newValue) {
        mId = newValue;
    }

    public long getId() {
        return mId;
    }

}
