package com.smarcom.smarpay.helper;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.smarcom.smarpay.cloudapi.model.AdvItem;
import com.smarcom.smarpay.cloudapi.model.CacheData;
import com.smarcom.smarpay.cloudapi.model.ResultDataTofAdvList;
import com.smarcom.smarpay.cloudapi.helper.FormatHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class BannerHelper {

    public static AdvItem getSplashItemForTrigger(CacheData data, AdvItem.AdvTrigger trigger) {
        AdvItem item = null;

        if (data != null && trigger != null) {
            ResultDataTofAdvList resultDataTofAdvList = data.getAdvList();

            if (resultDataTofAdvList != null) {
                for (AdvItem advItem : resultDataTofAdvList.getData().getItems()) {
                    if (advItem.getTriggerName() == trigger && (advItem.getTargetName() == AdvItem.AdvTarget.Splash || advItem.getTargetName() == AdvItem.AdvTarget.SplashSkip)) {
                        item = advItem;
                        break;
                    }
                }
            }
        }

        return item;
    }

    public static Map<Bitmap, Long> getBannersForTargets(CacheData data, AdvItem.AdvTarget advTarget) {
        Map<Bitmap, Long> banners = null;

        if (data != null) {
            ResultDataTofAdvList resultDataTofAdvList = data.getAdvList();
            Map<String, Bitmap> imgMap = data.getImagesMap();

            if (resultDataTofAdvList != null && imgMap != null) {
                banners = new LinkedHashMap<> ();

                for (AdvItem item : resultDataTofAdvList.getData().getItems()) {
                    if (item.getTargetName() == advTarget) {
                        banners.put(imgMap.get(FormatHelper.replaceSlashes(item.getImg())), (long) item.getDurationMs());
                    }
                }

            }
        }

        return banners;
    }

    public static void showBanners(Map<Bitmap, Long> banners, Handler showBannerHandler, ImageView imageView, boolean isLooped, BannerRunnableListener bannerRunnableListener) {
        if (banners != null && !banners.isEmpty()) {
            showBannerHandler.post(new BannerRunnableHelper<>(banners, imageView, isLooped, showBannerHandler, bannerRunnableListener));
        }
    }
}
