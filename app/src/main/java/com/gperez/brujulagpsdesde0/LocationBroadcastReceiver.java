package com.gperez.brujulagpsdesde0;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.function.Consumer;

class LocationBroadcastReceiver extends BroadcastReceiver {

    private Consumer<Location> callback;

    public LocationBroadcastReceiver(Consumer<Location> callback) {
        this.callback = callback;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != "CURRENT_LOCATION")
            return;

        Location location = new Location("");
        location.setLatitude(intent.getDoubleExtra("latitude", 0f));
        location.setLongitude(intent.getDoubleExtra("longitude", 0f));
        location.setSpeed(intent.getFloatExtra("speed", 0f));
        location.setBearing(intent.getFloatExtra("bearing", 0f));

        this.callback.accept(location);
    }
}
