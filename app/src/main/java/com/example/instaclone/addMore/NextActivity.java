package com.example.instaclone.addMore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instaclone.R;
import com.example.instaclone.databinding.ActivityNextBinding;
import com.example.instaclone.main.HomeActivity;
import com.example.instaclone.model.Photo;
import com.example.instaclone.utils.FilePaths;
import com.example.instaclone.utils.FireBaseMethods;
import com.example.instaclone.utils.ImageManger;
import com.example.instaclone.utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NextActivity extends AppCompatActivity {

    ActivityNextBinding binding;
    private DatabaseReference dbRef;
    private FirebaseUser currentUser;
    private long totalImagePosted = 0;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private FilePaths filePaths;
    private Intent intent;
    private String selectedImage;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        filePaths = new FilePaths();
        dbRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FireBaseMethods fireBaseMethods = new FireBaseMethods(this);
        currentUser = fireBaseMethods.getCurrentUser();

        countPhotos();

        init();

        showImage();

        binding.nextShareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhotoToStorage(selectedImage, bitmap);
            }
        });
    }

    private void uploadPhotoToStorage(String selectedImage, Bitmap bitmap) {

        FirebaseUser firebaseUser = new FireBaseMethods(this).getCurrentUser();
        if (firebaseUser == null)
            return;
        final StorageReference ref = storageReference.child(filePaths.FIREBASE_PHOTO_PATH + firebaseUser.getUid() + "/" +
                getString(R.string.new_photo) + (totalImagePosted + 1));
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
                        Toast.makeText(NextActivity.this, "Image Uploading Task Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NextActivity.this, "Image Uploading Task Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveImageDataToDatabase(String url) {
        String caption = binding.captionTv.getText().toString();
        String photoId = databaseReference.child("photos").push().getRef().getKey();
        String tags = getTags(caption);

        Photo photo = new Photo(caption, getDateCreated(), url, photoId, currentUser.getUid(), tags, null,null);
        databaseReference.child("photos/"+photoId).setValue(photo);
        assert photoId != null;
        databaseReference.child("user_photos/"+currentUser.getUid()).child(photoId).setValue(photo);

        startActivity(new Intent(this, HomeActivity.class));

    }

    private String getTags(String caption) {
        String tags = "";

        if (caption.indexOf("#") > 0) {
            StringBuilder sb = new StringBuilder();
            boolean isMatched = false;
            for (char c : caption.toCharArray()) {
                if (c == '#') {
                    isMatched = true;
                    sb.append(c);
                } else {
                    if (c == ' ') {
                        isMatched = false;
                    } else {
                        if (isMatched) {
                            sb.append(c);
                        }
                    }
                }
            }
            tags = sb.toString().trim().replace(" ", "");
        }

        return tags;
    }

    private String getDateCreated() {
        String date ;
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyy HH:mm:ss", Locale.getDefault());
        date = format.format(new Date());
        return date;
    }

    private void init() {
        binding.nextBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showImage() {
        intent = getIntent();
        if (intent.hasExtra(getString(R.string.selectedImage))){ // came from gallery
            selectedImage = intent.getStringExtra(getString(R.string.selectedImage));

            UniversalImageLoader.setImage(getIntent().getStringExtra(getString(R.string.selectedImage)),
                    binding.shareIv, null, filePaths.FILE_NAME_PREFIX);
        } else if (intent.hasExtra(getString(R.string.selectedBitmap))) { // came from camera
            bitmap = intent.getParcelableExtra(getString(R.string.selectedBitmap));
            binding.shareIv.setImageBitmap(bitmap);
        }
    }

    private void countPhotos() {

        dbRef.child("user_photos/" + currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    totalImagePosted = dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
