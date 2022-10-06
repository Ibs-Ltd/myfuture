package com.myfutureapp.jobDetail.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.dashboard.model.UserProfileResponse;
import com.myfutureapp.jobDetail.model.ApplyOpportunityResponse;
import com.myfutureapp.jobDetail.model.EventApplyResponse;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.JobDetailResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.jobDetail.ui.JobDetailView;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;

import java.util.HashMap;

import okhttp3.Request;

public class JobDetailPresenter extends NetworkHandler {

    private JobDetailView jobDetailView;
    private final Context context;
    private String bookMarkType;
    private int position;


    public JobDetailPresenter(Context context, JobDetailView jobDetailView) {
        super(context, jobDetailView);
        this.jobDetailView = jobDetailView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (jobDetailView != null) {
            jobDetailView.hideLoader();
//            if (Helper.isContainValue(message)) {
            jobDetailView.showMessage("error occured");
//            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (jobDetailView != null) {
            jobDetailView.hideLoader();
            jobDetailView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (jobDetailView != null && response != null) {
            jobDetailView.hideLoader();
            if (response instanceof JobDetailResponse) {
                JobDetailResponse jobDetailResponse = (JobDetailResponse) response;
                jobDetailView.setOpportunityDetailResponse(jobDetailResponse);
            } else if (response instanceof InterviewTimeSlotsResponse) {
                InterviewTimeSlotsResponse interviewTimeSlotsResponse = (InterviewTimeSlotsResponse) response;
                jobDetailView.setInterviewTimeSlotsResponse(interviewTimeSlotsResponse);
            } else if (response instanceof ApplyOpportunityResponse) {
                ApplyOpportunityResponse applyOpportunityResponse = (ApplyOpportunityResponse) response;
                jobDetailView.setApplyOpportunityResponse(applyOpportunityResponse);
            } else if (response instanceof ScheduleInterviewResponse) {
                ScheduleInterviewResponse scheduleInterviewResponse = (ScheduleInterviewResponse) response;
                jobDetailView.setScheduleInterviewResponse(scheduleInterviewResponse);
            } else if (response instanceof BookMarkResponse) {
                BookMarkResponse bookMarkResponse = (BookMarkResponse) response;
                jobDetailView.setBookMarkResponse(bookMarkResponse, bookMarkType, position);
            } else if (response instanceof UserProfileResponse) {
                UserProfileResponse userProfileResponse = (UserProfileResponse) response;
                jobDetailView.setUserProfileResponse(userProfileResponse);
            } else if (response instanceof EventApplyResponse) {
                EventApplyResponse eventApplyResponse = (EventApplyResponse) response;
                jobDetailView.setEventOnOpportunityResponse(eventApplyResponse);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        jobDetailView = null;
    }

    public void callOpportunityDetailApi(String id) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).opportunityDetail(headerMap, id, "2").enqueue(this);
    }

    public void callGetTimeSlotslApi(String id) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getInterviewTimeSlots(headerMap, id).enqueue(this);

    }

    public void callApplyOpportunity(String jobId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).applyOpportunity(headerMap, jobId, "1").enqueue(this);
    }

    public void callEventApplyOpportunity(String jobId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("opportunity_id", jobId);
        NetworkManager.getApi(context).applyEventEntering(headerMap, jsonObject).enqueue(this);
    }

    public void callScheduleInterviewApi(String interview_datetime, String jobId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("interview_datetime", interview_datetime);
        jsonObject.addProperty("opportunity_id", Integer.parseInt(jobId));
        NetworkManager.getApi(context).scheduleInterview(headerMap, jsonObject).enqueue(this);
    }

    public void callBookMarkApi(String bookMarkType, JobsForYouResponse.JobDetail jobDetail, int position) {
        this.bookMarkType = bookMarkType;
        this.position = position;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).callBookmarkOpportunity(headerMap, String.valueOf(jobDetail.id), bookMarkType).enqueue(this);
    }

    public void callUserProfileApi(String studentId) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getUserProfile(headerMap, studentId).enqueue(this);
    }
}