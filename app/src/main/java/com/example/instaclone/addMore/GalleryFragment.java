package com.example.instaclone.addMore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.instaclone.R;
import com.example.instaclone.databinding.FragmentGalleryBinding;
import com.example.instaclone.utils.FilePaths;
import com.example.instaclone.utils.FileSearch;
import com.example.instaclone.utils.GridImageAdapter;
import com.example.instaclone.utils.UniversalImageLoader;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private String append = "file:/";

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

        binding.galleryCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().finish();
            }
        });

        return binding.getRoot();
    }

    private void init() {
        FilePaths filePaths = new FilePaths();
        FileSearch.getDirectoriesPath(filePaths.PICTURE_DIR);
        directories = FileSearch.getDirectoriesPath(filePaths.PICTURE_DIR);
        directories.add(filePaths.CAMERA_DIR);

        ArrayList<String> directoriesNames = new ArrayList<>();
        for (String directory : directories){
            int lastIndex = directory.lastIndexOf("/")+1;// to remove /
            String name = directory.substring(lastIndex);
            directoriesNames.add(name);
        }

        if (getContext()==null)
            return;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, directoriesNames);
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


        if (getActivity() == null)
            return;
        GridImageAdapter gridImageAdapter = new GridImageAdapter(getActivity(), filesPath, R.layout.grid_image_item, append);
        gridView.setAdapter(gridImageAdapter);

        if (filesPath.size() > 0)
            setImage(filesPath.get(0));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < filesPath.size())
                    setImage(filesPath.get(position));
            }
        });

    }

    private void setImage(String url) {
        UniversalImageLoader.setImage(url, binding.galleryImageView, null, append);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
