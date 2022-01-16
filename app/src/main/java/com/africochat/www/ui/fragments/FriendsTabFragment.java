package com.africochat.www.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.africochat.www.databinding.FriendsTabFragmentBinding;
import com.africochat.www.viewmodels.FriendsTabViewModel;

public class FriendsTabFragment extends Fragment {

    private FriendsTabFragmentBinding mFriendsTabFragmentBinding;

    public static FriendsTabFragment newInstance() {
        return new FriendsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFriendsTabFragmentBinding = FriendsTabFragmentBinding.inflate(getLayoutInflater(),container,false);
        // bind lifecycle owner
        mFriendsTabFragmentBinding.setLifecycleOwner(this);
        return mFriendsTabFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FriendsTabViewModel friendsTabViewModel = new ViewModelProvider(this).get(FriendsTabViewModel.class);
        mFriendsTabFragmentBinding.setFriendsTabViewModel(friendsTabViewModel);
    }
}