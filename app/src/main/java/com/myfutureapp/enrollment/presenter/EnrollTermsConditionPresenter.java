package com.myfutureapp.enrollment.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.enrollment.model.EnrollUserResponse;
import com.myfutureapp.enrollment.ui.EnrollTermsConditionView;
import com.myfutureapp.jobDetail.model.ApplyOpportunityResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class EnrollTermsConditionPresenter extends NetworkHandler {

    private EnrollTermsConditionView enrollTermsConditionView;
    private final Context context;

    public EnrollTermsConditionPresenter(Context context, EnrollTermsConditionView enrollTermsConditionView) {
        super(context, enrollTermsConditionView);
        this.enrollTermsConditionView = enrollTermsConditionView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (enrollTermsConditionView != null) {
            enrollTermsConditionView.hideLoader();
            if (Helper.isContainValue(message)) {
                enrollTermsConditionView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (enrollTermsConditionView != null) {
            enrollTermsConditionView.hideLoader();
            enrollTermsConditionView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (enrollTermsConditionView != null && response != null) {
            enrollTermsConditionView.hideLoader();
            if (response instanceof EnrollUserResponse) {
                EnrollUserResponse enrollUserResponse = (EnrollUserResponse) response;
                enrollTermsConditionView.setEnrollStatusUpdateResponse(enrollUserResponse);
            } else if (response instanceof InterviewTimeSlotsResponse) {
                InterviewTimeSlotsResponse interviewTimeSlotsResponse = (InterviewTimeSlotsResponse) response;
                enrollTermsConditionView.setInterviewTimeSlotsResponse(interviewTimeSlotsResponse);
            } else if (response instanceof ApplyOpportunityResponse) {
                ApplyOpportunityResponse applyOpportunityResponse = (ApplyOpportunityResponse) response;
                enrollTermsConditionView.setApplyOpportunityResponse(applyOpportunityResponse);
            } else if (response instanceof ScheduleInterviewResponse) {
                ScheduleInterviewResponse scheduleInterviewResponse = (ScheduleInterviewResponse) response;
                enrollTermsConditionView.setScheduleInterviewResponse(scheduleInterviewResponse);
            }
        }
        return true;
    }

    public void onDestroyedView() {
        enrollTermsConditionView = null;
    }


    public void callEnrollStatusUpdateApi() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).enrollStatusUpdate(headerMap).enqueue(this);
    }

    public void callApplyOpportunity(String jobId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).applyOpportunity(headerMap, jobId, "1").enqueue(this);
    }

    public void callGetTimeSlotslApi(String id) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getInterviewTimeSlots(headerMap, id).enqueue(this);

    }

    public void callScheduleInterviewApi(String interview_datetime, String jobId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("interview_datetime", interview_datetime);
        jsonObject.addProperty("opportunity_id", Integer.parseInt(jobId));
        NetworkManager.getApi(context).scheduleInterview(headerMap, jsonObject).enqueue(this);
    }


}