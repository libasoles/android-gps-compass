package com.gperez.brujulagpsdesde0;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private boolean wasLocationPermissionGranted = false;

    /**
     * On request permission result
     */
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                wasLocationPermissionGranted = isGranted;
                if (isGranted) {
                    subscribeToLocation();
                } else {
                    explainTheNeedForPermission();
                }
            });
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                displayCurrentLocation(locationResult.getLastLocation());
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };

        checkPermissions();
    }

    private void checkPermissions() {
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

        int result = ContextCompat.checkSelfPermission(this, locationPermission);
        wasLocationPermissionGranted = result == PackageManager.PERMISSION_GRANTED;

        if (wasLocationPermissionGranted) {
            subscribeToLocation();
        } else if (shouldShowRequestPermissionRationale(locationPermission)) {
            explainTheNeedForPermission();
        } else {
            requestPermissionLauncher.launch(locationPermission);
        }
    }

    @SuppressLint("MissingPermission")
    private void subscribeToLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void displayCurrentLocation(Location location) {
        String message = (location.getLatitude()) + ", " + location.getLongitude();
        ((TextView) findViewById(R.id.location)).setText("Coordinates: " + message);
        ((TextView) findViewById(R.id.speed)).setText("Speed: " + location.getSpeed());
        ((TextView) findViewById(R.id.orientation)).setText("Orientation: " + location.getBearing());
        findViewById(R.id.arrow).setRotation(location.getBearing());
    }

    private void explainTheNeedForPermission() {
        Toast.makeText(MainActivity.this, "We need to talk.", Toast.LENGTH_SHORT).show();
    }
}
