package com.myfutureapp.dashboard.profile.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.dashboard.profile.model.AddEditWorkExperienceResponse;
import com.myfutureapp.dashboard.profile.model.DeleteWorkExperinceModel;
import com.myfutureapp.dashboard.profile.model.ProfileCityListRsponseModel;
import com.myfutureapp.dashboard.profile.model.ProfileStateListResponseModel;
import com.myfutureapp.dashboard.profile.model.UpdateLocationPreferenceResponse;
import com.myfutureapp.dashboard.profile.ui.ProfileFragmentView;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class ProfileFragmentPresenter extends NetworkHandler {

    private ProfileFragmentView profileFragmentView;
    private final Context context;

    public ProfileFragmentPresenter(Context context, ProfileFragmentView profileFragmentView) {
        super(context, profileFragmentView);
        this.profileFragmentView = profileFragmentView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (profileFragmentView != null) {
            profileFragmentView.hideLoader();
            if (Helper.isContainValue(message)) {
                profileFragmentView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (profileFragmentView != null) {
            profileFragmentView.hideLoader();
            profileFragmentView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (profileFragmentView != null && response != null) {
            profileFragmentView.hideLoader();
            if (response instanceof UserProfileUpDataResponse) {
                UserProfileUpDataResponse userProfileUpDataResponse = (UserProfileUpDataResponse) response;
                profileFragmentView.setProfileDataResponse(userProfileUpDataResponse);
            } else if (response instanceof AddEditWorkExperienceResponse) {
                AddEditWorkExperienceResponse addEditWorkExperienceResponse = (AddEditWorkExperienceResponse) response;
                profileFragmentView.setWorkExperienceResponse(addEditWorkExperienceResponse);
            } else if (response instanceof UpdateLocationPreferenceResponse) {
                UpdateLocationPreferenceResponse addEditWorkExperienceResponse = (UpdateLocationPreferenceResponse) response;
                profileFragmentView.setUpdateLocationPreferenceResponse(addEditWorkExperienceResponse);
            } else if (response instanceof ProfileStateListResponseModel) {
                ProfileStateListResponseModel profileStateListResponseModel = (ProfileStateListResponseModel) response;
                profileFragmentView.setStateListResponse(profileStateListResponseModel);
            } else if (response instanceof ProfileCityListRsponseModel) {
                ProfileCityListRsponseModel profileCityListRsponseModel = (ProfileCityListRsponseModel) response;
                profileFragmentView.setCityListResponse(profileCityListRsponseModel);
            } else if (response instanceof DeleteWorkExperinceModel) {
                DeleteWorkExperinceModel deleteWorkExperinceModel = (DeleteWorkExperinceModel) response;
                profileFragmentView.setDeleteWorkExperinceResponse(deleteWorkExperinceModel);
            } else if (response instanceof UserProfileResponse) {
                UserProfileResponse userProfileResponse = (UserProfileResponse) response;
                profileFragmentView.setUserProfileResponse(userProfileResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        profileFragmentView = null;
    }

/*    public void callUserProfileUploadApi(JsonObject getUserDataresponse) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi().userProfileUpdateApi(headerMap, getUserDataresponse).enqueue(this);
    }*/


    public void callEditWorkExperienceUploadApi(JsonObject getUserDataresponse) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).addEditWorkExperienceUpdate(headerMap, getUserDataresponse).enqueue(this);
    }


    public void callUpdateLocationPreferenceApi(String preferences, String id) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).updateLocationPreference(headerMap, preferences, id).enqueue(this);
    }


    public void callProfileStateListApi() {
        // HashMap<String, String> headerMap = new HashMap<>();
        // headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).profilestatelist().enqueue(this);
    }


    public void callProfileCityListApi(String id) {
        // HashMap<String, String> headerMap = new HashMap<>();
        ///  headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).profilecitylist(id).enqueue(this);
    }


    public void callDeleteWorkExperinceApi(String id) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).deleteWorkExperince(headerMap, id).enqueue(this);
    }

    public void callUserProfileApi(String studentId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getUserProfile(headerMap, studentId).enqueue(this);
    }
}