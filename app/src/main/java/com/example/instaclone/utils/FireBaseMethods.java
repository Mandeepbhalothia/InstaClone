package com.example.instaclone.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.instaclone.R;
import com.example.instaclone.main.MainActivity;
import com.example.instaclone.model.User;
import com.example.instaclone.model.UserSetting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.example.instaclone.utils.Common.hideProgressBar;
import static com.example.instaclone.utils.Common.showProgressBar;

public class FireBaseMethods {
    private Context context;
    private FirebaseAuth mAuth;

    public FireBaseMethods(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signInUser(String email, String password, final ProgressBar progressBar) {

        showProgressBar(progressBar);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser!=null) {
                        if (currentUser.isEmailVerified()) {
                            hideProgressBar(progressBar);
                            Toast.makeText(context, context.getString(R.string.auth_success), Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, MainActivity.class));
                            ((Activity) context).finish();
                        } else {
                            mAuth.signOut();
                            hideProgressBar(progressBar);
                            Toast.makeText(context, context.getString(R.string.check_verification_mail), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        hideProgressBar(progressBar);
                        Toast.makeText(context, context.getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hideProgressBar(progressBar);
                    Toast.makeText(context, context.getString(R.string.auth_failed)+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registerUser(final String email, final String password, final String username, final String fullName,
                             final String phoneNumber,final ProgressBar progressBar, final DatabaseReference dbRef) {
        showProgressBar(progressBar);
        // first check userName is already available or not
        dbRef.child(context.getString(R.string.users)).orderByChild(context.getString(R.string.username)).equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.getValue() != null) {
                                                            hideProgressBar(progressBar);
                                                            Toast.makeText(context, "User name already taken by other user",
                                                                    Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                        proceedRegister(email, password, username, fullName,phoneNumber, progressBar, dbRef);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                }
                );
    }

    private void proceedRegister(final String email, String password, final String username, final String fullName, final String phoneNumber,
                                 final ProgressBar progressBar, final DatabaseReference dbRef) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        hideProgressBar(progressBar);
                        // save to db
                        final User user = new User(email, phoneNumber, currentUser.getUid(), username );
                        final UserSetting userSetting = new UserSetting("","","",username,"0","0","0",fullName);
                        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    saveDataToDb(user, dbRef, userSetting);
                                    Toast.makeText(context, context.getString(R.string.sending_email_verification_mail), Toast.LENGTH_LONG).show();
                                    mAuth.signOut();
                                    ((Activity) context).finish();
                                } else {
                                    currentUser.delete();
                                    hideProgressBar(progressBar);
                                    Toast.makeText(context, "" + context.getString(R.string.auth_failed) + " : " + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        hideProgressBar(progressBar);
                        Toast.makeText(context, "" + context.getString(R.string.auth_failed) + " : " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hideProgressBar(progressBar);
                    Toast.makeText(context, "" + context.getString(R.string.auth_failed) + " : " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveDataToDb(User user, DatabaseReference dbRef, UserSetting userSetting) {
        dbRef.child(context.getString(R.string.users)).child(user.getUser_Id()).setValue(user);
        dbRef.child(context.getString(R.string.user_account_settings)).child(user.getUser_Id()).setValue(userSetting);
    }
}
