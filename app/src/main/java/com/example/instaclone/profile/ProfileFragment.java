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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.instaclone.R;
import com.example.instaclone.model.User;
import com.example.instaclone.model.UserAccountDetails;
import com.example.instaclone.model.UserSetting;
import com.example.instaclone.utils.UniversalImageLoader;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private TextView userNameTv, userDisplayNameTv, descriptionTv, followersTv, followingTv, postTv;
    private TabLayout profileTabLayout;
    private ViewPager profileViewPager;
    private ImageView profileImageView;

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
        userDisplayNameTv = view.findViewById(R.id.toolbar_name);
        userNameTv = view.findViewById(R.id.userCustomIdTv);
        descriptionTv = view.findViewById(R.id.userBioTv);
        postTv = view.findViewById(R.id.postCountTv);
        followingTv = view.findViewById(R.id.followingCountTv);
        followersTv = view.findViewById(R.id.followersCountTv);
        profileImageView = view.findViewById(R.id.userIV);
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

//        setProfileImage();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        getUserAccountDetails(databaseReference);


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
                        startActivity(new Intent(getContext(), AccountsSettingActivity.class));
                    }
                }, 400);

            }
        });

        view.findViewById(R.id.editProfileBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AccountsSettingActivity.class);
                intent.putExtra(getString(R.string.calling_activity),getString(R.string.edit_profile_fragment));
                startActivity(intent);
            }
        });

        return view;
    }

    private void setUpAdapter() {
        ProfilePagerAdapter profilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.POSITION_UNCHANGED);
        profilePagerAdapter.addFragment(new PostFragment());
        profilePagerAdapter.addFragment(new TagFragment());
        profileViewPager.setAdapter(profilePagerAdapter);
        profileTabLayout.setupWithViewPager(profileViewPager, true);
        Objects.requireNonNull(profileTabLayout.getTabAt(0)).setIcon(R.drawable.ic_grid);
        Objects.requireNonNull(profileTabLayout.getTabAt(1)).setIcon(R.drawable.ic_tag);
        profileViewPager.setScrollX(500);

    }

    private void updateProfileWidgets(UserSetting userSetting){
        UniversalImageLoader.setImage(userSetting.getProfile_photo(), profileImageView, null, "");
        userDisplayNameTv.setText(userSetting.getDisplay_name());
        userNameTv.setText(userSetting.getUsername());
        descriptionTv.setText(userSetting.getDescription());
        postTv.setText(userSetting.getPosts());
        followersTv.setText(userSetting.getFollowers());
        followingTv.setText(userSetting.getFollowing());
    }

    private void setProfileImage() {
        String imgURL = "www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
        UniversalImageLoader.setImage(imgURL, profileImageView, null, "https://");
    }

    /*get user account settings from firebase*/
    private void getUserAccountDetails(final DatabaseReference databaseReference) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // first get user_account_settings
            databaseReference.child(getString(R.string.user_account_settings)).child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                UserSetting userSetting = dataSnapshot.getValue(UserSetting.class);
                                // update UI here full data received of current user
                                updateProfileWidgets(userSetting);

                            } else {
                                Toast.makeText(getContext(), R.string.data_issue, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    /*private void initBottomNavListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeMenu:
                        if (lasTSelectedMenu != item) {
                            // to always start home fragment
//                            if (mainFragment==null)
                            mainFragment = new MainFragment();
                            loadFragment(mainFragment);
                            lasTSelectedMenu = item;
                            break;
                        }
                    case R.id.searchMenu:
                        if (lasTSelectedMenu != item) {
                            if (searchFragment == null)
                                searchFragment = new SearchFragment();
                            loadFragment(searchFragment);
                            lasTSelectedMenu = item;
                            break;
                        }
                    case R.id.addMenu:
                        if (lasTSelectedMenu != item) {
                            if (addMoreFragment == null)
                                addMoreFragment = new AddMoreFragment();
                            loadFragment(addMoreFragment);
                            lasTSelectedMenu = item;
                            break;
                        }
                    case R.id.likeMenu:
                        if (lasTSelectedMenu != item) {
                            if (likesFragment == null)
                                likesFragment = new LikesFragment();
                            loadFragment(likesFragment);
                            lasTSelectedMenu = item;
                            break;
                        }
                    case R.id.profileMenu:
                        if (lasTSelectedMenu != item) {
                            if (profileFragment == null)
                                profileFragment = new ProfileFragment();
                            loadFragment(profileFragment);
                            lasTSelectedMenu = item;
                            break;
                        }
                }

                return true;
            }
        });
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }*/
}
