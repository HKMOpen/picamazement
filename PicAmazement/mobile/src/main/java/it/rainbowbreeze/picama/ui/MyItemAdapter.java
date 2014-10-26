package it.rainbowbreeze.picama.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.data.picture.PictureCursor;

/**
 * Created by alfredomorresi on 26/10/14.
 */
public class MyItemAdapter extends CursorAdapter {
    private final LayoutInflater mInflater;

    public MyItemAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View newView = mInflater.inflate(R.layout.vw_picture_card, null);
        TextView lblTitle = (TextView) newView.findViewById(R.id.item_title);

        ViewHolder holder = new ViewHolder(lblTitle);
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
    }

    /**
     * Holder class
     */
    private static class ViewHolder {
        public final TextView lblTitle;

        private ViewHolder(TextView lblTitle) {
            this.lblTitle = lblTitle;
        }
    }
}
