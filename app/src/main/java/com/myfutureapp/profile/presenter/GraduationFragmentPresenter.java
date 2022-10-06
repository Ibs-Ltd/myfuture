package com.myfutureapp.profile.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.profile.model.GraduationCourseResponse;
import com.myfutureapp.profile.ui.GraduationFragmentView;
import com.myfutureapp.util.Helper;

import okhttp3.Request;

public class GraduationFragmentPresenter extends NetworkHandler {

    private GraduationFragmentView graduationFragmentView;
    private final Context context;

    public GraduationFragmentPresenter(Context context, GraduationFragmentView graduationFragmentView) {
        super(context, graduationFragmentView);
        this.graduationFragmentView = graduationFragmentView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (graduationFragmentView != null) {
            graduationFragmentView.hideLoader();
            if (Helper.isContainValue(message)) {
                graduationFragmentView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (graduationFragmentView != null) {
            graduationFragmentView.hideLoader();
            graduationFragmentView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (graduationFragmentView != null && response != null) {
            graduationFragmentView.hideLoader();
            if (response instanceof GraduationCourseResponse) {
                GraduationCourseResponse graduationCourseResponse = (GraduationCourseResponse) response;
                graduationFragmentView.setGraduationCourseResponse(graduationCourseResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        graduationFragmentView = null;
    }

    public void callGraduationCourseApi() {
        NetworkManager.getApi(context).graduationCourse().enqueue(this);
    }
}