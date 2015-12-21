package com.smarcom.smarpay.cloudapi.model;

import com.google.api.client.util.Key;

public class AdvItem {

    public enum AdvTarget {
        Splash,
        SplashSkip,
        Banner1,
        Banner2,
    }

    public enum AdvTrigger {
        AppStartFinished,
        AppResumed,
        PaymentPanelStarted,
        PaymentPanelResumed,
        DeviceSelectionFinished,
        PaymentFinished
    }

    @Key("Trigger")
    private int trigger;
    @Key("Target")
    private int target;
    @Key("DurationMs")
    private int durationMs;
    @Key("Img")
    private String img;

    public int getTrigger() {
        return trigger;
    }

    public AdvTrigger getTriggerName() {
        return AdvTrigger.values()[trigger];
    }

    public void setTrigger(int trigger) {
        this.trigger = trigger;
    }

    public int getTarget() {
        return target;
    }

    public AdvTarget getTargetName() {
        return AdvTarget.values()[target];
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdvItem)) return false;

        AdvItem advItem = (AdvItem) o;

        if (durationMs != advItem.durationMs) return false;
        if (target != advItem.target) return false;
        if (trigger != advItem.trigger) return false;
        if (img != null ? !img.equals(advItem.img) : advItem.img != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = trigger;
        result = 31 * result + target;
        result = 31 * result + durationMs;
        result = 31 * result + (img != null ? img.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AdvItem{" +
                "trigger=" + trigger +
                ", target=" + target +
                ", durationMs=" + durationMs +
                ", img='" + img + '\'' +
                '}';
    }
}
