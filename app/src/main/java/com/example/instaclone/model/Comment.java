package com.example.instaclone.model;

import java.util.HashMap;

public class Comment {

    private String comment;
    private String username;
    private String profilePhotoUrl;
    private String date_created;
    private String user_id;
    private HashMap<String, Like> likes;

    public Comment(String comment, String username,String user_id, String profilePhotoUrl, String date_created, HashMap<String, Like> likes) {
        this.comment = comment;
        this.username = username;
        this.profilePhotoUrl = profilePhotoUrl;
        this.date_created = date_created;
        this.likes = likes;
        this.user_id = user_id;
    }

    public Comment() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public HashMap<String, Like> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, Like> likes) {
        this.likes = likes;
    }
}
