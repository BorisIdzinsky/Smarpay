package com.smarcom.smarpay.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.smarcom.smarpay.R;

import com.smarcom.smarpay.fragment.BaseFragment;
import com.smarcom.smarpay.fragment.DeviceListFragment;
import com.smarcom.smarpay.fragment.MapFragment;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {

    @InjectView(R.id.tool_bar)
    private Toolbar toolbar;

    @InjectView(R.id.map_search_btn)
    private ImageButton mapButton;

    @InjectView(R.id.marker_list_btn)
    private ImageButton markerListButton;

    public final static String DEVICE_ID_EXTRA = "com.smarcom.smarpay.DeviceId";

    private DeviceListFragment deviceListFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        deviceListFragment = new DeviceListFragment();
        mapFragment = new MapFragment();

        markerListButton.setActivated(true);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, deviceListFragment).commit();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.prev);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void replaceCurrentFragment (Fragment fragment) {
        BaseFragment baseFragment = (BaseFragment) fragment;
        baseFragment.setIsReplaceTransaction(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void mapSearchBtnHandler(View v) {
        mapButton.setActivated(true);
        markerListButton.setActivated(false);
        replaceCurrentFragment(mapFragment);
    }

    public void markerListBtnHandler(View v) {
        mapButton.setActivated(false);
        markerListButton.setActivated(true);
        replaceCurrentFragment(deviceListFragment);
    }

    @Override
    protected int getContentId() {
        return R.id.search_activity_layout;
    }
}