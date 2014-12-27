package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.AppPrefsManager;
import it.rainbowbreeze.picama.logic.twitter.TwitterScraperConfig;

/**
* Created by alfredomorresi on 05/12/14.
*/
public class TwitterSettingsFragment extends Fragment {
    private static final String LOG_TAG = TwitterSettingsFragment.class.getSimpleName();

    @Inject ILogFacility mLogFacility;
    @Inject AppPrefsManager mAppPrefsManager;
    @Inject TwitterScraperConfig mTwitterScraperConfig;
    private Context mAppContext;

    public TwitterSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_twitter_settings, container, false);

        Button btn;
        btn = (Button) rootView.findViewById(R.id.twittersettings_btnAddAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TwitterSettingsFragment.this.getActivity(), "Not implemented", Toast.LENGTH_SHORT).show();
            }
        });
        btn = (Button) rootView.findViewById(R.id.twittersettings_btnRemoveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TwitterSettingsFragment.this.getActivity(), "Not implemented", Toast.LENGTH_SHORT).show();
            }
        });

        List<String> userNames = mTwitterScraperConfig.getUserNames();
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userNames);
        ListView lst = (ListView) rootView.findViewById(R.id.twittersettings_lstAccounts);
        lst.setAdapter(itemsAdapter);

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAppContext = activity.getApplicationContext();
        ((MyApp) mAppContext).inject(this);
    }
}
