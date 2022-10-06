package com.myfutureapp.jobDetail.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.jobDetail.ui.SuccessActivityView;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class SuccessPresenter extends NetworkHandler {

    private SuccessActivityView successActivityView;
    private final Context context;
    private String jobId;
    private String bookMarkType;
    private int position;

    public SuccessPresenter(Context context, SuccessActivityView successActivityView) {
        super(context, successActivityView);
        this.successActivityView = successActivityView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (successActivityView != null) {
            successActivityView.hideLoader();
            if (Helper.isContainValue(message)) {
                successActivityView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (successActivityView != null) {
            successActivityView.hideLoader();
            successActivityView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (successActivityView != null && response != null) {
            successActivityView.hideLoader();
            if (response instanceof InterviewTimeSlotsResponse) {
                InterviewTimeSlotsResponse interviewTimeSlotsResponse = (InterviewTimeSlotsResponse) response;
                successActivityView.setInterviewTimeSlotsResponse(interviewTimeSlotsResponse, jobId);
            } else if (response instanceof ScheduleInterviewResponse) {
                ScheduleInterviewResponse scheduleInterviewResponse = (ScheduleInterviewResponse) response;
                successActivityView.setScheduleInterviewResponse(scheduleInterviewResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        successActivityView = null;
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

}