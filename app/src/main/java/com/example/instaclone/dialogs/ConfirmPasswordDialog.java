package com.example.instaclone.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.instaclone.R;

public class ConfirmPasswordDialog extends DialogFragment {

    private EditText passwordEditText;

    public interface OnConfirmPasswordListener {
        public void onConfirmPassword(String password);
    }

    private OnConfirmPasswordListener confirmPasswordListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_confirm_password, container, false);

        Button confirmBtn = view.findViewById(R.id.confirmDialogBtn);
        Button cancelBtn = view.findViewById(R.id.cancelDialogBtn);
        passwordEditText = view.findViewById(R.id.passwordDialogEt);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDialog() != null)
                    getDialog().cancel();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                if (!password.equals("")) {
                    confirmPasswordListener.onConfirmPassword(password);
                    if (getDialog() != null)
                        getDialog().dismiss();
                } else {
                    Toast.makeText(getContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            // getTargetFragment() to fetch the fragment that is using this dialog else getActivity is used
            confirmPasswordListener = (OnConfirmPasswordListener) getTargetFragment();

        } catch (Exception e) {
            Log.e("ConfirmPasswordDialog", "onAttach: " + e.getMessage());
        }


    }
}
