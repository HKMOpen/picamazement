package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.logic.PictureScraperManager;
import it.rainbowbreeze.picama.logic.twitter.TwitterScraperConfig;

/**
* Created by alfredomorresi on 05/12/14.
*/
public class TwitterSettingsFragment extends Fragment {
    private static final String LOG_TAG = TwitterSettingsFragment.class.getSimpleName();
    private static final int REQUEST_DELETE_ACCOUNT = 100;
    private static final int REQUEST_ADD_ACCOUNT = 101;

    @Inject ILogFacility mLogFacility;
    @Inject TwitterScraperConfig mTwitterScraperConfig;
    @Inject PictureScraperManager mPictureScraperManager;
    private ArrayAdapter<String> mItemsAdapter;
    List<String> mUserNames;
    private ListView mList;
    private Context mAppContext;

    public TwitterSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_twitter_settings, container, false);

        mUserNames = new ArrayList<>(mTwitterScraperConfig.getUserNames());
        mItemsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, mUserNames);
        mList = (ListView) rootView.findViewById(R.id.twittersettings_lstAccounts);
        mList.setAdapter(mItemsAdapter);
        mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Button button;
        button = (Button) rootView.findViewById(R.id.twittersettings_btnAddAccount);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                askAndAddNewAccount(mItemsAdapter);
            }
        });
        button = (Button) rootView.findViewById(R.id.twittersettings_btnRemoveAccount);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                askToRemoveUserName();
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Set<String> userNames = new TreeSet<>();
        for(String userName : mUserNames) {
            userNames.add(userName);
        }
        mTwitterScraperConfig.setUserNames(userNames);
        mPictureScraperManager.updatePictureScraperConfig(mTwitterScraperConfig);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAppContext = activity.getApplicationContext();
        ((MyApp) mAppContext).inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DELETE_ACCOUNT:
                if (Activity.RESULT_OK == resultCode) {
                    removeAccountFromList();
                }
                break;

            case REQUEST_ADD_ACCOUNT:
                if (Activity.RESULT_OK == resultCode) {
                    addAccountFromList(data.getStringExtra(UserInputDialog.INTENT_EXTRA_USERINPUT));
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * Removes selected account from the list
     */
    private void removeAccountFromList() {
        int pos = mList.getCheckedItemPosition();
        mUserNames.remove(pos);
        mItemsAdapter.notifyDataSetChanged();
        mList.setItemChecked(0, false);
    }

    /**
     * Add a new account to the list
     *
     * @param newTwitterAccount
     */
    private void addAccountFromList(String newTwitterAccount) {
        if (TextUtils.isEmpty(newTwitterAccount)) {
            mLogFacility.v(LOG_TAG, "Empty account inputed by the user");
            return;
        }

        newTwitterAccount = newTwitterAccount.trim();
        // Checks for duplicates
        for(String username : mUserNames) {
            if (newTwitterAccount.equalsIgnoreCase(username)) {
                Toast.makeText(getActivity(), R.string.twittersettings_msgDuplicatedAccount, Toast.LENGTH_LONG).show();
                mLogFacility.v(LOG_TAG, "Duplicate account, no need to add");
                return;
            }
        }

        // Finally add the new account
        mLogFacility.v(LOG_TAG, "Adding new account " + newTwitterAccount);
        mUserNames.add(newTwitterAccount);
        mItemsAdapter.notifyDataSetChanged();
    }

    private void askAndAddNewAccount(ArrayAdapter<String> mItemsAdapter) {
        DialogFragment newDialog = UserInputDialog.newInstance(
                R.string.twittersettings_msgAddAccountPrompt,
                R.string.twittersettings_msgAddAccountTip);
        newDialog.setTargetFragment(this, REQUEST_ADD_ACCOUNT);
        newDialog.show(getFragmentManager(), "AddAccount");
    }

    private void askToRemoveUserName() {
        int pos = mList.getCheckedItemPosition();
        // Checks the sizes because, after deleting an item, the list still store the index of the deleted
        // item as checked item, but without any visual clue
        if (-1 == pos || pos >= mUserNames.size()) {
            Toast.makeText(this.getActivity(), R.string.twittersettings_msgSelectAccountFirst, Toast.LENGTH_SHORT).show();
            return;
        }

        DialogFragment newFragment = AskForConfirmationDialog.newInstance();
        newFragment.setTargetFragment(this, REQUEST_DELETE_ACCOUNT);
        newFragment.show(getFragmentManager(), "DeleteAccount");
    }

}
