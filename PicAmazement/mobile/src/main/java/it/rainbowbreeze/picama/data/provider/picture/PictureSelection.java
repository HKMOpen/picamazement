package it.rainbowbreeze.picama.data.provider.picture;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import it.rainbowbreeze.picama.data.provider.base.AbstractSelection;

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

    public PictureSelection desc(String... value) {
        addEquals(PictureColumns.DESC, value);
        return this;
    }

    public PictureSelection descNot(String... value) {
        addNotEquals(PictureColumns.DESC, value);
        return this;
    }

    public PictureSelection descLike(String... value) {
        addLike(PictureColumns.DESC, value);
        return this;
    }

    public PictureSelection author(String... value) {
        addEquals(PictureColumns.AUTHOR, value);
        return this;
    }

    public PictureSelection authorNot(String... value) {
        addNotEquals(PictureColumns.AUTHOR, value);
        return this;
    }

    public PictureSelection authorLike(String... value) {
        addLike(PictureColumns.AUTHOR, value);
        return this;
    }

    public PictureSelection source(String... value) {
        addEquals(PictureColumns.SOURCE, value);
        return this;
    }

    public PictureSelection sourceNot(String... value) {
        addNotEquals(PictureColumns.SOURCE, value);
        return this;
    }

    public PictureSelection sourceLike(String... value) {
        addLike(PictureColumns.SOURCE, value);
        return this;
    }

    public PictureSelection date(Date... value) {
        addEquals(PictureColumns.DATE, value);
        return this;
    }

    public PictureSelection dateNot(Date... value) {
        addNotEquals(PictureColumns.DATE, value);
        return this;
    }

    public PictureSelection date(long... value) {
        addEquals(PictureColumns.DATE, toObjectArray(value));
        return this;
    }

    public PictureSelection dateAfter(Date value) {
        addGreaterThan(PictureColumns.DATE, value);
        return this;
    }

    public PictureSelection dateAfterEq(Date value) {
        addGreaterThanOrEquals(PictureColumns.DATE, value);
        return this;
    }

    public PictureSelection dateBefore(Date value) {
        addLessThan(PictureColumns.DATE, value);
        return this;
    }

    public PictureSelection dateBeforeEq(Date value) {
        addLessThanOrEquals(PictureColumns.DATE, value);
        return this;
    }

    public PictureSelection visible(boolean value) {
        addEquals(PictureColumns.VISIBLE, toObjectArray(value));
        return this;
    }

    public PictureSelection uploadasked(boolean value) {
        addEquals(PictureColumns.UPLOADASKED, toObjectArray(value));
        return this;
    }

    public PictureSelection uploadprogress(int... value) {
        addEquals(PictureColumns.UPLOADPROGRESS, toObjectArray(value));
        return this;
    }

    public PictureSelection uploadprogressNot(int... value) {
        addNotEquals(PictureColumns.UPLOADPROGRESS, toObjectArray(value));
        return this;
    }

    public PictureSelection uploadprogressGt(int value) {
        addGreaterThan(PictureColumns.UPLOADPROGRESS, value);
        return this;
    }

    public PictureSelection uploadprogressGtEq(int value) {
        addGreaterThanOrEquals(PictureColumns.UPLOADPROGRESS, value);
        return this;
    }

    public PictureSelection uploadprogressLt(int value) {
        addLessThan(PictureColumns.UPLOADPROGRESS, value);
        return this;
    }

    public PictureSelection uploadprogressLtEq(int value) {
        addLessThanOrEquals(PictureColumns.UPLOADPROGRESS, value);
        return this;
    }
}
