package com.example.instaclone.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instaclone.databinding.ActivitySplashBinding;
import com.example.instaclone.login.LoginActivity;
import com.example.instaclone.utils.FireBaseMethods;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME = 2000;
    ActivitySplashBinding binding;
    FireBaseMethods fireBaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fireBaseMethods = new FireBaseMethods(this);

    }

    /*...............firebase...............*/


    private void checkUser() {
        final FirebaseUser currentUser = fireBaseMethods.getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser == null) {// user is not signed in
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, SPLASH_TIME);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // get and check current user
        checkUser();
    }
}
