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
        switch (position){
            case 0: return new ListOfObjectsFragment();
            case 1: return new InteractiveMapFragment();
            default: return new ListOfObjectsFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
