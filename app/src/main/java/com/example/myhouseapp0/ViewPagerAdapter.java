package com.example.myhouseapp0;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull HomeFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new InteractiveMapFragment();
        }
        return new ListOfObjectsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
