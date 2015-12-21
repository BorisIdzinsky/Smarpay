package com.smarcom.smarpay.cloudapi.request;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.smarcom.smarpay.cloudapi.model.DeviceItem;
import com.smarcom.smarpay.cloudapi.model.ResultDataToDeviceList;
import com.smarcom.smarpay.cloudapi.service.SPService;
import com.smarcom.smarpay.cloudapi.helper.DistanceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FetchNearestDevicesFromCacheRequest extends BaseRequest<List<String>>{

    private static final String DEVICE_REQUEST_CACHE_KEY = "DeviceList2";
    private static final int RADIUS = 200;
    private static final int MAX_CUE_BAR_ITEMS = 5;

    private Location currentLocation;

    public FetchNearestDevicesFromCacheRequest(Location currentLocation) {
        super((Class) List.class);
        this.currentLocation = currentLocation;
    }

    @Override
    public List<String> loadDataFromNetwork() throws Exception {

        List<String> deviceIds = new ArrayList<>();

        SPService service = getService();

        if (service != null) {
            ResultDataToDeviceList resultDataToDeviceList = service.getDataFromCache(ResultDataToDeviceList.class, DEVICE_REQUEST_CACHE_KEY);

            if (resultDataToDeviceList != null) {
                Map<Float, String> map = getDevicesMap(resultDataToDeviceList.getData().getDevices(), currentLocation);
                deviceIds.addAll(getNearestDevicesId(map));
            }
        }

        return deviceIds;
    }

    private Map<Float, String> getDevicesMap(List<DeviceItem> items, Location location) {
        Map <Float, String> resultMap = null;
        if (items != null && location != null) {
            Map<Float, String> map = new HashMap<>();
            for (DeviceItem item : items) {
                float distance = DistanceHelper.calculateDistance(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(item.getLatitude(), item.getLongitude()));
                if (distance <= RADIUS) {
                    map.put(distance, item.getDeviceId());
                }
            }
            resultMap = new TreeMap<>(map);
        }

        return resultMap;
    }

    private List<String> getNearestDevicesId(Map<Float, String> map) {
        List<String> items = new ArrayList<>();
        if (map != null) {
            int currentItemIndex = 0;
            for (Map.Entry<Float, String> item : map.entrySet()) {
                if (currentItemIndex < MAX_CUE_BAR_ITEMS) {
                    items.add(item.getValue());
                    currentItemIndex++;
                } else break;
            }
        }

        return items;
    }
}
