package com.africochat.www.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.africochat.www.databinding.ChatsTabFragmentBinding;
import com.africochat.www.viewmodels.ChatsTabViewModel;

public class ChatsTabFragment extends Fragment {

    private ChatsTabFragmentBinding mChatsTabFragmentBinding;

    public static ChatsTabFragment newInstance() {
        return new ChatsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mChatsTabFragmentBinding = ChatsTabFragmentBinding.inflate(getLayoutInflater(),container,false);
        // bind lifecycle owner
        mChatsTabFragmentBinding.setLifecycleOwner(this);
        return mChatsTabFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChatsTabViewModel chatsTabViewModel = new ViewModelProvider(this).get(ChatsTabViewModel.class);
        mChatsTabFragmentBinding.setChatsTabViewModel(chatsTabViewModel);
    }
}