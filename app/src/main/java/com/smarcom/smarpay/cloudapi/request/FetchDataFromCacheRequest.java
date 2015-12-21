package com.smarcom.smarpay.cloudapi.request;

import android.graphics.Bitmap;

import com.smarcom.smarpay.cloudapi.model.AdvItem;
import com.smarcom.smarpay.cloudapi.model.CacheData;
import com.smarcom.smarpay.cloudapi.model.ResultDataToDeviceList;
import com.smarcom.smarpay.cloudapi.model.ResultDataTofAdvList;
import com.smarcom.smarpay.cloudapi.service.SPService;
import com.smarcom.smarpay.cloudapi.helper.FormatHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchDataFromCacheRequest extends BaseRequest<CacheData> {

    private static final String DEVICE_REQUEST_CACHE_KEY = "DeviceList2";
    private static final String ADVERTISEMENT_REQUEST_CACHE_KEY = "AdvertisementList";
    private AdvItem.AdvTarget target;

    private static final float PROGRESS_10_PERCENT = 0.1f;
    private static final float PROGRESS_90_PERCENT = 0.9f;
    private static final float PROGRESS_70_PERCENT = 0.7f;
    private static final float PROGRESS_100_PERCENT = 1.0f;

    public FetchDataFromCacheRequest() {
        super(CacheData.class);
        this.target = null;
    }

    public FetchDataFromCacheRequest(AdvItem.AdvTarget target) {
        super(CacheData.class);
        this.target = target;
    }

    @Override
    public CacheData loadDataFromNetwork() throws Exception {
        CacheData result = null;

        SPService service = getService();

        if (service != null ) {
            result = service.getData();

            if (result == null) {
                float currentProgress = PROGRESS_10_PERCENT;
                ResultDataTofAdvList resultDataTofAdvList = service.getDataFromCache(ResultDataTofAdvList.class, ADVERTISEMENT_REQUEST_CACHE_KEY);
                publishProgress(currentProgress);

                ResultDataToDeviceList resultDataToDeviceList = service.getDataFromCache(ResultDataToDeviceList.class, DEVICE_REQUEST_CACHE_KEY);
                currentProgress += PROGRESS_10_PERCENT;
                publishProgress(currentProgress);

                Map<String, Bitmap> imgMap = new HashMap<>();
                List<AdvItem> advItems = resultDataTofAdvList.getData().getItems();

                float imageProgressTick = advItems.size() > 0 ? (PROGRESS_70_PERCENT / (float) advItems.size()) : PROGRESS_90_PERCENT;

                for (AdvItem currentItem : advItems) {
                    if (target == null || currentItem.getTargetName() == target) {
                        String imgName = currentItem.getImg();
                        Bitmap bitmap = service.getDataFromCache(Bitmap.class, FormatHelper.replaceSlashes(imgName));
                        imgMap.put(FormatHelper.replaceSlashes(imgName), bitmap);
                        currentProgress += imageProgressTick;
                        publishProgress(currentProgress);
                    }
                }

                publishProgress(PROGRESS_100_PERCENT);
                result = new CacheData(resultDataTofAdvList, resultDataToDeviceList, imgMap);

                service.setData(result);
                SPService.setAdvVersion(resultDataTofAdvList.getData().getVersion());
                SPService.setDevVersion(resultDataToDeviceList.getData().getVersion());
            }
        }

        return result;
    }
}
