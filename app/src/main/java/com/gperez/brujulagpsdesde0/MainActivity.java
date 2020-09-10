package com.gperez.brujulagpsdesde0;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver locationBroadcastReceiver;
    private boolean wasLocationPermissionGranted = false;
    /**
     * On request permission result
     */
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                wasLocationPermissionGranted = isGranted;
                if (isGranted) {
                    startLocationService();
                } else {
                    explainTheNeedForPermission();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
    }

    private void startLocationService() {
        subscribeToLocationService();

        Intent locationService = new Intent(this, GoogleLocationService.class);
        startService(locationService);
    }

    private void subscribeToLocationService() {
        locationBroadcastReceiver = new LocationBroadcastReceiver(
                (location) -> displayCurrentLocation(location)
        );
        registerReceiver(locationBroadcastReceiver, new IntentFilter("CURRENT_LOCATION"));
    }

    private void checkPermissions() {
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

        int result = ContextCompat.checkSelfPermission(this, locationPermission);
        wasLocationPermissionGranted = result == PackageManager.PERMISSION_GRANTED;

        if (wasLocationPermissionGranted) {
            startLocationService();
        } else if (shouldShowRequestPermissionRationale(locationPermission)) {
            explainTheNeedForPermission();
        } else {
            requestPermissionLauncher.launch(locationPermission);
        }
    }

    private void explainTheNeedForPermission() {
        Toast.makeText(MainActivity.this, "We need to talk.", Toast.LENGTH_SHORT).show();
    }

    private void displayCurrentLocation(Location location) {
        String message = (location.getLatitude()) + ", " + location.getLongitude();
        setTextFor(R.id.location, "Coordinates: " + message);
        setTextFor(R.id.speed, "Speed: " + location.getSpeed());
        setTextFor(R.id.orientation, "Orientation: " + location.getBearing());

        findViewById(R.id.arrow).setRotation(location.getBearing());
    }

    private void setTextFor(int p, String text) {
        ((TextView) findViewById(p)).setText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(locationBroadcastReceiver);
    }
}
