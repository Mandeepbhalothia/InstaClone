package com.example.instaclone.utils;

import android.os.Environment;

public class FilePaths {
    private String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public String CAMERA_DIR = ROOT_DIR + "/DCIM/Camera";
    public String PICTURE_DIR = ROOT_DIR + "/Pictures";
    public String FIREBASE_PHOTO_PATH = "photos/users/";
    public String FILE_NAME_PREFIX = "file:/";
}
