package com.example.instaclone.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.instaclone.R;
import com.example.instaclone.addMore.NextActivity;
import com.example.instaclone.databinding.ActivityAccountsSettingBinding;
import com.example.instaclone.model.Photo;
import com.example.instaclone.utils.FilePaths;
import com.example.instaclone.utils.FireBaseMethods;
import com.example.instaclone.utils.ImageManger;
import com.example.instaclone.utils.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AccountsSettingActivity extends AppCompatActivity {


    private Group mainLayoutGroup;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private FilePaths filePaths;
    private DatabaseReference dbRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts_setting);
        BottomNavigationView bottomNavigationView = findViewById(R.id.accounts_bottom_nav_view);
        ImageButton backButton = findViewById(R.id.accountsBackButton);
        mainLayoutGroup = findViewById(R.id.account_fragmentGroup);
        viewPager = findViewById(R.id.account_setting_viewPager);

        filePaths = new FilePaths();
        dbRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FireBaseMethods fireBaseMethods = new FireBaseMethods(this);
        currentUser = fireBaseMethods.getCurrentUser();

        setUpPagerAdapter();

        // we can directly load edit profile fragment
        checkIntentData();

        bottomNavigationView.setSelectedItemId(R.id.profileMenu);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.accountTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // now we will use add accounts for edit profile purpose
                setViewPager(getString(R.string.edit_profile_fragment));
            }
        });

        findViewById(R.id.logOutTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewPager(getString(R.string.log_out_fragment));
            }
        });

    }

    private void checkIntentData() {
        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.return_to_fragment))) {
            if (intent.getStringExtra(getString(R.string.return_to_fragment)).
                    equalsIgnoreCase(getString(R.string.edit_profile_fragment))) {
                if (intent.hasExtra(getString(R.string.selectedImage))) {

                    String selectedImage = intent.getStringExtra(getString(R.string.selectedImage));
                    uploadPhotoToStorage(selectedImage, null);

                } else if (intent.hasExtra(getString(R.string.selectedBitmap))) {
                    Bitmap bitmap = intent.getParcelableExtra(getString(R.string.selectedBitmap));
                    uploadPhotoToStorage(null, bitmap);
                }
            }
        }
        if (intent.hasExtra(getString(R.string.calling_activity))) {
            setViewPager(intent.getStringExtra(getString(R.string.calling_activity)));
        }
    }


    private void uploadPhotoToStorage(String selectedImage, Bitmap bitmap) {

        FirebaseUser firebaseUser = new FireBaseMethods(this).getCurrentUser();
        if (firebaseUser == null)
            return;
        final StorageReference ref = storageReference.child(filePaths.FIREBASE_PHOTO_PATH + firebaseUser.getUid() + "/" +
                getString(R.string.profile_photo));
        if (bitmap == null) {
            bitmap = ImageManger.getBitmap(selectedImage);
        }
        byte[] bytes = ImageManger.getBytesFromBitmap(bitmap, 100);

        if (bytes == null) {
            Toast.makeText(this, "Image Not Fetched Correctly", Toast.LENGTH_SHORT).show();
            return;
        }

        ref.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // uploaded success
                Log.d("TAG", "onSuccess: upload done");

                // get downloadURL
                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful() && task.isComplete()) {
                            if (task.getResult() != null) {
                                String url = task.getResult().toString();
                                Log.d("TAG", "onComplete: " + url);
                                saveImageDataToDatabase(url);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AccountsSettingActivity.this, "Image Uploading Task Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountsSettingActivity.this, "Image Uploading Task Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveImageDataToDatabase(String url) {
        databaseReference.child(getString(R.string.user_account_settings)).child(currentUser.getUid())
                .child(getString(R.string.profile_photo)).setValue(url);

        // set edit profile fragment back
        setViewPager(getString(R.string.edit_profile_fragment));
    }

    private void setUpPagerAdapter() {
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        sectionsPagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile_fragment));// fragment 0
        sectionsPagerAdapter.addFragment(new LogOutFragment(), getString(R.string.log_out_fragment));// fragment 1
    }

    private void setViewPager(String fragmentName) {
        mainLayoutGroup.setVisibility(View.GONE);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(sectionsPagerAdapter.getFragmentNumber(fragmentName));
    }
}
