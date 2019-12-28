package com.vaibhav.myweather;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Dashboard extends AppCompatActivity {

    private TextView temperatureTextView;
    private TextView cityTextView;
    private TextView weatherConditionTextView;
    private TextView maxTempTextView;
    private TextView minTempTextView;
    private TextView pressureTextView;
    private TextView humidityTextView;
    private TextView aqiTextView;
    private TextView adviceTextView;
    private ImageView weatherIconImageView;
    private TextView windSpeedTextView;
    private TextView windDirectionTextView;
    private TextView visibilityTextView;
    private Integer toggle = 0, owmApiRequestComplete = 0, airVisualApiRequestComplete = 0;
    private String city = "", state = "", country = "", changed = "";
    private Button forecastButton;

    public interface OpenWeatherMapApi{
        @GET("weather?appid=0c42f7f6b53b244c78a418f4f181282a")
        Call<ApiResponse> getPosts(@Query("q") String city, @Query("units") String units);
    }

    public interface AirVisualApi{
        @GET("city?")
        Call<ApiResponseAirvisual> getPosts(@Query("city") String city,
                                            @Query("state") String state,
                                            @Query("country") String country,
                                            @Query("key") String key);
    }
    //****************Main*********************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Making following invisible at start of activity
        Button changeCityButton = findViewById(R.id.buttonChangeCity);
        changeCityButton.setVisibility(View.GONE);
        Button refreshButton = findViewById(R.id.buttonRefresh);
        refreshButton.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            city = extras.getString("city");
            state = extras.getString("state");
            country = extras.getString("country");
            changed = extras.getString("changed");
            Toast.makeText(getApplicationContext(), city, Toast.LENGTH_SHORT).show();
        }

        temperatureTextView = findViewById(R.id.temperatureTextView);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())     //Gson to parse the response
                .client(client)
                .build();

        OpenWeatherMapApi openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);
        Call<ApiResponse> call = openWeatherMapApi.getPosts(city, "metric");

        Callback<ApiResponse> owmCallback = new Callback<ApiResponse>() {
        @Override
        public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
            if(!response.isSuccessful()){
                String temperature = "?"; //response.code()
                temperatureTextView.setText(temperature);
                return;
            }

            try{
                ApiResponse weather = response.body();
                temperatureTextView.setText(String.valueOf(weather.getMain().getTemp()));

                String city = weather.getName() + ", " + weather.getSys().getCountry();
                cityTextView = findViewById(R.id.cityTextView);
                cityTextView.setText(city);

                List<WeatherItem> weatherItem = weather.getWeather();
                String condition = "Condition: " + weatherItem.get(0).getMain();
                weatherConditionTextView = findViewById(R.id.weatherCondition);
                weatherConditionTextView.setText(condition);

                String maxTemp = "Max: " + weather.getMain().getTempMax();
                maxTempTextView = findViewById(R.id.maxTempTextView);
                maxTempTextView.setText(maxTemp);

                String minTemp = "Min: " + weather.getMain().getTempMin();
                minTempTextView = findViewById(R.id.minTempTextView);
                minTempTextView.setText(minTemp);

                String pressure = "Pressure: " + weather.getMain().getPressure() + "hPa";
                pressureTextView = findViewById(R.id.pressureTextView);
                pressureTextView.setText(pressure);

                String humidity = "Humidity: " + weather.getMain().getHumidity() + "%";
                humidityTextView = findViewById(R.id.humidityTextView);
                humidityTextView.setText(humidity);

                String windSpeed = "Speed: " + weather.getWind().getSpeed() + "m/s";
                windSpeedTextView = findViewById(R.id.windSpeedTextView);
                windSpeedTextView.setText(windSpeed);

                String windDirection = "Dir: " + weather.getWind().getDeg() + "deg";
                windDirectionTextView = findViewById(R.id.windDirectionTextView);
                windDirectionTextView.setText(windDirection);

                String visibility = "Visibility: " + weather.getVisibility() + "m";
                visibilityTextView = findViewById(R.id.visibilityTextView);
                visibilityTextView.setText(visibility);

                weatherIconImageView = findViewById(R.id.weatherIconImageView);
                String icon = weatherItem.get(0).getIcon();
                switch (icon) {
                    case "01d":
                        weatherIconImageView.setImageResource(R.drawable.sun);
                        break;
                    case "01n":
                        weatherIconImageView.setImageResource(R.drawable.moon);
                        break;
                    case "02d":
                        weatherIconImageView.setImageResource(R.drawable.suncloud);
                        break;
                    case "02n":
                        weatherIconImageView.setImageResource(R.drawable.mooncloud);
                        break;
                    case "03d":
                    case "03n":
                        weatherIconImageView.setImageResource(R.drawable.cloud);
                        break;
                    case "04d":
                    case "04n":
                        weatherIconImageView.setImageResource(R.drawable.brokenclouds);
                        break;
                    case "09d":
                    case "09n":
                        weatherIconImageView.setImageResource(R.drawable.rain);
                        break;
                    case "10d":
                    case "10n":
                        weatherIconImageView.setImageResource(R.drawable.rainbrokenclouds);
                        break;
                    case "11d":
                    case "11n":
                        weatherIconImageView.setImageResource(R.drawable.thunderstorm);
                        break;
                    case "13d":
                    case "13n":
                        weatherIconImageView.setImageResource(R.drawable.snow);
                        break;
                    case "50d":
                    case "50n":  //if(icon.equals("50n")) //icon == "50n"
                        weatherIconImageView.setImageResource(R.drawable.mist);
                        break;
                }

                owmApiRequestComplete = 1;
                saveDetailsDialog();
                /*
                List<WeatherItem> weatherItem = response.body().getWeather();
                //apiTextView.setText(String.valueOf(weatherList.getBase()));

                for(WeatherItem item : weatherItem){
                    String content = "";
                    content += "Icon: " + item.getIcon() + "\n";
                    content += "Main: " + item.getMain() + "\n";
                    content += "Desc: " + item.getDescription() + "\n";
                    content += "ID: " + item.getId();
                    apiTextView.append(content);
                }*/
                }catch (Exception e){
                    temperatureTextView.setText("?");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                String error = "" + t.getMessage();
                Log.d("Error: ", error);// 2019-12-24 03:30:55.894 7233-7233/com.vaibhav.myplanner D/Error:: java.lang.NumberFormatException: Expected an int but was 1.11 at line 1 column 188 path $.main.temp_min
                temperatureTextView.setText("?");
            }
        };

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.airvisual.com/v2/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        aqiTextView = findViewById(R.id.aqiTextView);
        adviceTextView = findViewById(R.id.adviceTextView);

        AirVisualApi airVisualApi = retrofit.create(AirVisualApi.class);
        Call<ApiResponseAirvisual> callAV = airVisualApi.getPosts(city, state, country, "18a64175-2c52-44b8-9154-49ec0c30440e"); //city=Los Angeles&state=California&country=USA&key=18a64175-2c52-44b8-9154-49ec0c30440e

        Callback<ApiResponseAirvisual> airvisualCallback = new Callback<ApiResponseAirvisual>(){
            @Override
            public void onResponse(@NonNull Call<ApiResponseAirvisual> call, @NonNull Response<ApiResponseAirvisual> response) {
                if(!response.isSuccessful()){
                    String failed = "Failed! ApiForecastResponse: " + response.code();
                    aqiTextView.setText(failed);
                }

                try {
                    ApiResponseAirvisual apiResponseAirvisual = response.body();
                    Integer aqiInt = apiResponseAirvisual.getData().getCurrent().getPollution().getAqius();
                    String aqi = "AQI: " + apiResponseAirvisual.getData().getCurrent().getPollution().getAqius();
                    if(aqiInt <= 50){
                        aqi+=" (Good)";
                        aqiTextView.setTextColor(Color.GREEN);
                        adviceTextView.setText(R.string.lowMediumAqi);
                    }
                    else if(aqiInt <= 100){
                        aqi+=" (Moderate)";
                        aqiTextView.setTextColor(Color.YELLOW);
                        adviceTextView.setText(R.string.lowMediumAqi);
                    }
                    else if(aqiInt <= 150){
                        aqi+="\n(Unhealthy for Sensitive Groups)";
                        aqiTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.orange));
                        adviceTextView.setText(R.string.lowMediumAqi);
                    }
                    else if(aqiInt <= 200){
                        aqi+=" (Unhealthy)";
                        aqiTextView.setTextColor(Color.RED);
                        adviceTextView.setText(R.string.highAqi);
                    }
                    else if(aqiInt <= 300){
                        aqi+=" (Very Unhealthy)";
                        aqiTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.purple));
                        adviceTextView.setText(R.string.highAqi);
                    }
                    else{
                        aqi+=" (Hazardous)";
                        aqiTextView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.maroon));
                        adviceTextView.setText(R.string.veryHighAqi);
                    }

                    aqiTextView.setText(aqi);
                }
                catch (Exception e){
                    aqiTextView.setText(getString(R.string.citynotfound));
                }

                airVisualApiRequestComplete = 1;
                saveDetailsDialog();
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseAirvisual> call, @NonNull Throwable t) {
                String error = "" + t.getMessage();
                Log.d("Error: ",error);
                aqiTextView.setText(getString(R.string.networkError));
            }
        };

        /*
        * Note for reference:
        * Call and Callback are interfaces.
        * Call: Asynchronously send the request and notify callback of its response.
        * Callback: Communicates responses from a server or offline requests. (Has 2 functions onResponse() and onFailure().
        * */
        call.enqueue(owmCallback);
        callAV.enqueue(airvisualCallback);

        FloatingActionButton settingFAB = findViewById(R.id.settingFAB);
        settingFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggle == 0){
                    changeCityButton.setVisibility(View.VISIBLE);
                    refreshButton.setVisibility(View.VISIBLE);
                    toggle = 1;
                }
                else{
                    changeCityButton.setVisibility(View.GONE);
                    refreshButton.setVisibility(View.GONE);
                    toggle = 0;
                }
            }
        });
        //On refresh button press in floating action button
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ApiResponse> call = openWeatherMapApi.getPosts(city, "metric");
                call.enqueue(owmCallback);
                Call<ApiResponseAirvisual> callAV = airVisualApi.getPosts(city, state, country, "18a64175-2c52-44b8-9154-49ec0c30440e");
                callAV.enqueue(airvisualCallback);
                Toast.makeText(getApplicationContext(), "Refreshing..", Toast.LENGTH_SHORT).show();
            }
        });

        forecastButton = findViewById(R.id.fiveDayForecastBtn);
        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        Dashboard.this,
                        androidx.core.util.Pair.create(settingFAB, getString(R.string.logoFABTransition)),
                        androidx.core.util.Pair.create(forecastButton, getString(R.string.buttonTransition)));

                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainActivityIntent.putExtra("fromDashboard", "fromDashboard");
                startActivity(mainActivityIntent, activityOptions.toBundle());
            }
        });

        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Forecast.class);
                String city;
                Bundle extras = getIntent().getExtras();
                if(extras!=null){
                    city = extras.getString("city");
                    intent.putExtra("city", city);
                }
                CardView cardview = findViewById(R.id.dashboard_cardView);
                cityTextView = findViewById(R.id.cityTextView);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        Dashboard.this,
                        Pair.create(cardview, getString(R.string.cardTransition)),
                        Pair.create(cityTextView, getString(R.string.cityTransition))
                );
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });
    }

    private class MyException extends Exception{
        private MyException(String s){
            System.out.println("MyException: " + s);
        }
    }

    private void saveDetailsDialog(){
        try{
            FileInputStream fileOutputStream = openFileInput("cityInfo.txt");
            if(changed.equals("true")){
                owmApiRequestComplete = 1;
                airVisualApiRequestComplete = 1;
                throw new MyException("City changed");
            }
        }
        catch (Exception e) {
            changed = "false";
            //File is not found, ask if user wants to save the city.
            if (owmApiRequestComplete == 1 && airVisualApiRequestComplete == 1) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Save");
                alertDialogBuilder.setMessage("Do you wish to save this city?\nYou can always change the city later by clicking on app icon above.");
                alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Saving weather settings in a file
                        try {
                            FileOutputStream fileOutputStream = openFileOutput("cityInfo.txt", Context.MODE_PRIVATE);
                            city+="\n";
                            state+="\n";
                            country+="\n";
                            fileOutputStream.write(city.getBytes());
                            fileOutputStream.write(state.getBytes());
                            fileOutputStream.write(country.getBytes());
                            fileOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("No", null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }
}
