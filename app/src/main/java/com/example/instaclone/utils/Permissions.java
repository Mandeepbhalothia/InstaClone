package com.example.instaclone.utils;

import android.Manifest;

public class Permissions {

    public static String [] permissionArray = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static String cameraPermission = Manifest.permission.CAMERA;

}
