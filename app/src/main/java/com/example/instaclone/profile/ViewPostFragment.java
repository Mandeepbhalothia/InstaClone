package com.example.instaclone.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.instaclone.R;
import com.example.instaclone.model.Photo;
import com.example.instaclone.model.UserSetting;
import com.example.instaclone.utils.FireBaseMethods;
import com.example.instaclone.utils.Heart;
import com.example.instaclone.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPostFragment extends Fragment {


    private long previousClickTime;

    public ViewPostFragment() {
        super();
        setArguments(new Bundle());
    }

    private Photo photo;
    private TextView timeOfPost, postCaptionTv, userNameTv;
    private UserSetting userSetting;
    private CircleImageView userProfileImageView;
    private ImageButton whiteLikeBtn, redLikeBtn;
    private GestureDetector gestureDetector;
    private Heart heart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        timeOfPost = view.findViewById(R.id.postPostDateTv);
        postCaptionTv = view.findViewById(R.id.postCaptionTv);
        userProfileImageView = view.findViewById(R.id.userProfilePhoto);
        userNameTv = view.findViewById(R.id.userNameTv);
        whiteLikeBtn = view.findViewById(R.id.postLikeIB);
        redLikeBtn = view.findViewById(R.id.postRedLikeIB);

        whiteLikeBtn.setVisibility(View.VISIBLE);
        redLikeBtn.setVisibility(View.GONE);

        heart = new Heart(whiteLikeBtn, redLikeBtn);

        ImageView imageView = view.findViewById(R.id.postImageView);
        try {
            photo = getPhotoFromBundle();
            assert photo != null;
            UniversalImageLoader.setImage(photo.getImage_path(), imageView, null, "");

        } catch (NullPointerException e) {
            Log.d("TAG", "onCreateView: NullPointerException");
        }

        gestureDetector = new GestureDetector(getContext(), new GestureClass());

        setLikeToggle();
        getUserDetailsFromDb();

        return view;
    }

    private void setLikeToggle() {

        whiteLikeBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TAG", "onTouch: white ");
                return gestureDetector.onTouchEvent(event);
            }

        });

        redLikeBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TAG", "onTouch: red ");
                return gestureDetector.onTouchEvent(event);
            }
        });

    }

    private class GestureClass extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.d("TAG", "onDoubleTapEvent: ");
            if (!continueDoubleClick())
                heart.toggleLike();
            return true;
        }
    }

    private boolean continueDoubleClick() {
        if (System.currentTimeMillis() - previousClickTime > 300){
            previousClickTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

    private void getUserDetailsFromDb() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = new FireBaseMethods(getContext()).getCurrentUser();
        db.child(getString(R.string.user_account_settings)).child(currentUser.getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            userSetting = dataSnapshot.getValue(UserSetting.class);
                        }
                        setWidgets();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void setWidgets() {
        String datePostTime = getPostDate();
        if (datePostTime.equalsIgnoreCase("0")) {
            timeOfPost.setText("TODAY");
        } else {
            timeOfPost.setText(datePostTime + " DAYS AGO");
        }
        setCaption();
        setUserDetails();

    }

    private void setUserDetails() {
        if (userSetting != null) {
            userNameTv.setText(userSetting.getUsername());
            UniversalImageLoader.setImage(userSetting.getProfile_photo(), userProfileImageView, null, "");
        }
    }

    private void setCaption() {
        postCaptionTv.setText(photo.getCaption());
    }

    private String getPostDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss", Locale.getDefault());
        simpleDateFormat.format(date);
        String postDateString = photo.getDate_created();
        Date postDate;
        String difference = "0";
        try {
            postDate = simpleDateFormat.parse(postDateString);
            assert postDate != null;
            difference = String.valueOf(Math.round((date.getTime() - postDate.getTime()) / 1000 / 60 / 60 / 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return difference;
    }

    private Photo getPhotoFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.photo));
        } else {
            return null;
        }
    }
}
