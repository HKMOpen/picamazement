package it.rainbowbreeze.picama.data.provider.picture;

import android.database.Cursor;

import it.rainbowbreeze.picama.data.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code picture} table.
 */
public class PictureCursor extends AbstractCursor {
    public PictureCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code url} value.
     * Can be {@code null}.
     */
    public String getUrl() {
        Integer index = getCachedColumnIndexOrThrow(PictureColumns.URL);
        return getString(index);
    }

    /**
     * Get the {@code title} value.
     * Can be {@code null}.
     */
    public String getTitle() {
        Integer index = getCachedColumnIndexOrThrow(PictureColumns.TITLE);
        return getString(index);
    }
}
