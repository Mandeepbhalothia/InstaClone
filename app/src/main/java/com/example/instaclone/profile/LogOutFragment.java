package com.example.instaclone.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.instaclone.R;
import com.example.instaclone.databinding.FragmentLogOutBinding;
import com.example.instaclone.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LogOutFragment extends Fragment {

    public LogOutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FragmentLogOutBinding binding = FragmentLogOutBinding.inflate(inflater, container, false);

        binding.logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.logOutProgressBar.setVisibility(View.VISIBLE);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    mAuth.signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    binding.logOutProgressBar.setVisibility(View.GONE);
                    startActivity(intent);
                    if (getActivity() != null)
                        getActivity().finish();
                } else {
                    binding.logOutProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }
}
