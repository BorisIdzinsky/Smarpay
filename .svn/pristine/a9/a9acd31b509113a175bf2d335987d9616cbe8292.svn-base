package com.smarcom.smarpay.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.smarcom.smarpay.R;
import com.smarcom.smarpay.SPApplication;
import com.smarcom.smarpay.activity.PaymentActivity;
import com.smarcom.smarpay.activity.SearchActivity;
import com.smarcom.smarpay.adapter.DeviceListAdapter;
import com.smarcom.smarpay.cloudapi.model.CacheData;
import com.smarcom.smarpay.cloudapi.model.DeviceItem;

import com.smarcom.smarpay.cloudapi.request.CalculateDistancesRequest;
import com.smarcom.smarpay.helper.DeviceListAdapterListener;
import com.smarcom.smarpay.helper.FontHelper;
import com.smarcom.smarpay.helper.GoogleApiHelper;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

public class DeviceListFragment extends BaseFragment implements TextWatcher, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DeviceListAdapterListener {

    @InjectView(R.id.searchlist)
    private RecyclerView recyclerView;

    @InjectView(R.id.device_list_search_field)
    EditText searchField;

    private GoogleApiClient googleApiClient;
    private List<DeviceListAdapter.DeviceItemWithDistance> deviceItemWithDistances = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleApiClient = GoogleApiHelper.getGoogleApiClient(getActivity(), this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DeviceListAdapter deviceListAdapter = new DeviceListAdapter(getActivity(), this);
        recyclerView.setAdapter(deviceListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchField.setTypeface(FontHelper.getRegularFont(getActivity()));
        searchField.addTextChangedListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onStart() {
        super.onStart();

        SPApplication application = (SPApplication) getActivity().getApplication();
        CacheData data = application.getData();

        if (data != null) {
            GoogleApiHelper.setupGoogleApiClientConnection(googleApiClient);

            updateDeviceItems(data);
        }

        String text = searchField.getText().toString();

        if (isReplaceTransaction()) {
            searchField.getText().clear();
            setIsReplaceTransaction(false);
        } else if (application.isWasInBackground() && !TextUtils.isEmpty(text)) {
            filterDeviceListAdapter(text);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void filterDeviceListAdapter(String s) {
        DeviceListAdapter adapter = (DeviceListAdapter) recyclerView.getAdapter();
        adapter.getFilter().filter(s);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (searchField.isFocused()) {
            filterDeviceListAdapter(s.toString());
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (currentLocation != null) {
            calculateDistanceRequest(currentLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private void calculateDistanceRequest(Location currentLocation) {
        getSpiceManager().execute(new CalculateDistancesRequest(currentLocation, deviceItemWithDistances), new RequestListener<List<DeviceListAdapter.DeviceItemWithDistance>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
            }

            @Override
            public void onRequestSuccess(List<DeviceListAdapter.DeviceItemWithDistance> devicesWithDistances) {
                if (!devicesWithDistances.isEmpty()) {
                    updateDistances(devicesWithDistances);
                }
            }
        });
    }

    private void updateDistances(List<DeviceListAdapter.DeviceItemWithDistance> list) {
        DeviceListAdapter adapter = (DeviceListAdapter) recyclerView.getAdapter();

        deviceItemWithDistances = list;

        adapter.notifyDataSetChanged();
    }

    private void updateDeviceItems(CacheData data) {
        DeviceListAdapter adapter = (DeviceListAdapter) recyclerView.getAdapter();
        deviceItemWithDistances = adapter.getData();
        deviceItemWithDistances.clear();

        for (DeviceItem item : data.getDeviceList().getData().getDevices()) {
            deviceItemWithDistances.add(new DeviceListAdapter.DeviceItemWithDistance(item));
        }
    }

    private void hideKeyboard() {
        ((SPApplication) getActivity().getApplication()).setIsInputActive(searchField.isFocused());
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchField.getWindowToken(), 0);
    }


    @Override
    public void onAdapterItemClick(String deviceId) {
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(SearchActivity.DEVICE_ID_EXTRA, deviceId);
        startActivity(intent);

    }
}



