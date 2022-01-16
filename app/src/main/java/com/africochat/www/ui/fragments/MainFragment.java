package com.africochat.www.ui.fragments;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.africochat.www.R;
import com.africochat.www.adapters.HomeTabsAdapter;
import com.africochat.www.databinding.MainFragmentBinding;
import com.africochat.www.firebase.FirebaseAuthBase;
import com.africochat.www.utils.FragmentUtil;
import com.africochat.www.utils.MainUtil;
import com.africochat.www.utils.PhotoUtil;
import com.africochat.www.viewmodels.MainViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.theartofdev.edmodo.cropper.CropImage;

public class MainFragment extends Fragment implements PhotoUtil.CropPhotoCallback {
    private MainFragmentBinding mMainFragmentBinding;
    private MainViewModel mMainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (!FirebaseAuthBase.checkIfCurrentUserIsSignedIn()) {
            // user is not logged in
            mMainFragmentBinding = MainFragmentBinding.inflate(getLayoutInflater(), container, false);
            // bind lifecycle owner
            mMainFragmentBinding.setLifecycleOwner(this);

            // add sign in fragment to part of layout if device is landscape or device is tablet
            if (mMainFragmentBinding.signInLayout != null) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                SignInFragment signInFragment = new SignInFragment();
                // Add sign in fragment to its container using a transaction
                FragmentUtil.addSpecificFragmentLayoutToScreen(fragmentManager, signInFragment, R.id.signInLayout);
            }
            return mMainFragmentBinding.getRoot();
        } else {
            // user is logged in
            View rootView = inflater.inflate(R.layout.home_fragment, container, false);
            ViewPager2 viewPager2 = rootView.findViewById(R.id.view_pager2);
            // Set up home fragment tabs
            HomeTabsAdapter homeTabsAdapter = new HomeTabsAdapter(requireActivity(), requireContext());
            // Set the adapter onto the view pager
            viewPager2.setAdapter(homeTabsAdapter);
            // Give the TabLayout the ViewPager
            TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
            new TabLayoutMediator(tabLayout,viewPager2,(tab, position) -> tab.setText(homeTabsAdapter.titles[position])).attach();
            return rootView;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // check for authentication
        if (!FirebaseAuthBase.checkIfCurrentUserIsSignedIn()) {
            // user not logged in
            inflater.inflate(R.menu.main_menu, menu);
        } else {
            // user is logged in
            inflater.inflate(R.menu.home_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.terms_service) {
            // terms of service
            String termsPageUrl = getString(R.string.terms_page_link_text);
            MainUtil.openWebPage(requireContext(), termsPageUrl);
        } else if (item.getItemId() == R.id.privacy_policy) {
            // privacy policy
            String privacyPageUrl = getString(R.string.privacy_page_link_text);
            MainUtil.openWebPage(requireContext(), privacyPageUrl);
        } else if (item.getItemId() == R.id.share_app) {
            // share app
            MainUtil.shareApp(requireContext());
        } else if(item.getItemId() == R.id.settings) {
            // navigate to settings fragment screen
            NavDirections action =
                    MainFragmentDirections
                            .actionMainFragmentToSettingsFragment();
            Navigation.findNavController(this.requireView()).navigate(action);
            return true;
        } else if (item.getItemId() == R.id.signOut) {
            // sign out
            FirebaseAuthBase.signUserOut();
            requireActivity().finish();
            startActivity(requireActivity().getIntent());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // if user is not logged in
        if (!FirebaseAuthBase.checkIfCurrentUserIsSignedIn()) {
            mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
            mMainFragmentBinding.setMainViewModel(mMainViewModel);

            // observe navigation event to Sign In fragment screen
            mMainViewModel.getNavigateToSignInFragmentEvt().observe(getViewLifecycleOwner(), navigate -> {
                if (navigate) {
                    // navigate to sign in fragment screen
                    NavDirections action =
                            MainFragmentDirections
                                    .actionMainFragmentToSignInFragment();
                    Navigation.findNavController(this.requireView()).navigate(action);
                    mMainViewModel.onNavigateToSignInFragmentDone();
                }
            });
        }
    }

    @Override
    public void onCropPhotoDone(boolean isSuccessful, Uri photoUri) {
        if (isSuccessful && photoUri != null) {
            // start cropping photo
            CropImage.activity(photoUri)
                    .start(requireContext(), this);

            // listen for photo cropping result
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        CropImage.ActivityResult cropResult = CropImage.getActivityResult(result.getData());
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (cropResult != null) {
                                Uri resultUri = cropResult.getUri();
                            }
                            // Handle the Intent
                        } else if (result.getResultCode() == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            MainUtil.showToastMessage(requireContext(),"Failed to crop photo");
                        }
                    });

        }
    }
}