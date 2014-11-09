package it.rainbowbreeze.picama.common;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;

/**
 * Created by alfredomorresi on 31/10/14.
 */
public class Bag extends SharedBag {
    public static final int NOTIFICATION_ID_NEWIMAGE = 100;

    private static WeakReference<Bitmap> mPictureBitmap;
    public static void putPictureBitmap(Bitmap pictureBitmap) {
        if (null != mPictureBitmap) {
            mPictureBitmap.clear();
        }
        mPictureBitmap = new WeakReference<Bitmap>(pictureBitmap);
    }
    public static Bitmap getPictureBitmap() {
        if (null == mPictureBitmap) {
            return null;
        }
        return mPictureBitmap.get();
    }
}
