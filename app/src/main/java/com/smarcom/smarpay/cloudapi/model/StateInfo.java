package com.smarcom.smarpay.cloudapi.model;

import com.google.api.client.util.Key;

public class StateInfo {

    @Key("Alv")
    private int alv;
    @Key("Dlv")
    private int dlv;

    public StateInfo() {

    }

    public StateInfo(int alv, int dlv) {
        this.alv = alv;
        this.dlv = dlv;
    }

    public int getAlv() {
        return alv;
    }

    public void setAlv(int alv) {
        this.alv = alv;
    }

    public int getDlv() {
        return dlv;
    }

    public void setDlv(int dlv) {
        this.dlv = dlv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateInfo)) return false;

        StateInfo stateInfo = (StateInfo) o;

        if (alv != stateInfo.alv) return false;
        if (dlv != stateInfo.dlv) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = alv;
        result = 31 * result + dlv;
        return result;
    }

    @Override
    public String toString() {
        return "StateInfo{" +
                "alv=" + alv +
                ", dlv=" + dlv +
                '}';
    }
}
