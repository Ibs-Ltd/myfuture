package com.myfutureapp.dashboard.homeProfile.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.homeProfile.model.UserProfilePicModel;
import com.myfutureapp.dashboard.homeProfile.ui.UserProfileView;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UserProfilePresenter extends NetworkHandler {

    private UserProfileView userProfileView;
    private final Context context;

    public UserProfilePresenter(Context context, UserProfileView userProfileView) {
        super(context, userProfileView);
        this.userProfileView = userProfileView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (userProfileView != null) {
            userProfileView.hideLoader();
            if (Helper.isContainValue(message)) {
                userProfileView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (userProfileView != null) {
            userProfileView.hideLoader();
            userProfileView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (userProfileView != null && response != null) {
            userProfileView.hideLoader();
            if (response instanceof UserProfilePicModel) {
                UserProfilePicModel userProfilePicModel = (UserProfilePicModel) response;
                userProfileView.setUserProfilePicResponse(userProfilePicModel);
            } else if (response instanceof UserProfileUpDataResponse) {
                UserProfileUpDataResponse userProfileUpDataResponse = (UserProfileUpDataResponse) response;
                userProfileView.setProfileDataResponse(userProfileUpDataResponse);
            } else if (response instanceof UserProfileResponse) {
                UserProfileResponse userProfileResponse = (UserProfileResponse) response;
                userProfileView.setUserProfileResponse(userProfileResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        userProfileView = null;
    }


    public void callUserProfileUploadApi(JsonObject getUserDataresponse) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).userProfileUpdateApi(headerMap, getUserDataresponse).enqueue(this);
    }

    public void callUserProfilePicUploadApi(File result) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));

        RequestBody reqFile = RequestBody.create(MediaType.parse("text/plain"), result);
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_pic", result.getName(), reqFile);
        NetworkManager.getApi(context).userProfilePicUpdateApi(headerMap, body).enqueue(this);
    }


    public void callUserProfileApi(String studentId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getUserProfile(headerMap, studentId).enqueue(this);
    }
}