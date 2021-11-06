package com.project.adverstir;


import static com.project.adverstir.Const.Email;
import static com.project.adverstir.Const.Mobile;
import static com.project.adverstir.Const.Name;


//import static com.travel.travelmate.Const.Email;
//import static com.travel.travelmate.Const.Mobile;
//import static com.travel.travelmate.Const.Name;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.adverstir.R;

public class ProfileActivity extends AppCompatActivity {

    AppCompatButton btnLogout;
    AppCompatTextView tvName, tvMobile, tvEmail;
    ImageView btnGoBack;
    SharedPreferences sharedPreferences;

    private SharedPreferences getEncryptedSharedPrefs() {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    Const.SHAREDPREFERENCE,
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
        setContentView(R.layout.profile_activity);
        sharedPreferences = getEncryptedSharedPrefs();

        // Display information
        tvName = findViewById(R.id.tvName);
        tvMobile = findViewById(R.id.tvMobile);
        tvEmail = findViewById(R.id.tvEmail);
        tvName.setText("Username: " + sharedPreferences.getString(Name, "0"));
        tvMobile.setText("Mobile Number: " + sharedPreferences.getString(Mobile, "0"));
        tvEmail.setText("Email: " + sharedPreferences.getString(Email, "0"));


        // Logout
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            // clear the "sharedPreferences" object
            sharedPreferences.edit().clear().apply();
            // Still need to reset seen_onboard
            sharedPreferences.edit().putBoolean("seen_onboard", true).apply();
            Intent logout = new Intent(ProfileActivity.this, com.project.adverstir.LoginActivity.class);
            Toast.makeText(ProfileActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
            startActivity(logout);
            finish();
        });

        // Close button
        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(v -> {
            finish();
        });
    }
}