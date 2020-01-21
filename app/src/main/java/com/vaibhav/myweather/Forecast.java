package com.vaibhav.myweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Forecast extends AppCompatActivity {

    public interface ApiOpenweathermapFived{
        @GET("forecast?appid=0c42f7f6b53b244c78a418f4f181282a&q=noida&units=metric")
        Call<ApiForecastResponse> getPosts();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<String> dayOfWeekList = new ArrayList<>();
        ArrayList<Entry> xyValues = new ArrayList<>();
        ArrayList<String> conditionList = new ArrayList<>();
        ArrayList<String> maxTempList = new ArrayList<>();
        ArrayList<String> minTempList = new ArrayList<>();
        ArrayList<String> pressureList = new ArrayList<>();
        ArrayList<String> humidityList = new ArrayList<>();
        ArrayList<String> speedList = new ArrayList<>();
        ArrayList<String> directionList = new ArrayList<>();

        //Make them visible after recyclerView is inflated
        TextView chartHeadingTextView = findViewById(R.id.chartHeadingTextView);
        chartHeadingTextView.setVisibility(View.INVISIBLE);

        LineChart chart = findViewById(R.id.weatherChart);
        chart.setVisibility(View.INVISIBLE);

        TextView swipeTextView = findViewById(R.id.swipeHint);
        swipeTextView.setVisibility(View.INVISIBLE);

        String city = "";
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            city = extras.getString("city") + ",IN";
        }
        TextView cityTextView = findViewById(R.id.cityTextView);
        cityTextView.setText(city);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiOpenweathermapFived apiForecastResponse = retrofit.create(ApiOpenweathermapFived.class);
        Call<ApiForecastResponse> callFd = apiForecastResponse.getPosts();

        callFd.enqueue(new Callback<ApiForecastResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiForecastResponse> call, @NonNull Response<ApiForecastResponse> response) {

                List<ListItem> weatherList = response.body().getList();
                String dateNext, datePrev = "", time = "";
                float counter = 0;
                for (ListItem weather : weatherList){
                    String[] dateSplit = weather.getDtTxt().split(" ");
                    dateNext = dateSplit[0];
                    time = dateSplit[1].split(":")[0];
                    if(!dateNext.equals(datePrev) && time.equals("12")){
                        dateList.add(dateNext);
                        datePrev = dateNext;

                        String finalDay = "";
                        try{
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            Date date = sdf.parse(dateNext);
                            DateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                            if(date != null){
                                finalDay = dateFormat.format(date);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        dayOfWeekList.add(finalDay);
                        conditionList.add(String.valueOf(weather.getWeather().get(0).getMain()));
                        maxTempList.add(String.valueOf(weather.getMain().getTempMax()));
                        minTempList.add(String.valueOf(weather.getMain().getTempMin()));
                        pressureList.add(String.valueOf(weather.getMain().getPressure()));
                        humidityList.add(String.valueOf(weather.getMain().getHumidity()));
                        speedList.add(String.valueOf(weather.getWind().getSpeed()));
                        directionList.add(String.valueOf(weather.getWind().getDeg()));
                        xyValues.add(new Entry(counter+=1, weather.getMain().getTemp()));
                    }
                }

                LineChart chart = findViewById(R.id.weatherChart);
                LineDataSet lineDataSet = new LineDataSet(xyValues, "WeatherInfo");
                lineDataSet.setColor(ContextCompat.getColor(getApplicationContext() ,R.color.colorPrimary));
                lineDataSet.setCircleColor(ContextCompat.getColor(getApplicationContext() ,R.color.colorAccent));
                lineDataSet.setLineWidth(2.0f);
                lineDataSet.setValueTextColor(ContextCompat.getColor(getApplicationContext() ,R.color.colorPrimary));
                lineDataSet.setValueTextSize(10.0f);
                ArrayList<ILineDataSet> dataSet = new ArrayList<>();
                dataSet.add(lineDataSet);
                LineData data = new LineData(dataSet);
                chart.getDescription().setText("5 day forecast");
                chart.setData(data);
                chart.setVisibleXRangeMaximum(24f); //Only 24 values on X axis then scroll
                chart.invalidate();
                XAxis xAxis = chart.getXAxis();
                xAxis.setGranularity(1.0f);

                //Init RecyclerView
                CardView cardView = findViewById(R.id.forecast_cardView);
                cardView.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(linearLayoutManager);
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), dayOfWeekList, conditionList, maxTempList, minTempList, pressureList, humidityList, speedList, directionList);
                recyclerView.setAdapter(recyclerViewAdapter);

                chartHeadingTextView.setVisibility(View.VISIBLE);
                chart.setVisibility(View.VISIBLE);
                swipeTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<ApiForecastResponse> call,@NonNull Throwable t) {
                String error = "" + t.getMessage();
                Log.d("Error: ",error);
                cityTextView.setText(getString(R.string.networkError));
            }
        });
    }

    @Override
    public void onBackPressed(){
        //Make them invisible when leaving the activity
        TextView chartHeadingTextView = findViewById(R.id.chartHeadingTextView);
        chartHeadingTextView.setVisibility(View.INVISIBLE);

        LineChart chart = findViewById(R.id.weatherChart);
        chart.setVisibility(View.INVISIBLE);

        TextView swipeTextView = findViewById(R.id.swipeHint);
        swipeTextView.setVisibility(View.INVISIBLE);

        super.onBackPressed();  //To actually go back
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        private ArrayList<String> dayListRV = new ArrayList<>();
        private ArrayList<String> conditionListRV = new ArrayList<>();
        private ArrayList<String> maxTempListRV = new ArrayList<>();
        private ArrayList<String> minTempListRV = new ArrayList<>();
        private ArrayList<String> pressureListRV = new ArrayList<>();
        private ArrayList<String> humidityListRV = new ArrayList<>();
        private ArrayList<String> speedListRV = new ArrayList<>();
        private ArrayList<String> directionListRV = new ArrayList<>();
        private Context saveContext;

        private RecyclerViewAdapter(Context context, ArrayList<String> days, ArrayList<String> conditions, ArrayList<String> maxs, ArrayList<String> mins, ArrayList<String> pressures, ArrayList<String> humidities, ArrayList<String> speeds, ArrayList<String> directions){
            saveContext = context;
            dayListRV = days;
            conditionListRV = conditions;
            maxTempListRV = maxs;
            minTempListRV = mins;
            pressureListRV = pressures;
            humidityListRV = humidities;
            speedListRV = speeds;
            directionListRV = directions;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_carditem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.day.setText(dayListRV.get(position));
            String string = "Condition: " + conditionListRV.get(position);
            holder.condition.setText(string);
            string = "Max: " + maxTempListRV.get(position);
            holder.max.setText(string);
            string = "Min: " + minTempListRV.get(position);
            holder.min.setText(string);
            string = "Pressure: " + pressureListRV.get(position) + "hPa";
            holder.pressure.setText(string);
            string = "Humidity: " + humidityListRV.get(position) + "%";
            holder.humidity.setText(string);
            string = "Speed: " + speedListRV.get(position) + "m/s";
            holder.speed.setText(string);
            string = "Dir: " + directionListRV.get(position) + "deg";
            holder.direction.setText(string);
            Log.d("Forecast.java Condition: ", conditionListRV.get(position)+position);
        }

        @Override
        public int getItemCount() {
            return conditionListRV.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder{
            TextView day, condition, max, min, pressure, humidity, wind, speed, direction;
            private ViewHolder(View view){
                super(view);
                day = view.findViewById(R.id.dayCardItem);
                condition = view.findViewById(R.id.weatherConditionCardItem);   //direct use of findViewById() will cause NPE in onBindViewHolder
                max = view.findViewById(R.id.maxTempTextViewCardItem);
                min = view.findViewById(R.id.minTempTextViewCardItem);
                pressure = view.findViewById(R.id.pressureTextViewCardItem);
                humidity = view.findViewById(R.id.humidityTextViewCardItem);
                wind = view.findViewById(R.id.windTextViewCardItem);
                speed = view.findViewById(R.id.windSpeedTextViewCardItem);
                direction = view.findViewById(R.id.windDirectionTextViewCardItem);
            }
        }
    }
}
