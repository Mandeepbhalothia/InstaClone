package com.example.instaclone.addMore;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.instaclone.R;
import com.example.instaclone.databinding.FragmentEditProfileBinding;
import com.example.instaclone.databinding.FragmentGalleryBinding;
import com.example.instaclone.utils.FilePaths;
import com.example.instaclone.utils.FileSearch;
import com.example.instaclone.utils.GridImageAdapter;
import com.example.instaclone.utils.Permissions;
import com.example.instaclone.utils.UniversalImageLoader;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private static final int GALLERY_FRAG = 0;
    private static final int PHOTO_FRAG = 1;
    private static final int IMAGE_REQUEST = 5;
    private String append = "files:/";

    public GalleryFragment() {
        // Required empty public constructor
    }

    private FragmentGalleryBinding binding;
    private ArrayList<String> directories = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGalleryBinding.inflate(inflater, container, false);

        init();


        return binding.getRoot();
    }

    private void init() {
        FilePaths filePaths = new FilePaths();
        FileSearch.getDirectoriesPath(filePaths.PICTURE_DIR);
        directories = FileSearch.getDirectoriesPath(filePaths.PICTURE_DIR);
        directories.add(filePaths.CAMERA_DIR);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, directories);
        Spinner spinner = binding.gallerySpinner;
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < directories.size())
                    setUpGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setUpGridView(String directories) {
        final ArrayList<String> filesPath = FileSearch.getFilesPath(directories);
        GridView gridView = binding.galleryGridView;
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int columnWidth = gridWidth / 4;
        gridView.setColumnWidth(columnWidth);


        GridImageAdapter gridImageAdapter = new GridImageAdapter(getActivity(), filesPath, R.layout.grid_image_item, append);
        gridView.setAdapter(gridImageAdapter);

        setImage(filesPath.get(0));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < filesPath.size())
                    setUpGridView(filesPath.get(position));
            }
        });

    }

    private void setImage(String url){
        UniversalImageLoader.setImage(url, binding.galleryImageView, null, append);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
