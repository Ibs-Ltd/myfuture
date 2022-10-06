package com.myfutureapp.dashboard.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.ui.DashboardView;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class DashboardPresenter extends NetworkHandler {

    private DashboardView dashboardView;
    private final Context context;

    public DashboardPresenter(Context context, DashboardView dashboardView) {
        super(context, dashboardView);
        this.dashboardView = dashboardView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (dashboardView != null) {
            dashboardView.hideLoader();
            if (Helper.isContainValue(message)) {
                dashboardView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (dashboardView != null) {
            dashboardView.hideLoader();
            dashboardView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (dashboardView != null && response != null) {
            dashboardView.hideLoader();
            if (response instanceof UserProfileResponse) {
                UserProfileResponse userProfileResponse = (UserProfileResponse) response;
                dashboardView.setUserProfileResponse(userProfileResponse);
            }
        }
        return true;
    }

    public void onDestroyedView() {
        dashboardView = null;
    }


    public void callUserProfileApi(String studentId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getUserProfile(headerMap, studentId).enqueue(this);
    }

}