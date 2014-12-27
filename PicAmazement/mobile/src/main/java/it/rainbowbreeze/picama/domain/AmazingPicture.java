package it.rainbowbreeze.picama.domain;

import com.google.android.gms.wearable.DataMap;

import org.json.JSONObject;

import it.rainbowbreeze.picama.data.provider.picture.PictureContentValues;
import it.rainbowbreeze.picama.data.provider.picture.PictureCursor;

/**
 * Created by alfredomorresi on 09/11/14.
 */
public class AmazingPicture extends BaseAmazingPicture {
    public static final int UPLOAD_DONE_NONE = 0;
    public static final int UPLOAD_DONE_IMAGE = 1;
    public static final int UPLOAD_DONE_METADATA = 2;
    public static final int UPLOAD_DONE_ALL = UPLOAD_DONE_IMAGE + UPLOAD_DONE_METADATA;

    private boolean mUploadAsked;
    private int mUploadProgress = UPLOAD_DONE_NONE;

    public AmazingPicture() {
        super();
    }

    public boolean getUploadAsked() {
        return mUploadAsked;
    }
    public AmazingPicture setUploadAsked(boolean newValue) {
        mUploadAsked = newValue;
        return this;
    }

    public int getUploadProgress() {
        return mUploadProgress;
    }
    public AmazingPicture setUploadProgress(int newValue) {
        mUploadProgress = newValue;
        return this;
    }



    public void fillDataMap(DataMap dataMap) {
        dataMap.putLong(FIELD_ID, getId());
        dataMap.putString(FIELD_URL, getUrl());
        dataMap.putString(FIELD_TITLE, getTitle());
        dataMap.putString(FIELD_DESC, getDesc());
        dataMap.putString(FIELD_SOURCE, getSource());
        dataMap.putLong(FIELD_DATE, getDateLong());
        dataMap.putString(FIELD_AUTHOR, getAuthor());
    }

    public JSONObject toJson() {
        try {
            JSONObject jsonObject = new JSONObject()
                    .put(FIELD_ID, getId())
                    .put(FIELD_URL, getUrl())
                    .put(FIELD_TITLE, getTitle())
                    .put(FIELD_DESC, getDesc())
                    .put(FIELD_SOURCE, getSource())
                    .put(FIELD_AUTHOR, getAuthor())
                    .put(FIELD_DATE, getDateLong());
            return jsonObject;
        } catch (Exception e) {
            return null;
        }
    }

    public AmazingPicture fromCursor(PictureCursor c) {
        this
                .setUploadAsked(c.getUploadasked())
                .setUploadProgress(c.getUploadprogress())
                .setId(c.getId())
                .setUrl(c.getUrl())
                .setTitle(c.getTitle())
                .setDesc(c.getDesc())
                .setSource(c.getSource())
                .setAuthor(c.getAuthor())
                .setDate(c.getDate());
        return this;
    }

    public void fillContentValues(PictureContentValues values) {
        values
                .putUploadasked(getUploadAsked())
                .putUploadprogress(getUploadProgress())
                .putUrl(getUrl())
                .putTitle(getTitle())
                .putDesc(getDesc())
                .putSource(getSource())
                .putAuthor(getAuthor())
                .putDate(getDate());
    }

    /**
     * Returns true if the picture image need to be saved on cloud storage
     * @return
     */
    public boolean isUploadOfImageRequired() {
        return (mUploadProgress & UPLOAD_DONE_IMAGE) != UPLOAD_DONE_IMAGE;
    }
    /**
     * Returns true if the picture metadata need to be saved on cloud storage
     * @return
     */
    public boolean isUploadOfMetadataRequired() {
        return (mUploadProgress & UPLOAD_DONE_METADATA) != UPLOAD_DONE_METADATA;
    }

    public boolean isUploadDone() {
        return (mUploadProgress & UPLOAD_DONE_ALL) == UPLOAD_DONE_ALL;
    }
}
