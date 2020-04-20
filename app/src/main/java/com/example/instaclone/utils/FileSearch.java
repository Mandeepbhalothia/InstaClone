package com.example.instaclone.utils;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {

    public static ArrayList<String> getDirectoriesPath(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();

        File file = new File(directory);
        File[] fileArray = file.listFiles();

        if (fileArray != null)
            for (File file1 : fileArray) {
                if (file1.isDirectory())
                    pathArray.add(file1.getAbsolutePath());
            }

        return pathArray;
    }

    public static ArrayList<String> getFilesPath(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();

        File file = new File(directory);
        File[] fileArray = file.listFiles();

        if (fileArray != null)
            for (File file1 : fileArray) {
                if (file1.isFile())
                    pathArray.add(file1.getAbsolutePath());
            }

        return pathArray;
    }

}
