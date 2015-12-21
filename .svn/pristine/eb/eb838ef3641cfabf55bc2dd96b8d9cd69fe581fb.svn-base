package com.smarcom.smarpay;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.smarcom.smarpay.cloudapi.model.AdvItem;
import com.smarcom.smarpay.cloudapi.model.CacheData;
import com.smarcom.smarpay.cloudapi.service.SPService;

public class SPApplication extends Application {

    private CacheData data;
    private AdvItem.AdvTrigger trigger;

    private int numOfRunningActivities = 0;
    private boolean wasInBackground = false;
    private boolean isInputActive = false;

    @Override
    public void onCreate() {
        super.onCreate();
        trigger = AdvItem.AdvTrigger.AppStartFinished;
        registerActivityLifecycleCallbacks(new SPActivityLifecycleCallbacks());
    }

    public void setData(CacheData data) {
        this.data = data;
    }

    public CacheData getData() {
        return data;
    }

    public AdvItem.AdvTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(AdvItem.AdvTrigger trigger) {
        this.trigger = trigger;
    }

    public boolean isRequiredUpdate() {
        return (data.getAdvListVersion() < SPService.getAdvVersion() || data.getDeviceListVersion() < SPService.getDevVersion());
    }

    public boolean isWasInBackground() {
        return wasInBackground;
    }

    public boolean isInputActive() {
        return isInputActive;
    }

    public void setIsInputActive(boolean isInputActive) {
        this.isInputActive = isInputActive;
    }

    private class SPActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (numOfRunningActivities == 0) {
                wasInBackground = true;
            } else {
                wasInBackground = false;
            }

            numOfRunningActivities++;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            numOfRunningActivities--;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}
