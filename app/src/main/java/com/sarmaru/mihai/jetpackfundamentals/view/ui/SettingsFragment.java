package com.sarmaru.mihai.jetpackfundamentals.view.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.sarmaru.mihai.jetpackfundamentals.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}