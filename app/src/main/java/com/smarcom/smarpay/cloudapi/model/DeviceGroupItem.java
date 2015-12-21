package com.smarcom.smarpay.cloudapi.model;

import com.google.api.client.util.Key;

import java.util.List;

public class DeviceGroupItem {

    public enum BannerIconId {
        Smarpay,
        Selecta,
        Parking,
        Washroomr,
        Cigarettes
    }

    @Key("GroupId")
    private int groupId;
    @Key("Name")
    private String name;
    @Key("BannerIconId")
    private int bannerIconId;
    @Key("PaymentChannels")
    private List<PaymentChannel> paymentChannels;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBannerIconId() {
        return bannerIconId;
    }

    public void setBannerIconId(int bannerIconId) {
        this.bannerIconId = bannerIconId;
    }

    public List<PaymentChannel> getPaymentChannels() {
        return paymentChannels;
    }

    public void setPaymentChannels(List<PaymentChannel> paymentChannels) {
        this.paymentChannels = paymentChannels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceGroupItem)) return false;

        DeviceGroupItem that = (DeviceGroupItem) o;

        if (bannerIconId != that.bannerIconId) return false;
        if (groupId != that.groupId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (paymentChannels != null ? !paymentChannels.equals(that.paymentChannels) : that.paymentChannels != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + bannerIconId;
        result = 31 * result + (paymentChannels != null ? paymentChannels.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DeviceGroupItem{" +
                "groupId=" + groupId +
                ", name='" + name + '\'' +
                ", bannerIconId=" + bannerIconId +
                ", paymentChannels=" + paymentChannels +
                '}';
    }
}
