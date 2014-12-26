package it.rainbowbreeze.picama.domain;

import com.google.android.gms.wearable.DataMap;

import org.json.JSONObject;

import it.rainbowbreeze.picama.data.provider.picture.PictureContentValues;
import it.rainbowbreeze.picama.data.provider.picture.PictureCursor;

/**
 * Created by alfredomorresi on 09/11/14.
 */
public class AmazingPicture extends BaseAmazingPicture {
    private boolean mSaveAsked;
    private int mSaveFinished;

    public AmazingPicture() {
        super();
    }

    public boolean getSaveAsked() {
        return mSaveAsked;
    }
    public AmazingPicture setSaveAsked(boolean newValue) {
        mSaveAsked = newValue;
        return this;
    }

    public int getSaveFinished() {
        return mSaveFinished;
    }
    public AmazingPicture setSaveFinished(int newValue) {
        mSaveFinished = newValue;
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
                .setSaveAsked(c.getSaveasked())
                .setSaveFinished(c.getSavefinished())
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
                .putSaveasked(getSaveAsked())
                .putSavefinished(getSaveFinished())
                .putUrl(getUrl())
                .putTitle(getTitle())
                .putDesc(getDesc())
                .putSource(getSource())
                .putAuthor(getAuthor())
                .putDate(getDate());
    }
}
