package com.smarcom.smarpay.cloudapi.model;

import com.google.api.client.util.Key;

import java.util.List;

public class AdvList {

    @Key("Version")
    private int version;
    @Key("Items")
    private List<AdvItem> items;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<AdvItem> getItems() {
        return items;
    }

    public void setItems(List<AdvItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdvList)) return false;

        AdvList advList = (AdvList) o;

        if (version != advList.version) return false;
        if (items != null ? !items.equals(advList.items) : advList.items != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = version;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdvList{" +
                "version=" + version +
                ", items=" + items +
                '}';
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
