package com.smarcom.smarpay.cloudapi.request;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.smarcom.smarpay.adapter.DeviceListAdapter;
import com.smarcom.smarpay.cloudapi.helper.DistanceHelper;
import com.smarcom.smarpay.cloudapi.helper.FormatHelper;
import com.smarcom.smarpay.cloudapi.service.SPService;

import java.util.List;

public class CalculateDistancesRequest extends BaseRequest<List<DeviceListAdapter.DeviceItemWithDistance>>  {

    private Location location;
    private List<DeviceListAdapter.DeviceItemWithDistance> deviceItems;

    private static final String KILOMETERS = "km";
    private static final String METERS = "m";

    public CalculateDistancesRequest(Location location, List<DeviceListAdapter.DeviceItemWithDistance> deviceItems) {
        super((Class) List.class);
        this.location = location;
        this.deviceItems = deviceItems;
    }

    @Override
    public List<DeviceListAdapter.DeviceItemWithDistance> loadDataFromNetwork() throws Exception {

        SPService service = getService();

        if (service != null) {
            if (deviceItems != null && !deviceItems.isEmpty()) {
                for (int i = 0; i < deviceItems.size(); i++) {
                    String distance;
                    float distanceFloat = DistanceHelper.calculateDistance(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(deviceItems.get(i).getDeviceItem().getLatitude(), deviceItems.get(i).getDeviceItem().getLongitude()));
                    if (distanceFloat >= 1000.0f) {
                        distanceFloat /= 1000.0f;
                        distance = FormatHelper.roundToOneDigit(distanceFloat) + " " + KILOMETERS;
                    } else {
                        distance = FormatHelper.roundToOneDigit(distanceFloat) + " " + METERS;
                    }
                    deviceItems.get(i).setDistance(distance);
                }
            }
        }

        return deviceItems;
    }


}