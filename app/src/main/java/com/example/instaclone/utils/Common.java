package com.example.instaclone.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.instaclone.R;
import com.example.instaclone.addMore.AddMoreActivity;
import com.example.instaclone.likes.LikesActivity;
import com.example.instaclone.main.HomeActivity;
import com.example.instaclone.profile.ProfileActivity;
import com.example.instaclone.search.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Common {

    private Context context;
    private MenuItem lasTSelectedMenu;

    public Common(Context context) {
        this.context = context;
    }

    static void showProgressBar(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    static void hideProgressBar(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }


    public boolean isStringNull(String string) {
        return string.equals("");
    }

    public boolean isEditableNull(Editable editable) {
        return editable == null;
    }

    public void initBottomNavListener(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent();
                switch (item.getItemId()) {
                    case R.id.homeMenu:
                        intent = new Intent(context, HomeActivity.class);
                        break;
                    case R.id.searchMenu:
                        intent = new Intent(context, SearchActivity.class);
                        break;
                    case R.id.addMenu:
                        intent = new Intent(context, AddMoreActivity.class);
                        break;
                    case R.id.likeMenu:
                        intent = new Intent(context, LikesActivity.class);
                        break;
                    case R.id.profileMenu:
                        intent = new Intent(context, ProfileActivity.class);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
//                ((Activity)context).finish();

                return true;
            }
        });
    }

}
