package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.logic.action.ActionsManager;

/**
 * http://3.bp.blogspot.com/-YJSE-iQngrw/U3bLHPnB1YI/AAAAAAAABWs/CD03Kp6O-zM/s1600/fragmentlifecycle.png
 *
 */
public class PicturesListFragment extends Fragment {
    private static final String LOG_TAG = PicturesListFragment.class.getSimpleName();
    @Inject ILogFacility mLogFacility;
    @Inject ActionsManager mActionsManager;
    @Inject AmazingPictureDao mAmazingPictureDao;
    private int mQueryType;
    private Context mAppContext;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_QUERY_TYPE = "query_type";
    public static final int QUERY_VISIBLE_NOT_UPLOADED = 1;  // Pictures visible and still not uploaded
    public static final int QUERY_UPLOADED = 2;  // Pictures uploaded to cloud storage

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PicturesListFragment newInstance(int queryType) {
        PicturesListFragment fragment = new PicturesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUERY_TYPE, queryType);
        fragment.setArguments(args);
        return fragment;
    }

    public PicturesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mQueryType = getArguments().getInt(ARG_QUERY_TYPE, QUERY_VISIBLE_NOT_UPLOADED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_pictures_list, container, false);

        ListView lst = (ListView) rootView.findViewById(R.id.list_lstItems);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mAppContext, FullscreenPictureActivity.class);
                intent.putExtra(Bag.INTENT_EXTRA_PICTUREID, id);
                startActivity(intent);
            }
        });
        Cursor c = null;
        if (QUERY_VISIBLE_NOT_UPLOADED == mQueryType) {
            c = mAmazingPictureDao.getLatestVisibleAndNotUploaded(100);
            lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mActionsManager.hidePicture()
                            .setPictureId(id)
                            .executeAsync();
                    return true;
                }
            });
        } else if (QUERY_UPLOADED == mQueryType) {
            c = mAmazingPictureDao.getLatestUploaded(100);
        }
        lst.setAdapter(new PicturesAdapter(mAppContext, c, true));

        //TODO manage cursor closing while onPause etc, or using a loader
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAppContext = activity.getApplicationContext();
        ((MyApp) mAppContext).inject(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pictures_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (QUERY_UPLOADED == mQueryType) {
            MenuItem menuItem = menu.findItem(R.id.piclist_mnuHideHelp);
            menuItem.setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        boolean value = true;
        switch (id) {
            case R.id.piclist_mnuDeleteAll:
                mActionsManager.deleteAllPictures()
                        .executeAsync();
                break;
            case R.id.piclist_mnuRefresh:
                mActionsManager.searchForNewImages()
                        .executeAsync();
                break;
            case R.id.piclist_mnuHideHelp:
                Toast.makeText(mAppContext, "Long click to hide a picture", Toast.LENGTH_SHORT).show();
                break;
            default:
                value = super.onOptionsItemSelected(item);
        }
        return value;
    }

}
