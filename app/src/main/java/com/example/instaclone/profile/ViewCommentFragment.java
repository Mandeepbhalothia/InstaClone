package com.example.instaclone.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.instaclone.R;
import com.example.instaclone.model.Comment;
import com.example.instaclone.model.Photo;
import com.example.instaclone.model.UserSetting;
import com.example.instaclone.utils.CommentListAdapter;
import com.example.instaclone.utils.FireBaseMethods;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewCommentFragment extends Fragment {


    private boolean scrollToLastComment;

    public ViewCommentFragment() {
        super();
        setArguments(new Bundle());
        // Required empty public constructor
    }

    private Photo photo;
    private ArrayList<Comment> commentArrayList = new ArrayList<>();
    private CommentListAdapter commentListAdapter;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private ListView commentListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = new FireBaseMethods(getContext()).getCurrentUser();

        try {
            photo = getPhotoFromBundle();
            ImageLoader.getInstance().displayImage(photo.getImage_path(), (ImageView) view.findViewById(R.id.commentCurrentUserImage));
        } catch (NullPointerException e) {
            Log.e("TAG", "onCreateView: NullPointerException");
        }

        commentListView = view.findViewById(R.id.commentListView);
        commentListAdapter = new CommentListAdapter(getContext(), commentArrayList);
        commentListView.setAdapter(commentListAdapter);

        view.findViewById(R.id.commentBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        final EditText commentEt = view.findViewById(R.id.commentEt);

        view.findViewById(R.id.commentPostTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEt.getText().toString().trim();
                if (!comment.equalsIgnoreCase("")) {
                    scrollToLastComment = true;
                    postComment(comment);
                    commentEt.getText().clear();
                    closeKeyBoard();
                } else {
                    Toast.makeText(getContext(), "Can't post empty comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getUsersDetails();

        getComments();

        return view;
    }

    private void postComment(String comm) {

        Comment comment = new Comment();
        comment.setComment(comm);
        comment.setDate_created(getDateCreated());
        comment.setUser_id(currentUser.getUid());

        String ref = databaseReference.push().getRef().getKey();
        assert ref != null;
        databaseReference.child(getString(R.string.user_photos)).child(currentUser.getUid()).child(photo.getPhoto_id())
                .child(getString(R.string.field_comment)).child(ref).setValue(comment);
        databaseReference.child(getString(R.string.photos)).child(photo.getPhoto_id())
                .child(getString(R.string.field_comment)).child(ref).setValue(comment);
    }

    private void closeKeyBoard() {
        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            assert view != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private String getDateCreated() {
        String date;
        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyy HH:mm:ss", Locale.getDefault());
        date = format.format(new Date());
        return date;
    }

    private void getUsersDetails() {

        databaseReference.child(getString(R.string.user_account_settings)).child(currentUser.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserSetting userSetting = new UserSetting();
                        if (dataSnapshot.getValue() != null) {
                            userSetting = dataSnapshot.getValue(UserSetting.class);
                        }
                        assert userSetting != null;
                        commentArrayList.add(new Comment(photo.getCaption(), userSetting.getUsername(), currentUser.getUid()
                                , userSetting.getProfile_photo(), photo.getDate_created(), null));
                        commentListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private Photo getPhotoFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.photo));
        } else {
            return null;
        }
    }

    private void getComments() {
        databaseReference.child(getString(R.string.photos)).child(photo.getPhoto_id()).child(getString(R.string.field_comment))
                .addChildEventListener(childEventListener);
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Log.d("TAG", "onChildAdded: " + dataSnapshot);
            if (dataSnapshot.getValue() != null) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                commentArrayList.add(comment);
                commentListAdapter.notifyDataSetChanged();
                if (scrollToLastComment) {
                    commentListView.smoothScrollToPosition(commentArrayList.size() - 1);
                    scrollToLastComment = false;
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        databaseReference.removeEventListener(childEventListener);
    }
}
