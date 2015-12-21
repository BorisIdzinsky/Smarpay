package com.smarcom.smarpay.cloudapi.model;

import com.google.api.client.util.Key;

import java.util.List;

public class DeviceList {

    @Key("Version")
    private int version;
    @Key("Devices")
    private List<DeviceItem> devices;
    @Key("Groups")
    private List<DeviceGroupItem> groups;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<DeviceItem> getDevices() {
        return devices;
    }

//    public void setDevices(List<DeviceItem> devices) {
//        this.devices = devices;
//    }

    public List<DeviceGroupItem> getGroups() {
        return groups;
    }

//    public void setGroups(List<DeviceGroupItem> groups) {
//        this.groups = groups;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceList)) return false;

        DeviceList that = (DeviceList) o;

        if (version != that.version) return false;
        if (devices != null ? !devices.equals(that.devices) : that.devices != null) return false;
        if (groups != null ? !groups.equals(that.groups) : that.groups != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = version;
        result = 31 * result + (devices != null ? devices.hashCode() : 0);
        result = 31 * result + (groups != null ? groups.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeviceList{" +
                "version=" + version +
                ", devices=" + devices +
                ", groups=" + groups +
                '}';
    }
}
