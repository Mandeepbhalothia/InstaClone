package com.example.instaclone.profile;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.instaclone.model.Like;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPostFragment extends Fragment {


    private long previousClickTime;
    private String likedPostId, currentUserName;

    public interface OnCommentThreadClickListener {
        void onCommentThreadClicked(Photo photo);
    }

    private OnCommentThreadClickListener onCommentThreadClickListener;

    public ViewPostFragment() {
        super();
        setArguments(new Bundle());
    }

    private DatabaseReference db;
    private Photo photo;
    private TextView timeOfPost, postCaptionTv, userNameTv, likeTv;
    private UserSetting userSetting;
    private CircleImageView userProfileImageView;
    private ImageButton whiteLikeBtn, redLikeBtn;
    private GestureDetector gestureDetector;
    private Heart heart;
    private ArrayList<String> likedUserNameList = new ArrayList<>();
    private FirebaseUser currentUser;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        db = FirebaseDatabase.getInstance().getReference();
        currentUser = new FireBaseMethods(getContext()).getCurrentUser();

        timeOfPost = view.findViewById(R.id.postPostDateTv);
        postCaptionTv = view.findViewById(R.id.postCaptionTv);
        userProfileImageView = view.findViewById(R.id.userProfilePhoto);
        userNameTv = view.findViewById(R.id.userNameTv);
        whiteLikeBtn = view.findViewById(R.id.postLikeIB);
        redLikeBtn = view.findViewById(R.id.postRedLikeIB);
        likeTv = view.findViewById(R.id.postLikeTv);
        TextView commentTv = view.findViewById(R.id.postViewCommentTv);

        whiteLikeBtn.setVisibility(View.VISIBLE);
        redLikeBtn.setVisibility(View.GONE);

        heart = new Heart(whiteLikeBtn, redLikeBtn);

        ImageView imageView = view.findViewById(R.id.postImageView);
        try {
            photo = getPhotoFromBundle();
            assert photo != null;
            UniversalImageLoader.setImage(photo.getImage_path(), imageView, null, "");
            getLikes();

            if (photo.getComments() != null && photo.getComments().size() > 0){
                commentTv.setText("View all "+photo.getComments().size()+" comments");
            } else {
                commentTv.setText("");
            }

        } catch (NullPointerException e) {
            Log.e("TAG", "onCreateView: NullPointerException");
        }

        gestureDetector = new GestureDetector(getContext(), new GestureClass());

        setLikeToggle();
        getUserDetailsFromDb();

        view.findViewById(R.id.viewPostBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        view.findViewById(R.id.postCommentIB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommentThreadClickListener.onCommentThreadClicked(photo);
            }
        });

        commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommentThreadClickListener.onCommentThreadClicked(photo);
            }
        });


        return view;
    }

    private void getLikes() {
        HashMap<String, Like> likeHashMap = photo.getLikes();
        if (likeHashMap != null) {
            ArrayList<String> likedUserIdList = new ArrayList<>();
            ArrayList<String> keyList = new ArrayList<>();
            for (Map.Entry<String, Like> entry : likeHashMap.entrySet()) {
                String key = entry.getKey();
                Like like = entry.getValue();
                keyList.add(key);
                likedUserIdList.add(like.getUser_id());
            }

            // check user self liked or not
            if (likedUserIdList.contains(currentUser.getUid())) {
                likedPostId = keyList.get(likedUserIdList.indexOf(currentUser.getUid()));
                likedBySelf(true);
            }
            getLikedUserName(likedUserIdList, 0);
        }
    }

    /*this is not implemented in loop because fb db is async*/
    private void getLikedUserName(final ArrayList<String> likedUserIdList, final int startIndex) {
        final int[] currentIndex = {startIndex};
        if (startIndex >= likedUserIdList.size()) {
            // all user completed now set likes
            setLikes();
            return;
        }
        db.child(getString(R.string.users)).child(likedUserIdList.get(startIndex)).child("username").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            likedUserNameList.add(dataSnapshot.getValue().toString());
                        }
                        currentIndex[0]++;
                        getLikedUserName(likedUserIdList, currentIndex[0]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setLikes() {
        String likeString = getFormattedLikeString();
        likeTv.setText(likeString);
    }

    private void likedBySelf(boolean isLiked) {
        if (isLiked) {
            whiteLikeBtn.setVisibility(View.GONE);
            redLikeBtn.setVisibility(View.VISIBLE);
        } else {
            whiteLikeBtn.setVisibility(View.VISIBLE);
            redLikeBtn.setVisibility(View.GONE);
        }
    }

    private String getFormattedLikeString() {
        String likeString = "";
        int users = likedUserNameList.size();

        if (users == 1) {
            likeString = "Liked by " + likedUserNameList.get(0);
        } else if (users == 2) {
            likeString = "Liked by " + likedUserNameList.get(0)
                    + " and " + likedUserNameList.get(1);
        } else if (users == 3) {
            likeString = "Liked by " + likedUserNameList.get(0)
                    + ", " + likedUserNameList.get(1)
                    + " and " + likedUserNameList.get(2);
        } else if (users > 3) {
            likeString = "Liked by " + likedUserNameList.get(0)
                    + ", " + likedUserNameList.get(1)
                    + ", " + likedUserNameList.get(2)
                    + " and " + (likedUserNameList.size() - 3) + " others";
        }

        return likeString;
    }

    private void setLikeToggle() {

        whiteLikeBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }

        });

        redLikeBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
            if (!continueDoubleClick()) {
                heart.toggleLike();
                doubleClicked();
            }
            return true;
        }
    }

    private void doubleClicked() {
        if (whiteLikeBtn.getVisibility() == View.VISIBLE) { // disLiked by self

            // remove data from db
            db.child(getString(R.string.user_photos)).child(photo.getUser_id()).child(photo.getPhoto_id())
                    .child("likes").child(likedPostId).removeValue();
            db.child(getString(R.string.photos)).child(photo.getPhoto_id()).child("likes").child(likedPostId).removeValue();

            // update like by self
            likedBySelf(false);

            // update like string
            likedUserNameList.remove(currentUserName);
            setLikes();

        } else {// liked by self

            // add data to db
            String photoKey = db.child(getString(R.string.photos)).push().getRef().getKey();
            assert photoKey != null;
            db.child(getString(R.string.user_photos)).child(photo.getUser_id()).child(photo.getPhoto_id())
                    .child("likes").child(photoKey).child("user_id").setValue(currentUser.getUid());
            db.child(getString(R.string.photos)).child(photo.getPhoto_id())
                    .child("likes").child(photoKey).child("user_id").setValue(currentUser.getUid());

            // update like by self
            likedBySelf(true);

            // update like string
            likedUserNameList.add(0, currentUserName);
            setLikes();

        }
    }

    private boolean continueDoubleClick() {
        if (System.currentTimeMillis() - previousClickTime > 300) {
            previousClickTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

    private void getUserDetailsFromDb() {
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
            currentUserName = userSetting.getUsername();
            userNameTv.setText(currentUserName);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onCommentThreadClickListener = (OnCommentThreadClickListener) getActivity();
        } catch (ClassCastException e) {
            Log.e("TAG", "onAttach:ClassCastException ");
        }

    }
}
