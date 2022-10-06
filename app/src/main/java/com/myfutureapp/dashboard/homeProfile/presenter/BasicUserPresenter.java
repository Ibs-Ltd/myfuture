package com.myfutureapp.dashboard.homeProfile.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.homeProfile.ui.BasicUserView;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.profile.model.ProfileCityListRsponseModel;
import com.myfutureapp.dashboard.profile.model.ProfileStateListResponseModel;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class BasicUserPresenter extends NetworkHandler {

    private BasicUserView basicUserView;
    private final Context context;

    public BasicUserPresenter(Context context, BasicUserView basicUserView) {
        super(context, basicUserView);
        this.basicUserView = basicUserView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (basicUserView != null) {
            basicUserView.hideLoader();
            if (Helper.isContainValue(message)) {
                basicUserView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (basicUserView != null) {
            basicUserView.hideLoader();
            basicUserView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (basicUserView != null && response != null) {
            basicUserView.hideLoader();
            if (response instanceof UserProfileUpDataResponse) {
                UserProfileUpDataResponse userProfileUpDataResponse = (UserProfileUpDataResponse) response;
                basicUserView.setProfileDataResponse(userProfileUpDataResponse);
            } else if (response instanceof ProfileStateListResponseModel) {
                ProfileStateListResponseModel profileStateListResponseModel = (ProfileStateListResponseModel) response;
                basicUserView.setStateListResponse(profileStateListResponseModel);
            } else if (response instanceof ProfileCityListRsponseModel) {
                ProfileCityListRsponseModel profileCityListRsponseModel = (ProfileCityListRsponseModel) response;
                basicUserView.setCityListResponse(profileCityListRsponseModel);
            } else if (response instanceof UserProfileResponse) {
                UserProfileResponse userProfileResponse = (UserProfileResponse) response;
                basicUserView.setUserProfileResponse(userProfileResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        basicUserView = null;
    }

    public void callUserProfileUploadApi(JsonObject getUserDataresponse) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).userProfileUpdateApi(headerMap, getUserDataresponse).enqueue(this);
    }

    public void callProfileStateListApi() {
        NetworkManager.getApi(context).profilestatelist().enqueue(this);
    }

    public void callProfileCityListApi(String id) {
        NetworkManager.getApi(context).profilecitylist(id).enqueue(this);
    }

    public void callUserProfileApi(String studentId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getUserProfile(headerMap, studentId).enqueue(this);
    }
}