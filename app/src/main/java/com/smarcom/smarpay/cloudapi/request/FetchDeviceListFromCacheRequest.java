package com.smarcom.smarpay.cloudapi.request;

import com.smarcom.smarpay.cloudapi.model.ResultDataToDeviceList;
import com.smarcom.smarpay.cloudapi.service.SPService;

public class FetchDeviceListFromCacheRequest extends BaseRequest<ResultDataToDeviceList> {

    private static final String DEVICE_REQUEST_CACHE_KEY = "DeviceList2";


    public FetchDeviceListFromCacheRequest() {
        super(ResultDataToDeviceList.class);
    }

    @Override
    public ResultDataToDeviceList loadDataFromNetwork() throws Exception {
        ResultDataToDeviceList resultDataToDeviceList = null;

        SPService service = getService();

        if (service != null) {
            resultDataToDeviceList = service.getDataFromCache(ResultDataToDeviceList.class, DEVICE_REQUEST_CACHE_KEY);

        }

        return resultDataToDeviceList;
    }
}
