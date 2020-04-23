package com.example.instaclone.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserSetting implements Parcelable {
    private String website;
    private String description;
    private String profile_photo;
    private String username;
    private String posts;
    private String followers;
    private String following;
    private String display_name;

    public UserSetting() {
    }

    public UserSetting(String website, String description, String profile_photo, String username,
                       String posts, String followers, String following, String display_name) {
        this.website = website;
        this.description = description;
        this.profile_photo = profile_photo;
        this.username = username;
        this.posts = posts;
        this.followers = followers;
        this.following = following;
        this.display_name = display_name;
    }

    protected UserSetting(Parcel in) {
        website = in.readString();
        description = in.readString();
        profile_photo = in.readString();
        username = in.readString();
        posts = in.readString();
        followers = in.readString();
        following = in.readString();
        display_name = in.readString();
    }

    public static final Creator<UserSetting> CREATOR = new Creator<UserSetting>() {
        @Override
        public UserSetting createFromParcel(Parcel in) {
            return new UserSetting(in);
        }

        @Override
        public UserSetting[] newArray(int size) {
            return new UserSetting[size];
        }
    };

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(website);
        dest.writeString(description);
        dest.writeString(profile_photo);
        dest.writeString(username);
        dest.writeString(posts);
        dest.writeString(followers);
        dest.writeString(following);
        dest.writeString(display_name);
    }
}
