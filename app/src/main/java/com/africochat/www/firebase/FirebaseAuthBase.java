package com.africochat.www.firebase;

import android.app.Activity;
import android.content.Context;

import com.africochat.www.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Firebase Authentication :: contain every recurring task dealing with authentication
 */
final public class FirebaseAuthBase {
    /**
     * Get current user
     *
     * @return void
     */
    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }

    /**
     * Get current auth state
     *
     * @return void
     */
    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    /**
     * Check if current user is signed in
     *
     * @return true or false
     */
    public static boolean checkIfCurrentUserIsSignedIn() {
        return getCurrentUser() != null;
    }

    // interface that will check for sign in process status
    public interface signUserInProcessCallback {
        void onSignUserInProcessStatus(boolean isSuccessful, String errorMessage);
    }

    /**
     * Sign User into his or her account
     *
     * @param userEmail    :: user email
     * @param userPassword :: user password
     */
    public static void signUserInToAccount(Activity activity, String userEmail, String userPassword, signUserInProcessCallback signUserInProcessCallback) {
        getFirebaseAuth().signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        signUserInProcessCallback.onSignUserInProcessStatus(true, null);
                    }
                }).addOnFailureListener(e -> {
            // If sign in fails, generate the appropriate error message.
            if (e instanceof FirebaseAuthInvalidUserException) {
                String errorCode = ((FirebaseAuthInvalidUserException) e).getErrorCode();
                if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                    signUserInProcessCallback.onSignUserInProcessStatus(false, activity.getString(R.string.email_not_found_for_user_error));
                } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                    signUserInProcessCallback.onSignUserInProcessStatus(false, activity.getString(R.string.account_disabled_text));
                } else {
                    signUserInProcessCallback.onSignUserInProcessStatus(false, e.getLocalizedMessage());
                }
            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                signUserInProcessCallback.onSignUserInProcessStatus(false, activity.getString(R.string.incorrect_password_error));
            }
        });
    }

    // interface that will check for sign in process status
    public interface sendUserPasswordResetEmailCallback {
        void onUserPasswordResetEmailSent(boolean isSuccessful, String errorMessage);
    }

    /**
     * Send password reset email to user
     *
     * @param userEmail                          :: user email
     * @param sendUserPasswordResetEmailCallback :: callback
     */
    public static void sendUserPasswordResetEmail(Context context, String userEmail, sendUserPasswordResetEmailCallback sendUserPasswordResetEmailCallback) {
        getFirebaseAuth().useAppLanguage();
        getFirebaseAuth().sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(task -> sendUserPasswordResetEmailCallback.onUserPasswordResetEmailSent(task.isSuccessful(), null))
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        String errorCode = ((FirebaseAuthInvalidUserException) e).getErrorCode();
                        if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                            sendUserPasswordResetEmailCallback.onUserPasswordResetEmailSent(false, context.getString(R.string.email_not_found_for_user_error));
                        } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                            sendUserPasswordResetEmailCallback.onUserPasswordResetEmailSent(false, context.getString(R.string.account_disabled_text));
                        } else {
                            sendUserPasswordResetEmailCallback.onUserPasswordResetEmailSent(false, e.getLocalizedMessage());
                        }
                    }
                });
    }

    /**
     * Sign user out
     */
    public static void signUserOut() {
        getFirebaseAuth().signOut();
    }
}
