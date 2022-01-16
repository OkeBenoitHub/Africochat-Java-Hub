package com.africochat.www.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    /**
     * Variable that tells the Fragment to navigate to sign in fragment
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private MutableLiveData<Boolean> mNavigateToSignInFragmentEvt;

    public LiveData<Boolean> getNavigateToSignInFragmentEvt() {
        if (mNavigateToSignInFragmentEvt == null) {
            mNavigateToSignInFragmentEvt = new MutableLiveData<>();
        }
        return mNavigateToSignInFragmentEvt;
    }

    public void onNavigateToSignInFragment() {
        mNavigateToSignInFragmentEvt.postValue(true);
    }

    public void onNavigateToSignInFragmentDone() {
        mNavigateToSignInFragmentEvt.postValue(false);
    }
}