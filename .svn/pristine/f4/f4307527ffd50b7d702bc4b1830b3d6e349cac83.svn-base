package com.smarcom.smarpay.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.smarcom.smarpay.R;
import com.smarcom.smarpay.SPApplication;
import com.smarcom.smarpay.cloudapi.helper.FormatHelper;
import com.smarcom.smarpay.cloudapi.model.AdvItem;
import com.smarcom.smarpay.cloudapi.model.CacheData;
import com.smarcom.smarpay.cloudapi.model.ClientAction;
import com.smarcom.smarpay.cloudapi.request.FetchDataFromCacheRequest;
import com.smarcom.smarpay.cloudapi.request.LoginRequest;
import com.smarcom.smarpay.cloudapi.request.ReloadCacheRequest;
import com.smarcom.smarpay.cloudapi.service.SPService;
import com.smarcom.smarpay.fragment.SplashFragment;
import com.smarcom.smarpay.helper.BannerHelper;
import com.smarcom.smarpay.helper.BannerRunnableListener;
import com.smarcom.smarpay.helper.CacheHelper;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public abstract class BaseActivity extends RoboActionBarActivity implements BannerRunnableListener {

    @InjectView(R.id.default_splash_main_activity)
    private ImageView splashView;

    public static final String TRIGGER = "trigger";

    private static final String MOBILE_NR = "spdefaultuser";
    private static final String DEVICE_HW_ID = "xyz-xyzx-yzx-yzxyzxy4";

    private static final String PASSWORD = "8972ff3a6e6d1338c9e344519aa309a3";
    private static final int DEVICE_TYPE = 3;
    private static final String DEVICE_NAME_HR = null;
    private static final String USER_NAME = null;

    private boolean isBlockedSplash = false;
    private AdvItem.AdvTrigger intentTrigger = null;

    private SpiceManager spiceManager = new SpiceManager(SPService.class);
    private SplashFragment splashFragment;

    private Handler showBannerHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(TRIGGER)) {
            Bundle bundle = getIntent().getExtras();
            int triggerOrdinal = bundle.getInt(TRIGGER);
            intentTrigger = AdvItem.AdvTrigger.values()[triggerOrdinal];
        }

        splashFragment = (SplashFragment) getSupportFragmentManager().findFragmentById(R.id.splash_container);
    }

    @Override
    protected void onStart() {
        boolean isFirstRun = isFirstRun(); // Always call first!
        spiceManager.start(this);
        super.onStart();

        hideAllContent();
        CacheData data = getCacheData();

        if (isFirstRun) {
            reloadCache();
        } else if (data == null) {
            loadCache();
        } else if (((SPApplication) getApplication()).isWasInBackground()) {
            if (((SPApplication) getApplication()).isRequiredUpdate()) {
                loadCache();
            } else {
                login();
                if (!showSplash(data)) {
                    run();
                }
            }
        } else {
            if (!showSplash(data, intentTrigger)) {
                run();
            }
        }

        intentTrigger = null;
    }

    private void loadCache() {
        getSpiceManager().execute(new FetchDataFromCacheRequest(), new FetchDataRequestListener());
    }

    private void reloadCache() {
        getSpiceManager().execute(new ReloadCacheRequest(), new FetchDataRequestListener());
    }

    public CacheData getCacheData() {
        SPApplication application = (SPApplication) getApplication();
        return application.getData();
    }

    private boolean isFirstRun() {
        return (!CacheHelper.isInitCache(this) || CacheHelper.isClearCache(this));
    }

    public boolean isBlockedSplash() {
        return isBlockedSplash;
    }

    public void setIsBlockedSplash(boolean isBlockedSplash) {
        this.isBlockedSplash = isBlockedSplash;
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        SPApplication app = (SPApplication)getApplication();

        if(app.getTrigger() == AdvItem.AdvTrigger.AppStartFinished) {
            app.setTrigger(AdvItem.AdvTrigger.AppResumed);
        }

        super.onStop();
    }

    protected int getContentId() {
        return 0;
    }

    protected ImageView getBannerView() {
        return null;
    }

    protected boolean isLooped() {
        return false;
    }

    protected AdvItem.AdvTarget getTarget() {
        return AdvItem.AdvTarget.Splash;
    }

    public void onCompleteShowingBanners() {

    }

    private void setContentVisibility(boolean isVisible) {
        if (getContentId() > 0) {
            View contentView = findViewById(getContentId());
            contentView.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }

    public void showSplashFragment() {
        if (splashFragment != null) {
            setContentVisibility(false);
            splashView.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().show(splashFragment).commit();
        }
    }

    public void hideSplashFragment() {
        if (splashFragment != null) {
            setContentVisibility(true);
            splashView.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().hide(splashFragment).commit();
            run();
        }
    }

    private void hideAllContent() {
        if (splashFragment != null) {
            setContentVisibility(false);
            splashView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().hide(splashFragment).commit();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    protected void onCompleteLoadCache(CacheData data) {
        ((SPApplication) getApplication()).setData(data);
        login();

        if (!showSplash(data)) {
            run();
        }
    }

    private boolean showSplash(CacheData data, AdvItem.AdvTrigger trigger) {
        boolean isShowing = false;

        AdvItem item = BannerHelper.getSplashItemForTrigger(data, trigger);

        if (item != null && splashFragment != null && !isBlockedSplash()) {
            Map<Bitmap, Long> splashMap = new HashMap<>();
            Map<String, Bitmap> imgMap = data.getImagesMap();

            Bitmap img = imgMap.get(FormatHelper.replaceSlashes(item.getImg()));
            long duration = (img != null) ? (long) item.getDurationMs() : 0L;

            splashMap.put(img, duration);

            splashFragment.setSplashMap(splashMap);
            splashFragment.setTarget(item.getTargetName());

            showSplashFragment();
            isShowing = true;
        } else {
            setIsBlockedSplash(false);
        }

        return  isShowing;
    }

    private boolean showSplash(CacheData data) {
        AdvItem.AdvTrigger trigger = ((SPApplication) getApplication()).getTrigger();
        return showSplash(data, trigger);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void run() {
        CacheData data = getCacheData();

        if (data != null) {
            View banner = getBannerView();

            if (banner != null) {

                if (showBannerHandler != null) {
                    showBannerHandler.removeCallbacksAndMessages(null);
                    showBannerHandler = null;
                }

                showBannerHandler = new Handler();

                Map<Bitmap, Long> banners = BannerHelper.getBannersForTargets(data, getTarget());
                BannerHelper.showBanners(banners, showBannerHandler, getBannerView(), isLooped(), this);
            }
        }

        onDataPrepared(data);
        setContentVisibility(true);
        splashView.setVisibility(View.GONE);
    }

    protected void onDataPrepared(CacheData data) {
    }

    private void login() {
        try {
            LoginRequest request = new LoginRequest(MOBILE_NR, DEVICE_HW_ID, PASSWORD, DEVICE_TYPE, DEVICE_NAME_HR, USER_NAME, true);
            getSpiceManager().execute(request, new RequestListener<ClientAction>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                }

                @Override
                public void onRequestSuccess(ClientAction clientAction) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class FetchDataRequestListener implements RequestListener<CacheData> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
        }

        @Override
        public void onRequestSuccess(CacheData data) {
            CacheHelper.setIsInitCache(BaseActivity.this, true);
            onCompleteLoadCache(data);
        }

    }
}
