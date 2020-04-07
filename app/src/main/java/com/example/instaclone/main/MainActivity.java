package com.example.instaclone.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instaclone.R;
import com.example.instaclone.addMore.AddMoreFragment;
import com.example.instaclone.likes.LikesFragment;
import com.example.instaclone.profile.ProfileFragment;
import com.example.instaclone.search.SearchFragment;
import com.example.instaclone.utils.UniversalImageLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends AppCompatActivity {

    FrameLayout fragmentContainer;
    public static BottomNavigationView bottomNavigationView;
    MenuItem lasTSelectedMenu;
    Fragment mainFragment, searchFragment, addMoreFragment, likesFragment, profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initImageLoader();

        loadFragment(new MainFragment());

    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void initViews() {
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        // last selected menuItem
        lasTSelectedMenu = bottomNavigationView.getMenu().getItem(0);
        // load different fragment on item selected
        initBottomNavListener();

    }

    private void initBottomNavListener() {
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
    }
}
