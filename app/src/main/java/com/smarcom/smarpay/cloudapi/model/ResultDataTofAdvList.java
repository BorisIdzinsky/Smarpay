package com.smarcom.smarpay.cloudapi.model;

import com.google.api.client.util.Key;

public class ResultDataTofAdvList {

    @Key("Data")
    private AdvList data;
    @Key("State")
    private String state;
    @Key("ErrorMessage")
    private String errorMessage;

    public AdvList getData() {
        return data;
    }

    public void setData(AdvList data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultDataTofAdvList)) return false;

        ResultDataTofAdvList that = (ResultDataTofAdvList) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null)
            return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = data != null ? data.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResultDataTofAdvList{" +
                "data=" + data +
                ", state='" + state + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
