package it.rainbowbreeze.picama.data.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import it.rainbowbreeze.picama.BuildConfig;
import it.rainbowbreeze.picama.data.provider.picture.PictureColumns;

public class PicamaSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = PicamaSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "picama.db";
    private static final int DATABASE_VERSION = 1;
    private static PicamaSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final PicamaSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    private static final String SQL_CREATE_TABLE_PICTURE = "CREATE TABLE IF NOT EXISTS "
            + PictureColumns.TABLE_NAME + " ( "
            + PictureColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PictureColumns.URL + " TEXT NOT NULL, "
            + PictureColumns.TITLE + " TEXT NOT NULL, "
            + PictureColumns.DESC + " TEXT NOT NULL, "
            + PictureColumns.AUTHOR + " TEXT, "
            + PictureColumns.SOURCE + " TEXT NOT NULL, "
            + PictureColumns.DATE + " INTEGER NOT NULL, "
            + PictureColumns.VISIBLE + " INTEGER NOT NULL DEFAULT '1', "
            + PictureColumns.SAVEASKED + " INTEGER NOT NULL DEFAULT '0', "
            + PictureColumns.SAVEFINISHED + " INTEGER NOT NULL DEFAULT '0' "
            + " );";

    // @formatter:on

    public static PicamaSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static PicamaSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */

    private static PicamaSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new PicamaSQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    private PicamaSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        mOpenHelperCallbacks = new PicamaSQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static PicamaSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new PicamaSQLiteOpenHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private PicamaSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new PicamaSQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_PICTURE);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
