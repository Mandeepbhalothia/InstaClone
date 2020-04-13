package com.example.instaclone.addMore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.instaclone.R;
import com.example.instaclone.databinding.ActivityAddMoreBinding;

public class AddMoreActivity extends AppCompatActivity {

    private ActivityAddMoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
