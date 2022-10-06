package com.myfutureapp.util;

import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

abstract public class DeBouncedClickListener implements View.OnClickListener {

    private final Map<View, Long> lastClickMap;
    long minIntervalInMs;

    public DeBouncedClickListener(long minIntervalInMs) {
        this.minIntervalInMs = minIntervalInMs;
        lastClickMap = new WeakHashMap<>();
    }

    abstract public void onDeBounceClick(View view);

    @Override
    public void onClick(View view) {
        Long lastClickTime = lastClickMap.get(view);
        long currentTimeStamp = System.currentTimeMillis();
        lastClickMap.put(view, currentTimeStamp);
        if (lastClickTime == null || currentTimeStamp - lastClickTime > minIntervalInMs)
            onDeBounceClick(view);
    }
}
