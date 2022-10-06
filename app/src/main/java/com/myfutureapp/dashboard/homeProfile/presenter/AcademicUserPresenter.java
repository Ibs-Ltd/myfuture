package com.myfutureapp.dashboard.homeProfile.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.homeProfile.ui.AcademicUserView;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.profile.model.GraduationCourseResponse;
import com.myfutureapp.profile.model.PostGraduationCourseResponse;
import com.myfutureapp.profile.model.UserProfileUpDataResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class AcademicUserPresenter extends NetworkHandler {

    private AcademicUserView academicUserView;
    private final Context context;

    public AcademicUserPresenter(Context context, AcademicUserView academicUserView) {
        super(context, academicUserView);
        this.academicUserView = academicUserView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (academicUserView != null) {
            academicUserView.hideLoader();
            if (Helper.isContainValue(message)) {
                academicUserView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (academicUserView != null) {
            academicUserView.hideLoader();
            academicUserView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (academicUserView != null && response != null) {
            academicUserView.hideLoader();
            if (response instanceof UserProfileUpDataResponse) {
                UserProfileUpDataResponse userProfileUpDataResponse = (UserProfileUpDataResponse) response;
                academicUserView.setProfileDataResponse(userProfileUpDataResponse);
            } else if (response instanceof UserProfileResponse) {
                UserProfileResponse userProfileResponse = (UserProfileResponse) response;
                academicUserView.setUserProfileResponse(userProfileResponse);
            } else if (response instanceof GraduationCourseResponse) {
                GraduationCourseResponse graduationCourseResponse = (GraduationCourseResponse) response;
                academicUserView.setGraduationCourseResponse(graduationCourseResponse);
            } else if (response instanceof PostGraduationCourseResponse) {
                PostGraduationCourseResponse postGraduationCourseResponse = (PostGraduationCourseResponse) response;
                academicUserView.setPostGraduationCourseResponse(postGraduationCourseResponse);
            }


        }
        return true;
    }

    public void onDestroyedView() {
        academicUserView = null;
    }

    public void callGraduationCourseApi() {
        NetworkManager.getApi(context).graduationCourse().enqueue(this);
    }

    public void callPostGraduationCourseApi() {
        NetworkManager.getApi(context).postGraduationCourses().enqueue(this);
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