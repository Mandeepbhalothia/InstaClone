package com.example.instaclone.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.instaclone.R;
import com.example.instaclone.addMore.AddMoreFragment;
import com.example.instaclone.home.HomeFragment;
import com.example.instaclone.likes.LikesFragment;
import com.example.instaclone.profile.ProfileFragment;
import com.example.instaclone.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FrameLayout fragmentContainer;
    BottomNavigationView bottomNavigationView;
    MenuItem lasTSelectedMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        loadFragment(new MainFragment());

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
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
                            loadFragment(new MainFragment());
                            lasTSelectedMenu = item;
                            break;
                        }
                    case R.id.searchMenu:
                        if (lasTSelectedMenu != item) {
                            loadFragment(new SearchFragment());
                            lasTSelectedMenu = item;
                            break;
                        }
                    case R.id.addMenu:
                        if (lasTSelectedMenu != item) {
                            loadFragment(new AddMoreFragment());
                            lasTSelectedMenu = item;
                            break;
                        }
                    case R.id.likeMenu:
                        if (lasTSelectedMenu != item) {
                            loadFragment(new LikesFragment());
                            lasTSelectedMenu = item;
                            break;
                        }
                    case R.id.profileMenu:
                        if (lasTSelectedMenu != item) {
                            loadFragment(new ProfileFragment());
                            lasTSelectedMenu = item;
                            break;
                        }
                }

                return true;
            }
        });
    }
}
