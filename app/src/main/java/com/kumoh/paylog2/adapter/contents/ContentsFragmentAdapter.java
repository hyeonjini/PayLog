package com.kumoh.paylog2.adapter.contents;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kumoh.paylog2.fragment.contents.ContentsCalendarFragment;
import com.kumoh.paylog2.fragment.contents.ContentsListFragment;
import com.kumoh.paylog2.fragment.contents.ContentsMonthFragment;
import com.kumoh.paylog2.fragment.contents.ContentsStatisticsFragment;

import java.util.ArrayList;

public class ContentsFragmentAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragments;
    public ContentsFragmentAdapter(@NonNull FragmentManager fm, int behavior, int accountId) {
        super(fm, behavior);

        fragments = new ArrayList<Fragment>();
        fragments.add(new ContentsListFragment(accountId));
        fragments.add(new ContentsMonthFragment(accountId));
        fragments.add(new ContentsCalendarFragment());
        fragments.add(new ContentsStatisticsFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
