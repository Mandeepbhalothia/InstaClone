package com.example.instaclone.model;

public class UserAccountDetails {
    private User user;
    private UserSetting userSetting;

    public UserAccountDetails(User user, UserSetting userSetting) {
        this.user = user;
        this.userSetting = userSetting;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserSetting getUserSetting() {
        return userSetting;
    }

    public void setUserSetting(UserSetting userSetting) {
        this.userSetting = userSetting;
    }
}
