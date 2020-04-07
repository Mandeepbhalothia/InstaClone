package com.example.instaclone.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private HashMap<Fragment, Integer> mFragments = new HashMap<>();
    private HashMap<String, Integer> mFragmentNo = new HashMap<>();
    private HashMap<Integer, String> mFragmentName = new HashMap<>();

    public SectionsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
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

    public void addFragment(Fragment fragment, String name){
        fragmentList.add(fragment);
        mFragments.put(fragment, fragmentList.size()-1);
        mFragmentNo.put(name,fragmentList.size()-1);
        mFragmentName.put(fragmentList.size()-1, name);
    }

    public Integer getFragmentNumber(String fragmentName){
        if (mFragmentNo.containsKey(fragmentName)){
            return mFragmentNo.get(fragmentName);
        } else
            return null;
    }
    public String getFragmentNumber(int fragmentNo){
        if (mFragmentName.containsKey(fragmentNo)){
            return mFragmentName.get(fragmentNo);
        } else
            return null;
    }
    public Integer getFragmentNumber(Fragment fragment){
        if (mFragments.containsKey(fragment)){
            return mFragments.get(fragment);
        } else
            return null;
    }

}
