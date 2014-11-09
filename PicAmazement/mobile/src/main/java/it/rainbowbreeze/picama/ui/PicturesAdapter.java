package it.rainbowbreeze.picama.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.data.provider.picture.PictureCursor;

/**
 * Created by alfredomorresi on 26/10/14.
 *
 * Picasso consideration
 * - .fit() waits that the ImageView has been measured, then resize the image the the exact
 *    size of the ImageView and put it into the cache. If the first imageView that requires
 *    the image is inside the list item, the cache bitmap will have a small height and width,
 *    so all the following requests of the same image will obtain a small bitmap.
 *    To avoid this, download the image at a particular size (full in case of twitter images,
 *    TBD in case of other images) and use the ImageView scaleType attribute to show the image
 *    at the right size.
 *    Unfortunately, it's not always memory-saving, but it works!
 */
public class PicturesAdapter extends CursorAdapter {
    private final LayoutInflater mInflater;

    public PicturesAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View newView = mInflater.inflate(R.layout.vw_picture_card, null);
        TextView lblTitle = (TextView) newView.findViewById(R.id.picList_lblTitle);
        ImageView imgPhoto = (ImageView) newView.findViewById(R.id.picList_imgPicture);
        ViewHolder holder = new ViewHolder(lblTitle, imgPhoto);
        newView.setTag(holder);

        return newView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        PictureCursor pictureCursor = new PictureCursor(cursor);
        holder.lblTitle.setText(pictureCursor.getTitle());

        // Removed fit and centerCrop for caching reason, as in the comment of this class
        //  (following request of the same image will return the small image required by
        //  the listview
        // TODO: adjust for a smart memory management
        Picasso.with(holder.imgPicture.getContext())
                .load(pictureCursor.getUrl())
                //.fit()
                //.centerCrop()
                .into(holder.imgPicture);
    }

    /**
     * Holder class
     */
    private static class ViewHolder {
        public final TextView lblTitle;
        public final ImageView imgPicture;

        private ViewHolder(TextView lblTitle, ImageView imgPicture) {
            this.lblTitle = lblTitle;
            this.imgPicture = imgPicture;
        }
    }
}
