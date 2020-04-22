package com.example.instaclone.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.instaclone.R;
import com.example.instaclone.model.UserSetting;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UserSetting> userSetting;

    public SearchAdapter(Context context, ArrayList<UserSetting> userSetting) {
        this.context = context;
        this.userSetting = userSetting;
    }

    @Override
    public int getCount() {
        return userSetting.size();
    }

    @Override
    public Object getItem(int position) {
        return userSetting;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search,parent, false);
            viewHolder = new ViewHolder();
            viewHolder.userName = convertView.findViewById(R.id.searchItemUserName);
            viewHolder.userEmail = convertView.findViewById(R.id.searchItemEmail);
            viewHolder.userImage = convertView.findViewById(R.id.searchItemItemIV);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.userName.setText(userSetting.get(position).getDisplay_name());
        viewHolder.userEmail.setText(userSetting.get(position).getUsername());
        ImageLoader.getInstance().displayImage(userSetting.get(position).getProfile_photo(),viewHolder.userImage);


        return convertView;
    }

    private static class ViewHolder{
        TextView userName, userEmail;
        CircleImageView userImage;
    }
}
