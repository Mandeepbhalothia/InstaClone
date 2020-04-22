package com.example.instaclone.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instaclone.R;
import com.example.instaclone.model.Comment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Comment> commentArrayList;

    public CommentListAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
    }

    @Override
    public int getCount() {
        return commentArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.comment = convertView.findViewById(R.id.commentTv);
            viewHolder.userName = convertView.findViewById(R.id.commentItemUserName);
            viewHolder.userImageView = convertView.findViewById(R.id.commentItemIV);
            viewHolder.like = convertView.findViewById(R.id.commentItemLikesTv);
            viewHolder.reply = convertView.findViewById(R.id.commentItemReplyTv);
            viewHolder.time = convertView.findViewById(R.id.commentItemTimeTv);
            viewHolder.whiteHeart = convertView.findViewById(R.id.commentItemLikeIV);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Comment comment = commentArrayList.get(position);

        if (position == 0){
            viewHolder.reply.setVisibility(View.GONE);
            viewHolder.like.setVisibility(View.GONE);
            viewHolder.whiteHeart.setVisibility(View.GONE);
        } else {
            viewHolder.reply.setVisibility(View.VISIBLE);
            viewHolder.like.setVisibility(View.VISIBLE);
            viewHolder.whiteHeart.setVisibility(View.VISIBLE);
        }

        viewHolder.comment.setText(comment.getComment());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(comment.getProfilePhotoUrl(), viewHolder.userImageView);

        String timeStamp = getPostDate(comment);
        if (!timeStamp.equalsIgnoreCase("0")) {
            viewHolder.time.setText(timeStamp + " d");
        } else {
            viewHolder.time.setText("Today");
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView userName, comment, time, like, reply;
        CircleImageView userImageView;
        ImageView whiteHeart;
    }

    private String getPostDate(Comment comment) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss", Locale.getDefault());
        simpleDateFormat.format(date);
        String postDateString = comment.getDate_created();
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
}