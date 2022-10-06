package com.myfutureapp.profile.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.jobDetail.model.ApplyOpportunityResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.profile.ui.ProfileActivtyView;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class ProfileActivityPresenter extends NetworkHandler {

    private ProfileActivtyView profileActivtyView;
    private final Context context;

    public ProfileActivityPresenter(Context context, ProfileActivtyView profileActivtyView) {
        super(context, profileActivtyView);
        this.profileActivtyView = profileActivtyView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (profileActivtyView != null) {
            profileActivtyView.hideLoader();
            if (Helper.isContainValue(message)) {
                profileActivtyView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (profileActivtyView != null) {
            profileActivtyView.hideLoader();
            profileActivtyView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (profileActivtyView != null && response != null) {
            profileActivtyView.hideLoader();
            if (response instanceof UserProfileUpDataResponse) {
                UserProfileUpDataResponse userProfileUpDataResponse = (UserProfileUpDataResponse) response;
                profileActivtyView.setProfileDataResponse(userProfileUpDataResponse);
            } else if (response instanceof ApplyOpportunityResponse) {
                ApplyOpportunityResponse applyOpportunityResponse = (ApplyOpportunityResponse) response;
                profileActivtyView.setApplyOpportunityResponse(applyOpportunityResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        profileActivtyView = null;
    }

    public void callUserProfileUploadApi(JsonObject getUserDataresponse) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).userProfileUploadApi(headerMap, getUserDataresponse).enqueue(this);
    }

    public void callApplyOpportunity(String jobId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).applyOpportunity(headerMap, jobId, "1").enqueue(this);
    }

}