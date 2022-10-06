package com.myfutureapp.enrollment.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.enrollment.model.EnrollUserResponse;
import com.myfutureapp.enrollment.ui.EnrollView;
import com.myfutureapp.jobDetail.model.JobDetailResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class EnrollPresenter extends NetworkHandler {

    private EnrollView enrollView;
    private final Context context;

    public EnrollPresenter(Context context, EnrollView enrollView) {
        super(context, enrollView);
        this.enrollView = enrollView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (enrollView != null) {
            enrollView.hideLoader();
            if (Helper.isContainValue(message)) {
                enrollView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (enrollView != null) {
            enrollView.hideLoader();
            enrollView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (enrollView != null && response != null) {
            enrollView.hideLoader();
            if (response instanceof EnrollUserResponse) {
                EnrollUserResponse enrollUserResponse = (EnrollUserResponse) response;
                enrollView.setEnrollUserResponse(enrollUserResponse);
            } else if (response instanceof JobDetailResponse) {
                JobDetailResponse jobDetailResponse = (JobDetailResponse) response;
                enrollView.setOpportunityDetailResponse(jobDetailResponse);
            }
        }
        return true;
    }

    public void onDestroyedView() {
        enrollView = null;
    }


    public void callEnrollUserApi() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getEnrolledUser(headerMap).enqueue(this);
    }

    public void callOpportunityDetailApi(String id) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).opportunityDetail(headerMap, id, "2").enqueue(this);
    }
}