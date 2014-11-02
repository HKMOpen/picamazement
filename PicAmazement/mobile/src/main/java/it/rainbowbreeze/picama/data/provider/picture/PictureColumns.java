package it.rainbowbreeze.picama.data.provider.picture;

import android.net.Uri;
import android.provider.BaseColumns;

import it.rainbowbreeze.picama.data.provider.PictureProvider;

/**
 * Columns for the {@code picture} table.
 */
public class PictureColumns implements BaseColumns {
    public static final String TABLE_NAME = "picture";
    public static final Uri CONTENT_URI = Uri.parse(PictureProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    public static final String _ID = new String(BaseColumns._ID);
    public static final String URL = new String("url");
    public static final String TITLE = new String("title");
    public static final String SOURCE = new String("source");
    public static final String DATE = new String("date");

    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;
    
    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            URL,
            TITLE,
            SOURCE,
            DATE
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c == _ID) return true;
            if (c == URL) return true;
            if (c == TITLE) return true;
            if (c == SOURCE) return true;
            if (c == DATE) return true;
        }
        return false;
    }

    public static String getQualifiedColumnName(String columnName) {
        if (columnName == _ID) return TABLE_NAME + "." + columnName + " AS " + _ID;
        if (columnName == URL) return TABLE_NAME + "." + columnName + " AS " + TABLE_NAME + "__" + columnName;
        if (columnName == TITLE) return TABLE_NAME + "." + columnName + " AS " + TABLE_NAME + "__" + columnName;
        if (columnName == SOURCE) return TABLE_NAME + "." + columnName + " AS " + TABLE_NAME + "__" + columnName;
        if (columnName == DATE) return TABLE_NAME + "." + columnName + " AS " + TABLE_NAME + "__" + columnName;
        return null;
    }

    public static String getAlias(String columnName) {
        if (columnName == URL) return TABLE_NAME + "__" + columnName;
        if (columnName == TITLE) return TABLE_NAME + "__" + columnName;
        if (columnName == SOURCE) return TABLE_NAME + "__" + columnName;
        if (columnName == DATE) return TABLE_NAME + "__" + columnName;
        return null;
    }
}
