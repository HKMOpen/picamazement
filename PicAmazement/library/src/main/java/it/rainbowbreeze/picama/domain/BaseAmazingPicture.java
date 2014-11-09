package it.rainbowbreeze.picama.domain;

import java.util.Date;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public abstract class BaseAmazingPicture {
    private String mUrl;
    private String mTitle;
    private String mSource;
    private Date mDate;

    public static final String FIELD_IMAGE = "Image";
    public static final String FIELD_TITLE = "Title";
    public static final String FIELD_SOURCE = "Source";
    public static final String FIELD_TIMESTAMP = "Timestamp"; // Avoids caching

    public BaseAmazingPicture() {
    }

    public String getUrl() {
        return mUrl;
    }
    public BaseAmazingPicture setUrl(String newValue) {
        mUrl = newValue;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }
    public BaseAmazingPicture setTitle(String newValue) {
        mTitle = newValue;
        return this;
    }

    public String getSource() {
        return mSource;
    }
    public BaseAmazingPicture setSource(String newValue) {
        mSource = newValue;
        return this;
    }

    public Date getDate() {
        return mDate;
    }
    public BaseAmazingPicture setDate(Date newValue) {
        mDate = newValue;
        return this;
    }
}