package com.myfutureapp.profile.presenter;

import android.content.Context;

import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.profile.model.GraduationCourseResponse;
import com.myfutureapp.profile.ui.PostGraduationFragmentView;
import com.myfutureapp.util.Helper;

import okhttp3.Request;

public class PostGraduationFragmentPresenter extends NetworkHandler {

    private PostGraduationFragmentView postGraduationFragmentView;
    private final Context context;

    public PostGraduationFragmentPresenter(Context context, PostGraduationFragmentView postGraduationFragmentView) {
        super(context, postGraduationFragmentView);
        this.postGraduationFragmentView = postGraduationFragmentView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (postGraduationFragmentView != null) {
            postGraduationFragmentView.hideLoader();
            if (Helper.isContainValue(message)) {
                postGraduationFragmentView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (postGraduationFragmentView != null) {
            postGraduationFragmentView.hideLoader();
            postGraduationFragmentView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (postGraduationFragmentView != null && response != null) {
            postGraduationFragmentView.hideLoader();
            if (response instanceof GraduationCourseResponse) {
                GraduationCourseResponse graduationCourseResponse = (GraduationCourseResponse) response;
                postGraduationFragmentView.setPostGraduationCourseResponse(graduationCourseResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        postGraduationFragmentView = null;
    }

    public void callPostGraduationCourseApi() {
        NetworkManager.getApi(context).postGraduationCourse().enqueue(this);
    }
}