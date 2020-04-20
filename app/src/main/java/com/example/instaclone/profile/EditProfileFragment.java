package com.example.instaclone.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.instaclone.R;
import com.example.instaclone.databinding.FragmentEditProfileBinding;
import com.example.instaclone.dialogs.ConfirmPasswordDialog;
import com.example.instaclone.model.User;
import com.example.instaclone.model.UserAccountDetails;
import com.example.instaclone.model.UserSetting;
import com.example.instaclone.utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.OnConfirmPasswordListener {

    private String confirmedPassword;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    private FragmentEditProfileBinding binding;
    private UserAccountDetails userAccountDetails;
    private DatabaseReference databaseReference;
    private User user;
    private UserSetting userSetting;
    private String newEmail;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment by using view binding
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

//        setProfileImage();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getUserAccountDetails(databaseReference);

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().finish();
            }
        });

        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserDetails();
            }
        });

        return view;
    }

    private void updateUserDetails() {
        String displayName = Objects.requireNonNull(binding.nameEt.getText()).toString();
        String userName = Objects.requireNonNull(binding.userNameEt.getText()).toString();
        String description = Objects.requireNonNull(binding.bioEt.getText()).toString();
        String website = Objects.requireNonNull(binding.websiteEt.getText()).toString();
        String email = Objects.requireNonNull(binding.emailEt.getText()).toString();
        newEmail = email;
        String phoneNumber = Objects.requireNonNull(binding.phoneNoEt.getText()).toString();
        String gender = Objects.requireNonNull(binding.genderEt.getText()).toString();

        if (userAccountDetails == null)
            return;

        user = new User();
        user.setEmail(email);
        user.setPhone_number(phoneNumber);
        user.setUsername(userName);
        user.setGender(gender);
        user.setUser_Id(userAccountDetails.getUser().getUser_Id());

        userSetting = new UserSetting();
        userSetting.setDescription(description);
        userSetting.setDisplay_name(displayName);
        userSetting.setUsername(userName);
        userSetting.setWebsite(website);
        userSetting.setFollowers(userAccountDetails.getUserSetting().getFollowers());
        userSetting.setFollowing(userAccountDetails.getUserSetting().getFollowing());
        userSetting.setPosts(userAccountDetails.getUserSetting().getPosts());
        userSetting.setProfile_photo(userAccountDetails.getUserSetting().getProfile_photo());

        /*check username is updated or not*/
        if (userName.equals(userAccountDetails.getUser().getUsername())) { // username not changed
            if (email.equals(userAccountDetails.getUser().getEmail())) {// email also not changed
                /*update db*/
                updateDb(userSetting, user);
            } else {// check email is already field by some one or not
                ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(), getString(R.string.confirm_dialog));
                dialog.setTargetFragment(EditProfileFragment.this, 1);
//                checkEmail(userSetting, user, email);
            }
        } else {// check user name is already used by someone else or not
            checkUserName(userSetting, user, userName, email);
        }
    }

    private void checkUserName(final UserSetting userSetting, final User user, final String userName, final String email) {
        databaseReference.child(getString(R.string.users)).orderByChild("username").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Toast.makeText(getContext(), "UserName is already used", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (email.equals(userAccountDetails.getUser().getEmail())) {// email also not changed
                            updateDb(userSetting, user);
                        } else {// check email is already exist or not
                            ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                            assert getFragmentManager() != null;
                            dialog.show(getFragmentManager(), getString(R.string.confirm_dialog));
                            dialog.setTargetFragment(EditProfileFragment.this, 1);
//                            checkEmail(userSetting, user, email);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updateDb(UserSetting userSetting, User user) {
        databaseReference.child(getString(R.string.user_account_settings)).child(userAccountDetails.getUser().getUser_Id())
                .setValue(userSetting);
        databaseReference.child(getString(R.string.users)).child(userAccountDetails.getUser().getUser_Id())
                .setValue(user);
        Toast.makeText(getContext(), R.string.details_updated, Toast.LENGTH_SHORT).show();
        if (getActivity() != null)
            getActivity().finish();
    }

    private void checkEmail(final UserSetting userSetting, final User user, String email) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(
                new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        try {
                            if (task.getResult() != null && task.getResult().getSignInMethods() != null)
                                if (task.getResult().getSignInMethods().size() == 0) {
                                    // email not exists
                                    updateDb(userSetting, user);
                                } else {
                                    Toast.makeText(getContext(), "Email id is already used", Toast.LENGTH_SHORT).show();
                                }
                        } catch (NullPointerException e) {
                            Log.e("TAG", "onComplete: Checking email nullPointer exception");
                        }
                    }
                }
        );

        /*databaseReference.child(getString(R.string.users)).orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            Toast.makeText(getContext(), "Email id is already used", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        updateDb(userSetting, user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
    }

    /*get user details as well as user accounts setting details*/
    private void getUserAccountDetails(final DatabaseReference databaseReference) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            binding.editProfileProgressbar.setVisibility(View.VISIBLE);
            databaseReference.child(getString(R.string.user_account_settings)).child(currentUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                final UserSetting userSetting = dataSnapshot.getValue(UserSetting.class);
                                databaseReference.child(getString(R.string.users)).child(currentUser.getUid())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.getValue() != null) {
                                                    User user = dataSnapshot.getValue(User.class);
                                                    userAccountDetails = new UserAccountDetails(user, userSetting);
                                                    updateUiWidgets(userAccountDetails);
                                                } else {
                                                    Toast.makeText(getContext(), R.string.data_issue, Toast.LENGTH_SHORT).show();
                                                }
                                                binding.editProfileProgressbar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            } else {
                                binding.editProfileProgressbar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), R.string.data_issue, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void updateUiWidgets(UserAccountDetails userAccountDetails) {
        User user = userAccountDetails.getUser();
        UserSetting userSetting = userAccountDetails.getUserSetting();

        UniversalImageLoader.setImage(userSetting.getProfile_photo(), binding.editProfileUserImageView, null, "");
        binding.bioEt.setText(userSetting.getDescription());
        binding.nameEt.setText(userSetting.getDisplay_name());
        binding.userNameEt.setText(userSetting.getUsername());
        binding.websiteEt.setText(userSetting.getWebsite());
        binding.emailEt.setText(user.getEmail());
        binding.phoneNoEt.setText(user.getPhone_number());
        binding.genderEt.setText(user.getGender());
    }

    /*private void setProfileImage() {
        String imgURL = "www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
        UniversalImageLoader.setImage(imgURL, binding.editProfileUserImageView, null, "https://");
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onConfirmPassword(String password) {
        confirmedPassword = password;

        // reAuthenticate user
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null)
            return;
        if (currentUser.getEmail() == null)
            return;
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(),
                password);

        auth.getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    // check email is already used or not
                    checkEmail(userSetting, user, newEmail);

                } else {
                    Toast.makeText(getActivity(), "Error in ReAuthentication " + task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
