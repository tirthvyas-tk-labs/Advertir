package com.project.adverstir;


import static com.project.adverstir.Const.Name;
//import static com.travel.travelmate.Const.Name;

import static com.project.adverstir.Const.recentsDataList;
//import static com.travel.travelmate.Const.recentsDataList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.adverstir.R;
import com.project.adverstir.adapter.RecentsAdapter;
//import com.travel.travelmate.adapter.RecentsAdapter;

import com.project.adverstir.model.RecentsData;

//import com.travel.travelmate.model.RecentsData;

import java.util.List;

public class Travel_MainActivity extends AppCompatActivity implements RecyclerViewClickInterface{

    AppCompatButton btnProfile, btnCovid, btnExplore;
    AppCompatTextView tvWelcome;
    SharedPreferences sharedPreferences;
    String name;
    RecyclerView recentRecycler;
    RecentsAdapter recentsAdapter;


    private SharedPreferences getEncryptedSharedPrefs() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    com.project.adverstir.Const.SHAREDPREFERENCE,
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            return sharedPreferences;
        }
        catch(Exception e) {
            Log.e("Failed to create encrypted shared prefs", e.toString());
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_activity_main);
        tvWelcome = findViewById(R.id.tvName);
        sharedPreferences = getEncryptedSharedPrefs();
        name = sharedPreferences.getString(Name, "0");
        tvWelcome.setText("Welcome " + name + ",");

        // Create recent recycler view
        // get recentsDataList from Const.java
        setRecentRecycler(recentsDataList);

        // Go to profile
        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            Intent nav_to_profile = new Intent(Travel_MainActivity.this, com.project.adverstir.ProfileActivity.class);
            startActivity(nav_to_profile);
        });

        // Go to Covid stat
        btnCovid = findViewById(R.id.btnCovid);
        btnCovid.setOnClickListener(v -> {
            Intent nav_to_profile = new Intent(Travel_MainActivity.this, CovidActivity.class);
            startActivity(nav_to_profile);
            finish();
        });
        //Contact tracing

        btnExplore = findViewById(R.id.btnExplore);
        btnExplore.setOnClickListener(v -> {
                    Intent nav_to_profile = new Intent(Travel_MainActivity.this, com.project.adverstir.ui.onboarding.OnboardingActivity.class);
                    startActivity(nav_to_profile);
                    finish();
        });

    }

    // Setup RecyclerView
    private void setRecentRecycler(List<RecentsData> recentsDataList)
    {
        recentRecycler = findViewById(R.id.recent_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recentRecycler.setLayoutManager(layoutManager);
        recentsAdapter = new RecentsAdapter(this, recentsDataList, this);
        recentRecycler.setAdapter(recentsAdapter);
    }

    @Override
    // Enable the ability to click item in RecyclerView
    public void onItemClick(int position) {
        // Log.d("Tag", String.valueOf(recentsDataList.get(position).getPlaceName()));
        Intent nav_to_location_description = new Intent(Travel_MainActivity.this, com.project.adverstir.LocationDescriptionActivity.class);

        // Set location information
        sharedPreferences = getEncryptedSharedPrefs();
        if(sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(com.project.adverstir.Const.SHAREDPREFERENCE, MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("countryName", recentsDataList.get(position).getCountryName());
        editor.putString("placeName", recentsDataList.get(position).getPlaceName());
        editor.putString("price", recentsDataList.get(position).getPrice());
        editor.putFloat("star_rating", recentsDataList.get(position).getStarRating());
        editor.putInt("imageUrl", recentsDataList.get(position).getImageUrl());
        editor.apply();

        startActivity(nav_to_location_description);
    }
}