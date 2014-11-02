package it.rainbowbreeze.picama.data.provider.picture;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import it.rainbowbreeze.picama.data.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code picture} table.
 */
public class PictureContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return PictureColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, PictureSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public PictureContentValues putUrl(String value) {
        if (value == null) throw new IllegalArgumentException("value for url must not be null");
        mContentValues.put(PictureColumns.URL, value);
        return this;
    }



    public PictureContentValues putTitle(String value) {
        mContentValues.put(PictureColumns.TITLE, value);
        return this;
    }

    public PictureContentValues putTitleNull() {
        mContentValues.putNull(PictureColumns.TITLE);
        return this;
    }


    public PictureContentValues putSource(PictureSource value) {
        if (value == null) throw new IllegalArgumentException("value for source must not be null");
        mContentValues.put(PictureColumns.SOURCE, value.ordinal());
        return this;
    }



    public PictureContentValues putDate(Date value) {
        if (value == null) throw new IllegalArgumentException("value for date must not be null");
        mContentValues.put(PictureColumns.DATE, value.getTime());
        return this;
    }


    public PictureContentValues putDate(long value) {
        mContentValues.put(PictureColumns.DATE, value);
        return this;
    }

}
