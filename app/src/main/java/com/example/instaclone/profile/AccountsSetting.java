package com.example.instaclone.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.instaclone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountsSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_setting);
        BottomNavigationView bottomNavigationView = findViewById(R.id.accounts_bottom_nav_view);
        ImageButton backButton = findViewById(R.id.accountsBackButton);



        bottomNavigationView.setSelectedItemId(R.id.profileMenu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
