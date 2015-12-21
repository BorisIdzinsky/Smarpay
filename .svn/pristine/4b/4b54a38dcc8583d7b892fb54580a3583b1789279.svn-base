package com.smarcom.smarpay.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.smarcom.smarpay.R;
import com.smarcom.smarpay.activity.BaseActivity;
import com.smarcom.smarpay.cloudapi.model.AdvItem;
import com.smarcom.smarpay.helper.BannerHelper;
import com.smarcom.smarpay.helper.BannerRunnableListener;

import java.util.Map;

import roboguice.inject.InjectView;

public class SplashFragment extends BaseFragment implements View.OnClickListener, BannerRunnableListener {

    @InjectView(R.id.splash)
    ImageView imageView;

    @InjectView(R.id.circle_progress_bar)
    ProgressBar progressBar;

    @InjectView(R.id.btn_skip)
    ImageButton imageButton;

    ObjectAnimator animation;

    Map<Bitmap, Long> splashMap;
    AdvItem.AdvTarget target;

    private static final int PROGRESS_0_PERCENT = 0;

    private Handler showBannerHandler = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_splash, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            progressBar.setProgress(PROGRESS_0_PERCENT);

            if (splashMap != null) {

                if (target == AdvItem.AdvTarget.Splash) {
                    imageButton.setVisibility(View.INVISIBLE);
                } else {
                    imageButton.setVisibility(View.VISIBLE);
                }

                if (showBannerHandler != null) {
                    showBannerHandler.removeCallbacksAndMessages(null);
                    showBannerHandler = null;
                }

                showBannerHandler = new Handler();

                BannerHelper.showBanners(splashMap, showBannerHandler, imageView, false, this);

                Map.Entry<Bitmap, Long> entry = splashMap.entrySet().iterator().next();

                long timeLength = entry.getValue();

                progressBar.setMax((int)timeLength);

                if (animation != null) {
                    animation.removeAllListeners();
                    animation.cancel();
                    animation = null;
                }

                animation = ObjectAnimator.ofInt(progressBar, "progress", (int)timeLength);
                animation.setDuration(timeLength);
                animation.setInterpolator(new LinearInterpolator());

                animation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showContent();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animation.start();
            } else {
                showContent();
            }
        }
    }

    private void showContent() {
        BaseActivity baseActivity = (BaseActivity) getActivity();

        if (baseActivity != null && getSpiceManager().isStarted()) {
            baseActivity.hideSplashFragment();
        }
    }

    public void setSplashMap(Map<Bitmap, Long> splashMap) {
        this.splashMap = splashMap;
    }

    public void setTarget(AdvItem.AdvTarget target) {
        this.target = target;
    }

    @Override
    public void onCompleteShowingBanners() {

    }

    @Override
    public void onClick(View v) {
        if (animation != null) {
            animation.removeAllListeners();
            animation.cancel();
            animation = null;
        }

        showContent();
    }
}
