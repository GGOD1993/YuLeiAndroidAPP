package com.example.pc.myapplication.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ChildViewpagerAdapter extends FragmentPagerAdapter {

    private Activity activity;

    private ArrayList<Fragment> fragmentList;

    public ChildViewpagerAdapter(FragmentManager fm, Activity activity, ArrayList<Fragment> fragmentList) {
        super(fm);
        this.activity = activity;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
