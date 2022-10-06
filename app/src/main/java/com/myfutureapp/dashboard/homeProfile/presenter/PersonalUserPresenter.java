package com.myfutureapp.dashboard.homeProfile.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.homeProfile.ui.PersonalUserView;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class PersonalUserPresenter extends NetworkHandler {

    private PersonalUserView personalUserView;
    private final Context context;

    public PersonalUserPresenter(Context context, PersonalUserView personalUserView) {
        super(context, personalUserView);
        this.personalUserView = personalUserView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (personalUserView != null) {
            personalUserView.hideLoader();
            if (Helper.isContainValue(message)) {
                personalUserView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (personalUserView != null) {
            personalUserView.hideLoader();
            personalUserView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (personalUserView != null && response != null) {
            personalUserView.hideLoader();
            if (response instanceof UserProfileUpDataResponse) {
                UserProfileUpDataResponse userProfileUpDataResponse = (UserProfileUpDataResponse) response;
                personalUserView.setProfileDataResponse(userProfileUpDataResponse);
            } else if (response instanceof UserProfileResponse) {
                UserProfileResponse userProfileResponse = (UserProfileResponse) response;
                personalUserView.setUserProfileResponse(userProfileResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        personalUserView = null;
    }

    public void callUserProfileUploadApi(JsonObject getUserDataresponse) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).userProfileUpdateApi(headerMap, getUserDataresponse).enqueue(this);
    }


    public void callUserProfileApi(String studentId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getUserProfile(headerMap, studentId).enqueue(this);
    }
}