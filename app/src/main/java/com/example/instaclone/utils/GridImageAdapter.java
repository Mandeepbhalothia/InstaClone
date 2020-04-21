package com.example.instaclone.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.instaclone.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import static com.example.instaclone.utils.Common.hideProgressBar;
import static com.example.instaclone.utils.Common.showProgressBar;

public class GridImageAdapter extends ArrayAdapter<String> {

    private ArrayList<String> urlList;
    private Context context;
    private LayoutInflater layoutInflater;
    private int layout;
    private String append;

    public GridImageAdapter(@NonNull Context context, ArrayList<String> urlList, int layout, String append) {
        super(context, layout, urlList);
        this.urlList = urlList;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
        this.append = append;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.mProgressBar = convertView.findViewById(R.id.gridImageProgressBar);
            viewHolder.postImage = convertView.findViewById(R.id.gridImageImageView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String imageUrl = urlList.get(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        Log.d("TAG", "getView: "+viewHolder.postImage.getWidth());
        imageLoader.displayImage(append + imageUrl, viewHolder.postImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                showProgressBar(viewHolder.mProgressBar);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                hideProgressBar(viewHolder.mProgressBar);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                hideProgressBar(viewHolder.mProgressBar);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                hideProgressBar(viewHolder.mProgressBar);
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        SquareImage postImage;
        ProgressBar mProgressBar;

    }

}
