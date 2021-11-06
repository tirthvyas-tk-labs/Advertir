package com.project.adverstir;

import static com.project.adverstir.Const.UserId;
//import static com.travel.travelmate.Const.UserId;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import androidx.viewpager.widget.ViewPager;

import com.example.adverstir.R;

public class OnBoarding extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dots;
    com.project.adverstir.SliderAdapter sliderAdapter;
    //com.travel.travelmate.SliderAdapter sliderAdapter;
    Button btn_let_get_started;
    SharedPreferences sharedPreferences;
    String userId;


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
        setContentView(R.layout.activity_on_boarding);

        // Onboarding page only show once is enough
        // If not yet seen onboard page before
        sharedPreferences = getEncryptedSharedPrefs();
        if(sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(com.project.adverstir.Const.SHAREDPREFERENCE, MODE_PRIVATE);
        }
        if(sharedPreferences.getBoolean("seen_onboard", false))
        {
            Intent login_page = new Intent(OnBoarding.this, com.project.adverstir.LoginActivity.class);
            startActivity(login_page);
            finish();
        }

        // Check if there is userId in "sharedPreferences" object
        // If yes, then directly go to MainActivity
        userId = sharedPreferences.getString(UserId, "0");
        if (!TextUtils.equals(userId, "0")) {
            Intent main = new Intent(OnBoarding.this, com.project.adverstir.Travel_MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(main);
            finish();
        }

        // Hooks
        // CTRL + D (to copy multiple times)
        viewPager = findViewById(R.id.slider);
        dots = findViewById(R.id.dots);

        // Call adapter
        sliderAdapter = new com.project.adverstir.SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        // Go to Login Page
        btn_let_get_started = findViewById(R.id.get_started_button);
        // How does v-> works?
        btn_let_get_started.setOnClickListener(v -> {
            sharedPreferences.edit().putBoolean("seen_onboard", true).apply();
            Intent to_login = new Intent(OnBoarding.this, com.project.adverstir.LoginActivity.class);
            startActivity(to_login);
            finish();
        });
    }
}