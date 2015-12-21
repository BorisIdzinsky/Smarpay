package com.smarcom.smarpay.cloudapi.service;

import android.app.Application;
import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.networkstate.NetworkStateChecker;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileBitmapObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.googlehttpclient.json.Jackson2ObjectPersisterFactory;
import com.octo.android.robospice.persistence.memory.LruCacheBitmapObjectPersister;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.smarcom.smarpay.cloudapi.model.CacheData;
import com.smarcom.smarpay.cloudapi.request.BaseRequest;
import java.util.Set;

public class SPService extends Jackson2GoogleHttpClientSpiceService {

    private SpiceManager spiceManager;
    private SPService service;
    private CacheData data = null;

    static private int advVersion = 0;
    static private int devVersion = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        service = this;
        CookieSyncManager.createInstance(this);
        spiceManager = new SpiceManager(SPService.class);
        spiceManager.start(this);
    }

    @Override
    public void onDestroy() {
        spiceManager.shouldStop();
        super.onDestroy();
    }

    @Override
    public void addRequest(CachedSpiceRequest<?> request, Set<RequestListener<?>> listRequestListener) {

        SpiceRequest spiceRequest = request.getSpiceRequest();

        if (spiceRequest instanceof BaseRequest) {
            ((BaseRequest)spiceRequest).setService(service);
        }

        super.addRequest(request, listRequestListener);
    }

    public synchronized CacheData getData() {
        return data;
    }

    public synchronized void setData(CacheData data) {
        this.data = data;
    }

    static public synchronized int getAdvVersion() {
        return SPService.advVersion;
    }

    static public synchronized void setAdvVersion(int advVersion) {
        SPService.advVersion = advVersion;
    }

    static public synchronized int getDevVersion() {
        return SPService.devVersion;
    }

    static public void setDevVersion(int devVersion) {
        SPService.devVersion = devVersion;
    }

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        cacheManager.addPersister(new LruCacheBitmapObjectPersister(new InFileBitmapObjectPersister(application), 1024 * 1024));
        cacheManager.addPersister(new Jackson2ObjectPersisterFactory(application));
        return cacheManager;
    }

    @Override
    protected NetworkStateChecker getNetworkStateChecker() {
        return new NetworkStateChecker() {

            @Override
            public boolean isNetworkAvailable( Context context ) {
                return true;
            }

            @Override
            public void checkPermissions( Context context ) {
                // do nothing
            }
        };
    }
}
