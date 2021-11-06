package com.project.adverstir;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.adverstir.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationDescriptionActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ImageView location_image, close_button;
    TextView location_country, location_place, star_rating, new_covid_cases, total_death_covid;
    AppCompatButton covid_info, travel_restriction;
    ScrollView travel_scroll, covid_scroll;

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
        setContentView(R.layout.activity_location_more_detail);
        sharedPreferences = getEncryptedSharedPrefs();

        // Set location description background based on clicked item
        location_image = findViewById(R.id.backgroundImage);
        location_image.setImageResource(sharedPreferences.getInt("imageUrl", 0));

        // Set location and place
        location_country = findViewById(R.id.location_country);
        location_country.setText(sharedPreferences.getString("countryName", ""));
        location_place = findViewById(R.id.location_place);
        location_place.setText(sharedPreferences.getString("placeName", ""));

        // set star
        star_rating = findViewById(R.id.desc_star);
        star_rating.setText(String.valueOf(sharedPreferences.getFloat("star_rating", 0.0f)) + "/5.0");

        // Close the location description
        close_button = findViewById(R.id.close_button);
        close_button.setOnClickListener(v -> {
            finish();
        });

        // Fetch covid data API
        fetchData();

        // Clicked on covid info or travel restriction
        covid_info = findViewById(R.id.covid_info);
        travel_restriction = findViewById(R.id.travel_restriction);
        travel_scroll = findViewById(R.id.scrollview_1);
        covid_scroll = findViewById(R.id.scrollview_2);
        // click covid info
        covid_info.setOnClickListener(v -> {
            travel_restriction.setBackgroundResource(R.drawable.flag_transparent);
            travel_restriction.setTextColor(Color.parseColor("#BFBFBF"));
            covid_info.setBackgroundResource(R.drawable.two_section_interchangable_effect);
            covid_info.setTextColor(Color.parseColor("#EB5757"));
            covid_scroll.scrollTo(0,0);
            covid_scroll.setVisibility(View.VISIBLE);
            travel_scroll.setVisibility(View.INVISIBLE);
        });
        // click travel restriction
        travel_restriction.setOnClickListener(v -> {
            covid_info.setBackgroundResource(R.drawable.flag_transparent);
            covid_info.setTextColor(Color.parseColor("#BFBFBF"));
            travel_restriction.setBackgroundResource(R.drawable.two_section_interchangable_effect);
            travel_restriction.setTextColor(Color.parseColor("#EB5757"));
            travel_scroll.scrollTo(0,0);
            travel_scroll.setVisibility(View.VISIBLE);
            covid_scroll.setVisibility(View.INVISIBLE);
        });
    }

    // Get new cases and total deaths of covid
    private void fetchData() {

        new_covid_cases = findViewById(R.id.covid_new_cases);
        total_death_covid = findViewById(R.id.covid_total_death);
        String url = "https://coronavirus-monitor.p.rapidapi.com/coronavirus/who_latest_stat_by_country.php?country=" + sharedPreferences.getString("countryName", "");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "coronavirus-monitor.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "8dff65f1c6msh22f60d5065b4f90p1963e4jsnefd3323d4ace")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new_covid_cases.setText("error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful())
                {
                    String resp = response.body().string();
                    LocationDescriptionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(resp);
                                String val1 = jsonObject.getString("newCases");
                                String val2 = jsonObject.getString("totalDeaths");
                                new_covid_cases.setText(val1.substring(0, val1.length() - 2));
                                total_death_covid.setText(val2.substring(0, val2.length() - 2));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });
    }



}