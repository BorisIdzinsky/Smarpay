package com.smarcom.smarpay.cloudapi.model;

import com.google.api.client.util.Key;

public class PaymentChannel {

    public enum ChannelIconId {
        ChannelIconId,
        PostFinance
    }

    private static final String NEW_LINE_SYMBOL = "{n}";

    @Key("SmsNumber")
    private int smsNumber;
    @Key("Text")
    private String text;
    @Key("ChannelIconId")
    private int channelIconId;

    public int getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(int smsNumber) {
        this.smsNumber = smsNumber;
    }

    public String getText() {
        return text.replace(NEW_LINE_SYMBOL, "\n");
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getChannelIconId() {
        return channelIconId;
    }

    public void setChannelIconId(int channelIconId) {
        this.channelIconId = channelIconId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentChannel)) return false;

        PaymentChannel that = (PaymentChannel) o;

        if (channelIconId != that.channelIconId) return false;
        if (smsNumber != that.smsNumber) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = smsNumber;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + channelIconId;
        return result;
    }

    @Override
    public String toString() {
        return "PaymentChannel{" +
                "smsNumber=" + smsNumber +
                ", text='" + text + '\'' +
                ", channelIconId=" + channelIconId +
                '}';
    }
}
