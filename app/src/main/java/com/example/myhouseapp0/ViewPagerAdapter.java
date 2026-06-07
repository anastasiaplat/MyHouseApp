package com.example.myhouseapp0;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments;

    public ViewPagerAdapter(@NonNull HomeFragment fragmentActivity) {

        super(fragmentActivity);
        fragments = new ArrayList<>();

        fragments.add(new ListOfObjectsFragment());
        fragments.add(new InteractiveMapFragment());

        // позиция 0
        // позиция 1
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
//        if (position == 1) {
//            return new InteractiveMapFragment();
//        }
//        return new ListOfObjectsFragment();
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    // Метод для получения фрагмента по позиции
    public Fragment getFragment(int position) {
        if (position >= 0 && position < fragments.size()) {
            return fragments.get(position);
        }
        return null;
    }



}
