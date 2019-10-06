package com.kumoh.paylog2.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kumoh.paylog2.fragment.main.AccountListFragment;
import com.kumoh.paylog2.fragment.main.MainAccountFragment;

import java.util.ArrayList;

public class MainFragmentAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragmentList;
    public MainFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragmentList = new ArrayList<>();
        fragmentList.add(new MainAccountFragment());
        fragmentList.add(new AccountListFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
