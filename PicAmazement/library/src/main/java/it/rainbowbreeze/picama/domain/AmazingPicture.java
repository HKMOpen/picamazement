package it.rainbowbreeze.picama.domain;

import java.util.Date;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public class AmazingPicture {
    private String mUrl;
    private String mTitle;
    private Date mDate;

    public static final String FIELD_URL = "Url";
    public static final String FIELD_TITLE = "Title";

    public AmazingPicture() {
    }

    public String getUrl() {
        return mUrl;
    }
    public AmazingPicture setUrl(String newValue) {
        mUrl = newValue;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }
    public AmazingPicture setTitle(String newValue) {
        mTitle = newValue;
        return this;
    }

    public Date getDate() {
        return mDate;
    }
    public AmazingPicture setDate(Date newValue) {
        mDate = newValue;
        return this;
    }
}
