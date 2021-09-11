package com.kgec.knowyourweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.Permission;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String Api_Id = "a1640a37440872be76e6bf3cbaa6f356";
    final String Weather_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long min_time = 5000;
    final float min_distance = 1000;
    final int Request_code = 101;

    String Location_provider = LocationManager.GPS_PROVIDER;

    LocationManager locationManager;
    LocationListener locationListener;

    private ImageView weatherIcon;
    private TextView tempareture, city_name,weatherState;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherIcon = findViewById(R.id.weatherIcon);
        tempareture = findViewById(R.id.temperature);
        city_name = findViewById(R.id.cityName);
        weatherState=findViewById(R.id.weatherCondition);
        relativeLayout = findViewById(R.id.cityFinder);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Find_City_Activity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        String city=getIntent().getStringExtra("cityName");

        if (city!=null){

            getWeatherByCityName(city);
        }
        else {

            getWeatherForCurrentLocation();
        }



    }

    private void getWeatherByCityName(String city) {

        RequestParams params=new RequestParams();
        params.put("q",city);
        params.put("appId",Api_Id);
        NetWorkingMethod(params);

    }

    private void getWeatherForCurrentLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params=new RequestParams();
                params.put("lat",Latitude);
                params.put("lon",Longitude);
                params.put("key",Api_Id);

                NetWorkingMethod(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_code);
            return;
        }
        locationManager.requestLocationUpdates(Location_provider, min_time, min_distance, locationListener);
    }

    private void NetWorkingMethod(RequestParams params) {

        AsyncHttpClient client=new AsyncHttpClient();
        client.get(Weather_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);

                WeatherData data=WeatherData.fromJson(response);
                updateUI(data);
                Toast.makeText(MainActivity.this, "Data Get Success. .. .", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void updateUI(WeatherData data) {

        tempareture.setText(data.getmTempareture());
        weatherState.setText(data.getmWeatherType());
        city_name.setText(data.getmCity());
        //int resourceId=getResources().getIdentifier(data.getmIcon(),"drawable",getPackageName());
        //weatherIcon.setImageResource(resourceId);

        int resourceID=getResources().getIdentifier(data.getmIcon(),"drawable",getPackageName());
        weatherIcon.setImageResource(resourceID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==Request_code){

            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager!=null){

            locationManager.removeUpdates(locationListener);
        }
    }
}