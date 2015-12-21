package com.smarcom.smarpay.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.smarcom.smarpay.R;

import com.smarcom.smarpay.cloudapi.model.CacheData;
import com.smarcom.smarpay.cloudapi.request.FetchNearestDevicesFromCacheRequest;
import com.smarcom.smarpay.helper.BannerRunnableListener;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.smarcom.smarpay.cloudapi.model.AdvItem;
import com.smarcom.smarpay.helper.GoogleApiHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements ConnectionCallbacks, View.OnClickListener, OnConnectionFailedListener, BannerRunnableListener {

    @InjectView(R.id.imageView2)
    private ImageView banner;

    @InjectResource(R.array.smar_cue_buttons_ids)
    private String[] smarCueButtonsIds;
    @InjectResource(R.array.smar_cue_text_views_ids)
    private String[] smarCueTextViewIds;

    private static final String  DEF_TYPE = "id";

    private GoogleApiClient googleApiClient;
    private Map<Integer, String> deviceIdMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (GoogleApiHelper.checkPlayServices(this)) {
           googleApiClient = GoogleApiHelper.getGoogleApiClient(this, this, this);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        hideCueBar();
    }


    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiHelper.checkPlayServices(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected int getContentId() {
        return R.id.main_activity_layout;
    }

    @Override
    protected void onDataPrepared(CacheData data) {
        super.onDataPrepared(data);
        GoogleApiHelper.setupGoogleApiClientConnection(googleApiClient);
    }

    protected ImageView getBannerView() {
        return banner;
    }

    protected boolean isLooped() {
        return true;
    }

    protected AdvItem.AdvTarget getTarget() {
        return AdvItem.AdvTarget.Banner1;
    }

    private void loadNearestDeviceFromCache(Location currentLocation) {
        getSpiceManager().execute(new FetchNearestDevicesFromCacheRequest(currentLocation), new RequestListener<List<String>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Ln.e("test");
            }

            @Override
            public void onRequestSuccess(List<String> strings) {
                if (strings != null && !strings.isEmpty()) {
                    updateSmarCueBar(strings);
                }
            }
        });
    }

    private void updateSmarCueBar(List<String> data) {
        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                int btnId = getResources().getIdentifier(smarCueButtonsIds[i], DEF_TYPE, getPackageName());
                int textViewId = getResources().getIdentifier(smarCueTextViewIds[i], DEF_TYPE, getPackageName());
                deviceIdMap.put(btnId, data.get(i));
                showCueButton(btnId, textViewId, data.get(i));
            }
        }
    }

    private void showCueButton(int btnId, int textViewId, String text) {
        ImageButton imageButton = (ImageButton) findViewById(btnId);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(this);

        TextView textView = (TextView) findViewById(textViewId);
        textView.setVisibility(View.VISIBLE);
        textView.setText(text);
    }

    private void hideCueBar() {
        hideCueTextViews();
        hideCueButtons();
    }

    private void hideCueButtons() {
        for (String item : smarCueButtonsIds) {
            int id = getResources().getIdentifier(item, DEF_TYPE, getPackageName());
            ImageButton imageButton = (ImageButton) findViewById(id);
            imageButton.setVisibility(View.GONE);
        }
    }

    private void hideCueTextViews() {
        for (String item : smarCueTextViewIds) {
            int id = getResources().getIdentifier(item, DEF_TYPE, getPackageName());
            TextView textView = (TextView) findViewById(id);
            textView.setVisibility(View.GONE);
        }
    }

    private void startPaymentActivity(String deviceId) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(SearchActivity.DEVICE_ID_EXTRA, deviceId);
        startActivity(intent);
    }

    public void searchBtnHandler(View view) {
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
    }

    public void payBtnHandler(View view) {
        startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (currentLocation != null) {
            loadNearestDeviceFromCache(currentLocation);
       }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onClick(View v) {
        startPaymentActivity(deviceIdMap.get(v.getId()));
    }
}
