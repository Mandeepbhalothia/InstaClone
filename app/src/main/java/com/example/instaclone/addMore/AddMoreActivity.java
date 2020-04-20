package com.example.instaclone.addMore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.instaclone.R;
import com.example.instaclone.databinding.ActivityAddMoreBinding;
import com.example.instaclone.utils.Permissions;
import com.example.instaclone.utils.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class AddMoreActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 7;
    public static final int GALLERY_FRAG = 0;
    public static final int PHOTO_FRAG = 1;
    private ActivityAddMoreBinding binding;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (checkPermissionArray(Permissions.permissionArray)){
            setUpViewPager();
        } else {
            verifyPermissions(Permissions.permissionArray);
        }

    }

    public int getCurrentTabNo(){
        return viewPager.getCurrentItem();
    }

    private void setUpViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new GalleryFragment(),getString(R.string.gallery));
        adapter.addFragment(new PhotoFragment(),getString(R.string.photo));
        viewPager = binding.viewPagerLayout.viewPager;
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(GALLERY_FRAG);
        TabLayout tabLayout = binding.addMoreTabLayout;
        tabLayout.setupWithViewPager(viewPager,true);
        Objects.requireNonNull(tabLayout.getTabAt(GALLERY_FRAG)).setText(getString(R.string.gallery));
        Objects.requireNonNull(tabLayout.getTabAt(PHOTO_FRAG)).setText(getString(R.string.photo));

    }

    private void verifyPermissions(String[] permissionArray) {
        ActivityCompat.requestPermissions(this,permissionArray,PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermissionArray(String[] permissionArray){
        for (String permission : permissionArray) {
            if (!checkPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    boolean checkPermission(String permission){
        int permissionRequest = ActivityCompat.checkSelfPermission(this,permission);
        return permissionRequest == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
