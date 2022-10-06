package com.myfutureapp.dashboard.application.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.application.ui.OnGoingView;
import com.myfutureapp.dashboard.home.model.OngoingApplicationsResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class OnGoingPresenter extends NetworkHandler {

    private OnGoingView onGoingView;
    private final Context context;
    private String jobId;

    public OnGoingPresenter(Context context, OnGoingView onGoingView) {
        super(context, onGoingView);
        this.onGoingView = onGoingView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (onGoingView != null) {
            onGoingView.hideLoader();
            if (Helper.isContainValue(message)) {
                onGoingView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (onGoingView != null) {
            onGoingView.hideLoader();
            onGoingView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (onGoingView != null && response != null) {
            onGoingView.hideLoader();
            if (response instanceof OngoingApplicationsResponse) {
                OngoingApplicationsResponse ongoingApplicationsResponse = (OngoingApplicationsResponse) response;
                onGoingView.setOngoingApplicationsResponse(ongoingApplicationsResponse);
            } else if (response instanceof InterviewTimeSlotsResponse) {
                InterviewTimeSlotsResponse interviewTimeSlotsResponse = (InterviewTimeSlotsResponse) response;
                onGoingView.setInterviewTimeSlotsResponse(interviewTimeSlotsResponse, jobId);
            } else if (response instanceof ScheduleInterviewResponse) {
                ScheduleInterviewResponse scheduleInterviewResponse = (ScheduleInterviewResponse) response;
                onGoingView.setScheduleInterviewResponse(scheduleInterviewResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        onGoingView = null;
    }

    public void callScheduleInterviewApi(String interview_datetime, String jobId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("interview_datetime", interview_datetime);
        jsonObject.addProperty("opportunity_id", Integer.parseInt(jobId));
        NetworkManager.getApi(context).scheduleInterview(headerMap, jsonObject).enqueue(this);
    }

    public void callGetTimeSlotslApi(String id) {
        jobId = id;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getInterviewTimeSlots(headerMap, id).enqueue(this);

    }

    public void callOngoingApplicationsApi(int page) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getOngoingApplications(headerMap, "0", String.valueOf(page)).enqueue(this);
    }
}