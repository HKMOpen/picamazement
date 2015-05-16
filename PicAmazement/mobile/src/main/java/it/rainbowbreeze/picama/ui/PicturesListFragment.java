package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.UUID;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.Bag;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AmazingPictureDao;
import it.rainbowbreeze.picama.logic.StatusChangeNotifier;
import it.rainbowbreeze.picama.logic.action.ActionsManager;

/**
 * http://3.bp.blogspot.com/-YJSE-iQngrw/U3bLHPnB1YI/AAAAAAAABWs/CD03Kp6O-zM/s1600/fragmentlifecycle.png
 *
 */
public class PicturesListFragment
        extends InjectableFragment
        implements
                LoaderManager.LoaderCallbacks<Cursor>,
                StatusChangeNotifier.StatusChangerListener

{
    private static final String LOG_TAG = PicturesListFragment.class.getSimpleName();
    private static final int REQUEST_HIDE_ALL_VISIBLE_NOT_UPLOAD_PICTURES = 100;
    @Inject ActionsManager mActionsManager;
    @Inject AmazingPictureDao mAmazingPictureDao;
    @Inject StatusChangeNotifier mStatusChangeNotifier;
    private final String mStatusListenerId;

    private static final String ARG_QUERY_TYPE = "query_type";
    public static final int QUERY_VISIBLE_NOT_UPLOADED = 1;  // Pictures visible and still not uploaded
    public static final int QUERY_UPLOADED = 2;  // Pictures uploaded to cloud storage
    private PicturesAdapter mPicturesAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mQueryListType;

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
        // If I use a fix Listener id, the last fragment of the same type that
        //  registers to the notifier overriders all the others, so I need to
        //  use a listener id that is unique for each instance of the class
        mStatusListenerId = UUID.randomUUID().toString();
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

        ListView lst = (ListView) rootView.findViewById(R.id.piclist_lstItems);
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mAppContext, FullscreenPictureActivity.class);
                intent.putExtra(Bag.INTENT_EXTRA_PICTUREID, id);
                startActivity(intent);
            }
        });
        mPicturesAdapter = new PicturesAdapter(mAppContext, null, true);
        lst.setAdapter(mPicturesAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.piclist_laySwipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                launchPicturesRefresh();
            }
        });
        mQueryListType = getArguments().getInt(ARG_QUERY_TYPE, QUERY_VISIBLE_NOT_UPLOADED);
        mLogFacility.v(LOG_TAG, "Query type: " + mQueryListType);
        getLoaderManager().initLoader(mQueryListType, null, this);

        if (QUERY_VISIBLE_NOT_UPLOADED == mQueryListType) {
            lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mActionsManager.hidePicture()
                            .setPictureId(id)
                            .executeAsync();
                    return true;
                }
            });
        }
        setSwipeLayoutState();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pictures_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (QUERY_UPLOADED == getArguments().getInt(ARG_QUERY_TYPE, QUERY_VISIBLE_NOT_UPLOADED)) {
            MenuItem menuItem = menu.findItem(R.id.piclist_mnuHideHelp);
            menuItem.setVisible(false);
            menuItem = menu.findItem(R.id.piclist_mnuHideAllVisibleNotUploaded);
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
        DialogFragment newFragment;
        switch (id) {
            case R.id.piclist_mnuRefresh:
                launchPicturesRefresh();
                break;

            case R.id.piclist_mnuHideHelp:
                Toast.makeText(mAppContext, "Long click to hide a picture", Toast.LENGTH_SHORT).show();
                break;

            case R.id.piclist_mnuHideAllVisibleNotUploaded:
                newFragment = AskForConfirmationDialog.newInstance();
                newFragment.setTargetFragment(this, REQUEST_HIDE_ALL_VISIBLE_NOT_UPLOAD_PICTURES);
                newFragment.show(getFragmentManager(), "HideAllVisibleNotUploadedPictures");
                break;
            default:
                value = super.onOptionsItemSelected(item);
        }
        return value;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mLogFacility.v(LOG_TAG, "onActivityResult with request code " + requestCode + " and result code " + resultCode);
        switch (requestCode) {
            case REQUEST_HIDE_ALL_VISIBLE_NOT_UPLOAD_PICTURES:
                if (Activity.RESULT_OK == resultCode) {
                    mActionsManager.hideAllVisibleNotUploadedPictures()
                            .executeAsync();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onPause() {
        mStatusChangeNotifier.unregisterListener(mStatusListenerId);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mStatusChangeNotifier.registerListener(mStatusListenerId, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = null;
        if (QUERY_VISIBLE_NOT_UPLOADED == id) {
            cl = mAmazingPictureDao.getLatestVisibleAndNotUploaded(100);
        } else if (QUERY_UPLOADED == id) {
            cl = mAmazingPictureDao.getLatestUploaded(100);
        } else {
            mLogFacility.v(LOG_TAG, "Strange, no query type for creating a CursorLoader");
        }
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPicturesAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPicturesAdapter.changeCursor(null);
    }

    @Override
    public void OnStatusChanges(String statusKey) {
        if (StatusChangeNotifier.STATUSKEY_REFRESHPICTURES.equals(statusKey)) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogFacility.v(LOG_TAG, "Changing picture refresh status to: " + mStatusChangeNotifier.isRefreshPicturesInProgress());
                    setSwipeLayoutState();
                }
            });
        }
    }

    private void launchPicturesRefresh() {
        mActionsManager.searchForNewImages()
                .executeAsync();
    }


    private void setSwipeLayoutState() {
        // http://nlopez.io/swiperefreshlayout-with-listview-done-right/
        if (QUERY_UPLOADED == mQueryListType) {
            mSwipeRefreshLayout.setEnabled(false);
        } else {
            mSwipeRefreshLayout.setEnabled(true);
            mSwipeRefreshLayout.setRefreshing(mStatusChangeNotifier.isRefreshPicturesInProgress());
        }
    }

}
