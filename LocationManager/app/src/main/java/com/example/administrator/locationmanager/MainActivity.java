package com.example.administrator.locationmanager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private static final int SHOW_LOCATION =0;
    private TextView positionTextView;
    private LocationManager locationManager;
    private String provider;
    private LocationListener locationListener;
    private android.os.Handler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        positionTextView = (TextView) findViewById(R.id.position_text_view);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myHandler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case SHOW_LOCATION:
                        positionTextView.setText((String)msg.obj);
                }
            }
        };


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                showLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET}, 1);
            } else {
                getLocation();
            }
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "No location provider to use", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        while(location == null){
            locationManager.requestLocationUpdates(provider, 2000, (float) 0.1, locationListener);
        }

        if (location != null) {
            showLocation(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED&&grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }
    }


    private void showLocation(final Location location) {
//        String currentPosition = "latitude is " + location.getLatitude() + "\n" + "longitude is " + location.getLongitude();
//        positionTextView.setText(currentPosition);
//        positionTextView.setText(getLocation(location));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String message =getLocation(location);
                Message msg = new Message();
                msg.what=SHOW_LOCATION;
                msg.obj=message;
                myHandler.sendMessage(msg);
            }
        });
        thread.start();

    }

    private String getLocation(Location location)
    {
        String result="";
        try{
            URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="+location.getLatitude()+","+location.getLongitude()+"&sensor=true_or_false");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(45000);
            connection.setReadTimeout(45000);
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                response.append(line);
            }
            result = response.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
