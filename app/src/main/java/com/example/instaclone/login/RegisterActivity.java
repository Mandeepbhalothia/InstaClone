package com.example.instaclone.login;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instaclone.databinding.ActivityRegisterBinding;
import com.example.instaclone.utils.Common;
import com.example.instaclone.utils.FireBaseMethods;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FireBaseMethods fireBaseMethods;
    Common common;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        common = new Common(this);
        fireBaseMethods = new FireBaseMethods(this);

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable email = binding.emailRegisterEt.getText();
                Editable password = binding.passwordRegisterEt.getText();
                Editable userName = binding.userNameRegisterEt.getText();
                Editable fullName = binding.fullNameRegisterEt.getText();

                if (common.isEditableNull(email) || common.isEditableNull(password) || common.isEditableNull(userName) || common.isEditableNull(fullName)) {
                    Toast.makeText(RegisterActivity.this, "Null parameter is not accepted", Toast.LENGTH_SHORT).show();
                    return;
                }
                assert email != null;
                assert password != null;
                assert userName != null;
                assert fullName != null;
                if (common.isStringNull(email.toString()) || common.isStringNull(password.toString()) || common.isStringNull(userName.toString())
                        || common.isStringNull(fullName.toString())) {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbRef = FirebaseDatabase.getInstance().getReference();
                fireBaseMethods.registerUser(email.toString(), password.toString(), userName.toString(), fullName.toString(), "","",
                        binding.progressBarRegister, dbRef);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
