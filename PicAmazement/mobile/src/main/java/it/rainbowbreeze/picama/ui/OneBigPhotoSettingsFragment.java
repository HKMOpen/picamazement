package it.rainbowbreeze.picama.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.logic.scraper.onebigphoto.OneBigPhotoScraperConfig;

/**
* Created by alfredomorresi on 05/12/14.
*/
public class OneBigPhotoSettingsFragment extends InjectableFragment {
    private static final String LOG_TAG = OneBigPhotoSettingsFragment.class.getSimpleName();

    @Inject OneBigPhotoScraperConfig mScraperConfig;
    private CheckBox mChkEnabled;

    public OneBigPhotoSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_onebigpicture_settings, container, false);

        mChkEnabled = (CheckBox) rootView.findViewById(R.id.onebigpicturesettings_chkEnable);
        mChkEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setViewsStatus(isChecked);
            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Set<String> userNames = new TreeSet<>();
        mScraperConfig.setEnabled(mChkEnabled.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();
        mChkEnabled.setChecked(mScraperConfig.isEnabled());
    }

    private void setViewsStatus(boolean enabled) {
    }

}
