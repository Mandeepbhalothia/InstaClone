package com.example.instaclone.model;

public class UserSetting {
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
}
