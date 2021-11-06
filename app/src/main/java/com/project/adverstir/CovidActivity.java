package com.project.adverstir;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.adverstir.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CovidActivity extends AppCompatActivity {

    AppCompatButton btnProfile, btnHome, btnExplore;
    EditText EnterCountry;
    Button SearchButton;
    TextView activeCases, totalDeaths, newDeaths, fullyVaccinated;
    ImageView active_cases_image_view, total_death_image_view, fully_vaccinated_image_view, new_death_image_view;
    ProgressBar loading_circle;
    ConstraintLayout set_to_dimmer_if_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid);

        loading_circle = findViewById(R.id.loading_circle);
        set_to_dimmer_if_loading = findViewById(R.id.set_to_dimmer);
        EnterCountry = findViewById(R.id.country);
        SearchButton = findViewById(R.id.button);
        activeCases= findViewById(R.id.result);
        totalDeaths = findViewById(R.id.result2);
        newDeaths = findViewById(R.id.result3);
        fullyVaccinated = findViewById(R.id.result4);

        // Graph images
        active_cases_image_view = findViewById(R.id.active_cases_image);
        total_death_image_view = findViewById(R.id.total_death_image);
        fully_vaccinated_image_view = findViewById(R.id.fully_vaccinated_image);
        new_death_image_view = findViewById(R.id.new_death_image);
        // Set graph image to visible
        active_cases_image_view.setVisibility(View.VISIBLE);
        total_death_image_view.setVisibility(View.VISIBLE);
        fully_vaccinated_image_view.setVisibility(View.VISIBLE);
        new_death_image_view.setVisibility(View.VISIBLE);
        // Set graph image to alpha 0
        active_cases_image_view.setAlpha(0f);
        total_death_image_view.setAlpha(0f);
        fully_vaccinated_image_view.setAlpha(0f);
        new_death_image_view.setAlpha(0f);

        // Click on Search Button
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryName = EnterCountry.getText().toString();
                fetchData(countryName);
            }
        });

        // Go to profile
        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            Intent nav_to_profile = new Intent(CovidActivity.this, com.project.adverstir.ProfileActivity.class);
            startActivity(nav_to_profile);
        });

        // Go to Home
        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent nav_to_home = new Intent(CovidActivity.this, com.project.adverstir.Travel_MainActivity.class);
            startActivity(nav_to_home);
            finish();
        });


        btnExplore = findViewById(R.id.btnExplore);
        btnExplore.setOnClickListener(v -> {
            Intent nav_to_contact = new Intent(CovidActivity.this, com.project.adverstir.ui.onboarding.OnboardingActivity.class);
            startActivity(nav_to_contact);
            finish();
        });

    }

    private void fetchData(String stringy) {
        // Start progress bar
        loading_circle.setVisibility(View.VISIBLE);
        set_to_dimmer_if_loading.setAlpha(0.5f);

        String url = "https://coronavirus-monitor.p.rapidapi.com/coronavirus/who_latest_stat_by_country.php?country="+stringy;
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
                activeCases.setText("error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful())
                {
                    String resp = response.body().string();
                    CovidActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // End Progress bar
                                loading_circle.setVisibility(View.INVISIBLE);
                                set_to_dimmer_if_loading.setAlpha(1.0f);

                                JSONObject jsonObject = new JSONObject(resp);
                                String val1 = jsonObject.getString("newCases");
                                String val2 = jsonObject.getString("newDeaths");
                                String val3 = jsonObject.getString("totalDeaths");
                                String val4 = jsonObject.getString("peopleFullyVaccinatedPerHundred");

                                activeCases.setText(val1);
                                totalDeaths.setText(val3);
                                newDeaths.setText(val2);
                                fullyVaccinated.setText(val4);

                                // Set images alpha to 1
                                active_cases_image_view.animate().alpha(1f).setDuration(1000).setListener(null);
                                total_death_image_view.animate().alpha(1f).setDuration(1000).setListener(null);
                                fully_vaccinated_image_view.animate().alpha(1f).setDuration(1000).setListener(null);
                                new_death_image_view.animate().alpha(1f).setDuration(1000).setListener(null);

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