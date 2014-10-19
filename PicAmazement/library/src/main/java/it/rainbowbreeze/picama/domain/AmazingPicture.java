package it.rainbowbreeze.picama.domain;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public class AmazingPicture {
    private final String mUrl;
    private final long mId;

    public AmazingPicture(long id, String url) {
        mId = id;
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;

    }

    public long getId() {
        return mId;
    }

}
