package com.smarcom.smarpay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smarcom.smarpay.R;
import com.smarcom.smarpay.SPApplication;
import com.smarcom.smarpay.activity.PaymentActivity;
import com.smarcom.smarpay.activity.SearchActivity;
import com.smarcom.smarpay.cloudapi.model.CacheData;
import com.smarcom.smarpay.cloudapi.model.DeviceItem;
import com.smarcom.smarpay.helper.FontHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, OnInfoWindowClickListener, TextWatcher, GoogleMap.OnCameraChangeListener {

    private enum ZoomLevel {
        MapZoomLevelUnknown,
        MapZoomLevelCountries,
        MapZoomLevelCities,
        MapZoomLevelDevices
    }

    private static final int COUNTRY_LEVEL = 4;
    private static final int CITY_LEVEL = 7;
    private static final int STREET_LEVEL = 12;

    private static final int MAX_MAP_PINS = 3;

    @InjectView(R.id.map_search_field)
    private EditText searchField;

    private GoogleMap map;
    private Boolean gettingMap = false;

    private Map<Marker, Object> markerMap = new HashMap<>();

    private Map<String, DeviceCountryInfo> countryMap = new HashMap<>();
    private Map<String, DeviceCityInfo> cityMap = new HashMap<>();

    private List<DeviceItem> devices = new ArrayList<>();
    private List<DeviceItem> deviceItems = new ArrayList<>();

    private ZoomLevel mapZoomLevel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchField.setTypeface(FontHelper.getRegularFont(getActivity()));
        searchField.addTextChangedListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onStart() {
        super.onStart();

        mapZoomLevel = ZoomLevel.MapZoomLevelUnknown;

        SPApplication application = (SPApplication)getActivity().getApplication();
        CacheData data = application.getData();

        if (data != null) {
            devices.clear();
            devices.addAll(data.getDeviceList().getData().getDevices());
            SetUpMapIfNeeded();
        }

        String text = searchField.getText().toString();

        if (isReplaceTransaction()) {
            searchField.getText().clear();
            setIsReplaceTransaction(false);
        } else if (application.isWasInBackground() && !TextUtils.isEmpty(text)){
            handleQueryTextChange(text);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.setOnCameraChangeListener(this);
        map.setOnInfoWindowClickListener(this);
        gettingMap = false;

        updateCitiesAndCountriesItems();
        updateMapPinsAndAdjustZoom(true);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Object info = markerMap.get(marker);

        if (info instanceof DeviceCountryInfo) {
            mapZoomLevel = ZoomLevel.MapZoomLevelCities;
            DeviceCountryInfo deviceCountryInfo = (DeviceCountryInfo)info;

            cityMap.clear();

            for(DeviceCityInfo deviceCityInfo : deviceCountryInfo.getCityInfos()){
                cityMap.put(deviceCityInfo.getCity(), deviceCityInfo);
            }

        } else if (info instanceof DeviceCityInfo) {
            mapZoomLevel = ZoomLevel.MapZoomLevelDevices;
            DeviceCityInfo deviceCityInfo = (DeviceCityInfo)info;

            deviceItems.clear();
            deviceItems.addAll(deviceCityInfo.getDeviceItems());

        } else {
            DeviceItem deviceItem = (DeviceItem)info;
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra(SearchActivity.DEVICE_ID_EXTRA, deviceItem.getDeviceId());
            startActivity(intent);
        }

        updateMapPinsAndAdjustZoom(true);
    }

    private void SetUpMapIfNeeded() {
        if (gettingMap) {
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        gettingMap = true;
        mapFragment.getMapAsync(this);
    }

    private void handleQueryTextChange(String s) {
        devices.clear();

        SPApplication application = (SPApplication)getActivity().getApplication();
        CacheData data = application.getData();
        List<DeviceItem> cachedDevices = data.getDeviceList().getData().getDevices();

        if (cachedDevices != null) {
            if (!s.isEmpty()) {
                for (DeviceItem deviceItem : cachedDevices) {
                    if (deviceItem.getCity().toUpperCase().contains(s.toUpperCase()) || deviceItem.getStreet().toUpperCase().contains(s.toUpperCase())) {
                        devices.add(deviceItem);
                    }
                }
            } else {
                devices.addAll(cachedDevices);
            }
        }
        mapZoomLevel = ZoomLevel.MapZoomLevelUnknown;

        updateCitiesAndCountriesItems();
        updateMapPinsAndAdjustZoom(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (searchField.isFocused()) {
            handleQueryTextChange(s.toString());
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        ZoomLevel zoomLevel = ZoomLevel.MapZoomLevelUnknown;

        if (cameraPosition.zoom >= STREET_LEVEL) {
            zoomLevel = ZoomLevel.MapZoomLevelDevices;
        } else if (cameraPosition.zoom >= CITY_LEVEL) {
            zoomLevel = ZoomLevel.MapZoomLevelCities;
        } else {
            zoomLevel = ZoomLevel.MapZoomLevelCountries;
        }

        if (mapZoomLevel != zoomLevel) {
            this.mapZoomLevel = zoomLevel;
            updateMapPinsAndAdjustZoom(false);
        }
    }

    private void updateCitiesAndCountriesItems() {
        countryMap.clear();
        cityMap.clear();

        if (devices != null) {
            deviceItems.clear();
            deviceItems.addAll(devices);


            for (DeviceItem deviceItem : deviceItems) {
                String city = deviceItem.getCity();

                DeviceCityInfo deviceItems = cityMap.get(city);

                if (deviceItems == null) {
                    deviceItems = new DeviceCityInfo(city);
                    cityMap.put(city, deviceItems);
                }

                deviceItems.include(deviceItem);
            }

            for (DeviceCityInfo deviceCity : cityMap.values()) {
                String country = deviceCity.get(0).getCountry();

                DeviceCountryInfo deviceCountry = countryMap.get(country);

                if (deviceCountry == null) {
                    deviceCountry = new DeviceCountryInfo(country);
                    countryMap.put(country, deviceCountry);
                }

                deviceCountry.include(deviceCity);
            }

        }
    }

    private void updateMapPinsAndAdjustZoom(boolean isAdjustZoom) {
        map.clear();
        markerMap.clear();

        if (this.mapZoomLevel == ZoomLevel.MapZoomLevelUnknown) {
            if (this.deviceItems.size() <= MAX_MAP_PINS) {
                this.mapZoomLevel = ZoomLevel.MapZoomLevelDevices;
            } else if (this.cityMap.size() <= MAX_MAP_PINS) {
                this.mapZoomLevel = ZoomLevel.MapZoomLevelCities;
            } else {
                this.mapZoomLevel = ZoomLevel.MapZoomLevelCountries;
            }
        }

        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        CameraPosition cameraPosition = null;

        switch (mapZoomLevel) {
            case MapZoomLevelCountries:
                for (DeviceCountryInfo countryInfo : countryMap.values()) {
                    LatLng latLng = countryInfo.getPosition();
                    boundsBuilder = boundsBuilder.include(latLng);

                    Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(countryInfo.getCountry()).snippet(countryInfo.getTotalDeviceString()));
                    markerMap.put(marker, countryInfo);
                }

                if (isAdjustZoom && !countryMap.values().isEmpty()) {
                    cameraPosition = CameraPosition.builder().target(boundsBuilder.build().getCenter()).zoom(COUNTRY_LEVEL).build();
                }
                break;
            case MapZoomLevelCities:
                for (DeviceCityInfo cityInfo : cityMap.values()) {
                    LatLng latLng = cityInfo.getPosition();

                    boundsBuilder = boundsBuilder.include(latLng);
                    Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(cityInfo.getCity()).snippet(cityInfo.getTotalDeviceString()));

                    markerMap.put(marker, cityInfo);
                }

                if (isAdjustZoom && !cityMap.values().isEmpty()) {
                    cameraPosition = CameraPosition.builder().target(boundsBuilder.build().getCenter()).zoom(CITY_LEVEL).build();
                }
                break;
            case MapZoomLevelDevices:
                for (DeviceItem deviceItem : deviceItems) {
                    LatLng latLng = new LatLng(deviceItem.getLatitude(), deviceItem.getLongitude());
                    boundsBuilder = boundsBuilder.include(latLng);
                    Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(deviceItem.getStreet()).snippet(deviceItem.getCity()));

                    markerMap.put(marker, deviceItem);
                }

                if (isAdjustZoom && !deviceItems.isEmpty()) {
                    cameraPosition = CameraPosition.builder().target(boundsBuilder.build().getCenter()).zoom(STREET_LEVEL).build();
                }
                break;
            default:
                break;
        }

        if (cameraPosition != null) {
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private class DeviceCityInfo {
        private String city = null;
        private List<DeviceItem> deviceItems = new ArrayList<>();
        private LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();

        public DeviceCityInfo(String city) {
            this.city = city;
        }

        public DeviceItem get(int index) {
            DeviceItem result = null;

            if (index < deviceItems.size()) {
                result = deviceItems.get(index);
            }

            return result;
        }

        public String getCity() {
            return city;
        }

        public int size() {
            return deviceItems.size();
        }

        public String getTotalDeviceString() {
            return size() + " devices";
        }

        public LatLng getPosition() {
            return boundsBuilder.build().getCenter();
        }

        public List<DeviceItem> getDeviceItems() {
            return deviceItems;
        }

        public void include(DeviceItem deviceItem) {
            deviceItems.add(deviceItem);
            boundsBuilder.include(new LatLng(deviceItem.getLatitude(), deviceItem.getLongitude()));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DeviceCityInfo)) return false;

            DeviceCityInfo that = (DeviceCityInfo) o;

            if (city != null ? !city.equals(that.city) : that.city != null) return false;
            if (deviceItems != null ? !deviceItems.equals(that.deviceItems) : that.deviceItems != null)
                return false;
            return !(boundsBuilder != null ? !boundsBuilder.equals(that.boundsBuilder) : that.boundsBuilder != null);

        }

        @Override
        public int hashCode() {
            int result = city != null ? city.hashCode() : 0;
            result = 31 * result + (deviceItems != null ? deviceItems.hashCode() : 0);
            result = 31 * result + (boundsBuilder != null ? boundsBuilder.hashCode() : 0);
            return result;
        }
    }

    private class DeviceCountryInfo {

        private String country = null;
        private List<DeviceCityInfo> cityInfos = new ArrayList<>();
        private LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        private long totalDeviceCount = 0;

        public DeviceCountryInfo(String country) {
            this.country = country;
        }

        public String getCountry() {
            return country;
        }

        public String getTotalDeviceString() {
            return totalSize() + " devices";
        }

        public DeviceCityInfo get(int index) {
            DeviceCityInfo result = null;

            if (index < cityInfos.size()) {
                result = cityInfos.get(index);
            }

            return result;
        }

        public int size() {
            return cityInfos.size();
        }

        public long totalSize() {
            return totalDeviceCount;
        }

        public LatLng getPosition() {
            return boundsBuilder.build().getCenter();
        }

        public List<DeviceCityInfo> getCityInfos() {
            return cityInfos;
        }

        public void include(DeviceCityInfo deviceCityItem) {
            totalDeviceCount += deviceCityItem.size();
            cityInfos.add(deviceCityItem);
            boundsBuilder.include(deviceCityItem.getPosition());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DeviceCountryInfo)) return false;

            DeviceCountryInfo that = (DeviceCountryInfo) o;

            if (totalDeviceCount != that.totalDeviceCount) return false;
            if (country != null ? !country.equals(that.country) : that.country != null)
                return false;
            if (cityInfos != null ? !cityInfos.equals(that.cityInfos) : that.cityInfos != null)
                return false;
            return !(boundsBuilder != null ? !boundsBuilder.equals(that.boundsBuilder) : that.boundsBuilder != null);

        }

        @Override
        public int hashCode() {
            int result = country != null ? country.hashCode() : 0;
            result = 31 * result + (cityInfos != null ? cityInfos.hashCode() : 0);
            result = 31 * result + (boundsBuilder != null ? boundsBuilder.hashCode() : 0);
            result = 31 * result + (int) (totalDeviceCount ^ (totalDeviceCount >>> 32));
            return result;
        }
    }
}
