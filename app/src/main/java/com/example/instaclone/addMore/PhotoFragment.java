package com.example.instaclone.addMore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.instaclone.R;
import com.example.instaclone.databinding.FragmentPhotoBinding;
import com.example.instaclone.profile.AccountsSettingActivity;
import com.example.instaclone.utils.Permissions;

import java.util.Objects;

public class PhotoFragment extends Fragment {


    public PhotoFragment() {
        // Required empty public constructor
    }

    private static final int PHOTO_FRAG = 1;
    private static final int IMAGE_REQUEST = 5;
    private FragmentPhotoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPhotoBinding.inflate(inflater, container, false);

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((AddMoreActivity) Objects.requireNonNull(getActivity())).getCurrentTabNo() == PHOTO_FRAG) {
                    if (((AddMoreActivity) getActivity()).checkPermission(Permissions.cameraPermission)) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, IMAGE_REQUEST);
                    } else {
                        Toast.makeText(getContext(), "Camera Permission Not granted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), AddMoreActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == IMAGE_REQUEST) {
            Log.d("TAG", "onActivityResult: camera opened ");
            if (data != null) {

                if (data.getExtras() != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    if (isRootTask()) {
                        Intent intent = new Intent(getContext(), NextActivity.class);
                        intent.putExtra(getString(R.string.selectedBitmap), bitmap);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), AccountsSettingActivity.class);
                        intent.putExtra(getString(R.string.selectedBitmap), bitmap);
                        intent.putExtra(getString(R.string.return_to_fragment),
                                getString(R.string.edit_profile_fragment));
                        startActivity(intent);
                        if (getActivity() != null)
                            getActivity().finish();
                    }
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private boolean isRootTask() {
        return ((AddMoreActivity) Objects.requireNonNull(getActivity())).getFlag() == 0;
    }
}
