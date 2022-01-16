package com.africochat.www.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignInViewModel extends ViewModel {
    public String mUserEmail;
    public String mUserPassword;

    public String getUserEmail() {
        return mUserEmail;
    }

    /**
     * Variable that detects when forgot password link tapped
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private MutableLiveData<Boolean> mForgotPasswordTappedEvt;

    public LiveData<Boolean> getForgotPasswordTappedEvt() {
        if (mForgotPasswordTappedEvt == null) {
            mForgotPasswordTappedEvt = new MutableLiveData<>();
        }
        return mForgotPasswordTappedEvt;
    }

    public void onForgotPasswordTapped() {
        mForgotPasswordTappedEvt.postValue(true);
    }
    public void onForgotPasswordTappedDone() {
        mForgotPasswordTappedEvt.postValue(false);
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public String getUserPassword() {
        return mUserPassword;
    }

    public void setUserPassword(String userPassword) {
        mUserPassword = userPassword;
    }
}