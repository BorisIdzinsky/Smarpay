package com.smarcom.smarpay.cloudapi.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.octo.android.robospice.request.ProgressByteProcessor;
import com.smarcom.smarpay.cloudapi.model.AdvItem;
import com.smarcom.smarpay.cloudapi.model.AdvList;
import com.smarcom.smarpay.cloudapi.model.ClientAction;
import com.smarcom.smarpay.cloudapi.model.ResultDataToDeviceList;
import com.smarcom.smarpay.cloudapi.model.ResultDataTofAdvList;
import com.smarcom.smarpay.cloudapi.model.StateInfo;
import com.smarcom.smarpay.cloudapi.service.SPService;
import com.smarcom.smarpay.cloudapi.helper.FormatHelper;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


import roboguice.util.temp.Ln;

public class LoginRequest extends BaseRequest<ClientAction> {

    private static final String API_RESOURCE = "LoginOrReg";
    private static final String STATE_INFO_REQUEST_RESOURCE = "StateInfo";
    private static final String ADVERTISEMENT_REQUEST_RESOURCE = "AdvertisementList";
    private static final String DEVICE_REQUEST_RESOURCE = "DeviceList2";
    private static final String IMG_API_PATH = "/Image/File?imagePath=";
    private static final int BUF_SIZE = 4096;

    private JSONObject deviceLoginJson;
    private boolean isCheckVersion = false;

    public LoginRequest(String mobileNr, String deviceHwId, String password, int deviceType, String deviceNameHr, String userName, boolean isCheckVersion) throws JSONException {
        super(ClientAction.class);

        this.isCheckVersion = isCheckVersion;

        deviceLoginJson = new JSONObject();
        deviceLoginJson.put("MobileNr", mobileNr);
        deviceLoginJson.put("DeviceHwId", deviceHwId);
        deviceLoginJson.put("Password", password);
        deviceLoginJson.put("DeviceType", deviceType);
        deviceLoginJson.put("DeviceNameHr", deviceNameHr != null ? deviceNameHr : JSONObject.NULL);
        deviceLoginJson.put("UserName", userName != null ? userName : JSONObject.NULL);

    }

    @Override
    public String getResourceUri() {
        return API_RESOURCE;
    }

    @Override
    public long getCacheExpiryDuration() {
        return DurationInMillis.ALWAYS_EXPIRED;
    }

    @Override
    public ClientAction loadDataFromNetwork() throws Exception {
        HttpRequest request = getHttpRequestFactory().buildPostRequest(new GenericUrl(buildUrl()), ByteArrayContent.fromString("application/json", deviceLoginJson.toString()));
        request.setParser(new JacksonFactory().createJsonObjectParser());
        HttpResponse response = request.execute();

        List<String> cookies = request.getResponseHeaders().getHeaderStringValues("set-cookie");

        CookieManager cookieManager = CookieManager.getInstance();
        for (String cookieString : cookies) {
            cookieManager.setCookie(getDomain(), cookieString);
        }

        String cookieString = cookieManager.getCookie(getProtocol() + getDomain());

        ClientAction clientAction = response.parseAs(ClientAction.class);
        StateInfo stateInfo = clientAction.getState();

        if (isCheckVersion) {
            checkAndUpdateResources(stateInfo, cookieString);
        }

        return clientAction;
    }

    private void checkAndUpdateResources(StateInfo stateInfo, String cookie) {
        SPService service = getService();

        if (service != null) {
            try {
                boolean invalidate = false;

                StateInfo currentStateInfo = service.getDataFromCache(StateInfo.class, STATE_INFO_REQUEST_RESOURCE);

                if (stateInfo.getAlv() > currentStateInfo.getAlv()) {
                    ResultDataTofAdvList resultDataTofAdvList = getDataFromNetwork(ResultDataTofAdvList.class, ADVERTISEMENT_REQUEST_RESOURCE, cookie);
                    service.putDataInCache(ADVERTISEMENT_REQUEST_RESOURCE, resultDataTofAdvList);
                    loadImagesFromNetwork(resultDataTofAdvList.getData());
                    invalidate = true;
                }

                if (stateInfo.getDlv() > currentStateInfo.getDlv()) {
                    ResultDataToDeviceList resultDataToDeviceList = getDataFromNetwork(ResultDataToDeviceList.class, DEVICE_REQUEST_RESOURCE, cookie);
                    service.putDataInCache(DEVICE_REQUEST_RESOURCE, resultDataToDeviceList);
                    invalidate = true;
                }

                service.putDataInCache(STATE_INFO_REQUEST_RESOURCE, stateInfo);

                if (invalidate) {
                    service.setData(null);
                    SPService.setAdvVersion(stateInfo.getAlv());
                    SPService.setDevVersion(stateInfo.getDlv());
                }

            } catch (IOException | CacheCreationException | CacheLoadingException | CacheSavingException e) {
                e.printStackTrace();
            }
        }
    }

    private <T> T getDataFromNetwork(Class<T> clazz, String resource, String cookie) throws IOException {
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(buildUrl(resource)));
        request.setParser(new JacksonFactory().createJsonObjectParser());
        request.getHeaders().setCookie(cookie);
        HttpResponse response = request.execute();
        return response.parseAs(clazz);
    }

    private void loadImagesFromNetwork(AdvList advList) {
        if (advList != null && !advList.isEmpty()) {
            for (AdvItem item : advList.getItems()) {
                String imgPath = item.getImg();
                Bitmap bitmap = getImageFromNetwork(imgPath, 1);
                if (bitmap != null) {
                    try {
                        getService().putDataInCache(FormatHelper.replaceSlashes(imgPath), bitmap);
                    } catch (CacheSavingException | CacheCreationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private Bitmap getImageFromNetwork(String imagePath, int sampleSize) {
        Bitmap result = null;
        try {
            // create file
            File cacheFile = new File(getService().getCacheDir() + FormatHelper.replaceSlashes(imagePath));
            final HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(buildUrl(IMG_API_PATH, imagePath)).openConnection();
            processStream(httpURLConnection.getContentLength(), httpURLConnection.getInputStream(), cacheFile);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            result = BitmapFactory.decodeFile(cacheFile.getAbsolutePath(), options);
            // Delete file
            if (!cacheFile.delete()) {
                Ln.w("File not deleted");
            }
        } catch (OutOfMemoryError e) {
            if (sampleSize == 4) {
                return null;
            }

            return getImageFromNetwork(imagePath, sampleSize + 1);
        } catch (final MalformedURLException e) {
            Ln.e(e, "Unable to create URL");
        } catch (final IOException e) {
            Ln.e(e, "Unable to download binary");
        }

        return result;
    }

    public void processStream(int contentLength, final InputStream inputStream, File cacheFile) throws IOException {
        OutputStream fileOutputStream = null;
        try {
            // touch
            boolean isTouchedNow = cacheFile.setLastModified(System.currentTimeMillis());
            if (!isTouchedNow) {
                Ln.d("Modification time of file %s could not be changed normally ", cacheFile.getAbsolutePath());
            }
            fileOutputStream = new FileOutputStream(cacheFile);
            readBytes(inputStream, new ProgressByteProcessor(this, fileOutputStream, contentLength));
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    protected void readBytes(final InputStream in, final ProgressByteProcessor processor) throws IOException {
        final byte[] buf = new byte[BUF_SIZE];
        try {
            int amt;
            do {
                amt = in.read(buf);
                if (amt == -1) {
                    break;
                }
            } while (processor.processBytes(buf, 0, amt));
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}

