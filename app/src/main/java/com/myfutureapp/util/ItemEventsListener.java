package com.myfutureapp.util;

import android.view.View;

/**
 * Created by Deepak Chaudhary on 06-12-2020.
 */
public interface ItemEventsListener {

    void onItemClicked(View view, int position, String cityName);
}
