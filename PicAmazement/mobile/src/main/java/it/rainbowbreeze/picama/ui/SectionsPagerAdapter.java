package it.rainbowbreeze.picama.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import it.rainbowbreeze.picama.R;

/**
 * Created by alfredomorresi on 07/06/15.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    String[] mTitles;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mTitles = new String[] {
                context.getString(R.string.main_tabPictures),
                context.getString(R.string.main_tabSaved)
        };
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        int queryType = 0;

        if (0 == position) {
            queryType = PicturesListFragment.QUERY_VISIBLE_NOT_UPLOADED;
        } else if (1 == position) {
            queryType = PicturesListFragment.QUERY_UPLOADED;
        }
        return PicturesListFragment.newInstance(queryType);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
