package com.example.instaclone.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.instaclone.R;
import com.example.instaclone.model.UserSetting;
import com.example.instaclone.utils.FireBaseMethods;
import com.example.instaclone.utils.UniversalImageLoader;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ViewProfileFragment extends Fragment {


    public ViewProfileFragment() {
        super();
        setArguments(new Bundle());
        // Required empty public constructor
    }


    private TextView userNameTv, userDisplayNameTv, descriptionTv, followersTv, followingTv, postTv;
    private TabLayout profileTabLayout;
    private ViewPager profileViewPager;
    private ImageView profileImageView;
    private UserSetting userSetting;
    private String userID = "";
    private DatabaseReference databaseReference;
    private FirebaseUser currentUSer;
    private Button followBtn, unFollowBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUSer = new FireBaseMethods(getContext()).getCurrentUser();

        // Toolbar not needed now
        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);
//        setSupportActionBar(toolbar);
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

        try {
            userSetting = getDataFromBundle();
            assert userSetting != null;
            updateProfileWidgets(userSetting);
            isFollowingToUser();
        } catch (NullPointerException e){
            Log.e("TAG", "onCreateView: NullPointerException" );
            if (getActivity() != null)
                getActivity().getSupportFragmentManager().popBackStack();
        }

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
                        startActivity(new Intent(getContext(), AccountsSettingActivity.class));
                    }
                }, 400);

            }
        });

        followBtn = view.findViewById(R.id.followBtn);
        unFollowBtn = view.findViewById(R.id.unFollowBtn);
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userID.length() > 1) {
                    databaseReference.child(getString(R.string.followings)).child(currentUSer.getUid()).child(userID)
                            .child(getString(R.string.user_id)).setValue(userID);
                    databaseReference.child(getString(R.string.followers)).child(userID).child(currentUSer.getUid())
                            .child(getString(R.string.user_id)).setValue(currentUSer.getUid());

                    setFollowing();
                } else {
                    Toast.makeText(getContext(), "Issue in userId", Toast.LENGTH_SHORT).show();
                }
            }
        });

        unFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userID.length() > 1) {
                    databaseReference.child(getString(R.string.followings)).child(currentUSer.getUid()).child(userID)
                            .removeValue();
                    databaseReference.child(getString(R.string.followers)).child(userID).child(currentUSer.getUid())
                            .removeValue();

                    setUnFollowing();
                } else {
                    Toast.makeText(getContext(), "Issue in userId", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void isFollowingToUser() {
        databaseReference.child(getString(R.string.followings)).child(currentUSer.getUid()).child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null){
                            setFollowing();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setFollowing() {
        followBtn.setVisibility(View.GONE);
        unFollowBtn.setVisibility(View.VISIBLE);
    }

    private void setUnFollowing() {
        followBtn.setVisibility(View.VISIBLE);
        unFollowBtn.setVisibility(View.GONE);
    }

    private UserSetting getDataFromBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(getString(R.string.user_account_settings))) {
            return bundle.getParcelable(getString(R.string.user_account_settings));
        } else {
            return null;
        }
    }

    private void setUpAdapter() {
        ProfilePagerAdapter profilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.POSITION_UNCHANGED);

        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.username), userSetting.getUsername());
        postFragment.setArguments(bundle);

        profilePagerAdapter.addFragment(postFragment);
        profilePagerAdapter.addFragment(new TagFragment());
        profileViewPager.setAdapter(profilePagerAdapter);
        profileTabLayout.setupWithViewPager(profileViewPager, true);
        Objects.requireNonNull(profileTabLayout.getTabAt(0)).setIcon(R.drawable.ic_grid);
        Objects.requireNonNull(profileTabLayout.getTabAt(1)).setIcon(R.drawable.ic_tag);
    }

    private void updateProfileWidgets(UserSetting userSetting) {
        UniversalImageLoader.setImage(userSetting.getProfile_photo(), profileImageView, null, "");
        userDisplayNameTv.setText(userSetting.getDisplay_name());
        userNameTv.setText(userSetting.getUsername());
        descriptionTv.setText(userSetting.getDescription());
        postTv.setText(userSetting.getPosts());
        followersTv.setText(userSetting.getFollowers());
        followingTv.setText(userSetting.getFollowing());

        getUserId(userSetting.getUsername());

    }


    private void getUserId(String userName) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(getString(R.string.users)).orderByChild(getString(R.string.username))
                .equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        userID = dataSnapshot1.getKey();
                    }
                } else {
                    Toast.makeText(getContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
