package com.example.instaclone.likes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.instaclone.R;
import com.example.instaclone.databinding.ActivityLikesBinding;
import com.example.instaclone.utils.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LikesActivity extends AppCompatActivity {

    ActivityLikesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLikesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // load different fragment on item selected in bottom nav bar
        int ACTIVITY_NO = 3;
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
