package com.myfutureapp.dashboard.home.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.myfutureapp.core.NetworkHandler;
import com.myfutureapp.core.NetworkManager;
import com.myfutureapp.core.model.BaseResponse;
import com.myfutureapp.dashboard.home.model.BookMarkResponse;
import com.myfutureapp.dashboard.home.model.JobTagsResponse;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.dashboard.home.model.OngoingApplicationsResponse;
import com.myfutureapp.dashboard.home.ui.HomeActivityView;
import com.myfutureapp.jobDetail.model.InterviewTimeSlotsResponse;
import com.myfutureapp.jobDetail.model.ScheduleInterviewResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.Helper;

import java.util.HashMap;

import okhttp3.Request;

public class HomePresenter extends NetworkHandler {

    private final Context context;
    private HomeActivityView homeActivityView;
    private String jobId;
    private String bookMarkType;
    private int position;

    public HomePresenter(Context context, HomeActivityView homeActivityView) {
        super(context, homeActivityView);
        this.homeActivityView = homeActivityView;
        this.context = context;
    }

    @Override
    public boolean handleFailure(Request request, Exception ex, String message) {
        if (homeActivityView != null) {
            homeActivityView.hideLoader();
            if (Helper.isContainValue(message)) {
                homeActivityView.showMessage(message);
            }
        }
        return super.handleFailure(request, ex, message);
    }

    @Override
    public boolean handleError(Request request, Error error) {
        if (homeActivityView != null) {
            homeActivityView.hideLoader();
            homeActivityView.showMessage(error.getMessage());
        }
        return super.handleError(request, error);
    }

    @Override
    public boolean handleResponse(Request request, BaseResponse response) {
        if (homeActivityView != null && response != null) {
            homeActivityView.hideLoader();
            if (response instanceof JobTagsResponse) {
                JobTagsResponse tagsResponse = (JobTagsResponse) response;
                homeActivityView.setTagsResponse(tagsResponse);
            } else if (response instanceof JobsForYouResponse) {
                JobsForYouResponse jobsForYouResponse = (JobsForYouResponse) response;
                homeActivityView.setJobsResponse(jobsForYouResponse);
            } else if (response instanceof OngoingApplicationsResponse) {
                OngoingApplicationsResponse ongoingApplicationsResponse = (OngoingApplicationsResponse) response;
                homeActivityView.setOngoingApplicationsResponse(ongoingApplicationsResponse);
            } else if (response instanceof InterviewTimeSlotsResponse) {
                InterviewTimeSlotsResponse interviewTimeSlotsResponse = (InterviewTimeSlotsResponse) response;
                homeActivityView.setInterviewTimeSlotsResponse(interviewTimeSlotsResponse, jobId);
            } else if (response instanceof ScheduleInterviewResponse) {
                ScheduleInterviewResponse scheduleInterviewResponse = (ScheduleInterviewResponse) response;
                homeActivityView.setScheduleInterviewResponse(scheduleInterviewResponse);
            } else if (response instanceof BookMarkResponse) {
                BookMarkResponse bookMarkResponse = (BookMarkResponse) response;
                homeActivityView.setBookMarkResponse(bookMarkResponse, bookMarkType, position);
            }

        }
        return true;
    }

    public void onDestroyedView() {
        homeActivityView = null;
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

    public void callGetTagsApi() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getTags(headerMap).enqueue(this);
    }

    public void callGetJobsApi(int page, String tag_id) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getJobsForYou(headerMap, 0, tag_id, page, "").enqueue(this);
    }

    public void callOngoingApplicationsApi() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).getOngoingApplications(headerMap, "1", "0").enqueue(this);
    }

    public void callBookMarkApi(String bookMarkType, JobsForYouResponse.JobDetail jobDetail, int position) {
        this.bookMarkType = bookMarkType;
        this.position = position;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + AppPreferences.getInstance(context).getPreferencesString(AppConstants.GUID));
        NetworkManager.getApi(context).callBookmarkOpportunity(headerMap, String.valueOf(jobDetail.id), bookMarkType).enqueue(this);
    }
}