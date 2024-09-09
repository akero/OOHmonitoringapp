package com.acme.afsvendor.activity.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationHelper implements LocationListener {
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    public String locationString = "";
    LocationCallback lc;


    public void requestLocationPermission(Context context, LocationCallback l) {
        this.lc= l;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission from the activity or fragment
            Log.d("tag21", "1");
            // You'll need to implement onRequestPermissionsResult in that class
        } else {
            Log.d("tag21", "2");
            getLocation(context);
        }
    }

    private void getLocation(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Log.d("tag21", "3");
        /*if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                Log.d("tag21", "4");
            }
        }*/
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                Log.d("tag21", "5");
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        locationString = latitude + "," + longitude;
        Log.d("tag21", "6");
        // You can use the locationString as needed
        if (lc != null) {
            Log.d("tag21", "7");
            lc.callback(locationString);
        }
    }

    // Other overridden methods from LocationListener
    // ...
}