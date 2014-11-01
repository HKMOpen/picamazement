package it.rainbowbreeze.picama.data.picture;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import it.rainbowbreeze.picama.data.base.AbstractSelection;

/**
 * Selection for the {@code picture} table.
 */
public class PictureSelection extends AbstractSelection<PictureSelection> {
    @Override
    public Uri uri() {
        return PictureColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code PictureCursor} object, which is positioned before the first entry, or null.
     */
    public PictureCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new PictureCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public PictureCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public PictureCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public PictureSelection id(long... value) {
        addEquals("picture." + PictureColumns._ID, toObjectArray(value));
        return this;
    }


    public PictureSelection url(String... value) {
        addEquals(PictureColumns.URL, value);
        return this;
    }

    public PictureSelection urlNot(String... value) {
        addNotEquals(PictureColumns.URL, value);
        return this;
    }

    public PictureSelection urlLike(String... value) {
        addLike(PictureColumns.URL, value);
        return this;
    }

    public PictureSelection title(String... value) {
        addEquals(PictureColumns.TITLE, value);
        return this;
    }

    public PictureSelection titleNot(String... value) {
        addNotEquals(PictureColumns.TITLE, value);
        return this;
    }

    public PictureSelection titleLike(String... value) {
        addLike(PictureColumns.TITLE, value);
        return this;
    }
}