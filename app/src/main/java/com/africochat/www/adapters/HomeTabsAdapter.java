package com.africochat.www.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.africochat.www.R;
import com.africochat.www.ui.fragments.CallsTabFragment;
import com.africochat.www.ui.fragments.ChatsTabFragment;
import com.africochat.www.ui.fragments.FriendsTabFragment;

public class HomeTabsAdapter extends FragmentStateAdapter {

    public final String[] titles;

    public HomeTabsAdapter(@NonNull FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity);
        titles = new String[] {
                context.getString(R.string.chats_tab_fragment_title),
                context.getString(R.string.friends_tab_fragment_title),
                context.getString(R.string.calls_tab_fragment_title)
        };
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ChatsTabFragment();
        } else if (position == 1) {
            return new FriendsTabFragment();
        } else {
            return new CallsTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
