package com.example.tonu.getlatitudelongitude;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private GpsTracker gpsTracker;
    private TextView tvLatitude,tvLongitude,tvSunrise,tvSunset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tvLatitude = (TextView)findViewById(R.id.latitude);
        //tvLongitude = (TextView)findViewById(R.id.longitude);
        tvSunrise = (TextView)findViewById(R.id.sunrise);
        tvSunset = (TextView)findViewById(R.id.sunset);


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getLocation(View view){
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            //tvLatitude.setText(String.valueOf(latitude));
            //tvLongitude.setText(String.valueOf(longitude));

            TimeZone timezone = TimeZone.getDefault();
            String timezoneId = timezone.getID();

            Location location = new Location(String.valueOf(latitude), String.valueOf(longitude));
            SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, timezoneId);
            String officialSunrise = calculator.getOfficialSunriseForDate(Calendar.getInstance());
            String officialSunset = calculator.getOfficialSunsetForDate(Calendar.getInstance());
            Calendar officialSunsetCal = calculator.getOfficialSunsetCalendarForDate(Calendar.getInstance());


            Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Chuck Noon.ttf");

            tvSunrise.setTypeface(myTypeface);
            tvSunset.setTypeface(myTypeface);

            tvSunrise.setTextColor(Color.parseColor("#F39C12"));
            tvSunset.setTextColor(Color.parseColor("#6C3483"));

            tvSunrise.setText("Sunrise Time : "+officialSunrise);
            tvSunset.setText("Sunset Time : "+officialSunset);


            Log.e("Sunrise", officialSunrise);
            Log.e("Sunset", officialSunset);


        }else{
            gpsTracker.showSettingsAlert();
        }
    }
}