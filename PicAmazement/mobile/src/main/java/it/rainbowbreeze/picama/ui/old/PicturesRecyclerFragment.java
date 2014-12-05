package it.rainbowbreeze.picama.ui.old;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.domain.BaseAmazingPicture;

/**
 * A placeholder fragment containing a simple view.
 */
public class PicturesRecyclerFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PicturesRecyclerFragment newInstance(int sectionNumber) {
        PicturesRecyclerFragment fragment = new PicturesRecyclerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PicturesRecyclerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_pictures_recycler, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.picture_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //mLayoutManager = new LinearLayoutManager(container.getContext());
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        List<BaseAmazingPicture> pictures = new ArrayList<BaseAmazingPicture>();
//            pictures.add(new AmazingPicture(0, "http://lorempixel.com/600/400/"));
//            pictures.add(new AmazingPicture(0, "http://lorempixel.com/600/250/sports"));
//            pictures.add(new AmazingPicture(0, "http://lorempixel.com/600/200/sports/Dummy-Text"));
//            pictures.add(new AmazingPicture(0, "http://lorempixel.com/600/500/nature"));
//            pictures.add(new AmazingPicture(0, "http://lorempixel.com/500/200/food"));
        mAdapter = new PicturesRecyclerAdapter(pictures);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((PicturesRecyclerActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
