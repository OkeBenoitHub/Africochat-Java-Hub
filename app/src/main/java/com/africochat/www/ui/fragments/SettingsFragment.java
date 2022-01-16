package com.africochat.www.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.africochat.www.databinding.SettingsFragmentBinding;
import com.africochat.www.viewmodels.SettingsViewModel;

public class SettingsFragment extends Fragment {

    private SettingsFragmentBinding mSettingsFragmentBinding;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mSettingsFragmentBinding = SettingsFragmentBinding.inflate(getLayoutInflater(),container,false);
        // bind lifecycle owner
        mSettingsFragmentBinding.setLifecycleOwner(this);
        return mSettingsFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        mSettingsFragmentBinding.setSettingsViewModel(settingsViewModel);
    }
}