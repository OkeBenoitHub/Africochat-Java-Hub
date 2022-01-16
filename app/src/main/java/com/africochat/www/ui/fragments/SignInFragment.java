package com.africochat.www.ui.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.africochat.www.R;
import com.africochat.www.databinding.SignInFragmentBinding;
import com.africochat.www.firebase.FirebaseAuthBase;
import com.africochat.www.utils.DialogUtil;
import com.africochat.www.utils.MainUtil;
import com.africochat.www.utils.NetworkUtil;
import com.africochat.www.viewmodels.SignInViewModel;

public class SignInFragment extends Fragment implements FirebaseAuthBase.signUserInProcessCallback, DialogUtil.showBasicAlertDialogCallback, FirebaseAuthBase.sendUserPasswordResetEmailCallback {

    private SignInViewModel mSignInViewModel;
    private SignInFragmentBinding mSignInFragmentBinding;
    private boolean mIsErrorFound; // tracks errors :: user trying to sign in
    private EditText mResetEmailEdt;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mSignInFragmentBinding = SignInFragmentBinding.inflate(getLayoutInflater(), container,false);
        // bind lifecycle owner
        mSignInFragmentBinding.setLifecycleOwner(this);
        return mSignInFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSignInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        mSignInFragmentBinding.setSignInViewModel(mSignInViewModel);

        // sign in button tapped
        mSignInFragmentBinding.signInButton.setOnClickListener(view1 -> signCurrentUserIn());

        // detect user press done to sign in
        mSignInFragmentBinding.passwordEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // done button pressed from keyboard
                signCurrentUserIn();
            }
            return false;
        });

        // observe when forgot password link gets tapped
        mSignInViewModel.getForgotPasswordTappedEvt().observe(getViewLifecycleOwner(), forgotPasswordTapped -> {
            if (forgotPasswordTapped) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                mResetEmailEdt = new EditText(requireContext());
                mResetEmailEdt.setLayoutParams(lp);
                mResetEmailEdt.setHint(getString(R.string.enter_user_email_error_text));
                // show basic alert email box
                DialogUtil.showBasicAlertDialog(
                        requireContext(),
                        getString(R.string.reset_password_title_text),
                        "",
                        mResetEmailEdt,
                        getString(R.string.reset_button_text),
                        getString(R.string.cancel_button_text),
                        true,this

                );
                mSignInViewModel.onForgotPasswordTappedDone();
            }
        });
    }

    /**
     * Sign current user in
     */
    private void signCurrentUserIn() {
        // check for errors before trying to sign in
        checkForErrors();
        if (!mIsErrorFound) {
            // try to sign user into his or her account
            // show loader bar
            mSignInFragmentBinding.loaderBar.setVisibility(View.VISIBLE);
            FirebaseAuthBase.signUserInToAccount(
                    requireActivity(),
                    mSignInViewModel.getUserEmail(),
                    mSignInViewModel.getUserPassword(),
                    this
            );
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // check for user authentication
        if (FirebaseAuthBase.checkIfCurrentUserIsSignedIn()) {
            // user is logged in
            // navigate back to Main fragment screen
            Navigation.findNavController(this.requireView()).navigateUp();
        }
    }

    /**
     * Check sign in for errors
     */
    private void checkForErrors() {
        // check for user mail
        EditText userEmail = mSignInFragmentBinding.emailEdt;
        String userEmailValue = userEmail.getText().toString().trim();
        mSignInViewModel.setUserEmail(null);
        if (userEmailValue.isEmpty()) {
            userEmail.setError(getString(R.string.enter_user_email_error_text));
            mIsErrorFound = true;
            return;
        } else if (MainUtil.isEmailValid(userEmailValue)) {
            userEmail.setError(getString(R.string.invalid_email_format_error_text));
            mIsErrorFound = true;
            return;
        } else {
            userEmail.setError(null);
            mIsErrorFound = false;
            mSignInViewModel.setUserEmail(userEmailValue);
        }

        // check for user password
        EditText userPassword = mSignInFragmentBinding.passwordEdt;
        String userPasswordValue = userPassword.getText().toString().trim();
        mSignInViewModel.setUserPassword(null);
        if (userPasswordValue.isEmpty()) {
            userPassword.setError(getString(R.string.enter_user_password_error_text));
            mIsErrorFound = true;
            return;
        } else {
            userPassword.setError(null);
            mIsErrorFound = false;
            mSignInViewModel.setUserPassword(userPasswordValue);
        }

        // Check if user is connected to network
        if (!NetworkUtil.isUserConnectedToNetwork(requireContext())) {
            mIsErrorFound = true;
            MainUtil.showToastMessage(requireContext(), getString(R.string.no_internet_connection_text));
        }
    }

    @Override
    public void onSignUserInProcessStatus(boolean isSuccessful, String errorMessage) {
        // hide loader bar
        mSignInFragmentBinding.loaderBar.setVisibility(View.INVISIBLE);
        if (isSuccessful) {
            // sign in successfully
            // navigate back to Main fragment screen
            Navigation.findNavController(this.requireView()).navigateUp();
        } else {
            // display error message
            MainUtil.showToastMessage(requireContext(),errorMessage);
            mSignInFragmentBinding.loaderBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onShowBasicAlertDialog(boolean isPositiveBtnTapped) {
        if (isPositiveBtnTapped) {
            String resetEmailValue = mResetEmailEdt.getText().toString().trim();
            if (MainUtil.isEmailValid(resetEmailValue)) {
                MainUtil.showToastMessage(requireContext(),getString(R.string.invalid_email_format_error_text));
            } else {
                // try to send reset password email to user
                FirebaseAuthBase.sendUserPasswordResetEmail(requireContext(),resetEmailValue,this);
            }
        }
    }

    @Override
    public void onUserPasswordResetEmailSent(boolean isSuccessful, String errorMessage) {
        if (isSuccessful) {
            // reset password email sent successfully
            mSignInFragmentBinding.resetPasswordMessageTv.setVisibility(View.VISIBLE);
        } else {
            MainUtil.showToastMessage(requireContext(),errorMessage);
        }
    }
}