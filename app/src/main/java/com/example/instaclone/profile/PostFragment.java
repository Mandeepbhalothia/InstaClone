package com.example.instaclone.profile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
    }

    public interface OnGridItemClickListener {
        public void onGridItemClicked(Photo photo);
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

        setUpGridView();

        return view;
    }

    private void setUpGridView() {
        FirebaseUser currentUser = new FireBaseMethods(getContext()).getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(getString(R.string.user_photos)).child(currentUser.getUid())
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
