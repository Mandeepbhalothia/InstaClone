package com.example.instaclone.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.instaclone.R;
import com.example.instaclone.databinding.ActivityAccountsSettingBinding;
import com.example.instaclone.utils.SectionsPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountsSettingActivity extends AppCompatActivity {


    private Group mainLayoutGroup;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_setting);
        BottomNavigationView bottomNavigationView = findViewById(R.id.accounts_bottom_nav_view);
        ImageButton backButton = findViewById(R.id.accountsBackButton);
        mainLayoutGroup = findViewById(R.id.account_fragmentGroup);
        viewPager = findViewById(R.id.account_setting_viewPager);

        setUpPagerAdapter();

        // we can directly load edit profile fragment
        checkIntentData();

        bottomNavigationView.setSelectedItemId(R.id.profileMenu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.accountTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // now we will use add accounts for edit profile purpose
                setViewPager(getString(R.string.edit_profile_fragment));
            }
        });

        findViewById(R.id.logOutTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewPager(getString(R.string.log_out_fragment));
            }
        });

    }

    private void checkIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.calling_activity))) {
            setViewPager(intent.getStringExtra(getString(R.string.calling_activity)));
        }
    }

    private void setUpPagerAdapter() {
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        sectionsPagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile_fragment));// fragment 0
        sectionsPagerAdapter.addFragment(new LogOutFragment(), getString(R.string.log_out_fragment));// fragment 1
    }

    private void setViewPager(String fragmentName) {
        mainLayoutGroup.setVisibility(View.GONE);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(sectionsPagerAdapter.getFragmentNumber(fragmentName));
    }
}
