package com.example.instaclone.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageManger {

    public static Bitmap getBitmap(String url) {
        File file = new File(url);
        FileInputStream fileInputStream = null;
        Bitmap bitmap = null;
        try {
            fileInputStream = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        if (bitmap == null)
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,quality,outputStream);
        return outputStream.toByteArray();
    }

}
