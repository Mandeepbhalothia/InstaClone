package com.example.instaclone.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instaclone.R;
import com.example.instaclone.databinding.FragmentEditProfileBinding;
import com.example.instaclone.databinding.FragmentPostBinding;
import com.example.instaclone.utils.GridImageAdapter;

import java.util.ArrayList;
import java.util.Objects;


public class PostFragment extends Fragment {

    private static final int NO_OF_COLUMN = 3;

    public PostFragment() {
        // Required empty public constructor
    }


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

        getUrlList();

        return view;
    }

    private void getUrlList() {
        ArrayList<String> urlList = new ArrayList<>();
        urlList.add("https://www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf");
        urlList.add("https://i.redd.it/9bf67ygj710z.jpg");
        urlList.add("https://c1.staticflickr.com/5/4276/34102458063_7be616b993_o.jpg");
        urlList.add("http://i.imgur.com/EwZRpvQ.jpg");
        urlList.add("http://i.imgur.com/JTb2pXP.jpg");
        urlList.add("https://i.redd.it/59kjlxxf720z.jpg");
        urlList.add("https://i.redd.it/pwduhknig00z.jpg");
        urlList.add("https://i.redd.it/clusqsm4oxzy.jpg");
        urlList.add("https://i.redd.it/svqvn7xs420z.jpg");
        urlList.add("http://i.imgur.com/j4AfH6P.jpg");
        urlList.add("https://i.redd.it/89cjkojkl10z.jpg");
        urlList.add("https://i.redd.it/aw7pv8jq4zzy.jpg");
        urlList.add("https://i.redd.it/9bf67ygj710z.jpg");
        urlList.add("https://c1.staticflickr.com/5/4276/34102458063_7be616b993_o.jpg");
        urlList.add("https://i.redd.it/9bf67ygj710z.jpg");
        urlList.add("https://c1.staticflickr.com/5/4276/34102458063_7be616b993_o.jpg");
        urlList.add("https://i.redd.it/9bf67ygj710z.jpg");
        urlList.add("https://c1.staticflickr.com/5/4276/34102458063_7be616b993_o.jpg");
        setUpGridView(urlList);
    }

    private void setUpGridView(ArrayList<String> urlList) {
        int width = getResources().getDisplayMetrics().widthPixels / NO_OF_COLUMN;
        binding.postGridView.setColumnWidth(width);
        GridImageAdapter gridImageAdapter = new GridImageAdapter(Objects.requireNonNull(getContext()), urlList, R.layout.grid_image_item, "");
        binding.postGridView.setAdapter(gridImageAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
