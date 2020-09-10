package com.gperez.brujulagpsdesde0;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class GoogleLocationService extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                broadcastLocation(locationResult.getLastLocation());
            }
        };
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocation();

        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void broadcastLocation(Location lastLocation) {
        Intent locationInfo = new Intent("CURRENT_LOCATION");
        locationInfo.putExtra("latitude", lastLocation.getLatitude());
        locationInfo.putExtra("longitude", lastLocation.getLongitude());
        locationInfo.putExtra("speed", lastLocation.getSpeed());
        locationInfo.putExtra("bearing", lastLocation.getBearing());
        sendBroadcast(locationInfo);
    }
}
