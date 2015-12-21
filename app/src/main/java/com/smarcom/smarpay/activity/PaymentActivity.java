package com.smarcom.smarpay.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.smarcom.smarpay.R;
import com.smarcom.smarpay.SPApplication;
import com.smarcom.smarpay.adapter.DeviceListAdapter;
import com.smarcom.smarpay.adapter.PaymentAdapter;
import com.smarcom.smarpay.cloudapi.model.AdvItem;
import com.smarcom.smarpay.cloudapi.model.CacheData;
import com.smarcom.smarpay.cloudapi.model.DeviceGroupItem;
import com.smarcom.smarpay.cloudapi.model.DeviceItem;
import com.smarcom.smarpay.cloudapi.model.PaymentChannel;
import com.smarcom.smarpay.cloudapi.request.CalculateDistancesRequest;
import com.smarcom.smarpay.fragment.WarningFragment;
import com.smarcom.smarpay.helper.BannerRunnableListener;
import com.smarcom.smarpay.helper.DeviceListAdapterListener;
import com.smarcom.smarpay.helper.DialogHelper;
import com.smarcom.smarpay.helper.FontHelper;
import com.smarcom.smarpay.helper.GoogleApiHelper;
import com.smarcom.smarpay.helper.LuhnHelper;
import com.smarcom.smarpay.helper.PayButtonListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import roboguice.util.temp.Ln;

@ContentView(R.layout.activity_payment)
public class PaymentActivity extends BaseActivity implements TextWatcher, Filter.FilterListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnFocusChangeListener, PayButtonListener, BannerRunnableListener, DeviceListAdapterListener {

    @InjectView(R.id.payment_tool_bar)
    private android.support.v7.widget.Toolbar toolbar;
    @InjectView(R.id.operatorslist)
    private RecyclerView recyclerView;
    @InjectView(R.id.device_code)
    private EditText editText;
    @InjectView(R.id.banner2)
    private ImageView banner;
    @InjectView(R.id.choose_account)
    private TextView chooseAccount;
    @InjectView(R.id.define_device)
    private TextView defineDeviceId;
    @InjectView(R.id.btn_qr)
    private ImageButton qrButton;
    @InjectView(R.id.device_list_view)
    RecyclerView deviceListView;
    @InjectView(R.id.linear_layout_device_list_view)
    LinearLayout linearLayout;


    @InjectResource(R.string.invalid_qr)
    private String invalidQr;
    @InjectResource(R.string.invalid_device_id)
    private String invalidDeviceId;
    @InjectResource(R.string.sms_to)
    private String smsToText;
    @InjectResource(R.string.sms_support_error)
    private String smsSupportError;
    @InjectResource(R.string.SMS_sent_error)
    private String messageSentError;

    private static final int EDIT_TEXT_MIN_LENGTH = 5;
    private static final int EDIT_TEXT_MAX_LENGTH = 7;
    private static final String ACTION = "com.smarcom.smarpay.Sent_Message";


    private List<DeviceListAdapter.DeviceItemWithDistance> deviceItemWithDistances = new ArrayList<>();
    private List<DeviceItem> deviceItems = new ArrayList<>();

    private String currentDeviceId;
    private String qrDeviceId;
    private String deviceId;

    private List<DeviceGroupItem> groups = new ArrayList<>();
    private GoogleApiClient googleApiClient;

    private Toast toast = null;
    private boolean isWasQRScanner = false;
    private boolean isHideKeyboard = false;

    private BroadcastReceiver receiver;
    private boolean isReceiverRegister;
    private boolean isWarningFragmentShown = false;

    private WarningFragment warningFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setSupportActionBar(toolbar);
        googleApiClient = GoogleApiHelper.getGoogleApiClient(this, this, this);

        warningFragment =  (WarningFragment) getSupportFragmentManager().findFragmentById(R.id.warning_fragment);
        getSupportFragmentManager().beginTransaction().hide(warningFragment).commit();

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        recyclerView.setAdapter(new PaymentAdapter(getApplicationContext(), this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editText.setTypeface(FontHelper.getRegularFont(this));
        editText.addTextChangedListener(this);

        editText.setOnFocusChangeListener(this);

        linearLayout.setVisibility(View.GONE);
        deviceListView.setAdapter(new DeviceListAdapter(this, this, DeviceListAdapter.MachineFilter.DeviceId));
        deviceListView.setLayoutManager(new LinearLayoutManager(this));
        deviceListView.setVisibility(View.INVISIBLE);

        chooseAccount.setTypeface(FontHelper.getBoldFont(this));
        defineDeviceId.setTypeface(FontHelper.getBoldFont(this));

        currentDeviceId = getIntent().getStringExtra(SearchActivity.DEVICE_ID_EXTRA);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.prev);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        getSupportFragmentManager().beginTransaction().show(warningFragment).addToBackStack(null).commit();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        showAlert(messageSentError);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        showAlert(messageSentError);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        showAlert(messageSentError);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        showAlert(messageSentError);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        clearEditTextFocus();

        CacheData data = getCacheData();

        if (data != null) {
            GoogleApiHelper.setupGoogleApiClientConnection(googleApiClient);

            deviceItems.clear();
            deviceItems.addAll(data.getDeviceList().getData().getDevices());

            updateDeviceItemsWithDistances(data);

            groups.addAll(data.getDeviceList().getData().getGroups());

            if (qrDeviceId != null) {
                processQrCode(qrDeviceId);
                qrDeviceId = null;
            }

            if (currentDeviceId != null) {
                processCurrentDeviceId(currentDeviceId);
                currentDeviceId = null;
            }
        }
    }

    @Override
    protected void onDataPrepared(CacheData data) {
        super.onDataPrepared(data);
        showWarningFragment();
        showKeyboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (isReceiverRegister) {
            try {
                unregisterReceiver(receiver);
                isReceiverRegister = false;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        super.onPause();
        hideKeyboard();
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (toast != null) {
            toast.cancel();
        }

        hideWarningFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        isWasQRScanner = true;
        isHideKeyboard = true;
        setIsBlockedSplash(true);
        clearEditTextFocus();

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            qrDeviceId = result.getContents();
        }
    }

    private void showWarningFragment() {
        if (isWarningFragmentShown) {
            getSupportFragmentManager().beginTransaction().show(warningFragment).commitAllowingStateLoss();
            isWarningFragmentShown = false;
        }

    }

    public void hideWarningFragment() {
        if (warningFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().hide(warningFragment).commitAllowingStateLoss();
            isWarningFragmentShown = true;
        }
    }

    private void processQrCode(String qr) {
        Matcher matcher = Pattern.compile("(^\\s|^.+\\s|^)(SMSTO:)([\\d]{1,9}):(\\w{5,7})(\\s$|\\s.+$|$)").matcher(qr);

        if (matcher.matches()) {
            long smsNumber = Long.parseLong(matcher.group(3));
            qr = matcher.group(4);

            if ((isDeviceIdValid(qr) && (atLeastOneAlpha(qr) || isLuhnValid(qr))) || containsDeviceId(deviceItems, qr)) {
                updateEditText(qr);
                onPayHandler(smsNumber);
            } else {
                DialogHelper.showErrorDialogFragment(getSupportFragmentManager(), invalidQr);
            }

        } else {
            qr = parseCurrentDeviceId(qr);

            if ((isDeviceIdValid(qr) && (atLeastOneAlpha(qr) || isLuhnValid(qr))) || containsDeviceId(deviceItems, qr)) {
                updateEditText(qr);

            } else if (!qr.isEmpty()) {
                DialogHelper.showErrorDialogFragment(getSupportFragmentManager(), invalidQr);
            }
        }
    }

    private void processCurrentDeviceId(String str) {
        if ((isDeviceIdValid(str) && (atLeastOneAlpha(str) || isLuhnValid(str)) || containsDeviceId(deviceItems, str))) {
            updateEditText(str);
        } else {
            DialogHelper.showErrorDialogFragment(getSupportFragmentManager(), invalidDeviceId);
        }
    }

    private boolean isLuhnValid(String str) {
        return TextUtils.isDigitsOnly(str) && LuhnHelper.isLuhnValid(str);
    }

    private boolean atLeastOneAlpha(String str) {
        return str.matches(".*[a-zA-Z]+.*");
    }

    private boolean isCompletelyValid(String str) {
        return ((isDeviceIdValid(str) && (atLeastOneAlpha(str) || isLuhnValid(str))) || containsDeviceId(deviceItems, str));
    }

    private boolean isDeviceIdValid(String deviceId) {

        boolean valid;

        valid = !TextUtils.isEmpty(deviceId) && !(deviceId.length() < EDIT_TEXT_MIN_LENGTH || deviceId.length() > EDIT_TEXT_MAX_LENGTH) && deviceId.matches("^[a-zA-Z0-9]*$");

        return valid;
    }

    private boolean containsDeviceId(List<DeviceItem> items, String deviceId) {
        boolean contain = false;
        for (DeviceItem item : items) {
            if (item.getDeviceId().equals(deviceId)) {
                contain = true;
            }
        }

        return contain;
    }

    private String parseCurrentDeviceId(String string) {
        Matcher urlMatcher = Pattern.compile("(^\\s|^.+\\s|^)(http|https):\\/\\/.*(\\?device|\\?.*=.*&device)=(\\w{5,7})(\\s$|\\s.+$|&.*$|$)").matcher(string);
        if (urlMatcher.matches()) {
            string = urlMatcher.group(4);
        }

        return string;
    }

    private void updateEditText(String text) {
        hideDeviceList();
        editText.getText().clear();
        editText.setText(text);
    }

    private int getGroupId(String deviceId) {
        for (DeviceItem item : deviceItems) {
            if (item.getDeviceId().equals(deviceId)) {
                return item.getGroupId();
            }
        }
        return 0;
    }

    private DeviceGroupItem getGroupById(int groupId) {
        for (DeviceGroupItem group : groups) {
            if (group.getGroupId() == groupId) {
                return group;
            }
        }
        return null;
    }

    private void filterDeviceListAdapter(String s) {
        DeviceListAdapter adapter = (DeviceListAdapter) deviceListView.getAdapter();

        if (!TextUtils.isEmpty(s)) {
            adapter.getFilter().filter(s, this);
        } else {
            deviceListView.setVisibility(View.INVISIBLE);
        }
    }

    private void showDeviceList() {
        banner.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        chooseAccount.setText(getResources().getString(R.string.matching_devices));
        recyclerView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        deviceListView.setVisibility(View.INVISIBLE);
    }

    private void hideDeviceList() {
        banner.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        chooseAccount.setText(getResources().getString(R.string.choose_string));
        recyclerView.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        deviceListView.setVisibility(View.GONE);
    }

    private void clearEditTextFocus() {
        editText.clearFocus();
    }

    private void showAlert(String message) {
        if (toast != null) {
            toast.setText(message);
            toast.show();
        }
    }
    @Override
    protected int getContentId() {
        return R.id.payment_activity_layout;
    }

    protected ImageView getBannerView() {
        return banner;
    }

    protected boolean isLooped() {
        return true;
    }

    protected AdvItem.AdvTarget getTarget() {
        return AdvItem.AdvTarget.Banner2;
    }

    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String deviceId = s.toString();

        filterDeviceListAdapter(deviceId);

        PaymentAdapter adapter = (PaymentAdapter) recyclerView.getAdapter();
        List<PaymentChannel> paymentChannels = adapter.getData();
        paymentChannels.clear();

        editText.setBackgroundResource(R.drawable.payment_text_field);
        qrButton.setBackgroundResource(R.drawable.qr_btn);

        if (containsDeviceId(deviceItems, s.toString())) {
            paymentChannels.addAll(getGroupById(getGroupId(deviceId)).getPaymentChannels());
        } else if (isDeviceIdValid(deviceId) && (atLeastOneAlpha(deviceId) || isLuhnValid(deviceId))) {
            paymentChannels.addAll(getGroupById(0).getPaymentChannels());
        } else {
            if (!TextUtils.isEmpty(deviceId)) {
                editText.setBackgroundResource(R.drawable.payment_text_field_active);
                qrButton.setBackgroundResource(R.drawable.qr_btn_active);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPayHandler(long smsNumber) {
        sendMessage(smsNumber);
    }

    @Override
    public void onCompleteShowingBanners() {
        Ln.v("Banners are shown");
    }

    public void clearBtnHandler(View v) {
        editText.setBackgroundResource(R.drawable.payment_text_field);
        qrButton.setBackgroundResource(R.drawable.qr_btn);
        editText.getText().clear();
    }

    public void scanBtnHandler(View v) {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }

    private void hideKeyboard() {
        ((SPApplication) getApplication()).setIsInputActive(editText.isFocused());
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void showKeyboard() {
        if (((SPApplication) getApplication()).isInputActive() && !isHideKeyboard) {
            editText.requestFocus();

            showDeviceList();
            filterDeviceListAdapter(editText.getText().toString());

            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
            ((SPApplication) getApplication()).setIsInputActive(false);

        } else {

            isHideKeyboard = false;
        }
    }

    public void sendMessage(long smsNumber) {
        if (checkForSmsSupport()) {
            deviceId = editText.getText().toString();

            PendingIntent sent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION), 0);

            if (!isReceiverRegister) {
                registerReceiver(receiver, new IntentFilter(ACTION));
                isReceiverRegister = true;
            }

            String smsCommand = smsToText + ":" + smsNumber + ":" + deviceId + " Q";

            SmsManager.getDefault().sendTextMessage(Long.toString(smsNumber), null, smsCommand, sent, null);

        } else {
            DialogHelper.showErrorDialogFragment(getSupportFragmentManager(), smsSupportError);
        }
    }

    private boolean checkForSmsSupport() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showDeviceList();
            filterDeviceListAdapter(editText.getText().toString());
        } else {
            hideDeviceList();
        }
    }

    @Override
    public void onAdapterItemClick(String deviceId) {
        clearEditTextFocus();
        hideKeyboard();

        editText.setText(deviceId);

        editText.setBackgroundResource(R.drawable.payment_text_field);
        qrButton.setBackgroundResource(R.drawable.qr_btn);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {

            case KeyEvent.KEYCODE_BACK:
                clearEditTextFocus();

                if (!isWasQRScanner && !warningFragment.isVisible()) {
                    showMainActivity(null);
                }

                break;

            case KeyEvent.KEYCODE_ENTER:
                String deviceId = editText.getText().toString();
                if (!deviceId.isEmpty() && !isCompletelyValid(deviceId)) {
                    DialogHelper.showErrorDialogFragment(getSupportFragmentManager(), invalidDeviceId);
                }
                clearEditTextFocus();
                break;

            default:
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void calculateDistanceRequest(Location currentLocation) {
        getSpiceManager().execute(new CalculateDistancesRequest(currentLocation, deviceItemWithDistances), new RequestListener<List<DeviceListAdapter.DeviceItemWithDistance>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
            }

            @Override
            public void onRequestSuccess(List<DeviceListAdapter.DeviceItemWithDistance> list) {
                if (!list.isEmpty())
                    updateDistances(list);
            }
        });
    }

    private void updateDistances(List<DeviceListAdapter.DeviceItemWithDistance> list) {
        DeviceListAdapter adapter = (DeviceListAdapter) deviceListView.getAdapter();

        deviceItemWithDistances = list;

        adapter.notifyDataSetChanged();
    }

    private void updateDeviceItemsWithDistances(CacheData data) {
        DeviceListAdapter adapter = (DeviceListAdapter) deviceListView.getAdapter();
        deviceItemWithDistances = adapter.getData();
        deviceItemWithDistances.clear();

        for (DeviceItem item : data.getDeviceList().getData().getDevices()) {
            deviceItemWithDistances.add(new DeviceListAdapter.DeviceItemWithDistance(item));
        }
    }

    public void showMainActivity(AdvItem.AdvTrigger trigger) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (trigger != null) {
            intent.putExtra(TRIGGER, trigger.ordinal());
        }

        startActivity(intent);
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

    @Override
    public void onFilterComplete(int count) {
        deviceListView.setVisibility(View.VISIBLE);
    }
}