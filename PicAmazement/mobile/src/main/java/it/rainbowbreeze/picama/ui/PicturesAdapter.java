package it.rainbowbreeze.picama.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
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
        Log.d("ALFREDO", "Creating view");

        return newView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        PictureCursor pictureCursor = new PictureCursor(cursor);
        holder.lblTitle.setText(pictureCursor.getTitle());
        Log.d("ALFREDO", "Binding view");

        Picasso.with(holder.imgPicture.getContext())
                .load(pictureCursor.getUrl())
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
