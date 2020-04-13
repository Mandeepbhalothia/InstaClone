package com.example.instaclone.model;

public class User {
    private String email;
    private String phone_number;
    private String user_Id;
    private String username;
    private String gender;

    public User(String email, String phone_number, String user_Id, String username, String gender) {
        this.email = email;
        this.phone_number = phone_number;
        this.user_Id = user_Id;
        this.username = username;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
