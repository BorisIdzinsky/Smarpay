package com.smarcom.smarpay.cloudapi.helper;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class DistanceHelper{

    public static float calculateDistance(LatLng latLng1, LatLng latLng2) {
        Location locationA = new Location("A");
        locationA.setLatitude(latLng1.latitude);
        locationA.setLongitude(latLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(latLng2.latitude);
        locationB.setLongitude(latLng2.longitude);
        return locationA.distanceTo(locationB);
    }
}
