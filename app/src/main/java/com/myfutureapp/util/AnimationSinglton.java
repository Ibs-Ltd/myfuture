package com.myfutureapp.util;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class AnimationSinglton {

    private AnimationSinglton() {
    }

    static AnimationSinglton instance;

    public static AnimationSinglton getInstance() {
        if (instance == null) {
            instance = new AnimationSinglton();
        }
        return instance;
    }

    public static void expandView(View llItemDetail) {
        llItemDetail.setVisibility(View.VISIBLE);
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) llItemDetail.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int prevHeight = llItemDetail.getHeight();
        llItemDetail.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = llItemDetail.getMeasuredHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                llItemDetail.getLayoutParams().height = (int) animation.getAnimatedValue();
                llItemDetail.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }

    public static void collapse(View llItemDetail) {
        int prevHeight = llItemDetail.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, 0);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                llItemDetail.getLayoutParams().height = (int) animation.getAnimatedValue();
                llItemDetail.requestLayout();
                if (llItemDetail.getLayoutParams().height == 0)
                    llItemDetail.setVisibility(View.GONE);
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(400);
        valueAnimator.start();
    }
}
