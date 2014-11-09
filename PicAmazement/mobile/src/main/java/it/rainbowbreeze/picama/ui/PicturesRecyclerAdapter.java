package it.rainbowbreeze.picama.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.domain.BaseAmazingPicture;

/**
 * Created by alfredomorresi on 19/10/14.
 */
public class PicturesRecyclerAdapter extends RecyclerView.Adapter<PicturesRecyclerAdapter.ViewHolder> {
    private List<BaseAmazingPicture> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView txtViewTitle;
        public ImageView imgViewIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtViewTitle = (TextView) itemView.findViewById(R.id.picList_lblTitle);
            imgViewIcon = (ImageView) itemView.findViewById(R.id.picList_imgPicture);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PicturesRecyclerAdapter(List<BaseAmazingPicture> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PicturesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View views = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vw_picture_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(views);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtViewTitle.setText(mDataset.get(position).getUrl());

        //Picasso.with(holder.imgViewIcon.getContext()).cancelRequest(holder.imgViewIcon);
        Picasso.with(holder.imgViewIcon.getContext())
                .load(mDataset.get(position).getUrl())
                .into(holder.imgViewIcon);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}