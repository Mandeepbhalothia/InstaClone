package com.example.instaclone.utils;

import android.os.Environment;

public class FilePaths {
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public String CAMERA_DIR = ROOT_DIR + "/DCIM/Camera";
    public String PICTURE_DIR = ROOT_DIR + "/Pictures";
}
