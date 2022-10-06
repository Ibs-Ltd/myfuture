package com.myfutureapp.jobApplyLocationSelection.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.profile.model.UpdateLocationPreferenceResponse;
import com.myfutureapp.jobApplyLocationSelection.JobApplyView;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class JobApplyLocationPresenter extends NetworkHandler {

    private JobApplyView jobApplyView;
    private final Context context;

    public JobApplyLocationPresenter(Context context, JobApplyView jobApplyView) {
        super(context, jobApplyView);
        this.jobApplyView = jobApplyView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (jobApplyView != null) {
            jobApplyView.hideLoader();
            if (Helper.isContainValue(message)) {
                jobApplyView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (jobApplyView != null) {
            jobApplyView.hideLoader();
            jobApplyView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (jobApplyView != null && response != null) {
            jobApplyView.hideLoader();
            if (response instanceof UpdateLocationPreferenceResponse) {
                UpdateLocationPreferenceResponse addEditWorkExperienceResponse = (UpdateLocationPreferenceResponse) response;
                jobApplyView.setUpdateLocationPreferenceResponse(addEditWorkExperienceResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        jobApplyView = null;
    }


    public void callUpdateLocationPreferenceApi(String preferences, String id) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).updateLocationPreference(headerMap, preferences, id).enqueue(this);
    }

}