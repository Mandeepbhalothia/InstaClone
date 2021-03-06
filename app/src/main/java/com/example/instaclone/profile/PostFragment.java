package com.example.instaclone.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.instaclone.R;
import com.example.instaclone.databinding.FragmentPostBinding;
import com.example.instaclone.model.Photo;
import com.example.instaclone.utils.FireBaseMethods;
import com.example.instaclone.utils.GridImageAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class PostFragment extends Fragment {

    private static final int NO_OF_COLUMN = 3;

    public PostFragment() {
        // Required empty public constructor
        super();
        setArguments(new Bundle());
    }

    public interface OnGridItemClickListener {
        void onGridItemClicked(Photo photo);
    }

    private OnGridItemClickListener onGridItemClickListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private FragmentPostBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        init();

        return view;
    }

    private void init() {
        Bundle bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(getString(R.string.username))) {
            String userName = bundle.getString(getString(R.string.username));
            getUserId(userName);
        } else {
            getCurrentUserId();
        }
    }

    private void getUserId(String userName) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(getString(R.string.users)).orderByChild(getString(R.string.username))
                .equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    String userID = "";
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        userID = dataSnapshot1.getKey();
                    }
                    setUpGridView(userID);
                } else {
                    Toast.makeText(getContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getCurrentUserId() {
        FirebaseUser currentUser = new FireBaseMethods(getContext()).getCurrentUser();
        setUpGridView(currentUser.getUid());
    }

    private void setUpGridView(String userId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(getString(R.string.user_photos)).child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            final ArrayList<String> urlList = new ArrayList<>();
                            final ArrayList<Photo> photoList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Photo photo = dataSnapshot1.getValue(Photo.class);
                                photoList.add(photo);
                                if (photo != null) {
                                    urlList.add(photo.getImage_path());
                                }
                            }
                            GridView gridView = binding.postGridView;
                            int width = getResources().getDisplayMetrics().widthPixels / NO_OF_COLUMN;
                            gridView.setColumnWidth(width);
                            Log.d("TAG", "onDataChange: " + getResources().getDisplayMetrics().widthPixels + " " + width);
                            GridImageAdapter gridImageAdapter = new GridImageAdapter(Objects.requireNonNull(getContext()),
                                    urlList, R.layout.grid_image_item, "");
                            gridView.setAdapter(gridImageAdapter);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position >= 0 && position < photoList.size())
                                        onGridItemClickListener.onGridItemClicked(photoList.get(position));
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onGridItemClickListener = (OnGridItemClickListener) getActivity();
        } catch (ClassCastException e) {
            Log.e("TAG", "onAttach: classCastException" + e);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
