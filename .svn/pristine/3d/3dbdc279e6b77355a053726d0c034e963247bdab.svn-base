package com.smarcom.smarpay.fragment;

import android.os.Bundle;
import com.octo.android.robospice.SpiceManager;
import com.smarcom.smarpay.cloudapi.service.SPService;

import roboguice.fragment.RoboFragment;

public abstract class BaseFragment extends RoboFragment {

    private SpiceManager spiceManager = new SpiceManager(SPService.class);
    private boolean isReplaceTransaction = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }

        super.onStop();
    }

    public boolean isReplaceTransaction() {
        return isReplaceTransaction;
    }

    public void setIsReplaceTransaction(boolean isReplaceTransaction) {
        this.isReplaceTransaction = isReplaceTransaction;
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
