package com.vaibhav.myweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTextCity = findViewById(R.id.EditTextCity);
        final EditText editTextState = findViewById(R.id.EditTextState);
        final EditText editTextCountry = findViewById(R.id.EditTextCountry);

        String extras = "";
        try{
            //If main activity is started from dashboard don't redirect it back to dashboard.
            extras = getIntent().getExtras().getString("fromDashboard");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(!extras.equals("fromDashboard")){
            try{
                //If safe file exists then simply continue to dashboard
                FileInputStream fileInputStream = openFileInput("cityInfo.txt");
                String city, state, country;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                city = bufferedReader.readLine();
                state = bufferedReader.readLine();
                country = bufferedReader.readLine();

                editTextCity.setText(city);
                editTextState.setText(state);
                editTextCountry.setText(country);

                Intent dashboard = new Intent(getApplicationContext(), Dashboard.class);
                dashboard.putExtra("city", city);
                dashboard.putExtra("state", state);
                dashboard.putExtra("country", country);
                dashboard.putExtra("changed", "false");
                startActivity(dashboard);
                finish();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        Button buttonSave = findViewById(R.id.ButtonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String city = editTextCity.getText().toString();
                    String state = editTextState.getText().toString();
                    String country = editTextCountry.getText().toString();
                    Toast.makeText(getApplicationContext(), "Working..", Toast.LENGTH_SHORT).show();
                    Intent dashboard = new Intent(getApplicationContext(), Dashboard.class);
                    dashboard.putExtra("city", city);
                    dashboard.putExtra("state", state);
                    dashboard.putExtra("country", country);
                    dashboard.putExtra("changed", "true");

                    FloatingActionButton logoFAB = findViewById(R.id.logoFAB);
                    Button button = findViewById(R.id.ButtonSave);

                    ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    MainActivity.this,
                                    androidx.core.util.Pair.create(logoFAB, getString(R.string.logoFABTransition)),
                                    androidx.core.util.Pair.create(button, getString(R.string.buttonTransition)));
                    startActivity(dashboard, activityOptions.toBundle());
                }
            }
        );
    }
}
