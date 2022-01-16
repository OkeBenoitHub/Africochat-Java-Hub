package com.africochat.www.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Fragment Util :: contain every recurring task dealing with Fragments
 */
final public class FragmentUtil {

    /**
     * Add a specific fragment layout to screen
     * @param fragmentManager :: fragment manager
     * @param fragment :: fragment name
     * @param fragmentLayoutResId :: fragment layout resource id
     */
    public static void addSpecificFragmentLayoutToScreen(FragmentManager fragmentManager, Fragment fragment, int fragmentLayoutResId) {
        // getSupportFragmentManager
        if (fragmentManager != null) {
            if (fragmentManager.findFragmentById(fragmentLayoutResId) == null) {
                fragmentManager.beginTransaction()
                        .add(fragmentLayoutResId, fragment)
                        .commit();
            }
        }
    }
}
