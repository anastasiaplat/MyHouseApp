package com.example.myhouseapp0;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

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

    // Метод для получения Tab1Fragment
//    public ListOfObjectsFragment getTab1Fragment() {
//        FragmentManager fm = requireActivity().getSupportFragmentManager();
//
//        List<Fragment> fragments = fm.getFragments();
//
//        for (Fragment fragment : fragments) {
//            if (fragment instanceof ListOfObjectsFragment) {
//                return (ListOfObjectsFragment) fragment;
//            }
//        }
//        return null;
//    }


}
