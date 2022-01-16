package com.africochat.www.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.africochat.www.R;
import com.africochat.www.utils.MainUtil;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private NavController mNavController;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        MainUtil.setActionBarBackgroundColor(this,getSupportActionBar(),R.color.colorPrimary);
        MainUtil.setStatusBarBackgroundColor(this,getWindow(),R.color.bottom_black_color);

        // Add support for up button for fragment navigation
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, mNavController,mAppBarConfiguration);

        // remove bottom shadow from action bar
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}