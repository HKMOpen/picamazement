package it.rainbowbreeze.picama.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.rainbowbreeze.picama.R;

/**
* Created by alfredomorresi on 05/12/14.
*/
public class GDriveSettingsFragment extends Fragment {
    public GDriveSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_dropbox_settings, container, false);
        return rootView;
    }
}
