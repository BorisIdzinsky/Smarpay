package com.smarcom.smarpay.cloudapi.model;

import android.graphics.Bitmap;

import java.util.Map;

public class CacheData {

    private ResultDataTofAdvList advList;
    private ResultDataToDeviceList deviceList;
    private Map<String, Bitmap> imagesMap;

    public CacheData(ResultDataTofAdvList advList, ResultDataToDeviceList deviceList, Map<String, Bitmap> imagesMap) {
        this.advList = advList;
        this.deviceList = deviceList;
        this.imagesMap = imagesMap;
    }

    public CacheData() {
    }

    public int getAdvListVersion() {
        return advList.getData().getVersion();
    }

    public int getDeviceListVersion() {
        return deviceList.getData().getVersion();
    }

    public ResultDataTofAdvList getAdvList() {
        return advList;
    }

    public void setAdvList(ResultDataTofAdvList advList) {
        this.advList = advList;
    }

    public ResultDataToDeviceList getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(ResultDataToDeviceList deviceList) {
        this.deviceList = deviceList;
    }

    public Map<String, Bitmap> getImagesMap() {
        return imagesMap;
    }

    public void setImagesMap(Map<String, Bitmap> imagesMap) {
        this.imagesMap = imagesMap;
    }
}
