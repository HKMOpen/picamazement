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
    private Context mAppContext;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PicturesListFragment newInstance(int sectionNumber) {
        PicturesListFragment fragment = new PicturesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PicturesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_pictures_list, container, false);

        ListView lst = (ListView) rootView.findViewById(R.id.list_lstItems);
        Cursor c = mAmazingPictureDao.getLatest(100);
        lst.setAdapter(new PicturesAdapter(mAppContext, c, true));

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mAppContext, FullscreenPictureActivity.class);
                intent.putExtra(Bag.INTENT_EXTRA_PICTUREID, id);
                startActivity(intent);
            }
        });
        lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mActionsManager.hidePicture()
                        .setPictureId(id)
                        .executeAsync();
                return true;
            }
        });

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
