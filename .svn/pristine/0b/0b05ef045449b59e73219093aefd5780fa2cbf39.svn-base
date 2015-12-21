package com.smarcom.smarpay.helper;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import org.roboguice.shaded.goole.common.collect.Iterables;

import java.util.Iterator;
import java.util.Map;

public class BannerRunnableHelper<T> implements Runnable {

    private ImageView destinationView;
    private Iterator iterator;
    private Handler handler;
    private BannerRunnableListener listener;

    public BannerRunnableHelper(Map<T, Long> banners, ImageView destinationView, boolean isLooped, Handler handler, BannerRunnableListener listener) {

        this.listener = listener;
        this.destinationView = destinationView;
        this.handler = handler;

        if (banners != null) {
            iterator = isLooped ? Iterables.cycle(banners.entrySet()).iterator() : banners.entrySet().iterator();
        }
    }

    @Override
    public void run() {
        Map.Entry pair = null;

        if (iterator.hasNext()) {
            pair = (Map.Entry)iterator.next();
        }

        if (pair != null && destinationView != null && handler != null) {
            Bitmap img = (Bitmap) pair.getKey();

            if (img != null) {
                destinationView.setImageBitmap(img);
            }

            handler.postDelayed(this, (long) pair.getValue());
        } else if (listener != null) {
            listener.onCompleteShowingBanners();
        }
    }
}