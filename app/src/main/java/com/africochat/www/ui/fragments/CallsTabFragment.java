package com.africochat.www.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.africochat.www.databinding.CallsTabFragmentBinding;
import com.africochat.www.viewmodels.CallsTabViewModel;

public class CallsTabFragment extends Fragment {

    private CallsTabFragmentBinding mCallsTabFragmentBinding;

    public static CallsTabFragment newInstance() {
        return new CallsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mCallsTabFragmentBinding = CallsTabFragmentBinding.inflate(getLayoutInflater(),container,false);
        // bind lifecycle owner
        mCallsTabFragmentBinding.setLifecycleOwner(this);
        return mCallsTabFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CallsTabViewModel callsTabViewModel = new ViewModelProvider(this).get(CallsTabViewModel.class);
        mCallsTabFragmentBinding.setCallsTabViewModel(callsTabViewModel);
    }
}