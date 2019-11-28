package com.example.whatschat.ui.main;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.google.android.gms.maps.LocationSource;

public class CurrentLocationSource implements LocationSource, LocationListener {

    private OnLocationChangedListener listener;
    Context ctx;
    LocationManager manager;

    public CurrentLocationSource(Context ctx){
        manager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.listener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        this.listener = null;
    }
}
