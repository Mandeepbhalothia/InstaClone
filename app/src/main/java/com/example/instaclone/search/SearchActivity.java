package com.example.instaclone.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instaclone.R;
import com.example.instaclone.databinding.ActivitySearchBinding;
import com.example.instaclone.model.UserSetting;
import com.example.instaclone.profile.ProfileActivity;
import com.example.instaclone.utils.Common;
import com.example.instaclone.utils.SearchAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    ListView userList;
    SearchAdapter searchAdapter;
    ArrayList<UserSetting> userSettings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userList = binding.searchList;

        // load different fragment on item selected in bottom nav bar
        int ACTIVITY_NO = 1;
        BottomNavigationView bottomNavigationView = binding.bottomNavBarSearchActivity.bottomNavView;
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(ACTIVITY_NO).getItemId());
        new Common(this).initBottomNavListener(bottomNavigationView);

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getUser(s.toString());
            }
        });

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < userSettings.size()){
                    Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                    intent.putExtra(getString(R.string.calling_activity),getString(R.string.search));
                    intent.putExtra(getString(R.string.user_account_settings), userSettings.get(position));
                    startActivity(intent);
                }
            }
        });

    }

    private void getUser(String userName) {

        if (userName.length()>1) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            db.child(getString(R.string.user_account_settings)).orderByChild(getString(R.string.username))
                    .equalTo(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("TAG", "onDataChange: "+dataSnapshot);
                    if (dataSnapshot.getValue() != null) {
                        userSettings = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            UserSetting userSetting = dataSnapshot1.getValue(UserSetting.class);
                            userSettings.add(userSetting);
                        }
                        updateUserList(userSettings);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void updateUserList(ArrayList<UserSetting> userSetting){
        searchAdapter = new SearchAdapter(this, userSetting);
        userList.setAdapter(searchAdapter);
        closeKeyboard();
    }

    private void closeKeyboard(){
        if (getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
