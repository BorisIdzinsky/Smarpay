package com.smarcom.smarpay.helper;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.smarcom.smarpay.cloudapi.model.DeviceItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleApiHelper {

    public static final String COUNTRY_MARKER = "CountryMarker";
    public static final String CITY_MARKER = "CityMarker";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    public static  synchronized GoogleApiClient getGoogleApiClient(Context context, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        return new GoogleApiClient.Builder(context).addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener).addApi(LocationServices.API).build();
    }

    public static void setupGoogleApiClientConnection(GoogleApiClient googleApiClient) {
        if (googleApiClient != null) {
            googleApiClient.reconnect();
        }
    }

    public static boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(context,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                ((Activity)context).finish();
            }
            return false;
        }
        return true;
    }

    public static Map<String, List<LatLng>> groupMarkerByCountry(List<DeviceItem> deviceItems) {
        Map<String, List<LatLng>> countryLevelMap = new HashMap<>();

        if (deviceItems != null && !deviceItems.isEmpty()) {
            for (DeviceItem item : deviceItems) {
                String key = item.getCountry();
                if (countryLevelMap.containsKey(key)) {
                    countryLevelMap.get(key).add(new LatLng(item.getLatitude(), item.getLongitude()));
                } else {
                    List<LatLng> listLatLng = new ArrayList<>();
                    listLatLng.add(new LatLng(item.getLatitude(), item.getLongitude()));
                    countryLevelMap.put(key, listLatLng);
                }
            }
        }

        return countryLevelMap;
    }

    public static Map<String, List<LatLng>> groupMarkerByCity(List<DeviceItem> deviceItems) {
        Map<String, List<LatLng>> cityLevelMap = new HashMap<>();

        if (deviceItems != null && !deviceItems.isEmpty()) {
            for (DeviceItem item : deviceItems) {
                String key = item.getCity();
                if (cityLevelMap.containsKey(key)) {
                    cityLevelMap.get(key).add(new LatLng(item.getLatitude(), item.getLongitude()));
                } else {
                    List<LatLng> listLatLng = new ArrayList<>();
                    listLatLng.add(new LatLng(item.getLatitude(), item.getLongitude()));
                    cityLevelMap.put(key, listLatLng);
                }
            }
        }

        return cityLevelMap;
    }

    public static List<DeviceItem> getGroupedDeviceItems(Map<String, List<LatLng>> map, String deviceId) {
        List<DeviceItem> items = new ArrayList<>();
        if (!map.isEmpty()) {

            for (String item : map.keySet()) {
                double latitude = 0;
                double longitude = 0;
                int size = map.get(item).size();

                for (LatLng latLng : map.get(item)) {
                    latitude += latLng.latitude;
                    longitude +=  latLng.longitude;
                }

                String deviceQuantity = size + " devices";

                if (size == 1) {
                    deviceQuantity = size + "  device";
                }

                items.add(new DeviceItem(deviceId, item, deviceQuantity, latitude/size, longitude/size));
            }
        }

        return items;
    }

}