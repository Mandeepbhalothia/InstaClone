package com.example.instaclone.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.instaclone.R;
import com.example.instaclone.model.Photo;
import com.example.instaclone.utils.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity implements PostFragment.OnGridItemClickListener,
        ViewPostFragment.OnCommentThreadClickListener {

    private int ACTIVITY_NO = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        /*set bottom nav bar*/

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(ACTIVITY_NO).getItemId());
        new Common(this).initBottomNavListener(bottomNavigationView);

        // load profile fragment initially
        init();

    }

    private void init() {
        if (getIntent().hasExtra(getString(R.string.calling_activity))){

            if (getIntent().hasExtra(getString(R.string.user_account_settings))){
                ViewProfileFragment viewProfileFragment = new ViewProfileFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putParcelable(getString(R.string.user_account_settings),
                        getIntent().getParcelableExtra(getString(R.string.user_account_settings)));
                viewProfileFragment.setArguments(bundle);
                ft.replace(R.id.container, viewProfileFragment);
                ft.addToBackStack(getString(R.string.view_profile_fragment));
                ft.commit();
            }

        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container,new ProfileFragment());
            transaction.addToBackStack(getString(R.string.profile));
            transaction.commit();
        }
    }

    @Override
    public void onGridItemClicked(Photo photo) {
        if (photo != null){
            ViewPostFragment fragment = new ViewPostFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.photo), photo);
            bundle.putInt(getString(R.string.activity_no),ACTIVITY_NO);
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container,fragment);
            transaction.addToBackStack(getString(R.string.view_post_fragment));
            transaction.commit();
        }
    }

    @Override
    public void onCommentThreadClicked(Photo photo) {
        ViewCommentFragment viewCommentFragment = new ViewCommentFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.photo),photo);
        viewCommentFragment.setArguments(bundle);
        ft.replace(R.id.container, viewCommentFragment);
        ft.addToBackStack(getString(R.string.comment_fragment));
        ft.commit();
    }
}
