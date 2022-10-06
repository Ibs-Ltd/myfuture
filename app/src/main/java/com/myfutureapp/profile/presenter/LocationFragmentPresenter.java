package com.myfutureapp.profile.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.profile.model.CityListResponse;
import com.myfutureapp.profile.ui.LocationFragmentView;
import com.myfutureapp.util.Helper;

import okhttp3.Request;

public class LocationFragmentPresenter extends NetworkHandler {

    private LocationFragmentView locationFragmentView;
    private final Context context;

    public LocationFragmentPresenter(Context context, LocationFragmentView locationFragmentView) {
        super(context, locationFragmentView);
        this.locationFragmentView = locationFragmentView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (locationFragmentView != null) {
            locationFragmentView.hideLoader();
            if (Helper.isContainValue(message)) {
                locationFragmentView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (locationFragmentView != null) {
            locationFragmentView.hideLoader();
            locationFragmentView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (locationFragmentView != null && response != null) {
            locationFragmentView.hideLoader();
            if (response instanceof CityListResponse) {
                CityListResponse cityListResponse = (CityListResponse) response;
                locationFragmentView.setCityListResponse(cityListResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        locationFragmentView = null;
    }

    public void callCityListApi() {
        NetworkManager.getApi(context).cityList().enqueue(this);
    }
}