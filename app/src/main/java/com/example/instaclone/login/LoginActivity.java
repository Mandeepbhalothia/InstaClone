package com.example.instaclone.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instaclone.databinding.ActivityLoginBinding;
import com.example.instaclone.utils.Common;
import com.example.instaclone.utils.FireBaseMethods;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    Common common;
    FireBaseMethods fireBaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        common = new Common(this);
        fireBaseMethods = new FireBaseMethods(this);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable email = binding.emailLoginEt.getText();
                Editable password = binding.passwordLoginEt.getText();

                if (common.isEditableNull(email) || common.isEditableNull(password)) {
                    Toast.makeText(LoginActivity.this, "Null parameter is not accepted", Toast.LENGTH_SHORT).show();
                    return;
                }
                assert email != null;
                assert password != null;
                if (common.isStringNull(email.toString()) || common.isStringNull(password.toString())) {
                    Toast.makeText(LoginActivity.this, "Email and Password fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                fireBaseMethods.signInUser(email.toString(), password.toString(), binding.progressBarLogin);

            }
        });

        binding.signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}
