package com.smarcom.smarpay.cloudapi.request;

import android.graphics.Bitmap;

import com.smarcom.smarpay.cloudapi.model.CacheData;
import com.smarcom.smarpay.cloudapi.model.ResultDataToDeviceList;
import com.smarcom.smarpay.cloudapi.model.ResultDataTofAdvList;
import com.smarcom.smarpay.cloudapi.model.StateInfo;
import com.smarcom.smarpay.cloudapi.service.SPService;
import com.smarcom.smarpay.cloudapi.helper.FileHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReloadCacheRequest extends BaseRequest<CacheData> {

    private static final String ASSETS_IMG = "img";
    private static final String STATE_INFO_CACHE_KEY = "StateInfo";
    private static final String DEVICE_REQUEST_CACHE_KEY = "DeviceList2";
    private static final String ADVERTISEMENT_REQUEST_CACHE_KEY = "AdvertisementList";
    private static final String ADVERTISEMENT_FILE_NAME = "AdvertisementList";
    private static final String DEVICE_LIST_FILE_NAME = "DeviceList2";

    private static final float PROGRESS_10_PERCENT = 0.1f;
    private static final float PROGRESS_90_PERCENT = 0.9f;
    private static final float PROGRESS_60_PERCENT = 0.6f;
    private static final float PROGRESS_100_PERCENT = 1.0f;

    public ReloadCacheRequest() {
        super(CacheData.class);
    }

    @Override
    public CacheData loadDataFromNetwork() throws Exception {
        CacheData result = null;

        SPService service = getService();

        if (service != null) {
            float currentProgress = PROGRESS_10_PERCENT;
            ResultDataTofAdvList resultDataTofAdvList = (ResultDataTofAdvList) FileHelper.readFromFile(service, FileHelper.getJsonFullFileName(ADVERTISEMENT_FILE_NAME), ResultDataTofAdvList.class);
            ResultDataToDeviceList resultDataToDeviceList = (ResultDataToDeviceList) FileHelper.readFromFile(service, FileHelper.getJsonFullFileName(DEVICE_LIST_FILE_NAME), ResultDataToDeviceList.class);

            service.putDataInCache(ADVERTISEMENT_REQUEST_CACHE_KEY, resultDataTofAdvList);
            publishProgress(currentProgress);

            service.putDataInCache(DEVICE_REQUEST_CACHE_KEY, resultDataToDeviceList);
            currentProgress += PROGRESS_10_PERCENT;
            publishProgress(currentProgress);

            service.putDataInCache(STATE_INFO_CACHE_KEY, new StateInfo(resultDataTofAdvList.getData().getVersion(), resultDataToDeviceList.getData().getVersion()));
            currentProgress += PROGRESS_10_PERCENT;
            publishProgress(currentProgress);

            Map<String, Bitmap> imgMap = new HashMap<>();
            List<String> images = Arrays.asList(service.getAssets().list(ASSETS_IMG));

            float imageProgressTick = images.size() > 0 ? (PROGRESS_60_PERCENT / (float)images.size()) : PROGRESS_90_PERCENT;

            for(String currentItem : images) {
                Bitmap bitmap = FileHelper.readBitmapFromFile(service, ASSETS_IMG + "/" + currentItem);
                service.putDataInCache(currentItem, bitmap);
                imgMap.put(currentItem, bitmap);
                currentProgress += imageProgressTick;
                publishProgress(currentProgress);
            }

            publishProgress(PROGRESS_100_PERCENT);
            result = new CacheData(resultDataTofAdvList, resultDataToDeviceList, imgMap);
        }

        return result;
    }
}