package it.rainbowbreeze.picama.domain;

import java.util.Date;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public abstract class BaseAmazingPicture {
    private long mId;
    private String mUrl;
    private String mTitle;
    private String mDesc;
    private String mSource;
    private String mAuthor;
    private Date mDate;

    public static final String FIELD_ID = "Id";
    public static final String FIELD_URL = "Url";
    public static final String FIELD_IMAGE = "Image";
    public static final String FIELD_TITLE = "Title";
    public static final String FIELD_DESC = "Desc";
    public static final String FIELD_SOURCE = "Source";
    public static final String FIELD_DATE = "Date";
    public static final String FIELD_AUTHOR = "Author";

    public BaseAmazingPicture() {
    }

    public long getId() {
        return mId;
    }
    public BaseAmazingPicture setId(long newValue) {
        mId = newValue;
        return this;
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

    public String getDesc() {
        return mDesc;
    }
    public BaseAmazingPicture setDesc(String newValue) {
        mDesc = newValue;
        return this;
    }

    public String getAuthor() {
        return mAuthor;
    }
    public BaseAmazingPicture setAuthor(String newValue) {
        mAuthor = newValue;
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
    public long getDateLong() {
        return null != mDate
                ? mDate.getTime()
                : 0;
    }
    public BaseAmazingPicture setDate(Date newValue) {
        mDate = newValue;
        return this;
    }
}
