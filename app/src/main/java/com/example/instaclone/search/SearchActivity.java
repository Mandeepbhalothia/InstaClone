package com.example.instaclone.search;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instaclone.databinding.ActivitySearchBinding;
import com.example.instaclone.utils.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // load different fragment on item selected in bottom nav bar
        int ACTIVITY_NO = 1;
        BottomNavigationView bottomNavigationView = binding.bottomNavBarSearchActivity.bottomNavView;
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(ACTIVITY_NO).getItemId());
        new Common(this).initBottomNavListener(bottomNavigationView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
