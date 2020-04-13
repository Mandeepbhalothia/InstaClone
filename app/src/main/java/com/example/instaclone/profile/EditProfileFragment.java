package com.example.instaclone.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.instaclone.R;
import com.example.instaclone.databinding.FragmentEditProfileBinding;
import com.example.instaclone.model.User;
import com.example.instaclone.model.UserAccountDetails;
import com.example.instaclone.model.UserSetting;
import com.example.instaclone.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;

public class EditProfileFragment extends Fragment {

    public EditProfileFragment() {
        // Required empty public constructor
    }


    private FragmentEditProfileBinding binding;
    private UserAccountDetails userAccountDetails;
    private DatabaseReference databaseReference;


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
        String phoneNumber = Objects.requireNonNull(binding.phoneNoEt.getText()).toString();
        String gender = Objects.requireNonNull(binding.genderEt.getText()).toString();

        if (userAccountDetails == null)
            return;
        /*check username is update or not*/
        if (userName.equals(userAccountDetails.getUser().getUsername())){ // username not changed
            if (email.equals(userAccountDetails.getUser().getEmail())){// email also not changed
                User user = new User();
                user.setEmail(email);
                user.setPhone_number(phoneNumber);
                user.setUsername(userName);
                user.setGender(gender);
                user.setUser_Id(userAccountDetails.getUser().getUser_Id());

                UserSetting userSetting = new UserSetting();
                userSetting.setDescription(description);
                userSetting.setDisplay_name(displayName);
                userSetting.setUsername(userName);
                userSetting.setWebsite(website);
                userSetting.setFollowers(userAccountDetails.getUserSetting().getFollowers());
                userSetting.setFollowing(userAccountDetails.getUserSetting().getFollowing());
                userSetting.setPosts(userAccountDetails.getUserSetting().getPosts());
                userSetting.setProfile_photo(userAccountDetails.getUserSetting().getProfile_photo());

                databaseReference.child(getString(R.string.user_account_settings)).child(userAccountDetails.getUser().getUser_Id())
                        .setValue(userSetting);
                databaseReference.child(getString(R.string.users)).child(userAccountDetails.getUser().getUser_Id())
                        .setValue(user);
                Toast.makeText(getContext(), R.string.details_updated, Toast.LENGTH_SHORT).show();
                if (getActivity() != null)
                    getActivity().finish();

            }
        }
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

    private void setProfileImage() {
        String imgURL = "www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
        UniversalImageLoader.setImage(imgURL, binding.editProfileUserImageView, null, "https://");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
