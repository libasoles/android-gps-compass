package com.gperez.brujulagpsdesde0;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private boolean wasLocationPermissionGranted = false;

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                wasLocationPermissionGranted = isGranted;
                if (isGranted) {
                    getLastKnowLocation();
                } else {
                    explainTheNeedForPermission();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayMessage("Cargando...");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        checkPermissions();
    }

    private void displayMessage(String s) {
        ((TextView) findViewById(R.id.location)).setText(s);
    }

    private void checkPermissions() {
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;

        int result = ContextCompat.checkSelfPermission(this, locationPermission);
        wasLocationPermissionGranted = result == PackageManager.PERMISSION_GRANTED;

        if (wasLocationPermissionGranted) {
            getLastKnowLocation();
        } else if (shouldShowRequestPermissionRationale(locationPermission)) {
            explainTheNeedForPermission();
        } else {
            requestPermissionLauncher.launch(locationPermission);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastKnowLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, onLocationResult());
    }

    @NotNull
    private OnSuccessListener<Location> onLocationResult() {
        return location -> {
            if (location != null) {
                Toast.makeText(MainActivity.this, "Uju!", Toast.LENGTH_SHORT).show();

                displayCurrentCoordinates(location);
            } else {
                Toast.makeText(MainActivity.this, "Oh no!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void displayCurrentCoordinates(Location location) {
        String message = (location.getLatitude()) + ", " + location.getLongitude();
        displayMessage(message);
    }

    private void explainTheNeedForPermission() {
        Toast.makeText(MainActivity.this, "Tenemos que hablar.", Toast.LENGTH_SHORT).show();
    }
}
