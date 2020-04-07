package com.example.instaclone.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.instaclone.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private TextView userNameTv;
    private ProfilePagerAdapter profilePagerAdapter;
    private TabLayout profileTabLayout;
    private ViewPager profileViewPager;

    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Toolbar not needed now
        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        userNameTv = view.findViewById(R.id.toolbar_name);
        profileTabLayout = view.findViewById(R.id.profileTabLayout);
        profileViewPager = view.findViewById(R.id.profileViewPager);
        LinearLayout settingLayout = view.findViewById(R.id.nav_setting_layout);

        final ConstraintLayout contentLayout = view.findViewById(R.id.mainContent);
        ImageButton toolbarOptionBtn = view.findViewById(R.id.toolbar_option);
        final DrawerLayout drawerLayout = view.findViewById(R.id.drawerLayout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                contentLayout.setTranslationX(-slideX);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        toolbarOptionBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        setUpAdapter();


        settingLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getContext(), AccountsSetting.class));
                    }
                },400);

            }
        });

        return view;
    }

    private void setUpAdapter() {
        profilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.POSITION_UNCHANGED);
        profilePagerAdapter.addFragment(new PostFragment());
        profilePagerAdapter.addFragment(new TagFragment());
        profileViewPager.setAdapter(profilePagerAdapter);
        profileTabLayout.setupWithViewPager(profileViewPager, true);
        Objects.requireNonNull(profileTabLayout.getTabAt(0)).setIcon(R.drawable.ic_grid);
        Objects.requireNonNull(profileTabLayout.getTabAt(1)).setIcon(R.drawable.ic_tag);
        profileViewPager.setScrollX(500);

    }
}
