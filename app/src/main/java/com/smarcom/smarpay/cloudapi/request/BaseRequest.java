package com.smarcom.smarpay.cloudapi.request;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;
import com.smarcom.smarpay.cloudapi.service.SPService;

public abstract class BaseRequest<RESULT> extends GoogleHttpClientSpiceRequest<RESULT> {

    private static final String PROTOCOL = "https";
    private static final String DOMAIN = "smarpay.azurewebsites.net"; //"touchwaitersrv2.azurewebsites.net"
    private static final String API_VERSION = "v2";
    private static final String API_PATH = "Smarpay";
    private SPService service;

    private long cacheExpiryDuration = DurationInMillis.ALWAYS_RETURNED;

    protected BaseRequest(Class<RESULT> clazz) {
        super(clazz);
    }

    protected String getProtocol() {
        return PROTOCOL + "://";
    }

    protected String getDomain() {
        return DOMAIN;
    }

    protected String buildUrl() {
        return PROTOCOL + "://" + DOMAIN  + "/api/" + API_VERSION + "/" + API_PATH + "/" + getResourceUri();
    }

    protected String buildUrl(String resource) {
        return PROTOCOL + "://" + DOMAIN  + "/api/" + API_VERSION + "/" + API_PATH + "/" + resource;
    }

    protected String buildUrl(String apiPath, String resource) {
        return PROTOCOL + "://" + DOMAIN + apiPath + resource;
    }

    public void setService(SPService spiceService) {
        this.service = spiceService;
    }

    public SPService getService() {
        return service;
    }

    public long getCacheExpiryDuration() {
        return cacheExpiryDuration;
    }

    public String getResourceUri() {
        return "";
    }
}
