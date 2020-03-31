package com.example.instaclone.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.instaclone.R;
import com.example.instaclone.home.CameraFragment;
import com.example.instaclone.home.HomeFragment;
import com.example.instaclone.home.MessageFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setAdapter() {
        if (getFragmentManager() == null) {
            Toast.makeText(getContext(), "Fragment Manager Is Null", Toast.LENGTH_SHORT).show();
            return;
        }
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getChildFragmentManager(),
                FragmentPagerAdapter.POSITION_UNCHANGED);
        mainPagerAdapter.addFragment(new CameraFragment());
        mainPagerAdapter.addFragment(new HomeFragment());
        mainPagerAdapter.addFragment(new MessageFragment());

        viewPager.setAdapter(mainPagerAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_camera);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.instagram_logo);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_message);
        viewPager.setCurrentItem(1);
    }
}
