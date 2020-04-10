package com.example.instaclone.utils;

import android.text.Editable;
import android.view.View;
import android.widget.ProgressBar;

public class Common {

    public static void showProgressBar(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public static void hideProgressBar(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }


    public boolean isStringNull(String string) {
        return string.equals("");
    }

    public boolean isEditableNull(Editable editable) {
        return editable == null;
    }


}
