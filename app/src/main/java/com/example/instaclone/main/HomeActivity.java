package com.example.instaclone.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.instaclone.R;
import com.example.instaclone.databinding.ActivityHomeBinding;
import com.example.instaclone.home.CameraFragment;
import com.example.instaclone.home.HomeFragment;
import com.example.instaclone.home.MessageFragment;
import com.example.instaclone.utils.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setAdapter();
        // load different fragment on item selected in bottom nav bar
        int ACTIVITY_NO = 0;
        BottomNavigationView bottomNavigationView = binding.bottomNavBarHomeActivity.bottomNavView;
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(ACTIVITY_NO).getItemId());
        new Common(this).initBottomNavListener(bottomNavigationView);
    }

    private void setAdapter() {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.POSITION_UNCHANGED);
        mainPagerAdapter.addFragment(new CameraFragment());
        mainPagerAdapter.addFragment(new HomeFragment());
        mainPagerAdapter.addFragment(new MessageFragment());

        ViewPager viewPager = binding.viewPagerHomeActivity.viewPager;
        TabLayout tabLayout = binding.toolBarHomeActivity.tabLayout;
        viewPager.setAdapter(mainPagerAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_camera);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.instagram_logo);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_message);
        viewPager.setCurrentItem(1);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
